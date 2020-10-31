package io.spring.conduit.core.article;

import io.spring.conduit.core.article.favorite.ArticleFavoriteService;
import io.spring.conduit.core.comment.CommentRsFillerService;
import io.spring.conduit.core.user.following.FollowingService;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.article.ArticleRs;
import io.spring.conduit.dto.response.comment.CommentRs;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Comment;
import io.spring.conduit.model.Tag;
import io.spring.conduit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ArticleRsFillerServiceImpl implements ArticleRsFillerService {

    @Autowired
    private ArticleFavoriteService articleFavoriteService;
    @Autowired
    private FollowingService followingService;

    @Override
    public ArticleRs toArticleRs(Article a){
        User author = a.getAuthor();
        return ArticleRs.builder()
                .slug(a.getSlug())
                .title(a.getTitle())
                .description(a.getDescription())
                .body(a.getBody())
                .tagList(a.getTags().stream().map(Tag::getBody).collect(Collectors.toList()))
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .favoritesCount(articleFavoriteService.getFavoritedCount(a.getId()))
                .author(ProfileRs.builder()
                        .username(author.getUsername())
                        .bio(author.getBio())
                        .image(author.getImage())
                        .build()).build();
    }

    @Override
    public ArticleRs toArticleRs(Article a, User user) {
        User author = a.getAuthor();
        ArticleRs articleRs = toArticleRs(a);
        if(user != null){
            boolean isCurrentUserFollowingAuthor = followingService.isUserFollowing(user.getId(), author.getId());
            boolean isFavorite = articleFavoriteService.isFavoritedBy(a.getId(), user.getId());
            articleRs.setFavorited(isFavorite);
            articleRs.getAuthor().setFollowing(isCurrentUserFollowingAuthor);
        }
        return articleRs;
    }


}
