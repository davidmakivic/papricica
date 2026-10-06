package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.*;
import com.restaurant.papricica.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    @GetMapping("/me")
    public UserDetailDto me(Principal principal){
        LOGGER.info("Getting data for user '{}'", principal.getName());
        LOGGER.debug("Getting data for user '{}'", principal.getName());

        return userService.me(principal.getName());
    }

    @PermitAll
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody UserLoginDto userLoginDto) {
        LOGGER.info("Login attempt for user '{}'", userLoginDto.email());
        LOGGER.debug("Login DTO received");
        return userService.login(userLoginDto);
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDto dto) {
        LOGGER.info("Creating a new user");
        userService.createUser(dto);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(Principal principal) {
        userService.delete(principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_USER")
    @PutMapping
    public ResponseEntity<UserDetailDto> updateUser(@RequestBody UserUpdateDto userUpdateDto, Principal principal){
        UserDetailDto updated = userService.update(userUpdateDto, principal.getName());
        return ResponseEntity.ok(updated);
    }

    @Secured("ROLE_USER")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto, Principal principal) {
        userService.changePassword(dto, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email){
        userService.forgotPassword(email);
        return ResponseEntity.noContent().build();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("block")
    public ResponseEntity<Void> blockUser(@RequestParam String email){

        userService.blockUser(email);

        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenDto token){

        userService.deleteRefreshToken(token);

        return ResponseEntity.noContent().build();
    }

    @PermitAll
    @GetMapping("/refresh-token")
    public LoginResponse checkRefreshToken(@RequestBody RefreshTokenDto token) {
        LOGGER.info("Refreshing token");

        return userService.checkRefreshToken(token);
    }

    @Secured("ROLE_USER")
    @GetMapping("/redeem-meal")
    public ResponseEntity<Void> redeemMeal (Principal principal, @RequestParam Integer amount){
        userService.redeemMeal(principal.getName(), amount);
        return ResponseEntity.ok().build();
    }
}