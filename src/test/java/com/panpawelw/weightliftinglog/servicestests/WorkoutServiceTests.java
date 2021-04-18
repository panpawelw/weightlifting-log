package com.panpawelw.weightliftinglog.servicestests;

import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.repositories.WorkoutRepository;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutServiceTests {

  private static final User TEST_USER = new User(1L, "Test user", "Test password",
      null, "test@email.com", true);

  private static final WorkoutDeserialized TEST_WORKOUT = new WorkoutDeserialized(
      1L, "Workout title", null, null,
      new User(1L, "Test name", "Test password", null,
          "Test@email.com", true, null, null, null,
          null, null, new ArrayList<>()),
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

  @Mock
  private WorkoutRepository repository;

  private WorkoutService service;

  @Before
  public void setup() {
    service = new WorkoutService(repository);
  }

  @Test
  public void testFindWorkoutsByUser() {
    List<WorkoutSerialized> workoutList = Arrays.asList(new WorkoutSerialized(), new WorkoutSerialized());
    when(repository.findAllByUserOrderByCreatedDesc(TEST_USER)).thenReturn(workoutList);

    assertEquals(service.findWorkoutsByUser(TEST_USER), workoutList, "Workout list doesn't match!");
    verify(repository).findAllByUserOrderByCreatedDesc(TEST_USER);
  }

  @Test
  public void testFindWorkoutById() {
    when(repository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(service.serializeWorkout(TEST_WORKOUT)));

    assertEquals(service.findWorkoutById(1L), TEST_WORKOUT, "Workout objects don't match!");
    verify(repository).findById(1L);
  }

  @Test
  public void testFindWorkoutByIdReturnsNull() {
    when(repository.findById(111L))
        .thenReturn(java.util.Optional.empty());

    assertNull(service.findWorkoutById(111L), "Workout should be null!");
    verify(repository).findById(111L);
  }

  @Test
  public void testSaveWorkout() {
    WorkoutSerialized workoutSerialized = service.serializeWorkout(TEST_WORKOUT);
    when(repository.saveAndFlush(any())).thenReturn(workoutSerialized);

    assertEquals(service.saveWorkout(TEST_WORKOUT), TEST_WORKOUT.getId(),
        "Workout IDs don't match!");
    verify(repository).saveAndFlush(workoutSerialized);
  }

  @Test
  public void testDeleteWorkout() {
    service.deleteWorkout(1L);

    verify(repository).deleteById(1L);
  }

  @Test
  public void testSerializationConsistency() {
    WorkoutSerialized workoutSerialized1 = service.serializeWorkout(TEST_WORKOUT);
    WorkoutSerialized workoutSerialized2 = service.serializeWorkout(TEST_WORKOUT);
    WorkoutDeserialized workoutDeserialized1 = service.deserializeWorkout(workoutSerialized1);
    WorkoutDeserialized workoutDeserialized2 = service.deserializeWorkout(workoutSerialized2);

    assertEquals(workoutDeserialized1, workoutDeserialized2, "Workouts don't match!");
    assertEquals(TEST_WORKOUT, workoutDeserialized1, "First test workout doesn't match!");
    assertEquals(TEST_WORKOUT, workoutDeserialized2, "Second test workout doesn't match!");
  }

  @SuppressWarnings("all")
  @Test
  public void testInstance() {
    WorkoutSerialized workoutSerialized = service.serializeWorkout(TEST_WORKOUT);
    Object workoutDeserialized = service.deserializeWorkout(workoutSerialized);

    assertTrue(workoutDeserialized instanceof WorkoutDeserialized, "Wrong return class!");
    assertEquals(TEST_WORKOUT, workoutDeserialized, "Workout objects don't match!");
  }
}
