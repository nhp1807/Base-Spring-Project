package com.example.demo.controller;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return service.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return service.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenRequest request
    ) {
        service.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}
