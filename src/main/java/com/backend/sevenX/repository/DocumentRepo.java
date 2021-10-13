package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Integer> {

	List<Document> findByUserId(Integer userId);

}
