package mat.vsac;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import mat.vsacmodel.BasicResponse;
import mat.vsacmodel.CodeSystemVersionResponse;
import mat.vsacmodel.ValueSetResult;
import mat.vsacmodel.ValueSetWrapper;
import mat.vsacmodel.VsacCode;
import mat.vsac.util.VsacConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class VsacService {
    private static final String PROFILE = "Most Recent Code System Versions in VSAC";
    private static final String UTF8_BOM = "\uFEFF";

    // https://vsac.nlm.nih.gov
    private final String baseVsacUrl;
    private final RestTemplate restTemplate;
    private final VsacConverter vsacConverter = new VsacConverter();
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonMapper jsonMapper = new JsonMapper();

    public VsacService(String baseVsacUrl, RestTemplate restTemplate) {
        this.baseVsacUrl = baseVsacUrl;
        this.restTemplate = restTemplate;
    }


    public ValueSetWrapper getVSACValueSetWrapper(String oid, String apiKey) {
    	ValueSetResult vsacResponseResult = getValueSetResult(oid,  apiKey);

        if (isSuccessFull(vsacResponseResult)) {
            try {
                return vsacConverter.toWrapper(vsacResponseResult.getXmlPayLoad());
            } catch (Exception e) {
                log.warn("Cannot get XMl from mat.vsac oid: {}, reason: {}", oid, vsacResponseResult.getFailReason(), e);
                return null;
            }
        } else {
            log.warn("Error response from the mat.vsac service, result: {}", vsacResponseResult);
            return null;
        }
    }

    @Cacheable(value = "vsacCodesystemVersions")
    public CodeSystemVersionResponse getCodeSystemVersionFromName(String codeSystemName, String apiKey) {
        String path = "/CodeSystem/" + codeSystemName + "/Info";

        VsacCode vsacResponse = getCode(path, apiKey);

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
    public VsacCode getCode(String path,  String apiKey) {
        //  https://vsac.nlm.nih.gov/vsac/CodeSystem/LOINC/Version/2.66/Code/21112-8/Info?ticket=ST-281185-McNb53ZGHYtaGjHamgKg-cas&resultFormat=json&resultSet=standard
        // "/CodeSystem/LOINC22/Version/2.67/Code/21112-8/Info";

        try {
            Map<String, String> params = new HashMap<>();
            params.put("resultFormat", "json");
            params.put("resultSet", "standard");
            
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac" + path +
                "?resultFormat={resultFormat}&resultSet={resultSet}").buildAndExpand(params).encode().toUri();
            ResponseEntity<VsacCode> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), VsacCode.class);

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
     * @param Vsac apiKey.
     * @return VSACResponseResult The result.
     */
    public BasicResponse getProfileList(String apiKey) {
        // https://vsac.nlm.nih.gov/vsac/profiles

        try {
        	URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/profiles").build().encode().toUri();
        	return buildBasicResponseFromEntity(restTemplate.exchange(uri, HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Reterival of Versions for given OID.
     *
     * @param oid           The oid
     * @param VSAC apiKey
     * @return VSACResponseResult
     */
    	public BasicResponse reteriveVersionListForOid(String oid, String apiKey) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("oid", oid);
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/oid/{oid}/versions").buildAndExpand(params).encode().toUri();
          	return buildBasicResponseFromEntity(restTemplate.exchange(uri, HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class));
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid.
     *
     * @param oid           The oid
     * @param VSAC apiKey
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOID(String oid,  String profile, String apiKey) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            Map<String, String> params = new HashMap<>();
            params.put("oid", oid);
            params.put("profile", profile);
            params.put("includeDraft", "yes");
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
            		"id={oid}&profile={profile}&includeDraft={includeDraft}")
            		.buildAndExpand(params).encode().toUri();
            ResponseEntity<String> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class);
            return buildBasicResponseFromEntity(response);
            
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid and version.
     *
     * @param oid           THe oid.
     * @param version       The version.
     * @param VSAC apiKey
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndVersion(String oid, String version, String apiKey) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            Map<String, String> params = new HashMap<>();
            params.put("oid", oid);
            params.put("version", version);
            params.put("includeDraft", "yes");
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
            		"id={oid}&version={version}&includeDraft={includeDraft}").buildAndExpand(params).encode().toUri();
            ResponseEntity<String> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class); 
            return buildBasicResponseFromEntity(response);
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    /**
     * Multiple Value Set Retrieval based on oid and effective date.
     *
     * @param oid           The oid.
     * @param effectiveDate The effective date.
     * @param VSAC apiKey
     * @return VSACResponseResult
     * NOTE: This is NOT used, so leave as is.
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndEffectiveDate(String oid, String effectiveDate, String serviceTicket) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            Map<String, String> params = new HashMap<>();
            params.put("oid", oid);
            params.put("effectiveDate", effectiveDate);
            params.put("releaseType", "VSAC");
            params.put("includeDraft", "yes");
            params.put("st", serviceTicket);
            return buildBasicResponseFromEntity(restTemplate.getForEntity(
                    UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
                            "id={oid}&effectiveDate={effectiveDate}&ticket={st}&ReleaseType={releaseType}&includeDraft={includeDraft}")
                            .buildAndExpand(params)
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
     * @param VSAC apiKey
     * @return VSACResponseResult
     */
    public BasicResponse getMultipleValueSetsResponseByOIDAndProfile(String oid, String profile, String apiKey) {
        return getMultipleValueSetsResponseByOID(oid, profile, apiKey);
    }

    public BasicResponse getMultipleValueSetsResponseByOIDAndRelease(String oid, String release, String apiKey) {
        // https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets
        try {
            Map<String, String> params = new HashMap<>();
            params.put("oid", oid);
            params.put("release", release);
            //includeDraft doesn't work on this one.
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
            		"id={oid}&release={release}").buildAndExpand(params).encode().toUri();
            ResponseEntity<String> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class);
            return buildBasicResponseFromEntity(response);
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getValueSet(String oid, String apiKey) {
        final Map<String, String> params = new HashMap<>();
        params.put("oid", oid);
        params.put("profile", PROFILE);
        params.put("includeDraft", "yes");
        URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
            "id={oid}&profile={profile}&includeDraft={includeDraft}")
            .buildAndExpand(params)
            .encode()
            .toUri();

        try {
          ResponseEntity<String> response = restTemplate.exchange(uri,  
          		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class); 
          return buildBasicResponseFromEntity(response);
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public ValueSetResult getValueSetResult(String oid,  String apiKey) {
        Map<String, String> params = new HashMap<>();
        params.put("oid", oid);
        params.put("profile", PROFILE);
        params.put("includeDraft", "yes");
        URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/svs/RetrieveMultipleValueSets?" +
            "id={oid}&profile={profile}&includeDraft={includeDraft}")
            .buildAndExpand(params)
            .encode()
            .toUri();

        try {
        	ResponseEntity<String> response = restTemplate.exchange(uri,  
          		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class);
        	String xml = response.getBody();

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

    public BasicResponse getAllPrograms() {
        // https://vsac.nlm.nih.gov/vsac/programs
        try {
        	URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/programs").build().encode().toUri();
        	//temp
        	log.info("getAllPrograms(): baseVsacUrl = "+baseVsacUrl);
          ResponseEntity<String> response = restTemplate.exchange(uri,  
          		HttpMethod.GET, null, String.class);
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
                    log.error("Error parsing json in getAllPrograms " + response.getBody(), e);
                    return buildBasicResponseForFailure();
                }
            } else {
            //temp
            	log.error("getAllPrograms(): response.getStatusCode() = "+response.getStatusCode());
                return buildBasicResponseForFailure();
            }
        } catch (HttpClientErrorException e) {
        	log.error("getAllPrograms(): HttpClientErrorException -> "+e.getMessage());
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getReleasesOfProgram(String program) {
        // https://vsac.nlm.nih.gov/vsac/program/NAME
        try {
        	URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/program/{program}").buildAndExpand(program).encode().toUri();
          ResponseEntity<String> response = restTemplate.exchange(uri,  
          		HttpMethod.GET, null, String.class);
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
                    log.error("Error parsing json in getAllPrograms " + response.getBody(), e);
                    return buildBasicResponseForFailure();
                }
            } else {
                return buildBasicResponseForFailure();
            }
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        }
    }

    public BasicResponse getDirectReferenceCode(String codeURLString, String apiKey) {
        //  https://vsac.nlm.nih.gov/vsac/CodeSystem/LOINC/Version/2.66/Code/21112-8/Info?ticket=ST-281185-McNb53ZGHYtaGjHamgKg-cas&resultFormat=json&resultSet=standard
        // "/CodeSystem/LOINC22/Version/2.67/Code/21112-8/Info";
        try {
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac" + codeURLString)
            		.queryParam("resultFormat", "json")
            		.queryParam("resultSet", "standard")
            		.build().encode().toUri();
            ResponseEntity<String> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, getHeaderEntityWithAuthentication(apiKey), String.class);
            return buildBasicResponseFromEntity(response);
            
        } catch (HttpClientErrorException e) {
            return buildBasicResponseForHttpClientError(e);
        } catch(HttpServerErrorException e) {
            return buildBasicResponseForHttpServerError(e);
        }
    }

    public BasicResponse getLatestProfileOfProgram(String programName) {
        // https://vsac.nlm.nih.gov/vsac/program/NAME/latest profile
        try {

            Map<String, String> params = new HashMap<>();
            params.put("programName", programName);
            params.put("profile", "latest profile");
            URI uri = UriComponentsBuilder.fromUriString(baseVsacUrl + "/vsac/program/{programName}/{profile}").buildAndExpand(params).encode().toUri();
            ResponseEntity<String> response = restTemplate.exchange(uri,  
            		HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                BasicResponse result = new BasicResponse();
                result.setXmlPayLoad(response.getBody());
                result.setFailResponse(false);
                try {
                    JsonNode node = jsonMapper.readTree(response.getBody());
                    result.setXmlPayLoad(node.get("name").asText());
                    return result;
                } catch (Exception e) {
                    log.error("Error parsing json in getAllPrograms " + response.getBody(), e);
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
                log.debug(result.getXmlPayLoad());
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
        log.error(e.getResponseBodyAsString(), e);
        return buildBasicResponseForFailure();
    }

    private BasicResponse buildBasicResponseForHttpServerError(HttpServerErrorException e) {
        log.error(e.getResponseBodyAsString(), e);
        if (e.getStatusCode().is4xxClientError()) {
            return buildCustomResponseForFailure(BasicResponse.REQUEST_NOT_FOUND);
        }
        if (e.getStatusCode().is5xxServerError()) {
            return buildCustomResponseForFailure(BasicResponse.SERVER_ERROR);
        }
        return buildBasicResponseForFailure();
    }

    private BasicResponse buildCustomResponseForFailure(int errorCode) {
        BasicResponse result = new BasicResponse();
        result.setFailReason(errorCode);
        result.setFailResponse(true);
        return result;
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

    private <T> String postForString2xx(URI uri, HttpEntity<T> request) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.debug("Vsac Rest Error", e);
            return null;
        }
    }

    private HttpEntity<String> buildEntityWithTicketHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("content-type", "application/x-www-form-urlencoded");
        return new HttpEntity<>(headers);
    }

    private HttpEntity<MultiValueMap<String,String>> buildEntityWithUrlEncodedBody(MultiValueMap<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(body, headers);
    }
    
    private HttpEntity<String> getHeaderEntityWithAuthentication(String apiKey) {
    	HttpHeaders headers = new HttpHeaders();
      headers.setBasicAuth("apikey", apiKey);
      return new HttpEntity<>(headers);
    }
}
