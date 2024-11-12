package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hyeonmusic.MySongSpace.exception.FileDeleteException;
import com.hyeonmusic.MySongSpace.exception.FileNotFoundException;
import com.hyeonmusic.MySongSpace.exception.FileUploadException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String uploadFile(MultipartFile file, String fileType) {
        try {
            // 파일 이름에 고유한 식별자를 추가하여 중복 방지
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 폴더 경로 설정: 음악 파일은 'music/', 이미지 커버는 'covers/' 폴더에 저장
            String folderPath = fileType.equals("music") ? "music/" : "covers/";
            String fullFilePath = folderPath + fileName;
            if (fileType.equals("covers")) {
                throw new FileUploadException(FILE_UPLOAD_FAILED);
            }
            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucketName, fullFilePath, file.getInputStream(), metadata);

            // 업로드한 파일의 path 반환
            return amazonS3.getUrl(bucketName, fullFilePath).getPath().toString();

        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_FAILED);
        }
    }

    // 파일 삭제 메소드 추가
    @Transactional
    public void deleteFile(String filePath) {
        try {
            // URL 디코딩
            String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString());
            // S3에서 파일 삭제
            if (!amazonS3.doesObjectExist(bucketName, decodedFilePath)) {
                throw new FileNotFoundException(FILE_NOT_FOUND);
            }
            amazonS3.deleteObject(bucketName, decodedFilePath);
        } catch (Exception e) {
            throw new FileDeleteException(FILE_DELETE_FAILED);
        }
    }

}
