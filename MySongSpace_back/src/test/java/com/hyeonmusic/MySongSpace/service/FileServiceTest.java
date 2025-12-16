package com.hyeonmusic.MySongSpace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hyeonmusic.MySongSpace.common.utils.FileType;
import com.hyeonmusic.MySongSpace.entity.FilePath;
import com.hyeonmusic.MySongSpace.exception.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Spy
    @InjectMocks
    private FileService fileService;

    @Mock
    private S3Service s3Service;

    @Test
    @DisplayName("음악파일과 이미지파일 모두 정상적으로 업로드 되면 파일 경로를 반환")
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
    @DisplayName("파일 삭제시 S3에 파일이 없으면 예외 던짐")
    void isExistFileThrowsException() {
        //given
        String filePath = "covers/TestFilePath.jpg";
        //when
        when(s3Service.doesFileExist(filePath)).thenReturn(false);
        //then
        assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(filePath));
    }

    @Test
    @DisplayName("파일 경로 정규화 작업이 정상적으로 진행 (경로 앞 / 제거 + 디코딩 작업)")
    void isSuccessFileNormalizeFilePath() {
        //given
        String filePath = "/covers/%EB%B0%B0%EA%B2%BD.jpg";
        //when
        String result = fileService.normalizeFilePath(filePath);
        //then
        String exceptedResult = "covers/배경.jpg";
        assertEquals(result, exceptedResult);

    }

    @Test
    @DisplayName("음악 파일과 커버 파일이 모두 존재할 때 deleteFile이 각각 호출되는지 확인")
    void deleteIfFileUploaded_callsDeleteFileForBothPaths() {
        // given
        String musicPath = "music/test.mp3";
        String coverPath = "covers/test.jpg";
        // when
        doNothing().when(fileService).deleteFile(anyString());
        fileService.deleteIfFileUploaded(musicPath, coverPath);
        // then
        verify(fileService, times(1)).deleteFile(musicPath);
        verify(fileService, times(1)).deleteFile(coverPath);
    }

    @Test
    @DisplayName("음악 파일 경로만 존재할 때 deleteFile이 음악 파일에 대해서만 호출되는지 확인")
    void deleteIfFileUploaded_callsDeleteFileForMusicOnly() {
        // given
        String musicPath = "music/test.mp3";
        String coverPath = null;
        // when
        doNothing().when(fileService).deleteFile(anyString());
        fileService.deleteIfFileUploaded(musicPath, coverPath);
        // then
        verify(fileService, times(1)).deleteFile(musicPath);
        verify(fileService, never()).deleteFile(coverPath);
    }

    @Test
    @DisplayName("커버 파일 경로만 존재할 때 deleteFile이 커버 파일에 대해서만 호출되는지 확인")
    void deleteIfFileUploaded_callsDeleteFileForCoverOnly() {
        // given
        String musicPath = null;
        String coverPath = "covers/test.jpg";
        // when
        doNothing().when(fileService).deleteFile(anyString());
        fileService.deleteIfFileUploaded(musicPath, coverPath);
        // then
        verify(fileService, never()).deleteFile(musicPath);
        verify(fileService, times(1)).deleteFile(coverPath);
    }
}
