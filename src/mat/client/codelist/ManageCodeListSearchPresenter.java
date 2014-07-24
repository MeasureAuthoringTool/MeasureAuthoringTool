package mat.client.codelist;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.codelist.events.CancelEditCodeListEvent;
import mat.client.codelist.events.EditCodeListEvent;
import mat.client.codelist.events.EditGroupedCodeListEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.history.HistoryModel;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HasVisible;
import mat.client.shared.MatContext;
import mat.client.shared.PreviousContinueButtonBar;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSortHandler;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.PageSizeSelectionEvent;
import mat.client.shared.search.PageSizeSelectionEventHandler;
import mat.client.shared.search.PageSortEvent;
import mat.client.shared.search.PageSortEventHandler;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageCodeListSearchPresenter.
 */
public class ManageCodeListSearchPresenter {
	
	/** The my value sets. */
	private  String MY_VALUE_SETS = "My Value Sets";
	
	/** The Constant MY_VALUE_SETS_CREATE_DRAFT. */
	private static final String MY_VALUE_SETS_CREATE_DRAFT = "My Value Sets  > Create a Value Set Draft";
	
	/** The my value sets history. */
	private String MY_VALUE_SETS_HISTORY = "My Value Sets  > History";
	
	/** The is observer busy. */
	private boolean isObserverBusy = false;
	
	/** The empty search string. */
	private String emptySearchString = "";
	
	/**
	 * The Interface ValueSetSearchDisplay.
	 */
	public static interface ValueSetSearchDisplay extends mat.client.shared.search.SearchDisplay {
		
		/**
		 * Gets the select id for edit tool.
		 * 
		 * @return the select id for edit tool
		 */
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForEditTool();
		
		/**
		 * Gets the select id for qds element.
		 * 
		 * @return the select id for qds element
		 */
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 */
		public void buildDataTable(SearchResults<CodeListSearchDTO> results);
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 * @param isAsc
		 *            the is asc
		 */
		public void buildDataTable(SearchResults<CodeListSearchDTO> results, boolean isAsc);
		
		/**
		 * Gets the page sort tool.
		 * 
		 * @return the page sort tool
		 */
		public HasSortHandler getPageSortTool();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		// Code commented for User Story MAT-2372 : Remove Value Set Creation.
		//public HasClickHandlers getCreateButton();
		//public void clearSelections();
	//	public String getSelectedOption();
		/* (non-Javadoc)
		 * @see mat.client.shared.search.SearchDisplay#getPageSelectionTool()
		 */
		public HasPageSelectionHandler getPageSelectionTool();
		
		/* (non-Javadoc)
		 * @see mat.client.shared.search.SearchDisplay#getPageSizeSelectionTool()
		 */
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/* (non-Javadoc)
		 * @see mat.client.shared.search.SearchDisplay#getPageSize()
		 */
		public int getPageSize();
		
		/**
		 * Gets the value set search filter panel.
		 * 
		 * @return the value set search filter panel
		 */
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		
		/**
		 * Gets the data table.
		 * 
		 * @return the data table
		 */
		public Grid508 getDataTable();
		
		/**
		 * Clear all check boxes.
		 * 
		 * @param dataTable
		 *            the data table
		 */
		void clearAllCheckBoxes(Grid508 dataTable);
	}
	
	
	/**
	 * The Interface AdminValueSetSearchDisplay.
	 */
	public static interface AdminValueSetSearchDisplay {
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 */
		public void buildDataTable(SearchResults<CodeListSearchDTO> results);
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the transfer error message display.
		 * 
		 * @return the transfer error message display
		 */
		public ErrorMessageDisplayInterface getTransferErrorMessageDisplay();
		
		/**
		 * Gets the transfer button.
		 * 
		 * @return the transfer button
		 */
		public HasClickHandlers getTransferButton();
		
		/**
		 * Gets the clear button.
		 * 
		 * @return the clear button
		 */
		public HasClickHandlers getClearButton();
		
		/**
		 * Gets the search button.
		 * 
		 * @return the search button
		 */
		public HasClickHandlers getSearchButton();
		
		/**
		 * Gets the search string.
		 * 
		 * @return the search string
		 */
		public HasValue<String> getSearchString();
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		public int getPageSize();
		
		/**
		 * Gets the value set search filter panel.
		 * 
		 * @return the value set search filter panel
		 */
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		
		/**
		 * Sets the adapter.
		 * 
		 * @param adapter
		 *            the new adapter
		 */
		public void setAdapter(AdminCodeListSearchResultsAdapter adapter);
		
		/**
		 * Clear transfer check boxes.
		 */
		public void clearTransferCheckBoxes();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
	}	
	
	/**
	 * The Interface HistoryDisplay.
	 */
	public static interface HistoryDisplay {
		
		/**
		 * Sets the code list id.
		 * 
		 * @param codeListId
		 *            the new code list id
		 */
		public void setCodeListId(String codeListId);
		
		/**
		 * Gets the code list id.
		 * 
		 * @return the code list id
		 */
		public String getCodeListId();
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the save button.
		 *
		 * @param results the results
		 * @param pageCount the page count
		 * @param totalResults the total results
		 * @param currentPage the current page
		 * @param pageSize the page size
		 * @return the save button
		 */
		//public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the clear button.
		 * 
		 * @return the clear button
		 */
		//public HasClickHandlers getClearButton();
		
		/**
		 * Gets the user comment.
		 * 
		 * @return the user comment
		 */
		//public HasValue<String> getUserComment();
		
		/**
		 * Builds the data table.
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
		public void buildDataTable(SearchResults<AuditLogDTO> results, int pageCount,long totalResults,int currentPage,int pageSize);
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		public int getPageSize();
		
		/**
		 * Sets the page size.
		 * 
		 * @param pageNumber
		 *            the new page size
		 */
		public void setPageSize(int pageNumber);
		
		/**
		 * Gets the current page.
		 * 
		 * @return the current page
		 */
		public int getCurrentPage();
		
		/**
		 * Sets the current page.
		 * 
		 * @param pageNumber
		 *            the new current page
		 */
		public void setCurrentPage(int pageNumber);		
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		public HasPageSelectionHandler getPageSelectionTool();
		
		/**
		 * Reset.
		 *
		 * @param s the new error message
		 */
		//public void reset();
		
		/**
		 * Sets the error message.
		 * 
		 * @param s
		 *            the new error message
		 */
		public void setErrorMessage(String s);
		
		/**
		 * Clear error message.
		 */
		public void clearErrorMessage();
		
		/**
		 * Sets the code list name.
		 * 
		 * @param name
		 *            the new code list name
		 */
		public void setCodeListName(String name);
		
		/**
		 * Gets the code list name.
		 * 
		 * @return the code list name
		 */
		public String getCodeListName();
		
		/**
		 * Sets the return to link text.
		 * 
		 * @param s
		 *            the new return to link text
		 */
		public void setReturnToLinkText(String s);
		
		/**
		 * Gets the return to link.
		 * 
		 * @return the return to link
		 */
		public HasClickHandlers getReturnToLink();
		
		/**
		 * Sets the user comments read only.
		 * 
		 * @param readOnly
		 *            the new user comments read only
		 */
		//public void setUserCommentsReadOnly(boolean readOnly);
		
	}
	
	/**
	 * The Interface TransferDisplay.
	 */
	public static interface TransferDisplay{
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 * @param codeListIDs
		 *            the code list i ds
		 */
		void buildDataTable(SearchResults<TransferOwnerShipModel.Result> results , List<CodeListSearchDTO> codeListIDs);
		
		/**
		 * Gets the selected value.
		 * 
		 * @return the selected value
		 */
		public String getSelectedValue();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Builds the html for value sets.
		 * 
		 * @param codeListIDs
		 *            the code list i ds
		 */
		void buildHTMLForValueSets(List<CodeListSearchDTO> codeListIDs);
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		public HasPageSelectionHandler getPageSelectionTool();
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		public int getPageSize();
		
		/**
		 * Clear all radio buttons.
		 * 
		 * @param dataTable
		 *            the data table
		 */
		void clearAllRadioButtons(Grid508 dataTable);
		
		/**
		 * Gets the data table.
		 * 
		 * @return the data table
		 */
		public Grid508 getDataTable();
	}
	
	
	/**
	 * The Interface DraftDisplay.
	 */
	public static interface DraftDisplay{
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Builds the data table.
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
		public void buildDataTable(SearchResults<ManageValueSetSearchModel.Result> results,int pageCount,long totalResults,int currentPage,int pageSize);
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		public int getPageSize();
		
		/**
		 * Sets the page size.
		 * 
		 * @param pageNumber
		 *            the new page size
		 */
		public void setPageSize(int pageNumber);
		
		/**
		 * Gets the current page.
		 * 
		 * @return the current page
		 */
		public int getCurrentPage();
		
		/**
		 * Sets the current page.
		 * 
		 * @param pageNumber
		 *            the new current page
		 */
		public void setCurrentPage(int pageNumber);		
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		public HasPageSelectionHandler getPageSelectionTool();
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	}
	
	/** The history display. */
	private HistoryDisplay historyDisplay;
	
	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	/** The search display. */
	private ValueSetSearchDisplay searchDisplay;
	
	/** The admin search display. */
	private AdminValueSetSearchDisplay adminSearchDisplay;
	
	/** The last search text. */
	private String lastSearchText;
	
	/** The transfer display. */
	private TransferDisplay transferDisplay;
	
	/** The last start index. */
	private int lastStartIndex;
	
	/** The current sort column. */
	private String currentSortColumn = getSortKey(0);
	
	/** The sort is ascending. */
	private boolean sortIsAscending = true;
	
	/** The default code list. */
	private boolean defaultCodeList = false;
	
	/** The button bar. */
	private PreviousContinueButtonBar buttonBar = null;
	
	/** The history model. */
	private HistoryModel historyModel;
	
	/** The draft display. */
	private DraftDisplay draftDisplay;
	
	/** The draft value set results. */
	private ManageValueSetModel draftValueSetResults;
	
	/** The current details. */
	private ManageCodeListDetailModel currentDetails;
	
	/** The start index. */
	private int startIndex = 1;
	
	/** The model. */
	private TransferOwnerShipModel model = null;
	
	/** The search model. */
	private AdminManageCodeListSearchModel searchModel;
	
	/** The cancel click handler. */
	private ClickHandler cancelClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			draftDisplay.getErrorMessageDisplay().clear();
			draftDisplay.getSuccessMessageDisplay().clear();
			// Code commented for User Story MAT-2372 : Remove Value Set Creation.
			//searchDisplay.clearSelections();
			MatContext.get().getEventBus().fireEvent(new CancelEditCodeListEvent());
			displaySearch();
		}
	};
	
	/**
	 * Instantiates a new manage code list search presenter.
	 * 
	 * @param sDisplayArg
	 *            the s display arg
	 * @param adminValueSetDisplayArg
	 *            the admin value set display arg
	 * @param argHistDisplay
	 *            the arg hist display
	 * @param prevContButtons
	 *            the prev cont buttons
	 * @param dDisplay
	 *            the d display
	 */
	public ManageCodeListSearchPresenter(ValueSetSearchDisplay sDisplayArg,AdminValueSetSearchDisplay adminValueSetDisplayArg ,HistoryDisplay argHistDisplay, HasVisible prevContButtons, DraftDisplay dDisplay) {
		this.searchDisplay = sDisplayArg;
		this.adminSearchDisplay = adminValueSetDisplayArg;
		this.historyDisplay = argHistDisplay;
		this.buttonBar = (PreviousContinueButtonBar) prevContButtons;
		this.draftDisplay = dDisplay;
		displaySearch();		
		if(searchDisplay!=null)
			searchDisplayHandlers(searchDisplay);
		adminSearchDisplayHandlers(adminSearchDisplay);
		draftDisplayHandlers(draftDisplay);	
		historyDisplayHandlers(historyDisplay); 	
		
		MatContext.get().getEventBus().addHandler(MeasureSelectedEvent.TYPE, 
				new MeasureSelectedEvent.Handler() {
					@Override
					public void onMeasureSelected(MeasureSelectedEvent event) {
						//At any point whether the user in Read-only or View-only mode, we should allow the 
						//User to create  codeList.
						//searchDisplay.setCreateButtonsVisible(event.isEditable());
					}
				});
	}
	
	/**
	 * Instantiates a new manage code list search presenter.
	 * 
	 * @param sDisplayArg
	 *            the s display arg
	 * @param adminValueSetDisplayArg
	 *            the admin value set display arg
	 * @param argHistDisplay
	 *            the arg hist display
	 * @param prevContButtons
	 *            the prev cont buttons
	 * @param dDisplay
	 *            the d display
	 * @param tDisplay
	 *            the t display
	 */
	public ManageCodeListSearchPresenter(ValueSetSearchDisplay sDisplayArg,AdminValueSetSearchDisplay adminValueSetDisplayArg ,HistoryDisplay argHistDisplay, HasVisible prevContButtons, DraftDisplay dDisplay, TransferDisplay tDisplay) {
		this.searchDisplay = sDisplayArg;
		this.adminSearchDisplay = adminValueSetDisplayArg;
		this.historyDisplay = argHistDisplay;
		this.buttonBar = (PreviousContinueButtonBar) prevContButtons;
		this.draftDisplay = dDisplay;
		this.transferDisplay = tDisplay;
		if(searchDisplay!=null)
			searchDisplayHandlers(searchDisplay);
		adminSearchDisplayHandlers(adminSearchDisplay);
		draftDisplayHandlers(draftDisplay);	
		historyDisplayHandlers(historyDisplay); 	
		transferDisplayHandlers(transferDisplay);
		MatContext.get().getEventBus().addHandler(MeasureSelectedEvent.TYPE, 
				new MeasureSelectedEvent.Handler() {
					@Override
					public void onMeasureSelected(MeasureSelectedEvent event) {
						//At any point whether the user in Read-only or View-only mode, we should allow the 
						//User to create  codeList.
						//searchDisplay.setCreateButtonsVisible(event.isEditable());
					}
				});
		displaySearch();		
	}
	
	/**
	 * Transfer display handlers.
	 * 
	 * @param transferDisplay
	 *            the transfer display
	 */
	private void transferDisplayHandlers(final TransferDisplay transferDisplay) {
		
		transferDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				transferDisplay.getErrorMessageDisplay().clear();
				transferDisplay.getSuccessMessageDisplay().clear();
				boolean userSelected = false;
				for(int i=0;i<model.getData().size();i=i+1){
					if(model.getData().get(i).isSelected()){
						userSelected = true;
						final String emailTo =model.getData().get(i).getEmailId();
						final int rowIndex = i;
						MatContext.get().getCodeListService().transferOwnerShipToUser(searchModel.getLisObjectId(),emailTo ,
								new AsyncCallback<Void>(){
									@Override
									public void onFailure(Throwable caught) {
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
										searchModel.getTransferValueSetIDs().clear();
										searchModel.getLisObjectId().clear();
										model.getData().get(rowIndex).setSelected(false);
									}
									@Override
									public void onSuccess(Void result) {
										transferDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getTransferOwnershipSuccess()+emailTo);
										searchModel.getTransferValueSetIDs().clear();
										searchModel.getLisObjectId().clear();
										model.getData().get(rowIndex).setSelected(false);
									}
						});
					}
				}
				if(userSelected==false){
					transferDisplay.getSuccessMessageDisplay().clear();
					transferDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUserRequiredErrorMessage());
					
				}
					
				transferDisplay.clearAllRadioButtons(transferDisplay.getDataTable());
			}
			
		});
		transferDisplay.getCancelButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				searchModel.getLisObjectId().clear();
				searchModel.getTransferValueSetIDs().clear();
				transferDisplay.getSuccessMessageDisplay().clear();
				transferDisplay.getErrorMessageDisplay().clear();
				@SuppressWarnings("static-access")
				int filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				search(emptySearchString,startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
	
		transferDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int startIndex = transferDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				displayTransferView(startIndex, transferDisplay.getPageSize());
			}
		});
		transferDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				displayTransferView(startIndex, transferDisplay.getPageSize());
			}
		});
	}


	/**
	 * History display handlers.
	 * 
	 * @param historyDisplay
	 *            the history display
	 */
	private void historyDisplayHandlers(final HistoryDisplay historyDisplay) {
//			historyDisplay.getSaveButton().addClickHandler(new ClickHandler(){
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				if(historyDisplay.getUserComment().getValue().length() > 2000){
//					String s = historyDisplay.getUserComment().getValue();
//					historyDisplay.getUserComment().setValue(
//							s.substring(0, 2000));
//				}
//				
//				String codeListId = historyDisplay.getCodeListId();
//				String eventType = ConstantMessages.USER_COMMENT;
//				String additionalInfo = historyDisplay.getUserComment().getValue();
//					MatContext.get().getAuditService().recordCodeListEvent(codeListId, eventType, additionalInfo, new AsyncCallback<Boolean>(){
//					@Override
//					public void onSuccess(Boolean result) {
//						//add user message
//						historyDisplay.reset();
//						history();
//					}
//
//					@Override
//					public void onFailure(Throwable caught) {
//						historyDisplay.setErrorMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
//					}
//				});
//			}
//		});
//
//		historyDisplay.getClearButton().addClickHandler(new ClickHandler(){
//		
//			@Override
//			public void onClick(ClickEvent event) {
//				historyDisplay.clearErrorMessage();
//				historyDisplay.getUserComment().setValue("");
//			}
//		});

		historyDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				historyDisplay.setPageSize(event.getPageSize());
				history();
			}
		});
		
		historyDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				historyModel.setCurrentPage(pageNumber);
				if(pageNumber == -1){ // if next button clicked
					if(historyDisplay.getCurrentPage() == historyModel.getTotalpages()){
						pageNumber = historyDisplay.getCurrentPage();
					}else{
						pageNumber = historyDisplay.getCurrentPage() + 1;
					}
				}else if(pageNumber == 0){ // if first button clicked
					pageNumber = 1;
				}else if(pageNumber == -9){ // if first button clicked
					if(historyDisplay.getCurrentPage() == 1){
						pageNumber = historyDisplay.getCurrentPage();
					}else{
						pageNumber = historyDisplay.getCurrentPage() - 1;
					}
				}
				historyDisplay.setCurrentPage(pageNumber);
				history();
			}
		});

		historyDisplay.getReturnToLink().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				// detailDisplay.getName().setValue("");
				  // detailDisplay.getShortName().setValue("");
				   displaySearch();
				   
			}
		});
		
	}

	/**
	 * Draft display handlers.
	 * 
	 * @param draftDisplay
	 *            the draft display
	 */
	private void draftDisplayHandlers(final DraftDisplay draftDisplay) {
		
		draftDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		draftDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				if(pageNumber == -1){ // if next button clicked
					if(draftDisplay.getCurrentPage() == draftValueSetResults.getTotalpages()){
						pageNumber = draftDisplay.getCurrentPage();
					}else{
						pageNumber = draftDisplay.getCurrentPage() + 1;
					}
				}else if(pageNumber == 0){ // if first button clicked
					pageNumber = 1;
				}else if(pageNumber == -19){ // if first button clicked
					if(draftDisplay.getCurrentPage() == 1){
						pageNumber = draftDisplay.getCurrentPage();
					}else{
						pageNumber = draftDisplay.getCurrentPage() - 1;
					}
				}else if(pageNumber == -9){ // if first button clicked
					if(draftDisplay.getCurrentPage() == 1){
						pageNumber = draftDisplay.getCurrentPage();
					}else{
						pageNumber = draftDisplay.getCurrentPage() - 1;
					}
				}
				draftDisplay.setCurrentPage(pageNumber);
				int pageSize = draftDisplay.getPageSize();
				int startIndex = pageSize * (pageNumber - 1);
				searchValueSetsForDraft(startIndex,draftDisplay.getPageSize());
			}
		});
		draftDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				draftDisplay.setPageSize(event.getPageSize());
				searchValueSetsForDraft(startIndex,draftDisplay.getPageSize());
			}
		});
		draftDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				//O&M 17
				((Button)draftDisplay.getSaveButton()).setEnabled(false);
				
				draftDisplay.getErrorMessageDisplay().clear();
				draftDisplay.getSuccessMessageDisplay().clear();
				ManageValueSetSearchModel.Result selectedValueSet =  draftValueSetResults.getSelectedMeasure();
				if(selectedValueSet.getId() != null){
					MatContext.get().getCodeListService().createDraft(selectedValueSet.getId(), selectedValueSet.getOid(), new AsyncCallback<ManageValueSetSearchModel>() {
						
						@Override
						public void onSuccess(ManageValueSetSearchModel result) {
							//do nothing if no result
							if(result.getResultsTotal()>0)
								draftDisplay.getSuccessMessageDisplay().setMessage("Draft created successfully.");
							//O&M 17
							((Button)draftDisplay.getSaveButton()).setEnabled(true);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							draftDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
							//O&M 17
							((Button)draftDisplay.getSaveButton()).setEnabled(true);
						}
					});
				}else{
					draftDisplay.getErrorMessageDisplay().setMessage("Please select a Measure Version to create a Draft.");
					//O&M 17
					((Button)draftDisplay.getSaveButton()).setEnabled(true);
				}
			}
		});
		
	}

	/**
	 * Search display handlers.
	 * 
	 * @param searchDisplay
	 *            the search display
	 */
	private void searchDisplayHandlers(final ValueSetSearchDisplay searchDisplay) {
		
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<CodeListSearchDTO>() {
			@Override
			public void onSelection(SelectionEvent<CodeListSearchDTO> event) {
				searchDisplay.getSearchString().setValue("");
				if(!ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					if(event.getSelectedItem().isGroupedCodeList()) {
						MatContext.get().getEventBus().fireEvent(new EditGroupedCodeListEvent(event.getSelectedItem().getId()));
					}
					else {
						MatContext.get().getEventBus().fireEvent(new EditCodeListEvent(event.getSelectedItem().getId()));
					}
				}
			}
		});
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@SuppressWarnings("static-access")
			@Override
			public void onClick(ClickEvent event) {
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				int startIndex = 1;
				currentSortColumn = getSortKey(0);
				sortIsAscending = true;
				if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					 filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				}
				search(searchDisplay.getSearchString().getValue(),
						startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		searchDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@SuppressWarnings("static-access")
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int startIndex = searchDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					 filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				}
				search(lastSearchText, startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		searchDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@SuppressWarnings("static-access")
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				searchDisplay.getSearchString().setValue("");
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					 filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				}
				search(searchDisplay.getSearchString().getValue(), lastStartIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		searchDisplay.getPageSortTool().addPageSortHandler(new PageSortEventHandler() {
			@SuppressWarnings("static-access")
			@Override
			public void onPageSort(PageSortEvent event) {
				String sortColumn = getSortKey(event.getColumn());
				if(sortColumn.equals(currentSortColumn)) {
					sortIsAscending = !sortIsAscending;
				}
				else {
					currentSortColumn = sortColumn;
					sortIsAscending = true;
				}
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					 filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				}
				search(lastSearchText, lastStartIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		// Code commented for User Story MAT-2372 : Remove Value Set Creation.
		/*searchDisplay.getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_GROUPED_VALUE_SET)){
					MatContext.get().getEventBus().fireEvent(new CreateNewGroupedCodeListEvent());
				}else if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_VALUE_SET)){
					MatContext.get().getEventBus().fireEvent(new CreateNewCodeListEvent());
				}else if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_VALUE_SET_DRAFT)){
					createValueSetDraftScreen();
				}else{
					searchDisplay.getErrorMessageDisplay().setMessage("Please select an option from the Create list box.");
				}
			}
		});*/
		
		TextBox searchWidget = (TextBox)(searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					((Button)searchDisplay.getSearchButton()).click();
	            }
			}
		});
	}
	
	/**
	 * Admin search display handlers.
	 * 
	 * @param adminSearchDisplay
	 *            the admin search display
	 */
	private void adminSearchDisplayHandlers(final AdminValueSetSearchDisplay adminSearchDisplay) {
		
		adminSearchDisplay.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchModel.getTransferValueSetIDs().removeAll(searchModel.getTransferValueSetIDs());
				searchModel.getLisObjectId().removeAll(searchModel.getLisObjectId());
				@SuppressWarnings("static-access")
				int filter = adminSearchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				search(adminSearchDisplay.getSearchString().getValue(),
					startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		
		adminSearchDisplay.getTransferButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//adminSearchDisplay.clearTransferCheckBoxes();
				displayTransferView(startIndex,transferDisplay.getPageSize());
			}
		});
		adminSearchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@SuppressWarnings("static-access")
			@Override
			public void onClick(ClickEvent event) {
				int filter = adminSearchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				int startIndex = 1;
				currentSortColumn = getSortKey(0);
				sortIsAscending = true;
				if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
					
					 filter = adminSearchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
				}
				search(adminSearchDisplay.getSearchString().getValue(),
						startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
			}
		});
		
		TextBox searchWidget = (TextBox)(adminSearchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					((Button)adminSearchDisplay.getSearchButton()).click();
	            }
			}
		});
	}

	/**
	 * Reset display.
	 */
	@SuppressWarnings("static-access")
	void resetDisplay() {
		currentSortColumn = getSortKey(0);
		sortIsAscending = true;
		int filter;
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			 filter = adminSearchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
			 search(adminSearchDisplay.getSearchString().getValue(), 1, currentSortColumn, sortIsAscending,defaultCodeList, filter);
		}else{
			//MAT-1929 : Commented default search filter criteria for story Retain filter and search criteria.
			//filter = searchDisplay.getValueSetSearchFilterPanel().getDefaultFilter();
			filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
			search(searchDisplay.getSearchString().getValue(), 1, currentSortColumn, sortIsAscending,defaultCodeList, filter);
		}
		//MAT-1929 :Search for value set drop down retains filter criteria.This is done for story" Retain filter and search criteria.".
		//searchDisplay.getValueSetSearchFilterPanel().resetFilter();
		
		//MAT-1929 :Commented clearSelections.This is done for story" Retain filter and search criteria.".
		//searchDisplay.clearSelections();
		
	}
	
	/**
	 * Display search.
	 */
	private void displaySearch() {
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			MY_VALUE_SETS = "Value Sets";
			resetPanel(adminSearchDisplay.asWidget(), MY_VALUE_SETS);
		}else{
			resetPanel(searchDisplay.asWidget(), MY_VALUE_SETS);
		}
	}
	
	/**
	 * Display transfer view.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void displayTransferView(int startIndex, int pageSize){
		final ArrayList<CodeListSearchDTO> transferValueSetIDs = searchModel.getTransferValueSetIDs();
		adminSearchDisplay.getErrorMessageDisplay().clear();
		adminSearchDisplay.getTransferErrorMessageDisplay().clear();
		transferDisplay.getErrorMessageDisplay().clear();
		if(transferValueSetIDs.size() !=0){
			
			showAdminSearchingBusy(true);
			MatContext.get().getCodeListService().searchUsers(1,pageSize,new AsyncCallback<TransferOwnerShipModel>(){
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showAdminSearchingBusy(false);
				}
				@Override
				public void onSuccess(TransferOwnerShipModel result) {
					transferDisplay.buildHTMLForValueSets(transferValueSetIDs);
					transferDisplay.buildDataTable(result,transferValueSetIDs);
					resetPanel(transferDisplay.asWidget(), "Value Set Ownership >  Value Set Ownership Transfer");
					showAdminSearchingBusy(false);
					model = result;
				}
			});
			
		}else{
			adminSearchDisplay.getTransferErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getTransferCheckBoxError());
		}
		
	}
	
	/**
	 * History.
	 */
	private void history() {		
		
		int pageNumber = historyDisplay.getCurrentPage();
		int pageSize = historyDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		searchHistory(historyDisplay.getCodeListId(), startIndex, pageSize);		
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			MY_VALUE_SETS_HISTORY = "Value Sets Ownership  > History";
			
		}
		resetPanel(historyDisplay.asWidget(), MY_VALUE_SETS_HISTORY);
		Mat.focusSkipLists("MainContent");
	}

	
	/**
	 * Search history.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void searchHistory(String codeListId, int startIndex, int pageSize) {
	    /**
	     * The filterList tells us what need not to be shown in the UI. This is created as list, Since, in future if the list grows.
	     * 9-18-2013: Added "Measure Export","Measure Exported" and "Measure Package Exported" in the filter.
	     */		   
		List<String> filterList = new ArrayList<String>();
	    filterList.add("Export");
	    filterList.add("Measure Export");
	    filterList.add("Measure Exported");
	    filterList.add("Measure Package Exported");
		MatContext.get().getAuditService().executeCodeListLogSearch(codeListId, startIndex, pageSize,filterList, new AsyncCallback<SearchHistoryDTO>() {		
			@Override
			public void onSuccess(SearchHistoryDTO data) {
				historyModel = new HistoryModel(data.getLogs());
				historyModel.setPageSize(historyDisplay.getPageSize());
				historyModel.setTotalPages(data.getPageCount());
				historyDisplay.buildDataTable(historyModel, data.getPageCount(),data.getTotalResults(),historyDisplay.getCurrentPage(),historyDisplay.getPageSize());
			}
			@Override
			public void onFailure(Throwable caught) {
				//historyDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 */
	private void search(String searchText, int startIndex, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		lastSearchText = (searchText!=null)? searchText.trim() : null;
		lastStartIndex = startIndex;
		
		final boolean isAscending = isAsc;
		
		
		
		String trimmedSearchText = (searchText!=null)? searchText.trim() : null;
		if(MatContext.get().getLoggedInUserRole().equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			int pageSize= Integer.MAX_VALUE;
			adminSearchDisplay.getErrorMessageDisplay().clear();
			adminSearchDisplay.getTransferErrorMessageDisplay().clear();
			showAdminSearchingBusy(true);
			MatContext.get().getCodeListService().searchForAdmin(trimmedSearchText,
																 startIndex, pageSize, 
																 sortColumn, isAsc,defaultCodeList, filter, 
																 new AsyncCallback<AdminManageCodeListSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							adminSearchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
							showAdminSearchingBusy(false);
						}
						@Override
						public void onSuccess(AdminManageCodeListSearchModel result) {
							result.setTransferValueSetIDs(new ArrayList<CodeListSearchDTO>());
							result.setLisObjectId(new ArrayList<String>());
							searchModel = result;
							MatContext.get().setManageCodeListSearcModel(searchModel);
							isObserverBusy = false;
							AdminCodeListSearchResultsAdapter adapter = new AdminCodeListSearchResultsAdapter();
							adapter.setData(result);
							adapter.setObserver(new AdminCodeListSearchResultsAdapter.Observer() {
								@Override
								public void onHistoryClicked(CodeListSearchDTO codeList) {
									historyDisplay.setCodeListId(codeList.getId());
									historyDisplay.setCodeListName(codeList.getName());
									//historyDisplay.reset();
									historyDisplay.setReturnToLinkText("<< Return to Value Set Ownership");
									//historyDisplay.setUserCommentsReadOnly(!codeList.isDraft());
									history();
									adminSearchDisplay.clearTransferCheckBoxes();
								}

								@Override
								public void onTransferSelectedClicked(CodeListSearchDTO codeList) {
										adminSearchDisplay.getErrorMessageDisplay().clear();
										adminSearchDisplay.getTransferErrorMessageDisplay().clear();
										updateTransferIDs(codeList,searchModel);
								}

							});
							SearchResultUpdate sru = new SearchResultUpdate();
							sru.update(result, (TextBox)adminSearchDisplay.getSearchString(), lastSearchText);
							sru = null;
							if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
								adminSearchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoCodeListsMessage());
							}else{
								adminSearchDisplay.getErrorMessageDisplay().clear();
							}
							adminSearchDisplay.getTransferErrorMessageDisplay().clear();
							adminSearchDisplay.setAdapter(adapter);
							adminSearchDisplay.buildDataTable(adapter.getModel());
							displaySearch();
							adminSearchDisplay.getErrorMessageDisplay().setFocus();
							showAdminSearchingBusy(false);
						}
			});
		}else{
			int pageSize = searchDisplay.getPageSize();
			showSearchingBusy(true);
			searchDisplay.getErrorMessageDisplay().clear();
			MatContext.get().getCodeListService().search(trimmedSearchText,
														startIndex, pageSize, 
														sortColumn, isAsc,defaultCodeList, filter, 
														new AsyncCallback<ManageCodeListSearchModel>() {
				@Override
				public void onSuccess(ManageCodeListSearchModel result) {
					isObserverBusy = false;
					CodeListSearchResultsAdapter adapter = new CodeListSearchResultsAdapter();
					adapter.setData(result);
					adapter.setObserver(new CodeListSearchResultsAdapter.Observer() {
						@Override
						public void onHistoryClicked(CodeListSearchDTO result) {
							historyDisplay.setCodeListId(result.getId());
							historyDisplay.setCodeListName(result.getName());
							//historyDisplay.reset();
							historyDisplay.setReturnToLinkText("<< Return to Value Set Library");
							//historyDisplay.setUserCommentsReadOnly(!result.isDraft());
							history();
						}
						@Override
						public void onCloneClicked(CodeListSearchDTO result) {
							if(!isObserverBusy){
								isObserverBusy = true;
								String id = result.getId();
								MatContext.get().getCodeListService().createClone(id, new AsyncCallback<ManageValueSetSearchModel>(){
									@Override
									public void onFailure(Throwable caught) {
									}
									@Override
									public void onSuccess(ManageValueSetSearchModel result) {
										if(result != null){
											boolean isGroupedCodeList = result.get(0).isGrouped();
											String id = result.getKey(0);
											if(isGroupedCodeList) {
												MatContext.get().getEventBus().fireEvent(new EditGroupedCodeListEvent(id));
											}
											else {
												MatContext.get().getEventBus().fireEvent(new EditCodeListEvent(id));
											}
										}
									}});
							}
						}
						@Override
						public void onExportClicked(CodeListSearchDTO result) {
							String id = result.getId();
							String url = GWT.getModuleBaseURL() + "export?id=" + id + "&format=valueset";
							Window.open(url + "&type=save", "_self", "");	
						}
					}
					);
				
					SearchResultUpdate sru = new SearchResultUpdate();
					sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
					sru = null;
					if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoCodeListsMessage());
					}else{
						searchDisplay.getErrorMessageDisplay().clear();
					}
					searchDisplay.buildDataTable(adapter, isAscending);
					displaySearch();
					searchDisplay.getErrorMessageDisplay().setFocus();
					showSearchingBusy(false);
				}
				@Override
				public void onFailure(Throwable caught) {
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
					showSearchingBusy(false);
				}
			});
		}
	}
	
	/**
	 * Update transfer i ds.
	 * 
	 * @param codeList
	 *            the code list
	 * @param model
	 *            the model
	 */
	private void updateTransferIDs(CodeListSearchDTO codeList,AdminManageCodeListSearchModel model) {
		if(codeList.isTransferable()){
			List<String> codeIdList = model.getLisObjectId();
			if(!codeIdList.contains(codeList.getId())){
				model.getTransferValueSetIDs().add(codeList);
				codeIdList.add(codeList.getId());
			}
		}else{
			for(int i=0 ;i< model.getTransferValueSetIDs().size();i++){
				if(codeList.getId().equals(model.getTransferValueSetIDs().get(i).getId())){
					model.getTransferValueSetIDs().remove(i);
					model.getLisObjectId().remove(i);
				}
			}
			
		}
	}
	
	/**
	 * Show searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(boolean busy){
		if(busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
		((Button)searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox)(searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	/**
	 * Show admin searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showAdminSearchingBusy(boolean busy){
		if(busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
		((Button)adminSearchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox)(adminSearchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	/**
	 * Refresh search.
	 */
	void refreshSearch() {
		int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
		search(lastSearchText, lastStartIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
	}
	
	/**
	 * Gets the widget.
	 * 
	 * @return the widget
	 */
	public Widget getWidget() {
		return panel;
	}
		
	/**
	 * Gets the widget with heading.
	 * 
	 * @param widget
	 *            the widget
	 * @param heading
	 *            the heading
	 * @return the widget with heading
	 */
	public Widget getWidgetWithHeading(Widget widget, String heading) {
		FlowPanel vPanel = new FlowPanel();
		Label h = new Label(heading);
		h.addStyleName("myAccountHeader");
		h.addStyleName("leftAligned");
		vPanel.add(h);
		vPanel.add(widget);
		vPanel.addStyleName("myAccountPanel");
		widget.addStyleName("myAccountPanelContent");
		return vPanel;
	}
	
	/**
	 * Gets the sort key.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return the sort key
	 */
	public String getSortKey(int columnIndex) {
		String[] sortKeys = new String[] { "name","last modifed", "st.orgName", "c.description", "cs.description", "history"};
		return sortKeys[columnIndex];
	}
	
	/**
	 * Creates the value set draft screen.
	 */
	private void createValueSetDraftScreen(){
    	int pageNumber = draftDisplay.getCurrentPage();
		int pageSize = draftDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
    	searchValueSetsForDraft(startIndex,draftDisplay.getPageSize());
    	draftDisplay.getErrorMessageDisplay().clear();
    	resetPanel(draftDisplay.asWidget(), MY_VALUE_SETS_CREATE_DRAFT);
 	    /* TODO US537 determine which is correct */
 	    /* Mat.focusSkipLists("MeasureComposer"); */
    	Mat.focusSkipLists("MainContent");
	}
	
	/**
	 * Search value sets for draft.
	 * 
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void searchValueSetsForDraft(int startIndex, int pageSize){
		
		MatContext.get().getCodeListService().searchValueSetsForDraft(startIndex, pageSize, 
				new AsyncCallback<ManageValueSetSearchModel>() {
			@Override
			public void onSuccess(ManageValueSetSearchModel result) {
				draftValueSetResults = new ManageValueSetModel(result.getData());
				draftValueSetResults.setPageSize(result.getData().size());
				draftValueSetResults.setTotalPages(result.getPageCount());
				draftDisplay.buildDataTable(draftValueSetResults, result.getPageCount(), result.getResultsTotal(), draftDisplay.getCurrentPage(), draftDisplay.getPageSize());
			}
			@Override
			public void onFailure(Throwable caught) {
				draftDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		
	}
	
	/**
	 * Reset panel.
	 * 
	 * @param w
	 *            the w
	 * @param heading
	 *            the heading
	 */
	private void resetPanel(Widget w, String heading){
		panel.setHeading(heading, heading);
		panel.setContent(w);
	}
	

}
