package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.entity.ApiError;
import com.restaurant.papricica.exceptions.EmailAlreadyExistsException;
import com.restaurant.papricica.exceptions.PhoneNumberAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getErrorCode().name(), e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ApiError> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException e) {

        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getErrorCode().name(), e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
