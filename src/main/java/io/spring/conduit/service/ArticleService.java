package io.spring.conduit.service;


import io.spring.conduit.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> findRecentArticles(String tag, String author, String favoritedBy, int offset, int limit);
    Article save(Article article);
}
