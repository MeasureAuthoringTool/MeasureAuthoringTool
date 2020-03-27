package mat.server.service;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;

public interface FhirMeasureService {
    FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure, String vsacGrantingTicket, String loggedinUserId) throws MatException;
}
