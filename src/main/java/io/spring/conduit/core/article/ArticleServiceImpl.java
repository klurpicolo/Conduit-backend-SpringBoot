package io.spring.conduit.core.article;

import io.spring.conduit.core.tag.TagRepository;
import io.spring.conduit.core.user.UserRepository;
import io.spring.conduit.core.user.following.UserFollowingRepository;
import io.spring.conduit.exception.ResourceNotFoundException;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Tag;
import io.spring.conduit.model.User;
import io.spring.conduit.model.UserFollowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.spring.conduit.core.article.ArticleSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFollowingRepository userFollowingRepository;


    @Override
    public List<Article> getArticles(String tag, String author, String favoritedBy, int limit, int offset) {
        return articleRepository.findAll(
                where(hasTag(tag)
                        .and(hasAuthor(author)
                                .and(favoritedBy(favoritedBy)))),
                PageRequest.of(offset,limit)).toList();
    }

    @Override
    public List<Article> getFeedArticles(String userId, int limit, int offset) {
        List<String> userFollowings = userFollowingRepository.findByUserId(userId).stream()
                .map(UserFollowing::getFollowingUserId).collect(Collectors.toList());

        if(userFollowings.size()==0){
            return new ArrayList<>();
        }

        return articleRepository.findAll(
                where(hasAuthorIdIn(userFollowings)),
                PageRequest.of(offset, limit)).toList();

    }

    @Override
    @Transactional
    public Optional<Article> createArticle(String title, String description, String body, List<String> tags, String userId) {

        User author = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);

        Article createdArticle = articleRepository.save(
                Article.builder()
                .author(author)
                .slug(toSlug(title))
                .title(title)
                .description(description)
                .body(body)
                .tags(getPersistTags(tags))
                .build()
        );


        return Optional.of(createdArticle);
    }

    @Override
    public Optional<Article> getArticle(String slug) {
        return articleRepository.findBySlug(slug);
    }

    @Override
    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    private String toSlug(String title){
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }

    private List<Tag> getPersistTags(List<String> tags){
        List<Tag> articlesTag = new ArrayList<>();
        for(String tag : tags){
            Optional<Tag> checkTag = tagRepository.findByBody(tag);
            if(!checkTag.isPresent()){
                articlesTag.add(tagRepository.save(Tag.builder().body(tag).build()));
            } else {
                articlesTag.add(checkTag.get());
            }
        }
        return articlesTag;
    }

}
