package mat.client.shared;



import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;


/**
 * The Class CQLLabelBar.
 */
public class CQLPopulationTopLevelButtonGroup {
	
	/** The Add New button. */
	private Button addNewButton = new Button();
	private Button saveButton = new Button();
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	public CQLPopulationTopLevelButtonGroup(String sectionName, String displayName, String saveButtonText, String addNewButtonText) {
		generateTopLevelButtonGroup(sectionName, displayName,saveButtonText, addNewButtonText);
	}
	
	
	private void generateTopLevelButtonGroup(String sectionName, String displayName, String saveButtonText, String addNewButtonText) {
		
		saveButton.setType(ButtonType.LINK);
		saveButton.setText(saveButtonText);		
		saveButton.setId("saveButton_" + sectionName);
		saveButton.setTitle("Click this button to save " + displayName);
		saveButton.setIcon(IconType.SAVE);
		saveButton.setPull(Pull.RIGHT);
		saveButton.setIconSize(IconSize.LARGE);
		
		
		addNewButton.setType(ButtonType.LINK);
		addNewButton.setText(addNewButtonText);	
		addNewButton.setId("addNewButton_" + sectionName);
		addNewButton.setTitle("Click this button to add a new " + displayName.substring(0, displayName.length()-1));
		addNewButton.setIcon(IconType.PLUS);
		addNewButton.setPull(Pull.LEFT);
		
		buttonGroup.add(addNewButton);
		buttonGroup.add(saveButton);
		
	}


	public Button getAddNewButton() {
		return addNewButton;
	}


	public Button getSaveButton() {
		return saveButton;
	}


	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}


	public void setAddNewButton(Button addNewButton) {
		this.addNewButton = addNewButton;
	}


	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}


	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}
	
	
	
}
