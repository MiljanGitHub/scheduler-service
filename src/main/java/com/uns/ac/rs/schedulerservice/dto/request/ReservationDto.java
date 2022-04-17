package com.uns.ac.rs.schedulerservice.dto.request;

import com.uns.ac.rs.schedulerservice.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDto {

    private PaymentMethod paymentMethod;
    private String start;
    private String end;
}
