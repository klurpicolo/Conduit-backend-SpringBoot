package io.spring.conduit.core.comment;

import io.spring.conduit.dto.response.comment.CommentRs;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.User;

public interface CommentRsFillerService {

    CommentRs toCommentRs(Comment comment, User user);
}
