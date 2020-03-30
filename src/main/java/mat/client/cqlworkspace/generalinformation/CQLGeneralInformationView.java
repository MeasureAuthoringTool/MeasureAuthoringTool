package mat.client.cqlworkspace.generalinformation;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.buttons.SaveToolBarButton;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLGeneralInformationView {

	private HorizontalPanel generalInfoMainHPanel = new HorizontalPanel();
	private MatTextBox libraryNameTextBox = new MatTextBox();
	private MatTextBox libraryVersionTextBox = new MatTextBox();
	private MatTextBox usingModelTextBox = new MatTextBox();
	private MatTextBox modelVersionTextBox = new MatTextBox();
	private SaveToolBarButton saveButton = new SaveToolBarButton("GeneralInfo");
	private FormGroup libraryNameGroup = new FormGroup();
	private FormGroup usingModelGroup = new FormGroup();
	private FormGroup libraryVersionGroup = new FormGroup();
	private FormGroup modelVersionGroup = new FormGroup();
	private FormGroup commentsGroup = new FormGroup();
	private HTML heading = new HTML();
	
	private static final String STYLE = "style";
	private static final String PIXEL_150 = "150px";
	private static final String FONT_SIZE_90_MARGIN_LEFT_15PX = "font-size:90%;margin-left:15px;";
	private static final String MARGIN_STYLE = "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;";
	
	private static final String COMMENTS_MAX_LENGTH = "2500";
	private static final int CQL_LIBRARY_NAME_MAX_LENGTH = 500;
	private TextArea comments = new TextArea();
	private InAppHelp inAppHelp = new InAppHelp("");
	

	public CQLGeneralInformationView(){
		generalInfoMainHPanel.clear();
	}

	private void buildView() {
		
		libraryNameGroup.clear();
		usingModelGroup.clear();
		libraryVersionGroup.clear();
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

		libraryNameTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
		libraryNameTextBox.getElement().setId("libraryNameValue_TextBox");
		libraryNameTextBox.setTitle("Required");
		libraryNameTextBox.setMaxLength(CQL_LIBRARY_NAME_MAX_LENGTH);
		
		libraryNameGroup.add(libraryNameLabel);
		libraryNameGroup.add(libraryNameTextBox);
		
		FormLabel libraryVersionLabel = new FormLabel();
		libraryVersionLabel.setText("CQL Library Version");
		libraryVersionLabel.setTitle("CQL Library Version");
		libraryVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		libraryVersionLabel.setWidth(PIXEL_150);
		libraryVersionLabel.setId("libraryVersionLabel_Label");
		libraryVersionLabel.setFor("libraryVersionValue_TextBox");

		libraryVersionTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
		libraryVersionTextBox.getElement().setId("libraryVersionValue_TextBox");
		libraryVersionTextBox.setReadOnly(true);

		libraryVersionGroup.add(libraryVersionLabel);
		libraryVersionGroup.add(libraryVersionTextBox);

		FormLabel usingModeLabel = new FormLabel();
		usingModeLabel.setText("Using Model");
		usingModeLabel.setTitle("Using Model");
		usingModeLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		usingModeLabel.setId("usingModeLabel_Label");
		usingModeLabel.setWidth(PIXEL_150);
		usingModeLabel.setFor("usingModelValue_TextBox");

		usingModelTextBox.getElement().setAttribute(STYLE, MARGIN_STYLE);
		usingModelTextBox.getElement().setId("usingModelValue_TextBox");
		usingModelTextBox.setReadOnly(true);
		
		usingModelGroup.add(usingModeLabel);
		usingModelGroup.add(usingModelTextBox);
		
		FormLabel modelVersionLabel = new FormLabel();
		modelVersionLabel.setText("Model Version");
		modelVersionLabel.setTitle("Model Version");
		modelVersionLabel.getElement().setAttribute(STYLE, FONT_SIZE_90_MARGIN_LEFT_15PX);
		modelVersionLabel.getElement().setId("modelVersionLabel_Label");
		modelVersionLabel.setWidth(PIXEL_150);
		modelVersionLabel.setFor("modelVersionValue_TextBox");

		modelVersionTextBox.getElement().setAttribute(STYLE, "margin-left:15px;width:250px;height:32px;");
		modelVersionTextBox.getElement().setId("modelVersionValue_TextBox");
		modelVersionTextBox.setReadOnly(true);
		
		modelVersionGroup.add(modelVersionLabel);
		modelVersionGroup.add(modelVersionTextBox);
		
		heading.getElement().setTabIndex(0);
		
		generalInfoTopPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		
		generalInfoTopPanel.add(new SpacerWidget());
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryNameGroup);
		generalInfoTopPanel.add(new SpacerWidget());
		
		generalInfoTopPanel.add(libraryVersionGroup);
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
		comments.getElement().setAttribute("maxlength", COMMENTS_MAX_LENGTH);
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

	public HorizontalPanel getView(boolean isEditable){
		generalInfoMainHPanel.clear();
		buildView();
		buildButtonLayoutPanel();
		setIsEditable(isEditable);
		return generalInfoMainHPanel;
	}
	
	public MatTextBox getLibraryNameTextBox() {
		return libraryNameTextBox;
	}

	public MatTextBox getLibraryVersionTextBox() {
		return libraryVersionTextBox;
	}

	public MatTextBox getUsingModelTextBox() {
		return usingModelTextBox;
	}

	public MatTextBox getModelVersionTextBox() {
		return modelVersionTextBox;
	}
	
	public Button getSaveButton(){
		return saveButton;
	}

	public void setIsEditable(boolean isEditable) {
		getLibraryNameTextBox().setEnabled(isEditable);
		getSaveButton().setEnabled(isEditable);
		getCommentsTextBox().setEnabled(isEditable);
	}
	
	public void resetAll(){
		getLibraryNameTextBox().setText("");
		getLibraryVersionTextBox().setText("");
		getUsingModelTextBox().setText("");
		getModelVersionTextBox().setText("");
		getCommentsTextBox().setText("");
	}
	
	public void resetFormGroup(){
		getLibraryNameGroup().setValidationState(ValidationState.NONE);
		getCommentsGroup().setValidationState(ValidationState.NONE);
	}

	public FormGroup getLibraryNameGroup() {
		return libraryNameGroup;
	}

	public FormGroup getUsingModelGroup() {
		return usingModelGroup;
	}

	public FormGroup getLibraryVersionGroup() {
		return libraryVersionGroup;
	}

	public FormGroup getModelVersionGroup() {
		return modelVersionGroup;
	}

	public void setGeneralInfoOfLibrary(String libraryName, String version, String versionOfModel , String modelUsed,
			String comments) {
		getLibraryVersionTextBox().setText(version);
		getUsingModelTextBox().setText(modelUsed);
		getModelVersionTextBox().setText(versionOfModel);
		getLibraryNameTextBox().setText(libraryName);
		getCommentsTextBox().setText(comments);
		getCommentsTextBox().setCursorPos(0);
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public TextArea getCommentsTextBox() {
		return comments;
	}

	public FormGroup getCommentsGroup() {
		return this.commentsGroup;
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}
}
