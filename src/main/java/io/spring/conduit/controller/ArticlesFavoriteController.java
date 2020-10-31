package io.spring.conduit.controller;

import io.spring.conduit.core.article.ArticleRsFillerService;
import io.spring.conduit.core.article.ArticleService;
import io.spring.conduit.core.article.favorite.ArticleFavoriteService;
import io.spring.conduit.dto.response.article.ArticleRs;
import io.spring.conduit.exception.ResourceNotFoundException;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(path = "/articles/{slug}/favorite")
public class ArticlesFavoriteController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRsFillerService articleRsFillerService;
    @Autowired
    private ArticleFavoriteService articleFavoriteService;


    @PostMapping
    public ResponseEntity favoriteArticle(@AuthenticationPrincipal User currentUser,
                                          @PathVariable("slug") String slug){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);

        articleFavoriteService.favoriteArticle(article.getId(), currentUser.getId());

        ArticleRs articleRs = articleRsFillerService.toArticleRs(article, currentUser);
        return ResponseEntity.ok(ArticleRs.getArticleResponse(articleRs));

    }

    @DeleteMapping
    public ResponseEntity unfavoriteArticle(@AuthenticationPrincipal User currentUser,
                                            @PathVariable("slug") String slug){
        Article article = articleService.getArticle(slug).orElseThrow(ResourceNotFoundException::new);

        articleFavoriteService.unfavoriteArticle(article.getId(), currentUser.getId());

        ArticleRs articleRs = articleRsFillerService.toArticleRs(article, currentUser);
        return ResponseEntity.ok(ArticleRs.getArticleResponse(articleRs));
    }

}
