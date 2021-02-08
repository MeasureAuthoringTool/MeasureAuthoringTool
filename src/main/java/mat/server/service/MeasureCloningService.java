package mat.server.service;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.MatException;

public interface MeasureCloningService {
    /**
     * Clones the source QDM Measure into a new measure with a reset version and in draft state.
     * @param currentDetails Model details of the source QDM Measure.
     * @return Search Result consisting of the details of the new measure.
     * @throws MatException Source measure cannot be cloned.
     */
    ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails) throws MatException;

    /**
     *  Creates a draft version of an existing QDM or FHIR measure and bumps the current measure version.
     * @param currentDetails Model details of the source Measure.
     * @param isFhir True, source measure is FHIR based.
     *               False, source measure is QDM based.
     * @return Search Result consisting of the details of the new measure.
     * @throws MatException Source measure cannot be drafted.
     */
    ManageMeasureSearchModel.Result draft(ManageMeasureDetailModel currentDetails, boolean isFhir) throws MatException;

    /**
     * Converts the source QDM Measure to FHIR with a reset version and in draft state.
     * @param currentDetails Model details of the source QDM Measure
     * @return Search Result consisting of the details of the new measure.
     * @throws MatException Source QDM measure cannot be converted to FHIR.
     */
    ManageMeasureSearchModel.Result convert(ManageMeasureDetailModel currentDetails) throws MatException;
}
