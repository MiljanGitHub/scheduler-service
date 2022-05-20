package com.uns.ac.rs.schedulerservice.dto.response;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private Boolean authenticated;
    private String authenticationResponse;
    private String role;
}
