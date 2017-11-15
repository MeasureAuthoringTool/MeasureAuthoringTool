package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLGeneralInformationView.
 */
public class CQLGeneralInformationView {
	
	private String version = new String();
	private String libraryName = new String();
	private String modelUsed = new String();
	private String qdmVersionUsed = new String();
	
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
	
	/** The lib name group. */
	private FormGroup libNameGroup = new FormGroup();
	
	/** The using model group. */
	private FormGroup usingModelGroup = new FormGroup();
	
	/** The lib version group. */
	private FormGroup libVersionGroup = new FormGroup();
	
	/** The model version group. */
	private FormGroup modelVersionGroup = new FormGroup();
	
	HTML heading = new HTML();
	
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
		
		libNameGroup.clear();
		usingModelGroup.clear();
		libVersionGroup.clear();
		modelVersionGroup.clear();
		heading.addStyleName("leftAligned");
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		//Label libraryNameLabel = new Label(LabelType.INFO, "CQL Library Name");
		FormLabel libraryNameLabel = new FormLabel();
		libraryNameLabel.setText("CQL Library Name");
		libraryNameLabel.setTitle("CQL Library Name");
		libraryNameLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		libraryNameLabel.setWidth("150px");
		libraryNameLabel.setId("libraryNameLabel_Label");
		libraryNameLabel.setFor("libraryNameValue_TextBox");
		
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;");
		//libraryNameValue.setText(createCQLLibraryName(MatContext.get().getCurrentMeasureName()));
		libraryNameValue.getElement().setId("libraryNameValue_TextBox");
		
		libNameGroup.add(libraryNameLabel);
		//libNameGroup.add(new SpacerWidget());
		libNameGroup.add(libraryNameValue);
		
		//Label libraryVersionLabel = new Label(LabelType.INFO, "Version");
		FormLabel libraryVersionLabel = new FormLabel();
		libraryVersionLabel.setText("Version");
		libraryVersionLabel.setTitle("Version");
		libraryVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		libraryVersionLabel.setWidth("150px");
		libraryVersionLabel.setId("libraryVersionLabel_Label");
		libraryVersionLabel.setFor("libraryVersionValue_TextBox");
		
		libraryVersionValue.getElement().setAttribute("style", "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;");
		libraryVersionValue.getElement().setId("libraryVersionValue_TextBox");
		libraryVersionValue.setReadOnly(true);
		
	     libVersionGroup.add(libraryVersionLabel);
	   //  libVersionGroup.add(new SpacerWidget());
	     libVersionGroup.add(libraryVersionValue);
		
		//Label usingModeLabel = new Label(LabelType.INFO, "Using Model");
		FormLabel usingModeLabel = new FormLabel();
		usingModeLabel.setText("Using Model");
		usingModeLabel.setTitle("Using Model");
		usingModeLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		usingModeLabel.setId("usingModeLabel_Label");
		usingModeLabel.setWidth("150px");
		usingModeLabel.setFor("usingModelValue_TextBox");
		
		usingModelValue.getElement().setAttribute("style", "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;");
		usingModelValue.getElement().setId("usingModelValue_TextBox");
		usingModelValue.setReadOnly(true);
		
		usingModelGroup.add(usingModeLabel);
		//usingModelGroup.add(new SpacerWidget());
		usingModelGroup.add(usingModelValue);
		
		//Label modelVersionLabel = new Label(LabelType.INFO, "Version");
		FormLabel modelVersionLabel = new FormLabel();
		modelVersionLabel.setText("Version");
		modelVersionLabel.setTitle("Version");
		modelVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		modelVersionLabel.getElement().setId("modelVersionLabel_Label");
		modelVersionLabel.setWidth("150px");
		modelVersionLabel.setFor("modelVersionValue_TextBox");
		
		modelVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:32px;");
		modelVersionValue.getElement().setId("modelVersionValue_TextBox");
		modelVersionValue.setReadOnly(true);
		
		modelVersionGroup.add(modelVersionLabel);
		//modelVersionGroup.add(new SpacerWidget());
		modelVersionGroup.add(modelVersionValue);
		
		generalInfoTopPanel.add(heading);
		
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		
		//generalInfoTopPanel.add(libraryNameLabel);
		//generalInfoTopPanel.add(new SpacerWidget());
		//generalInfoTopPanel.add(libraryNameValue);
		generalInfoTopPanel.add(libNameGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		/*generalInfoTopPanel.add(libraryVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(libraryVersionValue);*/
		generalInfoTopPanel.add(libVersionGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		/*generalInfoTopPanel.add(usingModeLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(usingModelValue);
		*/
		generalInfoTopPanel.add(usingModelGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		/*generalInfoTopPanel.add(modelVersionLabel);
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(modelVersionValue);*/
		generalInfoTopPanel.add(modelVersionGroup);
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
	
	/**
	 * Reset all.
	 */
	public void resetAll(){
		getLibraryNameValue().setText("");
		getLibraryVersionValue().setText("");
		getUsingModelValue().setText("");
		getModelVersionValue().setText("");
	}
	
	/**
	 * Reset form group.
	 */
	public void resetFormGroup(){
		getLibNameGroup().setValidationState(ValidationState.NONE);
	}

	/**
	 * Gets the lib name group.
	 *
	 * @return the lib name group
	 */
	public FormGroup getLibNameGroup() {
		return libNameGroup;
	}

	/**
	 * Sets the lib name group.
	 *
	 * @param libNameGroup the new lib name group
	 */
	public void setLibNameGroup(FormGroup libNameGroup) {
		this.libNameGroup = libNameGroup;
	}

	/**
	 * Gets the using model group.
	 *
	 * @return the using model group
	 */
	public FormGroup getUsingModelGroup() {
		return usingModelGroup;
	}

	/**
	 * Sets the using model group.
	 *
	 * @param usingModelGroup the new using model group
	 */
	public void setUsingModelGroup(FormGroup usingModelGroup) {
		this.usingModelGroup = usingModelGroup;
	}

	/**
	 * Gets the lib version group.
	 *
	 * @return the lib version group
	 */
	public FormGroup getLibVersionGroup() {
		return libVersionGroup;
	}

	/**
	 * Sets the lib version group.
	 *
	 * @param libVersionGroup the new lib version group
	 */
	public void setLibVersionGroup(FormGroup libVersionGroup) {
		this.libVersionGroup = libVersionGroup;
	}

	/**
	 * Gets the model version group.
	 *
	 * @return the model version group
	 */
	public FormGroup getModelVersionGroup() {
		return modelVersionGroup;
	}

	/**
	 * Sets the model version group.
	 *
	 * @param modelVersionGroup the new model version group
	 */
	public void setModelVersionGroup(FormGroup modelVersionGroup) {
		this.modelVersionGroup = modelVersionGroup;
	}

	public void setGeneralInfoOfLibrary(String libraryName, String version, String qdmVersion , String modelUsed) {
		this.libraryName = libraryName;
		this.modelUsed = modelUsed;
		this.version = version;
		this.qdmVersionUsed = qdmVersion;
		getLibraryVersionValue().setText(version);
		getUsingModelValue().setText(modelUsed);
		getModelVersionValue().setText(qdmVersion);
		getLibraryNameValue().setText(libraryName);
		
	}
	
	public void clearAllGeneralInfoOfLibrary(){
		this.libraryName = "";
		this.modelUsed = "";
		this.version = "";
		this.qdmVersionUsed = "";
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

}
