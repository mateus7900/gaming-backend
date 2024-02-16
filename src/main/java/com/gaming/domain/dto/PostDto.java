package com.gaming.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.domain.entidade.Post;
import com.gaming.domain.entidade.Profile;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String postId;
    private String content;
    private Integer likes;
    private Integer replies;
    private String inReplyAt;
    private Timestamp createdAt;
    private String authorName;

    public PostDto() {}

    public PostDto(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.likes = post.getLikes();
        this.replies = post.getReplies();
        this.createdAt = post.getCreatedAt();
        this.inReplyAt = post.getInReplyAt();
        this.authorName = post.getAuthorName();
        this.postId = post.getPostId();
    }

    public PostDto(Post post, String username){
        this.id = post.getId();
        this.content = post.getContent();
        this.likes = post.getLikes();
        this.replies = post.getReplies();
        this.createdAt = post.getCreatedAt();
        this.inReplyAt = post.getInReplyAt();
        this.authorName = username;
        this.postId = post.getPostId();
    }

    public Post toEntity() {
        return new ModelMapper().map(this, Post.class);
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static List<PostDto> createDtoList(List<Post> posts){
        List<PostDto> dtoList = new ArrayList<>();
        for(Post post : posts){
            dtoList.add(new PostDto(post));
        }

        return dtoList;
    }

    public static List<PostDto> createDtoList(List<Post> posts, Profile author){
        author.setPosts(null);
        List<PostDto> dtoList = new ArrayList<>();
        for (Post post : posts){
            post.setAuthor(author);
            dtoList.add(new PostDto(post));
        }

        return dtoList;
    }
}
