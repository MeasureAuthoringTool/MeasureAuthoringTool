package mat.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.ConversionType;
import mat.client.shared.MatRuntimeException;
import mat.server.service.FhirOrchestrationGatewayService;

@Service
public class FhirOrchestrationGatewayServiceImpl implements FhirOrchestrationGatewayService {

    private static final Log logger = LogFactory.getLog(FhirOrchestrationGatewayServiceImpl.class);
    private static final String FHIR_ORCH_MEASURE_SRVC_PARAMS = "?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
    private static final String SIMPLE_XML_SOURCE = "SIMPLE";
    private static final String MEASURE_XML_SOURCE = "MEASURE";

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}orchestration/measure")
    private String fhirMeasureOrchestrationUrl;
    private RestTemplate restTemplate;

    public FhirOrchestrationGatewayServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ConversionResultDto convert(String measureId, boolean draft) {
        return callRemoteService(measureId, ConversionType.CONVERSION, draft);
    }

//    @Cacheable(value = "ConversionResultDto.validate", condition = "!#draft")
    @Override
    public ConversionResultDto validate(String measureId, boolean draft) {
        return callRemoteService(measureId, ConversionType.VALIDATION, draft);
    }

    private ConversionResultDto callRemoteService(String measureId, ConversionType conversionType, boolean draft) {
        if (logger.isDebugEnabled()) {
            logger.debug("callRemoteService " + measureId + " " + conversionType + " draft: " + draft);
        }
        String executeQuery = fhirMeasureOrchestrationUrl + FHIR_ORCH_MEASURE_SRVC_PARAMS;
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        uriVariables.put("conversionType", conversionType.name());
        uriVariables.put("xmlSource", draft ? MEASURE_XML_SOURCE : SIMPLE_XML_SOURCE);
        ResponseEntity<ConversionResultDto> response;
        try {
            response = restTemplate.exchange(executeQuery, HttpMethod.PUT, null, ConversionResultDto.class, uriVariables);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new MatRuntimeException("FHIR's conversion service returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

}
