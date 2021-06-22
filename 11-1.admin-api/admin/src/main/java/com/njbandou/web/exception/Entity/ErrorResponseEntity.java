package com.njbandou.web.exception.Entity;

public class ErrorResponseEntity {
    private int code;
    private String message;
    private String msg;

    public int getCode() {
        return code;
    }

    public ErrorResponseEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponseEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseEntity(int code, String message) {
        this.code = code;
        this.message = message;
        this.msg = this.message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
