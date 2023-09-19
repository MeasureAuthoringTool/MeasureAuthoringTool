package mat.server.service.cql;

import mat.client.umls.service.VsacTicketInformation;
import mat.model.cql.CQLModel;
import mat.server.service.VSACApiService;
import mat.shared.CQLError;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FhirCqlParserServiceTest {
    private static final String MATXML_FROM_CQL_SRVC = "cql-xml-gen/cql";
    private static final String SRV_BASE_URL = "http://localhost:9080/";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private VSACApiService vsacApiService;
    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private FhirCqlParserService fhirCqlParserService;

    @Test
    public void testValidateCqlExpression() throws IOException {

        Whitebox.setInternalState(fhirCqlParserService, "fhirServicesUrl", SRV_BASE_URL);

        String testCql = IOUtils.toString(FhirCqlParserServiceTest.class.getClassLoader().getResource("cqlExpressionForValidation.txt"));

        Mockito.when(httpSession.getId()).thenReturn("sessionId");

        VsacTicketInformation vsacTicketInformation = new VsacTicketInformation("vsacApiKey", true);
        Mockito.when(vsacApiService.getVsacInformation("sessionId")).thenReturn(vsacTicketInformation);

        CQLModel sourceModel = new CQLModel();

        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setCql(testCql);
        cqlXmlReq.setSourceModel(sourceModel);
        HttpHeaders headers = new HttpHeaders();
        headers.add("API-KEY", "vsacApiKey");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, headers);
        ResponseEntity<MatXmlResponse> response = ResponseEntity.ok(new MatXmlResponse());

        Mockito.when(restTemplate.exchange(SRV_BASE_URL + MATXML_FROM_CQL_SRVC,
                HttpMethod.PUT,
                request,
                MatXmlResponse.class,
                Collections.emptyMap())).thenReturn(response);


        MatXmlResponse actualResponse = fhirCqlParserService.parse(testCql, sourceModel);

        assertNotNull(actualResponse);

        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(SRV_BASE_URL + MATXML_FROM_CQL_SRVC),
                Mockito.eq(HttpMethod.PUT),
                Mockito.eq(request),
                Mockito.eq(MatXmlResponse.class),
                Mockito.eq(Collections.emptyMap()));

    }

    @Test
    public void testResult() {
        CQLModel sourceModel = new CQLModel();
        sourceModel.setLibraryName("Main");
        sourceModel.setVersionUsed("1.1");

        LibraryErrors le = new LibraryErrors();
        le.setName("Main");
        le.setVersion("1.1");

        CQLError mainErr = new CQLError();
        mainErr.setSeverity("Error");
        le.getErrors().add(mainErr);

        CQLError mainWarn = new CQLError();
        mainWarn.setSeverity("Warning");
        le.getErrors().add(mainWarn);


        LibraryErrors ie1 = new LibraryErrors();
        ie1.setName("Incl1");
        ie1.setVersion("1.2");

        LibraryErrors ie2 = new LibraryErrors();
        ie2.setName("Incl2");
        ie2.setVersion("1.3");

        CQLError ie2Error = new CQLError();
        ie2Error.setSeverity("Error");
        ie2.getErrors().add(ie2Error);


        List<LibraryErrors> libraryErrors = Arrays.asList(le, ie1, ie2);


        MatXmlResponse resp = new MatXmlResponse();
        resp.setErrors(libraryErrors);
        SaveUpdateCQLResult result = fhirCqlParserService.parseFhirCqlLibraryForErrors(sourceModel, resp);
        assertNotNull(result);
        assertNotNull(result.getCqlModel());
        assertEquals(sourceModel, result.getCqlModel());
        assertFalse(result.isSuccess());
        assertNotNull(result.getCqlErrors());
        assertEquals(3, result.getCqlErrors().size());
        assertNotNull(result.getLibraryNameErrorsMap());
        assertNotNull(result.getLibraryNameWarningsMap());
        assertEquals(1, result.getLibraryNameWarningsMap().size());
        assertEquals(1, result.getLibraryNameErrorsMap().size());
        assertNotNull(result.getIncludeLibrariesWithErrors());
        assertEquals(1, result.getIncludeLibrariesWithErrors().size());
        assertEquals(Collections.singleton("Incl2(1.3)"), result.getIncludeLibrariesWithErrors());
    }

}
