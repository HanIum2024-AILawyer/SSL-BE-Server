package com.lawProject.SSL.global.error.exception;

import com.lawProject.SSL.global.error.ErrorCode;

public class InvalidValueException extends BusinessException{

    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }

}