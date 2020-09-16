package io.spring.conduit.exception;

import io.spring.conduit.dto.ErrorResponseBody;
import lombok.Getter;

public class ApiException extends RuntimeException {

    @Getter
    private ErrorResponseBody errorResponseBody;

    ApiException(String message, final ErrorResponseBody errorResponseBody){
        super(message + (errorResponseBody != null ? " : " + errorResponseBody : "" ));
        this.errorResponseBody = errorResponseBody;
    }

}
