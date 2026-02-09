package com.restaurant.papricica.dtos;

import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
        @NotNull
        String email,
        @NotNull
        String password
) {
    @Override
    public String toString() {
        return "UserLoginDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
