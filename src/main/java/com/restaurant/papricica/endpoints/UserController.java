package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailDto createUser(@RequestBody UserCreateDto dto) {
        LOGGER.info("Creating a new user");
        return userService.createUser(dto);
    }
}
