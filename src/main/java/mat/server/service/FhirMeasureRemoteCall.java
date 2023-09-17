package mat.server.service;

import mat.client.measure.FhirMeasurePackageResult;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.PushValidationResult;
import mat.server.service.cql.HumanReadableArtifacts;

public interface FhirMeasureRemoteCall {
    /**
     * Call fhir conversion
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
		ConversionResultDto convert(String measureId, String apiKey, boolean isDraft);

    /**
     * Call fhir validation.
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
    ConversionResultDto validate(String measureId, String apiKey, boolean isDraft);

    FhirMeasurePackageResult packageMeasure(String measureId);

    PushValidationResult push(String measureId);

    HumanReadableArtifacts getHumanReadableArtifacts(String measureId);
}
