package com.collabrix.socialservice.service;

import com.collabrix.socialservice.client.AuthServiceClient;
import com.collabrix.socialservice.model.entities.FriendRequest;
import com.collabrix.socialservice.security.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestFactory {

    public FriendRequest create(
            AuthenticatedUser requester,
            AuthServiceClient.AuthUserSummaryResponse target
    ) {
        return FriendRequest.create(
                requester.authId(),
                requester.username(),
                target.authId(),
                target.username()
        );
    }
}
