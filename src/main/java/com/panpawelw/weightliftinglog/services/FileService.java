package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void storeAllFiles(WorkoutDeserialized workoutDeserialized,
      MultipartFile[] workoutFiles);

  MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename);

  void deleteFileByWorkoutAndFilename
      (WorkoutDeserialized workoutDeserialized, String filename);

  void deleteAllByWorkoutId(long workoutId);
}