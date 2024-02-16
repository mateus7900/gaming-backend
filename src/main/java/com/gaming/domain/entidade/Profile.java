package com.gaming.domain.entidade;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private Boolean verified;
    private String description;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postsCount;
    private Timestamp createdAt;
    private String profileImgLink;

    @Relationship(type = "POSTED")
    private List<Post> posts = new ArrayList<>();

    @Relationship(type = "FOLLOWS")
    private Set<Profile> following = new HashSet<>();

    @Relationship(type = "FOLLOWS", direction = Relationship.INCOMING)
    private Set<Profile> followers = new HashSet<>();
}
