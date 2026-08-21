package com.restaurant.papricica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
        @NotNull
        @NotBlank
        String email,

        @NotNull
        @NotBlank
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
