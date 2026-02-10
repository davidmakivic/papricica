package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.ReservationStatus;

import java.time.OffsetDateTime;
import java.util.Date;

public record ReservationDto(
        Long id,
        Long tableId,
        int partySize,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        OffsetDateTime createdAt,
        ReservationStatus status
) {
}
