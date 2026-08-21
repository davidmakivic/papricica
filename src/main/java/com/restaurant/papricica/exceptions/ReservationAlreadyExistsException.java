package com.restaurant.papricica.exceptions;

public class ReservationAlreadyExistsException extends AppException {
    public ReservationAlreadyExistsException() {
        super(ErrorCode.RESERVATION_ALREADY_EXISTS, "It looks like someone was quicker than you :(");
    }
}
