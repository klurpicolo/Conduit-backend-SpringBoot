package io.spring.conduit.dto.response;

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
public class ProfileRs {

    private String username;
    private String bio;
    private String image;
    private boolean following;

    public static Map<String, Object> getProfileResponse(ProfileRs profileRs) {
        return new HashMap<String, Object>() {{
            put("profile", profileRs);
        }};
    }

}
