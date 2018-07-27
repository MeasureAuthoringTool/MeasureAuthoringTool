package mat.client.buttons;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DropDown;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.dom.client.Style.Position;


public class HelpButton2 extends DropDown {
	private Button button = new Button();
	private AnchorListItem message  = new AnchorListItem();
	private DropDownMenu dropdown = new DropDownMenu();
	
	public HelpButton2(String uniqueId, String name) {
		super();
		button.setId(uniqueId);
		button.setIcon(IconType.QUESTION_CIRCLE);
		button.setIconSize(IconSize.LARGE);
		button.setTitle(name + " help button");
		button.getElement().getStyle().setBorderColor("transparent");
		button.getElement().getStyle().setBackgroundColor("transparent");
		button.setToggleCaret(false);
		button.setDataToggle(Toggle.DROPDOWN);
		dropdown.getElement().setAttribute("style", "font-size:small;");
		super.add(button);
		dropdown.add(message);
		super.add(dropdown);	
		super.getElement().getStyle().setPosition(Position.ABSOLUTE);
	}
	
	public void setHelpInformation(String info) {
		message.setText(info);
		message.setTitle(info);
	}
}
