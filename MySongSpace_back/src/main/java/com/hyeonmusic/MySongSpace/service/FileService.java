package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hyeonmusic.MySongSpace.common.utils.FileType;
import com.hyeonmusic.MySongSpace.entity.FilePath;
import com.hyeonmusic.MySongSpace.exception.FileDeleteException;
import com.hyeonmusic.MySongSpace.exception.FileNotFoundException;
import com.hyeonmusic.MySongSpace.exception.FileUploadException;
import com.hyeonmusic.MySongSpace.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    //파일 여러개 업로드
    public FilePath uploadTrackFileAndTrackCoverFile(MultipartFile trackMusicFile, MultipartFile trackCoverFile) {
        String coverPath = null;
        String musicPath = null;
        try {
            coverPath = uploadFile(trackCoverFile, FileType.COVERS);
            musicPath = uploadFile(trackMusicFile, FileType.MUSIC);
            FilePath filePath = new FilePath(musicPath, coverPath);
            return filePath;
        } catch (Exception e) {
            deleteIfFileUploaded(musicPath, coverPath);
            if (e instanceof S3UploadException) {
                throw new S3UploadException(S3_SERVICE_ERROR);
            } else {
                throw new FileUploadException(FILE_UPLOAD_FAILED);
            }
        }
    }

    //단일 파일 업로드
    public String uploadFile(MultipartFile file, String fileType) throws IOException {
        String fullFilePath = getFolderPath(fileType) + generateUniqueFileName(file.getOriginalFilename());
        ObjectMetadata metadata = createMetadata(file);
        return uploadToS3(file, fullFilePath, metadata);
    }

    public void deleteFile(String filePath) {
        try {
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }
            String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString());
            log.info("삭제할 파일={}", decodedFilePath);
            if (!amazonS3.doesObjectExist(bucketName, decodedFilePath)) {
                log.info("삭제할 파일이 스토리지에 없음");
                throw new FileNotFoundException(FILE_NOT_FOUND);
            }
            amazonS3.deleteObject(bucketName, decodedFilePath);
        } catch (Exception e) {
            //위의 경우를 제외한 모든 예외는 하나로 통일
            log.error("삭제 버그 에러 메시지", e);
            throw new FileDeleteException(FILE_DELETE_FAILED, e.getMessage());
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
                throw new IllegalArgumentException(fileType + "서버 오류");
        }
    }

    // 삭제 후속처리 로직
    private void deleteIfFileUploaded(String musicPath, String coverPath) {
        if (musicPath != null) {
            deleteFile(musicPath);
        }
        if (coverPath != null) {
            deleteFile(coverPath);
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

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
