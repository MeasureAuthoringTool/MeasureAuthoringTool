package mat.server.service;

import mat.client.measure.FhirMeasurePackageResult;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;

public interface FhirMeasureService {
    FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure,
                                      String vsacGrantingTicket,
                                      String loggedinUserId,
                                      boolean isUpdatingMatDB) throws MatException;

    FhirMeasurePackageResult packageMeasure(String measureId);

    String push(String measureId);
}
