package pl.pjm77.weightliftinglog.AWS;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configuration {

    @Value("${cloud.aws.credentials.accessKey}")
    private String awsKeyId;

    @Value("${cloud.aws.credentials.secretKey}")
    private String awsKeySecret;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Bean
    AWSStaticCredentialsProvider credentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKeyId, awsKeySecret));
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(awsRegion)
                .build();
    }
}