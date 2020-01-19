package pl.pjm77.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjm77.weightliftinglog.models.File;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
public interface FileRepository extends JpaRepository<File, Long> {

    ArrayList<File> findAllByWorkoutId(Long workoutId);

    File findFileByWorkoutIdAndFilename(Long workoutId, String filename);

    void deleteByWorkoutIdAndFilename(Long workoutId, String filename);
}
