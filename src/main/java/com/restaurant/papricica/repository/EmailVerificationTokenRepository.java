package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.User;
import com.restaurant.papricica.security.email.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    void deleteByUser(User user);
}
