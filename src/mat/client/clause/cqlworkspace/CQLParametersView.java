/**
 * 
 */
package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.LabelType;

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
public class CQLParametersView {

	/**
	 * TextArea parameterNameTxtArea.
	 */
	private MatTextBox parameterNameTxtArea = new MatTextBox();

	/** The parameter ace editor. */
	private AceEditor parameterAceEditor = new AceEditor();

	private CQLButtonToolBar parameterButtonBar = new CQLButtonToolBar("parameter");

	VerticalPanel mainParamViewVerticalPanel = new VerticalPanel();

	public CQLParametersView() {
		mainParamViewVerticalPanel.getElement().setId("mainParamViewVerticalPanel");
		parameterAceEditor.startEditor();
	}

	@SuppressWarnings("static-access")
	private void buildView() {
		VerticalPanel parameterVP = new VerticalPanel();
		HorizontalPanel parameterFP = new HorizontalPanel();

		Label parameterLabel = new Label(LabelType.INFO, "Parameter Name");
		parameterLabel.setMarginTop(5);
		parameterLabel.setId("Parameter_Label");
		parameterLabel.setText("Parameter Name");
		parameterNameTxtArea.setText("");
		parameterNameTxtArea.setSize("260px", "25px");
		parameterNameTxtArea.getElement().setId("parameterNameField");
		parameterNameTxtArea.setName("parameterName");

		SimplePanel paramAceEditorPanel = new SimplePanel();
		paramAceEditorPanel.setSize("685px", "510px");
		parameterAceEditor.setText("");
		System.out.println("In buildParameterLibraryView setText to ace editor.");
		parameterAceEditor.setMode(AceEditorMode.CQL);
		parameterAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		parameterAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		parameterAceEditor.setSize("675px", "500px");
		parameterAceEditor.setAutocompleteEnabled(true);
		parameterAceEditor.addAutoCompletions();
		parameterAceEditor.setUseWrapMode(true);
		parameterAceEditor.clearAnnotations();
		parameterAceEditor.removeAllMarkers();
		parameterAceEditor.redisplay();
		parameterAceEditor.getElement().setAttribute("id", "Parameter_AceEditorID");
		paramAceEditorPanel.add(parameterAceEditor);
		paramAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Parameter_AceEditor");
		paramAceEditorPanel.setStyleName("cqlRightContainer");

		parameterNameTxtArea.getElement().setAttribute("style", "width:250px;height:25px;margin-top:5px;");

		parameterButtonBar.getInsertButton().setVisible(false);
		parameterButtonBar.getTimingExpButton().setVisible(false);
		parameterButtonBar.getCloseButton().setVisible(false);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterLabel);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterNameTxtArea);
		parameterVP.add(new SpacerWidget());
		parameterVP.add(parameterButtonBar);
		parameterVP.add(paramAceEditorPanel);
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
		getParameterNameTxtArea().setText("");
		getParameterAceEditor().setText("");
	}

	public void setWidgetReadOnly(boolean isEditable) {

		getParameterNameTxtArea().setEnabled(isEditable);
		getParameterAceEditor().setReadOnly(!isEditable);
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
}
