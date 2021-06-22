package com.njbandou.web.exception.enums;

public enum ErrorEnums {
    TOKEN_MISS(302, "登录失效"),
    NOT_FOUND(404, "错误路径"),
    MULTIPLE_SUB(101, "请勿重复提交"),
    SERVER_ERROR(500, "系统内部错误，请联系管理员"),
    ILLEGAL_REQUEST(401, "非法请求");


    private int code;
    private String msg;

    ErrorEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
