package io.spring.conduit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class ErrorResponseBody {
    @Builder.Default
    private Date timestamp = new Date();
    private Integer status;
    private String error;
    private String message;
    @Builder.Default
    private String path = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
}
