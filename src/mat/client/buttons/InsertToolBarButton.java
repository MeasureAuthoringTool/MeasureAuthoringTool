package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class InsertToolBarButton extends GenericToolbarButton{

	public InsertToolBarButton(String sectionName) {
		super("Insert", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.PLUS_SQUARE);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
