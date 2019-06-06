package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

public class CQLPopulationTopLevelButtonGroup {
	private static final String CLICK_TEXT = "Click this button to ";
	
	private Button addNewButton = new Button();
	private Button saveButton = new Button();
	
	public CQLPopulationTopLevelButtonGroup(String sectionName, String displayName, String saveButtonText, String addNewButtonText, double marginRight) {
		generateTopLevelButtonGroup(sectionName, displayName,saveButtonText, addNewButtonText, marginRight);
	}
	
	private void generateTopLevelButtonGroup(String sectionName, String displayName, String saveButtonText, String addNewButtonText, double marginRight) {
		
		buildSaveButton(sectionName, displayName, saveButtonText, marginRight);
		buildAddNewButton(sectionName, displayName, addNewButtonText, marginRight);
	}

	private void buildAddNewButton(String sectionName, String displayName, String addNewButtonText, double marginRight) {
		addNewButton.setType(ButtonType.LINK);
		addNewButton.setText(addNewButtonText);	
		addNewButton.setId("addNewButton_" + sectionName);
		addNewButton.setTitle(CLICK_TEXT + buildTitle(displayName));
		addNewButton.setIcon(IconType.PLUS);
		addNewButton.setPull(Pull.RIGHT);
		addNewButton.setMarginRight(marginRight);
	}

	private String buildTitle(String displayName) {
		return (displayName != null && !displayName.isEmpty()) ? " add a new " + displayName.substring(0, displayName.length()-1) : " add new";
	}
	
	private void buildSaveButton(String sectionName, String displayName, String saveButtonText, double marginRight) {
		saveButton.setType(ButtonType.LINK);
		saveButton.setText(saveButtonText);		
		saveButton.setId("saveButton_" + sectionName);
		saveButton.setTitle(CLICK_TEXT + "save " + displayName);
		saveButton.setIcon(IconType.SAVE);
		saveButton.setPull(Pull.RIGHT);
		saveButton.setMarginRight(marginRight);
		saveButton.setIconSize(IconSize.LARGE);
	}

	public Button getAddNewButton() {
		return addNewButton;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setAddNewButton(Button addNewButton) {
		this.addNewButton = addNewButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}
	
}
