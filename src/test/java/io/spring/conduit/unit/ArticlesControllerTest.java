package io.spring.conduit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.conduit.controller.ArticlesController;
import io.spring.conduit.core.article.ArticleRsFillerService;
import io.spring.conduit.core.article.ArticleService;
import io.spring.conduit.core.article.favorite.ArticleFavoriteService;
import io.spring.conduit.core.jwt.JwtService;
import io.spring.conduit.core.user.UserRepository;
import io.spring.conduit.core.user.UserService;
import io.spring.conduit.core.user.following.FollowingService;
import io.spring.conduit.dto.request.NewArticleRq;
import io.spring.conduit.helper.ArticleTestHelper;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Tag;
import io.spring.conduit.model.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ArticlesController.class)
class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private ArticleFavoriteService articleFavoriteService;
    @MockBean
    private ArticleRsFillerService articleRsFillerService;
    @MockBean
    private UserService userService;
    @MockBean
    private FollowingService followingService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void when_getGlobalArticleWithNoAuth_then_getGlobalArticle() throws Exception {
        //given
        User author = User.builder()
                .username("warit")
                .password("tiraw")
                .bio("mock watit bio")
                .image("mock warit image")
                .build();

        String title = "this is title 1";
        String description = "this is desc";
        String body = "this is body";
        List<String> tags = Lists.newArrayList("tag1","tag2");
        Article article1 = Article.builder()
                .id(UUID.randomUUID().toString())
                .author(author)
                .title(title)
                .description(description)
                .body(body)
                .tags(tags.stream().map(t -> Tag.builder().body(t).build()).collect(Collectors.toList()))
                .slug("this-is-title-1")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        String title2 = "this is title 2";
        String description2 = "this is desc 2";
        String body2 = "this is body2";
        List<String> tags2 = new ArrayList<>();
        String userId2 = "mockUserId2";
        tags2.add("tag1");
        Article article2 = Article.builder()
                .author(author)
                .title(title2)
                .description(description2)
                .body(body2)
                .tags(tags2.stream().map(t -> Tag.builder().body(t).build()).collect(Collectors.toList()))
                .slug("this-is-title-2")
                .favoritedByUsers(Collections.singletonList(author))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        List<Article> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);

        //when
        when(articleService.getArticles(isNull(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(articles);
        when(articleRsFillerService.toArticleRs(article1, null)).thenReturn(ArticleTestHelper.getArticleRs(article1,author, 0,true,true));
        when(articleRsFillerService.toArticleRs(article2, null)).thenReturn(ArticleTestHelper.getArticleRs(article1,author,0,true,true));

        ResultActions resultActions = mockMvc.perform(get("/articles")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articlesCount", is(2)))
                .andExpect(jsonPath("$.articles[0].title", is("this is title 1")));

    }

    @Test
    void when_createArticle_then_returnCreatedArticle() throws Exception {
        //given
        String title = "this is title 1";
        String description = "this is desc";
        String body = "this is body";
        List<String> tags = new ArrayList<>();
        String userId = "mockId1";
        tags.add("tag1");
        tags.add("tag2");
        Article createdArticle = Article.builder()
                .title(title)
                .description(description)
                .body(body)
                .tags(tags.stream().map(t -> Tag.builder().body(t).build()).collect(Collectors.toList()))
                .slug("this-is-title-1")
                .favoritedByUsers(Lists.emptyList())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        NewArticleRq newArticleRq = NewArticleRq.builder()
                .title(title)
                .description(description)
                .body(body)
                .tagList(tags).build();

        Map<String, Object> requestBody = new HashMap<String, Object>(){{
            put("article",newArticleRq);
        }};

        String token = "token";

        User mockUser = User.builder()
                .id(userId)
                .username("mockUser")
                .build();

        //when
        when(articleService.createArticle(title, description, body, tags, userId)).thenReturn(Optional.of(createdArticle));
        when(jwtService.getSubjectFromToken(token)).thenReturn(Optional.of(userId));
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(mockUser));
        when(articleRsFillerService.toArticleRs(createdArticle)).thenReturn(ArticleTestHelper.getArticleRs(createdArticle, mockUser,0,false,false));

        ResultActions resultAction = mockMvc.perform(post("/articles")
                .contentType("application/json")
                .header("Authorization", "Token " + token)
                .content(objectMapper.writeValueAsBytes(requestBody)));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title", is(title)))
                .andExpect(jsonPath("$.article.description", is(description)))
                .andExpect(jsonPath("$.article.body",is(body)))
                .andExpect(jsonPath("$.article.favorited", is(Boolean.FALSE)))
                .andExpect(jsonPath("$.article.favoritesCount",is(0)))
                .andExpect(jsonPath("$.article.author.username", is(mockUser.getUsername())));

    }
}