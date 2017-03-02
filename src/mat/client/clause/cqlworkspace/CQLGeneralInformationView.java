package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLGeneralInformationView.
 */
public class CQLGeneralInformationView {
	
	/** The general info main V panel. */
	private HorizontalPanel generalInfoMainHPanel = new HorizontalPanel();
	
	/** The library name value. */
	private MatTextBox libraryNameValue = new MatTextBox();
	
	/** The library version value. */
	private MatTextBox libraryVersionValue = new MatTextBox();
	
	/** The using model value. */
	private MatTextBox usingModelValue = new MatTextBox();
	
	/** The model version value. */
	private MatTextBox modelVersionValue = new MatTextBox();
	
	/** The save and delete btn. */
	private CQLButtonToolBar saveAndDeleteBtn = new CQLButtonToolBar("GeneralInfo");
	
	/**
	 * Instantiates a new CQL general information view.
	 */
	public CQLGeneralInformationView(){
		generalInfoMainHPanel.clear();
	}

	/**
	 * Builds the view.
	 */
	private void buildView() {
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		Label libraryNameLabel = new Label(LabelType.INFO, "CQL Library Name");
		
		libraryNameLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		libraryNameLabel.setWidth("150px");
		libraryNameLabel.getElement().setId("libraryNameLabel_Label");
		
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		//libraryNameValue.setText(createCQLLibraryName(MatContext.get().getCurrentMeasureName()));
		libraryNameValue.getElement().setId("libraryNameValue_TextBox");
		Label libraryVersionLabel = new Label(LabelType.INFO, "Version");
		
		libraryVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		libraryVersionLabel.setWidth("150px");
		libraryVersionLabel.getElement().setId("libraryVersionLabel_Label");
		
		libraryVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		libraryVersionValue.getElement().setId("libraryVersionValue_TextBox");
		libraryVersionValue.setReadOnly(true);
		
		Label usingModeLabel = new Label(LabelType.INFO, "Using Model");
		
		usingModeLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		usingModeLabel.getElement().setId("usingModeLabel_Label");
		usingModeLabel.setWidth("150px");
		usingModelValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		usingModelValue.getElement().setId("usingModelValue_TextBox");
		usingModelValue.setReadOnly(true);
		
		Label modelVersionLabel = new Label(LabelType.INFO, "Version");
		
		modelVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;background-color:#0964A2;");
		modelVersionLabel.getElement().setId("modelVersionLabel_Label");
		modelVersionLabel.setWidth("150px");
		
		modelVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:25px;");
		modelVersionValue.getElement().setId("modelVersionValue_TextBox");
		modelVersionValue.setReadOnly(true);
		
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryNameLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryNameValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryVersionValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(usingModeLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModelValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(modelVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(modelVersionValue);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoMainHPanel.setStyleName("cqlRightContainer");
		generalInfoMainHPanel.getElement().setId("generalInfoMainHPanel_HPanel");
		generalInfoMainHPanel.setWidth("715px");
		generalInfoTopPanel.setStyleName("marginLeft15px");
		generalInfoMainHPanel.add(generalInfoTopPanel);
	}
	
	
	
	/**
	 * Builds the button layout panel.
	 */
	public void buildButtonLayoutPanel(){
		
		saveAndDeleteBtn.getSaveButton().setVisible(true);
		saveAndDeleteBtn.getCloseButton().setVisible(true);
		saveAndDeleteBtn.getEraseButton().removeFromParent();
		saveAndDeleteBtn.getDeleteButton().removeFromParent();
		saveAndDeleteBtn.getInfoButton().removeFromParent();
		saveAndDeleteBtn.getInsertButton().removeFromParent();
		saveAndDeleteBtn.getTimingExpButton().removeFromParent();
		saveAndDeleteBtn.setStylePrimaryName("floatLeft");
		generalInfoMainHPanel.add(saveAndDeleteBtn);
	}
	
	
	
	/**
	 * This method will take a String and remove all non-alphabet/non-numeric characters 
	 * except underscore ("_") characters.
	 *
	 * @param originalString the original string
	 * @return cleanedString
	 */
	public String createCQLLibraryName(String originalString) {
		originalString = originalString.replaceAll(" ", "");
		String cleanedString = "";
				
		for(int i=0;i<originalString.length();i++){
			char c = originalString.charAt(i);
			int intc = (int)c;
			if(c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)){
				
				if(!(cleanedString.isEmpty() && Character.isDigit(c))){
					cleanedString = cleanedString + "" + c;
				}
				
			} 
			
		}
		
		return cleanedString;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public HorizontalPanel getView(){
		generalInfoMainHPanel.clear();
		buildView();
		setWidgetReadOnly(false);
		return generalInfoMainHPanel;
	}
	
	/**
	 * Gets the CQL view.
	 *
	 * @return the CQL view
	 */
	public HorizontalPanel getCQLView(){
		generalInfoMainHPanel.clear();
		buildView();	
		buildButtonLayoutPanel();
		setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		return generalInfoMainHPanel;
	}

	/**
	 * Gets the library name value.
	 *
	 * @return the library name value
	 */
	public MatTextBox getLibraryNameValue() {
		return libraryNameValue;
	}

	/**
	 * Sets the library name value.
	 *
	 * @param libraryNameValue the new library name value
	 */
	public void setLibraryNameValue(MatTextBox libraryNameValue) {
		this.libraryNameValue = libraryNameValue;
	}

	/**
	 * Gets the library version value.
	 *
	 * @return the library version value
	 */
	public MatTextBox getLibraryVersionValue() {
		return libraryVersionValue;
	}

	/**
	 * Sets the library version value.
	 *
	 * @param libraryVersionValue the new library version value
	 */
	public void setLibraryVersionValue(MatTextBox libraryVersionValue) {
		this.libraryVersionValue = libraryVersionValue;
	}

	/**
	 * Gets the using model value.
	 *
	 * @return the using model value
	 */
	public MatTextBox getUsingModelValue() {
		return usingModelValue;
	}

	/**
	 * Sets the using model value.
	 *
	 * @param usingModelValue the new using model value
	 */
	public void setUsingModelValue(MatTextBox usingModelValue) {
		this.usingModelValue = usingModelValue;
	}

	/**
	 * Gets the model version value.
	 *
	 * @return the model version value
	 */
	public MatTextBox getModelVersionValue() {
		return modelVersionValue;
	}

	/**
	 * Sets the model version value.
	 *
	 * @param modelVersionValue the new model version value
	 */
	public void setModelVersionValue(MatTextBox modelVersionValue) {
		this.modelVersionValue = modelVersionValue;
	}
	
	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	public Button getSaveButton(){
		return saveAndDeleteBtn.getSaveButton();
	}
	
	/**
	 * Gets the cancel button.
	 *
	 * @return the cancel button
	 */
	public Button getCancelButton(){
		return saveAndDeleteBtn.getCloseButton();
	}
	
	/**
	 * Sets the widget read only.
	 *
	 * @param isEditable the new widget read only
	 */
	public void setWidgetReadOnly(boolean isEditable){
		getLibraryNameValue().setReadOnly(!isEditable);
		getSaveButton().setEnabled(isEditable);
		getCancelButton().setEnabled(isEditable);

	}

}
