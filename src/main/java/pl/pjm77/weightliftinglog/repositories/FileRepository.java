package pl.pjm77.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjm77.weightliftinglog.models.File;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByWorkoutId(Long workoutId);
}
