package io.spring.conduit.core.user.following;

import io.spring.conduit.model.UserFollowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowingRepository extends JpaRepository<UserFollowing, String> {
    Optional<UserFollowing> findByUserIdAndFollowingUserId(String userId, String followingUserId);
    List<UserFollowing> findByUserId(String userId);
    void deleteByUserIdAndFollowingUserId(String userId, String followingUserId);

}
