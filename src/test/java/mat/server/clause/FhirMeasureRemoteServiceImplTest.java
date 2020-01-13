package mat.server.clause;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.server.service.FhirMeasureService;

@ExtendWith(MockitoExtension.class)
public class FhirMeasureRemoteServiceImplTest {
    @Mock
    private FhirMeasureService fhirMeasureService;
    @InjectMocks
    private FhirMeasureRemoteServiceImpl service;

    @Test
    public void testResult() throws MatException {
        FhirConvertResultResponse expectedResponse = new FhirConvertResultResponse();
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any())).thenReturn(expectedResponse);
        FhirConvertResultResponse result = service.convert(new ManageMeasureSearchModel.Result());
        Assertions.assertNotNull(result);
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any());
    }

    @Test
    public void testMatException() throws MatException {
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any())).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            service.convert(new ManageMeasureSearchModel.Result());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any());
    }

    @Test
    public void testMatExceptionOnOtherException() throws MatException {
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any())).thenThrow(new IllegalArgumentException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            service.convert(new ManageMeasureSearchModel.Result());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.any());
    }
}
