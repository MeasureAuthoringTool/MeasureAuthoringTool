package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class PasteToolBarButton extends GenericToolbarButton{

	public PasteToolBarButton(String sectionName) {
		super("Paste", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.CLIPBOARD);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
