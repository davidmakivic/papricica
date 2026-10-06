package com.restaurant.papricica.datagenerator;

import com.restaurant.papricica.entity.Table;
import com.restaurant.papricica.repository.TableRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("generateData")
public class TableGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TableGenerator.class);

    private static final int NUMBER_OF_TABLES_TO_GENERATE = 10;

    private final TableRepository tableRepository;

    public TableGenerator(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @PostConstruct
    public void generateTables() {

        LOG.debug(
                "Generating {} development users",
                NUMBER_OF_TABLES_TO_GENERATE
        );
        createTables();
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

    private void createTables() {
        for (int i = 1; i <= NUMBER_OF_TABLES_TO_GENERATE; i++) {

            if(i == 1 || i == 4 || i == 6) {
                tableRepository.save(Table.builder().partySize(2).build());
           } else if (i == 2 || i == 5 || i == 7){
                tableRepository.save(Table.builder().partySize(4).build());
            } else {
                tableRepository.save(Table.builder().partySize(6).build());
            }

        }
    }
}
