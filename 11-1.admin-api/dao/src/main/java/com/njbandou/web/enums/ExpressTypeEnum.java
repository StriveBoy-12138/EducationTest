package com.njbandou.web.enums;

public enum ExpressTypeEnum {
    // 1圆通 2顺丰
    YUAN_TONG("圆通", 1), SHUN_FENG("顺丰", 2);

    ExpressTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private int value;

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
