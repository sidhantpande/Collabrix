package com.collabrix.socialservice.config;

import com.collabrix.socialservice.repository.CommentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = CommentRepository.class)
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of());
    }
}
