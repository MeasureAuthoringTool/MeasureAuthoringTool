package mat.client.measure.measuredetails;

import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.SaveButton;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.view.ComponentDetailView;
import mat.client.measure.measuredetails.view.MeasureDetailsViewFactory;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MatDetailItem;
import mat.client.shared.SpacerWidget;

public class MeasureDetailsView {
	private FlowPanel mainPanel = new FlowPanel();
	private HorizontalPanel mainContentPanel = new HorizontalPanel();
	private HorizontalPanel headingPanel = new HorizontalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel widgetComponentPanel = new VerticalPanel();
	private MatDetailItem currentMeasureDetail;
	private ComponentDetailView componentDetailView;
	private SaveButton saveButton = new SaveButton("Measure Details");
	
	public MeasureDetailsView(MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel) {
		currentMeasureDetail = measureDetail;
		mainContentPanel.add(navigationPanel.getWidget());
		buildDetailView(currentMeasureDetail);
		mainContentPanel.setStyleName("contentPanel");
		mainContentPanel.getElement().setId("measureDetailsView_ContentPanel");
		mainPanel.add(mainContentPanel);
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
			buttonPanel.add(saveButton);
			buttonPanel.setWidth("700px");
			saveButton.setPull(Pull.RIGHT);
			saveButton.setMarginRight(30);
			widgetComponentPanel.add(buttonPanel);
		}
	}
	
	public void buildDetailView(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
		widgetComponentPanel.clear();
		buildHeading();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(currentMeasureDetail);
		widgetComponentPanel.add(componentDetailView.getWidget());
		widgetComponentPanel.setWidth("700px");
		widgetComponentPanel.setStyleName("marginLeft15px");
		widgetComponentPanel.getElement().setId("measureDetailsView_ComponentPanel");
		mainContentPanel.add(widgetComponentPanel);
		buildSavePanel(currentMeasureDetail);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public boolean isValid() {
		return componentDetailView.isValid();
	}
}