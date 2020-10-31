package io.spring.conduit.core.comment;

import io.spring.conduit.model.Article;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAllComments(Article article);
    Optional<Comment> getComment(String commentId);
    Optional<Comment> addComment(Article article, String body, User user);
    void deleteComment(Comment comment);
}
