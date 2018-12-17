package mat.client.measure.measuredetails;

import mat.client.shared.MatDetailItem;

public interface MeasureDetailsObserver {
	void handleDeleteMeasureButtonClick();
	void handleSaveButtonClick();
	void handleStateChanged();
	void handleMenuItemClick(MatDetailItem menuItem); 
}
