package com.panpawelw.weightliftinglog.servicestests;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.panpawelw.weightliftinglog.models.MediaFile;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3FileServiceTests {

  private static final WorkoutDeserialized TEST_WORKOUT = new WorkoutDeserialized(1L,
      "Test title", null, null, new User(), new ArrayList<>(), new ArrayList<>(),
      Arrays.asList("audio_file.mp3", "photo.jpg", "video_clip.mp4"));

  @Mock
  private AmazonS3 client;

  @Mock
  private WorkoutService workoutService;

  private S3FileService service;

  @Before
  public void setup() {
    this.service = new S3FileService(workoutService);
    service.setAmazonS3Client(client);
    ReflectionTestUtils.setField(service, "bucketName", "correctbucketname");
  }

  @Test
  public void testStoreAllFilesByWorkout() {

  }

  @Test
  public void testGetFileByWorkoutIdAndFilename() {
    MediaFile testFile = new MediaFile(null, 3L, "testfile.mp3", "audio",
        new byte[] {69, 121, 101 , 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98});
    InputStream testFileInputStream = new ByteArrayInputStream(testFile.getContent());
    S3Object s3Object = mock(S3Object.class);
    ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
    when(client.getObject("correctbucketname", "3\\testfile.mp3")).thenReturn(s3Object);
    when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
    when(s3Object.getObjectContent()).thenReturn(
        new S3ObjectInputStream(testFileInputStream, null));
    when(s3Object.getKey()).thenReturn("testfile.mp3");
    when(objectMetadata.getContentType()).thenReturn("audio");
    assertEquals(testFile, service.getFileByWorkoutIdAndFilename(3L, "testfile.mp3"));
  }

  @Test
  public void testDeleteFileByWorkoutAndFilename() {
    service.deleteFileByWorkoutAndFilename(TEST_WORKOUT, "correctfilename.mp3");
    verify(client).deleteObject("correctbucketname", "1\\correctfilename.mp3");
  }

  @Test
  public void testDeleteFileByWorkoutAndFilenameWithIncorrectParameters() {
    doThrow(AmazonClientException.class)
        .when(client).deleteObject("correctbucketname", "1\\incorrectfilename.mp3");
    try {
      service.deleteFileByWorkoutAndFilename(TEST_WORKOUT, "incorrectfilename.mp3");
    }catch (RuntimeException exception) {
      assertEquals(exception.getMessage(), "Error deleting file!");
    }
    verify(client).deleteObject("correctbucketname", "1\\incorrectfilename.mp3");
  }

  @Test
  public void testDeleteAllFilesByWorkoutId() {
    when(workoutService.findWorkoutById(1)).thenReturn(TEST_WORKOUT);

    service.deleteAllFilesByWorkoutId(1);
    verify(client).deleteObject("correctbucketname", "1\\audio_file.mp3");
    verify(client).deleteObject("correctbucketname", "1\\photo.jpg");
    verify(client).deleteObject("correctbucketname", "1\\video_clip.mp4");
  }

  @Test
  public void testDeleteAllFilesByWorkoutIdWithIncorrectParameters() {
    when(workoutService.findWorkoutById(1)).thenReturn(TEST_WORKOUT);

    try {
      service.deleteAllFilesByWorkoutId(1);
    }catch (RuntimeException exception) {
      assertEquals(exception.getMessage(), "Error deleting files!");
    }
    verify(client).deleteObject("correctbucketname", "1\\audio_file.mp3");
    verify(client).deleteObject("correctbucketname", "1\\photo.jpg");
    verify(client).deleteObject("correctbucketname", "1\\video_clip.mp4");
  }
}
