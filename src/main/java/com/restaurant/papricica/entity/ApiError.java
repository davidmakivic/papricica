package com.restaurant.papricica.entity;

public record ApiError(
        int status,
        String code,
        String message
) {
}
