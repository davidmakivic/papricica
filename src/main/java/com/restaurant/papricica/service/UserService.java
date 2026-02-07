package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.mapper.UserMapper;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.security.JwtTokenizer;
import com.restaurant.papricica.util.UserStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReservationService reservationService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, UserRepository userRepository, UserMapper userMapper, ReservationService reservationService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.reservationService = reservationService;
    }

    public UserDetailDto createUser(UserCreateDto dto) {

        User newUser = User.UserBuilder.aUser()
                .withEmail(dto.email())
                .withPassword(passwordEncoder.encode(dto.password()))
                .withFirstName(dto.firstName())
                .withLastName(dto.lastName())
                .withPhoneNumber(dto.phoneNumber())
                .withRole(dto.role())
                .withFailedLoginAttempts(0)
                .withUserStatus(UserStatus.UNVERIFIED)
                .withCreatedAt()
                .build();

        return userMapper.userToUserDetailDto(userRepository.save(newUser));
    }

    @Transactional
    public void delete(String email) {

        List<ReservationDto> reservations = reservationService.getAllForUser(email);

        for(ReservationDto reservation : reservations) {
            reservationService.deleteForUser(reservation.id(),  email);
        }

        userRepository.deleteUserByEmail(email);
    }

}
