package mat.server;

import mat.client.shared.CQLConstantContainer;
import mat.client.shared.MatRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class CqlAttributesRemoteCallService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mat.fhirMatServices.url}")
    private String fhirMatServicesUrl;

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/find";

    public void getFhirAttributeAndDataTypes(CQLConstantContainer cqlConstantContainer) {

        HashSet<String> fhirDataTypeSet=new HashSet();
        HashSet<String> fhirAttributeSet=new HashSet();

        ResponseEntity<ConversionMapping[]> fhirResponse;
        try {
            fhirResponse = restTemplate.getForEntity(fhirMatServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, ConversionMapping[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        ConversionMapping[] conversionMappings = fhirResponse.getBody();

        for (ConversionMapping conversionMapping : conversionMappings) {
            fhirDataTypeSet.add(conversionMapping.getMatDataTypeDescription());
            fhirAttributeSet.add(conversionMapping.getMatAttributeName());
        }
        cqlConstantContainer.setFhirCqlDataTypeList(new ArrayList<>(fhirDataTypeSet));
        cqlConstantContainer.setFhirCqlAttributeList(new ArrayList<>(fhirAttributeSet));
    }

}
