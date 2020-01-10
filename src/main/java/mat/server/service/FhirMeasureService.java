package mat.server.service;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;

public interface FhirMeasureService {
    ManageMeasureSearchModel.Result convert(ManageMeasureSearchModel.Result currentMeasure) throws MatException;
}
