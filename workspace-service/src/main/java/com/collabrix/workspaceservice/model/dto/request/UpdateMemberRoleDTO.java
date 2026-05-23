package com.collabrix.workspaceservice.model.dto.request;

import com.collabrix.workspaceservice.model.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMemberRoleDTO {

    @NotNull(message = "Role is required")
    private WorkspaceRole role;
}
