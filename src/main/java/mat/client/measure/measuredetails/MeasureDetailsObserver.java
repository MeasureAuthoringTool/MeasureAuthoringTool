package mat.client.measure.measuredetails;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import mat.client.shared.MatDetailItem;

public interface MeasureDetailsObserver {
	void handleMenuItemClick(AnchorListItem anchorListItem, MatDetailItem menuItem);
	void handleDeleteMeasureButtonClick();
	void handleSaveButtonClick();
	void handleStateChanged(); 
}
