package mat.server.service.fhirvalidationreport;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mat.dto.fhirconversion.ConversionOutcome;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.CqlConversionError;
import mat.dto.fhirconversion.CqlConversionResult;
import mat.dto.fhirconversion.FhirValidationResult;
import mat.dto.fhirconversion.LibraryConversionResults;
import mat.dto.fhirconversion.MatCqlConversionException;
import mat.dto.fhirconversion.ValueSetConversionResults;
import mat.server.service.FhirOrchestrationGatewayService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.model.clause.ModelTypeHelper;
import mat.shared.DateUtility;

@Service
public class MeasureValidationReportImpl implements FhirValidationReport {

    private static final Log logger = LogFactory.getLog(MeasureValidationReportImpl.class);
    private static final String NO_MEASURE_FOUND_ERROR = "The measure with that measure id does not exist.";
    private static final String REPORT_FTL = "fhirvalidationreport/fhir_validation_report.ftl";

    private Configuration freemarkerConfiguration;
    private MeasureDAO measureDAO;
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    @Value("${mat.measure.current.release.version}")
    private String currentMatVersion;

    public MeasureValidationReportImpl(Configuration freemarkerConfiguration, MeasureDAO measureDAO, FhirOrchestrationGatewayService fhirOrchestrationGatewayService) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.measureDAO = measureDAO;
        this.fhirOrchestrationGatewayService = fhirOrchestrationGatewayService;
    }

    /**
     * This method calls the QDM to FHIR conversion validation micro service for a given measure
     * and generates the FHIR conversion validation report for measure and its cql libraries & value sets,
     *
     * @param measureId          - Measure id for which validation report is requested
     * @param vsacGrantingTicket - VSAC granting ticket
     * @param converted          - true if the report is shown for a newly FHIR measure coverted from QDM.
     * @return HTML string report
     * @throws IOException
     * @throws TemplateException
     */
    @Override
    public String generateReport(String measureId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException {
        ConversionResultDto conversionResult = null;
        Measure measure = measureDAO.getMeasureByMeasureId(measureId);
        if (measure != null) {
            conversionResult = validateFhirConversion(measureId, vsacGrantingTicket, measure.isDraft());
        }

        return generateValidationReport(conversionResult, measure, converted);
    }

    /**
     * Calls the QDM to FHIR conversion validation service and returns the Conversion Results
     *
     * @return an instance of FHIR ConversionResultDto
     * @throws IOException
     */
    private ConversionResultDto validateFhirConversion(String measureId, String vsacGrantingTicket, boolean isDraft) throws IOException {
        if (measureId == null) {
            return null;
        }
        logger.info("Calling FHIR conversion validation service for measure: " + measureId);
        try {
            return fhirOrchestrationGatewayService.validate(measureId, vsacGrantingTicket, isDraft);
        } catch (MatRuntimeException e) {
            throw new IOException(e);
        }
    }

    private String generateValidationReport(ConversionResultDto conversionResultDto, Measure measure, boolean converted) throws IOException, TemplateException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("matVersion", currentMatVersion);

        if (conversionResultDto != null && measure != null) {
            prepareForMeasure(conversionResultDto, measure, converted, paramsMap);
        } else if (measure == null) {
            paramsMap.put("noMeasureFoundError", NO_MEASURE_FOUND_ERROR);
        } else {
            paramsMap.put("conversionServiceError", CONVERSION_SERVICE_ERROR);
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(REPORT_FTL), paramsMap);
    }

    private void prepareForMeasure(ConversionResultDto conversionResultDto, Measure measure, boolean converted, Map<String, Object> paramsMap) {
        if (converted) {
            addConversionStatusMessage(conversionResultDto, paramsMap);
        }

        if (ConversionOutcome.SUCCESS != conversionResultDto.getOutcome()) {
            addErrors(conversionResultDto, paramsMap);
        }

        if (conversionResultDto.getModified() != null) {
            addReportDateTime(conversionResultDto, paramsMap);
        }
        addMeasureDetails(measure, paramsMap);
    }

    private void addMeasureDetails(Measure measure, Map<String, Object> paramsMap) {
        paramsMap.put("measureName", measure.getDescription());
        paramsMap.put("measureVersion", measure.getVersion());
        paramsMap.put("modelType", ModelTypeHelper.defaultTypeIfBlank(measure.getMeasureModel()));
    }

    private void addReportDateTime(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {
        Instant instant = Instant.parse(conversionResultDto.getModified());
        paramsMap.put("runDate", DateUtility.formatInstant(instant, DATE_FORMAT));
        paramsMap.put("runTime", DateUtility.formatInstant(instant, TIME_FORMAT));
    }

    private void addErrors(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {
        List<FhirValidationResult> valueSetFhirValidationErrors = getValueSetErrors(conversionResultDto);

        List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();
        HashMap<String, List<Object>> qdmCqlConversionErrorsMap = new HashMap<>();
        HashMap<String, List<Object>> fhirCqlConversionErrorsMap = new HashMap<>();
        Map<String, List<CqlConversionError>> externalErrorsMap = new HashMap<>();
        getLibraryErrors(conversionResultDto, libraryFhirValidationErrors, qdmCqlConversionErrorsMap, fhirCqlConversionErrorsMap, paramsMap, externalErrorsMap);

        List<FhirValidationResult> measureFhirValidationErrors = getMeasureErrors(conversionResultDto);

        paramsMap.put("valueSetFhirValidationErrors", valueSetFhirValidationErrors);
        paramsMap.put("libraryFhirValidationErrors", libraryFhirValidationErrors);
        paramsMap.put("measureFhirValidationErrors", measureFhirValidationErrors);
        paramsMap.put("qdmCqlConversionErrors", qdmCqlConversionErrorsMap);
        paramsMap.put("fhirCqlConversionErrors", fhirCqlConversionErrorsMap);
        paramsMap.put("externalErrorsMap", externalErrorsMap);
    }

    private void buildCqlConversionErrorMap(Set<CqlConversionError> cqlConversionErrorsSet, Map<String, List<Object>> cqlConversionErrorsMap) {
        cqlConversionErrorsSet.forEach(q ->  {
            if(StringUtils.isNotBlank(q.getTargetIncludeLibraryId()) && StringUtils.isNotBlank(q.getTargetIncludeLibraryVersionId())) {
                String targetIncludedLibraryWithVersion = q.getTargetIncludeLibraryId() + " " + q.getTargetIncludeLibraryVersionId();
                if(!cqlConversionErrorsMap.containsKey(targetIncludedLibraryWithVersion)) {
                    cqlConversionErrorsMap.put(targetIncludedLibraryWithVersion, new ArrayList<>());
                }
                cqlConversionErrorsMap.get(targetIncludedLibraryWithVersion).add(q);
            }
        });
    }

    private void buildMatCqlConversionExceptionMap(Set<MatCqlConversionException> matCqlConversionExceptionSet, Map<String, List<Object>> matCqlConversionExceptionMap) {
        matCqlConversionExceptionSet.forEach(q ->  {
            if(StringUtils.isNotBlank(q.getTargetIncludeLibraryId()) && StringUtils.isNotBlank(q.getTargetIncludeLibraryVersionId())) {
                String targetIncludedLibraryWithVersion = q.getTargetIncludeLibraryId() + " " + q.getTargetIncludeLibraryVersionId();
                if(!matCqlConversionExceptionMap.containsKey(targetIncludedLibraryWithVersion)) {
                    matCqlConversionExceptionMap.put(targetIncludedLibraryWithVersion, new ArrayList<>());
                }
                matCqlConversionExceptionMap.get(targetIncludedLibraryWithVersion).add(q);
            }
        });
    }

    private void addConversionStatusMessage(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {
        String conversionStatusMessage = ConversionOutcome.SUCCESS == conversionResultDto.getOutcome() ?
                "The FHIR measure was created successfully." :
                "Warning: The FHIR measure was created successfully with errors.";
        paramsMap.put("conversionStatusMessage", conversionStatusMessage);
        paramsMap.put("outcome", String.valueOf(conversionResultDto.getOutcome()));
        paramsMap.put("errorReason", StringUtils.trimToNull(conversionResultDto.getErrorReason()));
    }

    private List<FhirValidationResult> getMeasureErrors(ConversionResultDto conversionResultDto) {
        //Measure FHIR validation errors
        List<FhirValidationResult> measureFhirValidationErrors = new ArrayList<>();
        if (conversionResultDto.getMeasureConversionResults() != null &&
                (conversionResultDto.getMeasureConversionResults().getSuccess() == null ||
                        !conversionResultDto.getMeasureConversionResults().getSuccess())) {
            measureFhirValidationErrors.addAll(conversionResultDto.getMeasureConversionResults().getMeasureFhirValidationResults());
        }
        return measureFhirValidationErrors;
    }

    private List<FhirValidationResult> getValueSetErrors(ConversionResultDto conversionResultDto) {
        // ValueSet FHIR validation errors
        List<FhirValidationResult> valueSetFhirValidationErrors = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conversionResultDto.getValueSetConversionResults())) {
            for (ValueSetConversionResults valueSetConversionResults : conversionResultDto.getValueSetConversionResults()) {
                if (Boolean.FALSE.equals(valueSetConversionResults.getSuccess())) {
                    valueSetFhirValidationErrors.addAll(valueSetConversionResults.getValueSetFhirValidationResults());
                }
            }
        }
        return valueSetFhirValidationErrors;
    }

    private void getLibraryErrors(ConversionResultDto conversionResultDto, List<FhirValidationResult> libraryFhirValidationErrors, HashMap<String, List<Object>> qdmCqlConversionErrorsMap,
                           HashMap<String, List<Object>> fhirCqlConversionErrorsMap, Map<String, Object> paramsMap, Map<String, List<CqlConversionError>> externalErrorsMap) {
         // Library FHIR validation errors
         CqlConversionResult cqlConversionResult;
         if (CollectionUtils.isNotEmpty(conversionResultDto.getLibraryConversionResults())) {
             for (LibraryConversionResults results : conversionResultDto.getLibraryConversionResults()) {
                 if ("Not Found in Hapi".equals(results.getReason())) {
                     paramsMap.put("LibraryNotFoundInHapi", results.getReason());
                 }
                 if (results.getSuccess() == null || !results.getSuccess()) {

                     generateLibraryFhirValidationErrors(results, libraryFhirValidationErrors);

                     if(results.getSuccess() != null) {
                         generateExternalErrorsMap(results, externalErrorsMap);
                     }

                     // CQL conversion errors
                     cqlConversionResult = results.getCqlConversionResult();
                     if (cqlConversionResult != null) {
                         if (CollectionUtils.isNotEmpty(cqlConversionResult.getCqlConversionErrors())) {
                             buildCqlConversionErrorMap(cqlConversionResult.getCqlConversionErrors(), qdmCqlConversionErrorsMap);
                         }
                         if (CollectionUtils.isNotEmpty(cqlConversionResult.getMatCqlConversionErrors())) {
                             buildMatCqlConversionExceptionMap(cqlConversionResult.getMatCqlConversionErrors(), qdmCqlConversionErrorsMap);
                         }
                         if (CollectionUtils.isNotEmpty(cqlConversionResult.getFhirCqlConversionErrors())) {
                             buildCqlConversionErrorMap(cqlConversionResult.getFhirCqlConversionErrors(), fhirCqlConversionErrorsMap);
                         }
                         if (CollectionUtils.isNotEmpty(cqlConversionResult.getFhirMatCqlConversionErrors())) {
                             buildMatCqlConversionExceptionMap(cqlConversionResult.getFhirMatCqlConversionErrors(), fhirCqlConversionErrorsMap);
                         }
                     }
                 }
             }
         }
     }

     private void generateExternalErrorsMap(LibraryConversionResults libraryConversionResults, Map<String, List<CqlConversionError>> externalErrorsMap) {

         libraryConversionResults.getExternalErrors().forEach((k, v) -> {
             v.forEach(q -> {
                 if(StringUtils.isNotBlank(q.getTargetIncludeLibraryId()) && StringUtils.isNotBlank(q.getTargetIncludeLibraryVersionId())) {
                     String targetIncludedLibraryWithVersion = q.getTargetIncludeLibraryId() + " " + q.getTargetIncludeLibraryVersionId();
                     if(!externalErrorsMap.containsKey(targetIncludedLibraryWithVersion)) {
                         externalErrorsMap.put(targetIncludedLibraryWithVersion, new ArrayList<>());
                     }
                     externalErrorsMap.get(targetIncludedLibraryWithVersion).add(q);
                 }
             });
         });
     }

     private void generateLibraryFhirValidationErrors(LibraryConversionResults libraryConversionResults, List<FhirValidationResult> libraryFhirValidationErrors) {
         if (CollectionUtils.isNotEmpty(libraryConversionResults.getLibraryFhirValidationResults())) {
             libraryFhirValidationErrors.addAll(libraryConversionResults.getLibraryFhirValidationResults());
         }
     }
}
