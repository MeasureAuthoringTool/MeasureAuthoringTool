package mat.shared.measure.measuredetails.translate;

import mat.client.measure.ManageMeasureDetailModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;

public interface MeasureDetailModelMapper {
	ManageMeasureDetailModel convertMeasureDetailsToManageMeasureDetailModel();
	MeasureDetailsModel getMeasureDetailsModel(boolean isCompositeMeasure);
}
