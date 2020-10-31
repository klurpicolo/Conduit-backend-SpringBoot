package io.spring.conduit.core.comment;

import io.spring.conduit.model.Article;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getAllComments(Article article) {

        return commentRepository.findByArticleId(article.getId());
    }

    @Override
    public Optional<Comment> getComment(String commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Optional<Comment> addComment(Article article, String body, User user) {

        Comment newComment = Comment.builder()
                .article(article)
                .commenter(user)
                .body(body)
                .build();

        return Optional.of(commentRepository.save(newComment));
    }

    @Override
    @Transactional
    public void deleteComment(Comment comment) {

        commentRepository.delete(comment);
    }
}
