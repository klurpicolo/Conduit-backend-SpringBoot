package io.spring.conduit.core.article.favorite;

import io.spring.conduit.model.Article;
import io.spring.conduit.model.ArticleFarvorite;
import io.spring.conduit.model.User;

import java.util.Optional;

public interface ArticleFavoriteService {

    boolean isFavoritedBy(String articleId, String userId);
    int getFavoritedCount(String articleId);

    ArticleFarvorite favoriteArticle(String articleId, String userId);
    void unfavoriteArticle(String articleId, String userId);

}
