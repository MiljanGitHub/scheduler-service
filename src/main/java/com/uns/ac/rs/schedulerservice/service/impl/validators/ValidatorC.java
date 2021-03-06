package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
@NoArgsConstructor
public class ValidatorC extends Validator{

    private ReservationRepository reservationRepository;

    @Override
    public String handle(ReservationRequest reservationRequest) {
        //ValidatorC -> Check (compare for no overlap) each requested timeslot with time slot in DB


        boolean overlappingTimeslotExists = reservationRequest.getReservationDtos().stream().anyMatch(
                reservationDto -> !reservationRepository
                        .findOverlappingReservations(reservationDto.getStart(), reservationDto.getEnd(), reservationRequest.getCourtId()).isEmpty());

        if (overlappingTimeslotExists) return "There are already timeslots taken which overlap with one of your requested timeslots";

        return checkNext(reservationRequest);
    }
}
