package com.example.vccorp.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST(1001, "Request không hợp lệ."),
    EMAIL_INVALID(1002, "Email không hợp lệ."),
    USER_NOT_FOUND(1003, "User không tồn tại."),
    USER_ALREADY_EXISTS(1004, "User đã tồn tại."),
    PRODUCT_NOT_FOUND(1005, "Product không tồn tại."),
    PRODUCT_ALREADY_EXISTS(1006, "Product đã tồn tại."),
    CATEGORY_NOT_FOUND(1007, "Category không tồn tại."),
    CATEGORY_ALREADY_EXISTS(1008, "Category đã tồn tại."),
    BRAND_USER_NOT_FOUND(1009, "BrandUser không tồn tại."),
    BRAND_USER_ALREADY_EXISTS(1010, "BrandUser đã tồn tại."),
    EDITORIAL_USER_NOT_FOUND(1011, "EditorialUser không tồn tại."),
    EDITORIAL_USER_ALREADY_EXISTS(1012, "EditorialUser đã tồn tại."),
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
