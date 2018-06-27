package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;

/**
 * This class represents the generic form of a button
 * on the Copy/Paste/SelectAll/Clear tool bar
 */
public abstract class GenericToolbarButton extends Button{
	
	/**
	 * @param name	Name of button ('copy', 'paste', 'clear'...etc)
	 * @param sectionName 	where the button tool bar is located (what page)
	 */
	public GenericToolbarButton(String name, String sectionName) {
		super.setType(ButtonType.LINK);
		super.getElement().setId((name.toLowerCase().replace(" ", ""))+"Button_"+sectionName);
		super.setMarginTop(10);
		super.setTitle(name);
		super.setText(name);
		super.setIconSize(IconSize.LARGE);
		super.setColor("#0964A2");
		super.getElement().setAttribute("aria-label", name);
	}
	
	public abstract void setIcon();
	public abstract void setSize();
	
}
