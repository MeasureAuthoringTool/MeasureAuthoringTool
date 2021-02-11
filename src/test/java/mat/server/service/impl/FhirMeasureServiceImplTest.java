package mat.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dto.fhirconversion.ConversionOutcome;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.LibraryConversionResults;
import mat.dto.fhirconversion.MeasureConversionResults;
import mat.model.clause.Measure;
import mat.model.clause.MeasureDetails;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.service.CodeListService;
import mat.server.service.FhirMeasureRemoteCall;
import mat.server.service.MeasureAuditService;
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;
import mat.server.service.VSACApiService;
import mat.server.service.cql.FhirCqlParser;
import mat.server.service.cql.MatXmlResponse;
import mat.shared.CQLObject;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FhirMeasureServiceImplTest {

    @Mock
    private FhirMeasureRemoteCall fhirOrchestrationGatewayService;

    @Mock
    private MeasureLibraryService measureLibraryService;

    @Mock
    private MeasureCloningService measureCloningService;

    @Mock
    private MeasureDAO measureDAO;

    @Mock
    private MeasureXMLDAO measureXMLDAO;

    @Mock
    private CQLService cqlService;

    @Mock
    private PlatformTransactionManager platformTransactionManager;

    @Mock
    private CodeListService codeListService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpSession httpSession;

    @Mock
    private VSACApiService vsacApiService;

    @Mock
    private FhirCqlParser cqlParser;

    @Mock
    private MeasureAuditService auditService;

    @InjectMocks
    private FhirMeasureServiceImpl service;

    @BeforeEach
    public void before() {
        // The following statements are to appease our Codacy tooling it wont let us commit
        // if we dont use these some place:
        assertNotNull(platformTransactionManager);
        assertNotNull(cqlService);
        assertNotNull(codeListService);
        assertNotNull(cqlParser);
        assertNotNull(httpSession);
        assertNotNull(restTemplate);
        assertNotNull(vsacApiService);
    }

    @Test
    public void testIsNotConvertable() {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        String loggedinUserId = "someCurrentUser";

        Exception ex = assertThrows(MatException.class, () -> {
            service.convert(sourceMeasure, loggedinUserId, "vsacGrantingTicket", true);
        });

        assertEquals("Measure cannot be converted to FHIR", ex.getMessage());
    }

    @Test
    public void testIsConvertible() throws MatException {
        String sourceMeasureId = "sourceMeasureId";
        String setId = "setId";
        ManageMeasureSearchModel.Result sourceMeasureResult = new ManageMeasureSearchModel.Result();
        sourceMeasureResult.setId(sourceMeasureId);
        sourceMeasureResult.setFhirConvertible(true);
        sourceMeasureResult.setMeasureSetId(setId);
        String loggedinUserId = "someCurrentUser";

        LibraryConversionResults libraryConversionResults = new LibraryConversionResults();
        libraryConversionResults.getCqlConversionResult().setFhirCql(resourceAsString("convert-1.cql"));

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.convert(any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        sourceMeasureDetails.setMeasureSetId(setId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);


        String fhirMeasureId = "fhirMeasureId";

        ManageMeasureSearchModel.Result fhirMeasureResult = new ManageMeasureSearchModel.Result();
        fhirMeasureResult.setId(fhirMeasureId);
        Mockito.when(measureCloningService.convert(any())).thenReturn(fhirMeasureResult);

        sourceMeasureResult.setId(sourceMeasureId);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setId(sourceMeasureId);
        Mockito.when(measureDAO.deleteFhirMeasuresBySetId(eq(setId))).thenReturn(1);

        Measure fhirMeasure = new Measure();
        fhirMeasure.setMeasureDetails(new MeasureDetails());
        Mockito.when(measureDAO.find(any())).thenReturn(fhirMeasure);

        MeasureXML measureXML = new MeasureXML() {
            @Override
            public String getMeasureXMLAsString() {
                return resourceAsString("convert-1-mat.xml");
            }
        };

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(true);

        Mockito.when(measureXMLDAO.findForMeasure(any())).thenReturn(measureXML);


        Mockito.when(cqlParser.parse(Mockito.anyString(), any(CQLModel.class))).thenReturn(new MatXmlResponse(Collections.emptyList(), new CQLModel(), "CQL text", new CQLObject()));

        service.TEST_MODE = true;
        service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId, true);

        Mockito.verify(measureDAO, Mockito.times(1)).deleteFhirMeasuresBySetId(eq(setId));
    }

    @Test
    public void testWhenNoFhirCqlReturned() throws MatException {
        String sourceMeasureId = "sourceMeasureId";
        ManageMeasureSearchModel.Result sourceMeasureResult = new ManageMeasureSearchModel.Result();
        sourceMeasureResult.setId(sourceMeasureId);
        sourceMeasureResult.setFhirConvertible(true);
        String loggedinUserId = "someCurrentUser";

        LibraryConversionResults libraryConversionResults = new LibraryConversionResults();
        // No fhir cql returned due
        libraryConversionResults.getCqlConversionResult().setFhirCql(null);

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.convert(any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setId(sourceMeasureId);

        assertThrows(MatException.class, () -> {
            service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId, true);
        });

        Mockito.verify(measureDAO, Mockito.never()).deleteFhirMeasuresBySetId(Mockito.anyString());
    }

    @Test
    public void testCouldNotSaveCQL() throws MatException {
        String sourceMeasureId = "sourceMeasureId";
        ManageMeasureSearchModel.Result sourceMeasureResult = new ManageMeasureSearchModel.Result();
        sourceMeasureResult.setId(sourceMeasureId);
        sourceMeasureResult.setFhirConvertible(true);
        String loggedinUserId = "someCurrentUser";

        LibraryConversionResults libraryConversionResults = new LibraryConversionResults();
        libraryConversionResults.getCqlConversionResult().setFhirCql("some invalid fhir cql");

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        String fhirMeasureId = "fhirMeasureId";

        ManageMeasureSearchModel.Result fhirMeasureResult = new ManageMeasureSearchModel.Result();
        fhirMeasureResult.setId(fhirMeasureId);
        sourceMeasureResult.setId(sourceMeasureId);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setId(sourceMeasureId);

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(false);

        doThrow(MatRuntimeException.class).when(measureLibraryService).recordRecentMeasureActivity(any(), any());

        assertThrows(MatRuntimeException.class, () -> {
            service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId, true);
        });

        Mockito.verify(measureDAO, Mockito.never()).delete(any(Measure.class));
    }

    public String resourceAsString(String resource) {
        try (InputStream i = getClass().getResourceAsStream("/test-cql/" + resource)) {
            return IOUtils.toString(i);
        } catch (IOException ioe) {
            throw new MatRuntimeException(ioe);
        }
    }
}
