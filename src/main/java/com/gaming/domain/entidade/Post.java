package com.gaming.domain.entidade;

import com.gaming.utils.converters.Converters;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.sql.Timestamp;

@Getter
@Setter
@NodeEntity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String postId;
    private String content;
    private Integer likes;
    private Integer replies;
    private String inReplyAt;

    @Convert(Converters.TimestampStringConverter.class)
    private Timestamp createdAt;
    private String authorName;

    @Relationship(type = "POSTED", direction = Relationship.INCOMING)
    private Profile author;
}
