package com.uns.ac.rs.schedulerservice.service;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.*;

import java.util.List;

public interface SchedulerService {

    List<CourtInfo> findAllCourtInfo();

    CourtData findAllReservationCourtInfo(int courtId);

    BookingDto createReservations(ReservationRequest object);

   List<ReservationByCourtAndUser>  findReservationsByCourtAndUser(int userId, int courtId);

    DeleteResponse deleteReservation(int reservationId);
}
