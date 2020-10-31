package io.spring.conduit.core.article;

import io.spring.conduit.model.Article;
import io.spring.conduit.model.ArticleFarvorite;
import io.spring.conduit.model.Tag;
import io.spring.conduit.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleSpecifications {

    public static Specification<Article> hasTag(String tag){
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(tag == null){
                return null;
            } else {
                Join<Article, Tag> tags = root.join("tags");
                return criteriaBuilder.equal(tags.get("body"),tag);
            }
        };
    }

    public static Specification<Article> hasAuthor(String username){
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(username == null){
                return null;
            } else {
                Join<Article, User> user = root.join("author");
                return criteriaBuilder.equal(user.get("username"),username);
            }
        };
    }

    public static Specification<Article> hasAuthorIdIn(List<String> authorIds){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(authorIds.size()==0){
                return null;
            } else {
                Join<Article, User> user = root.join("author");
                predicates.add(user.get("id").in(authorIds));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    //Todo 3 table join and filter(where)
    public static Specification<Article> favoritedBy(String username){
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(username == null){
                return null;
            } else {
                Join<Article, User> favorites = root.join("favoritedByUsers");
                return criteriaBuilder.equal(favorites.get("username"),username);
            }
        };
    }

}
