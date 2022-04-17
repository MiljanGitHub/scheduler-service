package com.uns.ac.rs.schedulerservice.service.impl;

import com.uns.ac.rs.schedulerservice.dto.response.CourtData;
import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;
import com.uns.ac.rs.schedulerservice.dto.response.ReservationCourtInfo;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final CourtRepository courtRepository;

    @Override
    public List<CourtInfo> findAllCourtInfo() {
        return courtRepository.findCourtInfoList();
    }

    @Override
    public CourtData findAllReservationCourtInfo(int courtId) {
        return courtRepository.findReservationCourtInfoList(courtId, System.currentTimeMillis());
    }
}
