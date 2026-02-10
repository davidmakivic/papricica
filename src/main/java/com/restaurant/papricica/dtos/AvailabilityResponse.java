package com.restaurant.papricica.dtos;

import java.time.OffsetDateTime;
import java.util.List;

public record AvailabilityResponse(
        OffsetDateTime start,
        OffsetDateTime end,
        List<TableAvailabilityDto> tables
) {
}
