package com.ing.mortgagecheckservice.exception.handler;

import com.ing.mortgagecheckservice.exception.InterestRateNotFoundException;
import com.ing.mortgagecheckservice.exception.MortgageCheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for centralized error handling.
 */
@RestControllerAdvice
public class MortgageCheckExceptionHandler {

    /**
     * Handles InterestRateNotFoundException and returns a 404 response.
     */
    @ExceptionHandler(InterestRateNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInterestRateNotFound(InterestRateNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("Interest Rate Not Found", exception.getMessage()));
    }

    /**
     * Handles MortgageCheckException and returns a 500 response.
     */
    @ExceptionHandler(MortgageCheckException.class)
    public ResponseEntity<Map<String, Object>> handleMortgageCheckException(MortgageCheckException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Internal Server Error", exception.getMessage()));
    }

    /**
     * Handles validation errors for @Valid annotated request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(createErrorResponse("Validation Error",errors));
    }

    /**
     * Creates a structured error response map.
     *
     * @param error   The error title.
     * @param message The error message.
     * @return A structured map representing the error response.
     */
    private Map<String, Object> createErrorResponse(String error, Object message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "error", error,
                "message", message
        );
    }
}
