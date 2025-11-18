package com.example.demo.exception;

import com.example.demo.dto.response.APIResponse;
import com.example.demo.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", HttpStatus.FORBIDDEN.value());
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", "Bạn không có quyền truy cập vào tài nguyên này");
        errorDetails.put("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", "Xác thực thất bại. Vui lòng đăng nhập lại");
        errorDetails.put("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("error", "Bad Credentials");
        errorDetails.put("message", "Email hoặc mật khẩu không đúng");
        errorDetails.put("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Bad Request");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGlobalException(
//            Exception ex, WebRequest request) {
//        Map<String, Object> errorDetails = new HashMap<>();
//        errorDetails.put("timestamp", new Date());
//        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorDetails.put("error", "Internal Server Error");
//        errorDetails.put("message", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau");
//        errorDetails.put("path", request.getDescription(false));
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse<Object>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse<Object> response = APIResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();

        ex.printStackTrace();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String defaultMessage = fieldError != null ? fieldError.getDefaultMessage() : null;

        ErrorCode mapped = mapMessageToErrorCode(defaultMessage).orElse(ErrorCode.INVALID_REQUEST);

        APIResponse<Object> response = APIResponse.builder()
                .code(mapped.getCode())
                .message(mapped.getMessage())
                .data(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<APIResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        // Pick first violation if present
        String messageKey = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse(null);

        ErrorCode mapped = mapMessageToErrorCode(messageKey).orElse(ErrorCode.INVALID_REQUEST);

        APIResponse<Object> response = APIResponse.builder()
                .code(mapped.getCode())
                .message(mapped.getMessage())
                .data(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    private Optional<ErrorCode> mapMessageToErrorCode(String messageKey) {
        if (messageKey == null || messageKey.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(ErrorCode.valueOf(messageKey));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
} 