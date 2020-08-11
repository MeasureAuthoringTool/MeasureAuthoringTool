package mat.server.service.cql;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import mat.client.shared.MatRuntimeException;
import mat.client.umls.service.VsacTicketInformation;
import mat.model.cql.CQLModel;
import mat.server.service.VSACApiService;
import mat.shared.SaveUpdateCQLResult;

/**
 * A service used to parse FHIR cql.
 */
@Service
@Slf4j
public class FhirCqlParserService implements FhirCqlParser {

    private static final String MATXML_FROM_CQL_SRVC = "cql-xml-gen/cql";
    private static final String MATXML_FROM_MEASURE_ID = "cql-xml-gen/measure/";
    private static final String MATXML_FROM_LIB_ID = "cql-xml-gen/standalone-lib/";
    private static final String UMLS_TOKEN = "UMLS-TOKEN";

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}")
    private String fhirServicesUrl;
    @Qualifier("internalRestTemplate")
    private final RestTemplate restTemplate;
    private final VSACApiService vsacApiService;
    // HttpSession instance proxy
    private final HttpSession httpSession;


    public FhirCqlParserService(RestTemplate restTemplate, HttpSession httpSession, VSACApiService vsacApiService) {
        this.restTemplate = restTemplate;
        this.httpSession = httpSession;
        this.vsacApiService = vsacApiService;
    }

    @Override
    public MatXmlResponse parse(String cql, CQLModel sourceModel) {
        String eightHourTicket = getTicket();
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setCql(cql);
        cqlXmlReq.setSourceModel(sourceModel);
        HttpHeaders headers = new HttpHeaders();
        headers.add(UMLS_TOKEN, eightHourTicket);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, headers);
        return rest(fhirServicesUrl + MATXML_FROM_CQL_SRVC, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parse(String cql, CQLModel sourceModel, ValidationRequest validationRequest) {
        String eightHourTicket = getTicket();
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setCql(cql);
        cqlXmlReq.setSourceModel(sourceModel);
        cqlXmlReq.setValidationRequest(validationRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add(UMLS_TOKEN, eightHourTicket);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, headers);
        return rest(fhirServicesUrl + MATXML_FROM_CQL_SRVC, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parseFromMeasure(String measureId) {
        String eightHourTicket = getTicket();
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setValidationRequest(new ValidationRequest());
        HttpHeaders headers = new HttpHeaders();
        headers.add(UMLS_TOKEN, eightHourTicket);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, headers);
        return rest(fhirServicesUrl + MATXML_FROM_MEASURE_ID + measureId, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public MatXmlResponse parseFromLib(String libId) {
        String eightHourTicket = getTicket();
        MatCqlXmlReq cqlXmlReq = new MatCqlXmlReq();
        cqlXmlReq.setValidationRequest(new ValidationRequest());
        HttpHeaders headers = new HttpHeaders();
        headers.add(UMLS_TOKEN, eightHourTicket);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MatCqlXmlReq> request = new HttpEntity<>(cqlXmlReq, headers);
        return rest(fhirServicesUrl +  MATXML_FROM_LIB_ID + libId, HttpMethod.PUT, request, MatXmlResponse.class, Collections.emptyMap());
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, List<LibraryErrors> libraryErrors) {
        CqlValidationResultBuilder resultBuilder = new CqlValidationResultBuilder();
        resultBuilder.cqlModel(cqlModel);
        resultBuilder.libraryErrors(libraryErrors);
        return resultBuilder.buildFromLibraryErrors();
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, MatXmlResponse matXmlResponse, ValidationRequest validationRequest) {
        CqlValidationResultBuilder resultBuilder = new CqlValidationResultBuilder();
        resultBuilder.cqlModel(cqlModel);
        resultBuilder.libraryErrors(matXmlResponse.getErrors());
        resultBuilder.cqlObject(matXmlResponse.getCqlObject());
        return resultBuilder.buildFromLibraryErrors();
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString) {
        MatXmlResponse fhirResponse = parse(cqlString, cqlModel);
        return parseFhirCqlLibraryForErrors(cqlModel, fhirResponse.getErrors());
    }

    @Override
    public SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString, ValidationRequest validationRequest) {
        MatXmlResponse fhirResponse = parse(cqlString, cqlModel, validationRequest);
        return parseFhirCqlLibraryForErrors(cqlModel, fhirResponse, validationRequest);
    }

    private String getTicket() {
        VsacTicketInformation ticketInfo = vsacApiService.getTicketGrantingTicket(httpSession.getId());
        return ticketInfo == null ? null : ticketInfo.getTicket();
    }

    private <T> T rest(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, Object> paramMap) {
        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(url,
                    method,
                    requestEntity,
                    responseType,
                    paramMap);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException", e);
            throw new MatRuntimeException(e);
        }
        if (response.getStatusCode().isError()) {
            log.error("{} returned {}", url, response.getStatusCode());
            throw new MatRuntimeException("url " + url + " returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

}
