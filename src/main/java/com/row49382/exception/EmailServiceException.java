package com.row49382.exception;

public class EmailServiceException extends Exception {
    private static final long serialVersionUID = 6908812516953993654L;

    public EmailServiceException(String message) {
        super(message);
    }

    public EmailServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
