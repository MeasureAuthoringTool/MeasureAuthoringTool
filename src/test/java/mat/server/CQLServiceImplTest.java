package mat.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mat.dao.UserDAO;
import mat.model.cql.CQLModel;
import mat.server.service.MeasurePackageService;
import mat.server.service.cql.FhirCqlParser;
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
    private FhirCqlParser cqlParser;

    @InjectMocks
    private CQLServiceImpl cqlService;

    @BeforeEach
    public void setUp() {
        // Mute codacy "avoid unused private fields" warning
        assertNotNull(cqlParser);
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

        SaveUpdateCQLResult actualResult = new SaveUpdateCQLResult();
        Mockito.when(cqlParser.parseFhirCqlLibraryForErrors(Mockito.any(CQLModel.class), Mockito.anyString())).thenReturn(actualResult);

        SaveUpdateCQLResult result = cqlService.getCQLData(xml);
        assertEquals("FHIR", result.getCqlModel().getUsingModel());
        assertEquals("4.0.1", result.getCqlModel().getUsingModelVersion());
        assertFalse(result.isSuccess());

        Mockito.verify(cqlParser, Mockito.times(1)).parseFhirCqlLibraryForErrors(Mockito.any(CQLModel.class), Mockito.anyString());
    }

}
