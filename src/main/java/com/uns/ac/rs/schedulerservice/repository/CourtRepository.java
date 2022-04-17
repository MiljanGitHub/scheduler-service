package com.uns.ac.rs.schedulerservice.repository;

import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;
import com.uns.ac.rs.schedulerservice.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {

    @Query(name = "findAllCourtsInfo", nativeQuery = true)
    List<CourtInfo> findCourtInfoList();
}
