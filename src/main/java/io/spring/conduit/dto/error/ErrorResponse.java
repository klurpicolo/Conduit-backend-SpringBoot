package io.spring.conduit.dto.error;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@JsonSerialize(using = ErrorResponseSerializer.class)
@Getter
@Builder
@JsonRootName("errors")
@AllArgsConstructor
public class ErrorResponse {
    private List<ErrorResponseField> errors;

}
