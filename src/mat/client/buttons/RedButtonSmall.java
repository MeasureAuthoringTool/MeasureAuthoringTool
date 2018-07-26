package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.ButtonSize;

public class RedButtonSmall extends RedButton{

	public RedButtonSmall(String sectionName, String title) {
		super(title+"_"+sectionName, title);
		super.setSize(ButtonSize.SMALL);
	}
}