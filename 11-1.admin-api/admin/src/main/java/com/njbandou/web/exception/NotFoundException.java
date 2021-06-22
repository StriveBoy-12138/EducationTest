package com.njbandou.web.exception;

import com.njbandou.web.exception.enums.ErrorEnums;

import java.io.Serializable;

public class NotFoundException extends GlobalException implements Serializable {
    private static final long serialVersionUID = 9075881930891449811L;

    public NotFoundException() {
        super(ErrorEnums.NOT_FOUND.getMsg(), ErrorEnums.NOT_FOUND.getCode());
    }
}
