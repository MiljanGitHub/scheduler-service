package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CourtData {

    private Integer courtId;
    private String name;
    private String url;
    private String dimension;
    private String type;
    private Boolean covered;
    private List<ReservationDto> reservationCourtInfos;

    public CourtData(Integer courtId, String name, String url, String dimension,String type, Boolean covered, Integer reservationId, String start, String end){
        if(this.courtId == null) this.courtId = courtId;
        if(this.name == null) this.name = name;
        if(this.url == null) this.url = url;
        if(this.dimension == null) this.dimension = dimension;
        if(this.type == null) this.type = type;
        if(this.covered == null) this.covered = covered;

    }
}
