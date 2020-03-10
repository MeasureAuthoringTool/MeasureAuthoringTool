package mat.server.clause;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacTicketInformation;
import mat.server.service.FhirMeasureService;
import mat.server.service.VSACApiService;

@ExtendWith(MockitoExtension.class)
public class FhirMeasureRemoteServiceImplTest {
    @Mock
    private FhirMeasureService fhirMeasureService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpSession httpSession;
    @Mock
    private VSACApiService vsacApiService;
    @InjectMocks
    private FhirMeasureRemoteServiceImpl service;

    @BeforeEach
    public void setUp() {
        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(httpServletRequest);
        Whitebox.setInternalState(service, "perThreadRequest", threadLocal);
        Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getId()).thenReturn("sessionId");
        VsacTicketInformation ticketInfo = new VsacTicketInformation();
        ticketInfo.setTicket("vsacGrantingTicket");
        Mockito.when(vsacApiService.getTicketGrantingTicket(Mockito.eq("sessionId"))).thenReturn(ticketInfo);
    }

    @AfterEach
    public void tearDown() {
        Mockito.verify(vsacApiService, Mockito.times(1)).getTicketGrantingTicket(Mockito.eq("sessionId"));
    }

    @Test
    public void testResult() throws MatException {
        FhirConvertResultResponse expectedResponse = new FhirConvertResultResponse();
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any())).thenReturn(expectedResponse);
        FhirConvertResultResponse result = service.convert(new ManageMeasureSearchModel.Result());
        Assertions.assertNotNull(result);
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testMatException() throws MatException {
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any())).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            service.convert(new ManageMeasureSearchModel.Result());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testMatExceptionOnOtherException() throws MatException {
        Mockito.when(fhirMeasureService.convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any())).thenThrow(new IllegalArgumentException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> {
            service.convert(new ManageMeasureSearchModel.Result());
        });
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(Mockito.any(ManageMeasureSearchModel.Result.class), Mockito.anyString(), Mockito.any());
    }
}
