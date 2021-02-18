package mat.client.measure;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.codelist.HasListBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.validator.ErrorHandler;
import mat.shared.CompositeMethodScoringConstant;
import mat.shared.StringUtility;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.List;

public class NewCompositeMeasureView extends AbstractNewMeasureView {
	private String cautionMsgStr = "Caution: The Composite Scoring Method field controls the options available in the Measure Scoring field. The Measure Scoring field controls the options available in the Patient-based Measure field. Changing the selection in the Composite Scoring Method will reset the Measure Scoring and Patient-based Measure fields. Changing the Measure Scoring field will reset the Patient-based Measure field.";
	private ListBoxMVP compositeScoringListBox = new ListBoxMVP();
    private FormGroup compositeScoringGroup = new FormGroup();
    protected HTML compScoringMsgPlaceHolder = new HTML();
    private ErrorHandler errorHandler = new ErrorHandler();
	private String compScoringMsgStr = "<div style=\"padding-left:5px;\">WARNING: Changing the 'Composite Scoring Method and/or Measure Scoring' will have the following impacts:<br/>" +
			   "<img src='images/bullet.png'/> Populations in the Population Workspace that do not apply to the new settings will be deleted.<br/>" +
      "<img src='images/bullet.png'/> Existing Groupings in the Measure Packager will be deleted.</div>";

	SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("compositeMeasureDetail");
	
	@Override
	public void clearFields() {
		super.clearFields();
		compositeScoringListBox.setSelectedIndex(0);//default to --Select-- value.
		errorMessages.clearAlert();
		warningMessageAlert.clearAlert();
	}
	
	public NewCompositeMeasureView() {
		buildMainPanel();
		FlowPanel fPanel = buildFlowPanel();
		
		measureNameLabel.getElement().setId("measureNameLabel_MeasureNameLabel");
		Form createMeasureForm = new Form();
		FormLabel measureNameLabel = buildMeasureNameLabel();
		buildMeasureNameTextArea();
		measureNameGroup.add(measureNameLabel);
		measureNameGroup.add(measureNameTextBox);

		//Measure mode type radios
		addCompositeMeasureModelType();

        addCompositeGenerateCmsIdCheckbox();
		
		HorizontalPanel cqlLibraryNamePanel = buildCQLLibraryNamePanel();
		cqlLibraryNameGroup.add(cqlLibraryNamePanel);
		
		FormLabel shortNameLabel = buildShortNameLabel();
		buildShortNameTextBox();
		shortNameGroup.add(shortNameLabel);
		shortNameGroup.add(eCQMAbbreviatedTitleTextBox);
		
		FormLabel compositeScoringLabel = buildCompositeScoringLabel();
		compositeScoringGroup.add(compositeScoringLabel);
		buildCompositeScoringInput();
		HorizontalPanel compositeScoringPanel = buildCompositeScoringPanel();
		compositeScoringGroup.add(compositeScoringPanel);
		
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
		buttonBar.getSaveButton().setTitle("Continue");
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
		fPanel.add(warningMessageAlert);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		fPanel.getElement().appendChild(DOM.createElement(BRElement.TAG));
		
		return fPanel;
	}
	
	protected FieldSet buildFormFieldSet() {
		FormGroup buttonFormGroup = new FormGroup();
		buttonFormGroup.add(buttonBar);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(measureNameGroup);
		formFieldSet.add(measureModelGroup);
		formFieldSet.add(generateCmsIdCheckbox);
		formFieldSet.add(cqlLibraryNameGroup);
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
        scoringLabel.setShowRequiredIndicator(true);
		scoringLabel.setTitle("Composite Scoring Method");
		scoringLabel.setFor("CompositeScoringMethodListBox");
		scoringLabel.setId("CompositeScoringMethodListBox");
		return scoringLabel;
	}
	
	private void buildCompositeScoringInput() {
		compositeScoringListBox.getElement().setId("compositeScoringInput_ListBoxMVP");
		compositeScoringListBox.setTitle("Composite Scoring Method Required");
		compositeScoringListBox.setStyleName("form-control");
		compositeScoringListBox.setVisibleItemCount(1);
		compositeScoringListBox.setWidth("18em");
		compositeScoringListBox.addBlurHandler(errorHandler.buildRequiredBlurHandler(compositeScoringListBox));
	}

	public void setCompositeScoringChoices(List<? extends HasListBox> texts) {
		MatContext.get().setListBoxItems(compositeScoringListBox, texts, MatContext.PLEASE_SELECT);
	}
	
	public ListBoxMVP getCompositeScoringListBox() {
		return compositeScoringListBox;
	}

	public String getCompositeScoringValue() {
		return compositeScoringListBox.getItemText(compositeScoringListBox.getSelectedIndex());
	}
	
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	public void setCompositeScoringSelectedValue(String compositeScoringMethod) {
		if (CompositeMethodScoringConstant.ALL_OR_NOTHING.equals(compositeScoringMethod)) {
			getCompositeScoringListBox().setSelectedIndex(1);	
		} else if (CompositeMethodScoringConstant.OPPORTUNITY.equals(compositeScoringMethod)) {
			getCompositeScoringListBox().setSelectedIndex(2);
		} else if (CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR.equals(compositeScoringMethod)) {
			getCompositeScoringListBox().setSelectedIndex(3);
		} else {
			getCompositeScoringListBox().setSelectedIndex(0);
		}
		
		compositeScoringMethod = StringUtility.isEmptyOrNull(compositeScoringMethod) ? MatContext.PLEASE_SELECT : compositeScoringMethod;
		setScoringChoices(MatContext.get().getSelectionMap().get(compositeScoringMethod));
	}
	
	@Override
	public void showCautionMsg(boolean show) {
		super.showCautionMsg(show);
		if(show){
			compScoringMsgPlaceHolder.setHTML(compScoringMsgStr);
		}else{
			compScoringMsgPlaceHolder.setHTML("");
		}
	}
	
	private HorizontalPanel buildCompositeScoringPanel() {
		HorizontalPanel scoringPanel = new HorizontalPanel();
		scoringPanel.add(compositeScoringListBox);
		scoringPanel.add(new HTML("&nbsp;"));
		scoringPanel.add(compScoringMsgPlaceHolder);
		return scoringPanel;
	}
}

