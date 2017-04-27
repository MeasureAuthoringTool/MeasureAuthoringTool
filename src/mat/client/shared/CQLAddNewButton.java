package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;


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
		
		addNewButton = new Button("+ Add New");
		addNewButton.setType(ButtonType.LINK);
		addNewButton.setSize(ButtonSize.SMALL);
		addNewButton.setTitle("Add New");
		addNewButton.setStyleName("btn-link");
		addNewButton.getElement().setAttribute("aria-label", "Add New");
		addNewButton.getElement().setId("addNewButton_"+sectionName);
		
		initWidget(addNewButton);
		
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
