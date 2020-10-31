package io.spring.conduit.controller;

import io.spring.conduit.core.user.UserService;
import io.spring.conduit.core.user.following.FollowingService;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.exception.ResourceNotFoundException;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(path = "/profiles/{username}")
public class ProfileController {

    @Autowired
    private FollowingService followingService;
    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity getProfile(@AuthenticationPrincipal User currentUser,
                                     @PathVariable("username") String username){
        User user = userService.findByUsername(username).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(ProfileRs.getProfileResponse(
                ProfileRs.builder()
                        .username(user.getUsername())
                        .bio(user.getBio())
                        .image(user.getImage())
                        .following(followingService.isUserFollowing(currentUser.getId(), user.getId()))
                        .build()
        ));
    }

    @PostMapping(path = "follow")
    public ResponseEntity follow(@AuthenticationPrincipal User currentUser,
                                 @PathVariable("username") String username){
        User followingUser = userService.findByUsername(username).orElseThrow(ResourceNotFoundException::new);

        followingService.follow(currentUser.getId(), followingUser.getId());

        return ResponseEntity.ok(ProfileRs.getProfileResponse(
                ProfileRs.builder()
                    .username(followingUser.getUsername())
                    .bio(followingUser.getBio())
                    .image(followingUser.getImage())
                    .following(followingService.isUserFollowing(currentUser.getId(), followingUser.getId()))
                    .build()
        ));
    }

    @DeleteMapping(path = "follow")
    public ResponseEntity unfollow(@AuthenticationPrincipal User currentUser,
                                 @PathVariable("username") String username){
        User unfollowingUser = userService.findByUsername(username).orElseThrow(ResourceNotFoundException::new);

        if (!followingService.isUserFollowing(currentUser.getId(), unfollowingUser.getId())){
            throw new ResourceNotFoundException();
        } else {
            followingService.unfollow(currentUser.getId(), unfollowingUser.getId());
            return ResponseEntity.ok(ProfileRs.getProfileResponse(
                    ProfileRs.builder()
                            .username(unfollowingUser.getUsername())
                            .bio(unfollowingUser.getBio())
                            .image(unfollowingUser.getImage())
                            .following(followingService.isUserFollowing(currentUser.getId(), unfollowingUser.getId()))
                            .build()
            ));
        }
    }

}
