package com.lawProject.SSL.global.jwt.exception;

import com.lawProject.SSL.global.error.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class TokenException extends BusinessException {

    public TokenException(ErrorCode code) {
        super(code);
    }
}
