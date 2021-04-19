package com.panpawelw.weightliftinglog.servicestests;

import com.amazonaws.services.s3.AmazonS3;
import com.panpawelw.weightliftinglog.services.S3FileService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class S3FileServiceTests {

  @Mock
  private AmazonS3 client;

  @Mock
  private WorkoutService workoutService;

  private S3FileService service;

  @Before
  public void setup() {
    this.service = new S3FileService(workoutService);
  }

  @Test
  public void testStoreAllFilesByWorkout() {

  }

  @Test
  public void testGetFileByWorkoutAndFilename() {

  }

  @Test
  public void testDeleteFileByWorkoutAndFilename() {

  }

  @Test
  public void testDeleteAllFilesByWorkoutId() {

  }
}
