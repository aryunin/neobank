package com.aryunin.conveyor.controller;

import com.aryunin.conveyor.exception.ScoringFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// TODO common error response (for ex 404)
@RestControllerAdvice
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
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex) {
        ProblemDetail pd = ex.getBody();
        pd.setProperty("fields", formatFieldErrors(ex.getFieldErrors()));
        return pd;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ScoringFailedException.class)
    public ProblemDetail handleScoringFailedException(ScoringFailedException ex) {
        return ex.getBody();
    }
}
