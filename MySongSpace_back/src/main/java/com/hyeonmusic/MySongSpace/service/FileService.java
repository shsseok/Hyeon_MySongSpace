package com.hyeonmusic.MySongSpace.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hyeonmusic.MySongSpace.common.utils.FileType;
import com.hyeonmusic.MySongSpace.entity.FilePath;
import com.hyeonmusic.MySongSpace.exception.FileDeleteException;
import com.hyeonmusic.MySongSpace.exception.FileNotFoundException;
import com.hyeonmusic.MySongSpace.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final S3Service s3Service;

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
            throw new FileUploadException(FILE_UPLOAD_FAILED);
        }
    }

    //단일 파일 업로드
    public String uploadFile(MultipartFile file, String fileType) throws IOException {
        String fullFilePath = getFolderPath(fileType) + generateUniqueFileName(file.getOriginalFilename());
        ObjectMetadata metadata = createMetadata(file);
        return s3Service.uploadToS3(file, fullFilePath, metadata);
    }

    public void deleteFile(String filePath) {
        String decodedFilePath = normalizeFilePath(filePath);
        if (!s3Service.doesFileExist(decodedFilePath)) {
            throw new FileNotFoundException(FILE_NOT_FOUND);
        }
        //예외 처리 하는 부분을 S3Service에 책임을 넘기는 것이 옳다고 판단 테스트 로직 추후 작성
        try {
            s3Service.deleteFileToS3(filePath);
        } catch (Exception e) {
            throw new FileDeleteException(FILE_DELETE_FAILED, e.getMessage());
        }
    }

    //파일 정규화 작업(삭제시 S3에서 / 작업을 처리해줘야함)
    public String normalizeFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return filePath;
        }
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return URLDecoder.decode(filePath, StandardCharsets.UTF_8);
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

}
