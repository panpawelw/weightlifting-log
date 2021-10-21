package com.panpawelw.weightliftinglog.servicetests;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.services.S3FileService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.panpawelw.weightliftinglog.constants.TEST_WORKOUT;
import static com.panpawelw.weightliftinglog.constants.TEST_WORKOUT_FILES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    ReflectionTestUtils.setField(service, "bucketName", "correctbucketname");
  }

  @Test
  public void testStoreAllFilesByWorkout() throws IOException {
    service.storeAllFilesByWorkout(TEST_WORKOUT, TEST_WORKOUT_FILES);

    for(MultipartFile file : TEST_WORKOUT_FILES) {
      verify(client).putObject(eq("correctbucketname"), eq(TEST_WORKOUT.getId() + "\\"
          + file.getOriginalFilename()), any(InputStream.class), any(ObjectMetadata.class));
    }
    assertTrue(TEST_WORKOUT.getFilenames().containsAll(new ArrayList<>(
        Arrays.asList("testaudio.mp3", "testimage.bmp", "testvideo.mp4"))));
  }

  @Test
  public void testGetFileByWorkoutIdAndFilename() throws IOException {
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
    verify(client).getObject("correctbucketname", "3\\testfile.mp3");
  }

  @Test
  public void testDeleteFileByWorkoutAndFilename() {
    service.deleteFileByWorkoutAndFilename(TEST_WORKOUT, "correctfilename.mp3");
    verify(client).deleteObject("correctbucketname", "1\\correctfilename.mp3");
  }

  @Test
  public void testDeleteAllFilesByWorkoutId() {
    when(workoutService.findWorkoutById(1)).thenReturn(TEST_WORKOUT);

    service.deleteAllFilesByWorkoutId(1);
    verify(client).deleteObject("correctbucketname", "1\\audio_file.mp3");
    verify(client).deleteObject("correctbucketname", "1\\photo.jpg");
    verify(client).deleteObject("correctbucketname", "1\\video_clip.mp4");
  }
}
