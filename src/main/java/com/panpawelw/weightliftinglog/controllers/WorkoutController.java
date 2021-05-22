package com.panpawelw.weightliftinglog.controllers;

import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import com.panpawelw.weightliftinglog.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.User;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;
import com.panpawelw.weightliftinglog.services.UserService;
import com.panpawelw.weightliftinglog.services.WorkoutService;

import java.util.ArrayList;
import java.util.Base64;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

  private final WorkoutService workoutService;
  private final UserService userService;
  private final FileService fileService;

  @Autowired
  public WorkoutController(WorkoutService workoutService, UserService userService,
      @Qualifier("s3FileService") FileService fileService) {
    this.workoutService = workoutService;
    this.userService = userService;
    this.fileService = fileService;
  }

  @ResponseBody
  @GetMapping("/{workoutId}")
  public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
    WorkoutDeserialized result = workoutService.findWorkoutById(workoutId);
    if (result == null) {
      throw new ApiRequestException("This workout does not exist in the database");
    }
    return result;
  }

  @GetMapping("/")
  public String addWorkoutGet(Model model) {
    User user = userService.findUserByEmail(userService.getLoggedInUsersEmail());
    model.addAttribute("user", user.getEmail());
    model.addAttribute("userName", user.getName());
    model.addAttribute("adminRights", userService.checkLoggedInUserForAdminRights());
    model.addAttribute("page", "fragments.html :: user-panel");
    model.addAttribute("userPanelPage", "fragments.html :: user-panel-workout-details");
    model.addAttribute("workouts", workoutService.findWorkoutsByUser(user));
    return "home";
  }

  @ResponseBody
  @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void addWorkoutPost(@RequestPart("workout") WorkoutDeserialized workoutDeserialized,
      @RequestPart(name = "filesToRemove", required = false)
          ArrayList<String> filesToRemove,
      @RequestPart(name = "filesToUpload", required = false)
          MultipartFile[] filesToUpload) {
    workoutDeserialized.setUser
        (userService.findUserByEmail(userService.getLoggedInUsersEmail()));
    workoutDeserialized.setId(workoutService.saveWorkout(workoutDeserialized));
    if (!filesToRemove.isEmpty()) {
      filesToRemove.forEach((filename) ->
          fileService.deleteFileByWorkoutAndFilename(workoutDeserialized, filename));
    }
    if (filesToUpload.length > 0) {
      fileService.storeAllFilesByWorkout(workoutDeserialized, filesToUpload);
    }
    if (workoutService.saveWorkout(workoutDeserialized) == null) {
      throw new ApiRequestException("There was a problem saving workout to the database!");
    }
  }

  @ResponseBody
  @DeleteMapping("/{workoutId}")
  public void deleteWorkout(@PathVariable long workoutId) {
    if(!workoutService.findWorkoutById(workoutId).getFilenames().isEmpty()) {
      fileService.deleteAllFilesByWorkoutId(workoutId);
    }
    if(workoutService.deleteWorkout(workoutId) != 1) {
      throw new ApiRequestException("There was a problem deleting workout from the database!");
    }
  }

  @ResponseBody
  @GetMapping(value = "/file/{workoutId}/{filename}", produces =
      MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getFileByWorkoutId(@PathVariable Long workoutId,
      @PathVariable String filename) {
    MediaFile mediaFileToSend = fileService.getFileByWorkoutIdAndFilename(workoutId,
        filename);
    byte[] fileContent = mediaFileToSend.getContent();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=" + mediaFileToSend.getFilename())
        .header("type", mediaFileToSend.getType())
        .body(Base64.getEncoder().encode(fileContent));
  }
}