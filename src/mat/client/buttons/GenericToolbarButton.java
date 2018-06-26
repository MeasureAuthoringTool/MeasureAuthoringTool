package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;

/**
 * This class represents the generic form of a button
 * on the Copy/Paste/SelectAll/Clear tool bar
 */
public abstract class GenericToolbarButton extends Button{
	private Button button = new Button();
	
	/**
	 * @param name	Name of button ('copy', 'paste', 'clear'...etc)
	 * @param sectionName 	where the button tool bar is located (what page)
	 */
	public GenericToolbarButton(String name, String sectionName) {
		button.setType(ButtonType.LINK);
		button.getElement().setId((name.toLowerCase().replace(" ", ""))+"Button_"+sectionName);
		button.setMarginTop(10);
		button.setTitle(name);
		button.setText(name);
		button.setIconSize(IconSize.LARGE);
		button.setColor("#0964A2");
		button.getElement().setAttribute("aria-label", name);
	}
		
	public Button getButton() {
		return button;
	}
	
	public abstract void setIcon();
	public abstract void setSize();
	
}
