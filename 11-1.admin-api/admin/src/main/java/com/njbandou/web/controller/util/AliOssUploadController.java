package com.njbandou.web.controller.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.njbandou.web.config.OssConfig;
import com.njbandou.web.redis.RedisConstant;
import com.njbandou.web.redis.RedisUtil;
import io.swagger.annotations.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/token")
public class AliOssUploadController {
    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private RedisUtil redisUtil;


    @ResponseBody
    @GetMapping(value = "/aliOssToken")
    @ApiOperation(value = "阿里云对象存储登录验证，从redis拿OSS_TOKEN,若已登录有token则直接返回，无则新申请。",
            notes = "返回值字段accessid-String\n,policy-String\n,signature-String\n,dir-String\n,host-String,expire-String\n," +
                    "callback-String{callbackUrl-String,callbackBody-固定格式String，callbackBodyType-appxxxform格式}")
    @ApiImplicitParam(name = "type",value = "用户上传文件时指定的前缀（文件夹）",paramType = "query",dataType = "Integer")
    public Map aliOssToken(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam Integer type) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessId(), ossConfig.getAccessKey());
        // 用户上传文件时指定的前缀（文件夹）。
        String dir = "";
        switch (type){
            case 0:
                dir = "avatar/";
                break;
            case 1:
                dir = "icon/";
                break;
            case 2:
                dir = "pictures/";
                break;
            case 3:
                dir = "other/";
                break;
        }
        try {
            Map<String, Object> respMap = redisUtil.get(RedisConstant.OSS_TOKEN, LinkedHashMap.class);
            //已经登录过了,直接返回
            if (respMap != null && respMap.get("dir").equals(dir)) {
                return respMap;
            }
            //新的map填充
            respMap = new LinkedHashMap<>();

            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);

            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);


            respMap.put("accessid", ossConfig.getAccessId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", ossConfig.getHost());
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            JSONObject jsonCallback = new JSONObject();
            jsonCallback.put("callbackUrl", ossConfig.getCallbackUrl());
            jsonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&width=${imageInfo.width}&mimeType=${mimeType}&height=${imageInfo.height}");
            jsonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");

            String base64CallbackBody = BinaryUtil.toBase64String(jsonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);

            redisUtil.set(RedisConstant.OSS_TOKEN, respMap, expireTime);

            return respMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @PostMapping(value = "/callback")
    @ApiOperation(value = "阿里云对象存储方回调接口，验证入参ossCallbackBody后服务器响应结果",notes = "response的header填充Content-Length属性\n," +
            "callback-回调方法名称，url\n,location\n,status{ok-成功，err-失败}")
    @ApiImplicitParam(name = "ossCallbackBody",value = "格式如下-filename=${object}&size=${size}&width=${imageInfo.width}&mimeType=${mimeType}&height=${imageInfo.height}")
    protected void callback(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody String ossCallbackBody
    ) throws IOException {
        boolean ret = VerifyOSSCallbackRequest(request, ossCallbackBody);

        Map<String, String[]> paramsMap = request.getParameterMap();
        JSONObject jsonObject = new JSONObject();

        paramsMap.forEach((k, v) -> jsonObject.put(k, v[0]));

        jsonObject.put("url", ossConfig.getHost() + "/" + jsonObject.get("filename"));
        jsonObject.put("location", ossConfig.getHost() + "/" + jsonObject.get("filename"));

        if (ret) {
            jsonObject.put("status", "ok");
            response(request, response, jsonObject.toString(), HttpServletResponse.SC_OK);
        } else {
            jsonObject.put("status", "err");
            response(request, response, jsonObject.toString(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    /**
     * 获取Post消息体
     *
     * @param is
     * @param contentLen
     * @return
     */
    public String GetPostBody(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        // Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return new String(message);
            } catch (IOException e) {
            }
        }
        return "";
    }

    /**
     * 验证RSA
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(sign);
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取public key
     *
     * @param url
     * @return
     */
    @SuppressWarnings({"finally"})
    public String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            @SuppressWarnings("resource")
            HttpClient client = HttpClientBuilder.create().build();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            content = sb.toString();
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return content;
        }
    }

    /**
     * 验证上传回调的Request
     *
     * @param request
     * @param ossCallbackBody
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    protected boolean VerifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody)
            throws NumberFormatException, IOException {
        boolean ret = false;
        String autorizationInput = new String(request.getHeader("Authorization"));
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput);
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);
        String pubKeyAddr = new String(pubKey);
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
                && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            System.out.println("pub key addr must be oss addrss");
            return false;
        }
        String retString = executeGet(pubKeyAddr);
        retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
        retString = retString.replace("-----END PUBLIC KEY-----", "");
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
        String authStr = decodeUri;
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        authStr += "\n" + ossCallbackBody;
        ret = doCheck(authStr, authorization, retString);
        return ret;
    }

    /**
     * 服务器响应结果
     *
     * @param request
     * @param response
     * @param results
     * @param status
     * @throws IOException
     */
    private void response(HttpServletRequest request, HttpServletResponse response, String results, int status)
            throws IOException {
        String callbackFunName = request.getParameter("callback");
        response.addHeader("Content-Length", String.valueOf(results.length()));
        if (callbackFunName == null || callbackFunName.equalsIgnoreCase("")) {
            response.getWriter().println(results);
        } else {
            response.getWriter().println(callbackFunName + "( " + results + " )");
        }

        response.setStatus(status);
        response.flushBuffer();
    }

    /**
     * 服务器响应结果
     */
    private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
        String callbackFunName = request.getParameter("callback");
        if (callbackFunName == null || callbackFunName.equalsIgnoreCase("")) {
            response.getWriter().println(results);
        } else {
            response.getWriter().println(callbackFunName + "( " + results + " )");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }
}
