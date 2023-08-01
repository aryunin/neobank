package com.aryunin.conveyor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// TODO common error response (for ex 404)
@ControllerAdvice
public class GlobalExceptionHandler {
    private Map<String, String> formatFieldErrors(List<FieldError> errors) {
        return errors.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        e -> (e.getDefaultMessage() == null) ? "" : e.getDefaultMessage()
                )
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ProblemDetail pd = ex.getBody();
        pd.setProperty("fields", formatFieldErrors(ex.getFieldErrors()));
        return pd;
    }
}
