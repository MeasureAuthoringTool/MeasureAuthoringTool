package mat.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import mat.client.shared.MatRuntimeException;

@Service
public class CqlAttributesRemoteCallService {

    private static final String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/find";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${qdm.qicore.mapping.services.url}")
    private String fhirMatServicesUrl;


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
