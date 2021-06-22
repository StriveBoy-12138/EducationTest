package com.njbandou.web.utils;

import com.njbandou.web.constant.UtilConstant;

public class ImgUrlUtil {
    private ImgUrlUtil() {
    }


    /**
     * 生成完整多图字符串
     *
     * @param infoImg 原多图字符串
     * @return result
     */
    public static String generateFullInfoImg(String infoImg) {
        if (!infoImg.equals("")) {
            infoImg = UtilConstant.OSS_URL + infoImg;
            infoImg = infoImg.replaceAll(",", "," + UtilConstant.OSS_URL);
        }
        return infoImg;
    }

    /**
     * 生成精简多图字符串
     *
     * @param infoImg 原多图字符串
     * @return result
     */
    public static String generateLessInfoImg(String infoImg) {
        String result = "";
        if (!infoImg.equals("")) {
            StringBuilder resultBuilder = new StringBuilder();
            String[] infoArr = infoImg.split(",");
            for (String str : infoArr) {
                resultBuilder.append(",").append(generateLessImg(str));
            }

            String regex = "^,*|,*$";

            result = resultBuilder.toString();
            result = result.replaceAll(regex, "");
        }

        return result;
    }

    /**
     * 生成精简图片字符串
     *
     * @param img 原图片字符串
     * @return result
     */
    public static String generateLessImg(String img) {
        String[] infoStrArr = img.split(UtilConstant.OSS_URL);
        return infoStrArr.length > 1 ? infoStrArr[1] : "";
    }

    /**
     * 生成完整图片字符串
     *
     * @param img 原图片字符串
     * @return result
     */
    public static String generateFullImg(String img) {
        return img.equals("") ? "" : UtilConstant.OSS_URL + img;
    }
}
