package com.panpawelw.weightliftinglog.services;

import com.panpawelw.weightliftinglog.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.models.WorkoutSerialized;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {

  private final WorkoutRepository workoutRepository;

  @Autowired
  public WorkoutService(WorkoutRepository workoutRepository) {
    this.workoutRepository = workoutRepository;
  }

  /**
   * Loads a list of workouts by given user
   *
   * @param user - user object
   * @return List of serialized workouts
   */
  public List<WorkoutSerialized> findWorkoutsByUser(User user) {
    return workoutRepository.findAllByUserOrderByCreatedDesc(user);
  }

  /**
   * Finds a single workout with given id
   *
   * @param id - workout id
   * @return deserialized workout or null if there is no workout with such id
   */
  public WorkoutDeserialized findWorkoutById(long id) {
    Optional<WorkoutSerialized> optionalWorkoutSerialized = workoutRepository.findById(id);
    return optionalWorkoutSerialized.map(this::deserializeWorkout).orElse(null);
  }

  /**
   * Saves workout to database
   *
   * @param workoutDeserialized deserialized workout object
   */
  public Long saveWorkout(WorkoutDeserialized workoutDeserialized) {
    return workoutRepository.saveAndFlush(serializeWorkout(workoutDeserialized)).getId();
  }

  /**
   * Deletes workout of given id
   *
   * @param id - id of workout to be deleted
   */
  public Long deleteWorkout(long id) {
    return workoutRepository.deleteById(id);
  }

  /**
   * Serializes a deserialized workout object
   *
   * @param workoutDeserialized - workout object to be serialized
   * @return serialized workout
   */
  public WorkoutSerialized serializeWorkout (WorkoutDeserialized workoutDeserialized) {
    WorkoutSerialized workoutSerialized = new WorkoutSerialized();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(workoutDeserialized);
      out.flush();
      workoutSerialized.setData(bos.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        bos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    workoutSerialized.setId(workoutDeserialized.getId());
    workoutSerialized.setTitle(workoutDeserialized.getTitle());
    workoutSerialized.setCreated(workoutDeserialized.getCreated());
    workoutSerialized.setUpdated(workoutDeserialized.getUpdated());
    workoutSerialized.setUser(workoutDeserialized.getUser());
    return workoutSerialized;
  }

  /**
   * Deserializes serialized workout
   *
   * @param workoutSerialized - serialized workout to be deserialized
   * @return deserialized workout
   */
  public WorkoutDeserialized deserializeWorkout (WorkoutSerialized workoutSerialized) {
    WorkoutDeserialized workoutDeserialized = new WorkoutDeserialized();
    ByteArrayInputStream bis = new ByteArrayInputStream(workoutSerialized.getData());
    ObjectInput in = null;
    try {
      in = new ObjectInputStream(bis);
      Object o = in.readObject();
      workoutDeserialized = (WorkoutDeserialized) o;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    workoutDeserialized.setId(workoutSerialized.getId());
    return workoutDeserialized;
  }
}
