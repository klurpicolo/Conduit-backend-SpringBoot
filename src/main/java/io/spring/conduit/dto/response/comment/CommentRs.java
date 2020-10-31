package io.spring.conduit.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.conduit.dto.response.ProfileRs;
import io.spring.conduit.dto.response.article.ArticleRs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("comment")
public class CommentRs {

    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private String body;
    @JsonProperty("author")
    private ProfileRs author;

    public static Map<String, Object> getCommentResponse(CommentRs commentRs) {
        return new HashMap<String, Object>() {{
            put("comment", commentRs);
        }};
    }

}
