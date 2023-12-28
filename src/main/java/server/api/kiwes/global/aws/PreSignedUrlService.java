package server.api.kiwes.global.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * AWS PreSigned URL
 */
@Component
@RequiredArgsConstructor
public class PreSignedUrlService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String location;

    public String getPreSignedUrl(String prefix, String fileName) {

        if (!prefix.equals(" ")) {
            fileName = prefix  + fileName;
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

//    private String onlyOneFileName(String filename){
//        return UUID.randomUUID().toString()+filename;
//
//    }

//    public String findByName(String path) {
//        if (!amazonS3.doesObjectExist(bucket,editPath+ useOnlyOneFileName))
//            return "File does not exist";
//        log.info("Generating signed URL for file name {}", useOnlyOneFileName);
//        return  amazonS3.getUrl(bucket,editPath+useOnlyOneFileName).toString();
//        return "https://"+bucket+".s3."+location+".amazonaws.com/"+path+"/"+useOnlyOneFileName;
//    }
}
