package com.restaurant.papricica.entity;

import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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
    @Column(name = "role", nullable = false)
    private Roles role;

    @Getter
    @Setter
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Getter
    @Setter
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Getter
    @Setter
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts;

    public User() {
    }

    public User(Long userId, String phoneNumber, String email, String passwordHash, String firstName, String lastName, Timestamp createdAt, UserStatus status, Integer failedLoginAttempts
    ) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
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
        private String phoneNumber;
        private UserStatus status;
        private Integer failedLoginAttempts;

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

        public User build() {
            User User = new User();
            User.setUserId(userId);
            User.setEmail(email);
            User.setPasswordHash(passwordHash);
            User.setFirstName(firstName);
            User.setLastName(lastName);
            User.setRole(role);
            User.setStatus(status);
            User.setFailedLoginAttempts(failedLoginAttempts);
            return User;
        }
    }
}
