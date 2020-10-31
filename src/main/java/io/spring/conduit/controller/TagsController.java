package io.spring.conduit.controller;


import io.spring.conduit.core.tag.TagService;
import io.spring.conduit.dto.response.TagsRs;
import io.spring.conduit.model.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(path = "/tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity getTags(){
        List<Tag> tags = tagService.getTags();
        return ResponseEntity.ok(
                TagsRs.builder()
                    .tags(tags.stream().map(Tag::getBody).collect(Collectors.toList())).build());
    }
}
