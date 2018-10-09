package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class DeleteButton extends Button {

	public DeleteButton(String sectionName) {
		this(sectionName, "Delete");
	}
	
	public DeleteButton(String sectionName, String title) {
		super();
		super.setType(ButtonType.DANGER);
		super.setIcon(IconType.TRASH);
		super.setTitle(title);
		super.setText(title);
	}
}
