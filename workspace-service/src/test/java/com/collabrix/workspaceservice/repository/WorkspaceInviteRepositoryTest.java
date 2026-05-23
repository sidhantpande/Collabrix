package com.collabrix.workspaceservice.repository;

import com.collabrix.workspaceservice.model.entities.Workspace;
import com.collabrix.workspaceservice.model.entities.WorkspaceInvite;
import com.collabrix.workspaceservice.model.enums.InviteStatus;
import com.collabrix.workspaceservice.testsupport.AbstractPostgresWorkspaceServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static com.collabrix.workspaceservice.testsupport.WorkspaceServiceTestFixtures.workspace;
import static com.collabrix.workspaceservice.testsupport.WorkspaceServiceTestFixtures.workspaceInvite;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WorkspaceInviteRepositoryTest extends AbstractPostgresWorkspaceServiceTest {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceInviteRepository workspaceInviteRepository;

    @Test
    void existsByWorkspaceIdAndTargetAuthIdAndStatus_pendingInviteExists_returnsTrue() {
        UUID inviter = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        Workspace candidate = workspace(inviter);
        candidate.setId(null);
        Workspace workspace = workspaceRepository.save(candidate);
        WorkspaceInvite invite = workspaceInvite(workspace, target, inviter, InviteStatus.PENDING);
        invite.setId(null);
        workspaceInviteRepository.save(invite);

        assertThat(workspaceInviteRepository.existsByWorkspaceIdAndTargetAuthIdAndStatus(workspace.getId(), target, InviteStatus.PENDING))
                .isTrue();
    }

    @Test
    void findByIdAndTargetAuthId_matchingInvite_returnsInvite() {
        UUID inviter = UUID.randomUUID();
        UUID target = UUID.randomUUID();
        Workspace candidate = workspace(inviter);
        candidate.setId(null);
        Workspace workspace = workspaceRepository.save(candidate);
        WorkspaceInvite inviteCandidate = workspaceInvite(workspace, target, inviter, InviteStatus.PENDING);
        inviteCandidate.setId(null);
        WorkspaceInvite invite = workspaceInviteRepository.save(inviteCandidate);

        assertThat(workspaceInviteRepository.findByIdAndTargetAuthId(invite.getId(), target))
                .isPresent()
                .get()
                .extracting(WorkspaceInvite::getId)
                .isEqualTo(invite.getId());
    }
}
