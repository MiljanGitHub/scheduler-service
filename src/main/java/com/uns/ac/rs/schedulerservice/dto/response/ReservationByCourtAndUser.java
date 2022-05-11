package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Data;

@Data
public class ReservationByCourtAndUser {

    private Integer reservationId;
    private String start;
    private String end;
    private String paymentType;
    private Boolean paid;

    public ReservationByCourtAndUser(Integer reservationId, String start, String end, String paymentType, Boolean paid) {
        this.reservationId = reservationId;
        this.start = start;
        this.end = end;
        this.paymentType = paymentType;
        this.paid = paid;
    }
}
