package com.example.vccorp.service;

import com.example.vccorp.dto.request.user.BrandUserRequest;
import com.example.vccorp.dto.request.user.EditorialUserRequest;
import com.example.vccorp.dto.request.user.UserRequest;
import com.example.vccorp.dto.response.user.UserResponse;
import com.example.vccorp.enums.ErrorCode;
import com.example.vccorp.exception.AppException;
import com.example.vccorp.mapper.BrandUserMapper;
import com.example.vccorp.mapper.EditorialUserMapper;
import com.example.vccorp.mapper.UserMapper;
import com.example.vccorp.model.BrandUser;
import com.example.vccorp.model.EditorialUser;
import com.example.vccorp.model.User;
import com.example.vccorp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.toResponse(user);
    }

    public List<UserResponse> getUsersByRole(Integer role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(UserMapper::toResponse).collect(Collectors.toList());
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = UserMapper.toEntity(request);

        if (request.getRole() == 1) {
            BrandUserRequest brandUserRequest = BrandUserRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .companyName(request.getCompanyName())
                .build();
            user = BrandUserMapper.toEntity(brandUserRequest);
        } else if (request.getRole() == 2) {
            EditorialUserRequest editorialUserRequest = EditorialUserRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .build();
            user = EditorialUserMapper.toEntity(editorialUserRequest);
        }

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (existingUser instanceof BrandUser brandUser && request.getCompanyName() != null) {
            brandUser.setCompanyName(request.getCompanyName());
        } else if (existingUser instanceof EditorialUser editorialUser && request.getDepartment() != null) {
            editorialUser.setDepartment(request.getDepartment());
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
