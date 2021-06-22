package com.njbandou.web.utils;

import com.njbandou.web.exception.ServerException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DownloadFileUtil {

    public static void downloadFile(String fileName, String filePath, HttpServletResponse response, HttpServletRequest request, ResourceLoader resourceLoader) {
        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);

            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("charset", "utf-8");
            response.addHeader("Pragma", "no-cache");
            String encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeName + "\"; filename*=utf-8''" + encodeName);
            inputStream = classPathResource.getInputStream();
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ServerException("下载文件失败", e);
        } finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                System.gc();
            } catch (Exception e) {
                throw new ServerException("释放资源失败", e);
            }
        }
    }
}
