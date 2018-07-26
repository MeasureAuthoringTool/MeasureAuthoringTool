package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class RedButton extends GenericButton{
	
	public RedButton(String id, String title) {
		super(title+"_"+id, title);
		super.setType(ButtonType.DANGER);
	}

}