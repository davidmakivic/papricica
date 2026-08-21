package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.RefreshToken;
import com.restaurant.papricica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);

    void deleteRefreshTokenByToken(String token);

    RefreshToken findByUser(User user);
}
