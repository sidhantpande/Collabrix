package com.collabrix.workspaceservice.exception;

public class CannotRemoveOwnerException extends RuntimeException {
    public CannotRemoveOwnerException() {
        super("CANNOT_REMOVE_OWNER");
    }
}
