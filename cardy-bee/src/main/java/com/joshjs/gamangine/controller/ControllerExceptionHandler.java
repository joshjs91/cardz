package com.joshjs.gamangine.controller;

import com.joshjs.gamangine.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
        }

        /**
         * Handles invalid input from the user.
         * Returns 400 Bad Request.
         */
        @ExceptionHandler(InvalidInputException.class)
        public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid input: ");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
}
