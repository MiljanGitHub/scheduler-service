package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationDto;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@Order(1)
@Component
@NoArgsConstructor
public class ValidatorA extends Validator{


    @Override
    public String handle(ReservationRequest reservationRequest) {

        //ValidatorA. -> Check to see if requested timeslots are valid among them self's

        String invalidTimeslotMessage = hasInvalidTimeslot(reservationRequest);
        if (invalidTimeslotMessage != null) return invalidTimeslotMessage;

        String hasInvalidDateMessage = hasInvalidDate(reservationRequest);
        if (hasInvalidDateMessage != null) return hasInvalidDateMessage;

        String notInValidTimeslotRangeMessage = notInValidTimeslotRange(reservationRequest);
        if(notInValidTimeslotRangeMessage != null) return notInValidTimeslotRangeMessage;

        String hasOverlapsMessage = hasOverlaps(reservationRequest);
        if (hasOverlapsMessage != null) return hasOverlapsMessage;


        return checkNext(reservationRequest);
    }

    private String hasInvalidTimeslot(ReservationRequest reservationRequest){
        if (reservationRequest == null ||
                CollectionUtils.isEmpty(reservationRequest.getReservationDtos()) ||
                reservationRequest.getReservationDtos().stream().anyMatch(Objects::isNull)) return "You must specify timeslots!";
        return null;
    }

    private String hasInvalidDate(ReservationRequest reservationRequest){
        boolean invalidTime = reservationRequest.getReservationDtos().stream().filter(Objects::nonNull).anyMatch(reservationDto ->  reservationDto.getStart() <= 0 || reservationDto.getEnd() == 0);
        if(invalidTime) return "You must specify valid star end times";
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


            }


        }

        return null;
    }
}
