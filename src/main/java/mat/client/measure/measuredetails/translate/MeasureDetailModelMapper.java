package mat.client.measure.measuredetails.translate;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;

public interface MeasureDetailModelMapper {
	ManageMeasureDetailModel convertMeasureDetailsToModel();
	MeasureDetailsModel getMeasureDetailsModel(boolean isCompositeMeasure);
}
