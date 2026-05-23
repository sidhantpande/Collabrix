package com.collabrix.workspaceservice.model.dto.response;

import com.collabrix.workspaceservice.model.entities.WorkspaceMember;
import com.collabrix.workspaceservice.model.enums.WorkspaceRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WorkspaceMemberResponseDTO {

    private UUID id;
    private UUID workspaceId;
    private UUID authId;
    private WorkspaceRole role;
    private LocalDateTime joinedAt;

    public static WorkspaceMemberResponseDTO fromEntity(WorkspaceMember member) {
        return WorkspaceMemberResponseDTO.builder()
                .id(member.getId())
                .workspaceId(member.getWorkspace().getId())
                .authId(member.getAuthId())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
