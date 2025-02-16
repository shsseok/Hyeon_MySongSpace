package com.hyeonmusic.MySongSpace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.hyeonmusic.MySongSpace.common.utils.FileType;
import com.hyeonmusic.MySongSpace.entity.FilePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
//(1) 성공 케이스
//uploadTrackFileAndTrackCoverFile() 실행 시 정상적으로 업로드되어 파일 경로 반환 확인
//uploadFile() 실행 후 반환된 S3 URL이 예상 경로와 일치하는지 검증
//deleteFile() 실행 후 S3에서 해당 파일이 삭제되었는지 확인
//
//(2) 실패 케이스
//S3 업로드 중 예외 발생 시 FileUploadException이 발생하는지 확인
//S3에 존재하지 않는 파일을 삭제할 때 FileNotFoundException이 발생하는지 확인
//S3 서비스 자체 오류 발생 시 S3UploadException 처리 검증

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Spy
    @InjectMocks
    private FileService fileService;

    @Mock
    private AmazonS3 amazonS3;


    @Test
    @DisplayName("파일이 정상적으로 업로드 되어 파일 경로를 반환")
    void testUploadTrackFileAndTrackCoverFile_success() throws Exception {
        // given
        MockMultipartFile trackMusicFile = new MockMultipartFile("track.mp3", "테스트음악파일.mp3", "audio/mp3", "test-music-file".getBytes());
        MockMultipartFile trackCoverFile = new MockMultipartFile("cover.jpg", "테스트이미지파일.jpg", "image/jpeg", "test-image-file".getBytes());

        String expectedMusicPath = "music/테스트음악파일.mp3";
        String expectedCoverPath = "covers/테스트이미지파일.jpg";
        doReturn(expectedMusicPath).when(fileService).uploadFile(trackMusicFile, FileType.MUSIC);
        doReturn(expectedCoverPath).when(fileService).uploadFile(trackCoverFile, FileType.COVERS);
        // when
        FilePath result = fileService.uploadTrackFileAndTrackCoverFile(trackMusicFile, trackCoverFile);
        // then
        assertNotNull(result);
        assertEquals(expectedMusicPath, result.getMusicPath());
        assertEquals(expectedCoverPath, result.getCoverPath());
    }

    @Test
     void  () throws Exception {
        //given


        //when


        //then
    }
}
