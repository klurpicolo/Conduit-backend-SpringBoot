package io.spring.conduit.core.article.favorite;

import io.spring.conduit.exception.ConduitAppException;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.ArticleFarvorite;
import io.spring.conduit.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
public class ArticleFavoriteServiceImpl implements ArticleFavoriteService {

    @Autowired
    private ArticleFavoriteRepository articleFavoriteRepository;

    @Override
    public boolean isFavoritedBy(String articleId, String userId) {
//        return articleFavoriteRepository.existsByArticleIdAndExistsByUserId(articleId, userId);
        return articleFavoriteRepository.findByArticleIdAndUserId(articleId, userId).size() > 0;
    }

    @Override
    public int getFavoritedCount(String articleId) {
        return articleFavoriteRepository.countByArticleId(articleId);
    }

    @Override
    public ArticleFarvorite favoriteArticle(String articleId, String userId) {
        return articleFavoriteRepository.save(ArticleFarvorite.builder().articleId(articleId).userId(userId).build());
    }

    @Override
    @Transactional
    public void unfavoriteArticle(String articleId, String userId) {
        long effectedRow = articleFavoriteRepository.deleteByArticleIdAndUserId(articleId, userId);
        if(effectedRow!=1){
            log.error("unfavorite article got more that 1 effected row");
            throw new ConduitAppException();
        }
    }

}
