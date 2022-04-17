package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationCourtInfo {

    private Integer reservationId;
    private String start;
    private String end;

}
