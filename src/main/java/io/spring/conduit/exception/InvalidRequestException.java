package io.spring.conduit.exception;


import io.spring.conduit.dto.ErrorResponseBody;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message, ErrorResponseBody errorResponseBody) {
        super(message, errorResponseBody);
    }
}
