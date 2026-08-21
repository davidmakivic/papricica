package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceIntegrationTests {

    private final UserService underTest;

    @Autowired
    public UserServiceIntegrationTests (final UserService underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatUserGetsCreated() {
        UserCreateDto createDto = UserCreateDto.builder().
                email("muster@email.com").
                password("1234567").
                firstName("Michael").
                lastName("Enichlmayer").
                phoneNumber("+4367763470895").
                role(Roles.USER).
                build();

        UserDetailDto newUser = underTest.createUser(createDto);

        assertThat(newUser).isEqualTo(UserDetailDto.builder().
                userId(1L).
                email("muster@email.com").
                firstName("Michael").
                lastName("Enichlmayer").
                phoneNumber("+4367763470895").
                role(Roles.USER).
                status(UserStatus.UNVERIFIED).
                createdAt(newUser.createdAt()).
                build());
    }
}
