package com.tata.clientes_service.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    var errors = ex.getBindingResult().getFieldErrors().stream()
        .collect(HashMap::new, (m, fe) -> m.put(fe.getField(), fe.getDefaultMessage()), Map::putAll);
    return ResponseEntity.badRequest().body(Map.of("message", "Datos inválidos", "errors", errors));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Recurso no encontrado"));
  }

  @ExceptionHandler({ IllegalArgumentException.class, DataIntegrityViolationException.class })
  public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
  }
}
