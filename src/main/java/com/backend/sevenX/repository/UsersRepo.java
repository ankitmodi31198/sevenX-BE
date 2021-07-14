package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {

    Users findByUsernameAndDeletedAtIsNull(String username);
}
