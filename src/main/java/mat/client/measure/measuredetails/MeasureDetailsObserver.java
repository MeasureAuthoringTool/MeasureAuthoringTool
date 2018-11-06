package mat.client.measure.measuredetails;

import mat.client.shared.MatDetailItem;

public interface MeasureDetailsObserver {
	void onMenuItemClicked(MatDetailItem menuItem);
	void handleDeleteMeasureButtonClick(); 
}
