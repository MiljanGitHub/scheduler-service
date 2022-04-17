package com.uns.ac.rs.schedulerservice.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationRequest {

    private Integer user;
    private Integer courtId;
    private List<ReservationDto> reservationDtos;
}
