package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.repositories.FileRepository;
import com.panpawelw.weightliftinglog.services.DBFileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DBFileServiceTests {

  private static final WorkoutDeserialized TEST_WORKOUT = new WorkoutDeserialized(1L,
      "Test title", null, null, new User(), new ArrayList<>(), new ArrayList<>(),
      new ArrayList<>(Arrays.asList("audio_file.mp3", "photo.jpg", "video_clip.mp4")));

  private static final MultipartFile[] TEST_WORKOUT_FILES = new MultipartFile[]{
      new MockMultipartFile("testaudio.mp3", "testaudio.mp3",
          "audio/mpeg", new byte[]{110, (byte) 160, 7, 47, 49, 24, 41, 113, 103, 123}),
      new MockMultipartFile("testimage.bmp", "testimage.bmp",
          "image/bmp", new byte[]{41, 91, 115, 16, 22, 118, 122, 49, 28, 97}),
      new MockMultipartFile("testvideo.mp4", "testvideo.mp4",
          "video/mp4", new byte[]{113, 17, 78, 6, 89, 24, 23, (byte) 199, 83, 22}),
  };

  @Mock
  private FileRepository repository;

  private DBFileService service;

  @Before
  public void setup() {
    this.service = new DBFileService(repository);
  }

  @Test
  public void testStoreAllFilesByWorkout() throws IOException {
    service.storeAllFilesByWorkout(TEST_WORKOUT, TEST_WORKOUT_FILES);

    verify(repository).saveAll(any());
    verify(repository).flush();
    assertTrue(TEST_WORKOUT.getFilenames().containsAll(new ArrayList<>(
        Arrays.asList("testaudio.mp3", "testimage.bmp", "testvideo.mp4"))));
  }

  @Test
  public void testGetFileByWorkoutIdAndFilename() {
    MediaFile testFile = new MediaFile(null, 3L, "audio_file.mp3", "audio/mpeg",
        new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98});
    when(repository.findFileByWorkoutIdAndFilename(1L, "audio_file.mp3")).thenReturn(testFile);

    assertEquals(service.getFileByWorkoutIdAndFilename(1L, "audio_file.mp3"), testFile);
    verify(repository).findFileByWorkoutIdAndFilename(1L, "audio_file.mp3");
  }

  @Test
  public void testDeleteFileByWorkoutAndFilename() {
    when(repository.deleteByWorkoutIdAndFilename(TEST_WORKOUT.getId(),
        "testaudio.mp3")).thenReturn(1L);

    service.deleteFileByWorkoutAndFilename(TEST_WORKOUT, "testaudio.mp3");
    verify(repository).deleteByWorkoutIdAndFilename(TEST_WORKOUT.getId(), "testaudio.mp3");
  }

  @Test
  public void testDeleteAllFilesByWorkoutId() {
    when(repository.deleteAllByWorkoutId(1L)).thenReturn(3L);

    service.deleteAllFilesByWorkoutId(1L);
    verify(repository).deleteAllByWorkoutId(1L);
  }
}
