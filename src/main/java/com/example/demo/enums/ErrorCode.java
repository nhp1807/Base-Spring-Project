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
    EMAIL_ALREADY_EXISTS(1103, "Email đã tồn tại."),
    CANT_UPDATE_EMAIL(1104, "Không thể cập nhật email."),
    ROLE_NOT_FOUND(1105, "Role không tồn tại."),
    USER_STATUS_NOT_FOUND(1106, "Trạng thái user không tồn tại."),
    USER_NOT_SET_PASSWORD(1107, "Tài khoản này chưa thiết lập mật khẩu. Vui lòng đăng nhập bằng Google hoặc đặt mật khẩu mới."),

    // Refresh Token
    REFRESH_TOKEN_NOT_FOUND(1201, "Refresh token không tồn tại."),
    INVALID_REFRESH_TOKEN(1202, "Refresh token không hợp lệ.");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
