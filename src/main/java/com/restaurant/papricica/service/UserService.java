package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.mapper.UserMapper;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.security.JwtTokenizer;
import com.restaurant.papricica.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

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
                .build();

        return userMapper.userToUserDetailDto(userRepository.save(newUser));
    }

}
