package mat.client.measure.measuredetails;

import mat.client.shared.MatDetailItem;

public interface MeasureDetailsObserver {
	void handleMenuItemClick(MatDetailItem menuItem);
	void handleDeleteMeasureButtonClick();
	void handleSaveButtonClick();
	void handleStateChanged(); 
}
