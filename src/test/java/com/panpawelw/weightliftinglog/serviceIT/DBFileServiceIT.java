package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.services.DBFileService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

  }

  @Test
  public void getFileByWorkoutAndFilenameSucceeds() {
    MediaFile testFile = service.getFileByWorkoutIdAndFilename(5L, "testfile1.mp3");

    assertArrayEquals(new byte[]{110, -96, 7, 47, 49, 24, 41, 113, 103, 123}, testFile.getContent());
  }

  @Test
  public void deleteFileByWorkoutAndFilenameSucceeds() {
    long initialDatabaseCount = service.count();
    WorkoutDeserialized testWorkout = workoutService.findWorkoutById(6);

    service.deleteFileByWorkoutAndFilename(testWorkout, testWorkout.getFilenames().get(0));
    assertEquals(1, initialDatabaseCount - service.count());
  }

  @Test
  public void deleteAllFilesByWorkoutIdSucceeds() {

  }
}
