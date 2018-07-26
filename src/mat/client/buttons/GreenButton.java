package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class GreenButton extends GenericButton{
	
	public GreenButton(String uniqueId, String title) {
		super(title+"_"+uniqueId, title);
		super.setType(ButtonType.SUCCESS);
		super.setText(title);
		super.setTitle(title);
	}

}