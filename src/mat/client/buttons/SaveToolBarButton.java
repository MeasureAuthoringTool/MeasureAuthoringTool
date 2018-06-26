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
		getButton().setIcon(IconType.SAVE);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}

}
