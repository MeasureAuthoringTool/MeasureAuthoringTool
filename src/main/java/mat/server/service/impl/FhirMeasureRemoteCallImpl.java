package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mat.client.measure.FhirMeasurePackageResult;
import mat.client.shared.MatRuntimeException;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.dto.fhirconversion.PushValidationResult;
import mat.server.logging.LogFactory;
import mat.server.service.FhirMeasureRemoteCall;
import mat.server.service.cql.HumanReadableArtifacts;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class FhirMeasureRemoteCallImpl implements FhirMeasureRemoteCall {
    private static final String FHIR_ID_PARAMS = "?id={id}";
    private static final String GET_HUMAN_READABLE_ARTIFACTS = "measure/package/humanReadableArtifacts/{measureId}";
    private static final String FHIR_PUSH_MEASURE_PARAMS = "measure/pushMeasure" + FHIR_ID_PARAMS;
    private static final String FHIR_PACKAGE_MEASURE_PARAMS = "measure/package" + FHIR_ID_PARAMS;
    private static final String FHIR_ORCH_MEASURE_SRVC_PARAMS = "orchestration/measure" + FHIR_ID_PARAMS + "&conversionType={conversionType}&xmlSource={xmlSource}&vsacGrantingTicket={vsacGrantingTicket}";
    private static final String SIMPLE_XML_SOURCE = "SIMPLE";
    private static final String MEASURE_XML_SOURCE = "MEASURE";

    private static final Log log = LogFactory.getLog(FhirMeasureRemoteCallImpl.class);
    @Qualifier("internalRestTemplate")
    private final RestTemplate restTemplate;
    private final FhirContext fhirContext;
    @Value("${FHIR_SRVC_URL:http://localhost:9080/}")
    private String fhirServicesUrl;

    public FhirMeasureRemoteCallImpl(RestTemplate restTemplate, FhirContext fhirContext) {
        this.restTemplate = restTemplate;
        this.fhirContext = fhirContext;
    }

    @Override
    public PushValidationResult push(String measureId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        return rest(fhirServicesUrl + FHIR_PUSH_MEASURE_PARAMS,
                HttpMethod.POST,
                PushValidationResult.class,
                uriVariables);
    }

    @Override
    public ConversionResultDto convert(String measureId, String vsacGrantingTicket, boolean draft) {
        return convert(measureId, ConversionType.CONVERSION, vsacGrantingTicket, draft);
    }

    @Override
    public ConversionResultDto validate(String measureId, String vsacGrantingTicket, boolean draft) {
        return convert(measureId, ConversionType.VALIDATION, vsacGrantingTicket, draft);
    }

    @Override
    public FhirMeasurePackageResult packageMeasure(String measureId) {
        FhirMeasurePackageResult result = new FhirMeasurePackageResult();
        IParser jsonParser = fhirContext.newJsonParser();
        IParser xmlParser = fhirContext.newXmlParser();

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        MeasurePackageFullData packagingResp = rest(fhirServicesUrl + FHIR_PACKAGE_MEASURE_PARAMS,
                HttpMethod.GET,
                MeasurePackageFullData.class,
                uriVariables);

        Measure measure = jsonParser.parseResource(Measure.class, packagingResp.getMeasure());
        Library lib = jsonParser.parseResource(Library.class, packagingResp.getLibrary());
        Bundle bundle = jsonParser.parseResource(Bundle.class, packagingResp.getIncludeBundle());

        jsonParser.setPrettyPrint(true);
        xmlParser.setPrettyPrint(true);

        result.setMeasureJson(jsonParser.encodeResourceToString(measure));
        result.setMeasureXml(xmlParser.encodeResourceToString(measure));

        result.setMeasureLibJson(jsonParser.encodeResourceToString(lib));
        result.setMeasureLibXml(xmlParser.encodeResourceToString(lib));

        result.setInludedLibsJson(jsonParser.encodeResourceToString(bundle));
        result.setInludedLibsXml(xmlParser.encodeResourceToString(bundle));


        lib.getContent().forEach(a -> {
            if (StringUtils.equalsIgnoreCase("text/cql", a.getContentType())) {
                result.setMeasureLibCql(getDataFromAttachment(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+json", a.getContentType())) {
                result.setMeasureLibElmJson(getDataFromAttachment(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+xml", a.getContentType())) {
                result.setMeasureLibElmXml(getDataFromAttachment(a));
            }
        });
        return result;
    }

    @Override
    public HumanReadableArtifacts getHumanReadableArtifacts(String measureId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("measureId", measureId);
        return rest(fhirServicesUrl + GET_HUMAN_READABLE_ARTIFACTS
                , HttpMethod.GET,
                HumanReadableArtifacts.class,
                uriVariables);
    }

    private ConversionResultDto convert(String measureId, ConversionType conversionType, String vsacGrantingTicket, boolean draft) {
        if (log.isDebugEnabled()) {
            log.debug("callRemoteService " + measureId + " " + conversionType + " draft: " + draft);
        }

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        uriVariables.put("conversionType", conversionType.name());
        uriVariables.put("xmlSource", draft ? MEASURE_XML_SOURCE : SIMPLE_XML_SOURCE);
        uriVariables.put("vsacGrantingTicket", vsacGrantingTicket);  // vsac not used currently in mat fhir services

        return rest(fhirServicesUrl + FHIR_ORCH_MEASURE_SRVC_PARAMS,
                HttpMethod.PUT,
                ConversionResultDto.class,
                uriVariables);
    }

    private <T> T rest(String url, HttpMethod method, Class<T> responseType, Map<String, Object> paramMap) {
        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(url,
                    method,
                    null,
                    responseType,
                    paramMap);
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException", e);
            throw new MatRuntimeException(e);
        }
        if (response.getStatusCode().isError()) {
            log.error(url + " returned " + response.getStatusCode());
            throw new MatRuntimeException("url " + url + " returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

    private String getDataFromAttachment(Attachment a) {
        return new String(a.getData(), StandardCharsets.UTF_8);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeasurePackageFullData {
        private String includeBundle;
        private String measure;
        private String library;
    }
}
