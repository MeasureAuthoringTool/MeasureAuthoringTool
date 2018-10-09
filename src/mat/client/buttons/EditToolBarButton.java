package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class EditToolBarButton extends GenericToolbarButton{

	public EditToolBarButton(String sectionName) {
		super("Edit", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.EDIT);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
