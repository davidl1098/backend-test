package com.tata.cuentas_service.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(HashMap::new, (m, f) -> m.put(f.getField(), f.getDefaultMessage()), Map::putAll);
        return ResponseEntity.unprocessableEntity().body(body(422, "Datos inválidos", Map.of("errors", errors)));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body(404, "Recurso no encontrado", null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> conflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body(409, "Conflicto de datos", null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> bad(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(body(400, ex.getMessage(), null));
    }

    private Map<String, Object> body(int status, String message, Map<String, ?> extra) {
        var m = new LinkedHashMap<String, Object>();
        m.put("timestamp", OffsetDateTime.now().toString());
        m.put("status", status);
        m.put("message", message);
        if (extra != null)
            m.putAll(extra);
        return m;
    }
}
