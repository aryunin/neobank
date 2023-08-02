package com.aryunin.conveyor.exception.handler;

import com.aryunin.conveyor.exception.FieldErrorsResponse;
import com.aryunin.conveyor.exception.ScoringFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ProblemDetail handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.info("no handler exception handled with msg \"" + ex.getMessage() + "\"");
        return ex.getBody();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        log.info("validation exception handled");
        return new FieldErrorsResponse(ex, ex.getFieldErrors()).getBody();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ScoringFailedException.class)
    public ProblemDetail handleScoringFailedException(ScoringFailedException ex) {
        log.info("scoring failed exception handled with msg \"" + ex.getMessage() + "\"");
        return ex.getBody();
    }
}
