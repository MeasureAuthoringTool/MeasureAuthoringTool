package mat.client.measure.measuredetails.navigation;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.dom.client.Element;

import mat.client.measure.measuredetails.MeasureDetailState;

public class MeasureDetailsAnchorListItem extends AnchorListItem{
	private MeasureDetailState state;
	public MeasureDetailsAnchorListItem(String text) {
		super(text);
		this.state = MeasureDetailState.BLANK;
	}
	
	public void setState(MeasureDetailState state) {
		this.state = state;
		updateIcon();
	}
	
	private void updateIcon() {
		switch(this.state) {
		case BLANK:
			this.setIcon(null);
			break;
		case INCOMPLETE:
			this.setIcon(IconType.CHECK_CIRCLE_O);
			break;
		case COMPLETE:
			this.setIcon(IconType.CHECK_CIRCLE);
			this.setColor("#00FF00");
			break;
		}
		this.setIconPosition(IconPosition.RIGHT);
		Element anchorElement = this.getElement().getElementsByTagName("a").getItem(0);
		anchorElement.addClassName("measuredetails");
	}
}
