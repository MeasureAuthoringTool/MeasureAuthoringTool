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
		getButton().setIcon(IconType.EDIT);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}

}
