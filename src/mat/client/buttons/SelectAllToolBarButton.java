package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class SelectAllToolBarButton extends GenericToolbarButton{

	public SelectAllToolBarButton(String sectionName) {
		super("Clear", sectionName);
	}

	@Override
	public void setIcon() {
		getButton().setIcon(IconType.CHECK_SQUARE);
	}

	@Override
	public void setSize() {
		getButton().setSize("95px", "30px");
	}

}
