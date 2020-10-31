package io.spring.conduit.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("user")
public class RegisterUserRq {
    private String username;
    @Email(message = "This email is invalid")
    private String email;
    private String password;
}
