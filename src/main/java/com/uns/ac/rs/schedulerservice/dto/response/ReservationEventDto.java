package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ReservationEventDto implements Serializable {


    private List<ReservationDto> reservationDtos;
    private Integer userId;
    private String username;
    private Integer courtId;
}

