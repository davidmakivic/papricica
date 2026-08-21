package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByEmail(String email);

    boolean existsByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    void deleteUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("""
            UPDATE User u
            SET
                u.failedLoginAttempts = u.failedLoginAttempts + 1,
                u.status =
                    CASE
                        WHEN (u.failedLoginAttempts + 1) >= 4 THEN 'LOCKED'
                        ELSE 'UNLOCKED'
                    END
            WHERE u.email = :email
        """)
    void incrementFailedLoginAttempts(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("""
            UPDATE User u SET
                    u.failedLoginAttempts = 0,
                    u.status = 'UNLOCKED'
            WHERE u.email = :email
        """)
    void setFailedLoginAttemptsToZero(@Param("email") String email);
}
