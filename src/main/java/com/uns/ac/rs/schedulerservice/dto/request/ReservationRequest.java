package com.uns.ac.rs.schedulerservice.dto.request;

import com.stripe.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationRequest {

    private Integer user;
    private Integer courtId;
    private PaymentMethod paymentMethod;
    private List<ReservationDto> reservationDtos;
}
