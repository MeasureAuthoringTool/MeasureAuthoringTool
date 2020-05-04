package mat.server.service.fhirvalidationreport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import mat.DTO.fhirconversion.ConversionOutcome;
import mat.DTO.fhirconversion.ConversionResultDto;
import mat.DTO.fhirconversion.CqlConversionError;
import mat.DTO.fhirconversion.CqlConversionResult;
import mat.DTO.fhirconversion.FhirValidationResult;
import mat.DTO.fhirconversion.LibraryConversionResults;
import mat.DTO.fhirconversion.MatCqlConversionException;
import mat.DTO.fhirconversion.ValueSetConversionResults;
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
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CqlLibraryValidationReportImpl implements FhirValidationReport {

    private static final Log logger = LogFactory.getLog(CqlLibraryValidationReportImpl.class);
    private static final String currentMatVersion = "v6.0";

    public static final String CQL_LIBRARY_NOT_FOUND_ERROR = "CQL Library with the given id does not exist.";
    public static final String FTL_TEMPLATE_NAME = "fhirvalidationreport/fhir_library_validation_report.ftl";


    private Configuration freemarkerConfiguration;
    private CQLLibraryDAO libraryDAO;
    private FhirLibraryConversionRemoteCall fhirLibraryConversionRemoteCall;


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

        var template = freemarkerConfiguration.getTemplate(FTL_TEMPLATE_NAME);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, paramsMap);
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
        List<FhirValidationResult> valueSetFhirValidationErrors = getValueSetErrors(conversionResultDto);

        List<FhirValidationResult> libraryFhirValidationErrors = new ArrayList<>();
        HashMap<String, List<Object>> qdmCqlConversionErrorsMap = new HashMap<>();
        HashMap<String, List<Object>> fhirCqlConversionErrorsMap = new HashMap<>();
        Map<String, List<CqlConversionError>> externalErrorsMap = new HashMap<>();
        getLibraryErrors(conversionResultDto, libraryFhirValidationErrors, qdmCqlConversionErrorsMap, fhirCqlConversionErrorsMap, paramsMap, externalErrorsMap);

        paramsMap.put("valueSetFhirValidationErrors", valueSetFhirValidationErrors);
        paramsMap.put("libraryFhirValidationErrors", libraryFhirValidationErrors);
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
                     logger.debug("Not found in Hapi");
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
