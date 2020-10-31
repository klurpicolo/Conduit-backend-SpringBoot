package io.spring.conduit.core.comment;

import io.spring.conduit.core.user.following.FollowingService;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.comment.CommentRs;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentRsFillerServiceImpl implements CommentRsFillerService {

    @Autowired
    private FollowingService followingService;

    @Override
    public CommentRs toCommentRs(Comment comment, User user) {
        User commenter = comment.getCommenter();
        CommentRs commentRs = CommentRs.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .author(ProfileRs.builder()
                        .username(commenter.getUsername())
                        .bio(commenter.getBio())
                        .image(commenter.getImage()).build())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt()).build();
        if(user != null){
            boolean isCurrentUserFollowingAuthor = followingService.isUserFollowing(user.getId(), comment.getCommenter().getId());
            commentRs.getAuthor().setFollowing(isCurrentUserFollowingAuthor);
        }
        return commentRs;
    }
}
