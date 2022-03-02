package mat.server.util;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mat.dto.MeasureTransferDTO;
import org.apache.commons.lang3.StringUtils;

public class MeasureTransferUtil {

    public static final AmazonS3 buildAwsS3Client() {
        final String AWS_REGION = getMeasureTransferS3Region();

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(AWS_REGION)
                .withCredentials(new InstanceProfileCredentialsProvider(true))
                .build();
    }

    public static final PutObjectResult uploadMeasureDataToS3Bucket(MeasureTransferDTO ob, String measureId)
            throws JsonProcessingException {
        String objectKeyName = "measure_" + measureId;
        String bucketName = getMeasureTransferS3BucketName();

        ObjectMapper objectMapper = new ObjectMapper();
        String transferJson = objectMapper.writeValueAsString(ob);
        return buildAwsS3Client()
                .putObject(bucketName, objectKeyName, transferJson);
    }

    public static final String getMeasureTransferS3Region() {
        return getSystemProperty("MEASURE_TRANSFER_S3_AWS_REGION");
    }

    public static final String getMeasureTransferS3Endpoint() {
        return getSystemProperty("MEASURE_TRANSFER_S3_ENDPOINT");
    }

    public static final String getMeasureTransferS3BucketName() {
        return getSystemProperty("MEASURE_TRANSFER_S3_BUCKET_NAME");
    }

    public static final String getSystemProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if(!StringUtils.isBlank(property)){
            return property;
        }
        return null;
    }
}
