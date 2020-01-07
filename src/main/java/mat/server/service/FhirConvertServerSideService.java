package mat.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.ConversionType;
import mat.client.shared.MatRuntimeException;

@Service
public class FhirConvertServerSideService {

    private static final String FHIR_ORCH_MEASURE_SRVC_PARAMS = "?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
    @Value("${FHIR_ORCH_MEASURE_SRVC_URL:http://localhost:9080/orchestration/measure}")
    private String fhirOrchestration;
    private RestTemplate restTemplate;

    public FhirConvertServerSideService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ConversionResultDto convert(String measureId) {
        return callRemoteService(measureId, ConversionType.CONVERSION);
    }

    public ConversionResultDto validate(String measureId) {
        return callRemoteService(measureId, ConversionType.VALIDATION);
    }

    private ConversionResultDto callRemoteService(String measureId, ConversionType conversionType) {
        String executeQuery = fhirOrchestration + FHIR_ORCH_MEASURE_SRVC_PARAMS;
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        uriVariables.put("conversionType", conversionType.name());
        uriVariables.put("xmlSource", "SIMPLE");
        ResponseEntity<ConversionResultDto> response
                = restTemplate.exchange(executeQuery, HttpMethod.PUT, null, ConversionResultDto.class, uriVariables);
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new MatRuntimeException("FHIR conversion service returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

}
