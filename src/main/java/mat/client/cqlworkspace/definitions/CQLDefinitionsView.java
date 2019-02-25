package mat.client.cqlworkspace.definitions;

import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceCommand;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLDefinitionsView {
	private HelpBlock helpBlock = new HelpBlock();
	private MatTextBox defineNameTxtArea = new MatTextBox();
	private AceEditor defineAceEditor = new AceEditor();
	private ButtonGroup contextGroup = new ButtonGroup();
	private InlineRadio contextDefinePATRadioBtn = new InlineRadio("Patient");
	private InlineRadio contextDefinePOPRadioBtn = new InlineRadio("Population");
	private DefinitionFunctionButtonToolBar defineButtonBar = new DefinitionFunctionButtonToolBar("definition");
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("definition");
	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	private TextArea defineCommentTextArea = new TextArea();
	private TextBox returnTypeTextBox = new TextBox();
	private FormGroup defineNameGroup = new FormGroup();
	private FormGroup defineCommentGroup = new FormGroup();
	private FormGroup defineContextGroup = new FormGroup();
	private FormGroup returnTypeAndButtonPanelGroup = new FormGroup();
	private FocusPanel mainDefineViewVerticalPanel = new FocusPanel();
	HTML heading = new HTML();
	
	public CQLDefinitionsView() {
		defineAceEditor.startEditor();
		mainDefineViewVerticalPanel.getElement().setId("mainDefViewVerticalPanel");
		
		collapsibleCQLPanelWidget.getViewCQLAceEditor().startEditor();
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataToggle(Toggle.COLLAPSE);
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataParent("#panelGroup");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setHref("#panelCollapse");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setText("Click to View CQL");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setColor("White");
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
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		defineNameGroup.clear();
		defineCommentGroup.clear();
		defineContextGroup.clear();
		returnTypeAndButtonPanelGroup.clear();
		
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		definitionVP.add(heading);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(new SpacerWidget());
		definitionVP.add(buildHelpBlock());
		
		FormLabel defineNameLabel = new FormLabel();
		defineNameLabel.setText("Definition Name");
		defineNameLabel.setTitle("Definition Name");
		defineNameLabel.setMarginRight(15);
		defineNameLabel.setId("DefinitionName_Label");
		defineNameLabel.setFor("defineNameField");
		
		defineNameTxtArea.setText("");
		defineNameTxtArea.setSize("550px", "32px");
		defineNameTxtArea.getElement().setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineNameTxtArea.setTitle("Enter Definition Name Required");
		
		HorizontalPanel defineNameHPanel = new HorizontalPanel();
		defineNameHPanel.add(defineNameLabel);
		defineNameHPanel.add(defineNameTxtArea);
		defineNameHPanel.setWidth("700px");
			
		defineNameGroup.add(defineNameHPanel);
		FormLabel defineContextLabel = new FormLabel();
		defineContextLabel.setText("Context");
		defineContextLabel.setTitle("Context");
		defineContextLabel.setId("DefinitionContext_Label");
		defineContextLabel.setFor("contextGroup");
		
		FlowPanel defineContextPanel = new FlowPanel();
		contextDefinePATRadioBtn.setValue(true);
		contextDefinePATRadioBtn.setText("Patient");
		contextDefinePATRadioBtn.setId("context_PatientRadioButton");
		contextDefinePOPRadioBtn.setValue(false);
		contextDefinePOPRadioBtn.setText("Population");
		contextDefinePOPRadioBtn.setId("context_PopulationRadioButton");
		contextGroup.setId("contextGroup");
		contextGroup.add(contextDefinePATRadioBtn);
		contextGroup.add(contextDefinePOPRadioBtn);
		contextGroup.setStyleName("contextToggleSwitch");
		defineContextPanel.add(contextGroup);
		
		HorizontalPanel defineContextHPanel = new HorizontalPanel();
		defineContextHPanel.add(defineContextLabel);
		defineContextHPanel.add(defineContextPanel);
		defineContextHPanel.setWidth("500px");
		
		defineContextGroup.add(defineContextHPanel);

		FormLabel defineCommentLabel = new FormLabel();
		defineCommentLabel.setText("Comment");
		defineCommentLabel.setTitle("Comment");
		defineCommentLabel.setMarginRight(53);
		defineCommentLabel.setId("DefinitionComment_Label");
		defineCommentLabel.setFor("DefineCommentTextArea_Id");
		
		defineCommentTextArea.setId("DefineCommentTextArea_Id");
		defineCommentTextArea.setSize("550px", "40px");
		defineCommentTextArea.setText("");
		defineCommentTextArea.setName("Definition Comment");
		defineCommentTextArea.setTitle("Enter Comment");
		
		HorizontalPanel defineCommenttHPanel = new HorizontalPanel();
		defineCommenttHPanel.add(defineCommentLabel);
		defineCommenttHPanel.add(defineCommentTextArea);
		defineCommenttHPanel.setWidth("700px");
		
		defineCommentGroup.add(defineCommenttHPanel);
		FormLabel returnTypeLabel = new FormLabel();
		returnTypeLabel.setText("Return Type");
		returnTypeLabel.setTitle("Return Type");
		returnTypeLabel.setMarginRight(42);
		returnTypeLabel.setId("returnType_Label");
		returnTypeLabel.setFor("returnTypeTextArea_Id");
		
		returnTypeTextBox.setId("returnTypeTextArea_Id");
		returnTypeTextBox.setTitle("Return Type of CQL Expression");
		returnTypeTextBox.setReadOnly(true);
		returnTypeTextBox.setWidth("550px");
		setMarginInButtonBar();
		
		HorizontalPanel returnTypeHP = new HorizontalPanel();
		returnTypeHP.add(returnTypeLabel);
		returnTypeHP.add(returnTypeTextBox);
		
		returnTypeAndButtonPanelGroup.add(returnTypeHP);
		
		
		defineButtonBar.getTimingExpButton().setVisible(false);
		defineButtonBar.getCloseButton().setVisible(false);

		Panel aceEditorPanel = editableAceEditorPanel();
		definitionVP.add(addNewButtonBar);
		definitionVP.add(defineNameGroup);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextGroup);
		definitionVP.add(defineCommentGroup);
		
		definitionVP.add(new SpacerWidget());
		definitionVP.add(returnTypeAndButtonPanelGroup);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(defineButtonBar.getInfoButtonGroup());
		buttonPanel.add(defineButtonBar);
		definitionVP.add(buttonPanel);
		definitionVP.add(aceEditorPanel);
		definitionVP.add(defineButtonBar.getSaveButtonGroup());
		definitionVP.add(new SpacerWidget());
		definitionVP.add(collapsibleCQLPanelWidget.buildViewCQLCollapsiblePanel());
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

	public void setMarginInButtonBar() {
		defineButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:365px;");
		defineButtonBar.getEraseButton().setMarginRight(5.00);
		defineButtonBar.getInsertButton().setMarginRight(5.00);
		defineButtonBar.getExpressionBuilderButton().setMarginLeft(-5.00);
		
		defineButtonBar.getSaveButton().setMarginLeft(480.00);
	}

	public Panel editableAceEditorPanel() {
		Panel aceEditorPanel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();
		header.setText("Build CQL Expression");
		PanelBody body = new PanelBody();
		
		SimplePanel defAceEditorPanel = new SimplePanel();
		defAceEditorPanel.setSize("650px", "200px");
		defineAceEditor.setText("");
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("650px", "200px");
		defineAceEditor.setAutocompleteEnabled(true);
		defineAceEditor.addAutoCompletions();
		defineAceEditor.setUseWrapMode(true);
		defineAceEditor.removeAllMarkers();
		defineAceEditor.clearAnnotations();
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		defineAceEditor.setTitle("Build CQL Expression");
		defineAceEditor.getElement().getElementsByTagName("textarea").getItem(0).setTitle("Build CQL Expression");
		
		defineAceEditor.removeCommand(AceCommand.INDENT);
		defineAceEditor.removeCommand(AceCommand.OUTDENT);
		
		defAceEditorPanel.add(defineAceEditor);
		defAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Define_AceEditor");
		body.add(defAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);
		aceEditorPanel.setMarginBottom(-10.00);
		return aceEditorPanel;
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


	public void setDefineNameTxtArea(MatTextBox defineNameTxtArea) {
		this.defineNameTxtArea = defineNameTxtArea;
	}


	public AceEditor getDefineAceEditor() {
		return defineAceEditor;
	}

	public void setDefineAceEditor(AceEditor defineAceEditor) {
		this.defineAceEditor = defineAceEditor;
	}


	public ButtonGroup getContextGroup() {
		return contextGroup;
	}

	public void setContextGroup(ButtonGroup contextGroup) {
		this.contextGroup = contextGroup;
	}


	public InlineRadio getContextDefinePATRadioBtn() {
		return contextDefinePATRadioBtn;
	}


	public void setContextDefinePATRadioBtn(InlineRadio contextDefinePATRadioBtn) {
		this.contextDefinePATRadioBtn = contextDefinePATRadioBtn;
	}


	public InlineRadio getContextDefinePOPRadioBtn() {
		return contextDefinePOPRadioBtn;
	}


	public void setContextDefinePOPRadioBtn(InlineRadio contextDefinePOPRadioBtn) {
		this.contextDefinePOPRadioBtn = contextDefinePOPRadioBtn;
	}


	public DefinitionFunctionButtonToolBar getDefineButtonBar() {
		return defineButtonBar;
	}


	public void setDefineButtonBar(DefinitionFunctionButtonToolBar defineButtonBar) {
		this.defineButtonBar = defineButtonBar;
	}


	public CQLAddNewButton getAddNewButtonBar() {
		return addNewButtonBar;
	}


	public void setAddNewButtonBar(CQLAddNewButton addNewButtonBar) {
		this.addNewButtonBar = addNewButtonBar;
	}


	public void resetAll() {
		getDefineNameTxtArea().setText("");
		getDefineAceEditor().setText("");
		getViewCQLAceEditor().setText("");
		getReturnTypeTextBox().setText("");
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().getElement().setClassName("panel-collapse collapse");
	}


	public PanelCollapse getPanelViewCQLCollapse() {
		return collapsibleCQLPanelWidget.getPanelViewCQLCollapse();
	}


	public AceEditor getViewCQLAceEditor() {
		return collapsibleCQLPanelWidget.getViewCQLAceEditor();
	}


	public void hideAceEditorAutoCompletePopUp() {
		getDefineAceEditor().detach();
	}


	public void setWidgetReadOnly(boolean isEditable) {

		getDefineNameTxtArea().setEnabled(isEditable);
		getDefineCommentTextArea().setEnabled(isEditable);
		getDefineAceEditor().setReadOnly(!isEditable);
		getContextDefinePATRadioBtn().setEnabled(isEditable);
		getContextDefinePOPRadioBtn().setEnabled(isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getTimingExpButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
		getDefineButtonBar().getExpressionBuilderButton().setEnabled(isEditable);
	}


	public void resetDefineFormGroup(){
		getDefineCommentGroup().setValidationState(ValidationState.NONE);
		getDefineNameGroup().setValidationState(ValidationState.NONE);
	}
	
	public TextArea getDefineCommentTextArea() {
		return defineCommentTextArea;
	}


	public void setDefineCommentTextArea(TextArea commentTextArea) {
		this.defineCommentTextArea = commentTextArea;
	}

	public FormGroup getDefineNameGroup() {
		return defineNameGroup;
	}


	public void setDefineNameGroup(FormGroup defineNameGroup) {
		this.defineNameGroup = defineNameGroup;
	}


	public FormGroup getDefineCommentGroup() {
		return defineCommentGroup;
	}

	public void setDefineCommentGroup(FormGroup defineCommentGroup) {
		this.defineCommentGroup = defineCommentGroup;
	}


	public FormGroup getDefineContextGroup() {
		return defineContextGroup;
	}


	public void setDefineContextGroup(FormGroup defineContextGroup) {
		this.defineContextGroup = defineContextGroup;
	}

	public TextBox getReturnTypeTextBox() {
		return returnTypeTextBox;
	}


	public FocusPanel getMainDefineViewVerticalPanel() {
		return mainDefineViewVerticalPanel;
	}

	
	public void setMainDefineViewVerticalPanel(FocusPanel mainDefineViewVerticalPanel) {
		this.mainDefineViewVerticalPanel = mainDefineViewVerticalPanel;
	}

	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public void setReadOnly(boolean isEditable) {		
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getInfoButton().setEnabled(isEditable);
	}	
}
