package mat.server.service.impl;

import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.client.shared.MatRuntimeException;
import mat.server.service.FhirLibraryConversionRemoteCall;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FhirLibraryConversionRemoteCallImpl implements FhirLibraryConversionRemoteCall {

    private static final Log logger = LogFactory.getLog(FhirLibraryConversionRemoteCallImpl.class);

    private static final String FHIR_LIBRARY_SRVC_PARAMS = "?id={id}&conversionType={conversionType}";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}library/convertStandAlone")
    private String fhirLibraryConversionRemoteUrl;

    public FhirLibraryConversionRemoteCallImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ConversionResultDto convert(String libraryId, ConversionType conversionType) {
        if (logger.isDebugEnabled()) {
            logger.debug("callRemoteService " + libraryId + " " + ConversionType.CONVERSION);
        }
        String executeQuery = fhirLibraryConversionRemoteUrl + FHIR_LIBRARY_SRVC_PARAMS;
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", libraryId);
        uriVariables.put("conversionType", conversionType.name());
        ResponseEntity<ConversionResultDto> response;
        try {
            response = restTemplate.exchange(executeQuery, HttpMethod.POST, null, ConversionResultDto.class, uriVariables);
        } catch (HttpClientErrorException e) {
            throw new MatRuntimeException(e);
        }
        if (response.getStatusCode().isError()) {
            throw new MatRuntimeException("FHIR's Cql Library conversion service returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }
}
