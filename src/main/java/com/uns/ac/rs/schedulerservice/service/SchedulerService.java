package com.uns.ac.rs.schedulerservice.service;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.BookingDto;
import com.uns.ac.rs.schedulerservice.dto.response.CourtData;
import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;
import com.uns.ac.rs.schedulerservice.dto.response.ReservationByCourtAndUser;

import java.util.List;

public interface SchedulerService {

    List<CourtInfo> findAllCourtInfo();

    CourtData findAllReservationCourtInfo(int courtId);

    BookingDto createReservations(ReservationRequest object);

   List<ReservationByCourtAndUser>  findReservationsByCourtAndUser(int userId, int courtId);
}
