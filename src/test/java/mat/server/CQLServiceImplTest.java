package mat.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mat.cql.CqlParser;
import mat.cql.CqlVisitorFactory;
import mat.dao.UserDAO;
import mat.server.service.MeasurePackageService;
import mat.shared.SaveUpdateCQLResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CQLServiceImplTest {

    @Mock
    private MeasurePackageService measurePackageService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private CqlValidatorRemoteCallService cqlValidatorRemoteCallService;

    @Mock
    private CqlVisitorFactory visitorFactory;

    @Mock
    private CqlParser cqlParser;

    @Mock
    private FhirCQLResultParser fhirCQLResultParser;

    @InjectMocks
    private CQLServiceImpl cqlService;

    @BeforeEach
    public void setUp() {
        // Mute codacy "avoid unused private fields" warning
        assertNotNull(cqlParser);
        assertNotNull(visitorFactory);
        assertNotNull(userDAO);
        assertNotNull(measurePackageService);
    }

    @Test
    public void testGetCQLData() {
        String xml = "<cqlLookUp></cqlLookUp>";
        SaveUpdateCQLResult result = cqlService.getCQLData(xml);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetCQLDataQdm() {
        String xml = "<cqlLookUp><usingModel>QDM</usingModel><usingModelVersion>5.5</usingModelVersion></cqlLookUp>";
        SaveUpdateCQLResult result = cqlService.getCQLData(xml);
        assertEquals("QDM", result.getCqlModel().getUsingModel());
        assertEquals("5.5", result.getCqlModel().getUsingModelVersion());
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetCQLDataFhir() {
        String xml = "<cqlLookUp><usingModel>FHIR</usingModel><usingModelVersion>4.0.1</usingModelVersion></cqlLookUp>";
        Mockito.when(fhirCQLResultParser.generateParsedCqlObject(Mockito.any(), Mockito.any())).thenReturn(new SaveUpdateCQLResult());

        SaveUpdateCQLResult result = cqlService.getCQLData(xml);
        assertEquals("FHIR", result.getCqlModel().getUsingModel());
        assertEquals("4.0.1", result.getCqlModel().getUsingModelVersion());
        assertFalse(result.isSuccess());

        Mockito.verify(cqlValidatorRemoteCallService, Mockito.times(1)).validateCqlExpression(Mockito.anyString());
        Mockito.verify(fhirCQLResultParser, Mockito.times(1)).generateParsedCqlObject(Mockito.any(), Mockito.any());
    }

}
