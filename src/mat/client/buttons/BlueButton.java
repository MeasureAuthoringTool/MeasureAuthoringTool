package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class BlueButton extends GenericButton {
	
	public BlueButton(String uniqueId, String title) {
		super(title+"_"+uniqueId, title);
		super.setType(ButtonType.PRIMARY);
		super.getElement().setAttribute("background-color", "#337ab7");
		super.getElement().setAttribute("color", "white");
		super.getElement().setAttribute("border-color", "#2e6da4");
	}

}