package com.uns.ac.rs.schedulerservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourtEditDto {

    private Integer courtId;
    private String dimension;
    private Integer price;
    private String type;
    private Boolean covered;
    private String name;
    private String url;

}
