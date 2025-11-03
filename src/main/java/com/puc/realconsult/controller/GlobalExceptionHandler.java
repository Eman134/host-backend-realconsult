package com.puc.realconsult.controller;

import com.puc.realconsult.exception.BadRequestException;
import com.puc.realconsult.exception.ConflictException;
import com.puc.realconsult.exception.ResourceNotFound;
import com.puc.realconsult.exception.idNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(idNotFound.class)
    public ResponseEntity<String> handleIdNotFoundException(Exception ex){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
    }

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
