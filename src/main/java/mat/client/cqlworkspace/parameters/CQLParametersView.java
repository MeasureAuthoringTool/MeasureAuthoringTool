/**
 * 
 */
package mat.client.cqlworkspace.parameters;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.cqlworkspace.shared.CQLEditorPanel;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

public class CQLParametersView {

	
	private MatTextBox parameterNameTxtArea = new MatTextBox();
	private DefinitionFunctionButtonToolBar parameterButtonBar = new DefinitionFunctionButtonToolBar("parameter");
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("parameter");
	private FocusPanel mainParamViewVerticalPanel = new FocusPanel();
	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	private TextArea parameterCommentTextArea = new TextArea();
	private FormGroup parameterNameGroup = new FormGroup();
	private FormGroup parameterCommentGroup = new FormGroup();
	private HTML heading = new HTML();
	private InAppHelp inAppHelp = new InAppHelp("");
	private CQLEditorPanel editorPanel;
	
	public CQLParametersView() {
		editorPanel = new CQLEditorPanel("Build CQL Expression", true);
		mainParamViewVerticalPanel.getElement().setId("mainParamViewVerticalPanel");
		heading.addStyleName("leftAligned");
		collapsibleCQLPanelWidget.getViewCQLAceEditor().startEditor();
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataToggle(Toggle.COLLAPSE);
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataParent("#panelGroup");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setHref("#panelCollapse");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setText("Click to View CQL");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setColor("White");
	}

	private void buildView() {
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		parameterCommentGroup.clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		
		parameterVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		
		parameterVP.add(new SpacerWidget());
		parameterVP.add(new SpacerWidget());
		editorPanel = new CQLEditorPanel("Build CQL Expression", true);
		editorPanel.setId("parameter");
		editorPanel.setEditorSize("650px", "200px");
		
		parameterButtonBar.getInsertButton().setVisible(false);
		parameterButtonBar.getTimingExpButton().setVisible(false);
		parameterButtonBar.getCloseButton().setVisible(false);
		
		parameterNameGroup = buildParameterNameGroup();
		parameterCommentGroup = buildParameterCommentGroup();
		
		setMarginInButtonBar();
			
		parameterVP.add(addNewButtonBar);
		parameterVP.add(parameterNameGroup);
		parameterVP.add(parameterCommentGroup);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(parameterButtonBar.getInfoButtonGroup());
		buttonPanel.add(parameterButtonBar);
		parameterVP.add(buttonPanel);
		
		parameterVP.add(editorPanel);
		parameterVP.add(parameterButtonBar.getSaveButtonGroup());
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

	private FormGroup buildParameterCommentGroup() {
		FormGroup paramCommentGroup = new FormGroup();
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
		return paramCommentGroup;
	}

	private FormGroup buildParameterNameGroup() {
		FormGroup paramNameGroup = new FormGroup();
		HorizontalPanel paramNameHPanel = new HorizontalPanel();
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
		parameterNameTxtArea.setTitle("Enter Parameter Name Required");
		
		paramNameHPanel.add(parameterLabel);
		paramNameHPanel.add(parameterNameTxtArea);
		paramNameHPanel.setWidth("700px");
		paramNameGroup.add(paramNameHPanel);
		return paramNameGroup;
	}

	public void setMarginInButtonBar() {
		parameterButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:510px;");
		parameterButtonBar.getEraseButton().setMarginRight(5.00);
		parameterButtonBar.getSaveButton().setMarginLeft(480.00);
	}

	public MatTextBox getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}

	public AceEditor getParameterAceEditor() {
		return this.editorPanel.getEditor();
	}

	public DefinitionFunctionButtonToolBar getParameterButtonBar() {
		return parameterButtonBar;
	}

	public FocusPanel getView() {
		mainParamViewVerticalPanel.clear();
		resetAll();
		buildView();
		return mainParamViewVerticalPanel;
	}

	public void resetAll() {
		getParameterAceEditor().setText("");
		getParameterNameTxtArea().setText("");
		getParameterAceEditor().getElement().blur();
		
		getViewCQLAceEditor().setText("");
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().getElement().setClassName("panel-collapse collapse");
	}

	public PanelCollapse getPanelViewCQLCollapse() {
		return collapsibleCQLPanelWidget.getPanelViewCQLCollapse();
	}

	public AceEditor getViewCQLAceEditor() {
		return collapsibleCQLPanelWidget.getViewCQLAceEditor();
	}

	public CQLAddNewButton getAddNewButtonBar() {
		return addNewButtonBar;
	}

	public void setAddNewButtonBar(CQLAddNewButton addNewButtonBar) {
		this.addNewButtonBar = addNewButtonBar;
	}

	public void setWidgetReadOnly(boolean isEditable) {
		getParameterNameTxtArea().setEnabled(isEditable);
		getParameterCommentTextArea().setEnabled(isEditable);
		getParameterAceEditor().setReadOnly(!isEditable);
		
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInsertButton().setEnabled(isEditable);
		getParameterButtonBar().getEraseButton().setEnabled(isEditable);
	}

	public void reseetFormGroup(){
		getParamCommentGroup().setValidationState(ValidationState.NONE);
	}

	public void hideAceEditorAutoCompletePopUp() {
		getParameterAceEditor().detach();
	}

	public TextArea getParameterCommentTextArea() {
		return parameterCommentTextArea;
	}

	public void setParameterCommentTextArea(TextArea parameterCommentTextArea) {
		this.parameterCommentTextArea = parameterCommentTextArea;
	}

	public FormGroup getParamNameGroup() {
		return parameterNameGroup;
	}

	public FormGroup getParamCommentGroup() {
		return parameterCommentGroup;
	}

	public void setParamCommentGroup(FormGroup paramCommentGroup) {
		this.parameterCommentGroup = paramCommentGroup;
	}

	public void resetParamFormGroup(){
		getParamCommentGroup().setValidationState(ValidationState.NONE);
		getParamNameGroup().setValidationState(ValidationState.NONE);
	}

	public FocusPanel getMainParamViewVerticalPanel() {
		return mainParamViewVerticalPanel;
	}

	public void setMainParamViewVerticalPanel(FocusPanel mainParamViewVerticalPanel) {
		this.mainParamViewVerticalPanel = mainParamViewVerticalPanel;
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public void setReadOnly(boolean isEditable) {	
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getEraseButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInfoButton().setEnabled(isEditable);
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}

	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}	
}