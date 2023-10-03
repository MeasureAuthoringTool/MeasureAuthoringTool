package mat.server.clause;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacTicketInformation;
import mat.server.LoggedInUserUtil;
import mat.server.service.FhirMeasureService;
import mat.server.service.VSACApiService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.eq;

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

    private ManageMeasureSearchModel.Result searchModel = new ManageMeasureSearchModel.Result();

    @BeforeEach
    public void setUp() {
        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(httpServletRequest);
        Whitebox.setInternalState(service, "perThreadRequest", threadLocal);
        Mockito.when(httpServletRequest.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getId()).thenReturn("sessionId");
        VsacTicketInformation ticketInfo = new VsacTicketInformation("apiKey", true);
        Mockito.when(vsacApiService.getVsacInformation(eq("sessionId"))).thenReturn(ticketInfo);
        LoggedInUserUtil.setLoggedInUser("LoggedInUser");
    }

    @AfterEach
    public void tearDown() {
        Mockito.verify(vsacApiService, Mockito.times(1)).getVsacInformation(eq("sessionId"));
    }

    @Test
    public void testResult() throws MatException {
        FhirConvertResultResponse expectedResponse = new FhirConvertResultResponse();
        Mockito.when(fhirMeasureService.convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true))).thenReturn(expectedResponse);
        FhirConvertResultResponse result = service.convert(searchModel);
        Assertions.assertNotNull(result);
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true));
    }

    @Test
    public void testMatException() throws MatException {
        Mockito.when(fhirMeasureService.convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true))).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> service.convert(searchModel));
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true));
    }

    @Test
    public void testMatExceptionOnOtherException() throws MatException {
        Mockito.when(fhirMeasureService.convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true))).thenThrow(new MatException("Expected failure"));
        Exception ex = Assertions.assertThrows(MatException.class, () -> service.convert(new ManageMeasureSearchModel.Result()));
        Assertions.assertEquals("Expected failure", ex.getMessage());
        Mockito.verify(fhirMeasureService, Mockito.times(1)).convert(eq(searchModel), eq("apiKey"), eq("LoggedInUser"), eq(true));
    }

}
