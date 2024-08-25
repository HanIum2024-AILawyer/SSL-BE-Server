package com.lawProject.SSL.domain.lawyer.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class LawyerException extends BusinessException {
    public LawyerException(ErrorCode code) {
        super(code);
    }
}

