package mat.server.service.fhirvalidationreport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dto.fhirconversion.FhirValidationResult;
import mat.model.clause.Measure;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.LoggedInUserUtil;
import mat.server.service.FhirMeasureService;
import mat.server.service.cql.FhirCqlParser;
import mat.server.service.cql.LibraryErrors;
import mat.server.service.cql.MatXmlResponse;
import mat.shared.CQLError;
import mat.shared.DateUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MeasureValidationReportImpl implements FhirValidationReport {
    private static final String NO_MEASURE_FOUND_ERROR = "The measure with that measure id does not exist.";
    private static final String REPORT_FTL = "fhirvalidationreport/fhir_validation_report.ftl";

    private final Configuration freemarkerConfiguration;
    private final MeasureDAO measureDAO;
    private final MeasureXMLDAO measureXmlDAO;
    private final FhirCqlParser fhirCqlParser;
    private final FhirMeasureService measureService;

    @Value("${mat.measure.current.release.version}")
    private String currentMatVersion;

    public MeasureValidationReportImpl(Configuration freemarkerConfiguration,
                                       MeasureDAO measureDAO,
                                       MeasureXMLDAO measureXmlDAO, FhirCqlParser fhirCqlParser,
                                       FhirMeasureService measureService) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.measureDAO = measureDAO;
        this.measureXmlDAO = measureXmlDAO;
        this.fhirCqlParser = fhirCqlParser;
        this.measureService = measureService;
    }

    /**
     * This method calls the QDM to FHIR conversion validation micro service for a given measure
     * and generates the FHIR conversion validation report for measure and its cql libraries & value sets,
     *
     * @param measureId          - Measure id for which validation report is requested
     * @param vsacGrantingTicket - VSAC granting ticket
     * @param converted          - true if the report is shown for a newly FHIR measure coverted from QDM.
     * @return HTML string report
     */
    @Override
    public String generateReport(String measureId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException {
        MatXmlResponse parseResponse = null;
        Measure measure = measureDAO.getMeasureByMeasureId(measureId);
        if (measure != null) {
            if (measure.isFhirMeasure()) {
                //If it is a FHIR measure just do validation.
                parseResponse = fhirCqlParser.parseFromMeasure(measure.getId()); //measureId, vsacGrantingTicket, measure.isDraft());
            } else {
                //If it is a QDM measure we have to do a conversion without storing in the DB then run validation.
                ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
                result.setFhirConvertible(true);
                result.setId(measureId);
                try {
                    var fhirResult = measureService.convert(result,
                            vsacGrantingTicket,
                            LoggedInUserUtil.getLoggedInUser(),
                            false);
                    CQLModel model = CQLUtilityClass.getCQLModelFromXML(measureXmlDAO.findForMeasure(measureId).getMeasureXMLAsString());
                    parseResponse = fhirCqlParser.parse(fhirResult.getFhirCql(),
                            model);
                } catch (MatException e) {
                    log.error("Error running measureService.convert",e);
                    throw new MatRuntimeException(e);
                }
            }
        }
        return generateValidationReport(measure, parseResponse);
    }

    private String generateValidationReport(Measure measure,
                                            MatXmlResponse parseResponse)
            throws IOException, TemplateException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("matVersion", currentMatVersion);

        if (parseResponse != null && measure != null) {
            prepareForMeasure(measure, parseResponse, paramsMap);
        } else if (measure == null) {
            paramsMap.put("noMeasureFoundError", NO_MEASURE_FOUND_ERROR);
        } else {
            paramsMap.put("conversionServiceError", CONVERSION_SERVICE_ERROR);
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(REPORT_FTL), paramsMap);
    }

    private void prepareForMeasure(Measure measure,
                                   MatXmlResponse matXmlResponse,
                                   Map<String, Object> paramsMap) {
        addConversionStatusMessage(matXmlResponse, paramsMap);
        addErrors(measure, matXmlResponse, paramsMap);

        Instant instant = Instant.now();
        paramsMap.put("runDate", DateUtility.formatInstant(instant, DATE_FORMAT));
        paramsMap.put("runTime", DateUtility.formatInstant(instant, TIME_FORMAT));

        addMeasureDetails(measure, paramsMap);
    }

    private void addMeasureDetails(Measure measure, Map<String, Object> paramsMap) {
        paramsMap.put("measureName", measure.getDescription());
        paramsMap.put("measureVersion", measure.getVersion());
        paramsMap.put("modelType", ModelTypeHelper.defaultTypeIfBlank(measure.getMeasureModel()));
    }

    private void addErrors(Measure measure, MatXmlResponse matXmlResponse, Map<String, Object> paramsMap) {
        paramsMap.put("measureFhirValidationErrors", getMeasureErrors(measure, matXmlResponse));
        paramsMap.put("libraryFhirValidationErrors", getLibraryErrors(measure, matXmlResponse));
    }

    private void addConversionStatusMessage(MatXmlResponse matXmlResponse, Map<String, Object> paramsMap) {
        boolean hasErrors = CollectionUtils.isNotEmpty(matXmlResponse.getErrors()) &&
                matXmlResponse.getErrors().stream().anyMatch(le -> CollectionUtils.isNotEmpty(le.getErrors()));
        String conversionStatusMessage = !hasErrors ?
                "The FHIR measure was created successfully." :
                "Warning: The FHIR measure was created successfully with errors.";
        paramsMap.put("conversionStatusMessage", conversionStatusMessage);
        paramsMap.put("outcome", "");
        paramsMap.put("errorReason", "");
    }

    private List<FhirValidationResult> getMeasureErrors(Measure measure,
                                                        MatXmlResponse matXmlResponse) {
        List<FhirValidationResult> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(matXmlResponse.getErrors())) {
            matXmlResponse.getErrors().stream().
                    filter(le -> StringUtils.equals(le.getName(), measure.getCqlLibraryName())).
                    forEach(e -> {
                        if (CollectionUtils.isNotEmpty(e.getErrors())) {
                            e.getErrors().forEach(cqle ->
                                    result.add(convertCqlErrror(e, cqle)));
                        }
                    });
        }
        return result;
    }

    private List<FhirValidationResult> getLibraryErrors(Measure measure, MatXmlResponse matXmlResponse) {
        List<FhirValidationResult> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(matXmlResponse.getErrors())) {
            matXmlResponse.getErrors().stream().
                    filter(le -> !StringUtils.equals(le.getName(), measure.getCqlLibraryName())).
                    forEach(e -> {
                        if (CollectionUtils.isNotEmpty((e.getErrors()))) {
                            e.getErrors().forEach(cqle -> result.add(convertCqlErrror(e, cqle)));
                        }
                    });
        }
        return result;
    }

    private FhirValidationResult convertCqlErrror(LibraryErrors le, CQLError e) {
        var result = new FhirValidationResult();
        result.setSeverity(e.getSeverity());
        result.setLocationField(le.getName() + "-" + le.getVersion() + ": " + e.getStartErrorInLine());
        result.setErrorDescription(e.getErrorMessage());
        return result;
    }
}
