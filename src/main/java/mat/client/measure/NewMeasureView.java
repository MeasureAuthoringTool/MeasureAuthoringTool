package mat.client.measure;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormLabel;

public class NewMeasureView extends AbstractNewMeasureView {
	
	public NewMeasureView() {
		buildMainPanel();
		FlowPanel fPanel = buildFlowPanel();
		
		measureNameLabel.getElement().setId("measureNameLabel_MeasureNameLabel");
		Form createMeasureForm = new Form();
		FormLabel measureNameLabel = buildMeasureNameLabel();
		buildMeasureNameTextArea();
		measureNameGroup.add(measureNameLabel);
		measureNameGroup.add(measureNameTextBox);
		//Measure mode type radios
		addMeasureModelType();
		
		HorizontalPanel cqlLibraryNamePanel = buildCQLLibraryNamePanel();
		cqlLibraryNameGroup.add(cqlLibraryNamePanel);
		
		FormLabel shortNameLabel = buildShortNameLabel();
		buildShortNameTextBox();
		shortNameGroup.add(shortNameLabel);
		shortNameGroup.add(eCQMAbbreviatedTitleTextBox);
		
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
		
		mainPanel.add(fPanel);	
	}

}