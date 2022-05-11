package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Data;

@Data
public class CourtList {

    private Integer courtId;
    private String courtName;

    public CourtList(Integer courtId, String courtName) {
        this.courtId = courtId;
        this.courtName = courtName;
    }
}
