package com.uns.ac.rs.schedulerservice.dto.response;

import com.uns.ac.rs.schedulerservice.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CourtData {

    private Integer courtId;
    private String courtName;
    //add more data to Court dimensions, type, price tag etc..
    private List<ReservationCourtInfo> reservationCourtInfos;

    public CourtData(Integer courtId, String courtName, Integer reservationId, String start, String end){
        if(this.courtId == null) this.courtId = courtId;
        if(this.courtName == null) this.courtName = courtName;
        if (reservationCourtInfos == null)this.reservationCourtInfos = new ArrayList<>();
        this.reservationCourtInfos.add(new ReservationCourtInfo(reservationId, start, end));
    }
}
