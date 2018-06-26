package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class ClearToolBarButton extends GenericToolbarButton{

	public ClearToolBarButton(String sectionName) {
		super("Clear", sectionName);
	}

	@Override
	public void setIcon() {
		getButton().setIcon(IconType.ERASER);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}
}
