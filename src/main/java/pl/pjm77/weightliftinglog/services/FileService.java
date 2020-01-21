package pl.pjm77.weightliftinglog.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.models.File;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.repositories.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void storeAllFiles(Long workoutId, WorkoutDeserialized workout,
                              LinkedList<MultipartFile> workoutFiles) {
        List<File> files = new ArrayList<>();
        List<String> filenames = workout.getFilenames();
        workoutFiles.forEach((file) -> {
            String filename = file.getOriginalFilename();
            try {
                files.add(new File(0L, workoutId, filename,
                        file.getContentType(), file.getBytes()));
                filenames.add(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            workout.setFilenames(filenames);
        });
        fileRepository.saveAll(files);
        fileRepository.flush();
    }

    public File findFileByWorkoutIdAndFilename(Long workoutId, String filename) {
        return fileRepository.findFileByWorkoutIdAndFilename(workoutId, filename);
    }

    public void deleteFileByWorkoutAndFilename(Long workoutId, WorkoutDeserialized workout,
                                               String filename) {
        List<String> filenames = workout.getFilenames();
        fileRepository.deleteByWorkoutIdAndFilename(workoutId, filename);
        filenames.remove(filename);
        workout.setFilenames(filenames);
    }

    public void deleteAllByWorkoutId(long workoutId) {
        fileRepository.deleteAllByWorkoutId(workoutId);
    }
}