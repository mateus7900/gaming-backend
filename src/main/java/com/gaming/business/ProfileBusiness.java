package com.gaming.business;

import com.gaming.domain.dto.ProfileDto;
import com.gaming.domain.entidade.Profile;
import com.gaming.repositories.PostRepository;
import com.gaming.repositories.ProfileRepository;
import com.gaming.utils.IdGeneratorUtil;
import com.gaming.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileBusiness {

    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    @Autowired
    public ProfileBusiness(ProfileRepository _profileRepository, PostRepository _postRepository){
        profileRepository = _profileRepository;
        postRepository = _postRepository;
    }

    public ProfileDto createProfile(ProfileDto profileDto){
        Profile profile = profileDto.toEntity();
        profile.setPassword(PasswordUtil.Encrypt(profileDto.getPassword()));
        profile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        profile.setUserId(IdGeneratorUtil.getNumericString());

        Profile insertedProfile = profileRepository.save(profile, 0);
        insertedProfile.setPassword(null);

        return new ProfileDto(insertedProfile);
    }

    public ProfileDto getProfile(String username){
        Profile profile = profileRepository.getProfileByUsername(username);

        if (profile == null) return null;

        profile.setPassword(null);

        List<Profile> followers = profileRepository.queryFollowers(username);
        List<Profile> following = profileRepository.queryFollowing(username);
        profile.setPosts(postRepository.getPostsByUsername(username));

        return new ProfileDto(profile, followers, following);
    }

    public Boolean createFollowsRelationship(String followerName, String followedName){
        try{
            Optional<Boolean> relationshipExists = profileRepository.relationshipExists(followerName, followedName);
            if (relationshipExists.isPresent() && !relationshipExists.get()){
                profileRepository.insertFollowsRelationship(followerName, followedName);
                return true;
            }
            return false;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public List<ProfileDto> getExplore(String username){
        List<String> followingNames = getFollowingNames(profileRepository.queryFollowing(username));
        followingNames.add(username);
        return ProfileDto.createDtoList(profileRepository.getExplore(followingNames));
    }

    private List<String> getFollowingNames(List<Profile> profiles){
        List<String> nameList = new ArrayList<>();

        for(Profile profile : profiles){
            nameList.add(profile.getUsername());
        }

        return nameList;
    }

//    public List<ProfileDto> findProfilesLikesAPost(String postId){
//        try{
//            return ProfileDto.createDtoList(profileRepository.queryProfileByPostLiked(postId));
//        } catch (Exception ex){
//            ex.printStackTrace();
//            throw new
//        }
//    }
}
