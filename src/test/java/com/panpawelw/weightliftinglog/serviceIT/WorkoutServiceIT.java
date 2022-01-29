package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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

  @Test
  public void findWorkoutsByUserSucceeds() {
    System.out.println(service.findWorkoutById(1));
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
    long initialDatabaseCount = service.count();
    WorkoutDeserialized testWorkout = new WorkoutDeserialized(
        0L, "Test workout 5 title", java.sql.Timestamp.valueOf("2022-01-01 07:10:10.0"),
        java.sql.Timestamp.valueOf("2022-01-01 08:10:10.0"),
        userService.findUserByEmail("workout@test2.com"),
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    service.saveWorkout(testWorkout);
    assertEquals(1, service.count() - initialDatabaseCount);
  }

  @Test
  public void deleteWorkoutSucceeds() {
    long initialDatabaseCount = service.count();
    service.deleteWorkout(4);
    assertEquals(1, initialDatabaseCount - service.count());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void deleteWorkoutFails() {
    long initialDatabaseCount = service.count();
    service.deleteWorkout(100);
    assertEquals(0, initialDatabaseCount - service.count());
  }

  @Test
  public void serializeAndDeserializeWorkoutSucceed() {
    WorkoutDeserialized testWorkoutD = service.findWorkoutById(1);
    WorkoutSerialized testWorkoutS = service.serializeWorkout(testWorkoutD);

    assertEquals(testWorkoutD, service.deserializeWorkout(testWorkoutS));
  }
}
