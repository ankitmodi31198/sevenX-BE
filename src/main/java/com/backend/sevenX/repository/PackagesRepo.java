package com.backend.sevenX.repository;

import com.backend.sevenX.data.model.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackagesRepo  extends JpaRepository<Packages, Integer> {

    List<Packages> findByScreenName(String screenName);

    @Query(nativeQuery = true , value = "Select * from packages where screen_name in (?1)")
    List<Packages> findByScreenNameList(List<String> screenNameList);
}
