package com.gaming.repositories;

import com.gaming.domain.entidade.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends Neo4jRepository<Profile, String> {
    Profile findByUsername(String username);
}
