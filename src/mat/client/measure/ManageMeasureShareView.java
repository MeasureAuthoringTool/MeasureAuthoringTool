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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageMeasureShareView.
 */
public class ManageMeasureShareView implements ShareDisplay {
	
	/** The content. */
	private FlowPanel content = new FlowPanel();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The measure name label. */
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	/** The private check. */
	private CheckBox privateCheck = new CheckBox("Private Measure");
	
	/** The search view. */
	private SearchView<MeasureShareDTO> searchView = new SearchView<MeasureShareDTO>("Users");
	
	/**
	 * Instantiates a new manage measure share view.
	 */
	public ManageMeasureShareView() {
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		buttonBar.getSaveButton().setText("Save");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
		horizontalPanel.add(measureNameLabel);
		horizontalPanel.add(privateCheck);
//		content.add(measureNameLabel);
		content.add(horizontalPanel);
		
		content.add(new Label("Select users with whom you wish to share modify access:"));
		content.add(new SpacerWidget());
		
		Widget searchViewWidget = searchView.asWidget();
		searchViewWidget.setWidth("60%");
		content.add(searchViewWidget);
		content.add(new SpacerWidget());
		content.add(buttonBar);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getShareButton()
	 */
	@Override
	public HasClickHandlers getShareButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return content;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#setMeasureName(java.lang.String)
	 */
	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<MeasureShareDTO> results) {
		searchView.buildDataTable(results);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return searchView;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return searchView;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return searchView.getPageSize();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#privateCheckbox()
	 */
	@Override
	public HasValueChangeHandlers<Boolean> privateCheckbox() {
		return privateCheck;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#setPrivate(boolean)
	 */
	@Override
	public void setPrivate(boolean isPrivate) {
		privateCheck.setValue(isPrivate);
	}
}
