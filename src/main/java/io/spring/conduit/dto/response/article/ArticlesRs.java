package io.spring.conduit.dto.response.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.model.Article;
import io.spring.conduit.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesRs {

    @JsonProperty("articles")
    private List<ArticleRs> articles;
    @JsonProperty("articlesCount")
    private int articlesCount;

}
