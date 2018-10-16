package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

public class InfoToolBarButton extends GenericToolbarButton{

	public InfoToolBarButton(String sectionName) {
		super("Information", sectionName);
		setIcon();
		setSize();
		super.setTitle("Click to view available short cut keys information");
		super.setText("Information");
		super.getElement().setAttribute("aria-label", "Click to view available short cut keys information");
		super.setToggleCaret(false);
		super.setDataToggle(Toggle.DROPDOWN);
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.INFO_CIRCLE);
	}

	@Override
	public void setSize() {
		super.setSize("120px", "30px");
	}

}
