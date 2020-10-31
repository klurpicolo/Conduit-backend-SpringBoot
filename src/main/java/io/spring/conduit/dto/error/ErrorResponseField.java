package io.spring.conduit.dto.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ErrorResponseField {
    private String resource;
    private String field;
    private String code;
    private String message;

}
