package com.aryunin.conveyor.util;

import org.springframework.web.ErrorResponse;

public abstract class ErrorResponseDecorator implements ErrorResponse {
    protected ErrorResponse errorResponse;

    public ErrorResponseDecorator(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
