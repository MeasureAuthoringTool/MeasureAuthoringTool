package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class CancelToolBarButton extends GenericToolbarButton{

	public CancelToolBarButton(String sectionName) {
		super("Cancel", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.CLOSE);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
