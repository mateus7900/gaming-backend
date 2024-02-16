package com.gaming.api.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.gaming.repositories")
@EntityScan(basePackages = "com.gaming.domain")
@EnableTransactionManagement
public class GamingConfiguration {

    @Bean
    public SessionFactory sessionFactory(){
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder()
                .uri("bolt://0.0.0.0:7687")
                .credentials("neo4j", "password").build();
        return new SessionFactory(configuration, "com.gaming.domain");
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        return new Neo4jTransactionManager();
    }
}