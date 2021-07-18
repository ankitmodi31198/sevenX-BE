package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {

    Users findByUsername(String username);

    Optional<Users> findByForgotToken(String token);
}
