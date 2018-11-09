package mat.client.measure.measuredetails.view;

import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.components.GeneralInformationModel;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationModel generalInformationModel;
	private TextBox abbrInput = new TextBox();
	private TextBox measureNameInput = new TextBox();
	public GeneralMeasureInformationView(GeneralInformationModel generalInformationModel) {
		this.generalInformationModel = generalInformationModel;
		buildDetailView();
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
		//TODO float the labels and handle enabled/disabled
		mainPanel.clear();
		VerticalPanel leftPanel = new VerticalPanel();
		FormLabel measureNameLabel =  new FormLabel();
		measureNameLabel.setText("Measure Name");
		measureNameLabel.setTitle(measureNameLabel.getText());
		measureNameLabel.setId("measureNameLabel");
		measureNameLabel.setFor("measureNameInput");
		measureNameInput.setId("measureNameInput");
		measureNameInput.setText(generalInformationModel.getMeasureName());
		leftPanel.add(measureNameLabel);
		leftPanel.add(measureNameInput);
		
		VerticalPanel rightPanel = new VerticalPanel();
		FormLabel abbrInputLabel =  new FormLabel();
		abbrInputLabel.setText("eCQM Abbreviated Title");
		abbrInputLabel.setTitle(abbrInputLabel.getText());
		abbrInputLabel.setId("eCQMAbbrTitleLabel");
		abbrInputLabel.setFor("abbrInput");
		rightPanel.add(abbrInputLabel);
		abbrInput.setId("abbrInput");
		abbrInput.setText(generalInformationModel.geteCQMAbbreviatedTitle());
		rightPanel.add(abbrInput);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
	}

}
