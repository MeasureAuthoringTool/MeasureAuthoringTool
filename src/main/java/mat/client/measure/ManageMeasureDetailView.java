package mat.client.measure;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormLabel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ManageMeasureDetailView extends AbstractManageMeasureDetailView {
    Label invisibleRadioYes;   
    Label invisibleRadioNo;


	public ManageMeasureDetailView() {
		buildMainPanel();
		FlowPanel fPanel = buildFlowPanel();
		
		measureNameLabel.getElement().setId("measureNameLabel_MeasureNameLabel");
		Form createMeasureForm = new Form();
		FormLabel measureNameLabel = buildMeasureNameLabel();
		buildMeasureNameTextArea();
		measureNameGroup.add(measureNameLabel);
		measureNameGroup.add(measureName);
		
		
		cqlLibraryNameLabel.getElement().setId("cqlLibraryNamePanel_CQLLibraryNameLabel"); //TODO create id possibly
		Form createCQLLibraryForm = new Form();
		FormLabel cqlLibraryNameLabel = buildCQLLibraryNameLabel();
		buildCQLLibraryTextArea();
		cqlLibraryNameGroup.add(cqlLibraryNameLabel);
		cqlLibraryNameGroup.add(cqlLibraryName);
		
		FormLabel shortNameLabel = buildShortNameLabel();
		buildShortNameTextBox();
		shortNameGroup.add(shortNameLabel);
		shortNameGroup.add(shortName);
		
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
		
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		
		FieldSet formFieldSet = buildFormFieldSet();
		createMeasureForm.add(formFieldSet);
		createMeasureForm.add(messageFormGrp);
		fPanel.add(createMeasureForm);
		
		createCQLLibraryForm.add(formFieldSet);
		createCQLLibraryForm.add(messageFormGrp);
		fPanel.add(createCQLLibraryForm);
		mainPanel.add(fPanel);
				
	}

}