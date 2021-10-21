package com.panpawelw.weightliftinglog.servicetests;

import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.repositories.FileRepository;
import com.panpawelw.weightliftinglog.services.DBFileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.panpawelw.weightliftinglog.constants.TEST_WORKOUT;
import static com.panpawelw.weightliftinglog.constants.TEST_WORKOUT_FILES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DBFileServiceTests {

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
