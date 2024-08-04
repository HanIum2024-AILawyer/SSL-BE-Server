package com.lawProject.SSL.domain.notification.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class NotificationException extends BusinessException {
    public NotificationException(ErrorCode code) {
        super(code);
    }
}

