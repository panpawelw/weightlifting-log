package pl.pjm77.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjm77.weightliftinglog.models.MediaFile;

import javax.transaction.Transactional;

@Transactional
public interface FileRepository extends JpaRepository<MediaFile, Long> {

    MediaFile findFileByWorkoutIdAndFilename(Long workoutId, String filename);

    void deleteByWorkoutIdAndFilename(Long workoutId, String filename);

    void deleteAllByWorkoutId(Long workoutId);
}
