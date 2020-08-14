package mat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import mat.server.spreadsheet.MatAttribute;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CqlAttributesRemoteCallServiceTest {

    private final static String FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES = "/matAttributes";
    private String fhirMatMappingServicesUrl = "https://localhost:9090";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MappingSpreadsheetService cqlAttributesRemoteCallService;

    @Test
    void testGetFhirAttributeAndDataTypes() throws Exception {
        Whitebox.setInternalState(cqlAttributesRemoteCallService, "fhirMatMappingServicesUrl", fhirMatMappingServicesUrl);

        URL path = MappingSpreadsheetService.class.getClassLoader().getResource("fhirAttributes&DataTypes.txt");
        assertNotNull(path);
        MatAttribute[] conversionMappingsTestData = new ObjectMapper().readValue(new File(path.getFile()), MatAttribute[].class);
        Mockito.when(restTemplate.getForEntity(fhirMatMappingServicesUrl + FHIR_MAT_SERVICES_RECOURSE_FOR_ATTRIBUTES, MatAttribute[].class))
                .thenReturn(new ResponseEntity<>(conversionMappingsTestData, HttpStatus.OK));

        List<MatAttribute> conversionMappings = cqlAttributesRemoteCallService.getMatAttributes();
        assertEquals(Arrays.asList(conversionMappingsTestData), conversionMappings);
    }
}