package com.gaming.repositories;

import com.gaming.domain.entidade.Post;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends Neo4jRepository<Post, String> {

    Post findByPostId(String postId);

    @Query("MATCH (profile: Profile {username = {username}}) -[:POSTED]-> (post) RETURN post")
    List<Post> getAllPostsFromUser(@Param("username") String username);

    @Query("MATCH (profile:Profile {username: $authorName})" +
            "CREATE (post:Post {" +
            "content: $content, " +
            "likes: $likes, " +
            "replies: $replies, " +
            "inReplyAt: $inReplyAt, " +
            "authorName: $authorName, " +
            "postId: $postId, " +
            "createdAt: $createdAt})" +
            "CREATE (profile)-[:POSTED]->(post)" +
            "RETURN post")
    void insertNewPost(
            @Param("authorName") String authorName,
            @Param("content") String content,
            @Param("likes") Integer likes,
            @Param("replies") Integer replies,
            @Param("inReplyAt") String inReplyAt,
            @Param("postId") String postId,
            @Param("createdAt") Timestamp createdAt
    );

    @Query("MATCH (profile:Profile {username: $username})" +
            "MATCH (post:Post {postId: $postId})" +
            "CREATE (profile)-[l:LIKE]->(post)")
    void likePost(@Param("username") String username, @Param("postId") String postId);

    @Query("MATCH (profile: Profile{username: $username})" +
            "MATCH (replyPost: Post{postId: $replyAtId})" +
            "CREATE (post: Post{authorName: $username, content: $content, createdAt: $createdAt, likes: 0, replies:0, postId: $postId})" +
            "CREATE (profile)-[:POSTED]->(post)-[:IN_REPLY]->(replyPost)" +
            "return post;")
    Optional<Post> queryPostByAuthorAndPostId(
            @Param("username") String username,
            @Param("replyAtId") String replyAtId,
            @Param("content") String content,
            @Param("createdAt") Timestamp createdAt,
            @Param("postId") String postId
    );

    @Query("MATCH (parent:Post {postId: $postId})<-[:IN_REPLY]-(reply:Post)" +
            "RETURN reply;")
    List<Post> queryPostsByPostId(@Param("postId") String postId);

    @Query("MATCH (p: Profile {username: $username})-[:POSTED]->(post: Post) RETURN post")
    List<Post> getPostsByUsername(@Param("username") String username);

    @Query("MATCH (p:Profile)-[:POSTED]->(post:Post) " +
            "WHERE p.username IN $usernames " +
            "RETURN post " +
            "ORDER BY post.createdAt ASC")
    List<Post> getTimeline(@Param("usernames") List<String> usernames);

    @Query("MATCH (post:Post) " +
            "WHERE post.createdAt >= $oneWeekAgo AND " +
            "      post.createdAt <= $today " +
            "RETURN post")
    List<Post> getTopicsLastWeek(@Param("oneWeekAgo") Timestamp oneWeekAgo, @Param("today") Timestamp today);
}
