package mat.client.measure;

import mat.client.measure.ManageMeasurePresenter.ShareDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.clause.MeasureShareDTO;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureShareView implements ShareDisplay {
	private FlowPanel content = new FlowPanel();
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	private SearchView<MeasureShareDTO> searchView = new SearchView<MeasureShareDTO>("Users");
	
	public ManageMeasureShareView() {
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		buttonBar.getSaveButton().setText("Share");
		
		content.add(measureNameLabel);
		
		content.add(new Label("Select users with whom you wish to share modify access:"));
		content.add(new SpacerWidget());
		
		Widget searchViewWidget = searchView.asWidget();
		searchViewWidget.setWidth("40%");
		content.add(searchViewWidget);
		content.add(new SpacerWidget());
		content.add(buttonBar);
		
	}
	
	@Override
	public HasClickHandlers getShareButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public Widget asWidget() {
		return content;
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public void buildDataTable(SearchResults<MeasureShareDTO> results) {
		searchView.buildDataTable(results);
	}
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return searchView;
	}

	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return searchView;
	}

	@Override
	public int getPageSize() {
		return searchView.getPageSize();
	}
}
