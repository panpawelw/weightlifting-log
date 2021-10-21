package com.panpawelw.weightliftinglog;

import com.panpawelw.weightliftinglog.models.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class constants {
  public static final User TEST_USER = new User(1L, "Test name",
      "Test password", "Test password",
      "test@email.com", true, "Test first name",
      "Test last name", 20, true, "ADMIN", new ArrayList<>());

  public static final WorkoutDeserialized TEST_WORKOUT = new WorkoutDeserialized(
      1L, "Workout title", null, null, TEST_USER,
      Arrays.asList(
          new Exercise("Exercise 1", Arrays.asList(
              new Set("Exercise 1 set 1", Arrays.asList(
                  new Note(0, "Exercise 1 set 1 note 1"),
                  new Note(0, "Exercise 1 set 1 note 2"))),
              new Set("Exercise 1 set 2", Arrays.asList(
                  new Note(0, "Exercise 1 set 2 note 1"),
                  new Note(0, "Exercise 1 set 2 note 2")))),
              Collections.singletonList(new Note(0, "Exercise 1 note"))),
          new Exercise("Exercise 2", Arrays.asList(
              new Set("Exercise 2 set 1", Arrays.asList(
                  new Note(0, "Exercise 2 set 1 note 1"),
                  new Note(0, "Exercise 2 set 1 note 2"))),
              new Set("Exercise 2 set 2", Arrays.asList(
                  new Note(0, "Exercise 2 set 2 note 1"),
                  new Note(0, "Exercise 2 set 2 note 2")))),
              Collections.singletonList(new Note(0, "Exercise 2 note"))),
          new Exercise("Exercise 3", Arrays.asList(
              new Set("Exercise 3 set 1", Arrays.asList(
                  new Note(0, "Exercise 3 set 1 note 1"),
                  new Note(0, "Exercise 3 set 1 note 2"))),
              new Set("Exercise 3 set 2", Arrays.asList(
                  new Note(0, "Exercise 3 set 2 note 1"),
                  new Note(0, "Exercise 3 set 2 note 2")))),
              Collections.singletonList(new Note(0, "Exercise 3 note")))),
      Collections.singletonList(new Note(0, "Workout note")),
      new ArrayList<>(Arrays.asList("audio_file.mp3", "photo.jpg", "video_clip.mp4")));

  public static final MultipartFile[] TEST_WORKOUT_FILES = new MultipartFile[]{
      new MockMultipartFile("testaudio.mp3", "testaudio.mp3",
          "audio/mpeg", new byte[]{110, (byte) 160, 7, 47, 49, 24, 41, 113, 103, 123}),
      new MockMultipartFile("testimage.bmp", "testimage.bmp",
          "image/bmp", new byte[]{41, 91, 115, 16, 22, 118, 122, 49,  28, 97}),
      new MockMultipartFile("testvideo.mp4", "testvideo.mp4",
          "video/mp4", new byte[]{113, 17, 78, 6, 89, 24, 23, (byte) 199, 83, 22}),
  };
}
