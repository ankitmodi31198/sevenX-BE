package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.StartupIdeaDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartupIdeaDetailsRepo extends JpaRepository<StartupIdeaDetails, Integer> {

}
