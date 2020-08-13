package mat.client.measure.measuredetails.navigation;

import com.google.gwt.dom.client.Element;
import mat.client.measure.measuredetails.MeasureDetailState;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

public class MeasureDetailsAnchorListItem extends AnchorListItem{
	private MeasureDetailState state;
	private Badge badge;
	public MeasureDetailsAnchorListItem(String text) {
		super(text);
		badge = new Badge();
		badge.addStyleName("measureDetailsBadge");
	}
	
	public void setState(MeasureDetailState state) {
		this.state = state;
		updateIcon();
	}
	
	public void updateIcon() {
		Icon icon = new Icon();
		icon.setSize(IconSize.LARGE);
		Element anchorElement = this.getElement().getElementsByTagName("a").getItem(0);
		badge.setPull(Pull.RIGHT);
		switch(this.state) {
		case BLANK:
			break;
		case INCOMPLETE:
			icon.setType(IconType.CHECK_CIRCLE_O);
			icon.setColor("#b06500");
			badge.add(icon.asWidget());
			this.add(badge, anchorElement);
			break;
		case COMPLETE:
			icon.setType(IconType.CHECK_CIRCLE);
			icon.setColor("#449d44");
			badge.add(icon.asWidget());
			this.add(badge, anchorElement);
			break;
		}
	}
}
