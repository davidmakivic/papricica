package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.ReservationStatus;

import java.util.Date;

public record ReservationDto(
        Long id,
        Long tableId,
        int partySize,
        Date startDate,
        Date endDate,
        Date createdAt,
        ReservationStatus status
) {
}
