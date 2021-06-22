package com.njbandou.web.exception;

import com.njbandou.web.exception.enums.ErrorEnums;

import java.io.Serializable;

public class TokenMissException extends GlobalException implements Serializable {
    private static final long serialVersionUID = 3708214338420089274L;

    public TokenMissException() {
        super(ErrorEnums.TOKEN_MISS.getMsg(), ErrorEnums.TOKEN_MISS.getCode());
    }
}
