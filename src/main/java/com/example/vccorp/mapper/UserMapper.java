package com.example.vccorp.mapper;

import com.example.vccorp.dto.request.user.UserRequest;
import com.example.vccorp.dto.response.user.UserResponse;
import com.example.vccorp.model.BrandUser;
import com.example.vccorp.model.EditorialUser;
import com.example.vccorp.model.User;

public class UserMapper {
    public static User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        User user = User.builder()
                .name(request.getName() != null ? request.getName().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .build();
        
        if (user instanceof BrandUser brandUser) {
            brandUser.setCompanyName(request.getCompanyName() != null ? request.getCompanyName().trim() : null);
        } else if (user instanceof EditorialUser editorialUser) {
            editorialUser.setDepartment(request.getDepartment() != null ? request.getDepartment().trim() : null);
        }
        return user;
    }

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName().trim() : null)
                .email(user.getEmail() != null ? user.getEmail().trim() : null)
                .role(user.getRole() != null ? user.getRole() : null)
                .build();
        if (user instanceof BrandUser brandUser) {
            userResponse.setCompanyName(brandUser.getCompanyName() != null ? brandUser.getCompanyName().trim() : null);
        } else if (user instanceof EditorialUser editorialUser) {
            userResponse.setDepartment(editorialUser.getDepartment() != null ? editorialUser.getDepartment().trim() : null);
        }
        return userResponse;
    }
}
