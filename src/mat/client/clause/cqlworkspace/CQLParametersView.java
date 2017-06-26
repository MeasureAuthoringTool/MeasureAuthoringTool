/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.CommentTextAreaWithMaxLength;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

/**
 * @author jnarang
 *
 */
public class CQLParametersView {

	/**
	 * TextArea parameterNameTxtArea.
	 */
	private MatTextBox parameterNameTxtArea = new MatTextBox();

	/** The parameter ace editor. */
	private AceEditor parameterAceEditor = new AceEditor();

	private CQLButtonToolBar parameterButtonBar = new CQLButtonToolBar("parameter");

	/** The parameter add new button. */
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("parameter");

	VerticalPanel mainParamViewVerticalPanel = new VerticalPanel();

	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	
	private CommentTextAreaWithMaxLength parameterCommentTextArea = new CommentTextAreaWithMaxLength(250);

	public CQLParametersView() {
		mainParamViewVerticalPanel.getElement().setId("mainParamViewVerticalPanel");
		parameterAceEditor.startEditor();

		collapsibleCQLPanelWidget.getViewCQLAceEditor().startEditor();
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataToggle(Toggle.COLLAPSE);
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataParent("#panelGroup");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setHref("#panelCollapse");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setText("Click to View CQL");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setColor("White");
	}

	@SuppressWarnings("static-access")
	private void buildView() {
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();
		HorizontalPanel parameterHP = new HorizontalPanel();
		FormGroup parameterFormGroup = new FormGroup();
		Label parameterLabel = new Label(LabelType.INFO, "Parameter Name");
		parameterLabel.setMarginTop(5);
		parameterLabel.setId("Parameter_Label");
		parameterLabel.setText("Parameter Name");
		parameterNameTxtArea.setText("");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.getElement().setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");
		parameterFormGroup.clear();
		parameterFormGroup.add(parameterLabel);
		parameterFormGroup.add(addNewButtonBar);

		/*Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, parameterFormGroup);

		parameterHP.add(queryGrid);*/
		
		Panel aceEditorPanel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();
		header.setText("Build CQL Expression");
		PanelBody body = new PanelBody();

		SimplePanel paramAceEditorPanel = new SimplePanel();
		paramAceEditorPanel.setSize("650px", "200px");
		parameterAceEditor.setText("");
		System.out.println("In buildParameterLibraryView setText to ace editor.");
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("650px", "200px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.addAutoCompletions();
		parameterAceEditor.setUseWrapMode(true);
		parameterAceEditor.clearAnnotations();
		parameterAceEditor.removeAllMarkers();
		//parameterAceEditor.redisplay();
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		paramAceEditorPanel.add(parameterAceEditor);
		paramAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Parameter_AceEditor");
		//paramAceEditorPanel.setStyleName("cqlRightContainer");
		
		body.add(paramAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);

		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;");

		parameterButtonBar.getInsertButton().setVisible(false);
		parameterButtonBar.getTimingExpButton().setVisible(false);
		parameterButtonBar.getCloseButton().setVisible(false);
		
		Label parameterCommentLabel = new Label(LabelType.INFO, "Comment");
		parameterCommentLabel.setId("definComment_Label");
		
		parameterCommentTextArea.setId("DefineCommentTextArea_Id");
		parameterCommentTextArea.setSize("260px", "80px");
		parameterCommentTextArea.setText("");
		parameterCommentTextArea.setName("Parameter Comment");
		
		Grid queryGrid = new Grid(3, 2);
		queryGrid.setWidget(0, 0, parameterFormGroup);
		queryGrid.setWidget(0, 1, parameterNameTxtArea);
		queryGrid.setWidget(1, 0, new SpacerWidget());
		queryGrid.setWidget(2, 0, parameterCommentLabel);
		queryGrid.setWidget(2, 1, parameterCommentTextArea);
		
		parameterVP.add(new SpacerWidget());
		/*parameterVP.add(parameterHP);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterNameTxtArea);
		*/
		parameterVP.add(queryGrid);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterButtonBar);
		parameterVP.add(aceEditorPanel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(collapsibleCQLPanelWidget.buildViewCQLCollapsiblePanel());
		parameterVP.add(new SpacerWidget());
		
		
		parameterVP.setStyleName("topping");
		parameterFP.add(parameterVP);
		parameterFP.setStyleName("cqlRightContainer");

		mainParamViewVerticalPanel.setStyleName("cqlRightContainer");
		mainParamViewVerticalPanel.setWidth("700px");
		mainParamViewVerticalPanel.setHeight("500px");
		parameterFP.setWidth("700px");
		parameterFP.setStyleName("marginLeft15px");
		mainParamViewVerticalPanel.add(new SpacerWidget());
		mainParamViewVerticalPanel.add(parameterFP);
		mainParamViewVerticalPanel.setHeight("675px");
	}

	

	public MatTextBox getParameterNameTxtArea() {
		return parameterNameTxtArea;
	}

	public AceEditor getParameterAceEditor() {
		return parameterAceEditor;
	}

	public void setParameterAceEditor(AceEditor parameterAceEditor) {
		this.parameterAceEditor = parameterAceEditor;
	}

	public CQLButtonToolBar getParameterButtonBar() {
		return parameterButtonBar;
	}

	public VerticalPanel getView() {
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
		getParameterAceEditor().setReadOnly(!isEditable);
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		System.out.println(
				"in setParameterWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);
		getParameterButtonBar().getSaveButton().setEnabled(isEditable);
		getParameterButtonBar().getDeleteButton().setEnabled(isEditable);
		getParameterButtonBar().getInsertButton().setEnabled(isEditable);
		getParameterButtonBar().getEraseButton().setEnabled(isEditable);
	}

	public void hideAceEditorAutoCompletePopUp() {
		getParameterAceEditor().detach();
	}

	public CommentTextAreaWithMaxLength getParameterCommentTextArea() {
		return parameterCommentTextArea;
	}

	public void setParameterCommentTextArea(CommentTextAreaWithMaxLength parameterCommentTextArea) {
		this.parameterCommentTextArea = parameterCommentTextArea;
	}
}
