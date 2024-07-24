package com.lawProject.SSL.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lawProject.SSL.global.common.code.BaseCode;
import com.lawProject.SSL.global.common.dto.ReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    @JsonProperty("is_success")
    private final Boolean isSuccess;
    private final HttpStatus status;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;

    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(BaseCode code, T payload) {
        ReasonDto reasonHttpStatus = code.getReasonHttpStatus();
        ApiResponse<T> response = new ApiResponse<>(true, reasonHttpStatus.getHttpStatus(), reasonHttpStatus.getCode(), reasonHttpStatus.getMessage(), payload);
        return ResponseEntity.status(reasonHttpStatus.getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(BaseCode code) {
        ReasonDto reasonHttpStatus = code.getReasonHttpStatus();
        ApiResponse<T> response = new ApiResponse<>(true, reasonHttpStatus.getHttpStatus(), reasonHttpStatus.getCode(), reasonHttpStatus.getMessage(), null);
        return ResponseEntity.status(reasonHttpStatus.getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> onFailure(BaseCode code) {
        ReasonDto reasonHttpStatus = code.getReasonHttpStatus();
        ApiResponse<T> response = new ApiResponse<>(false, reasonHttpStatus.getHttpStatus(), reasonHttpStatus.getCode(), reasonHttpStatus.getMessage(), null);
        return ResponseEntity.status(reasonHttpStatus.getHttpStatus()).body(response);
    }
}