package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDto {

    private Integer reservationId;
    private String start;
    private String end;

    public ReservationDto(Integer reservationId, String start, String end){
        this.reservationId = reservationId;
        this.start = start;
        this.end = end;
    }

}
