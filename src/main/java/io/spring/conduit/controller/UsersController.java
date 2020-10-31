package io.spring.conduit.controller;


import io.spring.conduit.core.encrypt.EncryptService;
import io.spring.conduit.core.jwt.JwtService;
import io.spring.conduit.core.user.UserService;
import io.spring.conduit.dto.request.LoginUserRq;
import io.spring.conduit.dto.request.RegisterUserRq;
import io.spring.conduit.dto.response.UserWithTokenRs;
import io.spring.conduit.exception.InvalidInputException;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EncryptService encryptService;

    private String defaultBio = "default bio";
    private String defaultImage = "default image";

    @PostMapping
    public ResponseEntity registerUser(@RequestBody RegisterUserRq user, BindingResult bindingResult){
        checkInput(user,bindingResult);

        User newUser = User.builder()
                .email(user.getEmail())
                .password(encryptService.encrypt(user.getPassword()))
                .username(user.getUsername())
                .bio(defaultBio)
                .image(defaultImage).build();
        User createdUser = userService.save(newUser);
        return ResponseEntity.ok().body(
                UserWithTokenRs.getUserResponse(new UserWithTokenRs(createdUser, jwtService.getUserToken(createdUser)))
        );
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginUserRq loginUserRq, BindingResult bindingResult){
        Optional<User> optionalUser = userService.findByEmail(loginUserRq.getEmail());
        if (optionalUser.isPresent() && encryptService.check(loginUserRq.getPassword(),optionalUser.get().getPassword())){
            return ResponseEntity.ok().body(
                    UserWithTokenRs.getUserResponse(new UserWithTokenRs(optionalUser.get(), jwtService.getUserToken(optionalUser.get())))
            );
        } else {
            bindingResult.rejectValue("password", "INVALID", "invalid email or password!!!");
            throw new InvalidInputException(bindingResult);
        }
    }

    private void checkInput(RegisterUserRq registerUserRq, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }
        if(userService.findByEmail(registerUserRq.getEmail()).isPresent()){
            log.info("email : {} is duplicated", registerUserRq.getEmail() );
            bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
        }
        if(userService.findByUsername(registerUserRq.getUsername()).isPresent()){
            bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
        }
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }

    }



}
