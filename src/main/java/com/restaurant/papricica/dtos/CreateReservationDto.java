package com.restaurant.papricica.dtos;


import java.time.OffsetDateTime;

public record CreateReservationDto(
        Long tableId,
        int partySize,
        OffsetDateTime startDate
) {
}
