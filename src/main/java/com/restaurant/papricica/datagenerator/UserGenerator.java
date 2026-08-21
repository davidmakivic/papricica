package com.restaurant.papricica.datagenerator;

import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("generateData")
public class UserGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(UserGenerator.class);

    private static final int NUMBER_OF_USERS_TO_GENERATE = 1;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void generateUsers() {
        if (userRepository.existsByEmail("admin@email.com")) {
            LOG.debug("Development users already generated");
            return;
        }

        LOG.debug(
                "Generating {} development users",
                NUMBER_OF_USERS_TO_GENERATE
        );

        //createAdmin();
        createTestUsers();
    }

//    private void createAdmin() {
//        ApplicationUser admin =
//                ApplicationUser.ApplicationUserBuilder
//                        .aApplicationUser()
//                        .withEmail("admin@email.com")
//                        .withPassword(
//                                passwordEncoder.encode("password")
//                        )
//                        .withFirstName("Admin")
//                        .withLastName("Admin")
//                        .withZipCode("1040")
//                        .withCity("Wien")
//                        .withAddress("Wiedner Hauptstraße 78")
//                        .withRole(Roles.ADMIN)
//                        .withRewardPoints(0)
//                        .withCountry("Austria")
//                        .withUserStatus(UserStatus.UNLOCKED)
//                        .withFailedLoginAttempts(0)
//                        .build();
//
//        userRepository.save(admin);
//    }

    private void createTestUsers() {
        for (int i = 1; i <= NUMBER_OF_USERS_TO_GENERATE; i++) {

            User user = User.builder()
                    .passwordHash(passwordEncoder.encode("password"))
                    .firstName("David")
                    .lastName("Makivic")
                    .phoneNumber("*4367763470895")
                    .failedLoginAttempts(0)
                    .role(Roles.USER)
                    .status(UserStatus.UNLOCKED)
                    .build();

            if (i == 1) {
                user.setEmail("user@email.com");
            }

//            if (i % 2 == 0) {
//                user.setStatus(UserStatus.LOCKED);
//                user.setFailedLoginAttempts(5);
//            } else {
//                user.setStatus(UserStatus.UNLOCKED);
//                user.setFailedLoginAttempts(i % 5);
//            }

            userRepository.save(user);
        }
    }
}
