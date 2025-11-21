package com.example.demo.config;

import org.springframework.util.AntPathMatcher;

public final class SecurityPaths {
    private SecurityPaths() {
    }

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html"
    };

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    public static boolean isPublicPath(String path) {
        for (String pattern : PUBLIC_ENDPOINTS) {
            if (MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}

