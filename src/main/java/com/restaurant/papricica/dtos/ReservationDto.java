package com.restaurant.papricica.dtos;

import com.restaurant.papricica.entity.Table;
import com.restaurant.papricica.util.ReservationStatus;

import java.time.OffsetDateTime;

public record ReservationDto(
        Long id,
        Table table,
        int partySize,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        OffsetDateTime createdAt,
        ReservationStatus status
) {
}
