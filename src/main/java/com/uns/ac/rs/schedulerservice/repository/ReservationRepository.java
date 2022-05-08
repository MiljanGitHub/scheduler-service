package com.uns.ac.rs.schedulerservice.repository;

import com.uns.ac.rs.schedulerservice.dto.response.ReservationDto;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT r.reservation_id " +
                  "FROM reservations r " +
                  "WHERE r.court_id = :courtId AND NOT ( ( (r.start+0) < :start AND (r.start+0) < :end AND (r.end+0) < :start AND  (r.end+0) < :end ) " +
                  "OR " +
                  "( :start < (r.start+0) AND :start < (r.end+0) AND :end < (r.start+0) AND :end < (r.end+0) ) ) " +
                  "LIMIT 1;",
                  nativeQuery = true)
    List<Integer> findOverlappingReservations(@Param("start") long start, @Param("end") long end, @Param("courtId") int courtId);

    @Query(name = "findByCourt", nativeQuery = true)
    List<ReservationDto> findByCourt(@Param("courtId") int courtId);


}
