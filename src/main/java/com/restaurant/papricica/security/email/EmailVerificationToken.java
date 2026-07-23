package com.restaurant.papricica.security.email;

import com.restaurant.papricica.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    @Getter
    @Setter
    private String token;

    @OneToOne(optional=false)
    @Getter
    @Setter
    private User user;

    @Column(nullable = false)
    @Getter
    @Setter
    private Instant expiresAt;

    @Getter
    @Setter
    private Instant usedAt;

    public boolean isExpired(){
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isUsed(){
        return usedAt != null;
    }
}
