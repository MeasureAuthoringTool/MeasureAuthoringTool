package mat.server.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FhirValidationReportServiceTest {

    private final String templateName = "fhirvalidationreport/fhir_validation_report.ftl";

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private MeasureDAO measureDAO;

    @Mock
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    @InjectMocks
    private FhirValidationReportService fhirValidationReportService;

    @Test
    void testGetFhirConversionReportForMeasure() throws Exception {
        String measureId = "402803826c7bec34016cba7ed1b60b77";
        Measure measure = new Measure();
        measure.setId(measureId);
        measure.setDescription("Appropriate Use of DXA Scans in Women Under 65 Years Who Do Not Meet the Risk Factor Profile for Osteoporotic Fracture v2.000");
        measure.setVersion("v2.0.0");
        measure.setMeasureModel("QDM");
        measure.setDraft(true);
        Mockito.when(measureDAO.getMeasureByMeasureId(measureId)).thenReturn(measure);

        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        Template template = configuration.getTemplate(templateName);
        Mockito.when(freemarkerConfiguration.getTemplate(templateName)).thenReturn(template);


        URL testResult = FhirValidationReportService.class.getClassLoader().getResource("report.json");
        ConversionResultDto validationResults = new ObjectMapper().readValue(new File(testResult.getFile()), ConversionResultDto.class);


        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(validationResults);

        ReflectionTestUtils.setField(fhirValidationReportService, "currentMatVersion", "v6.0");


        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId, "vsacGrantingTicket");
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        assertTrue(report.contains("<div class=\"card-header\">Value Set</div>"));
        assertTrue(report.contains("<div class=\"card-header\">Library</div>"));
        assertTrue(report.contains("<div class=\"card-header\">Measure</div>"));
        assertTrue(report.contains("<div class=\"card-header\">QDM CQL Conversion Errors</div>"));
        assertTrue(report.contains("<div class=\"card-header\">FHIR CQL Conversion Errors</div>"));
        assertTrue(report.endsWith("</body>\n</html>\n"));
    }

    @Test
    void testGetFhirConversionReportForInvalidMeasureId() throws IOException, TemplateException {
        String measureId = "notAvalidId";
        Mockito.when(measureDAO.getMeasureByMeasureId(measureId)).thenReturn(null);

        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        Template template = configuration.getTemplate(templateName);
        Mockito.when(freemarkerConfiguration.getTemplate(templateName)).thenReturn(template);

        ReflectionTestUtils.setField(fhirValidationReportService, "currentMatVersion", "v6.0");

        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId, "vsacGrantingTicket");
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        assertTrue(report.contains("<div class=\"error-msg\">The measure with that measure id does not exist.</div>"));
        assertTrue(report.endsWith("</body>\n</html>\n"));
    }
}
