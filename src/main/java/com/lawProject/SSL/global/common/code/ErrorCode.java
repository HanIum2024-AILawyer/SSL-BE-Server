package com.lawProject.SSL.global.common.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lawProject.SSL.global.common.dto.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode{

    /**
     * 전역 에러
     */
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR,"500", "서버에서 요청을 처리 하는 동안 오류가 발생했습니다."),
    _BAD_REQUEST(BAD_REQUEST,"400", "입력 값이 잘못된 요청입니다."),
    _INVALID_TYPE_VALUE(BAD_REQUEST, "400", "입력 타입이 잘못된 요청입니다."),
    _INVALID_INPUT_VALUE(BAD_REQUEST, "400", "입력 값이 잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED,"401", "인증이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, "403", "금지된 요청입니다."),

    /**
     * Auth
     */
    USER_ALREADY_EXISTS(BAD_REQUEST, "A001", "이미 가입된 유저입니다."),
    NEED_TO_JOIN(BAD_REQUEST, "A002", "회원가입이 필요한 요청입니다."),
    USER_MISMATCH(UNAUTHORIZED, "A003", "회원 정보가 불일치합니다."),

    /**
     * User
     */
    NICKNAME_DUPLICATION(CONFLICT, "U001", "중복되는 닉네임입니다."),
    USER_NOT_FOUND(NOT_FOUND, "U002", "존재하지 않는 회원입니다."),
    INVALID_INPUT_ID_PASSWORD(BAD_REQUEST, "U003", "Id 또는 Password가 일치하지 않습니다."),
    SOCIAL_TYPE_ERROR(BAD_REQUEST,"U004","소셜 타입 검증에 실패했습니다."),

    /**
     * Token
     */
    INVALID_TOKEN(UNAUTHORIZED, "T001", "유효하지 않은 토큰입니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "T002", "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "T003", "유효하지 않은 리프레쉬 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "T004", "해당 유저 ID의 리프레쉬 토큰이 없습니다."),
    ACCESS_TOKEN_NOT_FOUND(NOT_FOUND, "T005", "AccessToken 토큰이 없습니다."),

    /**
     * File
     */
    FILE_NOT_UPLOADED(BAD_REQUEST, "F001", "파일이 업로드되지 않았습니다."),
    FILE_EMPTY(BAD_REQUEST, "F002", "빈 파일입니다."),
    FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "F003", "파일 업로드에 실패했습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "F004", "파일을 찾을 수 없습니다."),
    INVALID_FILE_TYPE(UNAUTHORIZED, "F005", "파일 형식이 맞지 않습니다."),
    FILE_STORGE_ERROR(INSUFFICIENT_STORAGE, "F005", "파일 저장에 실패했습니다"),

    /**
     * ChatRoom
     */
    ROOM_NOT_FOUND(NOT_FOUND, "R001", "존재하지 않는 채팅방입니다."),

    /**
     * Notification
     */
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "N001", "존재하지 않는 문의글입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}