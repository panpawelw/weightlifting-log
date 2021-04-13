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

  @Mock
  private WorkoutRepository repository;

  private WorkoutService service;

  @Before
  public void setup() {
    service = new WorkoutService(repository);
  }

  @Test
  public void testFindWorkoutsByUser() {
    User testUser = new User();
    List<WorkoutSerialized> workoutList = new ArrayList<>();
    when(repository.findAllByUserOrderByCreatedDesc(testUser)).thenReturn(workoutList);

    assertEquals(service.findWorkoutsByUser(testUser), workoutList, "Workout list doesn't match!");
    verify(repository).findAllByUserOrderByCreatedDesc(testUser);
  }

  @Test
  public void testFindWorkoutById() {
    WorkoutDeserialized testWorkout = createTestWorkout();
    when(repository.findById(1L))
        .thenReturn(java.util.Optional.ofNullable(service.serializeWorkout(testWorkout)));

    assertEquals(service.findWorkoutById(1L), testWorkout, "Workout objects don't match!");
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
    WorkoutDeserialized testWorkout = createTestWorkout();
    WorkoutSerialized workoutSerialized = service.serializeWorkout(testWorkout);
    when(repository.saveAndFlush(any())).thenReturn(workoutSerialized);

    assertEquals(service.saveWorkout(testWorkout), testWorkout.getId(),
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
    WorkoutDeserialized testWorkout = createTestWorkout();
    WorkoutSerialized workoutSerialized1 = service.serializeWorkout(testWorkout);
    WorkoutSerialized workoutSerialized2 = service.serializeWorkout(testWorkout);
    WorkoutDeserialized workoutDeserialized1 = service.deserializeWorkout(workoutSerialized1);
    WorkoutDeserialized workoutDeserialized2 = service.deserializeWorkout(workoutSerialized2);

    assertEquals(workoutDeserialized1, workoutDeserialized2, "Workouts don't match!");
    assertEquals(testWorkout, workoutDeserialized1, "First test workout doesn't match!");
    assertEquals(testWorkout, workoutDeserialized2, "Second test workout doesn't match!");
  }

  @SuppressWarnings("all")
  @Test
  public void testInstance() {
    WorkoutSerialized workoutSerialized = service.serializeWorkout(createTestWorkout());
    Object workoutDeserialized = service.deserializeWorkout(workoutSerialized);

    assertTrue(workoutDeserialized instanceof WorkoutDeserialized, "Wrong return class!");
    assertEquals(createTestWorkout(), workoutDeserialized, "Workout objects don't match!");
  }

  private WorkoutDeserialized createTestWorkout() {
    return new WorkoutDeserialized(1L, "Workout title", null, null,
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
  }
}
