package com.collabrix.authservice.exceptions;

import com.collabrix.authservice.model.enums.ErrorTP;

public class GenericAplicationException extends RuntimeException {
    public GenericAplicationException(ErrorTP error) {
        super(String.valueOf(error));
    }
}
