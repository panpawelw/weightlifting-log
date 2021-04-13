package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.repositories.WorkoutRepository;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkoutServiceTests {

  @Mock
  private WorkoutRepository repository;

  private final WorkoutService service = new WorkoutService(repository);

  @Test
  public void findWorkoutsByUserTest() {

  }

  @Test
  public void findWorkoutById() {

  }

  @Test
  public void saveWorkoutTest() {

  }

  @Test
  public void deleteWorkoutTest() {

  }

  @Test
  public void testSerializationConsistency() {
    WorkoutDeserialized testWorkout = createTestWorkout();
    WorkoutSerialized workoutSerialized1 = service.serializeWorkout(testWorkout);
    WorkoutSerialized workoutSerialized2 = service.serializeWorkout(testWorkout);
    WorkoutDeserialized workoutDeserialized1 = service.deserializeWorkout(workoutSerialized1);
    WorkoutDeserialized workoutDeserialized2 = service.deserializeWorkout(workoutSerialized2);
    assertEquals(workoutDeserialized1, workoutDeserialized2);
    assertEquals(testWorkout, workoutDeserialized1);
    assertEquals(testWorkout, workoutDeserialized2);
  }

  @SuppressWarnings("all")
  @Test
  public void testInstance() {
    WorkoutSerialized workoutSerialized = service.serializeWorkout(createTestWorkout());
    Object workoutDeserialized = service.deserializeWorkout(workoutSerialized);

    assertTrue(workoutDeserialized instanceof WorkoutDeserialized);
    assertEquals(createTestWorkout(), workoutDeserialized);
  }

  public WorkoutDeserialized createTestWorkout() {
    return new WorkoutDeserialized(1L, "Workout title", null, null, new User(),
        Arrays.asList(
            new Exercise("Exercise 1", Arrays.asList(
                new Set("Exercise 1 set 1", Arrays.asList(
                    new Note(0, "Exercise 1 set 1 note 1"),
                    new Note(0, "Exercise 1 set 1 note 2"))),
                new Set("Exercise 1 set 2", Arrays.asList(
                    new Note(0, "Exercise 1 set 2 note 1"),
                    new Note(0, "Exercise 1 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Exercise 1 note"))),
            new Exercise("Exercise 2", Arrays.asList(
                new Set("Exercise 2 set 1", Arrays.asList(
                    new Note(0, "Exercise 2 set 1 note 1"),
                    new Note(0, "Exercise 2 set 1 note 2"))),
                new Set("Exercise 2 set 2", Arrays.asList(
                    new Note(0, "Exercise 2 set 2 note 1"),
                    new Note(0, "Exercise 2 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Exercise 2 note"))),
            new Exercise("Exercise 3", Arrays.asList(
                new Set("Exercise 3 set 1", Arrays.asList(
                    new Note(0, "Exercise 3 set 1 note 1"),
                    new Note(0, "Exercise 3 set 1 note 2"))),
                new Set("Exercise 3 set 2", Arrays.asList(
                    new Note(0, "Exercise 3 set 2 note 1"),
                    new Note(0, "Exercise 3 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Exercise 3 note")))),
        Collections.singletonList(new Note(0, "Workout note")),
        null);
  }
}
