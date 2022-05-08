package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.BookingDto;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
@NoArgsConstructor
public class ValidatorC extends Validator{

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public BookingDto handle(ReservationRequest reservationRequest) {
        //ValidatorC -> Check (compare for no overlap) each requested timeslot with time slot in DB

        List<Integer> overlappingReservations = reservationRepository
                .findOverlappingReservations(reservationRequest.getReservationDtos().get(0).getStart(), reservationRequest.getReservationDtos().get(0).getEnd(), reservationRequest.getCourtId());


        boolean overlappingTimeslotExists = reservationRequest.getReservationDtos().stream().anyMatch(
                reservationDto -> !reservationRepository
                        .findOverlappingReservations(reservationDto.getStart(), reservationDto.getEnd(), reservationRequest.getCourtId()).isEmpty());

        if (overlappingTimeslotExists) return errorResponse("There are already timeslots taken which overlap with one of your requested timeslots");

        return checkNext(reservationRequest);
    }

    private BookingDto errorResponse(String errorMessage){
        return BookingDto.builder().hasErrorMessage(true).errorMessage(errorMessage).build();
    }
}
