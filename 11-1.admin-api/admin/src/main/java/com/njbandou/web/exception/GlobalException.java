package com.njbandou.web.exception;

import java.io.Serializable;

public class GlobalException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -4429264209412248900L;

    private int code;

    private Exception e;

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, int code, Exception e) {
        super(message);
        this.code = code;
        this.e = e;
    }

    public GlobalException(String message, int code) {
        super(message);
        this.code = code;
    }


}
