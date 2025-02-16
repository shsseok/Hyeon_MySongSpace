package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadToS3(MultipartFile file, String fullFilePath, ObjectMetadata metadata) throws IOException {
        amazonS3.putObject(bucketName, fullFilePath, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fullFilePath).getPath().toString();
    }

    public boolean doesFileExist(String filePath) {
        return amazonS3.doesObjectExist(bucketName, filePath);
    }

    public void deleteFileToS3(String filePath) {
        amazonS3.deleteObject(bucketName, filePath);
    }

}
