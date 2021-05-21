package com.panpawelw.weightliftinglog.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.panpawelw.weightliftinglog.exceptions.ApiRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.panpawelw.weightliftinglog.models.MediaFile;
import com.panpawelw.weightliftinglog.models.WorkoutDeserialized;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class S3FileService implements FileService {

  @Value("${cloud.aws.credentials.accessKey}")
  private String awsKeyId;

  @Value("${cloud.aws.credentials.secretKey}")
  private String awsKeySecret;

  @Value("${cloud.aws.region.static}")
  private String awsRegion;

  @Value("${cloud.aws.bucket.name}")
  private String bucketName;

  @PostConstruct
  private void initializeAmazon() {
    BasicAWSCredentials credentialsProvider =
        new BasicAWSCredentials(awsKeyId, awsKeySecret);
    this.amazonS3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentialsProvider))
        .withRegion(Regions.fromName(awsRegion))
        .build();
  }

  private AmazonS3 amazonS3Client;

  private final WorkoutService workoutService;

  public S3FileService(WorkoutService workoutService) {
    this.workoutService = workoutService;
  }

  public void storeAllFilesByWorkout(WorkoutDeserialized workoutDeserialized,
                                     MultipartFile[] workoutFiles) {
    List<String> filenames = workoutDeserialized.getFilenames();
    String filename;
    for (MultipartFile file : workoutFiles) {
      filename = file.getOriginalFilename();
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(file.getContentType());
      objectMetadata.setContentLength(file.getSize());
      try {
        amazonS3Client.putObject(bucketName, workoutDeserialized.getId() + "\\"
            + filename, file.getInputStream(), objectMetadata);
        filenames.add(filename);
      } catch (AmazonClientException | IOException e) {
        throw new ApiRequestException("Error uploading file!");
      }
      workoutDeserialized.setFilenames(filenames);
    }
  }

  public MediaFile getFileByWorkoutIdAndFilename(Long workoutId, String filename) {
    S3Object s3Object;
    ObjectMetadata objectMetadata;
    byte[] content;
    filename = workoutId + "\\" + filename;
    try {
      s3Object = amazonS3Client.getObject(bucketName, filename);
      objectMetadata = s3Object.getObjectMetadata();
      content = IOUtils.toByteArray(s3Object.getObjectContent());
    } catch (IOException | AmazonClientException e) {
      throw new ApiRequestException("Error streaming file!");
    }
    return new MediaFile(null, workoutId, s3Object.getKey(), objectMetadata.getContentType(),
        content);
  }

  public void deleteFileByWorkoutAndFilename(WorkoutDeserialized workout, String filename) {
    filename = workout.getId() + "\\" + filename;
    try {
      amazonS3Client.deleteObject(bucketName, filename);
    } catch (AmazonClientException e) {
      throw new ApiRequestException("Error deleting file!");
    }
  }

  public void deleteAllFilesByWorkoutId(long workoutId) {
    List<String> filesToDelete = workoutService.findWorkoutById(workoutId).getFilenames();
    filesToDelete.forEach(filename -> {
      try {
        filename = workoutId + "\\" + filename;
        amazonS3Client.deleteObject(bucketName, filename);
      } catch (AmazonClientException e) {
        throw new ApiRequestException("Error deleting files!");
      }
    });
  }

  public void setAmazonS3Client(AmazonS3 client) {
    this.amazonS3Client = client;
  }
}