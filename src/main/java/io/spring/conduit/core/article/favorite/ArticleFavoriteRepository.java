package io.spring.conduit.core.article.favorite;


import io.spring.conduit.model.ArticleFarvorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleFavoriteRepository extends JpaRepository<ArticleFarvorite, String> {

//    boolean existsByArticleIdAndExistsByUserId(String articleId, String userId);
    List<ArticleFarvorite> findByArticleIdAndUserId(String articleId, String userId);
    int countByArticleId(String articleId);

    long deleteByArticleIdAndUserId(String articleId, String userId);

}
