package com.restaurant.papricica.dtos;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
