package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationDto;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.BookingDto;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;

@Order(1)
@Component
@NoArgsConstructor
public class ValidatorB extends Validator{

    private static final EnumSet<DayOfWeek> WORK_DAYS = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
    private static final EnumSet<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    @Override
    public BookingDto handle(ReservationRequest reservationRequest) {
        //ValidatorB. -> Check business validation


        boolean hasInvalidReservationTime = reservationRequest.getReservationDtos().stream().anyMatch(reservationDto -> !workDayIsOK(reservationDto) || !weekendIsOK(reservationDto));

        if (hasInvalidReservationTime) return errorResponse("Each reservation must be made between 18h and 23h on work days and between 17h and 22h on weekends");


        return checkNext(reservationRequest);
    }

    private BookingDto errorResponse(String errorMessage){
        return BookingDto.builder().hasErrorMessage(true).errorMessage(errorMessage).build();
    }

    private boolean workDayIsOK(ReservationDto reservationDto){

        LocalDateTime start =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDto.getStart()),
                        TimeZone.getDefault().toZoneId());

        if (WORK_DAYS.contains(start.getDayOfWeek()) && 18 < start.getHour() && 23 > start.getHour()) return true;

        return false;
    }

    private boolean weekendIsOK(ReservationDto reservationDto){

        LocalDateTime start =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(reservationDto.getStart()),
                        TimeZone.getDefault().toZoneId());

        if (WEEKEND_DAYS.contains(start.getDayOfWeek()) && 17 < start.getHour() && 22 > start.getHour()) return true;

        return false;
    }
}
