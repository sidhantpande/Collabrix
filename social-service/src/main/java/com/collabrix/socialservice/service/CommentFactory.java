package com.collabrix.socialservice.service;

import com.collabrix.socialservice.client.TaskServiceClient;
import com.collabrix.socialservice.model.entities.Comment;
import com.collabrix.socialservice.security.AuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentFactory {

    public Comment create(
            TaskServiceClient.TaskCommentContextResponse taskContext,
            AuthenticatedUser authenticatedUser,
            String content,
            List<String> mentions
    ) {
        return Comment.create(
                taskContext.taskId(),
                taskContext.workspaceId(),
                authenticatedUser.authId(),
                authenticatedUser.username(),
                content,
                mentions
        );
    }
}
