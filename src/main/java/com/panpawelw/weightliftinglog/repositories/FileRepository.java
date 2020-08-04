package com.panpawelw.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panpawelw.weightliftinglog.models.MediaFile;

import javax.transaction.Transactional;

@Transactional
public interface FileRepository extends JpaRepository<MediaFile, Long> {

    MediaFile findFileByWorkoutIdAndFilename(Long workoutId, String filename);

    void deleteByWorkoutIdAndFilename(Long workoutId, String filename);

    void deleteAllByWorkoutId(Long workoutId);
}
