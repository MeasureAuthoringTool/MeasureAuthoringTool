package mat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import mat.client.shared.CQLConstantContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CqlAttributesRemoteCallServiceTest {

    private String fhirMatServicesUrl = "https://localhost:9090";

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/find";

    @Mock
    private RestTemplate restTemplate;

    private CQLConstantContainer cqlConstantContainer = new CQLConstantContainer();

    @InjectMocks
    private CqlAttributesRemoteCallService cqlAttributesRemoteCallService;

    @Test
    public void testGetFhirAttributeAndDataTypes() throws Exception {

        URL path = CqlAttributesRemoteCallService.class.getClassLoader().getResource("fhirAttributes&DataTypes.txt");
        ConversionMapping[] conversionMappings = new ObjectMapper().readValue(new File(path.getFile()), ConversionMapping[].class);
        Mockito.when(restTemplate.getForEntity(fhirMatServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, ConversionMapping[].class))
                .thenReturn(new ResponseEntity(conversionMappings, HttpStatus.OK));

        cqlAttributesRemoteCallService.getFhirAttributeAndDataTypes(cqlConstantContainer);
        assertEquals(5,cqlConstantContainer.getFhirCqlAttributeList().size());
        assertEquals(2,cqlConstantContainer.getFhirCqlDataTypeList().size());

    }
}