package com.augusto.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);
    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Autowired
    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String localFilePath) {
        try {
            File file = new File(localFilePath);
            LOG.info("Starting file upload: ");
            s3Client.putObject(new PutObjectRequest(bucketName, "testFile", file));
            LOG.info("Upload finished");
        } catch (AmazonServiceException ase) {
            LOG.info("Server erro: " + ase.getStatusCode() + "Message: " + ase.getMessage());
        } catch (AmazonClientException ace) {
            LOG.info(ace.getMessage());
        }
    }
}
