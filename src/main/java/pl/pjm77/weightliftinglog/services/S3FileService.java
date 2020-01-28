package pl.pjm77.weightliftinglog.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.AWS.AmazonS3Configuration;
import pl.pjm77.weightliftinglog.models.MediaFile;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class S3FileService {

    private final String bucketName = "weightliftinglogbucket";
    private final AmazonS3Configuration amazonS3Configuration;

    public S3FileService(AmazonS3Configuration amazonS3Configuration) {
        this.amazonS3Configuration = amazonS3Configuration;
    }

    public void StoreAllFiles(WorkoutDeserialized workoutDeserialized,
                              MultipartFile[] workoutFiles) {
        AmazonS3 amazonS3Client = amazonS3Configuration.amazonS3Client();
        List<String> filenames = workoutDeserialized.getFilenames();
        String filename;
        for (MultipartFile workoutFile : workoutFiles) {
                filename = workoutFile.getOriginalFilename();
                System.out.println(filename);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(workoutFile.getContentType());
                objectMetadata.setContentLength(workoutFile.getSize());
            try {
                amazonS3Client.putObject(bucketName, filename,
                        workoutFile.getInputStream(), objectMetadata);
                filenames.add(filename);
            } catch (AmazonClientException | IOException e) {
                throw new RuntimeException("Error while uploading file!");
            }
            workoutDeserialized.setFilenames(filenames);
        }
        System.out.println(workoutDeserialized.getFilenames().toString());
    }

    public File getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
        return new File(filename);
    }

    public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized,
                                               String filename) {
        AmazonS3 amazonS3Client = amazonS3Configuration.amazonS3Client();
        try {
            amazonS3Client.deleteObject(bucketName, filename);
        } catch (AmazonClientException e) {
            throw new RuntimeException("Error while deleting file!");
        }
    }

    public void deleteAllByWorkoutId(long workoutId) {
    }
}