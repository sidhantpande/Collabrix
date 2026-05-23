package com.collabrix.workspaceservice.exception;

public class UserServiceUnavailableException extends RuntimeException {
    public UserServiceUnavailableException() {
        super("USER_SERVICE_UNAVAILABLE");
    }
}
