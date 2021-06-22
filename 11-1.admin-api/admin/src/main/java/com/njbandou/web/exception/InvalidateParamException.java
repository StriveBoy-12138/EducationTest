package com.njbandou.web.exception;

import org.springframework.validation.BindingResult;

import java.io.Serializable;

public class InvalidateParamException extends GlobalException implements Serializable {

    private static final long serialVersionUID = 4790714808740671158L;

    public InvalidateParamException(String message) {
        super(message);
    }

    public InvalidateParamException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors().get(0).getDefaultMessage(), 1);
    }

    public InvalidateParamException(String message, int code, Exception e) {
        super(message, 1, e);
    }

    public InvalidateParamException(String message, int code) {
        super(message, 1);
    }
}
