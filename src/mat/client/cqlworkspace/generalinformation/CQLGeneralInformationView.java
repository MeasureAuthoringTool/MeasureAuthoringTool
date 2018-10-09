package mat.client.cqlworkspace.generalinformation;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.buttons.SaveToolBarButton;
import mat.client.shared.CustomTextAreaWithMaxLength;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLGeneralInformationView {

	private HorizontalPanel generalInfoMainHPanel = new HorizontalPanel();
	private MatTextBox libraryNameValue = new MatTextBox();
	private MatTextBox libraryVersionValue = new MatTextBox();
	private MatTextBox usingModelValue = new MatTextBox();
	private MatTextBox modelVersionValue = new MatTextBox();
	private SaveToolBarButton saveButton = new SaveToolBarButton("GeneralInfo");
	private FormGroup libNameGroup = new FormGroup();
	private FormGroup usingModelGroup = new FormGroup();
	private FormGroup libVersionGroup = new FormGroup();
	private FormGroup modelVersionGroup = new FormGroup();
	private FormGroup commentsGroup = new FormGroup();
	HTML heading = new HTML();
	
	private static final String STYLE = "style";
	private static final String PIXEL_150 = "150px";
	private static final String DISABLED = "disabled";
	private static final String FONT_SIZE_90_MARGIN_LEFT_15PX = "font-size:90%;margin-left:15px;";
	private static final String MARGIN_STYLE = "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;";
	
	private static final int COMMENTS_MAX_LENGTH = 2500;
	private CustomTextAreaWithMaxLength comments = new CustomTextAreaWithMaxLength(COMMENTS_MAX_LENGTH);
	

	public CQLGeneralInformationView(){
		generalInfoMainHPanel.clear();
	}

	private void buildView() {
		
		libNameGroup.clear();
		usingModelGroup.clear();
		libVersionGroup.clear();
		modelVersionGroup.clear();
		commentsGroup.clear();
		heading.addStyleName("leftAligned");
		VerticalPanel generalInfoTopPanel = new VerticalPanel();
		
		FormLabel libraryNameLabel = new FormLabel();
		libraryNameLabel.setText("CQL Library Name");
		libraryNameLabel.setTitle("CQL Library Name");
		libraryNameLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		libraryNameLabel.setWidth(PIXEL_150);
		libraryNameLabel.setId("libraryNameLabel_Label");
		libraryNameLabel.setFor("libraryNameValue_TextBox");

		libraryNameValue.getElement().setAttribute(STYLE, MARGIN_STYLE);
		libraryNameValue.getElement().setId("libraryNameValue_TextBox");
		libraryNameValue.setTitle("Required");
		
		libNameGroup.add(libraryNameLabel);
		libNameGroup.add(libraryNameValue);
		
		FormLabel libraryVersionLabel = new FormLabel();
		libraryVersionLabel.setText("CQL Library Version");
		libraryVersionLabel.setTitle("CQL Library Version");
		libraryVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		libraryVersionLabel.setWidth(PIXEL_150);
		libraryVersionLabel.setId("libraryVersionLabel_Label");
		libraryVersionLabel.setFor("libraryVersionValue_TextBox");

		libraryVersionValue.getElement().setAttribute(STYLE, MARGIN_STYLE);
		libraryVersionValue.getElement().setId("libraryVersionValue_TextBox");
		libraryVersionValue.setReadOnly(true);

		libVersionGroup.add(libraryVersionLabel);
		libVersionGroup.add(libraryVersionValue);

		FormLabel usingModeLabel = new FormLabel();
		usingModeLabel.setText("Using Model");
		usingModeLabel.setTitle("Using Model");
		usingModeLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		usingModeLabel.setId("usingModeLabel_Label");
		usingModeLabel.setWidth(PIXEL_150);
		usingModeLabel.setFor("usingModelValue_TextBox");

		usingModelValue.getElement().setAttribute(STYLE, MARGIN_STYLE);
		usingModelValue.getElement().setId("usingModelValue_TextBox");
		usingModelValue.setReadOnly(true);
		
		usingModelGroup.add(usingModeLabel);
		usingModelGroup.add(usingModelValue);
		
		FormLabel modelVersionLabel = new FormLabel();
		modelVersionLabel.setText("Model Version");
		modelVersionLabel.setTitle("Model Version");
		modelVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		modelVersionLabel.getElement().setId("modelVersionLabel_Label");
		modelVersionLabel.setWidth(PIXEL_150);
		modelVersionLabel.setFor("modelVersionValue_TextBox");

		modelVersionValue.getElement().setAttribute(STYLE, "margin-left:15px;width:250px;height:32px;");
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
		
		VerticalPanel generalInfoVPanel = new VerticalPanel();
		generalInfoVPanel.add(saveButton);
		
		FormLabel commentsLabel = new FormLabel();
		commentsLabel.setId("commentsLabel");
		commentsLabel.setFor("commentsContent");
		commentsLabel.setText("Comments");
		comments.getElement().setAttribute("maxlength", "2500");
		comments.getElement().setAttribute("id", "commentsContent");
		comments.setHeight("220px");
		comments.setWidth("250px");
		commentsGroup.add(commentsLabel);
		commentsGroup.add(comments);	
		commentsGroup.setStylePrimaryName("floatLeft");
		generalInfoVPanel.add(new SpacerWidget());
		generalInfoVPanel.add(new SpacerWidget());
		generalInfoVPanel.add(commentsGroup);
		generalInfoVPanel.add(new SpacerWidget());
		generalInfoMainHPanel.add(generalInfoVPanel);
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
		buildButtonLayoutPanel();
		setWidgetReadOnlyForMeasure(MatContext.get().isCurrentMeasureEditable());
		return generalInfoMainHPanel;
	}
	
	public HorizontalPanel getCQLView(){
		generalInfoMainHPanel.clear();
		buildView();	
		buildButtonLayoutPanel();
		setWidgetReadOnlyForCQLLibrary(MatContext.get().getLibraryLockService().checkForEditPermission());
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
		return saveButton;
	}

	public void setWidgetReadOnlyForMeasure(boolean isEditable) {
		getLibraryNameValue().setReadOnly(true);
		setButtonsAndCommentsReadOnly(isEditable);
	}

	public void setWidgetReadOnlyForCQLLibrary(boolean isEditable) {
		getLibraryNameValue().setReadOnly(!isEditable);
		setButtonsAndCommentsReadOnly(isEditable);
	}

	private void setButtonsAndCommentsReadOnly(boolean isEditable){
		getSaveButton().setEnabled(isEditable);
		if(isEditable) {
			getComments().getElement().removeAttribute(DISABLED);
		} else {
			getComments().getElement().setAttribute(DISABLED, DISABLED);
		}
	}
	
	public void resetAll(){
		getLibraryNameValue().setText("");
		getLibraryVersionValue().setText("");
		getUsingModelValue().setText("");
		getModelVersionValue().setText("");
		getComments().setText("");
	}
	
	public void resetFormGroup(){
		getLibNameGroup().setValidationState(ValidationState.NONE);
		getCommentsGroup().setValidationState(ValidationState.NONE);
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

	public void setGeneralInfoOfLibrary(String libraryName, String version, String qdmVersion , String modelUsed,
			String comments) {
		getLibraryVersionValue().setText(version);
		getUsingModelValue().setText(modelUsed);
		getModelVersionValue().setText(qdmVersion);
		getLibraryNameValue().setText(libraryName);
		getComments().setText(comments);
		getComments().setCursorPos(0);
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public CustomTextAreaWithMaxLength getComments() {
		return comments;
	}

	public void setComments(CustomTextAreaWithMaxLength comments) {
		this.comments = comments;
	}

	public FormGroup getCommentsGroup() {
		return this.commentsGroup;
	}
}
