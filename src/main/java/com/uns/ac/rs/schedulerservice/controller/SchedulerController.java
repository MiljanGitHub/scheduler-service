package com.uns.ac.rs.schedulerservice.controller;

import com.uns.ac.rs.schedulerservice.dto.response.CourtInfo;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;


    @GetMapping("/courts")
    @ApiOperation(value = "Finds Court Info data", notes = "Returns Court Info data", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<List<CourtInfo>> findAllCourtInfo(){
        return ResponseEntity.ok(schedulerService.findAllCourtInfo());
    }
}
