package com.lawProject.SSL.domain.chatroom.exception;

import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.error.exception.BusinessException;

public class ChatRoomException extends BusinessException {
    public ChatRoomException(ErrorCode code) {
        super(code);
    }
}