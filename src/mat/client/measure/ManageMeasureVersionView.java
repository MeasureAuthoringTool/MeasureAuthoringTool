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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vandavar
 *
 * An view class to manage the widgets for the Version creation.
 */
public class ManageMeasureVersionView implements ManageMeasurePresenter.VersionDisplay{
	private FlowPanel mainPanel = new FlowPanel();
	private RadioButton majorRadio = new RadioButton("group","Major");
	private RadioButton minorRadio = new RadioButton("group","Minor");
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SearchView<ManageMeasureSearchModel.Result> view = 
		new SearchView<ManageMeasureSearchModel.Result>("Measures");
	
	public ManageMeasureVersionView(){
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(new Label("Select a Draft to create a Measure Version."));
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.add(errorMessages);
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.add(new Label("Select Version Type"));
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		radioPanel.add(minorRadio);
		
		
		mainPanel.add(radioPanel);
		SimplePanel buttonPanel = new SimplePanel();
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
		view.buildVersionDataTable(results,pageCount,totalResults,currentPage,pageSize);
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
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}



	@Override
	public RadioButton getMinorRadioButton() {
		return minorRadio;
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
