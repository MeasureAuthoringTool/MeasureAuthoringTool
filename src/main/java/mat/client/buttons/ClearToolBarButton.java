package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class ClearToolBarButton extends GenericToolbarButton{

	public ClearToolBarButton(String sectionName) {
		super("Clear", sectionName);
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
