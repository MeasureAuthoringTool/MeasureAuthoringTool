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
		super.setIcon(IconType.FILE_O);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
