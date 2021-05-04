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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3FileServiceTests {

  private static final WorkoutDeserialized TEST_WORKOUT = new WorkoutDeserialized(1L,
      "Test title", null, null, new User(), new ArrayList<>(), new ArrayList<>(),
      new ArrayList<>(Arrays.asList("audio_file.mp3", "photo.jpg", "video_clip.mp4")));

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
    MultipartFile[] testWorkoutFiles = new MultipartFile[]{
        new MockMultipartFile("testaudio.mp3", "testaudio.mp3",
            "audio/mpeg", new byte[]{1, 2, 3}),
        new MockMultipartFile("testimage.bmp", "testimage.bmp",
            "image/bmp", new byte[]{1, 2, 3}),
        new MockMultipartFile("testvideo.mp4", "testvideo.mp4",
            "video/mp4", new byte[]{1, 2, 3}),
    };

    service.storeAllFilesByWorkout(TEST_WORKOUT, testWorkoutFiles);

    String filename;
    for(MultipartFile file : testWorkoutFiles) {
      filename = file.getOriginalFilename();
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(file.getContentType());
      objectMetadata.setContentLength(file.getSize());
      verify(client).putObject(eq("correctbucketname"), eq(TEST_WORKOUT.getId() + "\\"
          + filename), any(InputStream.class), any(ObjectMetadata.class));
    }
    assertTrue(TEST_WORKOUT.getFilenames().containsAll(new ArrayList<>(
        Arrays.asList("testaudio.mp3", "testimage.bmp", "testvideo.mp4"))));
  }

  @Test
  public void testGetFileByWorkoutIdAndFilename() {
    MediaFile testFile = new MediaFile(null, 3L, "testfile.mp3", "audio/mpeg",
        new byte[]{69, 121, 101, 45, 62, 118, 101, 114, (byte) 196, (byte) 195, 61, 101, 98});
    InputStream testFileInputStream = new ByteArrayInputStream(testFile.getContent());
    S3Object s3Object = mock(S3Object.class);
    ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
    when(client.getObject("correctbucketname", "3\\testfile.mp3")).thenReturn(s3Object);
    when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
    when(s3Object.getObjectContent()).thenReturn(
        new S3ObjectInputStream(testFileInputStream, null));
    when(s3Object.getKey()).thenReturn("testfile.mp3");
    when(objectMetadata.getContentType()).thenReturn("audio/mpeg");
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
    } catch (RuntimeException exception) {
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
    } catch (RuntimeException exception) {
      assertEquals(exception.getMessage(), "Error deleting files!");
    }
    verify(client).deleteObject("correctbucketname", "1\\audio_file.mp3");
    verify(client).deleteObject("correctbucketname", "1\\photo.jpg");
    verify(client).deleteObject("correctbucketname", "1\\video_clip.mp4");
  }
}
