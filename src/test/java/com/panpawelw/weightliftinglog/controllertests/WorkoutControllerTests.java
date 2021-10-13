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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
      1L, "Workout title", null, null, TEST_USER,
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
      Collections.singletonList(new Note(0, "Workout note")), Collections.emptyList());

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
  public void getWorkoutById() throws Exception {
    when(service.findWorkoutById(TEST_WORKOUT.getId())).thenReturn(TEST_WORKOUT);
    MvcResult result = mockMvc.perform(get("/workout/1"))
        .andExpect(status().isOk())
        .andReturn();
    String actual = result.getResponse().getContentAsString();
    String expected = new ObjectMapper().writeValueAsString(TEST_WORKOUT);

    assertEquals(expected, actual);
    verify(service).findWorkoutById(TEST_WORKOUT.getId());
  }

  @Test(expected = ApiRequestException.class)
  public void getWorkoutByIdNoSuchWorkout() throws Throwable {
    when(service.findWorkoutById(1)).thenReturn(null);

    mockMvcPerform("get", "/workout/1", "No such workout in the database!");
    verify(service).findWorkoutById(1);
  }

  @Test(expected = ApiRequestException.class)
  public void getWorkoutByIdDatabaseError() throws Throwable {
    when(service.findWorkoutById(1)).thenThrow(HibernateException.class);

    mockMvcPerform("get", "/workout/1",
        "There's a problem with database connection!");
    verify(service).findWorkoutById(1);
  }

  @Test
  public void addWorkoutGet() throws Exception {
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
  public void addWorkoutGetNoSuchUser() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(null);

    mockMvcPerform("get", "/workout/", "No such user in the database!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
  }

  @Test(expected = ApiRequestException.class)
  public void addWorkoutGetDatabaseError() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenThrow(HibernateException.class);

    mockMvcPerform("get", "/workout/",
        "There's a problem with database connection!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
  }

  @Test
  public void addWorkoutPost() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER);
    when(service.saveWorkout(any())).thenReturn(TEST_WORKOUT.getId());

    mockMvcPerform();
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
    verify(service, times(2)).saveWorkout(TEST_WORKOUT);
  }

  @Test(expected = ApiRequestException.class)
  public void addWorkoutPostProblemSavingWorkout() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER);
    when(service.saveWorkout(TEST_WORKOUT)).thenReturn(null);

    mockMvcPerform("There's a problem saving workout to the database!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
    verify(service).saveWorkout(TEST_WORKOUT);
  }

  @Test(expected = ApiRequestException.class)
  public void addWorkoutPostDatabaseError() throws Throwable {
    when(userService.getLoggedInUsersEmail()).thenReturn(TEST_USER.getEmail());
    when(userService.findUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER);
    when(service.saveWorkout(TEST_WORKOUT)).thenThrow(HibernateException.class);

    mockMvcPerform("There's a problem with database connection!");
    verify(userService).getLoggedInUsersEmail();
    verify(userService).findUserByEmail(TEST_USER.getEmail());
    verify(service).saveWorkout(TEST_WORKOUT);
  }

  @Test
  public void deleteWorkoutWithoutFiles() throws Exception {
    when(service.findWorkoutById(TEST_WORKOUT.getId())).thenReturn(TEST_WORKOUT);
    when(service.deleteWorkout(TEST_WORKOUT.getId())).thenReturn(1L);

    mockMvc.perform(delete("/workout/1")).andExpect(status().isOk());
    verify(service).findWorkoutById(TEST_WORKOUT.getId());
    verify(service).deleteWorkout(TEST_WORKOUT.getId());
  }

  @Test
  public void deleteWorkoutWithFiles() throws Exception {
    WorkoutDeserialized testWorkout = new WorkoutDeserialized(TEST_WORKOUT);
    testWorkout.setFilenames(Arrays.asList("file1.mp4", "file2.jpg", "file3.mp3"));
    when(service.findWorkoutById(testWorkout.getId())).thenReturn(testWorkout);
    when(service.deleteWorkout(testWorkout.getId())).thenReturn(1L);

    mockMvc.perform(delete("/workout/1")).andExpect(status().isOk());
    verify(fileService).deleteAllFilesByWorkoutId(testWorkout.getId());
    verify(service).findWorkoutById(testWorkout.getId());
    verify(service).deleteWorkout(testWorkout.getId());
  }

  @Test(expected = ApiRequestException.class)
  public void deleteWorkoutCouldNotDelete() throws Throwable {
    when(service.findWorkoutById(TEST_WORKOUT.getId())).thenReturn(TEST_WORKOUT);

    mockMvcPerform("delete", "/workout/1",
        "Could not delete workout from the database!");
    verify(service).findWorkoutById(TEST_WORKOUT.getId());
  }

  @Test(expected = ApiRequestException.class)
  public void deleteWorkoutDatabaseError() throws Throwable {
    when(service.findWorkoutById(TEST_WORKOUT.getId())).thenReturn(TEST_WORKOUT);
    when(service.deleteWorkout(TEST_WORKOUT.getId())).thenThrow(HibernateException.class);

    mockMvcPerform("delete", "/workout/1",
        "There's a problem with database connection!");
    verify(service).findWorkoutById(TEST_WORKOUT.getId());
    verify(service).deleteWorkout(TEST_WORKOUT.getId());
  }

  @Test
  public void getMediaFileByWorkoutIdAndFilename() {

  }

  @Test(expected = ApiRequestException.class)
  public void getMediaFileByWorkoutIdAndFilenameNoSuchFile() throws Throwable {
    when(fileService.getFileByWorkoutIdAndFilename(1L, "testfile.mp3"))
        .thenReturn(null);
    try {
      mockMvc.perform(get("/workout/file/1/testfile.mp3")).andExpect(status().is(404));
    } catch (NestedServletException e) {
      assertEquals("No such file in the database!", e.getCause().getMessage());
      throw e.getCause();
    }
  }

  @Test(expected = ApiRequestException.class)
  public void getMediaFileByWorkoutIdAndFilenameDatabaseError() {

  }

  /**
   * Helper method that performs mockMvc method and unwraps original exception from
   * NestedServletException and checks exception message.
   *
   * @param method  get or delete
   * @param url     url address
   * @param message message to check against
   * @throws Throwable original exception (ApiRequestException in this case)
   */
  private void mockMvcPerform(
      String method, String url, String message) throws Throwable {
    try {
      if (method.equals("get")) {
        mockMvc.perform(get(url)).andExpect(status().isOk());
      }
      if (method.equals("delete")) {
        mockMvc.perform(delete(url)).andExpect(status().isOk());
      }
    } catch (NestedServletException e) {
      assertEquals(message, e.getCause().getMessage());
      throw e.getCause();
    }
  }

  /**
   * Helper method that performs mockMvc post and optionally unwraps original exception from
   * NestedServletException and checks exception message.
   *
   * @param message optional exception message to check against
   * @throws Throwable original exception (ApiRequestException in this case)
   */
  private void mockMvcPerform(String...message) throws Throwable {
    String testWorkout = new ObjectMapper().writeValueAsString(TEST_WORKOUT);
    MockMultipartFile workoutJson = new MockMultipartFile("workout", "",
        "application/json", testWorkout.getBytes());
    MockMultipartFile filesToRemove = new MockMultipartFile("filesToRemove", "",
        "application/json", "[]".getBytes());
    MockMultipartFile filesToUpload = new MockMultipartFile("filesToUpload", "",
        "application.json", "[]".getBytes());

    try {
      mockMvc.perform(multipart("/workout/")
              .file(workoutJson)
              .file(filesToRemove)
              .file(filesToUpload))
          .andExpect(status().isOk());
    } catch (NestedServletException e) {
        assertEquals(message[0], e.getCause().getMessage());
      throw e.getCause();
    }
  }
}
