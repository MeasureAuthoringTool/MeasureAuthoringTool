package mat.server.util;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mat.dto.MeasureTransferDTO;
import org.apache.commons.lang3.StringUtils;

public class MeasureTransferUtil {
    public static final int MEASURE_PACKAGE_EMPTY = 1;
    private static AmazonS3 s3Client = null;

    private static AmazonS3 getAwsS3Client() {
        if (s3Client == null) {
            s3Client = AmazonS3ClientBuilder
                    .standard()
                    .withRegion(getSystemProperty(
                            "MEASURE_TRANSFER_S3_AWS_REGION"))
                    .withCredentials(new DefaultAWSCredentialsProviderChain()) // to avoid credential lookup chain
                    .build();
        }
        return s3Client;
    }

    public static PutObjectResult uploadMeasureDataToS3Bucket(MeasureTransferDTO measureTransferDTO, String measureId)
            throws JsonProcessingException {
        String objectKeyName = "measure_" + measureId;
        String bucketName = getSystemProperty("MEASURE_TRANSFER_S3_BUCKET_NAME");

        ObjectMapper objectMapper = new ObjectMapper();
        String transferJson = objectMapper.writeValueAsString(measureTransferDTO);
        return getAwsS3Client()
                .putObject(bucketName, objectKeyName, transferJson);
    }

    public static String getSystemProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if(!StringUtils.isBlank(property)){
            return property;
        }
        return null;
    }
}
