package mat.server.service.fhirvalidationreport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.DTO.fhirconversion.*;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.server.service.FhirLibraryConversionRemoteCall;
import mat.shared.DateUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

@Service
public class CqlLibraryValidationReportImpl implements FhirValidationReport {

    private static final Log logger = LogFactory.getLog(CqlLibraryValidationReportImpl.class);
    private static final String DATE_FORMAT = "dd-MMM-YYYY";
    private static final String TIME_FORMAT = "hh:mm aa";
    private static final String CQL_LIBRARY_NOT_FOUND_ERROR = "CQL Library with the given id does not exist.";
    private static final String CONVERSION_SERVICE_ERROR = "An error occurred while validating the FHIR conversion. Please try again later. If this continues please contact the MAT help desk.";
    private static final String REPORT_FTL = "fhirvalidationreport/fhir_library_validation_report.ftl";

    private Configuration freemarkerConfiguration;
    private CQLLibraryDAO libraryDAO;
    private FhirLibraryConversionRemoteCall fhirLibraryConversionRemoteCall;

    @Value("${mat.measure.current.release.version}")
    private String currentMatVersion;

    public CqlLibraryValidationReportImpl(Configuration freemarkerConfiguration,
                                          CQLLibraryDAO libraryDAO,
                                          FhirLibraryConversionRemoteCall fhirLibraryConversionRemoteCall) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.libraryDAO = libraryDAO;
        this.fhirLibraryConversionRemoteCall = fhirLibraryConversionRemoteCall;
    }


    @Override
    public String generateReport(String libraryId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException {

        ConversionResultDto conversionResult = null;
        CQLLibrary cqlLibrary = libraryDAO.find(libraryId);
        if (cqlLibrary != null) {
            conversionResult = validateFhirLibraryConversion(libraryId);
        }

        return generateValidationReport(conversionResult, cqlLibrary, converted);
    }


    private ConversionResultDto validateFhirLibraryConversion(String libraryId) throws IOException {
        if (libraryId == null) {
            return null;
        }

        try {
            return fhirLibraryConversionRemoteCall.validate(libraryId);
        } catch (MatRuntimeException e) {
            throw new IOException(e);
        }
    }

    private String generateValidationReport(ConversionResultDto conversionResultDto, CQLLibrary library, boolean converted) throws IOException, TemplateException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("matVersion", currentMatVersion);

        if (library == null) {
            paramsMap.put("cqlLibraryNotFoundError", CQL_LIBRARY_NOT_FOUND_ERROR);
        } else if (conversionResultDto == null) {
            paramsMap.put("conversionServiceError", CONVERSION_SERVICE_ERROR);
        } else {
            prepareReport(conversionResultDto, library, converted, paramsMap);
        }

        return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(REPORT_FTL), paramsMap);
    }

    private void prepareReport(ConversionResultDto conversionResultDto, CQLLibrary library, boolean converted, Map<String, Object> paramsMap) {
        if (converted) {
            addConversionStatusMessage(conversionResultDto, paramsMap);
        }

        if (ConversionOutcome.SUCCESS != conversionResultDto.getOutcome()) {
            addErrors(conversionResultDto, paramsMap);
        }

        if (conversionResultDto.getModified() != null) {
            addReportDateTime(conversionResultDto, paramsMap);
        }
        addCqlLibraryDetails(library, paramsMap);
    }

    private void addConversionStatusMessage(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {
        String conversionStatusMessage = ConversionOutcome.SUCCESS == conversionResultDto.getOutcome() ?
                "The FHIR CQL Library was created successfully." :
                "Warning: The FHIR CQL Library was created successfully with errors.";
        paramsMap.put("conversionStatusMessage", conversionStatusMessage);
        paramsMap.put("outcome", String.valueOf(conversionResultDto.getOutcome()));
        paramsMap.put("errorReason", StringUtils.trimToNull(conversionResultDto.getErrorReason()));
    }

    private void addCqlLibraryDetails(CQLLibrary library, Map<String, Object> paramsMap) {
        paramsMap.put("cqlLibraryName", library.getName());
        paramsMap.put("cqlLibraryVersion", library.getVersion());
        paramsMap.put("cqlLibraryModel", ModelTypeHelper.defaultTypeIfBlank(library.getLibraryModelType()));
    }

    private void addReportDateTime(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {
        Instant instant = Instant.parse(conversionResultDto.getModified());
        paramsMap.put("runDate", DateUtility.formatInstant(instant, DATE_FORMAT));
        paramsMap.put("runTime", DateUtility.formatInstant(instant, TIME_FORMAT));
    }

    private void addErrors(ConversionResultDto conversionResultDto, Map<String, Object> paramsMap) {

        if (CollectionUtils.isEmpty(conversionResultDto.getLibraryConversionResults())) {
            paramsMap.put("libraryFhirValidationErrors", new ArrayList<Object>());
            paramsMap.put("externalErrorsMap", new ArrayList<Object>());
            paramsMap.put("qdmCqlConversionErrors", new ArrayList<Object>());
            paramsMap.put("fhirCqlConversionErrors", new ArrayList<Object>());
            paramsMap.put("valueSetFhirValidationErrors", new ArrayList<Object>());
        } else {
            paramsMap.put("valueSetFhirValidationErrors", buildValueSetErrors(conversionResultDto));
            paramsMap.put("LibraryNotFoundInHapi", toLibraryNotFoundInHapi(conversionResultDto));
            paramsMap.put("libraryFhirValidationErrors", buildLibraryFhirValidationErrors(conversionResultDto));
            paramsMap.put("qdmCqlConversionErrors", buildQdmConversionErrorsMap(conversionResultDto));
            paramsMap.put("fhirCqlConversionErrors", buildFhirConversionErrorsMap(conversionResultDto));
            paramsMap.put("externalErrorsMap", buildExternalErrorsMap(conversionResultDto));
        }

    }

    private List<FhirValidationResult> buildValueSetErrors(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getValueSetConversionResults().stream()
                .filter(results -> isNotSuccess(results.getSuccess()))
                .map(results -> results.getValueSetFhirValidationResults())
                .findAny()
                .orElse(Collections.emptyList());
    }

    private String toLibraryNotFoundInHapi(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> "Not Found in Hapi".equals(results.getReason()))
                .map(results -> results.getReason())
                .findAny()
                .orElse(null);
    }

    private List<FhirValidationResult> buildLibraryFhirValidationErrors(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.isNull(results) || isNotSuccess(results.getSuccess()))
                .map(results -> results.getLibraryFhirValidationResults())
                .findAny()
                .orElse(Collections.emptyList());
    }

    private HashMap<String, List<Object>> buildQdmConversionErrorsMap(ConversionResultDto conversionResultDto) {
        var qdmCqlConversionErrorsMap = new HashMap<String, List<Object>>();

        var qdmCqlConversionErrors = extractQdmCqlConversionErrorsMap(conversionResultDto);
        for (var entry : qdmCqlConversionErrors.entrySet()) {
            qdmCqlConversionErrorsMap.putIfAbsent(entry.getKey(), new ArrayList<>());
            qdmCqlConversionErrorsMap.get(entry.getKey()).addAll(entry.getValue());
        }

        var qdmMatCqlConversionErrors = extractQdmMatCqlConversionExceptionsMap(conversionResultDto);
        for (var entry : qdmMatCqlConversionErrors.entrySet()) {
            qdmCqlConversionErrorsMap.putIfAbsent(entry.getKey(), new ArrayList<>());
            qdmCqlConversionErrorsMap.get(entry.getKey()).addAll(entry.getValue());
        }

        return qdmCqlConversionErrorsMap;
    }

    private Map<String, List<CqlConversionError>> extractQdmCqlConversionErrorsMap(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.isNull(results) || isNotSuccess(results.getSuccess()))
                .map(results -> results.getCqlConversionResult())
                .filter(Objects::nonNull)
                .map(cqlConversionResult -> cqlConversionResult.getCqlConversionErrors())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(cqlConversionErrors -> cqlConversionErrors.stream())
                .filter(cqlConversionError -> StringUtils.isNotBlank(cqlConversionError.getTargetIncludeLibraryId()))
                .filter(cqlConversionError -> StringUtils.isNotBlank(cqlConversionError.getTargetIncludeLibraryVersionId()))
                .collect(Collectors.groupingBy(cqlConversionError -> toKey(cqlConversionError)));
    }

    private Map<String, List<MatCqlConversionException>> extractQdmMatCqlConversionExceptionsMap(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.isNull(results) || isNotSuccess(results.getSuccess()))
                .map(results -> results.getCqlConversionResult())
                .map(cqlConversionResult -> cqlConversionResult.getMatCqlConversionErrors())
                .filter(matCqlConversionException -> CollectionUtils.isNotEmpty(matCqlConversionException))
                .flatMap(fhirMatCqlConversionErrors -> fhirMatCqlConversionErrors.stream())
                .filter(mapConversionException -> StringUtils.isNotBlank(mapConversionException.getTargetIncludeLibraryId()))
                .filter(mapConversionException -> StringUtils.isNotBlank(mapConversionException.getTargetIncludeLibraryVersionId()))
                .collect(Collectors.groupingBy(mapCqlConversionException -> toKey(mapCqlConversionException)));
    }

    private Map<String, List<Object>> buildFhirConversionErrorsMap(ConversionResultDto conversionResultDto) {
        var fhirCqlConversionErrorsMap = new HashMap<String, List<Object>>();

        var fhirCqlConversionErrors = extractFhirCqlConversionErrorsMap(conversionResultDto);
        for (var entry : fhirCqlConversionErrors.entrySet()) {
            fhirCqlConversionErrorsMap.putIfAbsent(entry.getKey(), new ArrayList<>());
            fhirCqlConversionErrorsMap.get(entry.getKey()).addAll(entry.getValue());
        }

        var fhirMatCqlConversionErrors = extractFhirMatCqlConversionExceptionsMap(conversionResultDto);
        for (var entry : fhirMatCqlConversionErrors.entrySet()) {
            fhirCqlConversionErrorsMap.putIfAbsent(entry.getKey(), new ArrayList<>());
            fhirCqlConversionErrorsMap.get(entry.getKey()).addAll(entry.getValue());
        }

        return fhirCqlConversionErrorsMap;
    }

    private Map<String, List<CqlConversionError>> extractFhirCqlConversionErrorsMap(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.isNull(results) || isNotSuccess(results.getSuccess()))
                .map(results -> results.getCqlConversionResult())
                .filter(Objects::nonNull)
                .map(cqlConversionResult -> cqlConversionResult.getFhirCqlConversionErrors())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(cqlConversionErrors -> cqlConversionErrors.stream())
                .filter(cqlConversionError -> StringUtils.isNotBlank(cqlConversionError.getTargetIncludeLibraryId()))
                .filter(cqlConversionError -> StringUtils.isNotBlank(cqlConversionError.getTargetIncludeLibraryVersionId()))
                .collect(Collectors.groupingBy(cqlConversionError -> toKey(cqlConversionError)));
    }

    private Map<String, List<MatCqlConversionException>> extractFhirMatCqlConversionExceptionsMap(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.isNull(results) || isNotSuccess(results.getSuccess()))
                .map(results -> results.getCqlConversionResult())
                .map(cqlConversionResult -> cqlConversionResult.getFhirMatCqlConversionErrors())
                .filter(matCqlConversionException -> CollectionUtils.isNotEmpty(matCqlConversionException))
                .flatMap(fhirMatCqlConversionErrors -> fhirMatCqlConversionErrors.stream())
                .filter(mapConversionException -> StringUtils.isNotBlank(mapConversionException.getTargetIncludeLibraryId()))
                .filter(mapConversionException -> StringUtils.isNotBlank(mapConversionException.getTargetIncludeLibraryVersionId()))
                .collect(Collectors.groupingBy(mapConversionException -> toKey(mapConversionException)));
    }

    private Map<String, List<CqlConversionError>> buildExternalErrorsMap(ConversionResultDto conversionResultDto) {
        return conversionResultDto.getLibraryConversionResults().stream()
                .filter(results -> Objects.nonNull(results))
                .filter(results -> isNotSuccess(results.getSuccess()))
                .map(results -> results.getExternalErrors())
                .flatMap(entries -> entries.entrySet().stream())
                .flatMap(e -> e.getValue().stream())
                .filter(q -> StringUtils.isNotBlank(q.getTargetIncludeLibraryId()))
                .filter(q -> StringUtils.isNotBlank(q.getTargetIncludeLibraryVersionId()))
                .collect(Collectors.groupingBy(q -> toKey(q)));
    }

    private String toKey(CqlConversionError cqlConversionError) {
        return new StringBuilder(cqlConversionError.getTargetIncludeLibraryId())
                .append(" ")
                .append(cqlConversionError.getTargetIncludeLibraryVersionId())
                .toString();
    }

    private String toKey(MatCqlConversionException mapConversionException) {
        return new StringBuilder(mapConversionException.getTargetIncludeLibraryId())
                .append(" ")
                .append(mapConversionException.getTargetIncludeLibraryVersionId())
                .toString();
    }

    private boolean isNotSuccess(Boolean success) {
        return FALSE.equals(success);
    }

}
