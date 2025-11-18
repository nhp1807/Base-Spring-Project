package com.example.vccorp.exception;

import com.example.vccorp.dto.response.APIResponse;
import com.example.vccorp.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

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
