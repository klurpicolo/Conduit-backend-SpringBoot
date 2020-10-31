package io.spring.conduit.core.article;

import io.spring.conduit.dto.response.article.ArticleRs;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.User;

public interface ArticleRsFillerService {

    ArticleRs toArticleRs(Article article);
    ArticleRs toArticleRs(Article article, User user);

}
