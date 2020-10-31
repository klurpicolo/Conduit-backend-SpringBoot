package io.spring.conduit.core.user.following;

import io.spring.conduit.model.UserFollowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowingServiceImpl implements FollowingService {

    @Autowired
    private UserFollowingRepository userFollowingRepository;

    @Override
    public void follow(String userId, String followingUserId) {
        userFollowingRepository.save(UserFollowing.builder().userId(userId).followingUserId(followingUserId).build());
    }

    @Override
    @Transactional
    public void unfollow(String userId, String followingUserId) {
        userFollowingRepository.deleteByUserIdAndFollowingUserId(userId, followingUserId);
    }

    @Override
    public Boolean isUserFollowing(String userId, String followingUserId) {
        return userFollowingRepository.findByUserIdAndFollowingUserId(userId, followingUserId).isPresent();
    }

    @Override
    public List<UserFollowing> getUserFollowing(String userId) {
        return userFollowingRepository.findByUserId(userId);
    }
}
