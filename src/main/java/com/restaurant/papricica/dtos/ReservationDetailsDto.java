package com.restaurant.papricica.dtos;

import java.time.OffsetDateTime;

public record ReservationDetailsDto(
        Long tableId,
        int partySize,
        OffsetDateTime startDate,
        OffsetDateTime endDate
) {
}
