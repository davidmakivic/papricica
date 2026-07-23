package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.ReservationDto;
import com.restaurant.papricica.dtos.UserCreateDto;
import com.restaurant.papricica.dtos.UserDetailDto;
import com.restaurant.papricica.dtos.UserLoginDto;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.exceptions.EmailAlreadyExistsException;
import com.restaurant.papricica.exceptions.PhoneNumberAlreadyExistsException;
import com.restaurant.papricica.mapper.UserMapper;
import com.restaurant.papricica.repository.UserRepository;
import com.restaurant.papricica.security.JwtTokenizer;
import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public List<UserDetailDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDetailDto)
                .toList();
    }

    public String login(UserLoginDto userLoginDto) {
        LOGGER.info("Login attempt for user '{}'", userLoginDto.email());
        UserDetails storedUser = loadUserByEmail(userLoginDto.email());

        if (storedUser == null
                || !storedUser.isAccountNonExpired()
                || !storedUser.isCredentialsNonExpired()
        ) {
            throw new BadCredentialsException("Username or password is incorrect or account is locked");
        }

        if (!storedUser.isAccountNonLocked()) {
            throw new BadCredentialsException("This account is locked.");
        }

        if(!passwordEncoder.matches(userLoginDto.password(), storedUser.getPassword())) {
            boolean isAdmin = storedUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals("ROLE_ADMIN"));

            if (!isAdmin) {
                userRepository.incrementFailedLoginAttempts(userLoginDto.email());
            }
            throw new BadCredentialsException("Username or password is incorrect or account is locked");
        }

        List<String> roles = storedUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        userRepository.setFailedLoginAttemptsToZero(userLoginDto.email());
        return jwtTokenizer.getAuthToken(storedUser.getUsername(), roles);
    }

    public UserDetailDto createUser(UserCreateDto dto) {

        if(userRepository.findUserByEmail(dto.email()) != null) {
            throw new EmailAlreadyExistsException();
        }

        if(userRepository.findUserByPhoneNumber(dto.phoneNumber()) != null){
            throw new PhoneNumberAlreadyExistsException();
        }

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

    @Transactional
    public void delete(String email) {

        List<ReservationDto> reservations = reservationService.getAllForUser(email);

        for(ReservationDto reservation : reservations) {
            reservationService.deleteForUser(reservation.id(),  email);
        }

        userRepository.deleteUserByEmail(email);
    }

    public UserDetails loadUserByEmail(String email) {
        LOGGER.info("Loading user by email '{}'", email);
        try{
            User user = userRepository.findUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if(user.getRole() == Roles.ADMIN) {
               grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPasswordHash())
                    .authorities(grantedAuthorities)
                    .accountLocked(user.getStatus().equals(UserStatus.LOCKED))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

}
