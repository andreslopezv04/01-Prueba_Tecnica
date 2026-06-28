package com.vortexbird.orders.infrastructure.adapter.out.storage;

import com.vortexbird.orders.application.port.out.StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.util.UUID;

/*
 * Adaptador de StoragePort.
 * Hay que elegirlo por perfil de Spring.
 */

@Component
@Profile("s3")
public class S3StorageAdapter implements StoragePort {

    private final S3Client s3Client;
    private final String bucket;

    public S3StorageAdapter(
            @Value("${storage.s3.endpoint}") String endpoint,
            @Value("${storage.s3.region}") String region,
            @Value("${storage.s3.access-key}") String accessKey,
            @Value("${storage.s3.secret-key}") String secretKey,
            @Value("${storage.s3.bucket}") String bucket) {
        this.bucket = bucket;
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .forcePathStyle(true)
                .build();
    }

    @Override
    public String store(byte[] content, String filename) {
        String key = UUID.randomUUID() + "-" + filename;
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucket).key(key).build(),
                RequestBody.fromBytes(content));
        return key;
    }


    @Override
    public byte[] load(String reference) {
        // la referencia es la key
        return s3Client.getObjectAsBytes(
                GetObjectRequest.builder().bucket(bucket).key(reference).build()
        ).asByteArray();
    }
}