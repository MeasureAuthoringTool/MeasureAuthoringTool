package mat.server.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.cms.mat.fhir.rest.dto.ConversionOutcome;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.CqlConversionError;
import gov.cms.mat.fhir.rest.dto.CqlConversionResult;
import gov.cms.mat.fhir.rest.dto.FhirValidationResult;
import gov.cms.mat.fhir.rest.dto.LibraryConversionResults;
import gov.cms.mat.fhir.rest.dto.MatCqlConversionException;
import gov.cms.mat.fhir.rest.dto.ValueSetConversionResults;
import mat.shared.model.util.MeasureDetailsUtil;
import org.apache.commons.collections4.CollectionUtils;
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
import mat.shared.DateUtility;

@Service("fhirValidationService")
public class FhirValidationReportService {

    private static final String DATE_FORMAT = "dd-MMM-YYYY";
    private static final String TIME_FORMAT = "hh:mm aa";
    private static final String NO_MEASURE_FOUND_ERROR = "The measure with that measure id does not exist.";
    private static final String CONVERSION_SERVICE_ERROR = "An error occurred while validating the FHIR conversion. Please try again later.";
    private static final Log logger = LogFactory.getLog(FhirValidationReportService.class);

    private Configuration freemarkerConfiguration;
    private MeasureDAO measureDAO;
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    @Value("${mat.measure.current.release.version}")
    private String currentMatVersion;

    public FhirValidationReportService(Configuration freemarkerConfiguration, MeasureDAO measureDAO, FhirOrchestrationGatewayService fhirOrchestrationGatewayService) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.measureDAO = measureDAO;
        this.fhirOrchestrationGatewayService = fhirOrchestrationGatewayService;
    }

    /**
     * This method calls the QDM to FHIR conversion validation micro service for a given measure
     * and generates the FHIR conversion validation report for measure and it's cql libraries & value sets,
     *
     * @param measureId- Measure id for which validation report is requested
     * @return HTML string report
     * @throws IOException
     * @throws TemplateException
     */
    public String getFhirConversionReportForMeasure(String measureId) throws IOException, TemplateException {
        ConversionResultDto conversionResult = null;
        Measure measure = measureDAO.getMeasureByMeasureId(measureId);
        if (measure != null) {
            conversionResult = validateFhirConversion(measureId, measure.isDraft());
        }
        return generateValidationReport(conversionResult, measure);
    }

    /**
     * Calls the QDM to FHIR conversion validation service and returns the Conversion Results
     *
     * @return an instance of FHIR ConversionResultDto
     * @throws IOException
     */
    private ConversionResultDto validateFhirConversion(String measureId, boolean isDraft) throws IOException {
        if (measureId == null) {
            return null;
        }
        logger.info("Calling FHIR conversion validation service for measure: " + measureId);
        try {
            return fhirOrchestrationGatewayService.validate(measureId, isDraft);
        } catch (MatRuntimeException e) {
            throw new IOException(e);
        }
    }

    /**
     * Generates the validation report from FHIR conversion validation result object
     *
     * @return HTML String representation of a FHIR conversion validation report for given measure
     * @throws IOException
     * @throws TemplateException
     */
    private String generateValidationReport(ConversionResultDto conversionResultDto, Measure measure) throws IOException, TemplateException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("matVersion", currentMatVersion);

        if (conversionResultDto != null && measure != null) {
            if (ConversionOutcome.SUCCESS != conversionResultDto.getOutcome()) {

                if(!conversionResultDto.getErrorReason().isEmpty()) {
                    paramsMap.put("ErrorReason", conversionResultDto.getErrorReason());
                }
                // ValueSet FHIR validation errors
                List<FhirValidationResult> valueSetFhirValidationErrors = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(conversionResultDto.getValueSetConversionResults())) {
                    for (ValueSetConversionResults valueSetConversionResults : conversionResultDto.getValueSetConversionResults()) {
                        if (Boolean.FALSE.equals(valueSetConversionResults.getSuccess())) {
                            valueSetFhirValidationErrors = valueSetConversionResults.getValueSetFhirValidationResults();
                        }
                    }
                }

                // Library FHIR validation errors
                List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();
                Set<CqlConversionError> qdmCqlConversionErrors = new HashSet<>();
                Set<MatCqlConversionException> qdmMatCqlConversionErrors = null;
                Set<CqlConversionError> fhirCqlConversionErrors = null;
                Set<MatCqlConversionException> fhirMatCqlConversionErrors = null;
                CqlConversionResult cqlConversionResult;
                if (CollectionUtils.isNotEmpty(conversionResultDto.getLibraryConversionResults())) {
                    for (LibraryConversionResults results : conversionResultDto.getLibraryConversionResults()) {
                        if(results.getReason().equalsIgnoreCase("Not Found in Hapi")) {
                            paramsMap.put("LibraryNotFoundInHapi", results.getReason());
                        }
                        if (Boolean.FALSE.equals(results.getSuccess())) {
                            if (CollectionUtils.isNotEmpty(results.getLibraryFhirValidationResults())) {
                                libraryFhirValidationErrors = results.getLibraryFhirValidationResults();
                            }
                            // CQL conversion errors
                            cqlConversionResult = results.getCqlConversionResult();
                            if (cqlConversionResult != null) {
                                if (CollectionUtils.isNotEmpty(cqlConversionResult.getCqlConversionErrors())) {
                                    qdmCqlConversionErrors = cqlConversionResult.getCqlConversionErrors();
                                }
                                if (CollectionUtils.isNotEmpty(cqlConversionResult.getMatCqlConversionErrors())) {
                                    qdmMatCqlConversionErrors = cqlConversionResult.getMatCqlConversionErrors();
                                }
                                if (CollectionUtils.isNotEmpty(cqlConversionResult.getFhirCqlConversionErrors())) {
                                    fhirCqlConversionErrors = cqlConversionResult.getFhirCqlConversionErrors();
                                }
                                if (CollectionUtils.isNotEmpty(cqlConversionResult.getFhirMatCqlConversionErrors())) {
                                    fhirMatCqlConversionErrors = cqlConversionResult.getFhirMatCqlConversionErrors();
                                }
                            }
                        }
                    }
                }

                //Measure FHIR validation errors
                List<FhirValidationResult> measureFhirValidationErrors = new ArrayList<>();
                if (conversionResultDto.getMeasureConversionResults() != null && Boolean.FALSE.equals(conversionResultDto.getMeasureConversionResults().getSuccess())) {
                    measureFhirValidationErrors = conversionResultDto.getMeasureConversionResults().getMeasureFhirValidationResults();
                }

                paramsMap.put("valueSetFhirValidationErrors", valueSetFhirValidationErrors);
                paramsMap.put("libraryFhirValidationErrors", libraryFhirValidationErrors);
                paramsMap.put("measureFhirValidationErrors", measureFhirValidationErrors);
                paramsMap.put("qdmCqlConversionErrors", qdmCqlConversionErrors);
                paramsMap.put("qdmMatCqlConversionErrors", qdmMatCqlConversionErrors);
                paramsMap.put("fhirCqlConversionErrors", fhirCqlConversionErrors);
                paramsMap.put("fhirMatCqlConversionErrors", fhirMatCqlConversionErrors);

            }

            Instant instant = Instant.parse(conversionResultDto.getModified());
            paramsMap.put("runDate", DateUtility.formatInstant(instant, DATE_FORMAT));
            paramsMap.put("runTime", DateUtility.formatInstant(instant, TIME_FORMAT));
            paramsMap.put("measureName", measure.getDescription());
            paramsMap.put("measureVersion", measure.getVersion());
            paramsMap.put("modelType", MeasureDetailsUtil.defaultTypeIfBlank(measure.getMeasureModel()));

        } else if (measure == null) {
            paramsMap.put("noMeasureFoundError", NO_MEASURE_FOUND_ERROR);
        } else {
            paramsMap.put("conversionServiceError", CONVERSION_SERVICE_ERROR);
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfiguration.getTemplate("fhirvalidationreport/fhir_validation_report.ftl"),
                paramsMap);
    }
}
