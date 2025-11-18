package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
    private UserData user;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private Long userId;
        private String role;
        private String email;
        private String fullName;
    }
}
