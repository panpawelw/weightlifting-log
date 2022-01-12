package com.panpawelw.weightliftinglog.serviceIT;

import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkoutServiceIT {

  @Autowired
  private WorkoutService service;

  @Test
  public void findWorkoutsByUserSucceds() {

  }

  @Test
  public void findWorkoutByIdSucceeds() {

  }

  @Test
  public void findWorkoutByIdReturnsNull() {

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
}
