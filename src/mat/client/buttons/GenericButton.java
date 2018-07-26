package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;

public class GenericButton extends Button{

	public GenericButton(String id, String title) {
		super();
		super.setId(id);
		super.setTitle(title);
		super.setText(title);
	}
	
}