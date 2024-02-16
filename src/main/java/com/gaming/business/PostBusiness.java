package com.gaming.business;

import com.gaming.domain.dto.PostDto;
import com.gaming.domain.dto.TrendingDto;
import com.gaming.domain.entidade.Post;
import com.gaming.domain.entidade.Profile;
import com.gaming.repositories.PostRepository;
import com.gaming.repositories.ProfileRepository;
import com.gaming.utils.IdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostBusiness {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public PostBusiness(PostRepository _postRepository, ProfileRepository _profileRepository){
        postRepository = _postRepository;
        profileRepository = _profileRepository;
    }


    @Transactional
    public PostDto insertPost(String username, String content){
        Post post = getPost(username, content, null);

        postRepository.insertNewPost(
                post.getAuthorName(),
                post.getContent(),
                post.getLikes(),
                post.getReplies(),
                post.getInReplyAt(),
                post.getPostId(),
                post.getCreatedAt()
        );

        return new PostDto(post);
    }

    @Transactional
    public Boolean likePost(String username, String postId){
        try {
            postRepository.likePost(username, postId);

            Post post = postRepository.findByPostId(postId);
            post.setLikes(post.getLikes() + 1);

            postRepository.save(post, 0);
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Transactional
    public PostDto insertReplyPost(String username, String content, String inReplyAt){
        Post post = getPost(username, content, inReplyAt);

        Optional<Post> replyPost = postRepository.queryPostByAuthorAndPostId(post.getAuthorName(), inReplyAt, content, post.getCreatedAt(), post.getPostId());


        return replyPost.isPresent() ? new PostDto(replyPost.get(), username) : new PostDto();
    }

    public List<PostDto> getPosts(String username){
        Profile author = profileRepository.getProfileByUsername(username);
        List<Post> posts = postRepository.getPostsByUsername(username);
        return PostDto.createDtoList(posts, author);
    }

    public List<PostDto> getTimeline(String username) throws Exception {
        try{
            List<String> followingNames = getFollowingNames(profileRepository.queryFollowing(username));
            List<Post> timeline = postRepository.getTimeline(followingNames);
            return PostDto.createDtoList(mapAuthorInPost(timeline));
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    private Post getPost(String username, String content, String inReplyAt){
        Profile author = profileRepository.getProfileByUsername(username);

        Post post = new Post();
        post.setAuthor(author);
        post.setAuthorName(author.getUsername());
        post.setContent(content);
        post.setPostId(IdGeneratorUtil.getNumericString());
        post.setLikes(0);
        post.setReplies(0);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return post;
    }

    private List<String> getFollowingNames(List<Profile> profiles){
        List<String> nameList = new ArrayList<>();

        for(Profile profile : profiles){
            nameList.add(profile.getUsername());
        }

        return nameList;
    }

    public List<TrendingDto> getTrendingTopics(){
        Timestamp oneWeekAgo = Timestamp.valueOf(LocalDateTime.now().minusWeeks(1));
        Timestamp today = Timestamp.valueOf(LocalDateTime.now());

        List<Post> posts = postRepository.getTopicsLastWeek(oneWeekAgo, today);

        Map<String, Integer> wordOccurrences = new HashMap<>();

        for (Post post : posts){
            String content = post.getContent().toLowerCase();
            String[] words = content.split("\\s+");

            for (String word : words) {
                if (word.length() > 4) {
                    wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1);
                }
            }
        }

        List<TrendingDto> sortedEntries = wordOccurrences.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> new TrendingDto(entry.getKey(), entry.getValue()))
                .toList();


        return sortedEntries.subList(0, Math.min(sortedEntries.size(), 5));
    }

    private List<Post> mapAuthorInPost(List<Post> timeline){
        List<Post> postList = new ArrayList<>();

        for (Post post : timeline){
            Profile author = profileRepository.getProfileByUsername(post.getAuthorName());
            author.setPosts(null);
            post.setAuthor(author);

            postList.add(post);
        }

        return postList;
    }
}
