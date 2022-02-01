package com.panpawelw.weightliftinglog.services;

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
                                     MultipartFile[] workoutFiles) throws IOException {
    List<MediaFile> mediaFiles = new ArrayList<>();
    List<String> filenames = workoutDeserialized.getFilenames();
    Long workoutId = workoutDeserialized.getId();
    for (MultipartFile file : workoutFiles) {
      String filename = file.getOriginalFilename();
      mediaFiles.add(new MediaFile(0L, workoutId, filename,
          file.getContentType(), file.getBytes()));
      filenames.add(filename);
      workoutDeserialized.setFilenames(filenames);
    }
    fileRepository.saveAll(mediaFiles);
    fileRepository.flush();
  }

  public MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
    return fileRepository.findFileByWorkoutIdAndFilename(workoutId, filename);
  }

  public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized,
                                             String filename) {
    fileRepository.deleteByWorkoutIdAndFilename(workoutDeserialized.getId(), filename);
  }

  public void deleteAllFilesByWorkoutId(long workoutId) {
    fileRepository.deleteAllByWorkoutId(workoutId);
  }

  /**
   * Counts all records in files table
   * @return count
   */
  public Long count() {
    return fileRepository.count();
  }
}