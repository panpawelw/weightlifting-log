package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import com.panpawelw.weightliftinglog.repositories.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBFileService implements FileService {

  private final FileRepository fileRepository;

  public DBFileService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public void storeAllFilesByWorkout(WorkoutDeserialized workoutDeserialized,
                                     MultipartFile[] workoutFiles) {
    List<MediaFile> mediaFiles = new ArrayList<>();
    List<String> filenames = workoutDeserialized.getFilenames();
    Long workoutId = workoutDeserialized.getId();
    for (MultipartFile file : workoutFiles) {
      String filename = file.getOriginalFilename();
      try {
        mediaFiles.add(new MediaFile(0L, workoutId, filename,
            file.getContentType(), file.getBytes()));
        filenames.add(filename);
      } catch (IOException e) {
        e.printStackTrace();
      }
      workoutDeserialized.setFilenames(filenames);
    }
    try {
      fileRepository.saveAll(mediaFiles);
    } catch (IllegalArgumentException e) {
      throw new ApiRequestException("Error uploading files!");
    }
    fileRepository.flush();
  }

  public MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
    MediaFile result = fileRepository.findFileByWorkoutIdAndFilename(workoutId, filename);
    if (result == null) {
      throw new ApiRequestException("Error streaming file!");
    }
    return result;
  }

  public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized,
      String filename) {
    if(fileRepository.deleteByWorkoutIdAndFilename(workoutDeserialized.getId(), filename) == 0) {
      throw new ApiRequestException("Error deleting " + filename + "!");
    }
  }

  public void deleteAllFilesByWorkoutId(long workoutId) {
    if(fileRepository.deleteAllByWorkoutId(workoutId) == 0) {
      throw new ApiRequestException("Error deleting files!");
    }
  }
}