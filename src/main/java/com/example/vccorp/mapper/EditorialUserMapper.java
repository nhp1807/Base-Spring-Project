package com.example.vccorp.mapper;

import com.example.vccorp.dto.request.user.EditorialUserRequest;
import com.example.vccorp.dto.response.user.EditorialUserResponse;
import com.example.vccorp.model.EditorialUser;

public class EditorialUserMapper {
    public static EditorialUser toEntity(EditorialUserRequest request) {
        return EditorialUser.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .department(request.getDepartment() != null ? request.getDepartment().trim() : null)
                .build();
    }
    
    public static EditorialUserResponse toResponse(EditorialUser editorialUser) {
        return EditorialUserResponse.builder()
                .id(editorialUser.getId())
                .name(editorialUser.getName() != null ? editorialUser.getName().trim() : null)
                .email(editorialUser.getEmail() != null ? editorialUser.getEmail().trim() : null)
                .department(editorialUser.getDepartment() != null ? editorialUser.getDepartment().trim() : null)
                .build();
    }
}
