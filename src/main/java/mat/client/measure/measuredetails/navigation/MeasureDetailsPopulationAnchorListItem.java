package mat.client.measure.measuredetails.navigation;

import org.gwtbootstrap3.client.ui.constants.IconPosition;

import mat.client.measure.measuredetails.MeasureDetailState;

public class MeasureDetailsPopulationAnchorListItem extends MeasureDetailsAnchorListItem {
	public MeasureDetailsPopulationAnchorListItem(String text) {
		super(text);
		setIconPosition(IconPosition.LEFT);
	}
	
	public void setState(MeasureDetailState state) {}
}
