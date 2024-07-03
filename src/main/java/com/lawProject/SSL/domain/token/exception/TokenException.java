package com.lawProject.SSL.domain.token.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class TokenException extends BusinessException {

    public TokenException(ErrorCode code) {
        super(code);
    }
}
