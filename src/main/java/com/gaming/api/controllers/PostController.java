package com.gaming.api.controllers;

import com.gaming.api.models.LikeModel;
import com.gaming.api.models.PostModel;
import com.gaming.api.models.ReplyModel;
import com.gaming.business.PostBusiness;
import com.gaming.domain.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {

    private final PostBusiness postBusiness;

    @Autowired
    public PostController(PostBusiness postBusiness){
        this.postBusiness = postBusiness;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostModel post){
        try{
            return ResponseEntity.ok(postBusiness.insertPost(post.getAuthor(), post.getContent()));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<?> replyPost(@RequestBody ReplyModel reply){
        try{
            return ResponseEntity.ok(postBusiness.insertReplyPost(reply.getAuthor(), reply.getContent(), reply.getInReplyAt()));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @PutMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody LikeModel like){
        try{
            return ResponseEntity.ok(postBusiness.likePost(like.getUsername(), like.getPostId()));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getPostsByUsername(@RequestParam(name = "username") String username){
        try{
            return ResponseEntity.ok(postBusiness.getPosts(username));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @GetMapping("/timeline")
    public ResponseEntity<?> getTimeline(@RequestParam(name = "username") String username){
        try{
            return ResponseEntity.ok(postBusiness.getTimeline(username));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrending(){
        try{
            return ResponseEntity.ok(postBusiness.getTrendingTopics());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ex.getMessage());
        }
    }
}