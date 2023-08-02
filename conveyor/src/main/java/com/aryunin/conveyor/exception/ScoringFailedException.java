package com.aryunin.conveyor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class ScoringFailedException extends RuntimeException implements ErrorResponse {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String title = "Scoring failure";

    public ScoringFailedException(String message) {
        super(message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return httpStatus;
    }

    @Override
    public ProblemDetail getBody() {
        var pd = ProblemDetail.forStatusAndDetail(httpStatus, getMessage());
        pd.setTitle(title);
        return pd;
    }
}
