package com.gaming.business;

import com.gaming.api.models.AuthCredentials;
import com.gaming.domain.dto.ProfileDto;
import com.gaming.domain.entidade.Profile;
import com.gaming.repositories.AuthRepository;
import com.gaming.repositories.PostRepository;
import com.gaming.repositories.ProfileRepository;
import com.gaming.utils.JwtUtil;
import com.gaming.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
public class AuthBusiness {

    private final AuthRepository authRepository;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    @Autowired
    public AuthBusiness(AuthRepository _authRepository, ProfileRepository _profileRepository, PostRepository _postRepository){
        authRepository = _authRepository;
        profileRepository = _profileRepository;
        postRepository = _postRepository;
    }

    public ProfileDto login(AuthCredentials credentials) throws AuthenticationException{
        Profile profile = authRepository.findByUsername(credentials.getUsername());

        if (PasswordUtil.ComparePasswords(credentials.getPassword(), profile.getPassword())){
            List<Profile> followers = profileRepository.queryFollowers(profile.getUsername());
            List<Profile> following = profileRepository.queryFollowing(profile.getUsername());
            profile.setPosts(postRepository.getPostsByUsername(profile.getUsername()));
            return new ProfileDto(profile, followers, following);
        }

        throw new AuthenticationException("Invalid Credentials");
    }
}
