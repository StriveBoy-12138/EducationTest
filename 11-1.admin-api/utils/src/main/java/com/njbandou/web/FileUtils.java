package com.njbandou.web;

import java.io.File;

public class FileUtils {
    public FileUtils() {
    }

    // 判断路径是否存在，不存在则创建
    public static void checkPath(String path) {
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
    }
}
