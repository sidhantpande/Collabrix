package com.collabrix.workspaceservice.repository;

import com.collabrix.workspaceservice.model.entities.WorkspaceInvite;
import com.collabrix.workspaceservice.model.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceInviteRepository extends JpaRepository<WorkspaceInvite, UUID> {
    boolean existsByWorkspaceIdAndTargetAuthIdAndStatus(UUID workspaceId, UUID targetAuthId, InviteStatus status);
    Optional<WorkspaceInvite> findByIdAndTargetAuthId(UUID inviteId, UUID targetAuthId);
    Optional<WorkspaceInvite> findByIdAndTargetAuthIdAndStatus(UUID inviteId, UUID targetAuthId, InviteStatus status);
}
