package io.spring.conduit.dto.response;

import io.spring.conduit.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithTokenRs {

    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    public UserWithTokenRs(User user, String token){
        this.email = user.getEmail();
        this.token = token;
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
    }

    public static Map<String, Object> getUserResponse(UserWithTokenRs userWithTokenRs) {
        return new HashMap<String, Object>() {{
            put("user", userWithTokenRs);
        }};
    }

}
