package io.spring.conduit.controller;

import io.spring.conduit.core.encrypt.EncryptService;
import io.spring.conduit.core.user.UserService;
import io.spring.conduit.dto.request.UpdatedUserRq;
import io.spring.conduit.dto.response.UserWithTokenRs;
import io.spring.conduit.exception.InvalidInputException;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping(path = "/user")
public class CurrentUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptService encryptService;

    @GetMapping
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal User currentUser, @RequestHeader("Authorization") String authorization){
        User user = userService.findByUsername(currentUser.getUsername()).get();
        return ResponseEntity.ok(UserWithTokenRs.getUserResponse(new UserWithTokenRs(user, authorization.split(" ")[1])));
    }

    @PutMapping
    public ResponseEntity updateCurrentUser(@AuthenticationPrincipal User currentUser,
                                            @RequestHeader("Authorization") String authorization,
                                            @Valid @RequestBody UpdatedUserRq updatedUserRq,
                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult);
        }
        checkDuplicationEmailAndPassword(currentUser, updatedUserRq, bindingResult);

        currentUser.update(updatedUserRq.getEmail(),
                updatedUserRq.getUsername(),
                encryptService.encrypt(updatedUserRq.getPassword()),
                updatedUserRq.getBio(),
                updatedUserRq.getImage());
        User updatedUser = userService.save(currentUser);

        return ResponseEntity.ok(UserWithTokenRs.getUserResponse(new UserWithTokenRs(updatedUser, authorization.split(" ")[1])));

    }

    private void checkDuplicationEmailAndPassword(User currentUser, UpdatedUserRq updatedUserRq, BindingResult bindingResult){
        if(!updatedUserRq.getEmail().equals("")){
            Optional<User> checkEmail = userService.findByEmail(updatedUserRq.getEmail());
            if(checkEmail.isPresent() && !checkEmail.get().equals(currentUser)){
                bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
            }
        }
        if(!updatedUserRq.getUsername().equals("")){
            Optional<User> checkUsername = userService.findByUsername(updatedUserRq.getUsername());
            if(checkUsername.isPresent() && !checkUsername.get().equals(currentUser)){
                bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
            }
        }
        if(bindingResult.hasErrors()){
            throw new InvalidInputException(bindingResult);
        }
    }

}

