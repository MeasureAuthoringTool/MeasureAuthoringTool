package mat.client.measure.measuredetails.view;

import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.GeneralInformationModel;
import mat.client.shared.SpacerWidget;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationModel generalInformationModel;
	private TextBox eCQMAbbreviatedTitleTextBox = new TextBox();
	private TextBox measureNameTextBox = new TextBox();
	private boolean readOnly = false;
	public GeneralMeasureInformationView(GeneralInformationModel generalInformationModel) {
		this.generalInformationModel = generalInformationModel;
		buildDetailView();
		setReadOnly(readOnly);
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		//TODO compare model against old model for dirty check
		return false;
	}

	@Override
	public void buildDetailView() {
		mainPanel.clear();
		HorizontalPanel detailPanel = new HorizontalPanel();
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.getElement().getStyle().setProperty("marginRight", "10px");

		buildMeasureName(leftPanel);
		buildFinalizedDate(leftPanel);
		buildeCQMVersionNumber(leftPanel);
		
		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.getElement().getStyle().setProperty("marginLeft", "10px");
		buildAbbreviatedName(rightPanel);
		buildGUID(rightPanel);
		
		detailPanel.add(leftPanel);
		detailPanel.add(rightPanel);
		mainPanel.add(detailPanel);
	}
	
	private void buildMeasureName(CellPanel panel) {
		FormLabel measureNameLabel =  new FormLabel();
		measureNameLabel.setText("Measure Name");
		measureNameLabel.setTitle(measureNameLabel.getText());
		measureNameLabel.setId("measureNameLabel");
		measureNameLabel.setFor("measureNameInput");
		measureNameTextBox.setId("measureNameInput");
		measureNameTextBox.setText(generalInformationModel.getMeasureName());
		panel.add(measureNameLabel);
		panel.add(measureNameTextBox);
		panel.add(new SpacerWidget());
	}
	
	private void buildAbbreviatedName(CellPanel panel) {
		FormLabel eCQMAbbreviatedTitleLabel =  new FormLabel();
		eCQMAbbreviatedTitleLabel.setText("eCQM Abbreviated Title");
		eCQMAbbreviatedTitleLabel.setTitle(eCQMAbbreviatedTitleLabel.getText());
		eCQMAbbreviatedTitleLabel.setId("eCQMAbbrTitleLabel");
		eCQMAbbreviatedTitleLabel.setFor("abbrInput");
		panel.add(eCQMAbbreviatedTitleLabel);
		eCQMAbbreviatedTitleTextBox.setId("abbrInput");
		eCQMAbbreviatedTitleTextBox.setText(generalInformationModel.geteCQMAbbreviatedTitle());
		panel.add(eCQMAbbreviatedTitleTextBox);
		panel.add(new SpacerWidget());
	}
	
	private void buildFinalizedDate(CellPanel panel) {
		TextBox finalizedDateTextBox = new TextBox();
		FormLabel finalizedDateLabel = new FormLabel();
		finalizedDateLabel.setText("Finalized Date");
		finalizedDateLabel.setTitle(finalizedDateLabel.getText());
		panel.add(finalizedDateLabel);
		finalizedDateLabel.setId("finalizedDateLabel");
		finalizedDateLabel.setFor("finalizedDate");
		finalizedDateTextBox.setId("finalizedDate");
		finalizedDateTextBox.setReadOnly(true);
		finalizedDateTextBox.setEnabled(false);
		finalizedDateTextBox.setWidth("300px");
		panel.add(finalizedDateTextBox);
		panel.add(new SpacerWidget());
		
		finalizedDateTextBox.setText(generalInformationModel.getFinalizedDate());
	}
	
	
	
	private void buildGUID(CellPanel panel) {
		FormLabel guidLabel = new FormLabel();
		guidLabel.setText("GUID");
		guidLabel.setTitle(guidLabel.getText());
		guidLabel.setId("guidLabel");
		guidLabel.setFor("guidLabel");
		TextBox guidTextBox = new TextBox(); 
		guidTextBox.setId("guidLabel");
		guidTextBox.setReadOnly(true);
		guidTextBox.setEnabled(false);
		guidTextBox.setWidth("300px");
		panel.add(guidLabel);
		panel.add(guidTextBox);
		panel.add(new SpacerWidget());
		
		guidTextBox.setText(generalInformationModel.getGuid());
	}
	
	private void buildeCQMVersionNumber(CellPanel panel) {
		TextBox eCQMVersionNumberTextBox = new TextBox(); 
		FormLabel eCQMVersionNumberLabel = new FormLabel();
		eCQMVersionNumberLabel.setText("eCQM Version Number");
		eCQMVersionNumberLabel.setTitle(eCQMVersionNumberLabel.getText());
		eCQMVersionNumberLabel.setId("versionInputLabel");
		eCQMVersionNumberLabel.setFor("versionInput");
		eCQMVersionNumberTextBox.setReadOnly(true);
		eCQMVersionNumberTextBox.setEnabled(false);
		eCQMVersionNumberTextBox.setWidth("300px");
		eCQMVersionNumberTextBox.setId("versionInput");
		panel.add(eCQMVersionNumberLabel);
		panel.add(eCQMVersionNumberTextBox);
		
		eCQMVersionNumberTextBox.setText(generalInformationModel.geteCQMVersionNumber());
	}
	

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		eCQMAbbreviatedTitleTextBox.setReadOnly(readOnly);
		eCQMAbbreviatedTitleTextBox.setEnabled(!readOnly);
		measureNameTextBox.setReadOnly(readOnly);
		measureNameTextBox.setEnabled(!readOnly);
	}

	@Override
	public MeasureDetailState getState() {
		return MeasureDetailState.INCOMPLETE;
	}
}
