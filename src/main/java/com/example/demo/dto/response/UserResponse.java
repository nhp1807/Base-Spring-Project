package com.example.demo.dto.response;

import java.time.LocalDateTime;

import com.example.demo.enums.Role;
import com.example.demo.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String telegramUsername;
    private String lotusUsername;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
