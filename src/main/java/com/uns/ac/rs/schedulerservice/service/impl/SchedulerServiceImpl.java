package com.uns.ac.rs.schedulerservice.service.impl;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.*;
import com.uns.ac.rs.schedulerservice.model.Court;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import com.uns.ac.rs.schedulerservice.service.impl.validators.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final CourtRepository courtRepository;
    private final List<Validator> validators;
    private final ReservationRepository reservationRepository;

    @Override
    public List<CourtInfo> findAllCourtInfo() {
        return courtRepository.findCourtInfoList();
    }

    @Override
    public CourtData findAllReservationCourtInfo(int courtId) {

        Court court = courtRepository.getById(courtId);
        List<ReservationDto> reservationDtos = reservationRepository.findByCourt(courtId, System.currentTimeMillis());

        return CourtData.builder().courtId(courtId)
                .covered(court.isCovered())
                .dimension(court.getDimension())
                .name(court.getName())
                .type(court.getType())
                .url(court.getUrl())
                .reservationCourtInfos(reservationDtos).build();
    }

    @Override
    public BookingDto createReservations(ReservationRequest reservationRequest) {

        //Cannot be empty
        //No more than 5 time slots (taken from config)

        //Validation 1. -> Check to see if requested timeslots are valid among them self's

        //Validation 2. -> Check business validation

        //Validation 3. -> Check each requested timeslot with time slot in DB

        //Validation 4. -> Payment Service

        //Save to DB
        //Notify admin by email
        //WebSocket


        BookingDto handle = validators.get(0).handle(reservationRequest);


        return handle;
    }

    @Override
    public List<ReservationByCourtAndUser> findReservationsByCourtAndUser(int userId, int courtId) {
        return reservationRepository.findReservationsByCourtAndUser(courtId, userId);
    }

    @Override
    public DeleteResponse deleteReservation(int reservationId) {

        Optional<Reservation> byId = reservationRepository.findById(reservationId);
        if (byId.isEmpty()) {
            return DeleteResponse.builder().reason("Error deleting a reservation!").build();
        }

        reservationRepository.deleteById(reservationId);

        return DeleteResponse.builder().reason("Successfully deleted reservation!").build();
    }
}
