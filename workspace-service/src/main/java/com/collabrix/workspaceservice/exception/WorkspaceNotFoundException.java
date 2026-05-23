package com.collabrix.workspaceservice.exception;

public class WorkspaceNotFoundException extends RuntimeException {
    public WorkspaceNotFoundException() {
        super("WORKSPACE_NOT_FOUND");
    }
}
