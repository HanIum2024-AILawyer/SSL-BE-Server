package com.lawProject.SSL.domain.lawsuit.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class FileException extends BusinessException {
    public FileException(ErrorCode code) {
        super(code);
    }
}

