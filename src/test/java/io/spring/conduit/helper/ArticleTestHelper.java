package io.spring.conduit.helper;

import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.article.ArticleRs;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Tag;
import io.spring.conduit.model.User;

import java.util.stream.Collectors;

public class ArticleTestHelper {

    public static ArticleRs getArticleRs(Article article, User author, int favoritesCount, Boolean isFavorited, Boolean isFollowing){
        return ArticleRs.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(article.getTags().stream().map(Tag::getBody).collect(Collectors.toList()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favoritesCount(favoritesCount)
                .favorited(isFavorited)
                .author(ProfileRs.builder()
                        .username(author.getUsername())
                        .bio(author.getBio())
                        .following(isFollowing)
                        .image(author.getImage())
                        .build()).build();
    }

}
