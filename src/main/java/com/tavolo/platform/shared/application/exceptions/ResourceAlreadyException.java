package com.tavolo.platform.shared.application.exceptions;

public class ResourceAlreadyException extends RuntimeException {
    public ResourceAlreadyException(String message) {
        super(message);
    }
}
