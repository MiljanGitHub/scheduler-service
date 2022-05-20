package com.uns.ac.rs.schedulerservice.dto.request;

import com.uns.ac.rs.schedulerservice.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationRequest {

    private Integer user;
    private Integer courtId;
    private String paymentIntent;
    private long total;
    private PaymentMethod paymentMethod;
    private List<ReservationDto> reservationDtos;
}
