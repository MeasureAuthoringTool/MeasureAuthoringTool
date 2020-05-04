package mat.server.service;


import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;

public interface FhirLibraryConversionRemoteCall {
    ConversionResultDto convert(String libraryId, ConversionType conversionType);


    ConversionResultDto validate(String libraryId);
}
