package mat.server;

import mat.client.shared.MatRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class CqlAttributesRemoteCallService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${QDM_QICORE_MAPPING_SERVICES_URL:http://localhost:9090}")
    private String fhirMatServicesUrl;

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/find";

    @Cacheable("fhirAttributesAndDataTypes")
    public ConversionMapping[] getFhirAttributeAndDataTypes() {

        ResponseEntity<ConversionMapping[]> fhirResponse;
        try {
            fhirResponse = restTemplate.getForEntity(fhirMatServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, ConversionMapping[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        return fhirResponse.getBody();
    }

}
