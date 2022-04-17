package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@NoArgsConstructor
public class ValidatorD extends Validator{
    @Override
    public String handle(ReservationRequest reservationRequest) {
        //ValidatorB -> Payment Service (if user has card data and enough founds)
        return null;
    }
}
