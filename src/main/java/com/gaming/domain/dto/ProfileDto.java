package com.gaming.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.domain.entidade.Profile;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileDto {
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
    private List<PostDto> posts;

    private List<String> followers = new ArrayList<>();;
    private List<String> following = new ArrayList<>();

    public ProfileDto() {}

    public ProfileDto(Profile profile){
        this.username = profile.getUsername();
        this.password = profile.getPassword();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.age = profile.getAge();
        this.verified = profile.getVerified();
        this.description = profile.getDescription();
        this.createdAt = profile.getCreatedAt();
        this.profileImgLink = profile.getProfileImgLink();

        this.postsCount = profile.getPosts().size();
        this.followersCount = profile.getFollowers().size();
        this.followingCount = profile.getFollowing().size();

        this.posts = PostDto.createDtoList(profile.getPosts());
    }

    public ProfileDto(Profile profile, List<Profile> followers, List<Profile> following){
        this.username = profile.getUsername();
        this.password = profile.getPassword();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.age = profile.getAge();
        this.verified = profile.getVerified();
        this.description = profile.getDescription();
        this.createdAt = profile.getCreatedAt();
        this.profileImgLink = profile.getProfileImgLink();
        this.followersCount = followers.size();
        this.followingCount = following.size();
        this.postsCount = profile.getPosts().size();
        this.posts = PostDto.createDtoList(profile.getPosts());

        for (Profile p : followers){
            this.followers.add(p.getUsername());
        }

        for (Profile p: following){
            this.following.add(p.getUsername());
        }
    }

    public Profile toEntity(){
        return new ModelMapper().map(this, Profile.class);
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static List<ProfileDto> createDtoList(List<Profile> profiles){
        List<ProfileDto> dtoList = new ArrayList<>();
        for(Profile profile : profiles){
            dtoList.add(new ProfileDto(profile));
        }

        return dtoList;
    }
}
