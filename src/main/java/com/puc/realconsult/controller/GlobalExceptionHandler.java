package com.puc.realconsult.controller;

import com.puc.realconsult.exception.BadRequestException;
import com.puc.realconsult.exception.ConflictException;
import com.puc.realconsult.exception.ResourceNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<String> handleNotFoundException(Exception ex){
        return ResponseEntity.notFound().build();
    }
}
