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
import java.util.List;

@Service
public class CqlAttributesRemoteCall {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mat.fhirMatServices.url}")
    private String fhirMatServicesUrl;

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/find";

    List<String> fhirDataTypeList = new ArrayList<>();
    List<String> fhirAttributeList = new ArrayList<>();

    public void getFhirAttributeAndDataTypes(CQLConstantContainer cqlConstantContainer) {

        ResponseEntity<ConversionMapping[]> fhirResponse;
        try {
            fhirResponse = restTemplate.getForEntity(fhirMatServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, ConversionMapping[].class);
        } catch (RestClientResponseException e) {
            throw new MatRuntimeException(e);
        }
        ConversionMapping[] conversionMappings = fhirResponse.getBody();

        for (ConversionMapping conversionMapping : conversionMappings) {

            if(!fhirDataTypeList.contains(conversionMapping.getMatDataTypeDescription())) {
                fhirDataTypeList.add(conversionMapping.getMatDataTypeDescription());
            }
            if(!fhirAttributeList.contains(conversionMapping.getMatAttributeName())) {
                fhirAttributeList.add(conversionMapping.getMatAttributeName());
            }
        }
        cqlConstantContainer.setFhirCqlDataTypeList(fhirDataTypeList);
        cqlConstantContainer.setFhirCqlAttributeList(fhirAttributeList);
    }

}
