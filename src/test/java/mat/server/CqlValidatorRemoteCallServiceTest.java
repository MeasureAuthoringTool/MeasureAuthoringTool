package mat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CqlValidatorRemoteCallServiceTest {

    private String cqlElmtranslationUrl = "https://localhost:7070";

    private final static String CQL_VALIDATION_RESOURCE = "/cql/translator/cql";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CqlValidatorRemoteCallService cqlValidatorRemoteCallService;

    @Test
    void testValidateCqlExpression() throws IOException {

        Whitebox.setInternalState(cqlValidatorRemoteCallService, "cqlElmtranslationUrl", cqlElmtranslationUrl);


        URL testResult = CqlValidatorRemoteCallService.class.getClassLoader().getResource("cqlExpressionValidationResults.txt");
        String cqlExpressionValidationTestData = new ObjectMapper().readValue(new File(testResult.getFile()), String.class);
        URL testRequest = CqlValidatorRemoteCallService.class.getClassLoader().getResource("cqlExpressionForValidation.txt");
        String cqlExpressionValidationRequestData = new ObjectMapper().readValue(new File(testRequest.getFile()), String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(cqlExpressionValidationRequestData, headers);

        Mockito.when(restTemplate.exchange(cqlElmtranslationUrl + CQL_VALIDATION_RESOURCE, HttpMethod.PUT, request, String.class))
                .thenReturn(new ResponseEntity(cqlExpressionValidationTestData, HttpStatus.OK));

        String cqlExpressionValidationResults = cqlValidatorRemoteCallService.validateCqlExpression(cqlExpressionValidationRequestData);

        assertEquals(cqlExpressionValidationTestData, cqlExpressionValidationResults);

    }
}