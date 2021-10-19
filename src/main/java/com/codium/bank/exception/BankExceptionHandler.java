package com.codium.bank.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class BankExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof NullPointerException) {
            return ResponseEntity.badRequest().build();
        }
        else if(ex instanceof EntityNotFoundException)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
