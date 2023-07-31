package com.aryunin.conveyor.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ProblemDetail body = ex.getBody();
        ex.getAllErrors().stream()
                .map(e -> (FieldError) e)
                .forEach(e ->
                        body.setProperty(
                                e.getField(),
                                e.getDefaultMessage()
                        )
                );
        return body;
    }
}
