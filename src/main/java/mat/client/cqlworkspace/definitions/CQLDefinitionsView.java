package mat.client.cqlworkspace.definitions;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.cqlworkspace.shared.CQLEditorPanel;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.function.BooleanSupplier;

public class CQLDefinitionsView {
	private static final String DEFINITION = "definition";
	
	private HelpBlock helpBlock = new HelpBlock();
	private MatTextBox defineNameTxtArea = new MatTextBox();
	private DefinitionFunctionButtonToolBar defineButtonBar = new DefinitionFunctionButtonToolBar(DEFINITION);
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton(DEFINITION);
	private TextArea defineCommentTextArea = new TextArea();
	private TextBox returnTypeTextBox = new TextBox();
	private FormGroup definitionNameGroup = new FormGroup();
	private FormGroup defineCommentGroup = new FormGroup();
	private FormGroup definitionContextGroup = new FormGroup();
	private FormGroup returnTypeAndButtonPanelGroup = new FormGroup();
	private FocusPanel mainDefineViewVerticalPanel = new FocusPanel();
	private HTML heading = new HTML();
	private InAppHelp inAppHelp = new InAppHelp("");
	private CQLEditorPanel editorPanel= new CQLEditorPanel(DEFINITION, "CQL Expression Editor", false);
	private CQLEditorPanel viewCQLEditorPanel = new CQLEditorPanel("definitionViewCQL", "CQL Library Viewer", true);
	private BooleanSupplier isSelectedObjectFhir;
	private ErrorHandler errorHandler = new ErrorHandler();

	public CQLDefinitionsView(BooleanSupplier isSelectedObjectFhir) {
		this.isSelectedObjectFhir = isSelectedObjectFhir;
		mainDefineViewVerticalPanel.getElement().setId("mainDefViewVerticalPanel");
		heading.addStyleName("leftAligned");
	}
	
	private Widget buildHelpBlock() {
		helpBlock = new HelpBlock();
		helpBlock.setText("");
		helpBlock.setColor("transparent");
		helpBlock.setHeight("0px");
		helpBlock.setPaddingTop(0.0);
		helpBlock.setPaddingBottom(0.0);
		helpBlock.setMarginBottom(0.0);
		helpBlock.setMarginTop(0.0);
		return helpBlock;
	}
	
	public void setHelpBlockText(String message) {
		helpBlock.setText(message);
		helpBlock.getElement().setAttribute("role", "alert");
		helpBlock.getElement().focus();
	}

	private void buildView() {
		definitionNameGroup.clear();
		defineCommentGroup.clear();
		definitionContextGroup.clear();
		returnTypeAndButtonPanelGroup.clear();
		
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		definitionVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		definitionVP.add(new SpacerWidget());
		definitionVP.add(new SpacerWidget());
		definitionVP.add(buildHelpBlock());
		
		definitionNameGroup = buildDefinitionNameFormGroup();
		defineCommentGroup = buildDefinitionCommentGroup();
		returnTypeAndButtonPanelGroup = buildReturnTypeAndButtonPanelGroup();
		
		defineButtonBar.getTimingExpButton().setVisible(false);
		defineButtonBar.getCloseButton().setVisible(false);

		editorPanel.setSize("650px", "200px");
		editorPanel.getEditor().setText("");
		editorPanel.getEditor().clearAnnotations();
		this.editorPanel.getEditor().addDomHandler(event -> editorPanel.catchTabOutKeyCommand(event, defineButtonBar.getSaveButton()), KeyUpEvent.getType());
		
		viewCQLEditorPanel.setSize("655px", "200px");
		viewCQLEditorPanel.setCollabsable();
		editorPanel.getPanelGroup().setMarginBottom(-10.0);
		addNewButtonBar.getaddNewButton().setMarginRight(-13);

		definitionVP.add(addNewButtonBar);
		definitionVP.add(definitionNameGroup);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineCommentGroup);
		
		definitionVP.add(new SpacerWidget());
		definitionVP.add(returnTypeAndButtonPanelGroup);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(defineButtonBar.getInfoButtonGroup());
		buttonPanel.add(defineButtonBar);
		definitionVP.add(buttonPanel);
		definitionVP.add(editorPanel);
		definitionVP.add(defineButtonBar.getSaveButtonGroup());
		definitionVP.add(new SpacerWidget());
		definitionVP.add(viewCQLEditorPanel);
		definitionVP.add(new SpacerWidget());
		definitionVP.setStyleName("topping");
		
		definitionFP.add(definitionVP);
		definitionFP.setStyleName("cqlRightContainer");

		definitionFP.setWidth("700px");
		definitionFP.setStyleName("marginLeft15px");
		
		mainDefineViewVerticalPanel.setTitle("Definition Section");
		mainDefineViewVerticalPanel.setStyleName("cqlRightContainer");
		mainDefineViewVerticalPanel.setWidth("725px");
		mainDefineViewVerticalPanel.add(definitionFP);
	}

	private FormGroup buildReturnTypeAndButtonPanelGroup() {
		FormGroup formGroup = new FormGroup();
		FormLabel returnTypeLabel = new FormLabel();
		returnTypeLabel.setText("Return Type");
		returnTypeLabel.setTitle("Return Type");
		returnTypeLabel.setMarginRight(44);
		returnTypeLabel.setId("returnType_Label");
		returnTypeLabel.setFor("returnTypeTextArea_Id");
		
		returnTypeTextBox.setId("returnTypeTextArea_Id");
		returnTypeTextBox.setTitle("Return Type of CQL Expression");
		returnTypeTextBox.setReadOnly(true);
		returnTypeTextBox.setSize("550px", "32px");
		setMarginInButtonBar();
		
		HorizontalPanel returnTypeHP = new HorizontalPanel();
		returnTypeHP.add(returnTypeLabel);
		returnTypeHP.add(returnTypeTextBox);
		
		formGroup.add(returnTypeHP);
		return formGroup;
	}

	private FormGroup buildDefinitionCommentGroup() {
		FormGroup commentGroup = new FormGroup();

		FormLabel defineCommentLabel = new FormLabel();
		defineCommentLabel.setText("Comment");
		defineCommentLabel.setTitle("Comment");
		defineCommentLabel.setMarginRight(53);
		defineCommentLabel.setId("DefinitionComment_Label");
		defineCommentLabel.setFor("DefineCommentTextArea_Id");
		
		defineCommentTextArea.setId("DefineCommentTextArea_Id");
		defineCommentTextArea.setSize("550px", "32px");
		defineCommentTextArea.setText("");
		defineCommentTextArea.setName("Definition Comment");
		defineCommentTextArea.setTitle("Enter Comment");
		
		HorizontalPanel defineCommenttHPanel = new HorizontalPanel();
		defineCommenttHPanel.add(defineCommentLabel);
		defineCommenttHPanel.add(defineCommentTextArea);
		defineCommenttHPanel.setWidth("700px");
		
		commentGroup.add(defineCommenttHPanel);
		return commentGroup;
	}


	private FormGroup buildDefinitionNameFormGroup() {
		FormGroup nameGroup = new FormGroup();
		FormLabel defineNameLabel = new FormLabel();
		defineNameLabel.setText("Definition Name");
		defineNameLabel.setTitle("Definition Name");
		defineNameLabel.setMarginRight(14);
		defineNameLabel.setId("DefinitionName_Label");
		defineNameLabel.setFor("defineNameField");
		
		defineNameTxtArea.setText("");
		defineNameTxtArea.setSize("550px", "32px");
		defineNameTxtArea.getElement().setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineNameTxtArea.setTitle("Enter Definition Name Required");
		defineNameTxtArea.addBlurHandler(errorHandler.buildRequiredBlurHandler(defineNameTxtArea));
		
		HorizontalPanel defineNameHPanel = new HorizontalPanel();
		defineNameHPanel.add(defineNameLabel);
		defineNameHPanel.add(defineNameTxtArea);
		defineNameHPanel.setWidth("700px");
		nameGroup.add(defineNameHPanel);
		return nameGroup;
	}

	public void setMarginInButtonBar() {
		defineButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:365px;");
		defineButtonBar.getEraseButton().setMarginRight(5.00);
		defineButtonBar.getInsertButton().setMarginRight(-9.00);
		defineButtonBar.getExpressionBuilderButton().setMarginLeft(8.00);
		defineButtonBar.getSaveButton().setMarginLeft(480.00);
	}

	public FocusPanel getView() {
		mainDefineViewVerticalPanel.clear();
		resetAll();
		buildView();
		return mainDefineViewVerticalPanel;
	}

	public MatTextBox getDefineNameTxtArea() {
		return defineNameTxtArea;
	}

	public AceEditor getDefineAceEditor() {
		return this.editorPanel.getEditor();
	}

	public DefinitionFunctionButtonToolBar getDefineButtonBar() {
		return defineButtonBar;
	}

	public CQLAddNewButton getAddNewButtonBar() {
		return addNewButtonBar;
	}

	public void resetDefineFormGroup() {
		getDefineCommentGroup().setValidationState(ValidationState.NONE);
		getDefineNameGroup().setValidationState(ValidationState.NONE);
	}
	
	public void resetAll() {
	    errorHandler.clearErrors();
		editorPanel= new CQLEditorPanel(DEFINITION, "CQL Expression Editor", false);
		getDefineNameTxtArea().setText("");
		getDefineAceEditor().setText("");
		getViewCQLAceEditor().setText("");
		getReturnTypeTextBox().setText("");
		viewCQLEditorPanel.setPanelCollapsed(true);
	}
	
	public void setWidgetReadOnly(boolean isEditable) {
		getDefineNameTxtArea().setEnabled(isEditable);
		getDefineCommentTextArea().setEnabled(isEditable);
		editorPanel.setIsReadOnly(!isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getTimingExpButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
		getDefineButtonBar().getExpressionBuilderButton().setEnabled(isEditable && !isSelectedObjectFhir.getAsBoolean());
	}
	
	public void setIsEditable(boolean isEditable) {		
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getInfoButton().setEnabled(isEditable);
	}

	public PanelCollapse getPanelViewCQLCollapse() {
		return viewCQLEditorPanel.getPanelCollapse();
	}

	public AceEditor getViewCQLAceEditor() {
		return viewCQLEditorPanel.getEditor();
	}

	public void hideAceEditorAutoCompletePopUp() {
		getDefineAceEditor().detach();
	}
	
	public TextArea getDefineCommentTextArea() {
		return defineCommentTextArea;
	}

	public FormGroup getDefineNameGroup() {
		return definitionNameGroup;
	}

	public FormGroup getDefineCommentGroup() {
		return defineCommentGroup;
	}

	public FormGroup getDefineContextGroup() {
		return definitionContextGroup;
	}

	public TextBox getReturnTypeTextBox() {
		return returnTypeTextBox;
	}

	public FocusPanel getMainDefineViewVerticalPanel() {
		return mainDefineViewVerticalPanel;
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}
}
