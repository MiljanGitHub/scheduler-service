package com.uns.ac.rs.schedulerservice.controller;

import com.uns.ac.rs.schedulerservice.authorization.Authorizable;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.request.UserAndCourtId;
import com.uns.ac.rs.schedulerservice.dto.response.*;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/scheduler-service")
public class SchedulerController {

    private final SchedulerService schedulerService;

    //@Authorizable(roles = {"USER", "ADMIN"})
    @GetMapping("/courts-info")
    @ApiOperation(value = "Finds Court Info data", notes = "Returns Court Info data", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<List<CourtInfo>> findAllCourtInfo(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(schedulerService.findAllCourtInfo());
    }


    @GetMapping("/court-reservation-info/{courtId}")
    @ApiOperation(value = "Finds Court Reservation Info data", notes = "Returns Court Info Reservation data", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<CourtData> findAllReservationCourtInfo(HttpServletRequest httpServletRequest,@PathVariable("courtId") Integer courtId){
        return ResponseEntity.ok(schedulerService.findAllReservationCourtInfo(courtId));
    }

    @Authorizable(roles = {"USER", "ADMIN"})
    @PostMapping("/court-reservation")
    @ApiOperation(value = "Make new reservations", notes = "Returns confirmation and error message", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<BookingDto> createReservations(HttpServletRequest httpServletRequest, @RequestBody ReservationRequest  reservationRequest){
        return ResponseEntity.ok(schedulerService.createReservations(reservationRequest));
    }

    @Authorizable(roles = {"USER", "ADMIN"})
    @PostMapping("/reservations-by-user-and-court")
    @ApiOperation(value = "Finds Court Reservation by user and court", notes = "Returns Court Info Reservation data", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<List<ReservationByCourtAndUser>> findReservationsByCourtAndUser(HttpServletRequest httpServletRequest, @RequestBody UserAndCourtId userAndCourtId){
        return ResponseEntity.ok(schedulerService.findReservationsByCourtAndUser(userAndCourtId.getUserId(), userAndCourtId.getCourtId()));
    }

    @Authorizable(roles = {"ADMIN"})
    @DeleteMapping("/reservation/{reservationId}")
    @ApiOperation(value = "Deletes reservation timeslot", notes = "Deletes reservation timeslot", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<DeleteResponse> deleteReservation(HttpServletRequest httpServletRequest, @PathVariable("reservationId") String reservationId){
        return ResponseEntity.ok(schedulerService.deleteReservation(Integer.parseInt(reservationId)));
    }

    @Authorizable(roles = {"ADMIN"})
    @RequestMapping(value = "/court/create-new", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public ResponseEntity<CourtResponse> createNewCourt(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile attachment, @RequestParam("court") String courtRequest) throws IOException {
        return ResponseEntity.ok(schedulerService.createCourt(attachment, courtRequest));
    }

    @Authorizable(roles = {"ADMIN"})
    @RequestMapping(value = "/court/edit", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public ResponseEntity<CourtResponse> editCourt(HttpServletRequest httpServletRequest, @RequestParam("file") @Nullable MultipartFile attachment, @RequestParam("court") String courtRequest) throws IOException {
        return ResponseEntity.ok(schedulerService.ediCourt(attachment, courtRequest));
    }

    @Authorizable(roles = {"ADMIN"})
    @DeleteMapping("/court/{courtId}")
    @ApiOperation(value = "Deactivate court", notes = "Deactivate court", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<CourtResponse> deactivateCourt(HttpServletRequest httpServletRequest, @PathVariable("courtId") Integer courtId){
        return ResponseEntity.ok(schedulerService.deactivateCourt(courtId));
    }

    @Authorizable(roles = {"USER", "ADMIN"})
    @GetMapping("/reservation-event/{paymentId}")
    @ApiOperation(value = "Get ReservationEventDto", notes = "Returns ReservationEventDto", response = ResponseEntity.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Successfully created a new item"),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Internal error")
            })
    public ResponseEntity<ReservationEventDto> fetchReservationEventDto(HttpServletRequest httpServletRequest, @PathVariable("paymentId")Integer paymentId){
        return ResponseEntity.ok(schedulerService.fetchReservationEventDto(paymentId));
    }


}
