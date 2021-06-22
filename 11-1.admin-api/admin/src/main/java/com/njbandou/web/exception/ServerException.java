package com.njbandou.web.exception;

import java.io.Serializable;
import java.util.Map;

public class ServerException extends GlobalException implements Serializable {

    private static final long serialVersionUID = -2553174310974329505L;

    private Map data;

    public ServerException(String message) {
        super(message, -1);
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map getData() {
        return data;
    }

    public ServerException setData(Map data) {
        this.data = data;
        return this;
    }

    public ServerException(String message, Exception e) {
        super(message, -1, e);
        this.data = null;
    }


    public ServerException(String message, int code, Exception e) {
        super(message, code, e);
        this.data = null;
    }

    public ServerException(String message, int code, Exception e, Map data) {
        super(message, code, e);
        this.data = data;
    }

    public ServerException(String message, Map data, Exception e) {
        super(message, -1, e);
        this.data = data;
    }

}
