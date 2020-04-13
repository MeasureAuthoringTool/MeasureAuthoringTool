package mat.server.service;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;

public interface FhirLibraryConversionRemoteCall {
    ConversionResultDto convert(String libraryId);
}
