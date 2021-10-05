package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddCartDetailsRepo  extends JpaRepository<CartDetails,Integer> {

    List<CartDetails> findByUserId(Integer userId);
}
