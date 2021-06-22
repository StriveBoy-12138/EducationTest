package com.njbandou.web.enums;

/**
 * @Dscription: 删除标识枚举
 */
public enum DeleteFlagEnum {
    // 0未删除 1已删除
    NOT_DELETE(0), HAD_DELETED(1);

    DeleteFlagEnum(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
