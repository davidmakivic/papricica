package com.restaurant.papricica.exceptions;

public class EmailAlreadyExistsException extends AppException{

    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, "The provided email address is already in use. Please use a different email address.");
    }

}
