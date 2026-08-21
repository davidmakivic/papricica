package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateDto(
        @NotBlank(message = "Please provide an email")
        @NotNull(message = "Please provide an email")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Please provide a password")
        @NotNull(message = "Please provide a password")
        String password,

        @NotBlank(message = "Please provide  your first name")
        @NotNull(message = "Please provide  your first name")
        String firstName,

        @NotBlank(message = "Please provide your second name")
        @NotNull(message = "Please provide your second name")
        String lastName,

        @NotBlank(message = "Please provide a phone number")
        @NotNull(message = "Please provide a phone number")
        String phoneNumber,

        @NotNull
        Roles role
) {
}
