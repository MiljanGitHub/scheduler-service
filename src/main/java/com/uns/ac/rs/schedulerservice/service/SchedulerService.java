package com.uns.ac.rs.schedulerservice.service;

import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;

import java.util.List;

public interface SchedulerService {

    List<CourtInfo> findAllCourtInfo();
}
