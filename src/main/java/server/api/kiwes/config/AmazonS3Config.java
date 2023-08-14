package server.api.kiwes.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AmazonS3Config {

//    @Value("${cloud.aws.credentials.access-key}")
    @Value("AKIA3RUYO7LUXFHORFEK")
    private String accessKey;

//    @Value("${cloud.aws.credentials.secret-key}")
    @Value("ED1KzSRwq4VwIJJUA0BtFx5p65LN++WCrxKZV4xE")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
    @Bean
    @Primary
    public BasicAWSCredentials awsCredentialsProvider(){
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    @Primary
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

}
