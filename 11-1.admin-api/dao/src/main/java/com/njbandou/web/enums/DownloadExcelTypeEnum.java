package com.njbandou.web.enums;

public enum DownloadExcelTypeEnum {
    // 0订单发货模板 1订单退款 2纸质发票发货模板 3电子发票发货模板
    ORDER_SEND(0), ORDER_REFUND(1), INVOICE_PAPER(2), INVOICE_EMAIL(3);

    DownloadExcelTypeEnum(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}