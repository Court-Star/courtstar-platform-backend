package com.example.courtstar.exception;

import com.example.courtstar.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobleHandlingException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {

        ApiResponse response = ApiResponse.builder()
                .message(e.getErrorCode().getMessage())
                .code(e.getErrorCode().getCode())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String getMessage = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(getMessage);
        ApiResponse response = ApiResponse.builder()
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
