package com.lawProject.SSL.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";

    private Integer statusCode;
    private String status;
    private T data;
    private String message;


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, data);
    }

    public static <T> ApiResponse<T> successWithNoContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null);
    }

    public static ApiResponse error(int errorCode, String message) {
        HashMap<String, String> empty = new HashMap<>();
        return new ApiResponse<>(errorCode, empty, message);
    }

    public static <T> ApiResponse<T> error(int errorCode, T data, String message) {
        return new ApiResponse<>(errorCode, data, message);
    }

    private ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    private ApiResponse(int status, T data) {
        this.statusCode = null;
        this.data = data;
    }

    private ApiResponse(int status, T data, String message) {
        this.statusCode = status;
        this.data = data;
        this.message = message;
    }
}
