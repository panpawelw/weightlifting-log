package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.constants;
import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.services.DBFileService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBFileServiceIT {

  @Autowired
  private DBFileService service;

  @Autowired
  private WorkoutService workoutService;

  @Test
  public void storeAllFilesByWorkoutSucceeds() {
    long initialDatabaseCount = service.count();
    WorkoutDeserialized testWorkout = workoutService.findWorkoutById(5);
    MultipartFile[] testFiles = constants.TEST_WORKOUT_FILES;
    List<String> filenames = testWorkout.getFilenames();
    for(MultipartFile file : testFiles) {
      filenames.add(file.getName());
    }
    testWorkout.setFilenames(filenames);

    try {
      service.storeAllFilesByWorkout(testWorkout, testFiles);
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals(3, service.count() - initialDatabaseCount);
  }

  @Test
  public void getFileByWorkoutAndFilenameSucceeds() {
    MediaFile testFile = service.getFileByWorkoutIdAndFilename(6L, "testfile4.mp3");

    assertArrayEquals(new byte[]{110, -96, 7, 47, 49, 24, 41, 113, 103, 123}, testFile.getContent());
  }

  @Test
  public void deleteFileByWorkoutAndFilenameSucceeds() {
    Long initialDatabaseCount = service.count();
    WorkoutDeserialized testWorkout = workoutService.findWorkoutById(6);

    service.deleteFileByWorkoutAndFilename(testWorkout, testWorkout.getFilenames().get(1));

    assertEquals(1, initialDatabaseCount - service.count());
  }

  @Test
  public void deleteFileByWorkoutAndFilenameFails() {
    Long initialDatabaseCount = service.count();
    WorkoutDeserialized testWorkout = workoutService.findWorkoutById(6);

    service.deleteFileByWorkoutAndFilename(testWorkout, "madeupfilename.lol");

    assertEquals(initialDatabaseCount, service.count());
  }

  @Test
  public void deleteAllFilesByWorkoutIdSucceeds() {
    Long initialDatabaseCount = service.count();

    service.deleteAllFilesByWorkoutId(7);

    assertEquals(3, initialDatabaseCount - service.count());
  }
}
