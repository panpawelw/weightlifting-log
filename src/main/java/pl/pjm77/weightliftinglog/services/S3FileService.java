package pl.pjm77.weightliftinglog.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.AWS.AmazonS3Configuration;
import pl.pjm77.weightliftinglog.models.MediaFile;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;

import java.io.IOException;
import java.util.List;

@Service
public class S3FileService {

    private final String bucketName = "weightliftinglogbucket";
    private final AmazonS3Configuration amazonS3Configuration;
    private final WorkoutService workoutService;

    public S3FileService(AmazonS3Configuration amazonS3Configuration, WorkoutService workoutService) {
        this.amazonS3Configuration = amazonS3Configuration;
        this.workoutService = workoutService;
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

    public MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
        AmazonS3 amazonS3Client = amazonS3Configuration.amazonS3Client();
        S3Object s3Object;
        ObjectMetadata objectMetadata;
        byte[] content;
        try {
            s3Object = amazonS3Client.getObject(bucketName, filename);
            objectMetadata = s3Object.getObjectMetadata();
            content = IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (IOException | AmazonClientException e) {
            throw new RuntimeException("Error while streaming file!");
        }
        return new MediaFile(null, workoutId, s3Object.getKey(), objectMetadata.getContentType(),
                content);
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
        AmazonS3 amazonS3Client = amazonS3Configuration.amazonS3Client();
        List<String> filesToDelete = workoutService.findWorkoutById(workoutId).getFilenames();
        filesToDelete.forEach(filename-> {
            try {
                amazonS3Client.deleteObject(bucketName, filename);
            } catch (AmazonClientException e) {
                throw new RuntimeException("Error while deleting files!");
            }
        });
    }
}