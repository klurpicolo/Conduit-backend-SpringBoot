package io.spring.conduit.controller;

import io.spring.conduit.core.article.ArticleRsFillerService;
import io.spring.conduit.core.article.ArticleService;
import io.spring.conduit.core.article.favorite.ArticleFavoriteService;
import io.spring.conduit.core.user.UserService;
import io.spring.conduit.core.user.following.FollowingService;
import io.spring.conduit.dto.request.NewArticleRq;
import io.spring.conduit.dto.request.UpdatedArticleRq;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.article.ArticleRs;
import io.spring.conduit.dto.response.article.ArticlesRs;
import io.spring.conduit.exception.ConduitAppException;
import io.spring.conduit.exception.InvalidInputException;
import io.spring.conduit.exception.NoAuthorizationException;
import io.spring.conduit.exception.ResourceNotFoundException;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(path = "/articles")
public class ArticlesController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleFavoriteService articleFavoriteService;
    @Autowired
    private UserService userService;
    @Autowired
    private FollowingService followingService;
    @Autowired
    private ArticleRsFillerService articleRsFillerService;

    @GetMapping
    public ResponseEntity getGlobalArticles(@AuthenticationPrincipal User currentUser,
                                            @RequestParam(value = "offset", defaultValue = "0") int offset,
                                            @RequestParam(value = "limit", defaultValue = "20") int limit,
                                            @RequestParam(value = "tag", required = false) String tag,
                                            @RequestParam(value = "author", required = false) String author,
                                            @RequestParam(value = "favorited", required = false) String favoritedBy){
        List<Article> articles = articleService.getArticles(tag, author, favoritedBy, limit, offset);
        List<ArticleRs> articleRss = articles.stream().map(a -> articleRsFillerService.toArticleRs(a, currentUser)).collect(Collectors.toList());
        return ResponseEntity.ok(ArticlesRs.builder()
                .articles(articleRss)
                .articlesCount(articleRss.size()).build());
    }

    @PostMapping
    public ResponseEntity createArticle(@AuthenticationPrincipal User currentUser,
                                        @Valid @RequestBody NewArticleRq newArticle,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new InvalidInputException(bindingResult);
        }

        Article createdArticle = articleService.createArticle(newArticle.getTitle(), newArticle.getDescription(), newArticle.getBody(), newArticle.getTagList(), currentUser.getId())
                .orElseThrow(ConduitAppException::new);
        return ResponseEntity.ok(ArticleRs.getArticleResponse(articleRsFillerService.toArticleRs(createdArticle)));

    }

    @GetMapping(path = "feed")
    public ResponseEntity getFeedArticles(@AuthenticationPrincipal User currentUser,
                                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                                          @RequestParam(value = "limit", defaultValue = "20") int limit){
        List<Article> feed = articleService.getFeedArticles(currentUser.getId(), limit, offset);
        return ResponseEntity.ok(ArticlesRs.builder()
                .articles(feed.stream().map(a-> articleRsFillerService.toArticleRs(a,currentUser)).collect(Collectors.toList()))
                .articlesCount(feed.size()).build());
    }

    @GetMapping(path = "{slug}")
    public ResponseEntity getArticle(@AuthenticationPrincipal User currentUser,
                                     @PathVariable("slug") String slug){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(ArticleRs.getArticleResponse(articleRsFillerService.toArticleRs(article,currentUser)));
    }

    @PutMapping(path = "{slug}")
    public ResponseEntity updateArticle(@AuthenticationPrincipal User currentUser,
                                        @PathVariable("slug") String slug,
                                        @Valid @RequestBody UpdatedArticleRq updateArticle){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        if(!article.getAuthor().getId().equals(currentUser.getId())){
            throw new NoAuthorizationException();
        }

        //TODO refactor
        if(!updateArticle.getTitle().equals("")){
            article.setTitle(updateArticle.getTitle());
        }
        if(!updateArticle.getDescription().equals("")){
            article.setDescription(updateArticle.getDescription());
        }
        if(!updateArticle.getBody().equals("")){
            article.setBody(updateArticle.getBody());
        }

        Article updatedArticle = articleService.updateArticle(article);
        return ResponseEntity.ok(ArticleRs.getArticleResponse(articleRsFillerService.toArticleRs(updatedArticle,currentUser)));
    }

    @DeleteMapping(path = "{slug}")
    public ResponseEntity deleteArticle(@AuthenticationPrincipal User currentUser,
                                        @PathVariable("slug") String slug){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);
        if(!article.getAuthor().getId().equals(currentUser.getId())){
            throw new NoAuthorizationException();
        }

        articleService.deleteArticle(article);
        return ResponseEntity.noContent().build();
    }

    private ArticlesRs getMockRes(){
        ArticlesRs articlesRs = new ArticlesRs();
        ArticleRs articleRs = ArticleRs.builder()
                .slug("slug1")
                .title("title1")
                .description("description is here")
                .body("mock body")
                .tagList(Arrays.asList("tag1","tag2"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorited(true)
                .favoritesCount(5)
                .author(ProfileRs.builder()
                        .username("klur")
                        .bio("bio")
                        .image("this image")
                        .following(true).build())
                .build();
        articlesRs.setArticles(Collections.singletonList(articleRs));
        return articlesRs;
    }


}
