package com.uns.ac.rs.schedulerservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourtNewDto {

    private String dimension;
    private String type;
    private Boolean covered;
    private Integer price;
    private String name;
    private String url;
}
