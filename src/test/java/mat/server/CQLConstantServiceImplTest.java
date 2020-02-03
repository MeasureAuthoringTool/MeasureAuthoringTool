package mat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import mat.client.shared.CQLConstantContainer;
import mat.config.DaoTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = DaoTestConfig.class)
@ExtendWith(MockitoExtension.class)
class CQLConstantServiceImplTest {

    @Value("${mat.fhirMatServices.url}")
    private String fhirMatServicesUrl;

    private final String fhirMatServiceResouceForAttributes = "/find";

    @Mock
    RestTemplate restTemplate;

    final CQLConstantContainer cqlConstantContainer = new CQLConstantContainer();

    @InjectMocks
    CQLConstantServiceImpl cqlConstantServiceImpl;

    @Test
    void getFhirAttributeAndDataTypes() {

        Mockito.when(restTemplate.getForObject(fhirMatServicesUrl + fhirMatServiceResouceForAttributes, String.class)).thenAnswer(invocation -> {
            URL path = CQLConstantServiceImpl.class.getClassLoader().getResource("fhirAttributes&DataTypes.txt");
            return new ObjectMapper().readValue(new File(path.getFile()), String.class);
        });

        cqlConstantServiceImpl.getFhirAttributeAndDataTypes(cqlConstantContainer);
        assertEquals(5,cqlConstantContainer.getFhirCqlAttributeList().size());
        assertEquals(2,cqlConstantContainer.getFhirCqlDataTypeList().size());
    }
}