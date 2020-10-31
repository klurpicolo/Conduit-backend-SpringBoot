package io.spring.conduit.core.user.following;

import io.spring.conduit.model.UserFollowing;

import java.util.List;

public interface FollowingService {

    void follow(String userId, String followingUserId);
    void unfollow(String userId, String followingUserId);
    Boolean isUserFollowing(String userId, String followingUsername);
    List<UserFollowing> getUserFollowing(String userId);

}
