package com.restaurant.papricica.exceptions;

public class PhoneNumberAlreadyExistsException extends AppException{

    public PhoneNumberAlreadyExistsException() {
        super(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS, "The provided phone number is already in use. Please use a different phone number.");
    }

}
