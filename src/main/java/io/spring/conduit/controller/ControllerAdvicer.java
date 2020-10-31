package io.spring.conduit.controller;

import io.spring.conduit.dto.error.ErrorResponse;
import io.spring.conduit.dto.error.ErrorResponseField;
import io.spring.conduit.exception.InvalidInputException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class ControllerAdvicer {

    @ExceptionHandler({InvalidInputException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidInputException(InvalidInputException ex) {
        List<ErrorResponseField> fields = ex.getErrors().getFieldErrors().stream().map(
                field ->
                    ErrorResponseField.builder()
                            .resource(field.getObjectName())
                            .field(field.getField())
                            .code(field.getCode())
                            .message(field.getDefaultMessage()).build()
        ).collect(Collectors.toList());
        log.error(fields);
        return ErrorResponse.builder().errors(fields).build();
    }

}
