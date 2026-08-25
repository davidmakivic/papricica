package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.MealRedemptionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRedemptionTokenRepository extends JpaRepository<MealRedemptionToken, Long> {
    MealRedemptionToken findByToken(String token);
}
