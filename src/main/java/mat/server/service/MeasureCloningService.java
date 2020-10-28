package mat.server.service;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;

public interface MeasureCloningService {
    ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, boolean creatingDraft) throws MatException;
    ManageMeasureSearchModel.Result cloneForFhir(ManageMeasureDetailModel currentDetails, boolean isQdmToFhir) throws MatException;
}
