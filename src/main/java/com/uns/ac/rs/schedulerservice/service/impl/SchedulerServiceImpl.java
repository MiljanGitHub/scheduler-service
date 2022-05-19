package com.uns.ac.rs.schedulerservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uns.ac.rs.schedulerservice.dto.request.CourtEditDto;
import com.uns.ac.rs.schedulerservice.dto.request.CourtNewDto;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.*;
import com.uns.ac.rs.schedulerservice.model.Court;
import com.uns.ac.rs.schedulerservice.model.Payment;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.repository.FeignClientUserService;
import com.uns.ac.rs.schedulerservice.repository.PaymentRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import com.uns.ac.rs.schedulerservice.service.SchedulerService;
import com.uns.ac.rs.schedulerservice.service.impl.validators.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final CourtRepository courtRepository;
    private final List<Validator> validators;
    private final ReservationRepository reservationRepository;
    private final ObjectMapper objectMapper;
    private final MinioService minioService;
    private final PaymentRepository paymentRepository;
    private final FeignClientUserService feignClientUserService;

    @Value("${minio.bucket.name}")
    private String minioBucketAttachment;

    @Override
    public List<CourtInfo> findAllCourtInfo() {
        return courtRepository.findCourtInfoList();
    }

    @Override
    public CourtData findAllReservationCourtInfo(int courtId) {

        Court court = courtRepository.getById(courtId);
        List<ReservationDto> reservationDtos = reservationRepository.findByCourt(courtId, System.currentTimeMillis());

        return CourtData.builder().courtId(courtId)
                .covered(court.isCovered())
                .dimension(court.getDimension())
                .price(court.getPrice())
                .name(court.getName())
                .type(court.getType())
                .url(court.getUrl())
                .reservationCourtInfos(reservationDtos).build();
    }

    @Override
    public BookingDto createReservations(ReservationRequest reservationRequest) {

        //Cannot be empty
        //No more than 5 time slots (taken from config)

        //Validation 1. -> Check to see if requested timeslots are valid among them self's

        //Validation 2. -> Check business validation

        //Validation 3. -> Check each requested timeslot with time slot in DB

        //Validation 4. -> Payment Service

        //Save to DB
        //Notify admin by email
        //WebSocket


        BookingDto handle = validators.get(0).handle(reservationRequest);


        return handle;
    }

    @Override
    public List<ReservationByCourtAndUser> findReservationsByCourtAndUser(int userId, int courtId) {
        return reservationRepository.findReservationsByCourtAndUser(courtId, userId);
    }

    @Override
    public DeleteResponse deleteReservation(int reservationId) {

        Optional<Reservation> byId = reservationRepository.findById(reservationId);
        if (byId.isEmpty()) {
            return DeleteResponse.builder().reason("Error deleting a reservation!").build();
        }

        reservationRepository.deleteById(reservationId);

        return DeleteResponse.builder().reason("Successfully deleted reservation!").build();
    }

    @Override
    public CourtResponse createCourt(MultipartFile multipartFile, String data) throws IOException {

        CourtNewDto courtNewDto = objectMapper.readValue(data, CourtNewDto.class);
        String objectName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        Court c = Court.builder().covered(courtNewDto.getCovered()).dimension(courtNewDto.getDimension())
                .name(courtNewDto.getName())
                .price(courtNewDto.getPrice())
                .mimeType(multipartFile.getContentType())
                .objectName(objectName)
                .type(courtNewDto.getType())
                .active(true)
                .url(minioService.save(multipartFile.getInputStream(), minioBucketAttachment, objectName, multipartFile.getOriginalFilename(), multipartFile.getContentType()))
                .created(String.valueOf(System.currentTimeMillis())).build();


        Court save = courtRepository.save(c);
        if (save == null) CourtResponse.builder().reason("Error on server!").build();


        return CourtResponse.builder().reason("Successfully created new Court").build();
    }

    @Override
    public CourtResponse ediCourt(MultipartFile multipartFile, String data) throws IOException {

        CourtEditDto courtDto = objectMapper.readValue(data, CourtEditDto.class);

        Court c = courtRepository.getById(courtDto.getCourtId());

        c.setCovered(courtDto.getCovered());
        c.setDimension(courtDto.getDimension());
        c.setName(courtDto.getName());
        c.setType(courtDto.getType());
        c.setPrice(courtDto.getPrice());

        if (courtDto.getUrl() == null && multipartFile != null){
            String objectName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            c.setMimeType(multipartFile.getContentType());
            c.setObjectName(objectName);
            c.setUrl(minioService.save(multipartFile.getInputStream(), minioBucketAttachment, objectName, multipartFile.getOriginalFilename(), multipartFile.getContentType()));
        } else c.setUrl(courtDto.getUrl());

        Court saved = courtRepository.save(c);
        if (saved == null) CourtResponse.builder().reason("Error while editing server!").build();

        return CourtResponse.builder().reason("Successfully edited Court").build();


    }

    @Override
    public CourtResponse deactivateCourt(Integer courtId) {
        Court court = courtRepository.getById(courtId);
        long now = System.currentTimeMillis();
        if (court.getReservations().stream().anyMatch(r -> Long.parseLong(r.getStart()) > now)){
            return CourtResponse.builder().reason("At the moment you cannot deactivate this court since it has reservations in the future").build();
        } else {
            court.setActive(false);
            courtRepository.save(court);
        }

        return CourtResponse.builder().reason("Court deactivated").build();
    }

    @Override
    public ReservationEventDto fetchReservationEventDto(Integer paymentId) {

        Payment payment = paymentRepository.getById(paymentId);

        List<Reservation> reservations = payment.getReservations();

        List<ReservationDto> reservationDtos = reservations.stream().map(r ->
                ReservationDto.builder().start(r.getStart()).end(r.getEnd()).reservationId(r.getId()).build())
                .collect(Collectors.toList());

        int courtId = reservations.get(0).getCourt().getId();

        int userId = payment.getUserId();
        UserNameDto userNameDto = feignClientUserService.fetchUserData(userId);

        return ReservationEventDto.builder().reservationDtos(reservationDtos)
                .courtId(courtId).userId(userId)
                .username(userNameDto.getUsername()).build();

    }
}
