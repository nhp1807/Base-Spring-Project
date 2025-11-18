package com.example.vccorp.mapper;

import com.example.vccorp.dto.request.user.BrandUserRequest;
import com.example.vccorp.dto.response.user.BrandUserResponse;
import com.example.vccorp.model.BrandUser;

public class BrandUserMapper {
    public static BrandUser toEntity(BrandUserRequest request) {
        return BrandUser.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .companyName(request.getCompanyName() != null ? request.getCompanyName().trim() : null)
                .build();
    }

    public static BrandUserResponse toResponse(BrandUser brandUser) {
        return BrandUserResponse.builder()
                .id(brandUser.getId())
                .name(brandUser.getName() != null ? brandUser.getName().trim() : null)
                .email(brandUser.getEmail() != null ? brandUser.getEmail().trim() : null)
                .companyName(brandUser.getCompanyName() != null ? brandUser.getCompanyName().trim() : null)
                .build();
    }
}
