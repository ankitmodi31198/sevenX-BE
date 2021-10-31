package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactFormRepo extends JpaRepository<ContactForm, Integer> {

    @Query("SELECT COUNT(id) FROM ContactForm")
    Integer countRecords();

}
