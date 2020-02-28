package mat.server.service;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;

public interface FhirOrchestrationGatewayService {
    /**
     * Call fhir conversion
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
    ConversionResultDto convert(String measureId, boolean isDraft);

    /**
     * Call fhir validation.
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
    ConversionResultDto validate(String measureId, boolean isDraft);
}
