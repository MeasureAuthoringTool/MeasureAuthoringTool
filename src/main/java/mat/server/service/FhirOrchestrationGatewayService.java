package mat.server.service;

import mat.dto.fhirconversion.ConversionResultDto;

public interface FhirOrchestrationGatewayService {
    /**
     * Call fhir conversion
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
    ConversionResultDto convert(String measureId, String vsacGrantingTicket, boolean isDraft);

    /**
     * Call fhir validation.
     *
     * @param measureId - required measure id
     * @return validation report object
     * @throws mat.client.shared.MatRuntimeException
     */
    ConversionResultDto validate(String measureId, String vsacGrantingTicket, boolean isDraft);
}
