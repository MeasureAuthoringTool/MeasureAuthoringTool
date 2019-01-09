package mat.client.measure.measuredetails;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.shared.MatDetailItem;

public interface MeasureDetailsObserver {
	void handleDeleteMeasureButtonClick();
	void handleSaveButtonClick();
	void handleStateChanged();
	void saveMeasureDetails();
	void handleMenuItemClick(MatDetailItem menuItem);
	void handleEditCompositeMeasures(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel);
}
