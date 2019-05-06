package mat.server.service;

import mat.client.measure.ManageMeasureDetailModel;
import mat.model.clause.MeasureDetails;

public interface MeasureDetailsService {

	MeasureDetails getMeasureDetailFromManageMeasureDetailsModel(MeasureDetails measureDetails, ManageMeasureDetailModel model);

	ManageMeasureDetailModel getManageMeasureDetailModelFromMeasureDetails(ManageMeasureDetailModel model, MeasureDetails measureDetails);

}
