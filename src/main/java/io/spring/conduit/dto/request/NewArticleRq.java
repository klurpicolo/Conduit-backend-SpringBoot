package io.spring.conduit.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("article")
public class NewArticleRq {
    @NotBlank(message = "Title Should not be blank")
    private String title;
    @NotBlank(message = "Description Should not be blank")
    private String description;
    @NotBlank(message = "Body Should not be blank")
    private String body;
    @JsonProperty("tagList")
    private List<String> tagList;
}
