package mat.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        libraryConversionResults.getCqlConversionResult().setFhirCql("some invalid fhir cql");

        ConversionResultDto validateResult = new ConversionResultDto();
        validateResult.setOutcome(ConversionOutcome.SUCCESS);
        validateResult.setMeasureId(sourceMeasureResult.getId());
        validateResult.setMeasureConversionResults(new MeasureConversionResults());
        validateResult.setLibraryConversionResults(Collections.singletonList(libraryConversionResults));
        validateResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

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

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(true);

        Mockito.when(measureLibraryService.saveCQLFile(Mockito.anyString(), Mockito.anyString())).thenReturn(saveUpdateCQLResult);

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

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

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

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

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

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(false);

        Mockito.when(measureLibraryService.saveCQLFile(Mockito.anyString(), Mockito.anyString())).thenReturn(saveUpdateCQLResult);

        assertThrows(MatRuntimeException.class, () -> {
            service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);
        });

        Mockito.verify(measureDAO, Mockito.never()).delete(Mockito.any(Measure.class));
    }

    @Test
    public void testIsConvertibleWithAlreadyExitingFhir() throws MatException {
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

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validateResult);

        ManageMeasureDetailModel sourceMeasureDetails = new ManageMeasureDetailModel();
        sourceMeasureDetails.setId(sourceMeasureId);
        Mockito.when(measureLibraryService.getMeasure(sourceMeasureResult.getId())).thenReturn(sourceMeasureDetails);

        List<Measure> existingFhirMeasures = Mockito.mock(List.class);

        Measure sourceMeasure = new Measure();
        sourceMeasure.setFhirMeasures(existingFhirMeasures);
        sourceMeasure.setId(sourceMeasureId);
        Mockito.when(measureDAO.find(Mockito.anyString())).thenReturn(sourceMeasure);

        ManageMeasureSearchModel.Result fhirMeasureResult = new ManageMeasureSearchModel.Result();
        fhirMeasureResult.setId("fhirMeasureId");
        Mockito.when(measureCloningService.cloneForFhir(Mockito.any())).thenReturn(fhirMeasureResult);

        SaveUpdateCQLResult saveUpdateCQLResult = new SaveUpdateCQLResult();
        saveUpdateCQLResult.setSuccess(true);

        Mockito.when(measureLibraryService.saveCQLFile(Mockito.anyString(), Mockito.anyString())).thenReturn(saveUpdateCQLResult);

        service.convert(sourceMeasureResult, "vsacGrantingTicket", loggedinUserId);

        Mockito.verify(existingFhirMeasures, Mockito.times(1)).clear();
    }
}
