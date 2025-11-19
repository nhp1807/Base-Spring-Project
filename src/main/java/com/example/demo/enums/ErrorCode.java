package com.example.demo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    // General
    INVALID_REQUEST(1000, "Request không hợp lệ."),
    VALIDATION_ERROR(1001, "Dữ liệu không hợp lệ."),
    UNAUTHORIZED(1002, "Không có quyền truy cập."),
    FORBIDDEN(1003, "Bị từ chối truy cập."),
    INTERNAL_ERROR(1004, "Lỗi hệ thống."),

    // User & Auth
    USER_NOT_FOUND(1100, "User không tồn tại."),
    USER_ALREADY_EXISTS(1101, "User đã tồn tại."),
    REFRESH_TOKEN_NOT_FOUND(1102, "Refresh token không tồn tại."),
    EMAIL_ALREADY_EXISTS(1103, "Email đã tồn tại."),
    CANT_UPDATE_EMAIL(1104, "Không thể cập nhật email."),
    ;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
