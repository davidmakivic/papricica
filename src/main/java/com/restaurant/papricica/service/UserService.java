package com.restaurant.papricica.service;

import com.restaurant.papricica.dtos.*;
import com.restaurant.papricica.entity.EmailVerificationToken;
import com.restaurant.papricica.entity.ForgotPasswordToken;
import com.restaurant.papricica.entity.MealRedemptionToken;
import com.restaurant.papricica.entity.RefreshToken;
import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.exceptions.EmailAlreadyExistsException;
import com.restaurant.papricica.exceptions.PhoneNumberAlreadyExistsException;
import com.restaurant.papricica.mapper.UserMapper;
import com.restaurant.papricica.repository.*;
import com.restaurant.papricica.security.TokenGenerator;
import com.restaurant.papricica.security.TokenHasher;
import com.restaurant.papricica.security.user.JwtTokenizer;
import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReservationRepository reservationRepository;
    private final TokenHasher tokenHasher;
    private final TokenGenerator tokenGenerator;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MealRedemptionTokenRepository mealRedemptionRepository;

    public UserService(PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer, UserRepository userRepository, UserMapper userMapper, ReservationRepository reservationService, TokenHasher tokenHasher, TokenGenerator tokenGenerator, EmailVerificationTokenRepository emailVerificationTokenRepository, EmailService emailService, ForgotPasswordTokenRepository forgotPasswordTokenRepository, RefreshTokenRepository refreshTokenRepository, MealRedemptionTokenRepository mealRedemptionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.reservationRepository = reservationService;
        this.tokenHasher = tokenHasher;
        this.tokenGenerator = tokenGenerator;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.emailService = emailService;
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mealRedemptionRepository = mealRedemptionRepository;
    }

    @Transactional
    public LoginResponse login(UserLoginDto userLoginDto) {
        LOGGER.info("Login attempt for user '{}'", userLoginDto.email());
        UserDetails storedUser = loadUserByEmail(userLoginDto.email());

        if (storedUser == null) {
            throw new BadCredentialsException("User non existent");
        }

        if (!storedUser.isAccountNonExpired()) {
            throw new BadCredentialsException("Account expired");
        }

        if (!storedUser.isCredentialsNonExpired()) {
            throw new BadCredentialsException("Credentials expired");
        }

        if (!storedUser.isAccountNonLocked()) {
            throw new BadCredentialsException("This account needs to be verified first");
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

        User user = userRepository.findUserByEmail(userLoginDto.email());
        String accessToken = jwtTokenizer.getAuthToken(storedUser.getUsername(), roles);
        String rawRefreshToken = tokenGenerator.generateToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenHasher.hash(rawRefreshToken))
                .expiresAt(Instant.now().plus(Duration.ofDays(90)))
                .build();

        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(accessToken, rawRefreshToken);
    }

    @Transactional
    public UserDetailDto createUser(UserCreateDto dto) {

        String normalizedEmail = dto.email().trim().toLowerCase();

        if(userRepository.findUserByEmail(normalizedEmail) != null) {
            throw new EmailAlreadyExistsException();
        }

        if(userRepository.findUserByPhoneNumber(dto.phoneNumber()) != null){
            throw new PhoneNumberAlreadyExistsException();
        }

        User newUser = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(dto.password()))
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(dto.phoneNumber())
                .role(dto.role())
                .failedLoginAttempts(0)
                .status(UserStatus.UNVERIFIED)
                .points(0L)
                .build();

        userRepository.save(newUser);

        String rawToken = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.hash(rawToken);

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(newUser)
                .token(hashedToken)
                .expiresAt(Instant.now().plus(Duration.ofMinutes(30)))
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        String confirmationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" // MAYBE DIFFERENT APPROACH NEEDED FOR ANDROID APP
                + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);

        emailService.sendVerificationEmail(newUser.getEmail(), confirmationUrl);

        return userMapper.userToUserDetailDto(newUser);
    }

    @Transactional
    public void delete(String email) {

        //List<Reservation> reservations = reservationRepository.findAllByUserEmail(email);

        //reservations.forEach(r -> reservationRepository.deleteById(r.getId()));

        userRepository.deleteUserByEmail(email);
    }

    @Transactional
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
                    .accountLocked(user.getStatus().equals(UserStatus.UNVERIFIED) || user.getStatus().equals(UserStatus.LOCKED))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public UserDetailDto update (UserUpdateDto userUpdateDto, String email) {

        User saved = userRepository.findUserByEmail(email);

        saved.setFirstName(userUpdateDto.firstName());
        saved.setLastName(userUpdateDto.lastName());
        saved.setPhoneNumber(userUpdateDto.phoneNumber());

        return userMapper.userToUserDetailDto(saved);

    }

    @Transactional
    public void changePassword (ChangePasswordDto dto, String email) {

        User saved = userRepository.findUserByEmail(email);

        if(!passwordEncoder.matches(dto.oldPassword(), saved.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect!");
        }

        saved.setPasswordHash(passwordEncoder.encode(dto.newPassword()));

    }

    @Transactional
    public void forgotPassword(String email) {
        User stored = userRepository.findUserByEmail(email);

        if(stored == null) {
            throw new UsernameNotFoundException("This user does not exist");
        }

        if(stored.getStatus().equals(UserStatus.UNVERIFIED) || stored.getStatus().equals(UserStatus.LOCKED)){
            throw new RuntimeException("There was some issue with the status of your account");
        }

        String rawToken = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.hash(rawToken);

        ForgotPasswordToken token = ForgotPasswordToken.builder()
                .token(hashedToken)
                .user(stored)
                .expiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                .build();

        forgotPasswordTokenRepository.save(token);

        String confirmationUrl = "http://localhost:8080/api/v1/auth/reset-password?token=" // MAYBE DIFFERENT APPROACH NEEDED FOR ANDROID APP
                + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);

        emailService.sendForgotPasswordEmail(email, confirmationUrl);
    }

    @Transactional
    public void blockUser(String email) {
        User user = userRepository.findUserByEmail(email);

        if(user == null || user.getRole() == Roles.ADMIN){
            throw new RuntimeException("User does not exist");
        }


        RefreshToken token = refreshTokenRepository.findByUser(user);

        if(token == null){
            throw new RuntimeException("User's refresh token does not exist");
        }

        refreshTokenRepository.deleteRefreshTokenByToken(token.getToken());

        user.setStatus(UserStatus.LOCKED);
    }

    @Transactional
    public LoginResponse checkRefreshToken(RefreshTokenDto token) {

        RefreshToken stored = refreshTokenRepository.findByToken(tokenHasher.hash(token.refreshToken()));

        if(stored == null){
            throw new RuntimeException("Something went wrong with your authentication");
        }

        if(stored.isExpired()){
            refreshTokenRepository.deleteById(stored.getId());
            return null;
        }

        UserDetails storedUser = loadUserByEmail(stored.getUser().getEmail());

        if (storedUser == null) {
            throw new BadCredentialsException("User non existent");
        }

        if (!storedUser.isAccountNonExpired()) {
            throw new BadCredentialsException("Account expired");
        }

        if (!storedUser.isCredentialsNonExpired()) {
            throw new BadCredentialsException("Credentials expired");
        }

        if (!storedUser.isAccountNonLocked()) {
            throw new BadCredentialsException("This account needs to be verified first");
        }

        List<String> roles = storedUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtTokenizer.getAuthToken(storedUser.getUsername(), roles);
        String rawRefreshToken = tokenGenerator.generateToken();

        RefreshToken updated = RefreshToken.builder()
                .user(stored.getUser())
                .token(tokenHasher.hash(rawRefreshToken))
                .expiresAt(Instant.now().plus(Duration.ofDays(90)))
                .build();

        refreshTokenRepository.save(updated);

        refreshTokenRepository.deleteById(stored.getId());

        return new LoginResponse(accessToken, rawRefreshToken);

    }

    @Transactional
    public void deleteRefreshToken(RefreshTokenDto token) {
        String hashedToken = tokenHasher.hash(token.refreshToken());

        refreshTokenRepository.deleteRefreshTokenByToken(hashedToken);
    }


    @Transactional
    public void redeemMeal(String email) {
        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new RuntimeException("User does not exist");
        }

        if(user.getPoints() < 100){
            throw new RuntimeException("User does not have enough points to redeem a meal");
        }

        String rawToken = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.hash(rawToken);

        MealRedemptionToken token = MealRedemptionToken.builder()
                .token(hashedToken)
                .user(user)
                .expiresAt(Instant.now().plus(Duration.ofDays(7)))
                .build();

        mealRedemptionRepository.save(token);

        emailService.sendRedemptionEmail(email, rawToken);

        token.getUser().setPoints(token.getUser().getPoints() - 100);
    }
}