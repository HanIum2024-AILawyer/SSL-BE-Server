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
    _UNAUTHORIZED(UNAUTHORIZED,"401", "인증이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, "403", "금지된 요청입니다."),

    /**
     * Auth
     */
    USER_ALREADY_EXISTS(BAD_REQUEST, "400", "이미 가입된 유저입니다."),
    NEED_TO_JOIN(BAD_REQUEST, "400", "회원가입이 필요한 요청입니다."),
    USER_MISMATCH(UNAUTHORIZED, "401", "회원 정보가 불일치합니다."),

    /**
     * User
     */
    NICKNAME_DUPLICATION(CONFLICT, "409", "중복되는 닉네임입니다."),
    USER_NOT_FOUND(NOT_FOUND, "404", "존재하지 않는 회원입니다."),
    INVALID_INPUT_ID_PASSWORD(BAD_REQUEST, "400", "Id 또는 Password가 일치하지 않습니다."),
    SOCIAL_TYPE_ERROR(BAD_REQUEST,"400","소셜 타입 검증에 실패했습니다."),

    /**
     * Token
     */
    INVALID_TOKEN(UNAUTHORIZED, "401", "유효하지 않은 토큰입니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "401", "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "401", "유효하지 않은 리프레쉬 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "404", "해당 유저 ID의 리프레쉬 토큰이 없습니다.");

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