package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails,Integer> {

    List<OrderDetails> findByUserId(Integer userId);

    @Query("SELECT COUNT(id) FROM OrderDetails")
    Integer countRecords();
}
