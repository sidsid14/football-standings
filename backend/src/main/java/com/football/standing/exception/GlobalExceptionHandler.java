package com.football.standing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Map<String, Object>> handleClientException(ClientException ex) {
        Map<String, Object> errorResponse = Map.of(
                "error", ex.getError(),
                "message", ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String, Object>> handleServerException(ServerException ex) {
        Map<String, Object> errorResponse = Map.of(
                "error", ex.getError(),
                "message", ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
