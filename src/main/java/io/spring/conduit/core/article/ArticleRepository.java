package io.spring.conduit.core.article;

import io.spring.conduit.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

//    @Query(value = "select a from Article a where a.tags = :tag")
//    List<Article> findArticle(@Param("tag") String tag);

//    @Query(value = "select a from Article a " +
//            "inner join Tag t" +
//            "on a.")
    List<Article> findByTags_Body(String tag);
    Optional<Article> findBySlug(String slug);
}
