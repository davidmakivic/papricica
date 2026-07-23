package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.dtos.UserLoginDto;
import com.restaurant.papricica.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        LOGGER.info("Login attempt for user '{}'", userLoginDto.email());
        LOGGER.debug("Login DTO received");
        return userService.login(userLoginDto);
    }

    @PermitAll
    @GetMapping
    ResponseEntity<List<UserDetailDto>> getAllUsers() {
        LOGGER.info("getallUsers");
        LOGGER.debug("getAllUsers");
        List<UserDetailDto> users = userService.getAll();
        return users != null ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailDto createUser(@Valid @RequestBody UserCreateDto dto) {
        LOGGER.info("Creating a new user");
        return userService.createUser(dto);
    }

    @PermitAll
    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> deleteUser(Principal principal) {
        userService.delete(principal.getName());
        return ResponseEntity.noContent().build();
    }
}
