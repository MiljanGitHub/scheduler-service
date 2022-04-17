package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Validator {

    private Validator next;

    public abstract String handle(ReservationRequest reservationRequest);

    /**
     * Runs check on the next handler in the chain or ends traversing if we're in last handler in
     * chain.
     */
    protected String checkNext(ReservationRequest reservationRequest) {
        if (next == null) {
            return null;
        }
        return next.handle(reservationRequest);
    }
}
