package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class EraseToolBarButton extends GenericToolbarButton{

	public EraseToolBarButton(String sectionName) {
		super("Erase", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.ERASER);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
