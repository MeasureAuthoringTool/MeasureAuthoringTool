package mat.client.shared.search;	



import java.util.Iterator;

import mat.client.Enableable;
import mat.client.event.MATClickHandler;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

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
import com.google.gwt.user.client.ui.Widget;

public class SearchView<T> implements HasSelectionHandlers<T>, 
	HasPageSelectionHandler, HasPageSizeSelectionHandler, HasSortHandler,HasSelectAllHandler, Enableable {
	
	public static final int PAGE_SIZE_ALL = Integer.MAX_VALUE;
	private static final int[] PAGE_SIZES= new int[] {50, PAGE_SIZE_ALL};
	private static final int[] HISTORY_PAGE_SIZES = new int[] {10, 50, 100};
	private static final String ARROW_DOWN = "\u25bc";
	private static final String ARROW_UP = "\u25b2";
	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_SIZE = 50;
	
	private Panel mainPanel;
	private HandlerManager handlerManager = new HandlerManager(this);
	
	protected Panel pageSizeSelector = new FlowPanel();
	protected Panel pageSelector = new HorizontalPanel();
	private HTML viewingNumber = new HTML();
	public Grid508 dataTable = new Grid508();
	private Grid508 qdsDataTable = new Grid508();
	//private FlexTable flexTable = new FlexTable();
	
	private int currentPageSize = DEFAULT_PAGE_SIZE;
	private int currentPage = DEFAULT_PAGE;
	private String descriptor = "";
	
	private int sortColumnIndex = 0;
	
	public SearchView(String descriptor) {
		this();
		this.descriptor = descriptor;
	}
	public SearchView() {
		dataTable.setCellPadding(5);
		dataTable.setStylePrimaryName("searchResultsTable");
		viewingNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		mainPanel = new SimplePanel();
		mainPanel.setStylePrimaryName("searchResultsContainer");
		
		FlowPanel fPanel = new FlowPanel();
		dataTable.setWidth("98%");
		mainPanel.add(fPanel);
		
		fPanel.add(pageSizeSelector);
		pageSizeSelector.setStylePrimaryName("searchResultsPageSize");
		fPanel.add(viewingNumber);
		fPanel.add(new SpacerWidget());
		fPanel.add(dataTable);	
		fPanel.add(new SpacerWidget());
		fPanel.add(pageSelector);
	}
	
	public SearchView(boolean QDSCodeListView){
		mainPanel = new SimplePanel();
		FlowPanel fPanel = new FlowPanel();
		qdsDataTable.setWidth("100%");
		qdsDataTable.setStylePrimaryName("searchResultsTable");
		qdsDataTable.setCellPadding(5);
		mainPanel.add(fPanel);
		pageSizeSelector.setHeight("20px");
		fPanel.add(pageSizeSelector);
		pageSizeSelector.setStylePrimaryName("searchResultsPageSize");
		fPanel.add(viewingNumber);
		fPanel.add(new SpacerWidget());
		fPanel.add(new SpacerWidget());
		fPanel.add(qdsDataTable);
	}

	public void setPageSizeVisible(boolean visible) {
		pageSizeSelector.setVisible(visible);
	}
	public void setViewingNumberVisible(boolean visible) {
		viewingNumber.setVisible(visible);
	}
	public void setPageSize(int i) {
		currentPageSize = i;
	}
	public void buildPageSizeSelector() {
		pageSizeSelector.clear();
		pageSelector.setHeight("30px");
		pageSizeSelector.add(new HTML("View:&nbsp; "));
		for(int i = 0; i < PAGE_SIZES.length; i++) {
			pageSizeSelector.add(buildPageSizeLink(PAGE_SIZES[i]));
			pageSizeSelector.add(new HTML("&nbsp;|&nbsp;"));
		}
		
	}
	
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
	 * @param pageCount
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
	
	private Widget buildPageLink(final int pageNumber) {
		return buildPageLink(pageNumber, null);
	}

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
	
	private Widget buildPageSizeLink(final int size) {
		String label = (size != PAGE_SIZE_ALL) ? Integer.toString(size) : "All";
	
		if(currentPageSize != size) {
			MATAnchor a = new MATAnchor(label);
			a.addClickHandler(new MATClickHandler() {
				@Override
				public void onEvent(GwtEvent event) {
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

	public void buildHistoryDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
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

	
	protected int findViewingNumber(int pageSize,int currentPage){
		return pageSize * (currentPage - 1) + 1;	
	}
	
	public void buildQDSDataTable(final SearchResults<T> results){
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		qdsDataTable.clear();
		qdsDataTable.resize((int)numRows + 1, (int)numColumns);
		buildQDSColumnHeaders(numRows,numColumns,results);
		buildQDSSearchResults(numRows,numColumns,results);
		buildPageSizeSelector();
	}
	
	public void buildQDSColumnHeaders(int numRows,int numColumns,SearchResults<T> results){
		boolean isClearAll = false;
		for(int i = 0; i < numColumns; i++) {
			Panel headerPanel = new FlowPanel();
			Widget columnHeader = null;
			columnHeader = new Label(results.getColumnHeader(i));
			columnHeader.setTitle(results.getColumnHeader(i));
			columnHeader.setStyleName("leftAligned");
			headerPanel.setStylePrimaryName("noBorder");
			if(!isClearAll) 
				headerPanel.add(columnHeader);
			qdsDataTable.setWidget(0, i,headerPanel);
			qdsDataTable.getColumnFormatter().setWidth(i, results.getColumnWidth(i));
			qdsDataTable.getColumnFormatter().addStyleName(i, "noWrap");
		}
		qdsDataTable.getRowFormatter().addStyleName(0, "header");
	}
	
	//build data table for user search
	public void buildDataTable(final SearchResults<T> results){		
		buildDataTable(results, true,false);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for view
	public void buildVersionDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){		
		buildDataTable(results, true,false, pageCount, totalResults, currentPage, pageSize);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for draft
	public void buildDraftDataTable(final SearchResults<T> results, int pageCount,long totalResults,int currentPage,int pageSize){		
		buildDataTable(results, true,false, pageCount, totalResults, currentPage, pageSize);//Default value for isAscending is true and isChecked is false.
	}
	
	//build data table for view and draft
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
	
	public void buildSearchResultsColumnHeaders(int numRows,int numColumns,SearchResults<T> results, boolean isAscending,boolean isChecked){
		boolean isClearAll = false;
		for(int i = 0; i < numColumns; i++) {
			Panel headerPanel = new FlowPanel();
			Widget columnHeader = null;
			if(results.isColumnSortable(i)){
				columnHeader = new Anchor(results.getColumnHeader(i)+ARROW_DOWN+ARROW_UP);
				
				final int columnIndex = i;
				((Anchor) columnHeader).addClickHandler(new ClickHandler() {
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
					
					title = title + ". Select to change sort order.";
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
					headerPanel.add(panel);
				}else if("TransferClear".equals(results.getColumnHeader(i))){
					isClearAll = true;
					HorizontalPanel panel = new HorizontalPanel();
					panel.add(new Label("Transfer"));
					Anchor clearAnchor = new Anchor("(Clear)");
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
				}else if("TransferMeasureClear".equals(results.getColumnHeader(i))){
					isClearAll = true;
					HorizontalPanel panel = new HorizontalPanel();
					panel.add(new Label("Transfer"));
					Anchor clearAnchor = new Anchor("(Clear)");
					clearAnchor.setStyleName("clearAnchorStyle");
					clearAnchor.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(MatContext.get().getManageMeasureSearchModel().getSelectedTransferResults() != null){
								MatContext.get().getManageMeasureSearchModel().getSelectedTransferResults().clear();
								MatContext.get().getManageMeasureSearchModel().getSelectedTransferIds().clear();
							}
							MatContext.get().getManageMeasureSearchView().clearBulkExportCheckBoxes(dataTable);
							MatContext.get().getManageMeasureSearchView().getErrorMessagesForTransferOS().clear();
						}
					});
					panel.add(clearAnchor);
					headerPanel.add(panel);
				}
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
	
	protected void addClickHandler(HasClickHandlers w, final SearchResults<T> results, final int rowIndex){
		w.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				SelectionEvent.fire(SearchView.this, results.get(rowIndex));
			}
		});
	}
	
	private void buildHistorySearchResults(int numRows,int numColumns,final SearchResults<T> results){
		int tableRow = 1;
		String rowStyle = "odd";
		for(int i = 0; i < numRows; i++) {			
			boolean isUserComment = false;
			String additionalInfo = "";
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {					
					Label text = new Label(results.getValue(i, j).getElement().getInnerText());
					final int rowIndex = i;					
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

	
	private void buildQDSSearchResults(int numRows,int numColumns,final SearchResults<T> results){
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				Widget widget = null;
				
				if(j == 0){
					CodeListSearchDTO clsdto = (CodeListSearchDTO) results.get(i);
					StringBuilder title = new StringBuilder();
					String steward = clsdto.getSteward();
					if(steward.equalsIgnoreCase("Other")){
						steward = clsdto.getStewardOthers();
					}
					
					title.append("Name : ").append(clsdto.getName()).append("\n").append("OID : ").append(clsdto.getOid()).append("\n").append("Steward : ").append(steward);
					widget = results.getValue(i, j);
					widget.getElement().setAttribute("title", title.toString());
					widget.setStylePrimaryName("pad-left5Right55px");
				}else{
					widget = results.getValue(i, j);
					widget.setStylePrimaryName("pad-left5Right21px");
				}
				
				qdsDataTable.setWidget(i+1, j,widget);
				qdsDataTable.getColumnFormatter().setWidth(j, results.getColumnWidth(j));
				
			}
			if(i % 2 == 0) {
				qdsDataTable.getRowFormatter().removeStyleName(i + 1, "odd");
				qdsDataTable.getRowFormatter().addStyleName(i+1,"noWrap");
				
			}else{
				qdsDataTable.getRowFormatter().addStyleName(i + 1, "odd");
				qdsDataTable.getRowFormatter().addStyleName(i+1,"noWrap");
			}
			//
			
		}
	}
	
	
	private Widget buildPageSelectionAnchor(String label, final int page) {
		Anchor a = new Anchor(label);
		a.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				selectPage(page);
			}
		});
		return a;
	}
	private void selectPage(int page) {
		PageSelectionEvent event = new PageSelectionEvent(page);
		this.fireEvent(event);
	}
	private Widget buildPagingSpacer() {
		HTML spacer = new HTML("&nbsp");
		spacer.setStylePrimaryName("spacer");
		return spacer;
		
	}
	
	
	
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
	
	
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<T> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addPageSelectionHandler(
			PageSelectionEventHandler handler) {
		return handlerManager.addHandler(PageSelectionEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addPageSizeSelectionHandler(
			PageSizeSelectionEventHandler handler) {
		return handlerManager.addHandler(PageSizeSelectionEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addPageSortHandler(PageSortEventHandler handler) {
		return handlerManager.addHandler(PageSortEvent.getType(), handler);
	}

	public int getPageSize() {
		return currentPageSize;
	}
	@Override
	public HandlerRegistration addSelectAllHandler(SelectAllEventHandler handler) {
		return handlerManager.addHandler(SelectAllEvent.TYPE, handler);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int pageNumber) {
		currentPage = pageNumber;
	}
	
	/**
	 * implementation of Enableable interface
	 * consider setting enablement of search page components
	 */
	public void setEnabled(boolean enabled){
		
		//dataTable
		dataTable.setEnabled(enabled);
		
		//qdsDataTable (radio buttons)
		qdsDataTable.setEnabled(enabled);
		
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
	 * @return the dataTable
	 */
	public Grid508 getDataTable() {
		return dataTable;
	}
	/**
	 * @param dataTable the dataTable to set
	 */
	public void setDataTable(Grid508 dataTable) {
		this.dataTable = dataTable;
	}
}
