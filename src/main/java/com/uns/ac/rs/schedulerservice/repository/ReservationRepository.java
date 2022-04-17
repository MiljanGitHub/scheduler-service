package com.uns.ac.rs.schedulerservice.repository;

import com.uns.ac.rs.schedulerservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
