package mat.vsac;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import mat.vsac.model.BasicResponse;
import mat.vsac.model.CodeSystemVersionResponse;
import mat.vsac.model.ValueSetResult;
import mat.vsac.model.ValueSetWrapper;
import mat.vsac.model.VsacCode;
import mat.vsac.util.VsacConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class VsacService {
    private static final String PROFILE = "Most Recent Code System Versions in VSAC";
    private static final String CANNOT_OBTAIN_A_SINGLE_USE_SERVICE_TICKET = "Cannot obtain a single-use service ticket.";
    private static final String UTF8_BOM = "\uFEFF";
    private static final String ACTION_TOKEN = "action=\"";

    // https://utslogin.nlm.nih.gov/cas/v1
    private final String baseTicketUrl;
    // https://vsac.nlm.nih.gov
    private final String baseVsacUrl;
    private final RestTemplate restTemplate;
    private final VsacConverter vsacConverter = new VsacConverter();
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonMapper jsonMapper = new JsonMapper();

    public VsacService(String baseTicketUrl, String baseVsacUrl, RestTemplate restTemplate) {
        this.baseTicketUrl = baseTicketUrl;
        this.baseVsacUrl = baseVsacUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * @param apiKey A users mat.vsac api-key.
     * @return Null if a ticket granting ticket could not be obtained.
     */
    public String getTicketGrantingTicket(String apiKey) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseTicketUrl + "/api-key")
                .queryParam("apikey", apiKey)
                .build()
                .encode()
                .toUri();

        String result = postForString2xx(uri, buildEntityWithTicketHeaders());
        if (result != null) {
            // body returns an html file we want to grab the part after the last / in the url:
            // ... action="https://utslogin.nlm.nih.gov/cas/v1/api-key/TGT-asdasdasdas-cas" ...
            int actionIndex = result.indexOf(ACTION_TOKEN);
            if (actionIndex != -1) {
                int endIndex = result.indexOf("\"", actionIndex + ACTION_TOKEN.length());
                if (endIndex != -1) {
                    String url = result.substring(actionIndex + ACTION_TOKEN.length(), endIndex);
                    int lastSlash = url.lastIndexOf('/');
                    if (lastSlash != -1) {
                        result = url.substring(lastSlash + 1);
                        log.info("Obtained TicketGrantingTicket");
                    } else {
                        log.error("Could not / in action url " + url);
                    }
                } else {
                    log.error("Could not find closing quote in action attribute!" + result);
                }
            } else {
                log.error("Could not find action attribute! " + result);
            }
        }
        return result;
    }

    /**
     * @param ticketGrantingTicket A users ticket granting ticket.
     * @return Null if a service ticket couldn't be obtained.
     */
    public String getServiceTicket(String ticketGrantingTicket) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseTicketUrl + "/tickets/" + ticketGrantingTicket)
                .queryParam("service", "http://umlsks.nlm.nih.gov")
                .build()
                .encode()
                .toUri();
        return postForString2xx(uri, buildEntityWithTicketHeaders());
    }

    public ValueSetResult getValueSetResult(String oid, String ticketGrantingTicket) {
        String singleUseTicket = getServiceTicket(ticketGrantingTicket);

        if (StringUtils.isEmpty(singleUseTicket)) {
            return ValueSetResult.builder()
                    .isFailResponse(true)
                    .failReason(CANNOT_OBTAIN_A_SINGLE_USE_SERVICE_TICKET)
                    .build();
        }

        String url = baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets";

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ticket", singleUseTicket)
                .queryParam("id", oid)
                .queryParam("profile", PROFILE)
                .build()
                .encode()
                .toUri();

        try {
            String xml = restTemplate.getForObject(uri, String.class);

            return ValueSetResult.builder()
                    .isFailResponse(false)
                    .xmlPayLoad(xml)
                    .build();
        } catch (RestClientException e) {
            String message;
            if (Objects.equals(e.getMessage(), "404 : [no body]")) {
                message = "404 Cannot find value set with oid: " + oid;
            } else {
                message = e.getMessage();
            }

            return ValueSetResult.builder()
                    .isFailResponse(true)
                    .failReason(message)
                    .build();
        }
    }

    public ValueSetWrapper getVSACValueSetWrapper(String oid, String ticketGrantingTicket) {
        ValueSetResult vsacResponseResult = getValueSetResult(oid, ticketGrantingTicket);

        if (isSuccessFull(vsacResponseResult)) {
            try {
                return vsacConverter.toWrapper(vsacResponseResult.getXmlPayLoad());
            } catch (Exception e) {
                log.warn("Cannot get XMl from mat.vsac oid: {}, reason: {}", oid, vsacResponseResult.getFailReason(),e);
                return null;
            }
        } else {
            log.warn("Error response from the mat.vsac service, result: {}", vsacResponseResult);
            return null;
        }
    }

    @Cacheable(value = "vsacCodesystemVersions")
    public CodeSystemVersionResponse getCodeSystemVersionFromName(String codeSystemName, String ticketGrantingTicket) {
        String path = "/CodeSystem/" + codeSystemName + "/Info";

        VsacCode vsacResponse = getCode(path, ticketGrantingTicket);

        if (vsacResponse.getMessage().equals("ok") && vsacResponse.getData() != null
                && vsacResponse.getData().getResultSet().size() == 1 &&
                vsacResponse.getData().getResultSet().get(0).getCsVersion() != null) {

            return CodeSystemVersionResponse.builder()
                    .message("ok")
                    .success(Boolean.TRUE)
                    .version(vsacResponse.getData().getResultSet().get(0).getCsVersion())
                    .build();
        } else {
            String errorMessage;

            if (vsacResponse.getErrors() == null || CollectionUtils.isEmpty(vsacResponse.getErrors().getResultSet())) {
                if (StringUtils.isEmpty(vsacResponse.getMessage())) {
                    errorMessage = "Unknown Error obtaining version from VSAC"; // should never happen
                } else {
                    errorMessage = vsacResponse.getMessage();
                }
            } else {
                List<String> strList = vsacResponse.getErrors().getResultSet()
                        .stream()
                        .map(VsacCode.VsacErrorResultSet::getErrDesc)
                        .collect(Collectors.toList());

                errorMessage = String.join(", ", strList);
            }

            return CodeSystemVersionResponse.builder()
                    .message(errorMessage)
                    .success(Boolean.FALSE)
                    .build();
        }
    }

    // cannot cache due to all users not having the same rights
    public VsacCode getCode(String path, String ticketGrantingTicket) {
        //  https://vsac.nlm.nih.gov/vsac/CodeSystem/LOINC/Version/2.66/Code/21112-8/Info?ticket=ST-281185-McNb53ZGHYtaGjHamgKg-cas&resultFormat=json&resultSet=standard
        // "/CodeSystem/LOINC22/Version/2.67/Code/21112-8/Info";

        String singleUseTicket = getServiceTicket(ticketGrantingTicket);

        if (StringUtils.isEmpty(singleUseTicket)) {
            return createErrorResponse(CANNOT_OBTAIN_A_SINGLE_USE_SERVICE_TICKET);
        }
        try {
            ResponseEntity<VsacCode> response = restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac" + path)
                            .queryParam("ticket", singleUseTicket)
                            .queryParam("resultFormat", "json")
                            .queryParam("resultSet", "standard")
                            .build()
                            .encode()
                            .toUri(), VsacCode.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                if (response.hasBody()) {
                    return response.getBody();
                } else {
                    return createErrorResponse(response.getStatusCode().toString());
                }
            }
        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();

            try {
                return mapper.readValue(errorBody, VsacCode.class);
            } catch (JsonProcessingException jsonProcessingException) {
                return createErrorResponse(e.getStatusText());
            }
        }
    }

    /**
     * Retrieve All profile List.
     *
     * @param ticketGrantingTicket The service ticket.
     * @return VSACResponseResult The result.
     */
    public BasicResponse getProfileList(String ticketGrantingTicket) {
        // https://vsac.nlm.nih.gov/vsac/profiles
        String singleUseTicket = getServiceTicket(ticketGrantingTicket);

        if (StringUtils.isEmpty(singleUseTicket)) {
            log.error("Error getting singleUseTicket");
            return buildBasicResponseForFailure();
        }

        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/profiles")
                            .queryParam("ticket", singleUseTicket)
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Reterival of Versions for given OID.
     *
     * @param oid           The oid
     * @param serviceTicket The service ticket.
     * @return VSACResponseResult
     */
    public BasicResponse reteriveVersionListForOid(String oid, String serviceTicket) {
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/oid/" + oid + "/versions")
                            .queryParam("ticket", serviceTicket)
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid.
     *
     * @param oid           The oid
     * @param serviceTicket The service ticket.
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOID(String oid, String serviceTicket, String profile) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets")
                            .queryParam("id", oid)
                            .queryParam("profile", profile)
                            .queryParam("ticket", serviceTicket)
                            .queryParam("includeDraft", "yes")
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid and version.
     *
     * @param oid           THe oid.
     * @param version       The version.
     * @param serviceTicket The service ticket.
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndVersion(String oid, String version, String serviceTicket) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets")
                            .queryParam("id", oid)
                            .queryParam("version", version)
                            .queryParam("ticket", serviceTicket)
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid and effective date.
     *
     * @param oid           The oid.
     * @param effectiveDate The effective date.
     * @param serviceTicket The service ticket.
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndEffectiveDate(String oid, String effectiveDate, String serviceTicket) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets")
                            .queryParam("id", oid)
                            .queryParam("effectiveDate", effectiveDate)
                            .queryParam("ticket", serviceTicket)
                            .queryParam("ReleaseType", "VSAC")
                            .queryParam("includeDraft", "yes")
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid and Profile Name.
     *
     * @param oid           THe oid.
     * @param serviceTicket The service ticket.
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndProfile(String oid, String profile, String serviceTicket) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets")
                            .queryParam("id", oid)
                            .queryParam("profile", profile)
                            .queryParam("ticket", serviceTicket)
                            .queryParam("includeDraft", "yes")
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getAllPrograms() {
        // https://vsac.nlm.nih.gov/vsac/programs
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/programs")
                            .build()
                            .encode()
                            .toUri(), String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BasicResponse result = new BasicResponse();
                result.setXmlPayLoad(response.getBody());
                result.setPgmRels(new ArrayList<>());
                result.setFailResponse(false);
                try {
                    JsonNode node = jsonMapper.readTree(response.getBody());
                    JsonNode programs = node.get("Program");
                    if (programs.isArray()) {
                        programs.elements().forEachRemaining(p -> result.getPgmRels().add(p.get("name").asText()));
                    }
                    return result;
                } catch (Exception e) {
                    log.error("Error parsing json in getAllPrograms " + response.getBody(),e);
                    return buildBasicResponseForFailure();
                }
            } else {
                return buildBasicResponseForFailure();
            }
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getReleasesOfProgram(String program) {
        // https://vsac.nlm.nih.gov/vsac/program/NAME/lastest profile
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/program/" + program)
                            .build()
                            .encode()
                            .toUri(), String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BasicResponse result = new BasicResponse();
                result.setXmlPayLoad(response.getBody());
                result.setPgmRels(new ArrayList<>());
                result.setFailResponse(false);
                try {
                    JsonNode node = jsonMapper.readTree(response.getBody());
                    JsonNode release = node.get("release");
                    if (release.isArray()) {
                        release.forEach(n -> result.getPgmRels().add(n.get("name").asText()));
                    }
                    return result;
                } catch (Exception e) {
                    log.error("Error parsing json in getAllPrograms " + response.getBody(),e);
                    return buildBasicResponseForFailure();
                }
            } else {
                return buildBasicResponseForFailure();
            }
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getMultipleValueSetsResponseByOIDAndRelease(String oid, String release, String serviceTicket) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets")
                            .queryParam("id", oid)
                            .queryParam("release", release)
                            .queryParam("ticket", serviceTicket)
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getDirectReferenceCode(String codeURLString, String serviceTicket) {
        //  https://vsac.nlm.nih.gov/vsac/CodeSystem/LOINC/Version/2.66/Code/21112-8/Info?ticket=ST-281185-McNb53ZGHYtaGjHamgKg-cas&resultFormat=json&resultSet=standard
        // "/CodeSystem/LOINC22/Version/2.67/Code/21112-8/Info";
        try {
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac" + codeURLString)
                            .queryParam("ticket", serviceTicket)
                            .queryParam("resultFormat", "json")
                            .queryParam("resultSet", "standard")
                            .build()
                            .encode()
                            .toUri(), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getLatestProfileOfProgram(String programName) {
        // https://vsac.nlm.nih.gov/vsac/program/NAME/lastest profile
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(baseVsacUrl + "/vsac/program/" + programName + "/lastest profile")
                            .build()
                            .encode()
                            .toUri(), String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BasicResponse result = new BasicResponse();
                result.setXmlPayLoad(response.getBody());
                result.setFailResponse(false);
                try {
                    JsonNode node = jsonMapper.readTree(response.getBody());
                    result.setXmlPayLoad(node.get("name").asText());
                    return result;
                } catch (Exception e) {
                    log.error("Error parsing json in getAllPrograms " + response.getBody(),e);
                    return buildBasicResponseForFailure();
                }
            } else {
                return buildBasicResponseForFailure();
            }
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    private BasicResponse buildBasicResponseFromEntity(ResponseEntity<String> response) {
        BasicResponse result;
        if (response.getStatusCode().is2xxSuccessful()) {
            StringBuilder out = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new StringReader(Objects.requireNonNull(response.getBody())))) {
                boolean firstLine = true;
                for (String s; (s = r.readLine()) != null; ) {
                    if (firstLine) {
                        s = removeUTF8BOM(s);
                        firstLine = false;
                    }
                    out.append(s);
                }
                result = buildBasicResponseForSuccess(out.toString());
                log.info(result.getXmlPayLoad());
            } catch (IOException ioe) {
                result = buildBasicResponseForFailure();
                log.error("buildResponseResult", ioe);
            }
        } else {
            result = buildBasicResponseForFailure();
        }
        return result;
    }

    private BasicResponse buildBasicResponseForSuccess(String xml) {
        BasicResponse result = new BasicResponse();
        result.setXmlPayLoad(xml);
        result.setFailReason(0);
        result.setFailResponse(false);
        return result;
    }

    private BasicResponse buildBasicResponseForFailure() {
        BasicResponse result = new BasicResponse();
        result.setFailReason(BasicResponse.REQUEST_FAILED);
        result.setFailResponse(true);
        return result;
    }

    private BasicResponse buildBasicResponseForHttpClientError(HttpClientErrorException e) {
        log.error(e.getResponseBodyAsString());
        return buildBasicResponseForFailure();
    }

    /**
     * Method to remove UTF8BOM characters from retrieve xml from VSAC.
     */
    private String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    private VsacCode createErrorResponse(String message) {
        VsacCode vsacResponse = new VsacCode();
        vsacResponse.setStatus("error");
        vsacResponse.setMessage(message);
        return vsacResponse;
    }


    private boolean isSuccessFull(ValueSetResult vsacResponseResult) {
        return vsacResponseResult != null &&
                vsacResponseResult.getXmlPayLoad() != null &&
                !vsacResponseResult.isFailResponse();
    }

    private String postForString2xx(URI uri, HttpEntity<String> request) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info("Vsac Rest Error", e);
            return null;
        }
    }

    private HttpEntity<String> buildEntityWithTicketHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("content-type", "application/x-www-form-urlencoded");
        return new HttpEntity<>(headers);
    }
}
