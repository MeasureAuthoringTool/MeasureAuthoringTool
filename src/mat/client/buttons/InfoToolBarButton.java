package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

public class InfoToolBarButton extends GenericToolbarButton{

	public InfoToolBarButton(String sectionName) {
		super("Information", sectionName);
		setIcon();
		setSize();
		getButton().setTitle("Click to view available short cut keys information");
		getButton().setText("Information");
		getButton().getElement().setAttribute("aria-label", "Click to view available short cut keys information");
		getButton().setToggleCaret(false);
		getButton().setDataToggle(Toggle.DROPDOWN);
	}

	@Override
	public void setIcon() {
		getButton().setIcon(IconType.INFO_CIRCLE);
	}

	@Override
	public void setSize() {
		getButton().setSize("120px", "30px");
	}

}
