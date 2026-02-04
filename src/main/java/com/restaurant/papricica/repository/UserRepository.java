package com.restaurant.papricica.repository;

import com.restaurant.papricica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByEmail(String email);

    List<User> findAll();

    void deleteUserByEmail(String email);

}
