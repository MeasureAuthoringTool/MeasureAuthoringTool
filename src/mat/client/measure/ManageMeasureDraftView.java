/**
 * 
 */
package mat.client.measure;

import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vandavar
 * An view class to manage the widgets for the DRAFT creation.
 */
public class ManageMeasureDraftView implements ManageMeasurePresenter.DraftDisplay{
	
	private FlowPanel mainPanel = new FlowPanel();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private SearchView<ManageMeasureSearchModel.Result> view = 
		new SearchView<ManageMeasureSearchModel.Result>("Measures");
	
	public ManageMeasureDraftView(){
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(new Label("Select a Measure Version to create a Draft."));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.add(errorMessages);
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonBar);
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public void buildDataTable(SearchResults<Result> results,int pageCount,long totalResults,int currentPage,int pageSize) {
		view.buildDraftDataTable(results,pageCount,totalResults,currentPage,pageSize);
	}
	
	public int getPageSize() {
		return view.getPageSize();
	}

	public void setPageSize(int pageSize){
		view.setPageSize(pageSize);
	}
	
	
	public int getCurrentPage(){
		return view.getCurrentPage();
	}

	
	public void setCurrentPage(int pageNumber){
		view.setCurrentPage(pageNumber);
	}
	
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
}
