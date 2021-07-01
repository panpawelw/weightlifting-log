package com.panpawelw.weightliftinglog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.WorkoutSerialized;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface WorkoutRepository extends JpaRepository<WorkoutSerialized, Long> {

  Long deleteById(long id);
  List<WorkoutSerialized> findAllByUserOrderByCreatedDesc(User user);
}