package mat.client.shared.search;	



import java.util.Iterator;

import mat.client.Enableable;
import mat.client.event.MATClickHandler;
import mat.client.measure.MeasureSearchResultsAdapter;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.shared.ConstantMessages;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class SearchView.
 * 
 * @param <T>
 *            the generic type
 */
public class SearchView<T> implements HasSelectionHandlers<T>, 
	HasPageSelectionHandler, HasPageSizeSelectionHandler, HasSortHandler,HasSelectAllHandler, Enableable {
	
	/** The Constant PAGE_SIZE_ALL. */
	public static final int PAGE_SIZE_ALL = Integer.MAX_VALUE;
	
	/** The Constant PAGE_SIZES. */
	private static final int[] PAGE_SIZES= new int[] {50, PAGE_SIZE_ALL};
	
	/** The Constant HISTORY_PAGE_SIZES. */
	private static final int[] HISTORY_PAGE_SIZES = new int[] {50, PAGE_SIZE_ALL};
	
	/** The Constant ARROW_DOWN. */
	private static final String ARROW_DOWN = "\u25bc";
	
	/** The Constant ARROW_UP. */
	private static final String ARROW_UP = "\u25b2";
	
	/** The Constant DEFAULT_PAGE. */
	public static final int DEFAULT_PAGE = 1;
	
	/** The Constant DEFAULT_PAGE_SIZE. */
	public static final int DEFAULT_PAGE_SIZE = 50;
	
	/** The main panel. */
	private Panel mainPanel;
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The page size selector. */
	protected Panel pageSizeSelector = new FlowPanel();
	
	/** The page selector. */
	protected Panel pageSelector = new HorizontalPanel();
	
	/** The viewing number. */
	private HTML viewingNumber = new HTML();
	
	/**
	 * Gets the viewing number.
	 * 
	 * @return the viewing number
	 */
	public HTML getViewingNumber() {
		return viewingNumber;
	}
	
	/**
	 * Sets the viewing number.
	 * 
	 * @param viewingNumber
	 *            the new viewing number
	 */
	public void setViewingNumber(HTML viewingNumber) {
		this.viewingNumber = viewingNumber;
	}

	/** The data table. */
	public Grid508 dataTable = new Grid508();
	
	/** The v panel for qdm table. */
	public VerticalPanel vPanelForQDMTable = new VerticalPanel();
	
	/** The success message display. */
	public SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	/** The current page size. */
	private int currentPageSize = DEFAULT_PAGE_SIZE;
	
	/** The current page. */
	private int currentPage = DEFAULT_PAGE;
	
	/** The descriptor. */
	private String descriptor = "";
	
	/** The sort column index. */
	private int sortColumnIndex = 0;
	
	/**
	 * Instantiates a new search view.
	 * 
	 * @param descriptor
	 *            the descriptor
	 */
	public SearchView(String descriptor) {
		this();
		this.descriptor = descriptor;
	}
	
	/**
	 * Instantiates a new search view.
	 */
	public SearchView() {
		dataTable.setCellPadding(5);
		dataTable.getElement().setId("searchView_dataTable");
		dataTable.setStylePrimaryName("searchResultsTable");
		dataTable.setTitle("data Table");
		viewingNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		mainPanel = new SimplePanel();
		mainPanel.getElement().setId("serachView_mainPanel");
		mainPanel.setStylePrimaryName("searchResultsContainer");
		
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("serachView_FlowPanel");
		dataTable.setWidth("98%");
		mainPanel.add(fPanel);
		
		fPanel.add(pageSizeSelector);
		pageSizeSelector.getElement().setId("serachView_pageSizeSelector");
		pageSizeSelector.setStylePrimaryName("searchResultsPageSize");
		fPanel.add(viewingNumber);
		fPanel.add(new SpacerWidget());
		fPanel.add(successMessageDisplay);
		fPanel.add(new SpacerWidget());
		fPanel.add(dataTable);	
		fPanel.add(new SpacerWidget());
		fPanel.add(pageSelector);
		pageSelector.getElement().setId("serachView_pageSelector");
	}
	
	

	/**
	 * Instantiates a new search view.
	 * 
	 * @param QDSCodeListView
	 *            the qDS code list view
	 */
	public SearchView(boolean QDSCodeListView){
		mainPanel = new SimplePanel();
		mainPanel.getElement().setId("serachView_mainPanel");
		FlowPanel fPanel = new FlowPanel();
		fPanel.getElement().setId("serachView_FlowPanel");
		mainPanel.add(fPanel);
		//fPanel.add(viewingNumber);
		/*fPanel.add(new SpacerWidget());
		fPanel.add(new SpacerWidget());*/
		vPanelForQDMTable.getElement().setId("serachView_vPanelForQDMTable");
		fPanel.add(vPanelForQDMTable);
	}

	/**
	 * Sets the page size visible.
	 * 
	 * @param visible
	 *            the new page size visible
	 */
	public void setPageSizeVisible(boolean visible) {
		pageSizeSelector.setVisible(visible);
	}
	
	/**
	 * Sets the viewing number visible.
	 * 
	 * @param visible
	 *            the new viewing number visible
	 */
	public void setViewingNumberVisible(boolean visible) {
		viewingNumber.setVisible(visible);
	}
	
	/**
	 * Sets the page size.
	 * 
	 * @param i
	 *            the new page size
	 */
	public void setPageSize(int i) {
		currentPageSize = i;
	}
	
	/**
	 * Builds the page size selector.
	 */
	public void buildPageSizeSelector() {
		pageSizeSelector.clear();
		pageSelector.setHeight("30px");
		pageSizeSelector.add(new HTML("View:&nbsp; "));
		for(int i = 0; i < PAGE_SIZES.length; i++) {
			pageSizeSelector.add(buildPageSizeLink(PAGE_SIZES[i]));
			pageSizeSelector.add(new HTML("&nbsp;|&nbsp;"));
		}
		
	}
	
	/**
	 * Builds the history page size selector.
	 */
	private void buildHistoryPageSizeSelector() {
		pageSizeSelector.clear();

		pageSizeSelector.add(new HTML("View:&nbsp; "));
		for(int i = 0; i < HISTORY_PAGE_SIZES.length; i++) {
			pageSizeSelector.add(buildPageSizeLink(HISTORY_PAGE_SIZES[i]));
			pageSizeSelector.add(new HTML("&nbsp;|&nbsp;"));
		}
		
	}

	/**
	 * Build Page Selector links for a given page count.
	 * 
	 * @param pageCount
	 *            the page count
	 */
	public void buildPageSelector(int pageCount) {
		if(pageCount > 1){
			pageSelector.clear();
			int maxDisplay = ConstantMessages.MAX_PAGE_DISPLAY; 
			int start = (int) (currentPage/maxDisplay) * 10;
	
			if((currentPage % maxDisplay) > 0){
				start++;
			}
			start = (start > maxDisplay)?start:1; // default to 1 if less than max display
	
			int end = start + (maxDisplay - 1);
	
			if(end > pageCount){ //reset to max display
				end = pageCount;
			}
			
			if(start > maxDisplay){
				pageSelector.add(buildPageLink(0, "First"));
				pageSelector.add(new HTML("&nbsp;&nbsp;"));			
			}
			
			//add for previous
			if(currentPage!=1){
				pageSelector.add(buildPageLink(-9, "Prev"));
			}
			pageSelector.add(new HTML("&nbsp;&nbsp;"));
	
				
			for(int i = start; i <= end; i++) {			
				pageSelector.add(buildPageLink(i));
				if(i != end){
					//pageSelector.add(new HTML("&nbsp;|&nbsp;"));
					pageSelector.add(new HTML("&nbsp;"));
				}else{
					pageSelector.add(new HTML("&nbsp;"));
				}
			}
			
			if(pageCount > end ){
				int dots = pageCount - end;
				if(dots > ConstantMessages.PAGE_DOTS_DISPLAY){
					dots = ConstantMessages.PAGE_DOTS_DISPLAY;
				}
				for(int j=1; j <= dots; j++){
					end++;
					pageSelector.add(buildPageLink(end, "."));
					pageSelector.add(new HTML("&nbsp;"));
				}
	
				//add last page
				pageSelector.add(buildPageLink(pageCount));
				pageSelector.add(new HTML("&nbsp;"));			
			}
			if(currentPage != pageCount){
				//add for next
				pageSelector.add(buildPageLink(-1, "Next"));
				pageSelector.add(new HTML("&nbsp;&nbsp;"));
			
				//add for last
				pageSelector.add(buildPageLink(pageCount, "Last"));
				pageSelector.add(new HTML("&nbsp;"));
			}
		}else{
			 pageSelector.clear();
		}

	}
	
	/**
	 * Builds the page link.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @return the widget
	 */
	private Widget buildPageLink(final int pageNumber) {
		return buildPageLink(pageNumber, null);
	}

	/**
	 * Builds the page link.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @param mnemonic
	 *            the mnemonic
	 * @return the widget
	 */
	private Widget buildPageLink(final int pageNumber, String mnemonic) {
		
		Widget widget = null;
		String label = null;
		
		if(mnemonic != null){
			if(mnemonic.equalsIgnoreCase("Next")){
				label = "Next>";
			}else if(mnemonic.equalsIgnoreCase("Last")){
				label = "Last>>";
			}else if(mnemonic.equalsIgnoreCase("First")){
				label = "<<First";
			}else if(mnemonic.equalsIgnoreCase("Prev")){
				label = "<Prev";
			}else{
				label = ".";
			}
			widget = buildAnchorForPageSelection(label, pageNumber, mnemonic);
		}else{
			if(currentPage != pageNumber) {
				label = Integer.toString(pageNumber);
				widget = buildAnchorForPageSelection(label, pageNumber, mnemonic);
			}else {
				label = Integer.toString(pageNumber);
				Widget w = new HTML("<b>" + label + "</b>");
				w.setStyleName("page-numbers-current");
				FocusPanel fp = new FocusPanel();
				fp.add(w);
				fp.setTitle("Currently displaying the page "+label+" records.");
				return fp;
			}
		}
		return widget;
	}
	
	
	/**
	 * Builds the anchor for page selection.
	 * 
	 * @param label
	 *            the label
	 * @param pageNumber
	 *            the page number
	 * @param mnemonic
	 *            the mnemonic
	 * @return the anchor
	 */
	private Anchor buildAnchorForPageSelection(String label, final int pageNumber, String mnemonic){
		Anchor a = new Anchor(label);
		a.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				//currentPage = pageNumber;
				//
				// if we are switching the page size to all,
				// set the page size and fire an event to switch
				// to the first page
				//				
				PageSelectionEvent evt = new PageSelectionEvent(pageNumber);
				SearchView.this.fireEvent(evt);
			}
		});
		if(mnemonic != null){
			a.setTitle("Display the page \""+mnemonic+"\" records.");
		}else{
			a.setTitle("Display the page "+pageNumber+" records.");
		}
		if(!label.equalsIgnoreCase(".")){
			a.setStyleName("page-numbers");
		}
		return a;
	}
	
	/**
	 * Builds the page size link.
	 * 
	 * @param size
	 *            the size
	 * @return the widget
	 */
	private Widget buildPageSizeLink(final int size) {
		String label = (size != PAGE_SIZE_ALL) ? Integer.toString(size) : "All";
	
		if(currentPageSize != size) {
			MATAnchor a = new MATAnchor(label);
			a.addClickHandler(new MATClickHandler() {
				@Override
				public void onEvent(@SuppressWarnings("rawtypes") GwtEvent event) {
					MatContext.get().clearDVIMessages();
					currentPageSize = size;
					//
					// if we are switching the page size to all,
					// set the page size and fire an event to switch
					// to the first page
					//
					if(size == PAGE_SIZE_ALL) {
						PageSizeSelectionEvent evt = new PageSizeSelectionEvent(size);
						SearchView.this.fireEvent(evt);
					}
					else {
						PageSizeSelectionEvent evt = new PageSizeSelectionEvent(size);
						SearchView.this.fireEvent(evt);
					}
				}
			});
			a.setTitle("Display a maximum of "+label+ " records.");
			return a;
		}
		else {
			Widget w = new HTML("<b>" + label + "</b>");
			FocusPanel fp = new FocusPanel();
			fp.setHeight("20px");
			fp.add(w);
			fp.setTitle("Currently displaying a maximum of "+label+" records.");
			return fp;
		}
	}

	/**
	 * Builds the history data table.
	 * 
	 * @param results
	 *            the results
	 * @param pageCount
	 *            the page count
	 * @param totalResults
	 *            the total results
	 * @param currentPage
	 *            the current page
	 * @param pageSize
	 *            the page size
	 */
	public void buildHistoryDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		dataTable.getElement().setAttribute("id", "historytable");
		dataTable.getElement().setAttribute("aria-role", "grid");
		dataTable.getElement().setAttribute("aria-labelledby", "LiveRegion");
		dataTable.getElement().setAttribute("aria-live", "assertive");
		dataTable.getElement().setAttribute("aria-atomic", "true");
		dataTable.getElement().setAttribute("aria-relevant", "all");
		dataTable.getElement().setAttribute("role", "alert");
		dataTable.resize((int)numRows + 1, (int)numColumns);
		buildSearchResultsColumnHeaders(numRows, numColumns, results, false, false);
		buildHistorySearchResults(numRows,numColumns,results);
		
		//US501 SORTME
		buildHistoryPageSizeSelector();
		int viewStartNumber = findViewingNumber(pageSize,currentPage);
		this.descriptor ="Log Entries";
		setViewingRange2(viewStartNumber, viewStartNumber + numRows - 1, totalResults);
		buildPageSelector(pageCount);
	}

	
	/**
	 * Find viewing number.
	 * 
	 * @param pageSize
	 *            the page size
	 * @param currentPage
	 *            the current page
	 * @return the int
	 */
	protected int findViewingNumber(int pageSize,int currentPage){
		return pageSize * (currentPage - 1) + 1;	
	}
	
	/*public void  buildQDSDataTable(QDSCodeListSearchModel results){
		if(results == null) {
			return;
		}
		//buildPageSizeSelector();
		buildTableQDS(results);
	}*/
	
	/*public void buildTableQDS( QDSCodeListSearchModel results){
		 
		CellTable<CodeListSearchDTO> table = new CellTable<CodeListSearchDTO>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
		  
		// Display 50 rows in one page or all records.
		
		table.setPageSize(50);
		table.setSelectionModel(results.addSelectionHandlerOnTable());
		table = results.addColumnToTable(table);
		
		table.redraw();
		sortProvider.refresh();
		sortProvider.setList(results.getData());
	
		sortProvider.addDataDisplay(table);
		//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
		MatSimplePager spager;
		//SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
       // SimplePager spager = new SimplePager(TextLocation.CENTER, false, 0, true);
        //spager.setRangeLimited(false);
        spager.setDisplay(table);
        spager.setPageStart(0);
        spager.setToolTipAndTabIndex(spager);
        VerticalPanel vp = new VerticalPanel();
        vp.add(spager);
        vPanelForQDMTable.clear();
		vPanelForQDMTable.add(table);
		vPanelForQDMTable.add(new SpacerWidget());
		vPanelForQDMTable.add(vp);

	}*/
	
	//build data table for user search
	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 */
	public void buildDataTable(final SearchResults<T> results){		
		buildDataTable(results, true,false);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for view
	/**
	 * Builds the version data table.
	 * 
	 * @param results
	 *            the results
	 * @param pageCount
	 *            the page count
	 * @param totalResults
	 *            the total results
	 * @param currentPage
	 *            the current page
	 * @param pageSize
	 *            the page size
	 */
	public void buildVersionDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){		
		buildDataTable(results, true,false, pageCount, totalResults, currentPage, pageSize);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for draft
	/**
	 * Builds the draft data table.
	 * 
	 * @param results
	 *            the results
	 * @param pageCount
	 *            the page count
	 * @param totalResults
	 *            the total results
	 * @param currentPage
	 *            the current page
	 * @param pageSize
	 *            the page size
	 */
	public void buildDraftDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){		
		buildDataTable(results, true,false, pageCount, totalResults, currentPage, pageSize);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for view and draft
	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 * @param isAscending
	 *            the is ascending
	 * @param isChecked
	 *            the is checked
	 * @param pageCount
	 *            the page count
	 * @param totalResults
	 *            the total results
	 * @param currentPage
	 *            the current page
	 * @param pageSize
	 *            the page size
	 */
	public void buildDataTable(final SearchResults<T> results, boolean isAscending,boolean isChecked, int pageCount,long totalResults,int currentPage,int pageSize){
		if(results == null) {
			return;
		}
		int numRows = Math.min(results.getNumberOfRows(), pageSize);
		int numColumns = results.getNumberOfColumns();
		dataTable.clear();
		dataTable.resize((int)numRows + 1, (int)numColumns);
		buildSearchResultsColumnHeaders(numRows,numColumns,results, isAscending,isChecked);
		buildSearchResults(numRows,numColumns,results);
		int viewStartNumber = findViewingNumber(pageSize,currentPage);
		setViewingRange2(viewStartNumber, viewStartNumber + numRows - 1, totalResults);
		buildPageSizeSelector();
		buildPageSelector(pageCount);
	}
	
	//build data table for user search
	/**
	 * Builds the data table.
	 * 
	 * @param results
	 *            the results
	 * @param isAscending
	 *            the is ascending
	 * @param isChecked
	 *            the is checked
	 */
	public void buildDataTable(final SearchResults<T> results, boolean isAscending,boolean isChecked){
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		dataTable.clear();
		dataTable.resize((int)numRows + 1, (int)numColumns);
		buildSearchResultsColumnHeaders(numRows,numColumns,results, isAscending,isChecked);
		buildSearchResults(numRows,numColumns,results);
        setViewingRange(results.getStartIndex(), 
				results.getStartIndex() + numRows - 1, 
				results.getResultsTotal());
		buildPageSizeSelector();
	}
	
	/**
	 * Builds the search results column headers.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 * @param isAscending
	 *            the is ascending
	 * @param isChecked
	 *            the is checked
	 */
	public void buildSearchResultsColumnHeaders(int numRows,int numColumns,SearchResults<T> results, boolean isAscending,boolean isChecked){
		boolean isClearAll = false;
		for(int i = 0; i < numColumns; i++) {
			Panel headerPanel = new FlowPanel();
			Widget columnHeader = null;
			if(results.isColumnSortable(i)){
				HorizontalPanel headingPanel = new HorizontalPanel();
				Label headerName = new Label(results.getColumnHeader(i));
				Anchor sortingAnchor = new Anchor(ARROW_DOWN+ARROW_UP);
				sortingAnchor.getElement().getStyle().setCursor(Cursor.POINTER);
				headingPanel.add(headerName);
				headingPanel.add(sortingAnchor);
				columnHeader = headingPanel;
				final int columnIndex = i;
				((Anchor) sortingAnchor).addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
					
						MatContext.get().clearDVIMessages();
						
						sortColumnIndex = columnIndex;
						PageSortEvent evt = new PageSortEvent(columnIndex);
						SearchView.this.fireEvent(evt);
					}
				});
				columnHeader.setStylePrimaryName("sortAnchor");
				 
				String title = "";
				if(i == sortColumnIndex){
					title = "Sorted by "+results.getColumnHeader(i);
					
					if(isAscending)
						title = title + " in ascending order";
					else
						title = title + " in descending order";
					
					//title = title + ". Select to change sort order.";
				}
				else
					title = "Select to sort by "+results.getColumnHeader(i)+".";
				columnHeader.setTitle(title);
			}else if(results.isColumnSelectAll(i)){
				CustomCheckBox selectAllCheckBox = new CustomCheckBox(results.getColumnHeader(i),true);
				selectAllCheckBox.setValue(isChecked);
				String title ="Select All";//default title
				selectAllCheckBox.setStyleName("noWrap");
				selectAllCheckBox.setTitle(title);
				if(isChecked){
					title = "Select None";
					selectAllCheckBox.setTitle(title);
				}
					
				selectAllCheckBox.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						MatContext.get().clearDVIMessages();
						boolean checked = ((CustomCheckBox) event.getSource()).getValue();
				        SelectAllEvent evt = new SelectAllEvent(checked);
				        SearchView.this.fireEvent(evt);
						
					}
				});
				columnHeader = selectAllCheckBox;
				columnHeader.setTitle(title);
			}
			else{
				if("ExportClear".equals(results.getColumnHeader(i))){
					isClearAll = true;
					HorizontalPanel panel = new HorizontalPanel();
					panel.add(new Label("Export"));
					Anchor clearAnchor = new Anchor("(Clear)");
					clearAnchor.getElement().setId("clearAnchor_Anchor");
					clearAnchor.setTitle("Clear All Selection");
					clearAnchor.setStyleName("clearAnchorStyle");
					
					clearAnchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(MatContext.get().getManageMeasureSearchModel().getSelectedExportIds() != null){
								MatContext.get().getManageMeasureSearchModel().getSelectedExportIds().clear();
							}
							MatContext.get().getManageMeasureSearchView().clearBulkExportCheckBoxes(dataTable);
							MatContext.get().getManageMeasureSearchView().getErrorMessageDisplayForBulkExport().clear();
						}
					});
					panel.add(clearAnchor);
					panel.add(LabelBuilder.buildInvisibleLabel(new Label(), "clearSelections"));
					headerPanel.add(panel);
				}else if("TransferClear".equals(results.getColumnHeader(i))){
					isClearAll = true;
					HorizontalPanel panel = new HorizontalPanel();
					panel.add(new Label("Transfer"));
					Anchor clearAnchor = new Anchor("(Clear)");
					clearAnchor.getElement().setId("clearAnchor_Anchor");
					clearAnchor.setTitle("Clear Selection");
					clearAnchor.setStyleName("clearAnchorStyle");
					clearAnchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(MatContext.get().getManageCodeListSearchModel().getTransferValueSetIDs() != null){
								MatContext.get().getManageCodeListSearchModel().getTransferValueSetIDs().clear();
								MatContext.get().getManageCodeListSearchModel().getLisObjectId().clear();
							}
							MatContext.get().getManageCodeListSearchView().clearAllCheckBoxes(dataTable);
							MatContext.get().getManageCodeListSearchView().getErrorMessageDisplay().clear();
						}
					});
					panel.add(clearAnchor);
					headerPanel.add(panel);
				}/*else if("TransferMeasureClear".equals(results.getColumnHeader(i))){
					isClearAll = true;
					HorizontalPanel panel = new HorizontalPanel();
					panel.add(new Label("Transfer"));
					Anchor clearAnchor = new Anchor("(Clear)");
					clearAnchor.setTitle("Clear All Selection");
					clearAnchor.setStyleName("clearAnchorStyle");
					clearAnchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(MatContext.get().getManageMeasureSearchModel().getSelectedTransferResults() != null){
								MatContext.get().getManageMeasureSearchModel().getSelectedTransferResults().clear();
								MatContext.get().getManageMeasureSearchModel().getSelectedTransferIds().clear();
							}
							MatContext.get().getManageMeasureSearchView().clearBulkExportCheckBoxes(dataTable);
							//MatContext.get().getManageMeasureSearchView().getErrorMessagesForTransferOS().clear();
						}
					});
					panel.add(clearAnchor);
					headerPanel.add(panel);
				}*/
				else{
					columnHeader = new Label(results.getColumnHeader(i));
					columnHeader.setTitle(results.getColumnHeader(i));
					//Need to do this for IE or it centers them
					columnHeader.setStyleName("leftAligned");
				}
				
			}
			headerPanel.setStylePrimaryName("noBorder");
			if(!isClearAll) 
				headerPanel.add(columnHeader);
			dataTable.setWidget(0, i,headerPanel);
			dataTable.getColumnFormatter().setWidth(i, results.getColumnWidth(i));
			dataTable.getColumnFormatter().addStyleName(i, "noWrap");
		}
		dataTable.getRowFormatter().addStyleName(0, "header");
	}
	
	/**
	 * Builds the search results.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 */
	protected void buildSearchResults(int numRows,int numColumns,final SearchResults<T> results){		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
			    	Anchor a = new Anchor(results.getValue(i, j).getElement().getInnerText());
					final int rowIndex = i;
					addClickHandler(a, results, rowIndex);
					dataTable.setWidget(i+1, j, a);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(i % 2 == 0) {
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}
		}
	}
	
	/**
	 * Adds the click handler.
	 * 
	 * @param w
	 *            the w
	 * @param results
	 *            the results
	 * @param rowIndex
	 *            the row index
	 */
	protected void addClickHandler(HasClickHandlers w, final SearchResults<T> results, final int rowIndex){
		w.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				SelectionEvent.fire(SearchView.this, results.get(rowIndex));
			}
		});
	}
	
	/**
	 * Builds the history search results.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 */
	private void buildHistorySearchResults(int numRows,int numColumns,final SearchResults<T> results){
		int tableRow = 1;
		String rowStyle = "odd";
		for(int i = 0; i < numRows; i++) {			
			//boolean isUserComment = false;
		//	String additionalInfo = "";
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {					
					Label text = new Label(results.getValue(i, j).getElement().getInnerText());
					//final int rowIndex = i;					
					dataTable.setWidget(tableRow, j, text);					
				}
				else {
					dataTable.setWidget(tableRow, j,results.getValue(i, j));
				}
				/*if(j==0 && results.getValue(i, j).getElement().getInnerText().equalsIgnoreCase("User Comment")){
					isUserComment = true;
					additionalInfo = results.getValue(i, 3).getElement().getInnerText();
				}*/				
				dataTable.getCellFormatter().addStyleName(tableRow, j, "historycol");
			}

			dataTable.getRowFormatter().addStyleName(tableRow, "historyrow");
			if(rowStyle.equalsIgnoreCase("odd")) {
				rowStyle = "even";				
			}else{
				rowStyle = "odd";
				
			}
			dataTable.getRowFormatter().addStyleName(tableRow, rowStyle);
			
			tableRow++;
		}
	}

	
	/**
	 * Sets the viewing range2.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param total
	 *            the total
	 */
	protected void setViewingRange2(long start, long end, long total) {
		if(total == 0) {
//			((HTML) viewingNumber.getWidget()).setHTML("No Records Found");
			viewingNumber.setHTML("No Records Found"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		else if(start == 1 && end == total) {
//			((HTML) viewingNumber.getWidget()).setHTML("Viewing <b>" + total + 
			viewingNumber.setHTML("Viewing <b>" +start + " - " +total + 
					"</b> of <b>" + total + "</b> " + descriptor);
		}
		else {
//			((HTML) viewingNumber.getWidget()).setHTML("Viewing <b>" + 
			viewingNumber.setHTML("Viewing <b>" + 
					+start + " - " +end + "</b> of <b>" + total + "</b> " + descriptor);
		}
	}
	
	
	
	/**
	 * Sets the viewing range.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param total
	 *            the total
	 */
	public void setViewingRange(long start, long end, long total) {
		if(total == 0) {
//			((HTML) viewingNumber.getWidget()).setHTML("No Records Found");
			viewingNumber.setHTML("No Records Found ");
		}
		else if(start == 1 && end == total) {
//			((HTML) viewingNumber.getWidget()).setHTML("Viewing <b>" + total + 
			viewingNumber.setHTML("Viewing <b>" + total + 
					"</b> of <b>" + total + "</b> " + descriptor);
		}
		else {
//			((HTML) viewingNumber.getWidget()).setHTML("Viewing <b>" + 
			viewingNumber.setHTML("Viewing <b>" + 
					end + "</b> of <b>" + total + "</b> " + descriptor);
		}
	}
	
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<T> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.HasPageSelectionHandler#addPageSelectionHandler(mat.client.shared.search.PageSelectionEventHandler)
	 */
	@Override
	public HandlerRegistration addPageSelectionHandler(
			PageSelectionEventHandler handler) {
		return handlerManager.addHandler(PageSelectionEvent.getType(), handler);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.HasPageSizeSelectionHandler#addPageSizeSelectionHandler(mat.client.shared.search.PageSizeSelectionEventHandler)
	 */
	@Override
	public HandlerRegistration addPageSizeSelectionHandler(
			PageSizeSelectionEventHandler handler) {
		return handlerManager.addHandler(PageSizeSelectionEvent.getType(), handler);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.HasSortHandler#addPageSortHandler(mat.client.shared.search.PageSortEventHandler)
	 */
	@Override
	public HandlerRegistration addPageSortHandler(PageSortEventHandler handler) {
		return handlerManager.addHandler(PageSortEvent.getType(), handler);
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return currentPageSize;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.HasSelectAllHandler#addSelectAllHandler(mat.client.shared.search.SelectAllEventHandler)
	 */
	@Override
	public HandlerRegistration addSelectAllHandler(SelectAllEventHandler handler) {
		return handlerManager.addHandler(SelectAllEvent.TYPE, handler);
	}
	
	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param pageNumber
	 *            the new current page
	 */
	public void setCurrentPage(int pageNumber) {
		currentPage = pageNumber;
	}
	
	/**
	 * implementation of Enableable interface consider setting enablement of
	 * search page components.
	 * 
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled){
		
		//dataTable
		dataTable.setEnabled(enabled);
		
		//qdsDataTable (radio buttons)
		//qdsDataTable.setEnabled(enabled);
		
		//page size anchors 10|50|100|All
		Iterator<Widget> iter = pageSizeSelector.iterator();
		while(iter.hasNext()){
			Widget w = iter.next();
			if(w instanceof FocusWidget){
				((FocusWidget)w).setEnabled(enabled);
			}
		}
		
	}
	
	/**
	 * Gets the data table.
	 * 
	 * @return the dataTable
	 */
	public Grid508 getDataTable() {
		return dataTable;
	}
	
	/**
	 * Sets the data table.
	 * 
	 * @param dataTable
	 *            the dataTable to set
	 */
	public void setDataTable(Grid508 dataTable) {
		this.dataTable = dataTable;
	}
	
	/**
	 * Gets the v panel for qdm table.
	 * 
	 * @return the v panel for qdm table
	 */
	public VerticalPanel getvPanelForQDMTable() {
		return vPanelForQDMTable;
	}
	
	
	
}
