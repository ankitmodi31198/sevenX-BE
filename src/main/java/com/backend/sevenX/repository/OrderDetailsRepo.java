package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails,Integer> {

    OrderDetails findByUserId(Integer userId);
}
