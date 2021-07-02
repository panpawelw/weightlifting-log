package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

  void storeAllFilesByWorkout(WorkoutDeserialized workoutDeserialized,
      MultipartFile[] workoutFiles) throws IOException;

  MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename) throws IOException;

  void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized, String filename);

  void deleteAllFilesByWorkoutId(long workoutId);
}