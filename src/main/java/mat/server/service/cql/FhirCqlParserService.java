package mat.server.service.cql;

import mat.client.shared.MatRuntimeException;
import mat.client.umls.service.VsacTicketInformation;
import mat.model.cql.CQLModel;
import mat.server.logging.LogFactory;
import mat.server.service.VSACApiService;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

/**
 * A service used to parse FHIR cql.
 */
@Service
public class FhirCqlParserService implements FhirCqlParser {


    private static final String MATXML_FROM_CQL_SRVC = "cql-xml-gen/cql";
    private static final String MATXML_FROM_MEASURE_ID = "cql-xml-gen/measure/";
    private static final String MATXML_FROM_LIB_ID = "cql-xml-gen/standalone-lib/";
    private static final String UMLS_TOKEN = "UMLS-TOKEN";

    private static final String API_KEY = "API-KEY";
    private static final String REFRESHED_GRANTING_TICKET = "Refreshed-Granting-Ticket";

    @Qualifier("internalRestTemplate")
    private final RestTemplate restTemplate;
    private final VSACApiService vsacApiService;
    // HttpSession instance proxy
    private final HttpSession httpSession;
    private Log log = LogFactory.getLog(FhirCqlParserService.class);
    @Value("${FHIR_SRVC_URL:http://localhost:9080/}")
    private String fhirServicesUrl;


    public FhirCqlParserService(RestTemplate restTemplate, HttpSession httpSession, VSACApiService vsacApiService) {
        this.restTemplate = restTemplate;
        this.httpSession = httpSession;
        this.vsacApiService = vsacApiService;
    }

    @Override
    public MatXmlResponse parse(String cql, CQLModel sourceModel) {
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setCql(cql);
        cqlXmlReq.getValidationRequest().setValidateReturnType(true);
        cqlXmlReq.setSourceModel(sourceModel);

        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, createHeaders());

        return rest(fhirServicesUrl + MATXML_FROM_CQL_SRVC, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parse(String cql, CQLModel sourceModel, ValidationRequest validationRequest) {
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setCql(cql);
        cqlXmlReq.setSourceModel(sourceModel);
        cqlXmlReq.setValidationRequest(validationRequest);

        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, createHeaders());
        return rest(fhirServicesUrl + MATXML_FROM_CQL_SRVC, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parseFromMeasure(String measureId) {
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setValidationRequest(new ValidationRequest());

        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, createHeaders());

        return rest(fhirServicesUrl + MATXML_FROM_MEASURE_ID + measureId, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parseFromLib(String libId) {
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setValidationRequest(new ValidationRequest());

        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, createHeaders());

        return rest(fhirServicesUrl + MATXML_FROM_LIB_ID + libId, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, MatXmlResponse fhirResponse) {
        CqlValidationResultBuilder resultBuilder = new CqlValidationResultBuilder();
        resultBuilder.cqlModel(cqlModel);
        resultBuilder.cqlObject(fhirResponse.getCqlObject());
        resultBuilder.libraryErrors(fhirResponse.getErrors());
        resultBuilder.setUnusedCqlElements(fhirResponse.getUnusedCqlElements());

        return resultBuilder.buildFromLibraryErrors();
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel,
                                                            MatXmlResponse matXmlResponse,
                                                            ValidationRequest validationRequest) {
        CqlValidationResultBuilder resultBuilder = new CqlValidationResultBuilder();
        resultBuilder.cqlModel(cqlModel);
        resultBuilder.libraryErrors(matXmlResponse.getErrors());
        resultBuilder.cqlObject(matXmlResponse.getCqlObject());
        resultBuilder.setUnusedCqlElements(matXmlResponse.getUnusedCqlElements());
        return resultBuilder.buildFromLibraryErrors();
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString) {
        MatXmlResponse fhirResponse = parse(cqlString, cqlModel);
        return parseFhirCqlLibraryForErrors(cqlModel, fhirResponse);
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString, ValidationRequest validationRequest) {
        MatXmlResponse fhirResponse = parse(cqlString, cqlModel, validationRequest);
        return parseFhirCqlLibraryForErrors(cqlModel, fhirResponse, validationRequest);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        VsacTicketInformation ticketInformation = getVsacTicketInformation();

        if (ticketInformation != null) {
            headers.add(UMLS_TOKEN, ticketInformation.getTicket());
            headers.add(API_KEY, ticketInformation.getApiKey());
        } else {
            log.debug("No ticketInformation found to place in header.");
        }

        return headers;
    }

    private VsacTicketInformation getVsacTicketInformation() {
        return vsacApiService.getTicketGrantingTicket(httpSession.getId());
    }

    private void setRefreshedTicket(String newTicket) {
        VsacTicketInformation ticketInformation = getVsacTicketInformation();

        if( ticketInformation != null) {
            ticketInformation.setTicketIfNotBlank(newTicket);
        } else {
            log.debug("No ticketInformation found, new ticket not set.");
        }
    }

    private <T> T rest(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, Object> paramMap) {
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(url,
                    method,
                    requestEntity,
                    responseType,
                    paramMap);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException", e);
            throw new MatRuntimeException(e);
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException", e);
            throw new MatRuntimeException(e);
        }
        finally {
            if (response != null && response.getHeaders().containsKey(REFRESHED_GRANTING_TICKET)) {
                setRefreshedTicket(response.getHeaders().getFirst(REFRESHED_GRANTING_TICKET));
            }
        }

        if (response.getStatusCode().isError()) {
            log.error(url + " returned " + response.getStatusCode());
            throw new MatRuntimeException("url " + url + " returned error code " + response.getStatusCode());
        }

        return response.getBody();
    }

}
