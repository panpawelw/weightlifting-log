package pl.pjm77.weightliftinglog.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.models.File;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;
import pl.pjm77.weightliftinglog.repositories.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void storeAllFiles(WorkoutDeserialized workoutDeserialized,
                              MultipartFile[] workoutFiles) {
        List<File> files = new ArrayList<>();
        List<String> filenames = workoutDeserialized.getFilenames();
        Long workoutId = workoutDeserialized.getId();
        for (MultipartFile file : workoutFiles) {
            String filename = file.getOriginalFilename();
            try {
                files.add(new File(0L, workoutId, filename,
                        file.getContentType(), file.getBytes()));
                filenames.add(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            workoutDeserialized.setFilenames(filenames);
        }
        fileRepository.saveAll(files);
        fileRepository.flush();
    }

    public File getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
        return fileRepository.findFileByWorkoutIdAndFilename(workoutId, filename);
    }

    public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized,
                                               String filename) {
        List<String> filenames = workoutDeserialized.getFilenames();
        fileRepository.deleteByWorkoutIdAndFilename(workoutDeserialized.getId(), filename);
        filenames.remove(filename);
        workoutDeserialized.setFilenames(filenames);
    }

    public void deleteAllByWorkoutId(long workoutId) {
        fileRepository.deleteAllByWorkoutId(workoutId);
    }
}