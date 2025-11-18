package com.example.vccorp.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;
    @Email(message = "EMAIL_INVALID")
    private String email;
    private Integer role;
    private String companyName;
    private String department;
}
