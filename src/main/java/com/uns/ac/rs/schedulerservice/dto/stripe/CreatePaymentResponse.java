package com.uns.ac.rs.schedulerservice.dto.stripe;

import lombok.Data;

@Data
public class CreatePaymentResponse {
    private String clientSecret;
    public CreatePaymentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}