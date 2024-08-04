package com.lawProject.SSL.domain.inquery.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class InQueryException extends BusinessException {
    public InQueryException(ErrorCode code) {
        super(code);
    }
}

