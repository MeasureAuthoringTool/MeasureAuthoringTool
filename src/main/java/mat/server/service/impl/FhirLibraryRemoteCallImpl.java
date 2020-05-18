package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import mat.client.shared.MatRuntimeException;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.server.service.FhirLibraryRemoteCall;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.fhir.r4.model.Library;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
public class FhirLibraryRemoteCallImpl implements FhirLibraryRemoteCall {

    private static final Log logger = LogFactory.getLog(FhirLibraryRemoteCallImpl.class);

    private static final String ID_PARAMS = "?id={id}";

    private static final String CONVERT_PARAMS = ID_PARAMS + "&conversionType={conversionType}";

    private RestTemplate restTemplate;

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}library/convertStandAlone")
    private String fhirLibraryConversionRemoteUrl;

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}/library/pushStandAloneLibrary")
    private String pushUrl;

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}/library/packager/minimum/json")
    private String packageUrlXml;

    private FhirContext fhirContext;

    public FhirLibraryRemoteCallImpl(FhirContext fhirContext, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.fhirContext = fhirContext;
    }

    /**
     * @param libraryId The library id.
     * @return The URL of the resource on the hapi fhir server.
     */
    public String pushStandAlone(String libraryId) {
        return rest(pushUrl + ID_PARAMS,HttpMethod.POST,String.class,buildUrlMap(libraryId));
    }

    /**
     * @param libraryId The library id.
     * @return The fhir library object.
     */
    public Library packageStandAlone(String libraryId) {
        String json = rest(packageUrlXml + ID_PARAMS,HttpMethod.GET,String.class,buildUrlMap(libraryId));
        return fhirContext.newJsonParser().parseResource(Library.class,json);
    }

    @Override
    public ConversionResultDto convert(String libraryId, ConversionType conversionType) {
        if (logger.isDebugEnabled()) {
            logger.debug("convert " + libraryId + " " + ConversionType.CONVERSION);
        }
        return convertStandalone(libraryId, conversionType);
    }

    public ConversionResultDto validate(String libraryId) {
        if (logger.isDebugEnabled()) {
            logger.debug("validate " + libraryId + " " + ConversionType.VALIDATION);
        }
        return convertStandalone(libraryId, ConversionType.VALIDATION);
    }

    private <T> T rest(String url, HttpMethod method, Class<T> responseType, Map<String,Object> paramMap) {
        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(url,
                    method,
                    null,
                    responseType,
                    paramMap);
        } catch (HttpClientErrorException e) {
            throw new MatRuntimeException(e);
        }
        if (response.getStatusCode().isError()) {
            throw new MatRuntimeException("url returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

    private ConversionResultDto convertStandalone(String libraryId, ConversionType conversionType) {
        Map<String, Object> uriVariables = buildUrlMap(libraryId);
        uriVariables.put("conversionType", conversionType.name());

        return rest(fhirLibraryConversionRemoteUrl + CONVERT_PARAMS,HttpMethod.POST,ConversionResultDto.class, uriVariables);
    }

    private Map<String,Object> buildUrlMap(String libId) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", libId);
        return result;
    }
}
