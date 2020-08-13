package mat.server.service.fhirvalidationreport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import mat.dao.clause.CQLLibraryDAO;
import mat.dto.fhirconversion.FhirValidationResult;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.server.logging.LogFactory;
import mat.server.service.cql.FhirCqlParser;
import mat.server.service.cql.LibraryErrors;
import mat.server.service.cql.MatXmlResponse;
import mat.shared.CQLError;
import mat.shared.DateUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CqlLibraryValidationReportImpl implements FhirValidationReport {
    private static final String currentMatVersion = "v6.0";

    public static final String CQL_LIBRARY_NOT_FOUND_ERROR = "CQL Library with the given id does not exist.";
    public static final String FTL_TEMPLATE_NAME = "fhirvalidationreport/fhir_library_validation_report.ftl";

    private final Configuration freemarkerConfiguration;
    private final CQLLibraryDAO libraryDAO;
    private final FhirCqlParser fhirCqlParser;


    public CqlLibraryValidationReportImpl(Configuration freemarkerConfiguration,
                                          CQLLibraryDAO libraryDAO,
                                          FhirCqlParser fhirCqlParser) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.libraryDAO = libraryDAO;
        this.fhirCqlParser = fhirCqlParser;
    }


    @Override
    public String generateReport(String libraryId, String vsacGrantingTicket, boolean converted) throws IOException, TemplateException {
        MatXmlResponse parseResponse = null;
        CQLLibrary cqlLibrary = libraryDAO.find(libraryId);
        if (cqlLibrary != null) {
            parseResponse = fhirCqlParser.parseFromLib(libraryId);
        }
        return generateValidationReport(parseResponse, cqlLibrary);
    }

    private String generateValidationReport(MatXmlResponse parseResponse, CQLLibrary library) throws IOException, TemplateException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("matVersion", currentMatVersion);

        if (library == null) {
            paramsMap.put("cqlLibraryNotFoundError", CQL_LIBRARY_NOT_FOUND_ERROR);
        } else if (parseResponse == null) {
            paramsMap.put("conversionServiceError", CONVERSION_SERVICE_ERROR);
        } else {
            prepareReport(library, parseResponse, paramsMap);
        }

        var template = freemarkerConfiguration.getTemplate(FTL_TEMPLATE_NAME);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, paramsMap);
    }

    private void prepareReport(CQLLibrary library, MatXmlResponse parseResponse, Map<String, Object> paramsMap) {
        addConversionStatusMessage(parseResponse, paramsMap);
        addErrors(parseResponse, paramsMap);
        addReportDateTime(paramsMap);
        addCqlLibraryDetails(library, paramsMap);
    }

    private void addConversionStatusMessage(MatXmlResponse parseResponse, Map<String, Object> paramsMap) {
        boolean hasErrors = CollectionUtils.isNotEmpty(parseResponse.getErrors()) &&
                parseResponse.getErrors().stream().anyMatch(le -> CollectionUtils.isNotEmpty(le.getErrors()));
        String conversionStatusMessage = !hasErrors ?
                "The FHIR CQL Library was created successfully." :
                "Warning: The FHIR CQL Library was created successfully with errors.";
        paramsMap.put("conversionStatusMessage", conversionStatusMessage);
        paramsMap.put("outcome", "");
        paramsMap.put("errorReason", "");
    }

    private void addCqlLibraryDetails(CQLLibrary library, Map<String, Object> paramsMap) {
        paramsMap.put("cqlLibraryName", library.getName());
        paramsMap.put("cqlLibraryVersion", library.getVersion());
        paramsMap.put("cqlLibraryModel", ModelTypeHelper.defaultTypeIfBlank(library.getLibraryModelType()));
    }

    private void addReportDateTime(Map<String, Object> paramsMap) {
        Instant instant = Instant.now();
        paramsMap.put("runDate", DateUtility.formatInstant(instant, DATE_FORMAT));
        paramsMap.put("runTime", DateUtility.formatInstant(instant, TIME_FORMAT));
    }

    private void addErrors(MatXmlResponse parseResponse, Map<String, Object> paramsMap) {
        paramsMap.put("libraryFhirValidationErrors", getLibraryErrors(parseResponse));
    }

    private List<FhirValidationResult> getLibraryErrors(MatXmlResponse matXmlResponse) {
        List<FhirValidationResult> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(matXmlResponse.getErrors())) {
            matXmlResponse.getErrors().
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
