package com.gaming.api.controllers;

import com.gaming.api.models.FollowModel;
import com.gaming.business.ProfileBusiness;
import com.gaming.domain.dto.ProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/profile")
public class ProfileController {

    private final ProfileBusiness profileBusiness;

    @Autowired
    public ProfileController(ProfileBusiness _profileBusiness){
        profileBusiness = _profileBusiness;
    }

    @PostMapping
    public ResponseEntity<ProfileDto> createProfile(@RequestBody ProfileDto profileDto){
        try{
            return ResponseEntity.ok(profileBusiness.createProfile(profileDto));
        } catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getProfileByUsername(@RequestParam(name = "username") String username){
        try{
            return ResponseEntity.ok(profileBusiness.getProfile(username));
        } catch (Exception ex){
            System.out.println(ex);
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @PostMapping("/add-follow")
    public ResponseEntity<?> createFollowRelationship(@RequestBody FollowModel follow){
        try{
            return ResponseEntity.ok(profileBusiness.createFollowsRelationship(follow.getFollowerName(), follow.getFollowedName()));
        } catch (Exception ex){
            System.out.println(ex);
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @GetMapping("/explore")
    public ResponseEntity<?> getExplore(@RequestParam(name = "username") String username){
        try{
            return ResponseEntity.ok(profileBusiness.getExplore(username));
        } catch (Exception ex){
            System.out.println(ex);
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

//    @GetMapping("/likes")
//    public ResponseEntity<?> getWhoLikesAPost(@RequestParam String postId){
//        try{
//            return ResponseEntity.ok(p)
//        }
//    }

}
