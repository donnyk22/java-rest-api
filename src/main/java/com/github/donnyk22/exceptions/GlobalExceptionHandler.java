package com.github.donnyk22.exceptions;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.donnyk22.models.dtos.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> badRequest(BadRequestException ex) {
        return ResponseEntity.badRequest()
            .body(new ApiResponse<>(400, ex.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResponse<>(401, ex.getMessage(), null));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Object>> forbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ApiResponse<>(403, ex.getMessage(), null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(404, ex.getMessage(), null));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> conflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiResponse<>(409, ex.getMessage(), null));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse<Object>> internalCustom(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>(500, ex.getMessage(), null));
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> internal(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>(500, ex.getMessage(), null));
    }

    // === Handle Other Errors ===

    //Handle @Valid/jakarta.validation.constraints form
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", "));
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handle max file upload error
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxSizeException() {
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            "File too large",
            null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Custom any error message
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Invalid data. Please check your input.";
        Throwable root = getRootCause(ex);
        if (root != null && root.getMessage() != null) {
            String msg = root.getMessage().toUpperCase();
            logger.error("Global error handler: " + msg);
            if (msg.contains("CONSTRAINT")) {
                // Generic safe message for any constraint violation
                message = "Invalid reference or constraint violation";
            }
        }
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                message,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Throwable getRootCause(Throwable t) {
        Throwable root = t;
        while (root != null && root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }

}
