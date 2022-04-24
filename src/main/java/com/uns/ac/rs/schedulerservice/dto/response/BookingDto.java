package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDto {

    private boolean hasErrorMessage;
    private String errorMessage;
    private String clientSecret;
    private String successMessage;
}
