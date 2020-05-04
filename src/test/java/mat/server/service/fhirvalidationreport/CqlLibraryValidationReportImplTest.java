package mat.server.service.fhirvalidationreport;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.server.service.FhirLibraryConversionRemoteCall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URL;

import static mat.server.service.fhirvalidationreport.CqlLibraryValidationReportImpl.CONVERSION_SERVICE_ERROR;
import static mat.server.service.fhirvalidationreport.CqlLibraryValidationReportImpl.CQL_LIBRARY_NOT_FOUND_ERROR;
import static mat.server.service.fhirvalidationreport.CqlLibraryValidationReportImpl.FTL_TEMPLATE_NAME;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CqlLibraryValidationReportImplTest {

    private static final String LIBRARY_ID = "6d08bf08-f926-4e19-8d89-ad67ef89f17e";
    private static final String VSAC_ID = "7dfc2357-39af-44ea-a116-f188997b545b";

    @Mock private Configuration freemarkerConfig;
    @Mock private CQLLibraryDAO libraryDAO;
    @Mock private FhirLibraryConversionRemoteCall remoteProxy;

    @InjectMocks
    private CqlLibraryValidationReportImpl cut;

    @BeforeEach
    public void setUp() throws Exception {

        //1. set up fake test data
        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setId(LIBRARY_ID);
        cqlLibrary.setName("TEST_CQL_LIBRARY_NAME");
        cqlLibrary.setVersion("1.0");
        cqlLibrary.setLibraryModelType("QDM");
        when(libraryDAO.find(LIBRARY_ID)).thenReturn(cqlLibrary);

        //2. configure Freemarker template
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        Template template = configuration.getTemplate(FTL_TEMPLATE_NAME);
        when(freemarkerConfig.getTemplate(FTL_TEMPLATE_NAME)).thenReturn(template);

        //3. sample response from micro-service
        URL testResult = MeasureValidationReportImpl.class.getClassLoader().getResource("report.json");
        ConversionResultDto validationResults = new ObjectMapper().readValue(new File(testResult.getFile()), ConversionResultDto.class);
        Mockito.lenient().when(remoteProxy.validate(Mockito.anyString())).thenReturn(validationResults);
    }

    @Test
    public void shouldReturnErrorWhenLibraryIdDoesNotExist() throws Exception {
        when(libraryDAO.find(LIBRARY_ID)).thenReturn(null);

        var result = cut.generateReport(LIBRARY_ID, VSAC_ID, true);
        assertThat(result, containsString(CQL_LIBRARY_NOT_FOUND_ERROR));
    }

    @Test
    public void shouldReturnErrorWhenConversionFails() throws Exception {
        when(remoteProxy.validate(Mockito.anyString())).thenReturn(null);

        var result = cut.generateReport(LIBRARY_ID, VSAC_ID, true);
        assertThat(result, containsString(CONVERSION_SERVICE_ERROR));
    }


    @Test
    public void shouldReturnResponseWhenLibraryIdIsValid() throws Exception {

        var result = cut.generateReport(LIBRARY_ID, VSAC_ID, true);
        assertThat(result, startsWith("<html>"));
        assertThat(result, containsString("SUCCESS_WITH_ERROR"));
        assertThat(result, containsString("TEST_CQL_LIBRARY_NAME v1.0"));
        assertThat(result, endsWith("</html>\n"));
    }
}