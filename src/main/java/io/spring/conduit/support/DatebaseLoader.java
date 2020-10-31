package io.spring.conduit.support;

import io.spring.conduit.core.article.ArticleRepository;
import io.spring.conduit.core.article.favorite.ArticleFavoriteRepository;
import io.spring.conduit.core.comment.CommentRepository;
import io.spring.conduit.core.tag.TagRepository;
import io.spring.conduit.core.user.UserRepository;
import io.spring.conduit.core.user.following.UserFollowingRepository;
import io.spring.conduit.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(
        value="conduit.initData",
        havingValue = "true",
        matchIfMissing = false)
public class DatebaseLoader implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleFavoriteRepository articleFavoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserFollowingRepository userFollowingRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<User> mockUsers = getMockUser();
        userRepository.saveAll(mockUsers);

        List<UserFollowing> userFollowings = getMockFollowing(mockUsers);
        userFollowingRepository.saveAll(userFollowings);

        List<Tag> mockTags = getMockTags();
        tagRepository.saveAll(mockTags);

        List<Article> mockArticles = getMockArticle(mockTags, mockUsers);
        articleRepository.saveAll(mockArticles);

        List<ArticleFarvorite> mockfarvorites = getMockFavorites(mockArticles, mockUsers);
        articleFavoriteRepository.saveAll(mockfarvorites);

        List<Comment> mockComments = getMockComment(mockArticles, mockUsers);
        commentRepository.saveAll(mockComments);

    }

    private List<ArticleFarvorite> getMockFavorites(List<Article> articles, List<User> users) {
        List<ArticleFarvorite> farvorites = new ArrayList<>();
        User user1 = users.stream().filter(user -> user.getUsername().equals("warit")).findAny().get();
        Article article1 = articles.stream().filter(article -> article.getTitle().equals("this is title")).findAny().get();
        farvorites.add(ArticleFarvorite.builder().userId(user1.getId()).articleId(article1.getId()).build());

        User user2 = users.stream().filter(user -> user.getUsername().equals("jane")).findAny().get();
        farvorites.add(ArticleFarvorite.builder().userId(user2.getId()).articleId(article1.getId()).build());
        return farvorites;
    }

    private List<UserFollowing> getMockFollowing(List<User> users) {
        List<UserFollowing> mockUserFollowings = new ArrayList<>();
        User user1 = users.stream().filter(user -> user.getUsername().equals("warit")).findAny().get();
        User user3 = users.stream().filter(user -> user.getUsername().equals("johnjacob")).findAny().get();

        UserFollowing rel1 = UserFollowing.builder()
                .userId(user1.getId())
                .followingUserId(user3.getId()).build();
        mockUserFollowings.add(rel1);
        return mockUserFollowings;
    }

    private List<Tag> getMockTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(Tag.builder().body("dragons").build());
        tags.add(Tag.builder().body("tag2").build());
        return tags;
    }


    private List<User> getMockUser() {
        List<User> users = new ArrayList<>();
        User user1 = User.builder()
                .username("warit")
                .bio("default bio")
                .image("default image")
                .email("warit.b@gmail.com")
                .password("password").build();
        users.add(user1);

        User user2 = User.builder()
                .username("jane")
                .bio("default bio")
                .image("default image")
                .email("dao.b@gmail.com")
                .password("passworddao").build();
        users.add(user2);

        User user3 = User.builder()
                .username("johnjacob")
                .bio("default bio")
                .image("default image")
                .email("johnjacob.b@gmail.com")
                .password("passworddao").build();
        users.add(user3);

        return users;
    }

    private List<Article> getMockArticle(List<Tag> tags, List<User> users) {
        List<Article> articles = new ArrayList<>();
        User user1 = users.stream().filter(user -> user.getUsername().equals("warit")).findAny().get();
        User user3 = users.stream().filter(user -> user.getUsername().equals("johnjacob")).findAny().get();
        Article articleOfUser1 = Article.builder()
                .author(user1)
                .title("this is title")
                .body("this is body")
                .description("desc")
                .slug("slugsss")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .tags(Collections.singletonList(tags.stream().filter(tag -> tag.getBody().equals("dragons")).findAny().get()))
                .build();
        articles.add(articleOfUser1);
        Article articleOfUser1_2 = Article.builder()
                .author(user1)
                .title("this is title ar2")
                .body("this is body ar2")
                .description("desc ar2")
                .slug("slugsss ar2")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .tags(Collections.singletonList(tags.stream().filter(tag -> tag.getBody().equals("tag2")).findAny().get()))
                .build();
        articles.add(articleOfUser1_2);
        Article articleOfUser3_1 = Article.builder()
                .author(user3)
                .title("this is title ar3")
                .body("this is body ar3")
                .description("desc ar3")
                .slug("slugsssASDAD ar23")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .tags(Collections.singletonList(tags.stream().filter(tag -> tag.getBody().equals("dragons")).findAny().get()))
                .build();
        articles.add(articleOfUser3_1);

        return articles;
    }

    private List<Comment> getMockComment(List<Article> articles, List<User> users){
        Article article1 = articles.stream().filter(article -> article.getSlug().equals("slugsss")).findAny().get();
        User user2 = users.stream().filter(user -> user.getUsername().equals("jane")).findAny().get();
        Comment commentInArticle1OfUser2 = Comment.builder()
                .article(article1)
                .body("Dao comment on Warit's article.")
                .commenter(user2)
                .build();

        return Collections.singletonList(commentInArticle1OfUser2);
    }
}
