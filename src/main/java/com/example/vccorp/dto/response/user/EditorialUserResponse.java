package com.example.vccorp.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EditorialUserResponse extends UserResponse {
    private String department;
}
