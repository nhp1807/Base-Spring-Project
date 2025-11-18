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

    // Brand
    BRAND_NOT_FOUND(1200, "Brand không tồn tại."),
    BRAND_ALREADY_EXISTS(1201, "Brand đã tồn tại."),

    // Channel
    CHANNEL_NOT_FOUND(1210, "Channel không tồn tại."),
    CHANNEL_ALREADY_EXISTS(1211, "Channel đã tồn tại."),

    // Format
    FORMAT_NOT_FOUND(1220, "Format không tồn tại."),
    FORMAT_ALREADY_EXISTS(1221, "Format đã tồn tại."),

    // Brief
    BRIEF_NOT_FOUND(1230, "Brief không tồn tại."),
    BRIEF_ALREADY_EXISTS(1231, "Brief đã tồn tại."),
    BRIEF_CREATED_BY_NOT_ACCOUNT(1232, "Người tạo Brief phải là Account."),

    // Content Pillar & components
    CONTENT_PILLAR_NOT_FOUND(1240, "Content Pillar không tồn tại."),
    CONTENT_PILLAR_ALREADY_EXISTS(1241, "Content Pillar đã tồn tại."),
    CONTENT_PILLAR_LINE_NOT_FOUND(1242, "Content Pillar Line không tồn tại."),
    CONTENT_PILLAR_LINE_ALREADY_EXISTS(1243, "Content Pillar Line đã tồn tại."),
    CONTENT_FORMAT_PLAN_NOT_FOUND(1244, "Content Format Plan không tồn tại."),
    CONTENT_FORMAT_PLAN_ALREADY_EXISTS(1245, "Content Format Plan đã tồn tại."),

    // Campaign
    CAMPAIGN_NOT_FOUND(1250, "Campaign không tồn tại."),
    CAMPAIGN_ALREADY_EXISTS(1251, "Campaign đã tồn tại."),

    // Contract
    CONTRACT_NOT_FOUND(1260, "Contract không tồn tại."),
    CONTRACT_ALREADY_EXISTS(1261, "Contract đã tồn tại."),

    // Document
    DOCUMENT_NOT_FOUND(1270, "Document không tồn tại."),
    DOCUMENT_ALREADY_EXISTS(1271, "Document đã tồn tại."),

    // Notification & relations
    NOTIFICATION_NOT_FOUND(1280, "Notification không tồn tại."),
    NOTIFICATION_ALREADY_EXISTS(1281, "Notification đã tồn tại."),
    NOTIFICATION_RECIPIENT_NOT_FOUND(1282, "Notification Recipient không tồn tại."),
    NOTIFICATION_SENDING_METHOD_NOT_FOUND(1283, "Notification Sending Method không tồn tại."),

    // Requests & Work
    REQUEST_NOT_FOUND(1290, "Request không tồn tại."),
    REQUEST_ALREADY_EXISTS(1291, "Request đã tồn tại."),
    REQUEST_CHANNEL_NOT_FOUND(1292, "Request Channel không tồn tại."),
    WORK_ASSIGNMENT_NOT_FOUND(1293, "Work Assignment không tồn tại."),
    WRITING_TASK_NOT_FOUND(1294, "Writing Task không tồn tại."),
    ARTICLE_SUBMISSION_NOT_FOUND(1295, "Article Submission không tồn tại."),

    // BTV Lead domain
    BTV_RESPONSE_NOT_FOUND(1300, "BTV Response không tồn tại."),
    BTV_RESPONSE_DETAIL_NOT_FOUND(1301, "BTV Response Detail không tồn tại."),
    BTV_TEAM_NOT_FOUND(1302, "BTV Team không tồn tại."),
    LEAD_CHANNEL_NOT_FOUND(1303, "Lead Channel không tồn tại."),
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
