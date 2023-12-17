package com.example.giffunding.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class StorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public StorageService(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                          @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                          @Value("${cloud.aws.region.static}") String region) {
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }

    public String uploadFile(byte[] fileData, String fileName) {
        String key = UUID.randomUUID() + "-" + fileName;
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromByteBuffer(ByteBuffer.wrap(fileData)));
        return key;
    }
}
