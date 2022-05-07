package com.uns.ac.rs.schedulerservice.configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;


@Configuration
public class MinioConfig {

    @Value("${minio.client.username}")
    private String minioUsername;

    @Value("${minio.client.password}")
    private String minioPassword;

    @Value("${minio.bucket.endpoint}")
    private String minioEndpoint;

    @Value("${minio.bucket.name}")
    private String minioBucketAttachment;


    @Bean(name = "minioClient")
    public MinioClient minIoClient() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, IllegalArgumentException, InvalidBucketNameException, RegionConflictException {

        MinioClient client =  MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioUsername, minioPassword)
                .build();

        boolean bucketAttachmentExist = client.
                bucketExists(BucketExistsArgs.builder().bucket(minioBucketAttachment).build());

        if (!bucketAttachmentExist) client.makeBucket(MakeBucketArgs.builder().bucket(minioBucketAttachment).build());

        return client;
    }

}
