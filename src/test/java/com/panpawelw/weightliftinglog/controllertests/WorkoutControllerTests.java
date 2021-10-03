package com.panpawelw.weightliftinglog.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panpawelw.weightliftinglog.controllers.WorkoutController;
import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import com.panpawelw.weightliftinglog.models.*;
import com.panpawelw.weightliftinglog.services.FileService;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.WorkoutService;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(controllers = WorkoutController.class)
public class WorkoutControllerTests {

  private static final User TEST_USER = new User(1L, "Test name",
      "Test password", "Test password",
      "test@email.com", true, "Test first name",
      "Test last name", 20, true, "ADMIN", new ArrayList<>());

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
      Collections.singletonList(new Note(0, "Workout note")), null);

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private WorkoutService service;

  @Mock
  private UserService userService;

  @Mock
  private FileService fileService;

  @Before
  public void setup() {
    WorkoutController controller = new WorkoutController(service, userService, fileService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void getValidWorkoutById() throws Exception {
    when(service.findWorkoutById(TEST_WORKOUT.getId())).thenReturn(TEST_WORKOUT);
    MvcResult result = mockMvc.perform(get("/workout/1"))
        .andExpect(status().isOk())
        .andReturn();
    String actual = result.getResponse().getContentAsString();
    ObjectMapper mapper = new ObjectMapper();
    String expected = mapper.writeValueAsString(TEST_WORKOUT);

    assertEquals(expected, actual);
    verify(service).findWorkoutById(TEST_WORKOUT.getId());
  }

  @Test(expected = ApiRequestException.class)
  public void getNullWorkout() throws Throwable {
    when(service.findWorkoutById(1)).thenReturn(null);

    performMockMvcMethodAndCheckErrorMessage("get", "/workout/1",
        "No such workout in the database!");
    verify(service).findWorkoutById(1);
  }

  @Test(expected = ApiRequestException.class)
  public void getWorkoutDatabaseError() throws Throwable {
    when(service.findWorkoutById(1)).thenThrow(HibernateException.class);

    performMockMvcMethodAndCheckErrorMessage("get", "/workout/1",
        "There's a problem with database connection!");
    verify(service).findWorkoutById(1);
  }

  @Test
  public void addWorkoutGetValidUser() throws Exception {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER);
    when(userService.checkLoggedInUserForAdminRights()).thenReturn(true);
    when(service.findWorkoutsByUser(TEST_USER)).thenReturn(TEST_USER.getWorkouts());

    mockMvc.perform(get("/workout/"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("home"))
        .andExpect(model().attribute("user", TEST_USER.getEmail()))
        .andExpect(model().attribute("userName", TEST_USER.getName()))
        .andExpect(model().attribute("page", "fragments.html :: user-panel"))
        .andExpect(model().attribute("userPanelPage",
            "fragments.html :: user-panel-workout-details"))
        .andExpect(model().attribute("workouts", TEST_USER.getWorkouts()));

    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
    verify(userService).checkLoggedInUserForAdminRights();
    verify(service).findWorkoutsByUser(TEST_USER);
  }

  @Test(expected = ApiRequestException.class)
  public void addWorkoutGetUserIsNull() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(null);

    performMockMvcMethodAndCheckErrorMessage("get", "/workout/",
        "No such user in the database!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
  }

  @Test(expected = ApiRequestException.class)
  public void addWorkoutGetDatabaseError() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenThrow(HibernateException.class);

    performMockMvcMethodAndCheckErrorMessage("get", "/workout/",
        "There's a problem with database connection!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
  }

  private void performMockMvcMethodAndCheckErrorMessage(
      String method, String url, String message) throws Throwable {
    try {
      if (method.equals("get")) mockMvc.perform(get(url)).andExpect(status().isOk());
      if (method.equals("post")) mockMvc.perform(post(url)).andExpect(status().isOk());
      if (method.equals("delete")) mockMvc.perform(delete(url)).andExpect(status().isOk());
    } catch (NestedServletException e) {
      assertEquals(message, e.getCause().getMessage());
      throw e.getCause();
    }
  }
}
