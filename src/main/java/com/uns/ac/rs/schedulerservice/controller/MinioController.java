package com.uns.ac.rs.schedulerservice.controller;

import com.uns.ac.rs.schedulerservice.model.Court;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.service.impl.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
