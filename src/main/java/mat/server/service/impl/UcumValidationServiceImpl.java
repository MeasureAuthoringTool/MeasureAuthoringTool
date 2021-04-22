package mat.server.service.impl;

import mat.server.service.UcumValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class UcumValidationServiceImpl implements UcumValidationService {
    @Qualifier("internalRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${FHIR_SRVC_URL:http://localhost:9080/}")
    private String fhirServicesUrl;

    public UcumValidationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean validate(String unit) {
        return restTemplate.getForObject(createUrl(unit), Boolean.class);
    }

    private String createUrl(String unit) {
        String encoded = URLEncoder.encode(unit, StandardCharsets.UTF_8);
        return fhirServicesUrl + "ucum/" + encoded;
    }
}
