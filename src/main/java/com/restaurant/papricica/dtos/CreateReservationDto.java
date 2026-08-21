package com.restaurant.papricica.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateReservationDto(
        @NotNull(message = "Please provide your preferred table")
        Long tableId,

        @NotNull(message = "Please provide the size of your party")
        int partySize,

        @NotNull(message = "Please provide a reservation time")
        OffsetDateTime startDate
) {
}
