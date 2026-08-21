package com.restaurant.papricica.exceptions;

public abstract class AppException extends RuntimeException{

    private final ErrorCode errorCode;

    protected AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
