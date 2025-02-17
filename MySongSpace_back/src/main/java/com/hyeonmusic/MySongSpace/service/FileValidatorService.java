package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.common.utils.FileType;
import com.hyeonmusic.MySongSpace.exception.UnsupportedFileTypeException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Service
public class FileValidatorService {
    //한번만 생성한다 --> 왜냐면 상태가 변하지 않기 때문에
    private static final Tika tika = new Tika();

    public FileType getFileTypeFromMimeType(MultipartFile file) throws IOException {
        FileType fileType = isValidMimeTypeAndresolveFileType(file);
        if (fileType != null) {
            return fileType;
        }
        //지원하지 않는 파일 형식
        throw new UnsupportedFileTypeException(FILE_TYPE_UNSUPPORTED);
    }


    private FileType isValidMimeTypeAndresolveFileType(MultipartFile file) throws IOException {
        //tika를 통해 파일 검증 및 형식
        String detectedType = tika.detect(file.getBytes());
        if (detectedType.startsWith("audio/")) {
            return FileType.MUSIC;
        } else if (detectedType.startsWith("image/")) {
            return FileType.COVERS;
        }
        return null;
    }
}
