package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;


public class HelpButton extends Button{
	
	public HelpButton(String uniqueId, String name) {
		super();
		super.setId(uniqueId);
		super.setIcon(IconType.QUESTION_CIRCLE);
		super.setIconSize(IconSize.LARGE);
		super.setEnabled(false);
		super.setStylePrimaryName("helpbtn");
		
		
	}

	public void setHelpInformation(String info) {
		super.setTitle(info);
	}
	
	
}
