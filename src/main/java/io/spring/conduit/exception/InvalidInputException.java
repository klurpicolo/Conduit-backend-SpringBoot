package io.spring.conduit.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;

@Getter
@Setter
public class InvalidInputException extends ConduitAppException{
    private Errors errors;

    public InvalidInputException(Errors errors){
        this.errors = errors;
    }
}
