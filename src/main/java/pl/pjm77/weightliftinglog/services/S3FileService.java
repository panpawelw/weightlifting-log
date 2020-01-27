package pl.pjm77.weightliftinglog.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pjm77.weightliftinglog.AWS.AmazonS3Configuration;
import pl.pjm77.weightliftinglog.models.WorkoutDeserialized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        for (MultipartFile workoutFile : workoutFiles) {
            try {
//                File file = convertMultiPartFileToFile(workoutFile);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(workoutFile.getContentType());
                objectMetadata.setContentLength(workoutFile.getSize());
                amazonS3Client.putObject(bucketName, workoutFile.getOriginalFilename(),
                        workoutFile.getInputStream(), objectMetadata);
            } catch (AmazonClientException | IOException e) {
                throw new RuntimeException(("Error while uploading file!"));
            }
        }
//        return amazonS3Client.getUrl(bucketName, filename);
    }

    public File getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
        return new File(filename);
    }

    public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workoutDeserialized,
                                               String filename) {
    }

    public void deleteAllByWorkoutId(long workoutId) {
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}