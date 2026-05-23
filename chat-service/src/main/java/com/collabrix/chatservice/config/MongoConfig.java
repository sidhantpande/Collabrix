package com.collabrix.chatservice.config;

import com.collabrix.chatservice.repository.ConversationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = ConversationRepository.class)
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of());
    }
}
