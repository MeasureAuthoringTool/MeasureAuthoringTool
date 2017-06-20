/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

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
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;

/**
 * @author jnarang
 *
 */
public class CQlDefinitionsView {
	private MatTextBox defineNameTxtArea = new MatTextBox();
	/** The define ace editor. */
	private AceEditor defineAceEditor = new AceEditor();

	private ButtonGroup contextGroup = new ButtonGroup();

	/** The context pat toggle switch. */
	private InlineRadio contextDefinePATRadioBtn = new InlineRadio("Patient");

	/** The context pop toggle switch. */
	private InlineRadio contextDefinePOPRadioBtn = new InlineRadio("Population");

	/** The define button bar. */
	private CQLButtonToolBar defineButtonBar = new CQLButtonToolBar("definition");

	/** The define add new button. */
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("definition");

	private VerticalPanel mainDefineViewVerticalPanel = new VerticalPanel();

	private PanelCollapse panelViewCQLCollapse = new PanelCollapse();
	private AceEditor viewCQLAceEditor = new AceEditor();
	private Anchor viewCQLAnchor = new Anchor();

	public CQlDefinitionsView() {
		// TODO Auto-generated constructor stub
		defineAceEditor.startEditor();
		viewCQLAceEditor.startEditor();
		
		viewCQLAnchor.setDataToggle(Toggle.COLLAPSE);
		viewCQLAnchor.setDataParent("#panelGroup");
		viewCQLAnchor.setHref("#panelCollapse");
		viewCQLAnchor.setText("Click to View CQL");
		viewCQLAnchor.setColor("White");
	}

	@SuppressWarnings("static-access")
	private void buildView() {
		panelViewCQLCollapse.clear();
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		HorizontalPanel definitionHP = new HorizontalPanel();
		FormGroup definitionFormGroup = new FormGroup();

		Label defineLabel = new Label(LabelType.INFO, "Definition Name");
		defineLabel.setMarginTop(5);
		defineLabel.setId("Definition_Label");
		defineNameTxtArea.setText("");
		// defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.getElement().setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineLabel.setText("Definition Name");

		definitionFormGroup.clear();
		definitionFormGroup.add(defineLabel);
		definitionFormGroup.add(addNewButtonBar);

		Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, definitionFormGroup);

		definitionHP.add(queryGrid);

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
		defAceEditorPanel.add(defineAceEditor);
		defAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Define_AceEditor");
		//defAceEditorPanel.setStyleName("cqlRightContainer");
		body.add(defAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);
		

		Label defineContextLabel = new Label(LabelType.INFO, "Context");
		FlowPanel defineContextPanel = new FlowPanel();

		contextDefinePATRadioBtn.setValue(true);
		contextDefinePATRadioBtn.setText("Patient");
		contextDefinePATRadioBtn.setId("context_PatientRadioButton");
		contextDefinePOPRadioBtn.setValue(false);
		contextDefinePOPRadioBtn.setText("Population");
		contextDefinePOPRadioBtn.setId("context_PopulationRadioButton");
		contextGroup.add(contextDefinePATRadioBtn);
		contextGroup.add(contextDefinePOPRadioBtn);
		contextGroup.setStyleName("contextToggleSwitch");

		defineButtonBar.getTimingExpButton().setVisible(false);
		defineButtonBar.getCloseButton().setVisible(false);
		defineContextPanel.add(contextGroup);
		defineContextPanel.setStyleName("contextToggleSwitch");
		definitionVP.add(new SpacerWidget());
		definitionVP.add(definitionHP);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineNameTxtArea);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextPanel);
		definitionVP.add(defineButtonBar);
		/*definitionVP.add(new SpacerWidget());
		
		
		definitionVP.add(defineComment);
		definitionVP.add(new SpacerWidget());*/
		//definitionVP.add(commentBox);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(aceEditorPanel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(buildViewCQLCollapsiblePanel());
		definitionVP.add(new SpacerWidget());
		definitionVP.setStyleName("topping");
		definitionFP.add(definitionVP);
		definitionFP.setStyleName("cqlRightContainer");

		mainDefineViewVerticalPanel.setStyleName("cqlRightContainer");
		mainDefineViewVerticalPanel.setWidth("700px");
		mainDefineViewVerticalPanel.setHeight("500px");
		definitionFP.setWidth("700px");
		definitionFP.setStyleName("marginLeft15px");

		mainDefineViewVerticalPanel.add(new SpacerWidget());
		mainDefineViewVerticalPanel.add(definitionFP);
		mainDefineViewVerticalPanel.setHeight("675px");
	}

	/**
	 * Method to build collapsible View CQL Panel with Ace Editor.
	 * 
	 * @return PanelGroup
	 */
	PanelGroup buildViewCQLCollapsiblePanel() {
		PanelGroup panelGroup = new PanelGroup();
		panelGroup.setId("panelGroup");
		Panel panel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();

		header.add(viewCQLAnchor);

		panelViewCQLCollapse.setId("panelCollapse");
		PanelBody body = new PanelBody();

		viewCQLAceEditor.setMode(AceEditorMode.CQL);
		viewCQLAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		viewCQLAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		viewCQLAceEditor.setSize("655px", "200px");
		viewCQLAceEditor.setAutocompleteEnabled(true);
		viewCQLAceEditor.addAutoCompletions();
		viewCQLAceEditor.setUseWrapMode(true);
		viewCQLAceEditor.removeAllMarkers();
		viewCQLAceEditor.clearAnnotations();
		viewCQLAceEditor.redisplay();
		viewCQLAceEditor.getElement().setAttribute("id", "Define_ViewAceEditorID");
		viewCQLAceEditor.setReadOnly(true);

		body.add(viewCQLAceEditor);
		panelViewCQLCollapse.add(body);

		panel.add(header);
		panel.add(panelViewCQLCollapse);

		panelGroup.add(panel);

		return panelGroup;

	}

	public VerticalPanel getView() {
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

	public CQLButtonToolBar getDefineButtonBar() {
		return defineButtonBar;
	}

	public void setDefineButtonBar(CQLButtonToolBar defineButtonBar) {
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
		panelViewCQLCollapse.getElement().setClassName("panel-collapse collapse");
	}

	public PanelCollapse getPanelViewCQLCollapse() {
		return panelViewCQLCollapse;
	}

	public AceEditor getViewCQLAceEditor() {
		return viewCQLAceEditor;
	}

	public void hideAceEditorAutoCompletePopUp() {
		getDefineAceEditor().detach();
	}

	public void setWidgetReadOnly(boolean isEditable) {

		getDefineNameTxtArea().setEnabled(isEditable);
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

}
