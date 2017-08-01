/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

// TODO: Auto-generated Javadoc
/**
 * The Class CQlDefinitionsView.
 *
 * @author jnarang
 */
public class CQlDefinitionsView {
	
	/** The define name txt area. */
	private MatTextBox defineNameTxtArea = new MatTextBox();
	/** The define ace editor. */
	private AceEditor defineAceEditor = new AceEditor();

	/** The context group. */
	private ButtonGroup contextGroup = new ButtonGroup();

	/** The context pat toggle switch. */
	private InlineRadio contextDefinePATRadioBtn = new InlineRadio("Patient");

	/** The context pop toggle switch. */
	private InlineRadio contextDefinePOPRadioBtn = new InlineRadio("Population");

	/** The define button bar. */
	private CQLButtonToolBar defineButtonBar = new CQLButtonToolBar("definition");

	/** The define add new button. */
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("definition");

	/** The main define view vertical panel. */
	private VerticalPanel mainDefineViewVerticalPanel = new VerticalPanel();

	/** The collapsible CQL panel widget. */
	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	
	/** The define comment text area. */
	private TextArea defineCommentTextArea = new TextArea();
	
	private TextBox returnTypeTextBox = new TextBox();
	
	/** The define name group. */
	private FormGroup defineNameGroup = new FormGroup();
	
	/** The define comment group. */
	private FormGroup defineCommentGroup = new FormGroup();
	
	/** The define context group. */
	private FormGroup defineContextGroup = new FormGroup();
	
	private FormGroup returnTypeAndButtonPanelGroup = new FormGroup();
	
	/**
	 * Instantiates a new c ql definitions view.
	 */
	public CQlDefinitionsView() {

		defineAceEditor.startEditor();
		collapsibleCQLPanelWidget.getViewCQLAceEditor().startEditor();
		
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataToggle(Toggle.COLLAPSE);
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataParent("#panelGroup");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setHref("#panelCollapse");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setText("Click to View CQL");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setColor("White");
	}

	/**
	 * Builds the view.
	 */
	private void buildView() {
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		defineNameGroup.clear();
		defineCommentGroup.clear();
		defineContextGroup.clear();
		returnTypeAndButtonPanelGroup.clear();
		
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();

		// Build Definition Name Form Group
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
		defineNameTxtArea.setTitle("Enter Definition Name");
		
		HorizontalPanel defineNameHPanel = new HorizontalPanel();
		defineNameHPanel.add(defineNameLabel);
		defineNameHPanel.add(defineNameTxtArea);
		defineNameHPanel.setWidth("700px");
		
		defineNameGroup.add(defineNameHPanel);
		
		// Build Definition Context Form Group
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
	
		// Build Definition Comment Form Group
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
		
		//Build Return Type form Group
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
		//returnTypeHP.add(defineButtonBar);
		
		returnTypeAndButtonPanelGroup.add(returnTypeHP);
		
		
		defineButtonBar.getTimingExpButton().setVisible(false);
		defineButtonBar.getCloseButton().setVisible(false);
		
		// Build Ace Editor for CQL Expression building.
		Panel aceEditorPanel = editableAceEditorPanel();
		
		//definitionVP.setWidth("100%");
		definitionVP.add(addNewButtonBar);
		definitionVP.add(defineNameGroup);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextGroup);
		definitionVP.add(defineCommentGroup);
		
		definitionVP.add(new SpacerWidget());
		definitionVP.add(returnTypeAndButtonPanelGroup);
		definitionVP.add(defineButtonBar);
	//	definitionVP.add(new SpacerWidget());
		definitionVP.add(aceEditorPanel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(collapsibleCQLPanelWidget.buildViewCQLCollapsiblePanel());
		definitionVP.add(new SpacerWidget());
		definitionVP.setStyleName("topping");
		
		definitionFP.add(definitionVP);
		definitionFP.setStyleName("cqlRightContainer");

		mainDefineViewVerticalPanel.setStyleName("cqlRightContainer");
		mainDefineViewVerticalPanel.setWidth("700px");
		mainDefineViewVerticalPanel.setHeight("500px");
		definitionFP.setWidth("700px");
		definitionFP.setStyleName("marginLeft15px");
		
		mainDefineViewVerticalPanel.add(definitionFP);
		mainDefineViewVerticalPanel.setHeight("675px");
	}

	/**
	 * 
	 */
	public void setMarginInButtonBar() {
		defineButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:330px;");
		/*defineButtonBar.getSaveButton().setMarginRight(-15.00);*/
		defineButtonBar.getEraseButton().setMarginRight(5.00);
		defineButtonBar.getInsertButton().setMarginRight(10.00);
		defineButtonBar.getInfoButton().setMarginLeft(-10.00);
		defineButtonBar.getDeleteButton().setMarginLeft(-10.00);
		
	}

	/**
	 * @return
	 */
	@SuppressWarnings("static-access")
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
		defineAceEditor.redisplay();
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		defineAceEditor.setTitle("Build CQL Expression");
		defineAceEditor.getElement().getElementsByTagName("textarea").getItem(0).setTitle("Build CQL Expression");
		defAceEditorPanel.add(defineAceEditor);
		defAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Define_AceEditor");
		body.add(defAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);
		return aceEditorPanel;
	}


	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public VerticalPanel getView() {
		mainDefineViewVerticalPanel.clear();
		resetAll();
		buildView();
		return mainDefineViewVerticalPanel;
	}

	/**
	 * Gets the define name txt area.
	 *
	 * @return the define name txt area
	 */
	public MatTextBox getDefineNameTxtArea() {
		return defineNameTxtArea;
	}

	/**
	 * Sets the define name txt area.
	 *
	 * @param defineNameTxtArea the new define name txt area
	 */
	public void setDefineNameTxtArea(MatTextBox defineNameTxtArea) {
		this.defineNameTxtArea = defineNameTxtArea;
	}

	/**
	 * Gets the define ace editor.
	 *
	 * @return the define ace editor
	 */
	public AceEditor getDefineAceEditor() {
		return defineAceEditor;
	}

	/**
	 * Sets the define ace editor.
	 *
	 * @param defineAceEditor the new define ace editor
	 */
	public void setDefineAceEditor(AceEditor defineAceEditor) {
		this.defineAceEditor = defineAceEditor;
	}

	/**
	 * Gets the context group.
	 *
	 * @return the context group
	 */
	public ButtonGroup getContextGroup() {
		return contextGroup;
	}

	/**
	 * Sets the context group.
	 *
	 * @param contextGroup the new context group
	 */
	public void setContextGroup(ButtonGroup contextGroup) {
		this.contextGroup = contextGroup;
	}

	/**
	 * Gets the context define PAT radio btn.
	 *
	 * @return the context define PAT radio btn
	 */
	public InlineRadio getContextDefinePATRadioBtn() {
		return contextDefinePATRadioBtn;
	}

	/**
	 * Sets the context define PAT radio btn.
	 *
	 * @param contextDefinePATRadioBtn the new context define PAT radio btn
	 */
	public void setContextDefinePATRadioBtn(InlineRadio contextDefinePATRadioBtn) {
		this.contextDefinePATRadioBtn = contextDefinePATRadioBtn;
	}

	/**
	 * Gets the context define POP radio btn.
	 *
	 * @return the context define POP radio btn
	 */
	public InlineRadio getContextDefinePOPRadioBtn() {
		return contextDefinePOPRadioBtn;
	}

	/**
	 * Sets the context define POP radio btn.
	 *
	 * @param contextDefinePOPRadioBtn the new context define POP radio btn
	 */
	public void setContextDefinePOPRadioBtn(InlineRadio contextDefinePOPRadioBtn) {
		this.contextDefinePOPRadioBtn = contextDefinePOPRadioBtn;
	}

	/**
	 * Gets the define button bar.
	 *
	 * @return the define button bar
	 */
	public CQLButtonToolBar getDefineButtonBar() {
		return defineButtonBar;
	}

	/**
	 * Sets the define button bar.
	 *
	 * @param defineButtonBar the new define button bar
	 */
	public void setDefineButtonBar(CQLButtonToolBar defineButtonBar) {
		this.defineButtonBar = defineButtonBar;
	}

	/**
	 * Gets the adds the new button bar.
	 *
	 * @return the adds the new button bar
	 */
	public CQLAddNewButton getAddNewButtonBar() {
		return addNewButtonBar;
	}

	/**
	 * Sets the adds the new button bar.
	 *
	 * @param addNewButtonBar the new adds the new button bar
	 */
	public void setAddNewButtonBar(CQLAddNewButton addNewButtonBar) {
		this.addNewButtonBar = addNewButtonBar;
	}

	/**
	 * Reset all.
	 */
	public void resetAll() {
		getDefineNameTxtArea().setText("");
		getDefineAceEditor().setText("");
		getViewCQLAceEditor().setText("");
		getReturnTypeTextBox().setText("");
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().getElement().setClassName("panel-collapse collapse");
	}

	/**
	 * Gets the panel view CQL collapse.
	 *
	 * @return the panel view CQL collapse
	 */
	public PanelCollapse getPanelViewCQLCollapse() {
		return collapsibleCQLPanelWidget.getPanelViewCQLCollapse();
	}

	/**
	 * Gets the view CQL ace editor.
	 *
	 * @return the view CQL ace editor
	 */
	public AceEditor getViewCQLAceEditor() {
		return collapsibleCQLPanelWidget.getViewCQLAceEditor();
	}

	/**
	 * Hide ace editor auto complete pop up.
	 */
	public void hideAceEditorAutoCompletePopUp() {
		getDefineAceEditor().detach();
	}

	/**
	 * Sets the widget read only.
	 *
	 * @param isEditable the new widget read only
	 */
	public void setWidgetReadOnly(boolean isEditable) {

		getDefineNameTxtArea().setEnabled(isEditable);
		getDefineCommentTextArea().setEnabled(isEditable);
		getDefineAceEditor().setReadOnly(!isEditable);
		getContextDefinePATRadioBtn().setEnabled(isEditable);
		getContextDefinePOPRadioBtn().setEnabled(isEditable);
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		System.out.println(
				"in setDefinitionWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getTimingExpButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
	}

	/**
	 * Reset define form group.
	 */
	public void resetDefineFormGroup(){
		getDefineCommentGroup().setValidationState(ValidationState.NONE);
		getDefineNameGroup().setValidationState(ValidationState.NONE);
	}
	
	/**
	 * Gets the define comment text area.
	 *
	 * @return the define comment text area
	 */
	public TextArea getDefineCommentTextArea() {
		return defineCommentTextArea;
	}

	/**
	 * Sets the define comment text area.
	 *
	 * @param commentTextArea the new define comment text area
	 */
	public void setDefineCommentTextArea(TextArea commentTextArea) {
		this.defineCommentTextArea = commentTextArea;
	}

	/**
	 * Gets the define name group.
	 *
	 * @return the define name group
	 */
	public FormGroup getDefineNameGroup() {
		return defineNameGroup;
	}

	/**
	 * Sets the define name group.
	 *
	 * @param defineNameGroup the new define name group
	 */
	public void setDefineNameGroup(FormGroup defineNameGroup) {
		this.defineNameGroup = defineNameGroup;
	}

	/**
	 * Gets the define comment group.
	 *
	 * @return the define comment group
	 */
	public FormGroup getDefineCommentGroup() {
		return defineCommentGroup;
	}

	/**
	 * Sets the define comment group.
	 *
	 * @param defineCommentGroup the new define comment group
	 */
	public void setDefineCommentGroup(FormGroup defineCommentGroup) {
		this.defineCommentGroup = defineCommentGroup;
	}

	/**
	 * Gets the define context group.
	 *
	 * @return the define context group
	 */
	public FormGroup getDefineContextGroup() {
		return defineContextGroup;
	}

	/**
	 * Sets the define context group.
	 *
	 * @param defineContextGroup the new define context group
	 */
	public void setDefineContextGroup(FormGroup defineContextGroup) {
		this.defineContextGroup = defineContextGroup;
	}

	public TextBox getReturnTypeTextBox() {
		return returnTypeTextBox;
	}

}
