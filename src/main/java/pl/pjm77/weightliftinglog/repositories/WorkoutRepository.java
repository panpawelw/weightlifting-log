package pl.pjm77.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.WorkoutSerialized;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface WorkoutRepository extends JpaRepository<WorkoutSerialized, Long> {

    List<WorkoutSerialized> findAllByUserOrderByCreatedDesc(User user);
}