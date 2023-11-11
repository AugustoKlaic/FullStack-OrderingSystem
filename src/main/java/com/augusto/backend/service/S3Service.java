package com.augusto.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.augusto.backend.service.exception.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

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

    // used for testing purposes
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

    public Mono<URI> uploadFile(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(inputStream -> this.uploadFile(inputStream.asInputStream(), filePart.filename(),
                                Objects.requireNonNull(filePart.headers().getContentType()))
                        .doOnNext(uri -> DataBufferUtils.release(inputStream)))
                .doOnError(e -> LOG.info("Problem retrieving file: " + e.getMessage()));
    }

    public Mono<URI> uploadFile(InputStream inputStream, String fileName, MediaType contentType) {

        LOG.info("Starting file upload: ");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType.getType());
        s3Client.putObject(bucketName, fileName, inputStream, objectMetadata);
        LOG.info("Upload finished");

        try {
            return Mono.just(s3Client.getUrl(bucketName, fileName).toURI());
        } catch (URISyntaxException e) {
            return Mono.error(new FileException("Error converting URL to URI."));
        }
    }
}