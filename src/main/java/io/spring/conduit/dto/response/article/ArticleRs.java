package io.spring.conduit.dto.response.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.UserWithTokenRs;
import io.spring.conduit.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRs {
    private String slug;
    private String title;
    private String description;
    private String body;
    @JsonProperty("tagList")
    private List<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean favorited;
    private int favoritesCount;
    @JsonProperty("author")
    private ProfileRs author;

    public static Map<String, Object> getArticleResponse(ArticleRs articleRs) {
        return new HashMap<String, Object>() {{
            put("article", articleRs);
        }};
    }

}
