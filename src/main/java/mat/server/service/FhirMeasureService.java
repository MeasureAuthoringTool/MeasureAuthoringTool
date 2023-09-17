package mat.server.service;

import mat.client.measure.FhirMeasurePackageResult;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.shared.MatException;
import mat.dto.fhirconversion.PushValidationResult;

public interface FhirMeasureService {
    FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure,
    																	String apiKey,
                                      String loggedinUserId,
                                      boolean isUpdatingMatDB) throws MatException;

    FhirMeasurePackageResult packageMeasure(String measureId);

    PushValidationResult push(String measureId);
}
