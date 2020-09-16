package io.spring.conduit.service;

import io.spring.conduit.model.Article;
import io.spring.conduit.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public List<Article> findRecentArticles(String tag, String author, String favoritedBy, int offset, int limit) {
        return articleRepository.findAll(PageRequest.of(offset, limit)).toList();
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }
}
