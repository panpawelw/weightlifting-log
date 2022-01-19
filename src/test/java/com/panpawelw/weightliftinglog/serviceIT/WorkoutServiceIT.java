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

  @Autowired
  private WorkoutService service;

  @Autowired
  private UserService userService;

  @Before
  public void setup() {
    populateDatabase();
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
    assertEquals("Test workout 1 title",testWorkout.getTitle());
  }

  @Test
  public void findWorkoutByIdReturnsNull() {
    WorkoutDeserialized testWorkout = service.findWorkoutById(4L);
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
    WorkoutDeserialized testWorkout1 = new WorkoutDeserialized(
        1L, "Test workout 1 title",
        java.sql.Timestamp.valueOf("2022-01-01 08:10:10.0"),
        java.sql.Timestamp.valueOf("2022-01-01 09:10:10.0"),
        userService.findUserByEmail("workout@test1.com"),
        Arrays.asList(
            new Exercise("Test workout 1 exercise 1", Arrays.asList(
                new Set("Test workout 1 exercise 1 set 1", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 1 set 1 note 1"),
                    new Note(0, "Test workout 1 exercise 1 set 1 note 2"))),
                new Set("Test workout 1 exercise 1 set 2", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 1 set 2 note 1"),
                    new Note(0, "Test workout 1 exercise 1 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 1 exercise 1 note"))),
            new Exercise("Test workout 1 exercise 2", Arrays.asList(
                new Set("Test workout 1 exercise 2 set 1", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 2 set 1 note 1"),
                    new Note(0, "Test workout 1 exercise 2 set 1 note 2"))),
                new Set("Test workout 1 exercise 2 set 2", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 2 set 2 note 1"),
                    new Note(0, "Test workout 1 exercise 2 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 1 exercise 2 note"))),
            new Exercise("Test workout 1 exercise 3", Arrays.asList(
                new Set("Test workout 1 exercise 3 set 1", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 3 set 1 note 1"),
                    new Note(0, "Test workout 1 exercise 3 set 1 note 2"))),
                new Set("Test workout 1 exercise 3 set 2", Arrays.asList(
                    new Note(0, "Test workout 1 exercise 3 set 2 note 1"),
                    new Note(0, "Test workout 1 exercise 3 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 1 exercise 3 note")))),
        Collections.singletonList(new Note(0, "Test workout 1 note")),
        new ArrayList<>());
    WorkoutDeserialized testWorkout2 = new WorkoutDeserialized(
        2L, "Test workout 2 title",
        java.sql.Timestamp.valueOf("2022-01-01 10:10:10.0"),
        java.sql.Timestamp.valueOf("2022-01-01 11:10:10.0"),
        userService.findUserByEmail("workout@test1.com"),
        Arrays.asList(
            new Exercise("Test workout 2 exercise 1", Arrays.asList(
                new Set("Test workout 2 exercise 1 set 1", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 1 set 1 note 1"),
                    new Note(0, "Test workout 2 exercise 1 set 1 note 2"))),
                new Set("Test workout 2 exercise 1 set 2", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 1 set 2 note 1"),
                    new Note(0, "Test workout 2 exercise 1 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 2 exercise 1 note"))),
            new Exercise("Test workout 2 exercise 2", Arrays.asList(
                new Set("Test workout 2 exercise 2 set 1", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 2 set 1 note 1"),
                    new Note(0, "Test workout 2 exercise 2 set 1 note 2"))),
                new Set("Test workout 2 exercise 2 set 2", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 2 set 2 note 1"),
                    new Note(0, "Test workout 2 exercise 2 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 2 exercise 2 note"))),
            new Exercise("Test workout 2 exercise 3", Arrays.asList(
                new Set("Test workout 2 exercise 3 set 1", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 3 set 1 note 1"),
                    new Note(0, "Test workout 2 exercise 3 set 1 note 2"))),
                new Set("Test workout 2 exercise 3 set 2", Arrays.asList(
                    new Note(0, "Test workout 2 exercise 3 set 2 note 1"),
                    new Note(0, "Test workout 2 exercise 3 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 2 exercise 3 note")))),
        Collections.singletonList(new Note(0, "Test workout 2 note")),
        new ArrayList<>());
    WorkoutDeserialized testWorkout3 = new WorkoutDeserialized(
        3L, "Test workout 3 title",
        java.sql.Timestamp.valueOf("2022-01-01 12:10:10.0"),
        java.sql.Timestamp.valueOf("2022-01-01 13:10:10.0"),
        userService.findUserByEmail("workout@test1.com"),
        Arrays.asList(
            new Exercise("Test workout 3 exercise 1", Arrays.asList(
                new Set("Test workout 3 exercise 1 set 1", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 1 set 1 note 1"),
                    new Note(0, "Test workout 3 exercise 1 set 1 note 2"))),
                new Set("Test workout 3 exercise 1 set 2", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 1 set 2 note 1"),
                    new Note(0, "Test workout 3 exercise 1 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 3 exercise 1 note"))),
            new Exercise("Test workout 3 exercise 2", Arrays.asList(
                new Set("Test workout 3 exercise 2 set 1", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 2 set 1 note 1"),
                    new Note(0, "Test workout 3 exercise 2 set 1 note 2"))),
                new Set("Test workout 3 exercise 2 set 2", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 2 set 2 note 1"),
                    new Note(0, "Test workout 3 exercise 2 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 3 exercise 2 note"))),
            new Exercise("Test workout 3 exercise 3", Arrays.asList(
                new Set("Test workout 3 exercise 3 set 1", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 3 set 1 note 1"),
                    new Note(0, "Test workout 3 exercise 3 set 1 note 2"))),
                new Set("Test workout 3 exercise 3 set 2", Arrays.asList(
                    new Note(0, "Test workout 3 exercise 3 set 2 note 1"),
                    new Note(0, "Test workout 3 exercise 3 set 2 note 2")))),
                Collections.singletonList(new Note(0, "Test workout 3 exercise 3 note")))),
        Collections.singletonList(new Note(0, "Test workout 3 note")),
        new ArrayList<>());
    service.saveWorkout(testWorkout1);
    service.saveWorkout(testWorkout2);
    service.saveWorkout(testWorkout3);
  }

}
