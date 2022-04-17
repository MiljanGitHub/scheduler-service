package com.uns.ac.rs.schedulerservice.service;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.CourtData;
import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;

import java.util.List;

public interface SchedulerService {

    List<CourtInfo> findAllCourtInfo();

    CourtData findAllReservationCourtInfo(int courtId);

    Object createReservations(ReservationRequest object);
}
