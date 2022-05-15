package com.uns.ac.rs.schedulerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uns.ac.rs.schedulerservice.configuration.JmsConfig;
import com.uns.ac.rs.schedulerservice.model.Court;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.service.impl.MinioService;
import common.events.Student;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class MinioController {

    @Value("${minio.bucket.name}")
    private String minioBucketAttachment;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;



    @PostMapping("/test/{msg}")
    public String artemis(@PathVariable("msg")String msg) throws JsonProcessingException {
        Gson gson = new Gson();
        jmsTemplate.convertAndSend(JmsConfig.RESERVATIONS_QUEUE, gson.toJson(new Student(msg)));
        return "OK";

    }

    @RequestMapping(value = "/upload", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public String uploadCourtImages(@RequestParam("file") MultipartFile multipartFile){

            try {
                String objectName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Court court = Court.builder()
                        .mimeType(multipartFile.getContentType())
                        .name(multipartFile.getOriginalFilename())
                        .objectName(objectName)
                        .url(minioService.save(multipartFile.getInputStream(), minioBucketAttachment, objectName, multipartFile.getOriginalFilename(), multipartFile.getContentType()))
                        .created(String.valueOf(System.currentTimeMillis()))
                        .build();
                final Court save = courtRepository.save(court);
                return save.getUrl();

            } catch (IOException e) {
                e.printStackTrace();
                throw new  IllegalArgumentException("Cannot convert InputStream!");
            }

    }
}
