package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteResponse {

    private String reason;
}