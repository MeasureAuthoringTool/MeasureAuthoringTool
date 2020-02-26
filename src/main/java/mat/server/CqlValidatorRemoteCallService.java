package mat.server;

import mat.client.shared.MatRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CqlValidatorRemoteCallService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${CQL_ELM_TRANSLATION_URL:http://localhost:7070}")
    private String cqlElmtranslationUrl;

    private String CQL_VALIDATION_RESOURCE = "/cql/translator/cql";

    public String validateCqlExpression(String cqlFileString) {

        ResponseEntity<String> fhirCqlValidationResponse;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> request = new HttpEntity<>(cqlFileString, headers);
            fhirCqlValidationResponse = restTemplate.exchange(cqlElmtranslationUrl + CQL_VALIDATION_RESOURCE, HttpMethod.PUT, request, String.class);
        } catch (RestClientException e) {
            throw new MatRuntimeException(e);
        }
        return fhirCqlValidationResponse.getBody();
    }
}
