package io.spring.conduit.dto;

import io.spring.conduit.model.User;
import lombok.Getter;

@Getter
public class UserWithToken {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String token;

    public UserWithToken(User user, String token){
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.username = user.getUsername();
        this.image = user.getImage();
        this.token = token;
    }

}
