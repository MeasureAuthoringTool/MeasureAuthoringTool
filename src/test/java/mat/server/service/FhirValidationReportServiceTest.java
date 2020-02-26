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
import mat.server.service.impl.FhirOrchestrationGatewayService;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

//    @Test
    void testGetFhirConversionReportForMeasure() throws Exception {
        String measureId = "402804382649c54c230164d76256dd11dc";
        Measure measure = new Measure();
        measure.setId(measureId);
        measure.setDescription("Appropriate Use of DXA Scans in Women Under 65 Years Who Do Not Meet the Risk Factor Profile for Osteoporotic Fracture v2.000");
        measure.setVersion("v2.0.0");
        measure.setMeasureModel("QDM");
        Mockito.when(measureDAO.find(measureId)).thenReturn(measure);

        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        Template template = configuration.getTemplate(templateName);
        Mockito.when(freemarkerConfiguration.getTemplate(templateName)).thenReturn(template);

        Mockito.when(fhirOrchestrationGatewayService.validate(Mockito.anyString(), Mockito.anyBoolean())).thenAnswer(invocation -> {
            URL path = FhirValidationReportService.class.getClassLoader().getResource("report.json");
            return new ObjectMapper()
                    .readValue(new File(path.getFile()),
                            ConversionResultDto.class);
        });

        ReflectionTestUtils.setField(fhirValidationReportService, "currentMatVersion", "v6.0");


        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId);
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        // TODO: correct in MAT-432
//        assertTrue(report.contains("<div class=\"font-smaller\">Library Validation Errors</div>"));
//        assertTrue(report.contains("<div class=\"card-header\">CQL Conversion Errors</div>"));
//        assertFalse(report.contains("<div class=\"card-header\">ValueSet Validation Errors</div>"));
        assertTrue(report.endsWith("</body>\n</html>\n"));
    }

//    @Test
    void testGetFhirConversionReportForInvalidMeasureId() throws IOException, TemplateException {
        String measureId = "notAvalidId";
        Mockito.when(measureDAO.find(measureId)).thenReturn(null);

        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(getClass(), "/templates");
        Template template = configuration.getTemplate(templateName);
        Mockito.when(freemarkerConfiguration.getTemplate(templateName)).thenReturn(template);

        ReflectionTestUtils.setField(fhirValidationReportService, "currentMatVersion", "v6.0");

        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId);
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        assertTrue(report.contains("<div class=\"error-msg\">The measure with that measure id does not exist.</div>"));
        assertTrue(report.endsWith("</body>\n</html>\n"));
    }
}
