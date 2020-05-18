package mat.server.service;


import mat.client.shared.MatRuntimeException;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import org.hl7.fhir.r4.model.Library;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

public interface FhirLibraryRemoteCall {
    ConversionResultDto convert(String libraryId, ConversionType conversionType);
    ConversionResultDto validate(String libraryId);

    /**
     * @param libraryId
     * @return The URL of the fhir resrouce.
     */
    String pushStandAlone(String libraryId);
    Library packageStandAlone(String libraryId);
}
