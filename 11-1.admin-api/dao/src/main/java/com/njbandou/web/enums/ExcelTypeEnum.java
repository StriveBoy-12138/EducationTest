package com.njbandou.web.enums;

public enum ExcelTypeEnum {
    // xls, xlsx
    XLS(".xls"), XLSX(".xlsx");
    private String val;

    ExcelTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public ExcelTypeEnum setVal(String val) {
        this.val = val;
        return this;
    }
}
