package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.TableStatus;

public record TableAvailabilityDto(
        Long tableId,
        TableStatus status
) {
}
