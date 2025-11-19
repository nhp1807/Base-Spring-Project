package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.enums.ErrorCode;
import com.example.demo.enums.Role;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.specification.UserSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public PageResponse<UserResponse> findAll(String firstName, String lastName, String email, String phoneNumber, String telegramUsername, String lotusUsername, Role role, UserStatus status, String sortBy, String sortOrder, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<User> specification = UserSpecification.filter(firstName, lastName, email, phoneNumber, telegramUsername, lotusUsername, role, status, sortBy, sortOrder);
        Page<User> users = userRepository.findAll(specification, pageable);
        List<UserResponse> userResponses = users.getContent().stream().map(this::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .content(userResponses)
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return toUserResponse(user);
    }

    public void create(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .telegramUsername(request.getTelegramUsername())
                .lotusUsername(request.getLotusUsername())
                .role(request.getRole())
                .status(request.getStatus())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    public void update(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!request.getEmail().equals(user.getEmail())) {
            throw new AppException(ErrorCode.CANT_UPDATE_EMAIL);
        }

        user.setFirstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName());
        user.setLastName(request.getLastName() != null ? request.getLastName() : user.getLastName());
        user.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : user.getPhoneNumber());
        user.setTelegramUsername(request.getTelegramUsername() != null ? request.getTelegramUsername() : user.getTelegramUsername());
        user.setLotusUsername(request.getLotusUsername() != null ? request.getLotusUsername() : user.getLotusUsername());
        user.setRole(request.getRole() != null ? request.getRole() : user.getRole());
        user.setStatus(request.getStatus() != null ? request.getStatus() : user.getStatus());

        userRepository.save(user);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .telegramUsername(user.getTelegramUsername())
                .lotusUsername(user.getLotusUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
