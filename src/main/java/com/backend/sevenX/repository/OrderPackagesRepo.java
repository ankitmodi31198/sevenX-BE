package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.OrderPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPackagesRepo extends JpaRepository<OrderPackages, Integer> {
}
