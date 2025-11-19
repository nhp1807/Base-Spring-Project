package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.APIResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.enums.Role;
import com.example.demo.enums.UserStatus;
import com.example.demo.service.UserService;
import com.example.demo.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Object>> findAll(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phoneNumber,
        @RequestParam(required = false) String telegramUsername,
        @RequestParam(required = false) String lotusUsername,
        @RequestParam(required = false) Role role,
        @RequestParam(required = false) UserStatus status,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortOrder,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        PageResponse<UserResponse> response = userService.findAll(firstName, lastName, email, phoneNumber, telegramUsername, lotusUsername, role, status, sortBy, sortOrder, page, size);
        return ResponseEntity.ok(APIResponse.builder()
                .code(1000)
                .message("Users fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<APIResponse<UserResponse>> findById(@PathVariable Long id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(APIResponse.<UserResponse>builder()
                .code(1000)
                .message("User fetched successfully")
                .data(response)
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Object>> create(@RequestBody UserRequest request) {
        userService.create(request);
        return ResponseEntity.ok(APIResponse.builder()
                .code(1000)
                .message("User created successfully")
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<APIResponse<Object>> update(@PathVariable Long id, @RequestBody UserRequest request) {
        userService.update(id, request);
        return ResponseEntity.ok(APIResponse.builder()
                .code(1000)
                .message("User updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<APIResponse<Object>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(APIResponse.builder()
                .code(1000)
                .message("User deleted successfully")
                .build());
    }
}
