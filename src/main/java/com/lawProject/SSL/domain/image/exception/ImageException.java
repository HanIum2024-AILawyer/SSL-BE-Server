package com.lawProject.SSL.domain.image.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class ImageException extends BusinessException {
    public ImageException(ErrorCode code) {
        super(code);
    }
}

