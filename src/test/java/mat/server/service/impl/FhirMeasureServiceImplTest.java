package mat.server.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.cql.TestCqlToMatXml;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.clause.MeasureXML;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import gov.cms.mat.fhir.rest.dto.ConversionOutcome;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.LibraryConversionResults;
import gov.cms.mat.fhir.rest.dto.MeasureConversionResults;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.server.service.FhirOrchestrationGatewayService;
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;
import mat.shared.SaveUpdateCQLResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class FhirMeasureServiceImplTest {

    @Mock
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    @Mock
    private MeasureLibraryService measureLibraryService;

    @Mock
    private MeasureCloningService measureCloningService;

    @Mock
    private MeasureDAO measureDAO;

    @Mock
    private MeasureXMLDAO measureXMLDAO;

    @Mock
    private PlatformTransactionManager platformTransactionManager;

    @InjectMocks
    private FhirMeasureServiceImpl service;

    @Test
    public void testIsNotConvertable() {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        String loggedinUserId = "someCurrentUser";

        Exception ex = assertThrows(MatException.class, () -> {
            service.convert(sourceMeasure, loggedinUserId, "vsacGrantingTicket");
        });

        assertEquals("Measure cannot be converted to FHIR", ex.getMessage());
    }

    @Test
    public void testIsConvertible() throws MatException {
        String sourceMeasureId = "sourceMeasureId";
        ManageMeasureSearchModel.Result sourceMeasureResult = new ManageMeasureSearchModel.Result();
        sourceMeasureResult.setId(sourceMeasureId);
        sourceMeasureResult.setFhirConvertible(true);
        String loggedinUserId = "someCurrentUser";

        LibraryConversionResults libraryConversionResults = new LibraryConversionResults();
        libraryConversionResults.getCqlConversionResult().setFhirCql(resourceAsString("convert-1.cql"));

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.convert(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);


        String fhirMeasureId = "fhirMeasureId";

        ManageMeasureSearchModel.Result fhirMeasureResult = new ManageMeasureSearchModel.Result();
        fhirMeasureResult.setId(fhirMeasureId);
        Mockito.when(measureCloningService.cloneForFhir(Mockito.any())).thenReturn(fhirMeasureResult);

        sourceMeasureResult.setId(sourceMeasureId);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setFhirMeasures(new ArrayList<>());
        sourceMeasure.setId(sourceMeasureId);
        Mockito.when(measureDAO.find(Mockito.anyString())).thenReturn(sourceMeasure);

        MeasureXML measureXML = new MeasureXML() {
            @Override
            public String getMeasureXMLAsString() {
                return resourceAsString("convert-1-mat.xml");
            }
        };

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(true);

        Mockito.when(measureXMLDAO.findForMeasure(Mockito.any())).thenReturn(measureXML);

        service.TEST_MODE = true;
        service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);

        Mockito.verify(measureDAO, Mockito.never()).delete(Mockito.any(Measure.class));
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

        Mockito.when(fhirOrchestrationGatewayService.convert(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setFhirMeasures(new ArrayList<>());
        sourceMeasure.setId(sourceMeasureId);
        Mockito.when(measureDAO.find(Mockito.anyString())).thenReturn(sourceMeasure);

        assertThrows(MatException.class, () -> {
            service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);
        });

        Mockito.verify(measureDAO, Mockito.never()).delete(Mockito.any(Measure.class));
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
        sourceMeasure.setFhirMeasures(new ArrayList<>());
        sourceMeasure.setId(sourceMeasureId);

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(false);

        doThrow(MatRuntimeException.class).when(measureLibraryService).recordRecentMeasureActivity(Mockito.any(),Mockito.any());

        assertThrows(MatRuntimeException.class, () -> {
            service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);
        });

        Mockito.verify(measureDAO, Mockito.never()).delete(Mockito.any(Measure.class));
    }

    @Test
    public void testIsConvertibleWithAlreadyExitingFhir() throws MatException, IOException {
        String sourceMeasureId = "sourceMeasureId";
        ManageMeasureSearchModel.Result sourceMeasureResult = new ManageMeasureSearchModel.Result();
        sourceMeasureResult.setId(sourceMeasureId);
        sourceMeasureResult.setFhirConvertible(true);
        String loggedinUserId = "someCurrentUser";

        LibraryConversionResults libraryConversionResults = new LibraryConversionResults();
        libraryConversionResults.getCqlConversionResult().setFhirCql(resourceAsString("convert-1.cql"));

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.convert(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);

        List<Measure> existingFhirMeasures = Mockito.mock(List.class);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setFhirMeasures(existingFhirMeasures);
        sourceMeasure.setId(sourceMeasureId);
        Mockito.when(measureDAO.find(Mockito.anyString())).thenReturn(sourceMeasure);

        MeasureXML measureXML = new MeasureXML() {
            @Override
            public String getMeasureXMLAsString() {
                return resourceAsString("convert-1-mat.xml");
            }
        };

        ManageMeasureSearchModel.Result fhirMeasureResult = new ManageMeasureSearchModel.Result();
        fhirMeasureResult.setId("fhirMeasureId");
        Mockito.when(measureCloningService.cloneForFhir(Mockito.any())).thenReturn(fhirMeasureResult);

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(true);

        Mockito.when(measureXMLDAO.findForMeasure(Mockito.any())).thenReturn(measureXML);
        service.TEST_MODE = true;
        service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);

        Mockito.verify(existingFhirMeasures, Mockito.times(1)).clear();
    }

    public String resourceAsString(String resource) {
        try (InputStream i = getClass().getResourceAsStream("/test-cql/" + resource)) {
            return IOUtils.toString(i);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
