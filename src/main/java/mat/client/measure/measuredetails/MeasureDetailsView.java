package mat.client.measure.measuredetails;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.view.ComponentDetailView;
import mat.client.measure.measuredetails.view.MeasureDetailsViewFactory;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MatDetailItem;
import mat.client.shared.SpacerWidget;

public class MeasureDetailsView {
	private FlowPanel mainPanel = new FlowPanel();
	private HorizontalPanel contentPanel = new HorizontalPanel();
	private HorizontalPanel headingPanel = new HorizontalPanel();
	private VerticalPanel componentPanel = new VerticalPanel();
	private MatDetailItem currentMeasureDetail;
	private ComponentDetailView componentDetailView;
	//TODO add ids to dom objects
	//TODO handle population headings
	public MeasureDetailsView(MeasureDetailsItems measureDetail, MeasureDetailsNavigation navigationPanel) {
		currentMeasureDetail = measureDetail;
		contentPanel.add(navigationPanel.getWidget());
		buildDetailView(currentMeasureDetail);
		contentPanel.setStyleName("contentPanel");
		mainPanel.add(contentPanel);
	}

	private void buildHeading() {
		headingPanel.clear();
		HTML headingHTML = new HTML();
		headingHTML.setHTML("<h4><b>" + currentMeasureDetail.displayName() + "</b></h4>");
		headingPanel.add(headingHTML);
		componentPanel.add(headingPanel);
		componentPanel.add(new SpacerWidget());
	}
	
	public void buildDetailView(MatDetailItem currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
		componentPanel.clear();
		buildHeading();
		componentDetailView = MeasureDetailsViewFactory.get().getMeasureDetailComponentView(currentMeasureDetail);
		componentPanel.add(componentDetailView.getWidget());
		componentPanel.setWidth("700px");
		componentPanel.setStyleName("marginLeft15px");
		contentPanel.add(componentPanel);
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	public boolean isValid() {
		return componentDetailView.isValid();
	}
}
