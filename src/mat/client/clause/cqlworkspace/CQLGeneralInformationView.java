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
	
	private HorizontalPanel generalInfoMainHPanel = new HorizontalPanel();
	private MatTextBox libraryNameValue = new MatTextBox();
	private MatTextBox libraryVersionValue = new MatTextBox();
	private MatTextBox usingModelValue = new MatTextBox();
	private MatTextBox modelVersionValue = new MatTextBox();
	private CQLButtonToolBar saveAndDeleteBtn = new CQLButtonToolBar("GeneralInfo");
	private FormGroup libNameGroup = new FormGroup();
	private FormGroup usingModelGroup = new FormGroup();
	private FormGroup libVersionGroup = new FormGroup();
	private FormGroup modelVersionGroup = new FormGroup();
	HTML heading = new HTML();
	
	/**
	 * Instantiates a new CQL general information view.
	 */
	public CQLGeneralInformationView(){
		generalInfoMainHPanel.clear();
	}

	private void buildView() {
		
		libNameGroup.clear();
		usingModelGroup.clear();
		libVersionGroup.clear();
		modelVersionGroup.clear();
		heading.addStyleName("leftAligned");
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		FormLabel libraryNameLabel = new FormLabel();
		libraryNameLabel.setText("CQL Library Name");
		libraryNameLabel.setTitle("CQL Library Name");
		libraryNameLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		libraryNameLabel.setWidth("150px");
		libraryNameLabel.setId("libraryNameLabel_Label");
		libraryNameLabel.setFor("libraryNameValue_TextBox");
		
		libraryNameValue.getElement().setAttribute("style", "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;");
		libraryNameValue.getElement().setId("libraryNameValue_TextBox");
		
		libNameGroup.add(libraryNameLabel);
		libNameGroup.add(libraryNameValue);
		
		FormLabel libraryVersionLabel = new FormLabel();
		libraryVersionLabel.setText("CQL Library Version");
		libraryVersionLabel.setTitle("CQL Library Version");
		libraryVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		libraryVersionLabel.setWidth("150px");
		libraryVersionLabel.setId("libraryVersionLabel_Label");
		libraryVersionLabel.setFor("libraryVersionValue_TextBox");
		
		libraryVersionValue.getElement().setAttribute("style", "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;");
		libraryVersionValue.getElement().setId("libraryVersionValue_TextBox");
		libraryVersionValue.setReadOnly(true);
		
	     libVersionGroup.add(libraryVersionLabel);
	     libVersionGroup.add(libraryVersionValue);
		
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
		usingModelGroup.add(usingModelValue);
		
		FormLabel modelVersionLabel = new FormLabel();
		modelVersionLabel.setText("Model Version");
		modelVersionLabel.setTitle("Model Version");
		modelVersionLabel.getElement().setAttribute("style", "font-size:90%;margin-left:15px;");
		modelVersionLabel.getElement().setId("modelVersionLabel_Label");
		modelVersionLabel.setWidth("150px");
		modelVersionLabel.setFor("modelVersionValue_TextBox");
		
		modelVersionValue.getElement().setAttribute("style", "margin-left:15px;width:250px;height:32px;");
		modelVersionValue.getElement().setId("modelVersionValue_TextBox");
		modelVersionValue.setReadOnly(true);
		
		modelVersionGroup.add(modelVersionLabel);
		modelVersionGroup.add(modelVersionValue);
		
		generalInfoTopPanel.add(heading);
		
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libNameGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libVersionGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(usingModelGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(modelVersionGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoMainHPanel.setStyleName("cqlRightContainer");
		generalInfoMainHPanel.getElement().setId("generalInfoMainHPanel_HPanel");
		generalInfoMainHPanel.setWidth("715px");
		generalInfoTopPanel.setStyleName("marginLeft15px");
		generalInfoMainHPanel.add(generalInfoTopPanel);
	}
	
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

	public HorizontalPanel getView(){
		generalInfoMainHPanel.clear();
		buildView();
		setWidgetReadOnly(false);
		return generalInfoMainHPanel;
	}
	
	public HorizontalPanel getCQLView(){
		generalInfoMainHPanel.clear();
		buildView();	
		buildButtonLayoutPanel();
		setWidgetReadOnly(MatContext.get().getLibraryLockService().checkForEditPermission());
		return generalInfoMainHPanel;
	}

	public MatTextBox getLibraryNameValue() {
		return libraryNameValue;
	}

	public void setLibraryNameValue(MatTextBox libraryNameValue) {
		this.libraryNameValue = libraryNameValue;
	}

	public MatTextBox getLibraryVersionValue() {
		return libraryVersionValue;
	}

	public void setLibraryVersionValue(MatTextBox libraryVersionValue) {
		this.libraryVersionValue = libraryVersionValue;
	}

	public MatTextBox getUsingModelValue() {
		return usingModelValue;
	}

	public void setUsingModelValue(MatTextBox usingModelValue) {
		this.usingModelValue = usingModelValue;
	}

	public MatTextBox getModelVersionValue() {
		return modelVersionValue;
	}

	public void setModelVersionValue(MatTextBox modelVersionValue) {
		this.modelVersionValue = modelVersionValue;
	}
	
	public Button getSaveButton(){
		return saveAndDeleteBtn.getSaveButton();
	}
	
	public Button getCancelButton(){
		return saveAndDeleteBtn.getCloseButton();
	}
	
	public void setWidgetReadOnly(boolean isEditable){
		getLibraryNameValue().setReadOnly(!isEditable);
		getSaveButton().setEnabled(isEditable);
		getCancelButton().setEnabled(isEditable);

	}
	
	public void resetAll(){
		getLibraryNameValue().setText("");
		getLibraryVersionValue().setText("");
		getUsingModelValue().setText("");
		getModelVersionValue().setText("");
	}
	
	public void resetFormGroup(){
		getLibNameGroup().setValidationState(ValidationState.NONE);
	}

	public FormGroup getLibNameGroup() {
		return libNameGroup;
	}

	public void setLibNameGroup(FormGroup libNameGroup) {
		this.libNameGroup = libNameGroup;
	}

	public FormGroup getUsingModelGroup() {
		return usingModelGroup;
	}

	public void setUsingModelGroup(FormGroup usingModelGroup) {
		this.usingModelGroup = usingModelGroup;
	}

	public FormGroup getLibVersionGroup() {
		return libVersionGroup;
	}

	public void setLibVersionGroup(FormGroup libVersionGroup) {
		this.libVersionGroup = libVersionGroup;
	}

	public FormGroup getModelVersionGroup() {
		return modelVersionGroup;
	}

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
