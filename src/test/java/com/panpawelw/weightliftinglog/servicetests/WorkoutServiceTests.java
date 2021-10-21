package com.panpawelw.weightliftinglog.servicetests;

import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.repositories.WorkoutRepository;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.panpawelw.weightliftinglog.constants.TEST_USER;
import static com.panpawelw.weightliftinglog.constants.TEST_WORKOUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutServiceTests {

  @Mock
  private WorkoutRepository repository;

  private WorkoutService service;

  private final User testUser = new User(TEST_USER);

  private final WorkoutDeserialized testWorkout = new WorkoutDeserialized(TEST_WORKOUT);

  @Before
  public void setup() {
    testUser.setConfirmPassword(null);
    testWorkout.setUser(testUser);
    service = new WorkoutService(repository);
  }

  @Test
  public void testFindWorkoutsByUser() {
    List<WorkoutSerialized> workoutList = Arrays.asList(new WorkoutSerialized(), new WorkoutSerialized());
    when(repository.findAllByUserOrderByCreatedDesc(testUser)).thenReturn(workoutList);

    assertEquals(service.findWorkoutsByUser(testUser), workoutList, "Workout list doesn't match!");
    verify(repository).findAllByUserOrderByCreatedDesc(testUser);
  }

  @Test
  public void testFindWorkoutById() {
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
    WorkoutSerialized workoutSerialized = service.serializeWorkout(testWorkout);
    Object workoutDeserialized = service.deserializeWorkout(workoutSerialized);

    assertTrue(workoutDeserialized instanceof WorkoutDeserialized, "Wrong return class!");
    assertEquals(testWorkout, workoutDeserialized, "Workout objects don't match!");
  }
}
