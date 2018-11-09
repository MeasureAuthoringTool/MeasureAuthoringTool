package mat.client.measure.measuredetails.view;

import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.components.GeneralInformationModel;

public class GeneralMeasureInformationView implements ComponentDetailView{
	private FlowPanel mainPanel = new FlowPanel();
	private GeneralInformationModel generalInformationModel;
	private TextBox abbrInput = new TextBox();
	private TextBox measureNameInput = new TextBox();
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
		rightPanel.getElement().getStyle().setProperty("marginLeft", "10px");
		FormLabel abbrInputLabel =  new FormLabel();
		abbrInputLabel.setText("eCQM Abbreviated Title");
		abbrInputLabel.setTitle(abbrInputLabel.getText());
		abbrInputLabel.setId("eCQMAbbrTitleLabel");
		abbrInputLabel.setFor("abbrInput");
		rightPanel.add(abbrInputLabel);
		abbrInput.setId("abbrInput");
		abbrInput.setText(generalInformationModel.geteCQMAbbreviatedTitle());
		rightPanel.add(abbrInput);
		detailPanel.add(leftPanel);
		detailPanel.add(rightPanel);
		mainPanel.add(detailPanel);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		abbrInput.setReadOnly(readOnly);
		abbrInput.setEnabled(!readOnly);
		measureNameInput.setReadOnly(readOnly);
		measureNameInput.setEnabled(!readOnly);
	}

	@Override
	public MeasureDetailState getState() {
		return MeasureDetailState.INCOMPLETE;
	}
}
