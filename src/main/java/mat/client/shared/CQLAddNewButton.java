package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;


// TODO: Auto-generated Javadoc
/**
 * The Class CQLLabelBar.
 */
public class CQLAddNewButton extends Composite {
	
	/** The Add New button. */
	private Button addNewButton;
	
	/**
	 * Instantiates a new CQL Button tool bar.
	 *
	 * @param sectionName the section name
	 */
	public CQLAddNewButton(String sectionName) {
		VerticalPanel vp = new VerticalPanel();
		addNewButton = new Button();
		
		addNewButton.setType(ButtonType.LINK);
		addNewButton.getElement().setId("addNewButton_"+sectionName);
		//addNewButton.getElement().setAttribute("style", "padding-left:600px");
		addNewButton.setTitle("Click this button to add a new " + sectionName);
		addNewButton.setText("Add New");
		addNewButton.setId("Add_New_ID");
		addNewButton.setIcon(IconType.PLUS);
		addNewButton.setSize(ButtonSize.SMALL);
		addNewButton.setPull(Pull.RIGHT);
		vp.add(addNewButton);
		vp.setWidth("95%");
		initWidget(vp);
		
	}
	
	
	/**
	 * Sets the enabled.
	 *
	 * @param isEnabled the new enabled
	 */
	public void setEnabled(boolean isEnabled){
		addNewButton.setEnabled(isEnabled);
	}
	
	/**
	 * Gets the add new button.
	 *
	 * @return the add new button
	 */
	public Button getaddNewButton() {
		return addNewButton;
	}
	
}
