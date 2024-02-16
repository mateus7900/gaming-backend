package com.gaming.repositories;

import com.gaming.domain.entidade.Profile;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends Neo4jRepository<Profile, String> {
    @Query("MATCH (profile: Profile {username: $username}) RETURN profile")
    Profile getProfileByUsername(@Param("username") String username);

    @Query("MATCH (follower: Profile {username: $follower})" +
            "MATCH (followed: Profile {username: $followed})" +
            "CREATE (follower)-[:FOLLOW]->(followed)")
    void insertFollowsRelationship(@Param("follower") String follower, @Param("followed") String followed);

    @Query("MATCH (profile:Profile)-[r:LIKE]->(p:Post{postId: $postId}) " +
            "RETURN profile")
    List<Profile> queryProfileByPostLiked(@Param("postId") String postId);

    @Query("MATCH (follower: Profile)-[:FOLLOW]->(followed: Profile{ username: $username}) RETURN follower")
    List<Profile> queryFollowers(@Param("username") String username);

    @Query("MATCH (follower: Profile{username: $username})-[:FOLLOW]->(followed: Profile) RETURN followed")
    List<Profile> queryFollowing(@Param("username") String username);

    @Query("MATCH (p: Profile) " +
            "WHERE NOT p.username IN $usernames " +
            "RETURN p")
    List<Profile> getExplore(@Param("usernames") List<String> usernames);

    @Query("MATCH (follower: Profile{username: $follower})-[r:FOLLOW]->(p:Profile{username: $following}) RETURN COUNT(r) > 0")
    Optional<Boolean> relationshipExists(@Param("follower") String follower, @Param("following") String following);
}
