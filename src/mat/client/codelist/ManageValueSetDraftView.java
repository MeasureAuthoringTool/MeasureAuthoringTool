package mat.client.codelist;

import mat.client.codelist.ManageValueSetSearchModel.Result;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
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
 * The Class ManageValueSetDraftView.
 * 
 * @author aschmidt
 */
public class ManageValueSetDraftView implements ManageCodeListSearchPresenter.DraftDisplay{
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The view. */
	private SearchView<ManageValueSetSearchModel.Result> view = 
		new SearchView<ManageValueSetSearchModel.Result>("Value Sets");
	
	/**
	 * Instantiates a new manage value set draft view.
	 */
	public ManageValueSetDraftView(){
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(new Label("Select a Value Set to create a Draft."));
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(view.asWidget());
		mainPanel.add(errorMessages);
		mainPanel.add(successMessages);
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonBar);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#buildDataTable(mat.client.shared.search.SearchResults, int, long, int, int)
	 */
	@Override
	public void buildDataTable(SearchResults<Result> results,int pageCount,long totalResults,int currentPage,int pageSize) {
		view.buildDraftDataTable(results, pageCount, totalResults, currentPage, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getPageSize()
	 */
	public int getPageSize() {
		return view.getPageSize();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#setPageSize(int)
	 */
	public void setPageSize(int pageSize){
		view.setPageSize(pageSize);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getCurrentPage()
	 */
	public int getCurrentPage(){
		return view.getCurrentPage();
	}

	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#setCurrentPage(int)
	 */
	public void setCurrentPage(int pageNumber){
		view.setCurrentPage(pageNumber);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.DraftDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
}

