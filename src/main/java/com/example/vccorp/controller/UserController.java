package com.example.vccorp.controller;

import com.example.vccorp.dto.request.user.UserRequest;
import com.example.vccorp.dto.response.APIResponse;
import com.example.vccorp.dto.response.user.UserResponse;
import com.example.vccorp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> findById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        APIResponse<UserResponse> apiResponse = APIResponse.<UserResponse>builder()
                .data(userResponse)
                .message("Lấy thông tin user thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<APIResponse<List<UserResponse>>> getUsersByRole(@PathVariable Integer role) {
        List<UserResponse> userResponses = userService.getUsersByRole(role);
        APIResponse<List<UserResponse>> apiResponse = APIResponse.<List<UserResponse>>builder()
                .data(userResponses)
                .message("Lấy thông tin user thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<APIResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        APIResponse<UserResponse> apiResponse = APIResponse.<UserResponse>builder()
                .data(userResponse)
                .message("Tạo user thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        APIResponse<UserResponse> apiResponse = APIResponse.<UserResponse>builder()
                .data(userResponse)
                .message("Cập nhật user thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .message("Xóa user thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
