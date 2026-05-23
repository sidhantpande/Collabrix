package com.collabrix.userservice.exceptions;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException() {
        super("USER_PROFILE_NOT_FOUND");
    }
}
