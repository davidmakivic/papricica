package com.restaurant.papricica.entity;

import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 255)
    private String email;

    @Getter
    @Setter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    @Size(max = 255)
    private String firstName;

    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    @Size(max = 255)
    private String lastName;

    @Getter
    @Setter
    @Column(name = "phone_number", nullable = false)
    @Size(max = 20)
    private String phoneNumber;

    @Getter
    @Setter
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Getter
    @Setter
    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Getter
    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private Set<Reservation> reservations;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public User() {
    }

    public User(Long userId, String phoneNumber, String email, String passwordHash, String firstName, String lastName, UserStatus status, Integer failedLoginAttempts
    ) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.failedLoginAttempts = failedLoginAttempts;
        this.phoneNumber = phoneNumber;
    }

    public static final class UserBuilder {
        private Long userId;
        private String email;
        private String passwordHash;
        private String firstName;
        private String lastName;
        private Roles role;
        private UserStatus status;
        private Integer failedLoginAttempts;
        private String phoneNumber;
        private OffsetDateTime createdAt;

        private UserBuilder() {
        }

        public static User.UserBuilder aUser() {
            return new User.UserBuilder();
        }

        public User.UserBuilder withId(Long id) {
            this.userId = id;
            return this;
        }

        public User.UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public User.UserBuilder withPassword(String password) {
            this.passwordHash = password;
            return this;
        }

        public User.UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public User.UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User.UserBuilder withRole(Roles role) {
            this.role = role;
            return this;
        }

        public User.UserBuilder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }


        public User.UserBuilder withUserStatus(UserStatus userStatus) {
            this.status = userStatus;
            return this;
        }

        public User.UserBuilder withFailedLoginAttempts(Integer failedLoginAttempts) {
            this.failedLoginAttempts = failedLoginAttempts;
            return this;
        }

        public User.UserBuilder withCreatedAtNow() {
            this.createdAt = OffsetDateTime.now();
            return this;
        }

        public User build() {
            User User = new User();
            User.setUserId(userId);
            User.setEmail(email);
            User.setPasswordHash(passwordHash);
            User.setFirstName(firstName);
            User.setLastName(lastName);
            User.setRole(role);
            User.setStatus(status);
            User.setPhoneNumber(phoneNumber);
            User.setFailedLoginAttempts(failedLoginAttempts);
            if (createdAt != null) {
                User.setCreatedAt(createdAt);
            }
            return User;
        }
    }
}
