package mat.server.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mat.DTO.fhirconversion.ConversionOutcome;
import mat.DTO.fhirconversion.ConversionResultDto;
import mat.DTO.fhirconversion.CqlConversionError;
import mat.DTO.fhirconversion.FhirValidationResult;
import mat.DTO.fhirconversion.LibraryConversionResults;
import mat.DTO.fhirconversion.MatCqlConversionException;
import mat.DTO.fhirconversion.MeasureConversionResults;
import mat.DTO.fhirconversion.ValueSetConversionResults;
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
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId, "vsacGrantingTicket", false);
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        assertTrue(report.contains("<div class=\"card-header\">Value Set</div>"));
        assertTrue(report.contains("<div class=\"card-header\">Library</div>"));
        assertTrue(report.contains("<div class=\"card-header\">Measure</div>"));
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

        String report = fhirValidationReportService.getFhirConversionReportForMeasure(measureId, "vsacGrantingTicket", false);
        assertTrue(report.startsWith("<html>\n    <head>\n        <title>MAT | FHIR Conversion Report</title>"));
        assertTrue(report.contains("<div class=\"report-header\">\n                Measure Authoring Tool v6.0\n            </div>"));
        assertTrue(report.contains("<div class=\"error-msg\">The measure with that measure id does not exist.</div>"));
        assertTrue(report.endsWith("</body>\n</html>\n"));
    }

    @Test
    void testBuildCqlConversionErrorMap() {
        Set<CqlConversionError> cqlConversionErrors = new HashSet();
        CqlConversionError cqlConversionError = new CqlConversionError();
        cqlConversionError.setTargetIncludeLibraryId("Sample LibraryId");
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildCqlConversionErrorMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        sampleCqlConversionErrorsMap.put("Sample LibraryId 1.0.003", new ArrayList<>(cqlConversionErrors));

        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testBuildCqlConversionErrorMapIfTargetIdNull() {
        Set<CqlConversionError> cqlConversionErrors = new HashSet();
        CqlConversionError cqlConversionError = new CqlConversionError();
        cqlConversionError.setTargetIncludeLibraryId(null);
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildCqlConversionErrorMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testBuildCqlConversionErrorMapIfTargetIdEmpty() {
        Set<CqlConversionError> cqlConversionErrors = new HashSet();
        CqlConversionError cqlConversionError = new CqlConversionError();
        cqlConversionError.setTargetIncludeLibraryId(" ");
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildCqlConversionErrorMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testBuildMatCqlConversionExceptionMap() {
        Set<MatCqlConversionException> cqlConversionErrors = new HashSet();
        MatCqlConversionException cqlConversionError = new MatCqlConversionException();
        cqlConversionError.setTargetIncludeLibraryId("Sample LibraryId");
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildMatCqlConversionExceptionMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        sampleCqlConversionErrorsMap.put("Sample LibraryId 1.0.003", new ArrayList<>(cqlConversionErrors));

        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testBuildMatCqlConversionExceptionMapIfTargetIdNull() {
        Set<MatCqlConversionException> cqlConversionErrors = new HashSet();
        MatCqlConversionException cqlConversionError = new MatCqlConversionException();
        cqlConversionError.setTargetIncludeLibraryId(null);
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildMatCqlConversionExceptionMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testBuildMatCqlConversionExceptionMapIfTargetIdEmpty() {
        Set<MatCqlConversionException> cqlConversionErrors = new HashSet();
        MatCqlConversionException cqlConversionError = new MatCqlConversionException();
        cqlConversionError.setTargetIncludeLibraryId(" ");
        cqlConversionError.setTargetIncludeLibraryVersionId("1.0.003");
        cqlConversionErrors.add(cqlConversionError);

        HashMap<String, List<Object>> cqlConversionErrorsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "buildMatCqlConversionExceptionMap", cqlConversionErrors, cqlConversionErrorsMap);

        HashMap<String, List<Object>> sampleCqlConversionErrorsMap = new HashMap<>();
        assertEquals(sampleCqlConversionErrorsMap, cqlConversionErrorsMap);
    }

    @Test
    void testAddConversionStatusMessageSuccess() {
        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setOutcome(ConversionOutcome.SUCCESS);
        conversionResultDto.setErrorReason("Fhir measure created successfully");

        Map<String, Object> paramsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "addConversionStatusMessage", conversionResultDto, paramsMap);

        String conversionStatusMessage = "The FHIR measure was created successfully.";
        Map<String, Object> sampleParamsMap = new HashMap<>();
        sampleParamsMap.put("conversionStatusMessage", conversionStatusMessage);
        sampleParamsMap.put("outcome", "SUCCESS");
        sampleParamsMap.put("errorReason", "Fhir measure created successfully");

        assertEquals(sampleParamsMap, paramsMap);
    }

    @Test
    void testAddConversionStatusMessageWarning() {
        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setOutcome(ConversionOutcome.LIBRARY_CONVERSION_FAILED);
        conversionResultDto.setErrorReason("Unable to validate selected measure");

        Map<String, Object> paramsMap = new HashMap<>();

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "addConversionStatusMessage", conversionResultDto, paramsMap);

        String conversionStatusMessage = "Warning: The FHIR measure was created successfully with errors.";
        Map<String, Object> sampleParamsMap = new HashMap<>();
        sampleParamsMap.put("conversionStatusMessage", conversionStatusMessage);
        sampleParamsMap.put("outcome", "LIBRARY_CONVERSION_FAILED");
        sampleParamsMap.put("errorReason", "Unable to validate selected measure");

        assertEquals(sampleParamsMap, paramsMap);
    }

    @Test
    void testGetMeasureErrors() {
        FhirValidationResult fhirValidationResult = new FhirValidationResult();
        fhirValidationResult.setLocationField("Measure.group[2]");
        fhirValidationResult.setErrorDescription("This is a valid Measure Conversion Error");
        fhirValidationResult.setSeverity("Error");

        List<FhirValidationResult> measureFhirValidationResults = new ArrayList();
        measureFhirValidationResults.add(fhirValidationResult);

        MeasureConversionResults measureConversionResults = new MeasureConversionResults();
        measureConversionResults.setSuccess(false);
        measureConversionResults.setMeasureFhirValidationResults(measureFhirValidationResults);

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setMeasureConversionResults(measureConversionResults);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getMeasureErrors", conversionResultDto);

        assertEquals(measureFhirValidationResults, result);
    }

    @Test
    void testGetMeasureErrorsIfNull() {

        List<FhirValidationResult> sampleMeasureFhirValidationErrors = new ArrayList<>();

        MeasureConversionResults measureConversionResults = new MeasureConversionResults();

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setMeasureConversionResults(measureConversionResults);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getMeasureErrors", conversionResultDto);

        assertEquals(sampleMeasureFhirValidationErrors, result);
    }

    @Test
    void testGetMeasureErrorsIfSuccessNull() {

        List<FhirValidationResult> sampleMeasureFhirValidationErrors = new ArrayList<>();

        MeasureConversionResults measureConversionResults = new MeasureConversionResults();
        measureConversionResults.setSuccess(null);

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setMeasureConversionResults(measureConversionResults);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getMeasureErrors", conversionResultDto);

        assertEquals(sampleMeasureFhirValidationErrors, result);
    }

    @Test
    void testGetValueSetErrors() {
        FhirValidationResult fhirValidationResult = new FhirValidationResult();
        fhirValidationResult.setLocationField("ValueSet.group[2]");
        fhirValidationResult.setErrorDescription("This is a valid Value Set Conversion Error");
        fhirValidationResult.setSeverity("Error");

        List<FhirValidationResult> sampleValueSetFhirValidationErrors = new ArrayList<>();
        sampleValueSetFhirValidationErrors.add(fhirValidationResult);


        ValueSetConversionResults valueSetConversionResults = new ValueSetConversionResults();
        valueSetConversionResults.setSuccess(false);
        valueSetConversionResults.setValueSetFhirValidationResults(sampleValueSetFhirValidationErrors);


        List<ValueSetConversionResults> valueSetConversionResultsList = new ArrayList<>();
        valueSetConversionResultsList.add(valueSetConversionResults);

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setValueSetConversionResults(valueSetConversionResultsList);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getValueSetErrors", conversionResultDto);

        assertEquals(sampleValueSetFhirValidationErrors, result);

    }

    @Test
    void testGetValueSetErrorsIfEmpty() {
        List<FhirValidationResult> samplevalueSetFhirValidationErrors = new ArrayList<>();

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        List<ValueSetConversionResults> valueSetConversionResults = new ArrayList<>();
        conversionResultDto.setValueSetConversionResults(valueSetConversionResults);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getValueSetErrors", conversionResultDto);

        assertEquals(samplevalueSetFhirValidationErrors, result);
    }

    @Test
    void testGetValueSetErrorsIfSuccessTrue() {
        List<FhirValidationResult> samplevalueSetFhirValidationErrors = new ArrayList<>();


        ValueSetConversionResults valueSetConversionResults = new ValueSetConversionResults();
        valueSetConversionResults.setSuccess(false);

        List<ValueSetConversionResults> valueSetConversionResultsList = new ArrayList<>();
        valueSetConversionResultsList.add(valueSetConversionResults);

        ConversionResultDto conversionResultDto = new ConversionResultDto();
        conversionResultDto.setValueSetConversionResults(valueSetConversionResultsList);

        List<FhirValidationResult> result = ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getValueSetErrors", conversionResultDto);

        assertEquals(samplevalueSetFhirValidationErrors, result);
    }

    @Test
    void testGetLibraryErrorsNotFoundInHapi() throws IOException {
        List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();
        HashMap<String, List<Object>> qdmCqlConversionErrorsMap = new HashMap<>();
        HashMap<String, List<Object>> fhirCqlConversionErrorsMap = new HashMap<>();
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, List<CqlConversionError>> externalErrorsMap = new HashMap<>();

        URL path = FhirValidationReportService.class.getClassLoader().getResource("report.json");
        ConversionResultDto conversionResultDto = new ObjectMapper().readValue(new File(path.getFile()), ConversionResultDto.class);

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getLibraryErrors", conversionResultDto,
                libraryFhirValidationErrors, qdmCqlConversionErrorsMap, fhirCqlConversionErrorsMap, paramsMap, externalErrorsMap);
        Map<String, Object> responseParamsMap = new HashMap<>();
        responseParamsMap.put("LibraryNotFoundInHapi", "Not Found in Hapi");

        assertEquals(responseParamsMap, paramsMap);
    }

    @Test
    void testGetLibraryErrors() throws IOException {
        List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();
        HashMap<String, List<Object>> qdmCqlConversionErrorsMap = new HashMap<>();
        HashMap<String, List<Object>> fhirCqlConversionErrorsMap = new HashMap<>();
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, List<CqlConversionError>> externalErrorsMap = new HashMap<>();

        URL path = FhirValidationReportService.class.getClassLoader().getResource("validationReportResponse.json");
        ConversionResultDto conversionResultDto = new ObjectMapper().readValue(new File(path.getFile()), ConversionResultDto.class);

        ReflectionTestUtils.invokeMethod(fhirValidationReportService, "getLibraryErrors", conversionResultDto,
                libraryFhirValidationErrors, qdmCqlConversionErrorsMap, fhirCqlConversionErrorsMap, paramsMap, externalErrorsMap);

        assertEquals(qdmCqlConversionErrorsMap.size(), 0);
        assertEquals(fhirCqlConversionErrorsMap.size(), 2);
    }

    @Test
    void testGenerateExternalErrorsMap() throws IOException {

        Map<String, List<CqlConversionError>> externalErrorsMap = new HashMap<>();

        URL path = FhirValidationReportService.class.getClassLoader().getResource("validationReportResponse.json");
        ConversionResultDto conversionResultDto = new ObjectMapper().readValue(new File(path.getFile()), ConversionResultDto.class);

        for (LibraryConversionResults results : conversionResultDto.getLibraryConversionResults()) {
            ReflectionTestUtils.invokeMethod(fhirValidationReportService, "generateExternalErrorsMap", results, externalErrorsMap);
        }
        assertEquals(externalErrorsMap.size(), 1);
        assertEquals(externalErrorsMap.get("MATGlobalCommonFunctions_FHIR4 4.0.000").size(), 2);
    }

    @Test
    void testGenerateLibraryFhirValidationErrors() throws IOException {

        List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();

        URL path = FhirValidationReportService.class.getClassLoader().getResource("validationReportResponse.json");
        ConversionResultDto conversionResultDto = new ObjectMapper().readValue(new File(path.getFile()), ConversionResultDto.class);

        for (LibraryConversionResults results : conversionResultDto.getLibraryConversionResults()) {
            ReflectionTestUtils.invokeMethod(fhirValidationReportService, "generateLibraryFhirValidationErrors", results, libraryFhirValidationErrors);
        }

        assertEquals(libraryFhirValidationErrors.size(), 3);
    }

}
