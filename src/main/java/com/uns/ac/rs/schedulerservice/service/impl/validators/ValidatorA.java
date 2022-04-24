package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationDto;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.BookingDto;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@NoArgsConstructor
public class ValidatorA extends Validator{


    @Override
    public BookingDto handle(ReservationRequest reservationRequest) {

        //ValidatorA. -> Check to see if requested timeslots are valid among them self's

        String invalidTimeslotMessage = hasInvalidTimeslot(reservationRequest);
        if (invalidTimeslotMessage != null) return errorResponse(invalidTimeslotMessage);

        String hasInvalidDateMessage = hasInvalidDate(reservationRequest);
        if (hasInvalidDateMessage != null) return errorResponse(hasInvalidDateMessage);

        String notInValidTimeslotRangeMessage = notInValidTimeslotRange(reservationRequest);
        if(notInValidTimeslotRangeMessage != null) return errorResponse(notInValidTimeslotRangeMessage);

        String hasOverlapsMessage = hasOverlaps(reservationRequest);
        if (hasOverlapsMessage != null) return errorResponse(hasOverlapsMessage);

        String notInFutureMessage = notInFuture(reservationRequest);
        if (notInFutureMessage != null) return errorResponse(notInFutureMessage);

        return checkNext(reservationRequest);
    }

    private BookingDto errorResponse(String errorMessage){
        return BookingDto.builder().hasErrorMessage(true).errorMessage(errorMessage).build();
    }

    private String hasInvalidTimeslot(ReservationRequest reservationRequest){
        if (reservationRequest == null ||
                CollectionUtils.isEmpty(reservationRequest.getReservationDtos()) ||
                reservationRequest.getReservationDtos().stream().anyMatch(Objects::isNull)) return "You must specify timeslots!";
        return null;
    }

    private String hasInvalidDate(ReservationRequest reservationRequest){
        boolean invalidTime = reservationRequest.getReservationDtos().stream().filter(Objects::nonNull).anyMatch(reservationDto ->  reservationDto.getStart() <= 0 || reservationDto.getEnd() <= 0);
        if(invalidTime) return "You must specify valid star end times";
        return null;
    }

    private String notInFuture(ReservationRequest reservationRequest){
        long now = System.currentTimeMillis();
        boolean notInFuture = reservationRequest.getReservationDtos().stream().anyMatch(reservationDto -> now > reservationDto.getStart() || now > reservationDto.getEnd());
        if (notInFuture) return "Each date must be in future";
        return null;
    }

    private String notInValidTimeslotRange(ReservationRequest reservationRequest){
        boolean containsInvalidTimeslotRange = reservationRequest.getReservationDtos().stream().anyMatch(reservationDto ->
                (reservationDto.getEnd() - reservationDto.getStart()) <= 1800000L && (reservationDto.getEnd() - reservationDto.getStart()) >= 7200000L);
        if (containsInvalidTimeslotRange) return "Timeslot must be between 0.5h and 2h";
        return null;
    }


    private String hasOverlaps(ReservationRequest reservationRequest){

        for (int i = 0; i < reservationRequest.getReservationDtos().size(); i++){
            ReservationDto outer = reservationRequest.getReservationDtos().get(0);

            LocalDateTime startI =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(outer.getStart()),
                            TimeZone.getDefault().toZoneId());

            LocalDateTime endI =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(outer.getEnd()),
                            TimeZone.getDefault().toZoneId());

            if (startI.isAfter(endI)) return "Start date of each timeslot must be before end date.";

            for (int k = 0; k < reservationRequest.getReservationDtos().size(); k++){

                if (i == k) continue;

                LocalDateTime startK =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(outer.getStart()),
                                TimeZone.getDefault().toZoneId());

                LocalDateTime endK =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(outer.getEnd()),
                                TimeZone.getDefault().toZoneId());

                if (startK.isAfter(endK)) return "Start date of each timeslot must be before end date.";

                //check for overlaps of start* Dates;

                if (hasOverlaps(startI, endI, startK, endK)) return "There must not be any overlaps between dates";

            }
        }

        return null;
    }

    private boolean hasOverlaps(LocalDateTime startI, LocalDateTime endI, LocalDateTime startK, LocalDateTime endK){

        //to be tested
        return !(startI.isBefore(startK) && startI.isBefore(endK) && endI.isBefore(startK) && endI.isBefore(endK)) ||

                (startK.isBefore(startI) && endK.isBefore(startI) && startK.isBefore(endI) && endK.isBefore(endI));

    }
}
