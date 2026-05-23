package com.collabrix.socialservice.repository;

import com.collabrix.socialservice.model.entities.Friendship;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends MongoRepository<Friendship, UUID> {

    Optional<Friendship> findByUserAAndUserB(UUID userA, UUID userB);

    List<Friendship> findByUserAOrUserB(UUID userA, UUID userB);
}
