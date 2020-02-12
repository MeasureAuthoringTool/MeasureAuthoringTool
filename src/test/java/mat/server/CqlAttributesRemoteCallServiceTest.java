package mat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CqlAttributesRemoteCallServiceTest {

    private String fhirMatServicesUrl = "https://localhost:9090";

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/filtered";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CqlAttributesRemoteCallService cqlAttributesRemoteCallService;

    @Test
    public void testGetFhirAttributeAndDataTypes() throws Exception {

        Whitebox.setInternalState(cqlAttributesRemoteCallService, "fhirMatServicesUrl", fhirMatServicesUrl);

        URL path = CqlAttributesRemoteCallService.class.getClassLoader().getResource("fhirAttributes&DataTypes.txt");
        ConversionMapping[] conversionMappingsTestData = new ObjectMapper().readValue(new File(path.getFile()), ConversionMapping[].class);
        Mockito.when(restTemplate.getForEntity(fhirMatServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, ConversionMapping[].class))
                .thenReturn(new ResponseEntity(conversionMappingsTestData, HttpStatus.OK));

        ConversionMapping[] conversionMappings = cqlAttributesRemoteCallService.getFhirAttributeAndDataTypes();
        assertEquals(conversionMappingsTestData, conversionMappings);

    }
}