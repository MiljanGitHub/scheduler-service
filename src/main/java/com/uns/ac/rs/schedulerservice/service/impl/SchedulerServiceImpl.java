package com.uns.ac.rs.schedulerservice.service.impl;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.CourtData;
import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import com.uns.ac.rs.schedulerservice.service.impl.validators.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final CourtRepository courtRepository;
    private final Validator validator;

    @Override
    public List<CourtInfo> findAllCourtInfo() {
        return courtRepository.findCourtInfoList();
    }

    @Override
    public CourtData findAllReservationCourtInfo(int courtId) {
        return courtRepository.findReservationCourtInfoList(courtId, System.currentTimeMillis());
    }

    @Override
    public Object createReservations(ReservationRequest object) {

        //Cannot be empty
        //No more than 5 time slots (taken from config)

        //Validation 1. -> Check to see if requested timeslots are valid among them self's

        //Validation 2. -> Check business validation

        //Validation 3. -> Check each requested timeslot with time slot in DB

        //Validation 4. -> Payment Service

        //Save to DB
        //Notify admin by email
        //WebSocket

        final List<String> errors = validator.handle(object);

        return null;
    }
}
