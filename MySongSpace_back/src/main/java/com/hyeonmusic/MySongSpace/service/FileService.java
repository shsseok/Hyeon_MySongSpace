package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hyeonmusic.MySongSpace.exception.FileDeleteException;
import com.hyeonmusic.MySongSpace.exception.FileNotFoundException;
import com.hyeonmusic.MySongSpace.exception.FileUploadException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;
import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.FILE_UPLOAD_FAILED;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String fileType) {
        try {
            String folderPath = getFolderPath(fileType);
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String fullFilePath = folderPath + fileName;

            ObjectMetadata metadata = createMetadata(file);

            // 업로드한 파일의 상대 경로 반환 해준다!!
            return uploadToS3(file, fullFilePath, metadata);

        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_FAILED);
        }
    }

    // 파일 삭제 메소드 추가
    public void deleteFile(String filePath) {
        try {
            // URL 디코딩
            String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString());
            // S3에서 파일 삭제
            if (!amazonS3.doesObjectExist(bucketName, decodedFilePath)) {
                log.info("삭제할 파일이 스토리지에 없음");
                throw new FileNotFoundException(FILE_NOT_FOUND);
            }
            amazonS3.deleteObject(bucketName, decodedFilePath);
        } catch (FileNotFoundException e) {
            //삭제할 파일이 스토리지에 없으면 예외를 던짐
            throw e;
        } catch (IOException e) {
            throw new FileDeleteException(FILE_DELETE_FAILED);
        }
    }

    // 폴더 경로 결정
    private String getFolderPath(String fileType) {
        switch (fileType) {
            case "music":
                return "music/";
            case "covers":
                return "covers/";
            default:
                throw new IllegalArgumentException(fileType+"서버 오류");
        }
    }

    // 고유한 파일 이름 생성
    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    // 파일 메타데이터 생성
    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    // S3 업로드 수행
    private String uploadToS3(MultipartFile file, String fullFilePath, ObjectMetadata metadata) throws IOException {
        amazonS3.putObject(bucketName, fullFilePath, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fullFilePath).getPath().toString();
    }
}
