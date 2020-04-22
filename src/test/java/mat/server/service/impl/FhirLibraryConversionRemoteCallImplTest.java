package mat.server.service.impl;

import mat.DTO.fhirconversion.ConversionResultDto;
import mat.DTO.fhirconversion.ConversionType;
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
class FhirLibraryConversionRemoteCallImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FhirLibraryConversionRemoteCallImpl fhirLibraryConversionRemoteCall;

    @Test
    public void testConvert() {
        Whitebox.setInternalState(fhirLibraryConversionRemoteCall, "fhirLibraryConversionRemoteUrl", "http://localhost:9080/library/convertStandAlone");

        ResponseEntity<ConversionResultDto> responseEntity = new ResponseEntity<>(new ConversionResultDto(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.POST), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.any(Map.class))).thenReturn(responseEntity);
        fhirLibraryConversionRemoteCall.convert("libraryId", ConversionType.CONVERSION);
        Map<String, String> expectedParams = Map.of("id", "libraryId", "conversionType", "CONVERSION");
        String expectedUrl = "http://localhost:9080/library/convertStandAlone?id={id}&conversionType={conversionType}";
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.POST), Mockito.nullable(HttpEntity.class), Mockito.eq(ConversionResultDto.class), Mockito.eq(expectedParams));
    }
}