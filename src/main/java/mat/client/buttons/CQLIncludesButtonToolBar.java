package mat.client.buttons;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;

public class CQLIncludesButtonToolBar extends Composite {

	private Button saveButton;
	private Button eraseButton;
	
	private Button replaceButton; 
	private Button deleteButton; 
	private Button cancelButton; 

	private HorizontalPanel buttonLayout = new HorizontalPanel();
	
	private ButtonGroup includesBtnGroup = new ButtonGroup();

	private String sectionName;
	
	public CQLIncludesButtonToolBar(String sectionName, boolean isIncludesInitialView) {
		this.sectionName = sectionName;
		
		buttonLayout.getElement().setId("cql_buttonLayout_HorizontalPanel");		

		if(isIncludesInitialView) {
			addSaveButton();
			addEraseButton();
		} else {
			addReplaceButton();
			addDeleteButton();
			addCancelButton();
		}
		
		initWidget(buttonLayout);	
	}

	private void addCancelButton() {
		cancelButton = new CancelToolBarButton(sectionName);
		buttonLayout.add(cancelButton);
	}
	
	private void addEraseButton() {
		eraseButton = new EraseToolBarButton(sectionName);
		buttonLayout.add(eraseButton);
	}
	
	private void addDeleteButton() {
		deleteButton = new DeleteToolBarButton(sectionName);
		buttonLayout.add(deleteButton);
	}
	
	private void addReplaceButton() {
		replaceButton = new EditToolBarButton(sectionName);
		buttonLayout.add(replaceButton);
	}
	
	private void addSaveButton() {
		saveButton = new SaveToolBarButton(sectionName);
		buttonLayout.add(saveButton);
	}
	
	public Button getSaveButton() {
		return saveButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public Button getEraseButton() {
		return eraseButton;
	}

	public Button getCancelButton(){
		return cancelButton;
	}

	public ButtonGroup getInfoButtonGroup() {
		return includesBtnGroup;
	}

	public Button getReplaceButton() {
		return replaceButton;
	}
	
	public HorizontalPanel getButtonLayout() {
		return buttonLayout;
	}

}
