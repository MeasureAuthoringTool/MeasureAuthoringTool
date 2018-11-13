package mat.client.measure.measuredetails;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.DeleteButton;
import mat.client.buttons.SaveButton;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.view.ComponentDetailView;
import mat.client.measure.measuredetails.view.MeasureDetailsViewFactory;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;

public class MeasureDetailsView {
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel mainContentPanel = new HorizontalPanel();
	private HorizontalPanel headingPanel = new HorizontalPanel();
	private HorizontalPanel saveButtonPanel = new HorizontalPanel();
	private VerticalPanel widgetComponentPanel = new VerticalPanel();
	private ErrorMessageAlert errorAlert = new ErrorMessageAlert();
	private MatDetailItem currentMeasureDetail;
	private ComponentDetailView componentDetailView;
	private SaveButton saveButton = new SaveButton("Measure Details");
	private DeleteButton deleteMeasureButton = new DeleteButton("Measure Details", "Delete Measure");
	private MeasureDetailsModel measureDetailsComponent;
	
	
	public MeasureDetailsView(MeasureDetailsModel measureDetailsComponent, MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel) {
		currentMeasureDetail = measureDetail;
		this.measureDetailsComponent = measureDetailsComponent;
		mainPanel.add(errorAlert);
		buildMeasureDetailsButtonPanel();

		mainContentPanel.add(navigationPanel.getWidget());
		mainContentPanel.setWidth("100%");
		widgetComponentPanel = buildDetailView(currentMeasureDetail);
		mainContentPanel.add(widgetComponentPanel);
		mainContentPanel.getElement().setId("measureDetailsView_ContentPanel");
		mainPanel.add(mainContentPanel);
		mainPanel.setStyleName("contentPanel");
	}

	private void buildHeading() {
		headingPanel.clear();
		HTML headingHTML = new HTML();
		headingHTML.setHTML("<h4><b>" + currentMeasureDetail.displayName() + "</b></h4>");
		headingHTML.getElement().setId("measureDetailsView_HeadingContent");
		headingHTML.setTitle(currentMeasureDetail.displayName());
		headingPanel.add(headingHTML);
		headingPanel.getElement().setId("measureDetailsView_HeadingPanel");
		widgetComponentPanel.add(headingPanel);
		widgetComponentPanel.add(new SpacerWidget());
	}
	
	private void buildSavePanel(MatDetailItem currentMeasureDetail) {
		if(currentMeasureDetail != MeasureDetailsItems.POPULATIONS) {
			widgetComponentPanel.add(new SpacerWidget());
			saveButtonPanel.add(saveButton);
			saveButtonPanel.setWidth("100%");
			saveButton.setPull(Pull.RIGHT);
			saveButton.setMarginRight(30);
			widgetComponentPanel.add(saveButtonPanel);
		}
	}
	
	private void buildMeasureDetailsButtonPanel() {
		mainPanel.add(new SpacerWidget());
		HorizontalPanel panel = new HorizontalPanel();
		ButtonToolBar toolbar = new ButtonToolBar();	
		toolbar.add(deleteMeasureButton);
		panel.add(toolbar);
		mainPanel.add(panel);
		mainPanel.add(new SpacerWidget());
	}
	
	public VerticalPanel buildDetailView(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
		widgetComponentPanel.clear();
		buildHeading();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(measureDetailsComponent, currentMeasureDetail);
		widgetComponentPanel.add(componentDetailView.getWidget());
		widgetComponentPanel.setWidth("100%");
		widgetComponentPanel.setStyleName("marginLeft15px");
		widgetComponentPanel.getElement().setId("measureDetailsView_ComponentPanel");
		buildSavePanel(currentMeasureDetail);
		return widgetComponentPanel;
	}
	
	public VerticalPanel buildDetailView(MeasureDetailsModel measureDetailsComponent, MatDetailItem currentMeasureDetail, MeasureDetailsNavigation navigationPanel) {
		this.currentMeasureDetail = currentMeasureDetail;
		this.measureDetailsComponent = measureDetailsComponent;
		return buildDetailView(currentMeasureDetail);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public Button getDeleteMeasureButton() {
		return this.deleteMeasureButton;
	}
	
	public boolean isValid() {
		return componentDetailView.isComplete();
	}
	
	public MessageAlert getErrorMessageAlert() {
		return errorAlert;
	}

	public void setReadOnly(boolean isReadOnly) {
		saveButton.setEnabled(!isReadOnly);
		deleteMeasureButton.setEnabled(!isReadOnly);
		componentDetailView.setReadOnly(isReadOnly);
	}
	
	public MeasureDetailState getState() {
		return componentDetailView.getState();
	}
}