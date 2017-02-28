/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
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
	
	/** The context pat toggle switch. */
	private InlineRadio contextDefinePATRadioBtn = new InlineRadio("Patient");
	
	/** The context pop toggle switch. */
	private InlineRadio contextDefinePOPRadioBtn = new InlineRadio("Population");
	
	/** The define button bar. */
	private CQLButtonToolBar defineButtonBar = new CQLButtonToolBar("definition");
	
	
	private VerticalPanel mainDefineViewVerticalPanel = new VerticalPanel();;
	
	public CQlDefinitionsView() {
		// TODO Auto-generated constructor stub
		defineAceEditor.startEditor();
	}
	
	@SuppressWarnings("static-access")
	private void buildView(){
		VerticalPanel definitionVP = new VerticalPanel();
		HorizontalPanel definitionFP = new HorizontalPanel();
		
		Label defineLabel = new Label(LabelType.INFO, "Definition Name");
		defineLabel.setMarginTop(5);
		defineLabel.setId("Definition_Label");
		defineNameTxtArea.setText("");
		//defineNameTxtArea.setPlaceholder("Enter Definition Name here.");
		defineNameTxtArea.setSize("260px", "25px");
		defineNameTxtArea.getElement().setId("defineNameField");
		defineNameTxtArea.setName("defineName");
		defineLabel.setText("Definition Name");
		
		SimplePanel defAceEditorPanel = new SimplePanel();
		defAceEditorPanel.setSize("685px", "510px");
		defineAceEditor.setText("");
		defineAceEditor.setMode(AceEditorMode.CQL);
		defineAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		defineAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		defineAceEditor.setSize("675px", "500px");
		defineAceEditor.setAutocompleteEnabled(true);
		defineAceEditor.addAutoCompletions();
		defineAceEditor.setUseWrapMode(true);
		defineAceEditor.removeAllMarkers();
		defineAceEditor.clearAnnotations();
		defineAceEditor.redisplay();
		defineAceEditor.getElement().setAttribute("id", "Define_AceEditorID");
		defAceEditorPanel.add(defineAceEditor);
		defAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Define_AceEditor");
		defAceEditorPanel.setStyleName("cqlRightContainer");
		
		Label defineContextLabel = new Label(LabelType.INFO, "Context");
		FlowPanel defineConextPanel = new FlowPanel();
				
		contextDefinePATRadioBtn.setValue(true);
		contextDefinePATRadioBtn.setText("Patient");
		contextDefinePATRadioBtn.setId("context_PatientRadioButton");
		contextDefinePOPRadioBtn.setValue(false);
		contextDefinePOPRadioBtn.setText("Population");
		contextDefinePOPRadioBtn.setId("context_PopulationRadioButton");
		
		defineButtonBar.getTimingExpButton().setVisible(false);
		defineButtonBar.getCloseButton().setVisible(false);
		defineConextPanel.add(contextDefinePATRadioBtn);
		defineConextPanel.add(contextDefinePOPRadioBtn);
		defineConextPanel.setStyleName("contextToggleSwitch");
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineNameTxtArea);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineContextLabel);
		definitionVP.add(new SpacerWidget());
		definitionVP.add(defineConextPanel);
		definitionVP.add(defineButtonBar);
		definitionVP.add(defAceEditorPanel);
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
	

	public VerticalPanel getView(){
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
	
	public void resetAll() {
		getDefineNameTxtArea().setText("");
		getDefineAceEditor().setText("");
	}
	
	public void hideAceEditorAutoCompletePopUp() {
		getDefineAceEditor().detach();
	}
	
	public void setWidgetReadOnly(boolean isEditable) {

		getDefineNameTxtArea().setEnabled(isEditable);
		getDefineAceEditor().setReadOnly(!isEditable);
		getContextDefinePATRadioBtn().setEnabled(isEditable);
		getContextDefinePOPRadioBtn().setEnabled(isEditable);
		System.out.println("in setDefinitionWidgetReadOnly: setting Ace Editor read only flag. read only = " + !isEditable);
		getDefineButtonBar().getSaveButton().setEnabled(isEditable);
		getDefineButtonBar().getDeleteButton().setEnabled(isEditable);
		getDefineButtonBar().getInsertButton().setEnabled(isEditable);
		getDefineButtonBar().getTimingExpButton().setEnabled(isEditable);
		getDefineButtonBar().getEraseButton().setEnabled(isEditable);
	}

}
