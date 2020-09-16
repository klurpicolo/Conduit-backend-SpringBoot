package io.spring.conduit.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.conduit.exception.InternalErrorException;
import io.spring.conduit.model.User;
import io.spring.conduit.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
public class UserApi {

    private UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam){

        User newUser = User.builder()
                .username(registerParam.getUsername())
                .email(registerParam.getEmail())
                .password(registerParam.getPassword())
                .image("www.defaultImages.com/klur")
                .bio("").build();
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.status(201).body(createdUser);
    }

}
@Getter
@JsonRootName("user")
class RegisterParam {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @Email(message = "Wrong Email format")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Can't be empty")
    private String password;
}
