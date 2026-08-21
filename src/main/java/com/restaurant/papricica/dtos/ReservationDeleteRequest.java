package com.restaurant.papricica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ReservationDeleteRequest(
        @NotNull
        Long tableId,

        @NotNull
        OffsetDateTime startDate
) {
}
