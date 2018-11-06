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
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.view.ComponentDetailView;
import mat.client.measure.measuredetails.view.MeasureDetailsViewFactory;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.SpacerWidget;

public class MeasureDetailsView {
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel mainContentPanel = new HorizontalPanel();
	private HorizontalPanel headingPanel = new HorizontalPanel();
	private HorizontalPanel saveButtonPanel = new HorizontalPanel();
	private VerticalPanel widgetComponentPanel = new VerticalPanel();
	private MatDetailItem currentMeasureDetail;
	private ComponentDetailView componentDetailView;
	private SaveButton saveButton = new SaveButton("Measure Details");
	private DeleteButton deleteMeasureButton = new DeleteButton("Measure Details", "Delete Measure");
	
	public MeasureDetailsView(MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel) {
		currentMeasureDetail = measureDetail;
		buildMeasureDetailsButtonPanel();
		mainContentPanel.add(navigationPanel.getWidget());
		mainContentPanel.setWidth("100%");
		buildDetailView(currentMeasureDetail);
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
	
	public void buildDetailView(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
		widgetComponentPanel.clear();
		buildHeading();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(currentMeasureDetail);
		widgetComponentPanel.add(componentDetailView.getWidget());
		widgetComponentPanel.setWidth("100%");
		widgetComponentPanel.setStyleName("marginLeft15px");
		widgetComponentPanel.getElement().setId("measureDetailsView_ComponentPanel");
		mainContentPanel.add(widgetComponentPanel);
		buildSavePanel(currentMeasureDetail);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public Button getDeleteMeasureButton() {
		return this.deleteMeasureButton;
	}
	
	public boolean isValid() {
		return componentDetailView.isValid();
	}
}