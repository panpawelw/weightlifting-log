package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkoutServiceIT {

  private static boolean dbInitialized = false;

  @Autowired
  private WorkoutService service;

  @Autowired
  private UserService userService;

  @Before
  public void setup() {
    if (!dbInitialized) {
      populateDatabase();
      dbInitialized = true;
    }
  }

  @Test
  public void findWorkoutsByUserSucceeds() {
    List<WorkoutSerialized> workoutList =
        service.findWorkoutsByUser(userService.findUserByEmail("workout@test1.com"));
    assertEquals(3, workoutList.size());
  }

  @Test
  public void findWorkoutByIdSucceeds() {
    WorkoutDeserialized testWorkout = service.findWorkoutById(1L);
    assertEquals("Test workout 1 title", testWorkout.getTitle());
  }

  @Test
  public void findWorkoutByIdReturnsNull() {
    WorkoutDeserialized testWorkout = service.findWorkoutById(5L);
    assertNull(testWorkout);
  }

  @Test
  public void saveWorkoutSucceeds() {

  }

  @Test
  public void deleteWorkoutSucceeds() {

  }

  @Test
  public void serializeWorkoutSucceeds() {

  }

  @Test
  public void deserializeWorkoutSucceeds() {

  }

  private void populateDatabase() {
    for (long id = 1L; id <= 3; id++) {
      WorkoutDeserialized workout = createTestWorkout(id, "workout@test1.com");
      service.saveWorkout(workout);
    }
    WorkoutDeserialized workout = createTestWorkout(4L, "workout@test2.com");
    service.saveWorkout(workout);
  }

  private WorkoutDeserialized createTestWorkout(Long id, String email) {
    return new WorkoutDeserialized(0L, "Test workout " + id + " title",
        java.sql.Timestamp.valueOf("2022-01-01 0" + id + ":10:10.0"),
        java.sql.Timestamp.valueOf("2022-01-01 0" + (id + 1) + ":10:10.0"),
        userService.findUserByEmail(email),
        Arrays.asList(
            new Exercise("Test workout " + id + " exercise 1", Arrays.asList(
                new Set("Test workout " + id + " exercise 1 set 1", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 1 set 1 note 1"),
                    new Note(0, "Test workout " + id + " exercise 1 set 1 note 2"))),
                new Set("Test workout " + id + " exercise 1 set 2", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 1 set 2 note 1"),
                    new Note(0, "Test workout " + id + " exercise 1 set 2 note 2")))),
                Collections.singletonList(
                    new Note(0, "Test workout " + id + " exercise 1 note"))),
            new Exercise("Test workout " + id + " exercise 2", Arrays.asList(
                new Set("Test workout " + id + " exercise 2 set 1", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 2 set 1 note 1"),
                    new Note(0, "Test workout " + id + " exercise 2 set 1 note 2"))),
                new Set("Test workout " + id + " exercise 2 set 2", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 2 set 2 note 1"),
                    new Note(0, "Test workout " + id + " exercise 2 set 2 note 2")))),
                Collections.singletonList(
                    new Note(0, "Test workout " + id + " exercise 2 note"))),
            new Exercise("Test workout " + id + " exercise 3", Arrays.asList(
                new Set("Test workout " + id + " exercise 3 set 1", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 3 set 1 note 1"),
                    new Note(0, "Test workout " + id + " exercise 3 set 1 note 2"))),
                new Set("Test workout " + id + " exercise 3 set 2", Arrays.asList(
                    new Note(0, "Test workout " + id + " exercise 3 set 2 note 1"),
                    new Note(0, "Test workout " + id + " exercise 3 set 2 note 2")))),
                Collections.singletonList(
                    new Note(0, "Test workout " + id + " exercise 3 note")))),
        Collections.singletonList(new Note(0, "Test workout " + id + " note")),
        new ArrayList<>());
  }
}
