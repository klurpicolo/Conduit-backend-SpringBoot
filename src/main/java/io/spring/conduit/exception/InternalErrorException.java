package io.spring.conduit.exception;

import io.spring.conduit.dto.ErrorResponseBody;

public class InternalErrorException extends ApiException{
    public InternalErrorException(String message, ErrorResponseBody errorResponseBody) {
        super(message, errorResponseBody);
    }
}
