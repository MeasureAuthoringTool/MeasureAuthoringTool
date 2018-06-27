package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class DeleteToolBarButton extends GenericToolbarButton{

	public DeleteToolBarButton(String sectionName) {
		super("Delete", sectionName);
		setIcon();
		setSize();
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.TRASH);
	}

	@Override
	public void setSize() {
		super.setSize("70px", "30px");
	}

}
