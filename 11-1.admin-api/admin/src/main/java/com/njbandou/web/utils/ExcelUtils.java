package com.njbandou.web.utils;

import com.njbandou.web.constant.UtilConstant;
import com.njbandou.web.enums.ExcelTypeEnum;
import com.njbandou.web.exception.ServerException;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ExcelUtils {
    /**
     * 获取上传根路径
     *
     * @param request 请求信息
     * @return string
     */
    public static String getRootPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath(UtilConstant.UPLOAD_PATH);
    }

    public static Workbook getWorkbook(String excelType) {
        try {
            return WorkbookFactory.create(!excelType.equals(ExcelTypeEnum.XLS.getVal()));
        } catch (IOException e) {
            throw new ServerException("创建excel文件失败", e);
        }
    }

    /**
     * 创建表
     *
     * @param wb         目标文件
     * @param sheetName  表名
     * @param sheetTitle 表头
     * @return sheet
     */
    public static Sheet createSheet(Workbook wb, String sheetName, String[] sheetTitle) {
        Sheet sheet = wb.createSheet(sheetName);

        Row row = sheet.createRow(0);

        for (int i = 0; i < sheetTitle.length; i++) {
            row.createCell(i).setCellValue(sheetTitle[i]);
        }

        return sheet;
    }


    /**
     * 创建单元格.
     *
     * @param row    行
     * @param column 列
     * @param value  值
     */
    public static void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }
}
