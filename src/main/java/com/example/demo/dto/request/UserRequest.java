package com.example.demo.dto.request;

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
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String telegramUsername;
    private String lotusUsername;
    private String password;
    private Role role;
    private UserStatus status;
}
