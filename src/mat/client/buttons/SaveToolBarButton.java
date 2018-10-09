package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class SaveToolBarButton extends GenericToolbarButton{

	public SaveToolBarButton(String sectionName) {
		super("Save", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.SAVE);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
