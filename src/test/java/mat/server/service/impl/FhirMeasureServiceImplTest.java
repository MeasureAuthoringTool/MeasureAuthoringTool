package mat.server.service.impl;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import gov.cms.mat.fhir.rest.dto.ConversionOutcome;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.MeasureConversionResults;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;

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
    public void testIsNotConvertable() throws MatException {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        String loggedinUserId = "someCurrentUser";

        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            service.convert(sourceMeasure, loggedinUserId);
        });

        Assertions.assertEquals("Measure cannot be converted to FHIR", ex.getMessage());
    }

    @Test
    public void testIsConvertible() throws MatException {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        sourceMeasure.setId("sourceMeasureId");
        sourceMeasure.setFhirConvertible(true);
        String loggedinUserId = "someCurrentUser";

        ConversionResultDto preValidationResult = new ConversionResultDto();
        preValidationResult.setOutcome(ConversionOutcome.SUCCESS);
        preValidationResult.setMeasureId(sourceMeasure.getId());
        preValidationResult.setMeasureConversionResults(new MeasureConversionResults());
        preValidationResult.setLibraryConversionResults(Collections.emptyList());
        preValidationResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyBoolean())).thenReturn(preValidationResult);

        Mockito.when(measureLibraryService.getMeasure(sourceMeasure.getId())).thenReturn(new ManageMeasureDetailModel());

        ConversionResultDto convertResult = new ConversionResultDto();
        convertResult.setMeasureId(sourceMeasure.getId());
        convertResult.setMeasureConversionResults(new MeasureConversionResults());
        convertResult.setLibraryConversionResults(Collections.emptyList());
        convertResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.convert(Mockito.any(), Mockito.anyBoolean())).thenReturn(convertResult);

        ManageMeasureSearchModel.Result fhirMeasure = new ManageMeasureSearchModel.Result();
        fhirMeasure.setId("fhirMeasureId");
        Mockito.when(measureCloningService.cloneForFhir(Mockito.any())).thenReturn(fhirMeasure);

        service.convert(sourceMeasure, loggedinUserId);

        Mockito.verify(measureDAO, Mockito.never()).delete(Mockito.any(Measure.class));
    }

    @Test
    public void testIsConvertibleWithAlreadyExitingFhir() throws MatException {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        sourceMeasure.setId("sourceMeasureId");
        sourceMeasure.setFhirConvertible(true);
        sourceMeasure.setConvertedToFhir(true);
        String loggedinUserId = "someCurrentUser";

        ConversionResultDto preValidationResult = new ConversionResultDto();
        preValidationResult.setOutcome(ConversionOutcome.SUCCESS);
        preValidationResult.setMeasureId(sourceMeasure.getId());
        preValidationResult.setMeasureConversionResults(new MeasureConversionResults());
        preValidationResult.setLibraryConversionResults(Collections.emptyList());
        preValidationResult.setValueSetConversionResults(Collections.emptyList());

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.any(), Mockito.anyBoolean())).thenReturn(preValidationResult);

        ManageMeasureDetailModel currentMeasureDetails = new ManageMeasureDetailModel();
        Mockito.when(measureLibraryService.getMeasure(sourceMeasure.getId())).thenReturn(currentMeasureDetails);

        ConversionResultDto convertResult = new ConversionResultDto();
        convertResult.setMeasureId(sourceMeasure.getId());
        convertResult.setMeasureConversionResults(new MeasureConversionResults());
        convertResult.setLibraryConversionResults(Collections.emptyList());
        convertResult.setValueSetConversionResults(Collections.emptyList());


        Measure existingFhirMeasure = new Measure();
        Mockito.when(measureDAO.find(Mockito.anyString())).thenReturn(existingFhirMeasure);

        Mockito.when(fhirOrchestrationGatewayService.convert(Mockito.any(), Mockito.anyBoolean())).thenReturn(convertResult);

        ManageMeasureSearchModel.Result fhirMeasure = new ManageMeasureSearchModel.Result();
        fhirMeasure.setId("fhirMeasureId");
        Mockito.when(measureCloningService.cloneForFhir(Mockito.any())).thenReturn(fhirMeasure);

        service.convert(sourceMeasure, loggedinUserId);

        Mockito.verify(measureDAO, Mockito.times(1)).delete(Mockito.any(Measure.class));
    }
}
