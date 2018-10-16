package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class SelectAllToolBarButton extends GenericToolbarButton{

	public SelectAllToolBarButton(String sectionName) {
		super("Select All", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.CHECK_SQUARE);
	}

	@Override
	public void setSize() {
		super.setSize("95px", "30px");
	}

}
