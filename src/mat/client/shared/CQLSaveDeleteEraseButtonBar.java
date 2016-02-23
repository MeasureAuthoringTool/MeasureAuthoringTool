package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * The Class CQLSaveDeleteEraseButtonBar.
 */
public class CQLSaveDeleteEraseButtonBar extends Composite{
	
	
	/** The save button. */
	private Button saveButton = new Button();
	
	/** The delete button. */
	private Button deleteButton = new Button();
	
	/** The erase button. */
	private Button eraseButton = new Button();
	
	
	/**
	 * Instantiates a new CQL save delete erase button bar.
	 */
	public CQLSaveDeleteEraseButtonBar() {
		
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout continueButton");
		
        saveButton.setType(ButtonType.LINK);
        saveButton.getElement().setId("saveButton_Button");
        saveButton.setMarginTop(10);
        saveButton.setTitle("Save");
        saveButton.setIcon(IconType.SAVE);
        saveButton.setIconSize(IconSize.LARGE);
        saveButton.setColor("#00BFFF");
        saveButton.setSize("30px", "30px");
		
        deleteButton.setType(ButtonType.LINK);
        deleteButton.getElement().setId("deleteButton_Button");
        deleteButton.setMarginTop(10);
        deleteButton.setTitle("Delete");
        deleteButton.setIcon(IconType.TRASH);
        deleteButton.setIconSize(IconSize.LARGE);
        deleteButton.setColor("red");
        deleteButton.setSize("30px", "30px");
		
        eraseButton.setType(ButtonType.LINK);
        eraseButton.getElement().setId("eraseButton_Button");
        eraseButton.setMarginTop(10);
        eraseButton.setTitle("Erase");
        eraseButton.setIcon(IconType.ERASER);
        eraseButton.setIconSize(IconSize.LARGE);
        eraseButton.setColor("#0964A2");
        eraseButton.setSize("30px", "30px");
		
		saveButton.setTitle("Save");
		deleteButton.setTitle("Delete");
		eraseButton.setTitle("Erase");
		buttonLayout.add(saveButton);
		buttonLayout.add(deleteButton);
		buttonLayout.add(eraseButton);
		initWidget(buttonLayout);
		
	}


	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	public Button getSaveButton() {
		return saveButton;
	}


	/**
	 * Gets the delete button.
	 *
	 * @return the delete button
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}


	/**
	 * Gets the erase button.
	 *
	 * @return the erase button
	 */
	public Button getEraseButton() {
		return eraseButton;
	}

}
