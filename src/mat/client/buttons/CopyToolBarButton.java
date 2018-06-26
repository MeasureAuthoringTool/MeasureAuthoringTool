package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class CopyToolBarButton extends GenericToolbarButton{

	public CopyToolBarButton(String sectionName) {
		super("Copy", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		getButton().setIcon(IconType.FILE_O);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}

}
