package mat.client.measure;

import java.util.List;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.codelist.HasListBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

public class ManageCompositeMeasureDetailView extends AbstractManageMeasureDetailView {
	private String cautionMsgStr = "Caution: The Composite Scoring Method field controls the options available in the Measure Scoring field. The Measure Scoring field controls the options available in the Patient-based Measure field. Changing the selection in the Composite Scoring Method will reset the Measure Scoring and Patient-based Measure fields. Changing the Measure Scoring field will reset the Patient-based Measure field.";
	private ListBoxMVP  compositeScoringMethodInput = new ListBoxMVP();
    private FormGroup compositeScoringGroup = new FormGroup();
	SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("compositeMeasureDetail");

	@Override
	public void clearFields() {
		super.clearFields();
		compositeScoringMethodInput.setSelectedIndex(0);//default to --Select-- value.
	}
	
	public ManageCompositeMeasureDetailView() {
		buildMainPanel();
		FlowPanel fPanel = buildFlowPanel();
		measureNameLabel.getElement().setId("measureNameLabel_MeasureNameLabel");
		Form createMeasureForm = new Form();
		FormLabel measureNameLabel = buildMeasureNameLabel();
		
		buildNameTextArea();
		measureNameGroup.add(measureNameLabel);
		measureNameGroup.add(name);
		
		FormLabel shortNameLabel = buildShortNameLabel();
		buildShortNameTextBox();
		shortNameGroup.add(shortNameLabel);
		shortNameGroup.add(shortName);
		
		FormLabel compositeScoringLabel = buildCompositeScoringLabel();
		compositeScoringGroup.add(compositeScoringLabel);
		buildCompositeScoringInput();
		compositeScoringGroup.add(compositeScoringMethodInput);
		
		FormLabel scoringLabel = buildScoringLabel();
		scoringGroup.add(scoringLabel);
		buildMeasureScoringInput();
		HorizontalPanel scoringPanel = buildScoringPanel();
		scoringGroup.add(scoringPanel);
		
		cautionMsgPlaceHolder.getElement().setId("cautionMsgPlaceHolder_HTML");
		cautionPatientbasedMsgPlaceHolder.getElement().setId("cautionPatientbasedMsgPlaceHolder_HTML");
		
		FormLabel patientBasedLabel = buildPatientBasedLabel();
		patientBasedFormGrp.add(patientBasedLabel);
		
		buildPatientBasedInput();
		
		HorizontalPanel patientBasedPanel = buildPatientBasedPanel();
		patientBasedFormGrp.add(patientBasedPanel);
		
		FormGroup buttonFormGroup = new FormGroup();
		buttonBar.getSaveButton().setText("Continue");
		buttonFormGroup.add(buttonBar);
		
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		
		FieldSet formFieldSet = buildFormFieldSet();
		createMeasureForm.add(formFieldSet);
		createMeasureForm.add(messageFormGrp);
		fPanel.add(createMeasureForm);

		mainPanel.add(fPanel);
	}
	
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	protected FlowPanel buildFlowPanel() {
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("fPanel_FlowPanel");
		fPanel.setWidth("90%");	
		fPanel.setHeight("100%");
		fPanel.add(measureNameLabel);
		
		fPanel.add(requiredInstructions);
		requiredInstructions.getElement().setId("requiredInstructions_HTML");
		fPanel.add(new SpacerWidget());
		HTML cautionHTML = new HTML(cautionMsgStr);
		fPanel.add(cautionHTML);
		cautionHTML.getElement().setId("cautionMessage_HTML");
		
		fPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		return fPanel;
	}
	
	protected FieldSet buildFormFieldSet() {
		FormGroup buttonFormGroup = new FormGroup();
		buttonFormGroup.add(buttonBar);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(measureNameGroup);
		formFieldSet.add(shortNameGroup);
		formFieldSet.add(compositeScoringGroup);
		formFieldSet.add(scoringGroup);
		formFieldSet.add(patientBasedFormGrp);
		formFieldSet.add(buttonFormGroup);
		return formFieldSet;
	}
	
	private FormLabel buildCompositeScoringLabel() {
		FormLabel scoringLabel = new FormLabel();
		scoringLabel.setText("Composite Scoring Method");
		scoringLabel.setTitle("Composite Scoring Method");
		scoringLabel.setFor("CompositeScoringMethodListBox");
		scoringLabel.setShowRequiredIndicator(true);
		scoringLabel.setId("CompositeScoringMethodListBox");
		return scoringLabel;
	}
	
	private void buildCompositeScoringInput() {
		compositeScoringMethodInput.getElement().setId("compositeScoringInput_ListBoxMVP");
		compositeScoringMethodInput.setTitle("Composite Scoring Method");
		compositeScoringMethodInput.setStyleName("form-control");
		compositeScoringMethodInput.setVisibleItemCount(1);
		compositeScoringMethodInput.setWidth("18em");
	}

	public void setCompositeScoringChoices(List<? extends HasListBox> texts) {
		setListBoxItems(compositeScoringMethodInput, texts, MatContext.PLEASE_SELECT);
	}
	
	public ListBoxMVP getCompositeScoringMethodInput() {
		return compositeScoringMethodInput;
	}

	public String getCompositeScoringValue() {
		return compositeScoringMethodInput.getItemText(compositeScoringMethodInput.getSelectedIndex());
	}
}