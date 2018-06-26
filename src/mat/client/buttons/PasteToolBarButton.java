package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class PasteToolBarButton extends GenericToolbarButton{

	public PasteToolBarButton(String sectionName) {
		super("Paste", sectionName);
	}

	@Override
	public void setIcon() {
		getButton().setIcon(IconType.CLIPBOARD);
	}

	@Override
	public void setSize() {
		getButton().setSize("70px", "30px");
	}

}
