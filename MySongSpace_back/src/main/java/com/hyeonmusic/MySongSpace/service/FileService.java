package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String fileType) {
        try {
            // 파일 이름에 고유한 식별자를 추가하여 중복 방지
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 폴더 경로 설정: 음악 파일은 'music/', 이미지 커버는 'covers/' 폴더에 저장
            String folderPath = fileType.equals("music") ? "music/" : "covers/";
            String fullFilePath = folderPath + fileName;

            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucketName, fullFilePath, file.getInputStream(), metadata);

            // 업로드한 파일의 URL 반환
            return amazonS3.getUrl(bucketName, fullFilePath).toString();

        } catch (IOException e) {
            // 예외 처리: 로그를 남기거나, 사용자에게 적절한 메시지를 반환
            System.err.println("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }
    }
}
