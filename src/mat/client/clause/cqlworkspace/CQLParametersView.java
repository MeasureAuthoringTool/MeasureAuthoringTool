/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceCommand;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.shared.CQLAddNewButton;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLParametersView {

	/**
	 * TextArea parameterNameTxtArea.
	 */
	private MatTextBox parameterNameTxtArea = new MatTextBox();

	/** The parameter ace editor. */
	private AceEditor parameterAceEditor = new AceEditor();

	/** The parameter button bar. */
	private DefinitionFunctionButtonToolBar parameterButtonBar = new DefinitionFunctionButtonToolBar("parameter");

	/** The parameter add new button. */
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("parameter");

	/** The main param view focus panel. */
	FocusPanel mainParamViewVerticalPanel = new FocusPanel();

	/** The collapsible CQL panel widget. */
	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	
	/** The parameter comment text area. */
	private TextArea parameterCommentTextArea = new TextArea();
	
	/** The param name group. */
	private FormGroup paramNameGroup = new FormGroup();
	
	/** The param comment group. */
	private FormGroup paramCommentGroup = new FormGroup();
	
	HTML heading = new HTML();
	
	/**
	 * Instantiates a new CQL parameters view.
	 */
	public CQLParametersView() {
		mainParamViewVerticalPanel.getElement().setId("mainParamViewVerticalPanel");
		parameterAceEditor.startEditor();
		heading.addStyleName("leftAligned");
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
	@SuppressWarnings("static-access")
	private void buildView() {
		
		
		
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		paramNameGroup.clear();
		paramCommentGroup.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		parameterVP.add(heading);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		FormLabel parameterLabel = new FormLabel();
		parameterLabel.setText("Parameter Name");
		parameterLabel.setTitle("Parameter Name");
		parameterLabel.setMarginRight(15);
		parameterLabel.setId("ParameterName_Label");
		parameterLabel.setFor("parameterNameField");
		
		parameterNameTxtArea.setText("");
		parameterNameTxtArea.setSize("550px", "32px");
		parameterNameTxtArea.getElement().setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterNameTxtArea.setTitle("Enter Parameter Name");
		
		HorizontalPanel paramNameHPanel = new HorizontalPanel();
		paramNameHPanel.add(parameterLabel);
		paramNameHPanel.add(parameterNameTxtArea);
		paramNameHPanel.setWidth("700px");
		paramNameGroup.add(paramNameHPanel);
		
		Panel aceEditorPanel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();
		header.setText("Build CQL Expression");
		PanelBody body = new PanelBody();

		SimplePanel paramAceEditorPanel = new SimplePanel();
		paramAceEditorPanel.setSize("650px", "200px");
		parameterAceEditor.setText("");
		
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("650px", "200px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.addAutoCompletions();
		parameterAceEditor.setUseWrapMode(true);
		parameterAceEditor.clearAnnotations();
		parameterAceEditor.removeAllMarkers();
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		parameterAceEditor.getElement().getElementsByTagName("textarea").getItem(0).setTitle("Build CQL Expression");
		
		// MAT-8735 Disable tab and shift-tab
		parameterAceEditor.removeCommand(AceCommand.INDENT);
		parameterAceEditor.removeCommand(AceCommand.OUTDENT);

		paramAceEditorPanel.add(parameterAceEditor);
		paramAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Parameter_AceEditor");
		
		body.add(paramAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);

		parameterButtonBar.getInsertButton().setVisible(false);
		parameterButtonBar.getTimingExpButton().setVisible(false);
		parameterButtonBar.getCloseButton().setVisible(false);
		
		
		FormLabel parameterCommentLabel = new FormLabel();
		parameterCommentLabel.setText("Comment");
		parameterCommentLabel.setTitle("Comment");
		parameterCommentLabel.setMarginRight(60);
		parameterCommentLabel.setId("ParameterComment_Label");
		parameterCommentLabel.setFor("ParameterCommentTextArea_Id");
		
		parameterCommentTextArea.setId("ParameterCommentTextArea_Id");
		parameterCommentTextArea.setSize("550px", "40px");
		parameterCommentTextArea.setText("");
		parameterCommentTextArea.setName("Parameter Comment");
		parameterCommentTextArea.setTitle("Enter Comment");

		HorizontalPanel paramCommentHPanel = new HorizontalPanel();
		paramCommentHPanel.add(parameterCommentLabel);
		paramCommentHPanel.add(parameterCommentTextArea);
		paramCommentHPanel.setWidth("700px");
		paramCommentGroup.add(paramCommentHPanel);
		
		setMarginInButtonBar();
			
		parameterVP.add(addNewButtonBar);
		parameterVP.add(paramNameGroup);
		parameterVP.add(paramCommentGroup);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(parameterButtonBar.getInfoButtonGroup());
		buttonPanel.add(parameterButtonBar);
		parameterVP.add(buttonPanel);
		
		//parameterVP.add(parameterButtonBar);
		parameterVP.add(aceEditorPanel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(collapsibleCQLPanelWidget.buildViewCQLCollapsiblePanel());
		parameterVP.add(new SpacerWidget());
		
		
		parameterVP.setStyleName("topping");
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");

		mainParamViewVerticalPanel.setTitle("Parameter Section");
		mainParamViewVerticalPanel.setStyleName("cqlRightContainer");
		mainParamViewVerticalPanel.setWidth("725px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		mainParamViewVerticalPanel.add(parameterFP);
	}

	
	/**
	 * 
	 */
	public void setMarginInButtonBar() {
		parameterButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:350px;");
		//parameterButtonBar.getSaveButton().setMarginRight(-15.00);
		parameterButtonBar.getDeleteButton().setMarginLeft(-5.00);
	}


	/**
	 * Gets the parameter name txt area.
	 *
	 * @return the parameter name txt area
	 */
	public MatTextBox getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}

	/**
	 * Gets the parameter ace editor.
	 *
	 * @return the parameter ace editor
	 */
	public AceEditor getParameterAceEditor() {
		return parameterAceEditor;
	}

	/**
	 * Sets the parameter ace editor.
	 *
	 * @param parameterAceEditor the new parameter ace editor
	 */
	public void setParameterAceEditor(AceEditor parameterAceEditor) {
		this.parameterAceEditor = parameterAceEditor;
	}

	/**
	 * Gets the parameter button bar.
	 *
	 * @return the parameter button bar
	 */
	public DefinitionFunctionButtonToolBar getParameterButtonBar() {
		return parameterButtonBar;
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public FocusPanel getView() {
		mainParamViewVerticalPanel.clear();
		resetAll();
		buildView();
		return mainParamViewVerticalPanel;
	}

	/**
	 * Reset all.
	 */
	public void resetAll() {
		getParameterAceEditor().setText("");
		getParameterNameTxtArea().setText("");
		getParameterAceEditor().getElement().blur();
		
		getViewCQLAceEditor().setText("");
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
	 * Sets the widget read only.
	 *
	 * @param isEditable the new widget read only
	 */
	public void setWidgetReadOnly(boolean isEditable) {
	//	getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getParameterNameTxtArea().setEnabled(isEditable);
		getParameterCommentTextArea().setEnabled(isEditable);
		getParameterAceEditor().setReadOnly(!isEditable);
		
	/*	System.out.println(
				"in setParameterWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);*/
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInsertButton().setEnabled(isEditable);
		getParameterButtonBar().getEraseButton().setEnabled(isEditable);
	}
	
	/**
	 * Reseet form group.
	 */
	public void reseetFormGroup(){
		getParamCommentGroup().setValidationState(ValidationState.NONE);
	}

	/**
	 * Hide ace editor auto complete pop up.
	 */
	public void hideAceEditorAutoCompletePopUp() {
		getParameterAceEditor().detach();
	}

	/**
	 * Gets the parameter comment text area.
	 *
	 * @return the parameter comment text area
	 */
	public TextArea getParameterCommentTextArea() {
		return parameterCommentTextArea;
	}

	/**
	 * Sets the parameter comment text area.
	 *
	 * @param parameterCommentTextArea the new parameter comment text area
	 */
	public void setParameterCommentTextArea(TextArea parameterCommentTextArea) {
		this.parameterCommentTextArea = parameterCommentTextArea;
	}

	/**
	 * Gets the param name group.
	 *
	 * @return the param name group
	 */
	public FormGroup getParamNameGroup() {
		return paramNameGroup;
	}

	/**
	 * Sets the param name group.
	 *
	 * @param paramNameGroup the new param name group
	 */
	public void setParamNameGroup(FormGroup paramNameGroup) {
		this.paramNameGroup = paramNameGroup;
	}

	/**
	 * Gets the param comment group.
	 *
	 * @return the param comment group
	 */
	public FormGroup getParamCommentGroup() {
		return paramCommentGroup;
	}

	/**
	 * Sets the param comment group.
	 *
	 * @param paramCommentGroup the new param comment group
	 */
	public void setParamCommentGroup(FormGroup paramCommentGroup) {
		this.paramCommentGroup = paramCommentGroup;
	}
	
	/**
	 * Reset param form group.
	 */
	public void resetParamFormGroup(){
		getParamCommentGroup().setValidationState(ValidationState.NONE);
		getParamNameGroup().setValidationState(ValidationState.NONE);
	}

	/**
	 * @return the mainParamViewVerticalPanel
	 */
	public FocusPanel getMainParamViewVerticalPanel() {
		return mainParamViewVerticalPanel;
	}

	/**
	 * @param mainParamViewVerticalPanel the mainParamViewVerticalPanel to set
	 */
	public void setMainParamViewVerticalPanel(FocusPanel mainParamViewVerticalPanel) {
		this.mainParamViewVerticalPanel = mainParamViewVerticalPanel;
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}
	
	/**
	 * Added this method as part of MAT-8882.
	 * @param isEditable
	 */
	public void setReadOnly(boolean isEditable) {		
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getEraseButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInfoButton().setEnabled(isEditable);
	}	
	
}