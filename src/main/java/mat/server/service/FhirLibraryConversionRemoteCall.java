package mat.server.service;


import mat.DTO.fhirconversion.ConversionResultDto;
import mat.DTO.fhirconversion.ConversionType;

public interface FhirLibraryConversionRemoteCall {
    ConversionResultDto convert(String libraryId, ConversionType conversionType);


    ConversionResultDto validate(String libraryId);
}
