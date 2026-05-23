package com.collabrix.socialservice.service;

import com.collabrix.socialservice.client.TaskServiceClient;
import com.collabrix.socialservice.client.WorkspaceServiceClient;
import com.collabrix.socialservice.exception.SocialServiceException;
import com.collabrix.socialservice.model.dto.request.CreateCommentRequest;
import com.collabrix.socialservice.model.dto.request.UpdateCommentRequest;
import com.collabrix.socialservice.model.dto.response.CommentResponse;
import com.collabrix.socialservice.model.entities.Comment;
import com.collabrix.socialservice.model.enums.WorkspaceRole;
import com.collabrix.socialservice.repository.CommentRepository;
import com.collabrix.socialservice.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskServiceClient taskServiceClient;
    private final WorkspaceServiceClient workspaceServiceClient;
    private final MentionService mentionService;
    private final CommentFactory commentFactory;
    private final SocialNotificationService socialNotificationService;

    public CommentService(
            CommentRepository commentRepository,
            TaskServiceClient taskServiceClient,
            WorkspaceServiceClient workspaceServiceClient,
            MentionService mentionService,
            CommentFactory commentFactory,
            SocialNotificationService socialNotificationService
    ) {
        this.commentRepository = commentRepository;
        this.taskServiceClient = taskServiceClient;
        this.workspaceServiceClient = workspaceServiceClient;
        this.mentionService = mentionService;
        this.commentFactory = commentFactory;
        this.socialNotificationService = socialNotificationService;
    }

    public CommentResponse createComment(UUID taskId, AuthenticatedUser authenticatedUser, CreateCommentRequest request) {
        TaskServiceClient.TaskCommentContextResponse taskContext = taskServiceClient.getTaskContext(taskId);
        workspaceServiceClient.requireMembership(taskContext.workspaceId(), authenticatedUser.authId());

        List<MentionService.ResolvedMention> mentions =
                mentionService.resolveMentions(request.getContent(), taskContext.workspaceId());
        Comment comment = commentFactory.create(
                taskContext,
                authenticatedUser,
                request.getContent().trim(),
                mentions.stream().map(MentionService.ResolvedMention::username).toList()
        );

        Comment savedComment = commentRepository.save(comment);

        socialNotificationService.publishCommentCreated(savedComment, taskContext, authenticatedUser);
        socialNotificationService.publishMentions(savedComment, taskContext, authenticatedUser, mentions);

        return CommentResponse.fromEntity(savedComment);
    }

    public Page<CommentResponse> listComments(UUID taskId, AuthenticatedUser authenticatedUser, int page, int size) {
        TaskServiceClient.TaskCommentContextResponse taskContext = taskServiceClient.getTaskContext(taskId);
        workspaceServiceClient.requireMembership(taskContext.workspaceId(), authenticatedUser.authId());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return commentRepository.findByTaskId(taskId, pageable)
                .map(CommentResponse::fromEntity);
    }

    public CommentResponse updateComment(UUID commentId, AuthenticatedUser authenticatedUser, UpdateCommentRequest request) {
        Comment comment = getRequiredComment(commentId);
        workspaceServiceClient.requireMembership(comment.getWorkspaceId(), authenticatedUser.authId());

        if (!comment.getAuthorId().equals(authenticatedUser.authId())) {
            throw SocialServiceException.accessDenied();
        }
        if (comment.isDeleted()) {
            throw SocialServiceException.invalidCommentState();
        }

        List<MentionService.ResolvedMention> mentions =
                mentionService.resolveMentions(request.getContent(), comment.getWorkspaceId());
        comment.edit(
                request.getContent().trim(),
                mentions.stream().map(MentionService.ResolvedMention::username).toList()
        );
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    public void deleteComment(UUID commentId, AuthenticatedUser authenticatedUser) {
        Comment comment = getRequiredComment(commentId);
        WorkspaceRole role = workspaceServiceClient.requireMembership(comment.getWorkspaceId(), authenticatedUser.authId());

        boolean canDelete = comment.getAuthorId().equals(authenticatedUser.authId())
                || role == WorkspaceRole.OWNER
                || role == WorkspaceRole.ADMIN;

        if (!canDelete) {
            throw SocialServiceException.accessDenied();
        }
        if (comment.isDeleted()) {
            return;
        }

        comment.softDelete();
        commentRepository.save(comment);
    }

    private Comment getRequiredComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(SocialServiceException::commentNotFound);
    }
}
