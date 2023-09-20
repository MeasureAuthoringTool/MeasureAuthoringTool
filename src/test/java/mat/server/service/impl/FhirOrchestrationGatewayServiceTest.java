package mat.server.service.impl;

import mat.dto.fhirconversion.ConversionResultDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class FhirOrchestrationGatewayServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private FhirMeasureRemoteCallImpl service;

    @BeforeEach
    public void setUp() {
        Whitebox.setInternalState(service, "fhirServicesUrl", "http://localhost:9080/");
    }

    @Test
    public void testValidateDraft() {
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.validate("measureId", "vsacApiKey", true);
        Map<String, String> expectedParams = Map.of("conversionType", "VALIDATION", "xmlSource", "MEASURE", "id", "measureId", "apiKey", "vsacApiKey");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&conversionType={conversionType}&xmlSource={xmlSource}&apiKey={apiKey}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testValidate() {
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.validate("measureId", "vsacApiKey", false);
        Map<String, String> expectedParams = Map.of("conversionType", "VALIDATION", "xmlSource", "SIMPLE", "id", "measureId", "apiKey", "vsacApiKey");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&conversionType={conversionType}&xmlSource={xmlSource}&apiKey={apiKey}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testConvertDraft() {
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.convert("measureId", "vsacApiKey", true);
        Map<String, String> expectedParams = Map.of("conversionType", "CONVERSION", "xmlSource", "MEASURE", "id", "measureId", "apiKey", "vsacApiKey");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&conversionType={conversionType}&xmlSource={xmlSource}&apiKey={apiKey}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testConvert() {
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.convert("measureId", "vsacApiKey", false);
        Map<String, String> expectedParams = Map.of("conversionType", "CONVERSION", "xmlSource", "SIMPLE", "id", "measureId", "apiKey", "vsacApiKey");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&conversionType={conversionType}&xmlSource={xmlSource}&apiKey={apiKey}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

}
