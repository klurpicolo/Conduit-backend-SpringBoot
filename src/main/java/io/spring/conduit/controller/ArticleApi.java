package io.spring.conduit.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.conduit.dto.ErrorResponseBody;
import io.spring.conduit.exception.InvalidRequestException;
import io.spring.conduit.model.Article;
import io.spring.conduit.service.ArticleService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Log4j2
@RestController
@RequestMapping(path = "/articles")
public class ArticleApi {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity getArticles(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "tag", required = false) String tag,
                                      @RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "favorited", required = false) String favoritedBy,
                                      @RequestParam(value = "limit", defaultValue = "20") int limit){
        log.debug("Get all articles");
        return ResponseEntity.ok(articleService.findRecentArticles(tag, author, favoritedBy, offset, limit));
    }

    @PostMapping
    public ResponseEntity createArticles(@Valid @RequestBody NewArticleParam newArticleParam,
                                         BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("This is message",
                    ErrorResponseBody.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("fck bad article create request")
                            .error("error")
                            .build());
        }

        Article newArticle = Article.builder()
                .title(newArticleParam.getTitle())
                .body(newArticleParam.getBody())
                .description(newArticleParam.getDescription())
                .slug(newArticleParam.getTitle())
                .build();

        return ResponseEntity.status(201).body(articleService.save(newArticle));
    }

}

@Getter
@JsonRootName("article")
@NoArgsConstructor
class NewArticleParam {
    @NotBlank(message = "can't be empty")
    private String title;
    @NotBlank(message = "can't be empty")
    private String description;
    @NotBlank(message = "can't be empty")
    private String body;
    private String[] tagList;

}
