package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.models.MediaFile;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.services.DBFileService;
import pl.pjm77.weightliftinglog.services.S3FileService;
import pl.pjm77.weightliftinglog.services.UserService;
import pl.pjm77.weightliftinglog.services.WorkoutService;

import java.util.ArrayList;
import java.util.Base64;

import static pl.pjm77.weightliftinglog.services.UserService.checkLoggedInUserForAdminRights;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;
    private final DBFileService fileService;
    private final S3FileService s3FileService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService,
                             DBFileService fileService, S3FileService s3FileService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.fileService = fileService;
        this.s3FileService = s3FileService;
    }

    @ResponseBody
    @GetMapping("/{workoutId}")
    public WorkoutDeserialized getWorkoutById(@PathVariable long workoutId) {
        return workoutService.findWorkoutById(workoutId);
    }

    @GetMapping("/")
    public String addWorkoutGet(Model model) {
        User user = userService.findUserByEmail(UserService.getLoggedInUsersEmail());
        model.addAttribute("user", user.getEmail());
        model.addAttribute("userName", user.getName());
        model.addAttribute("adminRights", checkLoggedInUserForAdminRights());
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
                (userService.findUserByEmail(UserService.getLoggedInUsersEmail()));
        workoutDeserialized.setId(workoutService.saveWorkout(workoutDeserialized));
        if (!filesToRemove.isEmpty()) {
            filesToRemove.forEach((filename) ->
                    fileService.deleteFileByWorkoutAndFilename(workoutDeserialized, filename));
        }
        if (filesToUpload.length > 0) {
            s3FileService.StoreAllFiles(workoutDeserialized, filesToUpload);
        }
        workoutService.saveWorkout(workoutDeserialized);
    }

    @ResponseBody
    @DeleteMapping("/{workoutId}")
    public void deleteWorkout(@PathVariable long workoutId) {
        workoutService.deleteWorkout(workoutId);
        fileService.deleteAllByWorkoutId(workoutId);
    }

    @ResponseBody
    @GetMapping(value = "/file/{workoutId}/{filename}", produces =
            MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getFileByWorkoutId(@PathVariable Long workoutId,
                                             @PathVariable String filename) {
        MediaFile mediaFileToSend = fileService.getFileByWorkoutIdAndFilename(workoutId, filename);
        byte[] fileContent = mediaFileToSend.getContent();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + mediaFileToSend.getFilename())
                .header("type", mediaFileToSend.getType())
                .body(Base64.getEncoder().encode(fileContent));
    }
}