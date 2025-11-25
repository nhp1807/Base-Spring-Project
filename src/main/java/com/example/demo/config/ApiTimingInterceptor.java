package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ApiTimingInterceptor implements HandlerInterceptor {
    public static final String[] MONITORED_PATHS = {
            "/api/v1/auth/register",
            "/api/v1/auth/authenticate",
            "/api/v1/auth/google",
            "/api/v1/auth/facebook"
    };

    private static final String START_TIME_ATTRIBUTE = "apiTimingStart";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Object attr = request.getAttribute(START_TIME_ATTRIBUTE);
        if (attr instanceof Long startTime) {
            long durationMs = (System.nanoTime() - startTime) / 1_000_000;
            log.info("[API Timing] {} {} -> {} ms (status {})",
                    request.getMethod(),
                    request.getRequestURI(),
                    durationMs,
                    response.getStatus());
        }
    }
}

