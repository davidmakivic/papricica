package com.restaurant.papricica.entity;

public record Restaurant(
    Long id,
    String name,
    String address,
    String phoneNumber,
    String email
) {
}
