package pl.pjm77.weightliftinglog.AWS;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configuration {

    @Value("${aws.access.key.id}")
    private String awsKeyId;

    @Value("${aws.access.key.secret}")
    private String awsKeySecret;

    @Value("S{aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket")
    private String awsS3Bucket;

    @Bean(name = "awsKeyId")
    public String getAwsKeyId() {
        return awsKeyId;
    }

    @Bean(name = "awsKeySecret")
    public String getAwsKeySecret() {
        return awsKeySecret;
    }

    @Bean(name = "awsRegion")
    public String getAwsRegion() {
        return awsRegion;
    }

    @Bean(name = "awsS3Bucket")
    public String getAwsS3Bucket() {
        return awsS3Bucket;
    }

    @Bean(name = "awsCredentialsProvider")
    public AWSCredentialsProvider getAWSCredentials() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsKeyId, this.awsKeySecret);
        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}
