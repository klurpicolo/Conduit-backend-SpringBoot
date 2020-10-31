package io.spring.conduit.controller;


import io.spring.conduit.core.article.ArticleService;
import io.spring.conduit.core.comment.CommentRsFillerService;
import io.spring.conduit.core.comment.CommentService;
import io.spring.conduit.dto.request.NewCommentRq;
import io.spring.conduit.dto.response.comment.CommentRs;
import io.spring.conduit.dto.response.comment.CommentsRs;
import io.spring.conduit.exception.ConduitAppException;
import io.spring.conduit.exception.InvalidInputException;
import io.spring.conduit.exception.NoAuthorizationException;
import io.spring.conduit.exception.ResourceNotFoundException;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(path = "/articles/{slug}/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentRsFillerService commentRsFillerService;

    @GetMapping
    public ResponseEntity getCommentOfArticle(@AuthenticationPrincipal User currentUser,
                                              @PathVariable("slug") String slug){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        List<CommentRs> commentRsList = commentService.getAllComments(article).stream()
                .map(comment -> commentRsFillerService.toCommentRs(comment,currentUser))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CommentsRs.builder().commentRsList(commentRsList).build());
    }

    @PostMapping
    public ResponseEntity addCommentToArticle(@AuthenticationPrincipal User currentUser,
                                              @PathVariable("slug") String slug,
                                              @Valid @RequestBody NewCommentRq commentRq,
                                              BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new InvalidInputException(bindingResult);
        }

        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        Comment comment = commentService.addComment(article,commentRq.getBody(),currentUser).orElseThrow(ConduitAppException::new);

        return ResponseEntity.ok(CommentRs.getCommentResponse(commentRsFillerService.toCommentRs(comment,currentUser)));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity deleteCommentFromArticle(@AuthenticationPrincipal User currentUser,
                                                   @PathVariable("slug") String slug,
                                                   @PathVariable("id") String commentId){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        if(!article.getAuthor().getId().equals(currentUser.getId())){
            throw new NoAuthorizationException();
        }
        Comment toDeleteComment = commentService.getComment(commentId).orElseThrow(ResourceNotFoundException::new);
        commentService.deleteComment(toDeleteComment);

        return ResponseEntity.noContent().build();
    }

}
