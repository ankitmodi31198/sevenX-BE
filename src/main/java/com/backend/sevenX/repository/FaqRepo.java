package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepo extends JpaRepository<FAQ, Integer> {

}
