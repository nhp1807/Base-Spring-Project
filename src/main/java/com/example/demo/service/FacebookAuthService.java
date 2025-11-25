package com.example.demo.service;

import com.example.demo.cache.BloomFilterUserId;
import com.example.demo.dto.request.FacebookAuthRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.FacebookUserInfo;
import com.example.demo.enums.LoginProvider;
import com.example.demo.enums.Role;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacebookAuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BloomFilterUserId bloomFilterUserId;
    
    @Value("${facebook.oauth2.app-id}")
    private String facebookAppId;
    
    @Value("${facebook.oauth2.app-secret}")
    private String facebookAppSecret;
    
    @Value("${token.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String FACEBOOK_USER_INFO_URL = "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name,picture";
    private static final String FACEBOOK_TOKEN_VERIFY_URL = "https://graph.facebook.com/debug_token";
    
    @Transactional
    public AuthenticationResponse authenticateWithFacebook(FacebookAuthRequest request) {
        try {
            // 1. Validate Facebook access token and get user info
            FacebookUserInfo facebookUserInfo = validateFacebookToken(request.getAccessToken());

            // 2. Find or create user
            User user = findOrCreateUser(facebookUserInfo);

            // 3. Xóa refresh token cũ của user
//            refreshTokenRepository.deleteByUserId(user.getId());

            // 4. Generate JWT tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // 5. Lưu refresh token mới vào database
            RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .isRevoked(false)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION / 1000))
                .build();
            refreshTokenRepository.save(refreshTokenEntity);

            log.info("User {} logged in via Facebook authentication", user.getEmail());
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Facebook authentication failed: " + e.getMessage());
        }
    }
    
    private FacebookUserInfo validateFacebookToken(String accessToken) {
        try {
            // First, verify the token is valid
            verifyFacebookToken(accessToken);
            
            // Then, get user info from Facebook Graph API
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            String url = FACEBOOK_USER_INFO_URL + "&access_token=" + accessToken;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            
            // Parse the response
            JsonNode userInfo = objectMapper.readTree(response.getBody());
            
            // Extract profile picture URL
            String profilePicture = null;
            if (userInfo.has("picture") && userInfo.get("picture").has("data")) {
                profilePicture = userInfo.get("picture").get("data").get("url").asText();
            }
            
            return FacebookUserInfo.builder()
                    .facebookId(userInfo.get("id").asText())
                    .email(userInfo.has("email") ? userInfo.get("email").asText() : null)
                    .firstName(userInfo.has("first_name") ? userInfo.get("first_name").asText() : null)
                    .lastName(userInfo.has("last_name") ? userInfo.get("last_name").asText() : null)
                    .profilePicture(profilePicture)
                    .emailVerified(userInfo.has("email") && !userInfo.get("email").isNull())
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate Facebook token: " + e.getMessage());
        }
    }
    
    private void verifyFacebookToken(String accessToken) {
        try {
            String url = FACEBOOK_TOKEN_VERIFY_URL + "?input_token=" + accessToken + 
                        "&access_token=" + facebookAppId + "|" + facebookAppSecret;
            
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            
            JsonNode result = objectMapper.readTree(response.getBody());
            
            if (!result.has("data") || !result.get("data").has("is_valid") || 
                !result.get("data").get("is_valid").asBoolean()) {
                throw new RuntimeException("Invalid Facebook access token");
            }
            
            // Verify app ID matches
            if (result.get("data").has("app_id") && 
                !result.get("data").get("app_id").asText().equals(facebookAppId)) {
                throw new RuntimeException("Facebook token app ID does not match");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Facebook token: " + e.getMessage());
        }
    }
    
    private User findOrCreateUser(FacebookUserInfo facebookUserInfo) {
        // First, try to find user by Facebook ID
        Optional<User> existingUser = userRepository.findByFacebookId(facebookUserInfo.getFacebookId());
        
        if (existingUser.isPresent()) {
            // User already exists with this Facebook ID
            return existingUser.get();
        }
        
        // Try to find user by email
//        if (facebookUserInfo.getEmail() != null) {
//            Optional<User> userByEmail = userRepository.findByEmail(facebookUserInfo.getEmail());
//
//            if (userByEmail.isPresent()) {
//                // User exists but doesn't have Facebook ID - link the accounts
//                User user = userByEmail.get();
//                user.setFacebookId(facebookUserInfo.getFacebookId());
//                user.setProvider(LoginProvider.FACEBOOK);
//                if (facebookUserInfo.getProfilePicture() != null) {
//                    user.setProfilePicture(facebookUserInfo.getProfilePicture());
//                }
//                user.setEmailVerified(facebookUserInfo.isEmailVerified());
//                return userRepository.save(user);
//            }
//        }
        
        // Create new user
        User newUser = User.builder()
//                .id(randomNewUserId())
                .facebookId(facebookUserInfo.getFacebookId())
                .email(facebookUserInfo.getEmail())
                .firstName(facebookUserInfo.getFirstName())
                .lastName(facebookUserInfo.getLastName())
                .profilePicture(facebookUserInfo.getProfilePicture())
                .emailVerified(facebookUserInfo.isEmailVerified())
                .provider(LoginProvider.FACEBOOK)
                .role(Role.USER)
                .build();
        
        return userRepository.save(newUser);
    }

    private Long randomNewUserId() {
        // Tạo random userId có đúng 16 chữ số (từ 10^15 đến 10^16-1)
        Long randomUserId = ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L, 9_999_999_999_999_999L);
        while (true) {
            boolean inBloom = bloomFilterUserId.contains(randomUserId);
            if (inBloom) {
                // Bloom báo có -> xác minh DB
                if (userRepository.existsById(randomUserId)) {
                    randomUserId = ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L, 9_999_999_999_999_999L);
                    continue; // thật sự trùng -> quay lại
                }
                // false positive -> chấp nhận
                break;
            } else {
                // Bloom không có -> chấp nhận ngay theo yêu cầu của bạn
                break;
            }
        }
        bloomFilterUserId.add(randomUserId);
        return randomUserId;
    }
}

