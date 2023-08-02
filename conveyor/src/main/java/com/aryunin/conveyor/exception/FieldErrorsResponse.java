package com.aryunin.conveyor.exception;

import com.aryunin.conveyor.util.ErrorResponseDecorator;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FieldErrorsResponse extends ErrorResponseDecorator {
    private final List<FieldError> fieldErrors;

    public FieldErrorsResponse(ErrorResponse errorResponse, List<FieldError> fieldErrors) {
        super(errorResponse);
        this.fieldErrors = fieldErrors;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return errorResponse.getStatusCode();
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail pd = errorResponse.getBody();
        pd.setProperty("fields", formatFieldErrors(fieldErrors));
        return pd;
    }

    private Map<String, String> formatFieldErrors(List<FieldError> errors) {
        return errors.stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        e -> (e.getDefaultMessage() == null) ? "" : e.getDefaultMessage()
                )
        );
    }
}
