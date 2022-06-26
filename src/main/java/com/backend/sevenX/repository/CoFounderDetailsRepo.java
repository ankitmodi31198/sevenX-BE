package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.CoFounderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoFounderDetailsRepo extends JpaRepository<CoFounderDetails,Integer> {

    @Query("SELECT COUNT(id) FROM CoFounderDetails")
    Integer countRecords();
}
