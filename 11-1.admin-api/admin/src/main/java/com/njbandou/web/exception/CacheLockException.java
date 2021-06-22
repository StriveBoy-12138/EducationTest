package com.njbandou.web.exception;

import com.njbandou.web.exception.enums.ErrorEnums;

import java.io.Serializable;

public class CacheLockException extends GlobalException implements Serializable {
    private static final long serialVersionUID = -2553174310974329505L;

    public CacheLockException() {
        super(ErrorEnums.MULTIPLE_SUB.getMsg(), ErrorEnums.MULTIPLE_SUB.getCode());
    }
}