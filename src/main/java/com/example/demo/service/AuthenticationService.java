package com.example.demo.service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.enums.ErrorCode;
import com.example.demo.enums.Role;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.AppException;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.cache.AccessTokenCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccessTokenCache accessTokenCache;

    @Value("${token.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    @Transactional
    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role[] roles = Role.values();
        if (!Arrays.asList(roles).contains(request.getRole())) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .status(UserStatus.ACTIVE)
                .build();

        repository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token to database
        saveRefreshToken(refreshToken, user);

        accessTokenCache.put(user.getEmail(), accessToken);

        AuthenticationResponse.UserData userData = new AuthenticationResponse.UserData();
        userData.setUserId(user.getId());
        userData.setRole(user.getRole().name());
        userData.setEmail(user.getEmail());
        userData.setFullName(user.getFirstName() + " " + user.getLastName());

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userData)
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_SET_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token to database
        saveRefreshToken(refreshToken, user);

        accessTokenCache.put(user.getEmail(), accessToken);

        AuthenticationResponse.UserData userData = new AuthenticationResponse.UserData();
        userData.setUserId(user.getId());
        userData.setRole(user.getRole().name());
        userData.setEmail(user.getEmail());
        userData.setFullName(user.getFirstName() + " " + user.getLastName());

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userData)
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.getRefreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User userDetails = repository.findByEmail(userEmail)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Check if refresh token exists in database
            RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
            if (jwtService.isTokenValid(refreshToken, userDetails)
                    && storedRefreshToken.getExpiryDate().isAfter(Instant.now())) {

                // Only generate new access token, keep the same refresh token
                String accessToken = jwtService.generateAccessToken(userDetails);

                accessTokenCache.put(userDetails.getEmail(), accessToken);

                return ResponseEntity.ok(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken) // Return the same refresh token
                        .build());
            }
        }

        throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Transactional
    public void saveRefreshToken(String token, User user) {
        // Delete existing refresh token for this user first
        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION/100)) // 7 days
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        User user = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND))
                .getUser();
        refreshTokenRepository.deleteByUserId(user.getId());
        accessTokenCache.invalidate(user.getEmail());
    }
}
