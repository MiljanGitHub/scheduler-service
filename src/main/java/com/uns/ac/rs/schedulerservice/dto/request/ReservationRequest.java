package com.uns.ac.rs.schedulerservice.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonValue;
import com.uns.ac.rs.schedulerservice.model.PaymentMethod;
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
