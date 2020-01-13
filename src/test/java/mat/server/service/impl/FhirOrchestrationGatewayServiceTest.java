package mat.server.service.impl;

import java.util.Map;

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

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;

@ExtendWith(MockitoExtension.class)
public class FhirOrchestrationGatewayServiceTest {

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    FhirOrchestrationGatewayService service;

    @Test
    public void testValidateDraft() {
        Whitebox.setInternalState(service, "fhirOrchestrationUrl", "http://localhost:9080/orchestration/measure");
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.validate("measureId", true);
        Map<String, String> expectedParams = Map.of("conversionType", "VALIDATION", "xmlSource", "MEASURE", "id", "measureId");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testValidate() {
        Whitebox.setInternalState(service, "fhirOrchestrationUrl", "http://localhost:9080/orchestration/measure");
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.validate("measureId", false);
        Map<String, String> expectedParams = Map.of("conversionType", "VALIDATION", "xmlSource", "SIMPLE", "id", "measureId");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testConvertDraft() {
        Whitebox.setInternalState(service, "fhirOrchestrationUrl", "http://localhost:9080/orchestration/measure");
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.convert("measureId", true);
        Map<String, String> expectedParams = Map.of("conversionType", "CONVERSION", "xmlSource", "MEASURE", "id", "measureId");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

    @Test
    public void testConvert() {
        Whitebox.setInternalState(service, "fhirOrchestrationUrl", "http://localhost:9080/orchestration/measure");
        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        service.convert("measureId", false);
        Map<String, String> expectedParams = Map.of("conversionType", "CONVERSION", "xmlSource", "SIMPLE", "id", "measureId");
        String expectedUrl = "http://localhost:9080/orchestration/measure?id={id}&&conversionType={conversionType}&xmlSource={xmlSource}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.PUT), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }

}
