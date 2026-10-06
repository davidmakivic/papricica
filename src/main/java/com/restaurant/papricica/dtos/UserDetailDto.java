package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record UserDetailDto(
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Roles role,
        Long points
) {
}
