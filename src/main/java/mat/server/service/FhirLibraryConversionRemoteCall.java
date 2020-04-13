package mat.server.service;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.ConversionType;

public interface FhirLibraryConversionRemoteCall {
    ConversionResultDto convert(String libraryId, ConversionType conversionType);
}
