package com.njbandou.web.controller.util;

import com.njbandou.web.FileUtils;
import com.njbandou.web.constant.UtilConstant;
import com.njbandou.web.utils.ExcelUtils;
import io.swagger.annotations.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "file")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(UtilConstant.FULL_DATE);

    /**
     * excel文件上传接口
     *
     * @param request
     * @param response
     * @param file     上传的文件
     * @throws IOException
     */
    @PostMapping(value = "excelUpload")
    @ApiOperation(value = "excel文件上传接口",notes = "返回结果fileName-String，path-String，code-{1-上传失败，0-上传成功}，msg" +
            "-{文件为空！，上传失败，上传成功}，",
            httpMethod = "POST")
    @ApiImplicitParam(name = "file",value = "后缀为xls或xlsx格式的文件")
    public void excelUpload(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("file") MultipartFile file) throws IOException {
        response.setContentType(UtilConstant.CONTENT_TYPE);
        PrintWriter outWriter = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> returnMap = new HashMap<>();
        int code = 1;
        //判断文件是否为空
        if (!file.isEmpty()) {
            // 存储根路径
            String rootPath = ExcelUtils.getRootPath(request);
            // 存储Excel文件夹路径(/yyyymmdd/excel/)
            String folderPath = dateFormat.format(new Date()) + UtilConstant.EXCEL_FOLDER + UtilConstant.SEPA;
            // 完整路径
            String fullPath = rootPath + UtilConstant.SEPA + folderPath;
            // 获取后缀名
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            // 检查路径，无则创建
            FileUtils.checkPath(fullPath);
            // 文件名
            String fileName = System.currentTimeMillis() + ext;

            // 文件类型检查（只允许xls和xlsx格式）
            if (!UtilConstant.EXCEL_EXT.equals(ext) && !UtilConstant.EXCELX_EXT.equals(ext)) {
                returnMap.put(UtilConstant.MSG, "文件格式非法！请上传xls或xlsx格式文件");
                outWriter.write(mapper.writeValueAsString(returnMap));
                return;
            }

            // 本地保存
            File targetFile = new File(fullPath, fileName);
            logger.info("上传文件的名字：{}", file.getOriginalFilename());
            file.transferTo(targetFile);
            code = 0;

            returnMap.put(UtilConstant.FILE_NAME, fileName);
            returnMap.put(UtilConstant.PATH, folderPath);
        } else {
            returnMap.put(UtilConstant.MSG, "文件为空！");
        }

        returnMap.put(UtilConstant.CODE, code);
        returnMap.put(UtilConstant.MSG, code == 1 ? "上传失败" : "上传成功");

        outWriter.write(mapper.writeValueAsString(returnMap));
    }

}
