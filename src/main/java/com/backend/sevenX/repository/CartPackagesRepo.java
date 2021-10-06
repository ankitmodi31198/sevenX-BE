package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.CartPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartPackagesRepo extends JpaRepository<CartPackages, Integer> {

    void deleteByCartDetailsId(Integer cartDetailsId);
}
