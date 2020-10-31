package io.spring.conduit.core.article;

import io.spring.conduit.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> getArticles(String tag, String author, String favoritedBy, int limit, int offset);
    List<Article> getFeedArticles(String userId, int limit, int offset);

    Optional<Article> createArticle(String title, String description, String body, List<String> tags, String userId);
    Optional<Article> getArticle(String slug);

    Article updateArticle(Article article);
    void deleteArticle(Article article);
}
