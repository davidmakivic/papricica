package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.ReservationStatus;

import java.time.OffsetDateTime;

public record ReservationDetailsDto(
        Long tableId,
        int partySize,
        ReservationStatus status,
        OffsetDateTime startDate,
        OffsetDateTime endDate
) {
}
