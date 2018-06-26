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
		getButton().setIcon(IconType.CLOSE);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}

}
