package io.spring.conduit.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("article")
public class UpdatedArticleRq {
    private String title = "";
    private String description = "";
    private String body = "";
}
