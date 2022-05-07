package com.uns.ac.rs.schedulerservice.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.activation.DataSource;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;


    public void deleteByObjectName(String objectName, String bucketName) {

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            //.versionId("my-versionid")
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                | InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
                | ServerException | XmlParserException | IOException e) {
            e.printStackTrace();
        }
        return;

    }


    public String save(DataSource ds, String bucketName, String objectName) {

        try {
            UploadObjectArgs.Builder uploadBuilder = UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName) //this will be the name of a file (with extension) in Object Storage folder; it can be initialized to any name we choose
                    .filename(getPath(ds).toString());

//		minioClient.putObject(
//				PutObjectArgs.builder()
//				.bucket(bucketName)
//				.object(objactName).stream(
//		    	    		ds.getInputStream(), -1, 10485760)
//		    	        .contentType(ds.getContentType()) //for now only PDF documents
//		    	        .build());

            minioClient.uploadObject(uploadBuilder.build());


            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)  //this will be the name of a file (with extension) in Object Storage server folder;
                            .expiry(7, TimeUnit.DAYS) //optional
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                | InternalException | InvalidBucketNameException | InvalidExpiresRangeException
                | InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return "";
    }

    private Path getPath(DataSource ds) throws IOException {

        Path tempFile = Files.createTempFile(ds.getName(), getExtension(ds.getContentType()));
        try (InputStream in = ds.getInputStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

    private String getExtension(String mimeType) {

        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mType;
        try {
            mType = allTypes.forName(mimeType);
            return mType.getExtension();
        } catch (MimeTypeException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot find extension type!");
        }

    }


    public InputStream findDataSourceByObjectName(String objectName, String bucketName) {

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());


        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String save(InputStream is, String bucketName, String objectName, String fileName, String contentType) {
        try {
//			UploadObjectArgs.Builder uploadBuilder = UploadObjectArgs.builder()
//				  .bucket(bucketName)
//		          .object(objectName) //this will be the name of a file (with extension) in Object Storage folder; it can be initialized to any name we choose
//		          .filename(getPath(is, fileName, contentType).toString());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName).stream(
                                    is, -1, 10485760)
                            .contentType(contentType) //for now only PDF documents
                            .build());

            //minioClient.uploadObject(uploadBuilder.build());


            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)  //this will be the name of a file (with extension) in Object Storage server folder;
                            .expiry(7, TimeUnit.DAYS) //optional
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                | InternalException | InvalidBucketNameException | InvalidExpiresRangeException
                | InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
                | IOException e) {

            e.printStackTrace();

        }
        return "";
    }

}
