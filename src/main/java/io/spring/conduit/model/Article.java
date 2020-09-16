package io.spring.conduit.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Data
@Entity
@Builder
public class Article {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private String userId;
    private String slug;
    private String title;
    private String description;
    private String body;
//    @OneToMany(mappedBy="article")
//    private List<Tag> tags;

    @CreationTimestamp
    private Calendar createdAt;
    @UpdateTimestamp
    private Calendar updatedAt;

}
