package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mat.client.measure.FhirMeasurePackageResult;
import mat.client.shared.MatRuntimeException;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.server.service.FhirMeasureRemoteCall;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.dv.util.Base64;
import org.checkerframework.checker.units.qual.A;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
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

    private static final Log logger = LogFactory.getLog(FhirMeasureRemoteCallImpl.class);
    private static final String FHIR_ID_PARAMS = "?id={id}";
    private static final String FHIR_PACKAGE_MEASURE_PARAMS = "measure/packager/full/json" + FHIR_ID_PARAMS;
    private static final String FHIR_ORCH_MEASURE_SRVC_PARAMS = "orchestration/measure" + FHIR_ID_PARAMS + "&conversionType={conversionType}&xmlSource={xmlSource}&vsacGrantingTicket={vsacGrantingTicket}";
    private static final String SIMPLE_XML_SOURCE = "SIMPLE";
    private static final String MEASURE_XML_SOURCE = "MEASURE";

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}")
    private String fhirServicesUrl;
    private final RestTemplate restTemplate;
    private final FhirContext fhirContext;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeasurePackageFullData {
            String includeBundle;
            String measure;
            String library;
    }

    public FhirMeasureRemoteCallImpl(RestTemplate restTemplate,FhirContext fhirContext) {
        this.restTemplate = restTemplate;
        this.fhirContext = fhirContext;
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

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        MeasurePackageFullData packagingResp = rest(fhirServicesUrl + FHIR_PACKAGE_MEASURE_PARAMS,
                HttpMethod.GET,
                MeasurePackageFullData.class,
                uriVariables);

        result.setMeasureJson(packagingResp.getMeasure());
        result.setInludedLibsJson(packagingResp.getIncludeBundle());
        result.setMeasureLibJson(packagingResp.getLibrary());

        Measure measure = fhirContext.newJsonParser().parseResource(Measure.class,result.getMeasureJson());
        result.setMeasureXml(fhirContext.newXmlParser().encodeResourceToString(measure));

        Library lib = fhirContext.newJsonParser().parseResource(Library.class,result.getMeasureJson());
        result.setInludedLibsXml(fhirContext.newXmlParser().encodeResourceToString(lib));
        lib.getContent().forEach(a -> {
            if (StringUtils.equalsIgnoreCase("text/cql", a.getContentType())) {
                result.setMeasureLibCql(decodeBase64(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+json", a.getContentType())) {
                result.setMeasureLibElmJson(decodeBase64(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+xml", a.getContentType())) {
                result.setMeasureLibElmXml(decodeBase64(a));
            }
        });

        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class,result.getInludedLibsJson());
        result.setInludedLibsXml(fhirContext.newXmlParser().encodeResourceToString(bundle));
        return result;
    }

    private ConversionResultDto convert(String measureId, ConversionType conversionType, String vsacGrantingTicket, boolean draft) {
        if (logger.isDebugEnabled()) {
            logger.debug("callRemoteService " + measureId + " " + conversionType + " draft: " + draft);
        }

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", measureId);
        uriVariables.put("conversionType", conversionType.name());
        uriVariables.put("xmlSource", draft ? MEASURE_XML_SOURCE : SIMPLE_XML_SOURCE);
        uriVariables.put("vsacGrantingTicket", vsacGrantingTicket);

        return rest(fhirServicesUrl + FHIR_ORCH_MEASURE_SRVC_PARAMS,
                HttpMethod.PUT,
                ConversionResultDto.class,
                uriVariables);
    }

    private <T> T rest(String url, HttpMethod method, Class<T> responseType, Map<String,Object> paramMap) {
        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(url,
                    method,
                    null,
                    responseType,
                    paramMap);
        } catch (HttpClientErrorException e) {
            throw new MatRuntimeException(e);
        }
        if (response.getStatusCode().isError()) {
            throw new MatRuntimeException("url " + url + " returned error code " + response.getStatusCode());
        }
        return response.getBody();
    }

    private String decodeBase64(Attachment a) {
        return new String(Base64.decode(
                new String(a.getData(), StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }
}
