package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
@NoArgsConstructor
public class ValidatorA extends Validator{


    @Override
    public List<String> handle(ReservationRequest reservationRequest) {

        //ValidatorA. -> Check to see if requested timeslots are valid among them self's

        return null;
    }
}
