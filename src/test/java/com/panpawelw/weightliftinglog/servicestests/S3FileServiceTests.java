package com.panpawelw.weightliftinglog.servicestests;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.services.S3FileService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
    service.setAmazonS3Client(client);
  }

  @Test
  public void testStoreAllFilesByWorkout() {

  }

  @Test
  public void testGetFileByWorkoutAndFilename() {

  }

  @Test
  public void testDeleteFileByWorkoutAndFilename() {
    WorkoutDeserialized testWorkout = new WorkoutDeserialized(1L, "Test title", null,
        null, new User(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    ReflectionTestUtils.setField(service, "bucketName", "correctbucketname");

    service.deleteFileByWorkoutAndFilename(testWorkout, "correctfilename.mp3");
    verify(client).deleteObject("correctbucketname", "1\\correctfilename.mp3");
  }

  @Test
  public void testDeleteFileByWorkoutAndFilenameWithIncorrectParameters() {
    WorkoutDeserialized testWorkout = new WorkoutDeserialized(1L, "Test title", null,
        null, new User(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    ReflectionTestUtils.setField(service, "bucketName", "correctbucketname");
    doThrow(AmazonClientException.class)
        .when(client).deleteObject("correctbucketname", "1\\incorrectfilename.mp3");
    try {
      service.deleteFileByWorkoutAndFilename(testWorkout, "incorrectfilename.mp3");
    }catch (RuntimeException exception) {
      assertEquals(exception.getMessage(), "Error deleting file!");
    }
    verify(client).deleteObject("correctbucketname", "1\\incorrectfilename.mp3");
  }

  @Test
  public void testDeleteAllFilesByWorkoutId() {

  }
}
