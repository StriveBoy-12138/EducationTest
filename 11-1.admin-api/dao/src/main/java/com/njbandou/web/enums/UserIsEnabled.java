package com.njbandou.web.enums;

public enum UserIsEnabled {
    // 启用0，禁用1
    ENABLED(0), UNABLED(1);

    private int value;

    public int getValue() {
        return value;
    }

    UserIsEnabled(int value) {
        this.value = value;
    }
}
