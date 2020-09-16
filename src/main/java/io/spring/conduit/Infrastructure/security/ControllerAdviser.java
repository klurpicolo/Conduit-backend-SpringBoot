package io.spring.conduit.Infrastructure.security;

import io.spring.conduit.dto.ErrorResponseBody;
import io.spring.conduit.exception.InvalidRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class ControllerAdviser {

    @ExceptionHandler({InvalidRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseBody handleIDNotFoundException(InvalidRequestException ex) {
        log.error(ex.getErrorResponseBody().toString());
        return ex.getErrorResponseBody();
    }
}
