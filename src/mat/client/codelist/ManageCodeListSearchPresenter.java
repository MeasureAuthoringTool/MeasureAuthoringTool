package mat.client.codelist;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.codelist.events.CancelEditCodeListEvent;
import mat.client.codelist.events.CreateNewCodeListEvent;
import mat.client.codelist.events.CreateNewGroupedCodeListEvent;
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

public class ManageCodeListSearchPresenter {
	
	private  String MY_VALUE_SETS = "My Value Sets";
	private static final String MY_VALUE_SETS_CREATE_DRAFT = "My Value Sets  > Create a Value Set Draft";
	private String MY_VALUE_SETS_HISTORY = "My Value Sets  > History";
	private boolean isObserverBusy = false;
	
	public static interface ValueSetSearchDisplay extends mat.client.shared.search.SearchDisplay {
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForEditTool();
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		public void buildDataTable(SearchResults<CodeListSearchDTO> results);
		public void buildDataTable(SearchResults<CodeListSearchDTO> results, boolean isAsc);
		public HasSortHandler getPageSortTool();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public ErrorMessageDisplayInterface getTransferErrorMessageDisplay();
		public HasClickHandlers getTransferButton();
		/*US537*/
		public HasClickHandlers getCreateButton();
		public void clearSelections();
		public String getSelectedOption();
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public int getPageSize();
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		public Grid508 getDataTable();
		
		void clearAllCheckBoxes(Grid508 dataTable);
	}
	
	public static interface HistoryDisplay {
		public void setCodeListId(String codeListId);
		public String getCodeListId();
		public Widget asWidget();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getClearButton();
		public HasValue<String> getUserComment();
		public void buildDataTable(SearchResults<AuditLogDTO> results, int pageCount,long totalResults,int currentPage,int pageSize);
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);		
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public HasPageSelectionHandler getPageSelectionTool();
		public void reset();
		public void setErrorMessage(String s);
		public void clearErrorMessage();
		public void setCodeListName(String name);
		public String getCodeListName();
		public void setReturnToLinkText(String s);
		public HasClickHandlers getReturnToLink();
		public void setUserCommentsReadOnly(boolean readOnly);
		
	}
	
	public static interface TransferDisplay{
		public Widget asWidget();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		void buildDataTable(SearchResults<TransferOwnerShipModel.Result> results , List<CodeListSearchDTO> codeListIDs);
		public String getSelectedValue();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		void buildHTMLForValueSets(List<CodeListSearchDTO> codeListIDs);
	}
	
	
	public static interface DraftDisplay{
		public Widget asWidget();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void buildDataTable(SearchResults<ManageValueSetSearchModel.Result> results,int pageCount,long totalResults,int currentPage,int pageSize);
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);		
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	}
	
	private HistoryDisplay historyDisplay;
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	private ValueSetSearchDisplay searchDisplay;
	private String lastSearchText;
	private TransferDisplay transferDisplay;
	private int lastStartIndex;
	private String currentSortColumn = getSortKey(0);
	private boolean sortIsAscending = true;
	private boolean defaultCodeList = false;
	private PreviousContinueButtonBar buttonBar = null;
	private HistoryModel historyModel;
	private DraftDisplay draftDisplay;
	private ManageValueSetModel draftValueSetResults;
	private ManageCodeListDetailModel currentDetails;
	private int startIndex = 1;
	
	private TransferOwnerShipModel model = null;
	private AdminManageCodeListSearchModel searchModel;
	
	private ClickHandler cancelClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			draftDisplay.getErrorMessageDisplay().clear();
			draftDisplay.getSuccessMessageDisplay().clear();
			searchDisplay.clearSelections();
			MatContext.get().getEventBus().fireEvent(new CancelEditCodeListEvent());
			displaySearch();
		}
	};
	
	public ManageCodeListSearchPresenter(ValueSetSearchDisplay sDisplayArg, HistoryDisplay argHistDisplay, HasVisible prevContButtons, DraftDisplay dDisplay) {
		this.searchDisplay = sDisplayArg;
		this.historyDisplay = argHistDisplay;
		this.buttonBar = (PreviousContinueButtonBar) prevContButtons;
		this.draftDisplay = dDisplay;
		displaySearch();		
		
		searchDisplayHandlers(searchDisplay);
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
	
	public ManageCodeListSearchPresenter(ValueSetSearchDisplay sDisplayArg, HistoryDisplay argHistDisplay, HasVisible prevContButtons, DraftDisplay dDisplay, TransferDisplay tDisplay) {
		this.searchDisplay = sDisplayArg;
		this.historyDisplay = argHistDisplay;
		this.buttonBar = (PreviousContinueButtonBar) prevContButtons;
		this.draftDisplay = dDisplay;
		this.transferDisplay = tDisplay;
		displaySearch();		
		
		searchDisplayHandlers(searchDisplay);
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
	}
	
	private void transferDisplayHandlers(final TransferDisplay transferDisplay) {
		
		transferDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				for(int i=0;i<model.getData().size();i=i+1){
					if(model.getData().get(i).isSelected()){
						final String emailTo =model.getData().get(i).getEmailId();
						MatContext.get().getCodeListService().transferOwnerShipToUser(searchModel.getLisObjectId(),emailTo ,
								new AsyncCallback<Void>(){
									@Override
									public void onFailure(Throwable caught) {
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
										searchModel.getTransferValueSetIDs().clear();;
										searchModel.getLisObjectId().clear();
									}
									@Override
									public void onSuccess(Void result) {
										transferDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getTransferOwnershipSuccess()+emailTo);
										searchModel.getTransferValueSetIDs().clear();
										searchModel.getLisObjectId().clear();
										
									}
						});
					}
				}
				
			}
		});
		transferDisplay.getCancelButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				searchModel.getLisObjectId().clear();
				searchModel.getTransferValueSetIDs().clear();
				displaySearch();
			}
		});
	}


	private void historyDisplayHandlers(final HistoryDisplay historyDisplay) {
			historyDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				if(historyDisplay.getUserComment().getValue().length() > 2000){
					String s = historyDisplay.getUserComment().getValue();
					historyDisplay.getUserComment().setValue(
							s.substring(0, 2000));
				}
				
				String codeListId = historyDisplay.getCodeListId();
				String eventType = ConstantMessages.USER_COMMENT;
				String additionalInfo = historyDisplay.getUserComment().getValue();
					MatContext.get().getAuditService().recordCodeListEvent(codeListId, eventType, additionalInfo, new AsyncCallback<Boolean>(){
					@Override
					public void onSuccess(Boolean result) {
						//add user message
						historyDisplay.reset();
						history();
					}

					@Override
					public void onFailure(Throwable caught) {
						historyDisplay.setErrorMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
					}
				});
			}
		});

		historyDisplay.getClearButton().addClickHandler(new ClickHandler(){
		
			@Override
			public void onClick(ClickEvent event) {
				historyDisplay.clearErrorMessage();
				historyDisplay.getUserComment().setValue("");
			}
		});

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

	private void searchDisplayHandlers(final ValueSetSearchDisplay searchDisplay) {
		
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<CodeListSearchDTO>() {
			@Override
			public void onSelection(SelectionEvent<CodeListSearchDTO> event) {
				searchDisplay.getSearchString().setValue("");
				if(event.getSelectedItem().isGroupedCodeList()) {
					MatContext.get().getEventBus().fireEvent(new EditGroupedCodeListEvent(event.getSelectedItem().getId()));
				}
				else {
					MatContext.get().getEventBus().fireEvent(new EditCodeListEvent(event.getSelectedItem().getId()));
				}
			}
		});
		searchDisplay.getTransferButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.clearAllCheckBoxes(searchDisplay.getDataTable());
				displayTransferView();
			}
		});
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
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
		/*US537*/
		searchDisplay.getCreateButton().addClickHandler(new ClickHandler() {
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
		});
		
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

	@SuppressWarnings("static-access")
	void resetDisplay() {
		currentSortColumn = getSortKey(0);
		sortIsAscending = true;
		int filter;
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			 filter = searchDisplay.getValueSetSearchFilterPanel().ALL_VALUE_SETS;
		}else{
			filter = searchDisplay.getValueSetSearchFilterPanel().getDefaultFilter();
		}
		
		searchDisplay.getValueSetSearchFilterPanel().resetFilter();
		search("", 1, currentSortColumn, sortIsAscending,defaultCodeList, filter);
		searchDisplay.clearSelections();
		
	}
	
	private void displaySearch() {
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			MY_VALUE_SETS = "Value Sets";
		}
		resetPanel(searchDisplay.asWidget(), MY_VALUE_SETS);
	}
	
	private void displayTransferView(){
		final ArrayList<CodeListSearchDTO> transferValueSetIDs = searchModel.getTransferValueSetIDs();
		if(transferValueSetIDs.size() !=0){
			searchDisplay.getErrorMessageDisplay().clear();
			searchDisplay.getTransferErrorMessageDisplay().clear();
			transferDisplay.getErrorMessageDisplay().clear();
			showSearchingBusy(true);
			MatContext.get().getCodeListService().searchUser(new AsyncCallback<TransferOwnerShipModel>(){
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}
				@Override
				public void onSuccess(TransferOwnerShipModel result) {
					transferDisplay.buildHTMLForValueSets(transferValueSetIDs);
					transferDisplay.buildDataTable(result,transferValueSetIDs);
					resetPanel(transferDisplay.asWidget(), "Value Set Ownership >  Value Set Ownership Transfer");
					showSearchingBusy(false);
					model = result;
				}
			});
			
		}else{
			searchDisplay.getTransferErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getTransferCheckBoxError());
		}
		
	}

	
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

	
	private void searchHistory(String codeListId, int startIndex, int pageSize) {
	    //The filterList tells us what need not to be shown in the UI. This is created as list, Since, in future if the list grows.
		//TODO:- we should have propertyFile called filterList, rather than hardcoding here.
		List<String> filterList = new ArrayList<String>();
	    filterList.add("Export");
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
	
	
	private void search(String searchText, int startIndex, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		lastSearchText = searchText;
		lastStartIndex = startIndex;
		int pageSize = searchDisplay.getPageSize();
		final boolean isAscending = isAsc;
		showSearchingBusy(true);
		searchDisplay.getErrorMessageDisplay().clear();
		if(MatContext.get().getLoggedInUserRole().equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			MatContext.get().getCodeListService().searchForAdmin(searchText,
																 startIndex, pageSize, 
																 sortColumn, isAsc,defaultCodeList, filter, 
																 new AsyncCallback<AdminManageCodeListSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
							showSearchingBusy(false);
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
									historyDisplay.reset();
									historyDisplay.setReturnToLinkText("<< Return to Value Set Ownership");
									historyDisplay.setUserCommentsReadOnly(!codeList.isDraft());
									history();
								}

								@Override
								public void onTransferClicked(CodeListSearchDTO codeList) {
										searchDisplay.getErrorMessageDisplay().clear();
										searchDisplay.getTransferErrorMessageDisplay().clear();
										updateTransferIDs(codeList,searchModel);
								}

							});
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
			});
		}else{
			
			MatContext.get().getCodeListService().search(searchText,
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
							historyDisplay.reset();
							historyDisplay.setReturnToLinkText("<< Return to Value Set Library");
							historyDisplay.setUserCommentsReadOnly(!result.isDraft());
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
	
	private void updateTransferIDs(CodeListSearchDTO codeList,AdminManageCodeListSearchModel model) {
		if(codeList.isTransferable()){
			model.getTransferValueSetIDs().add(codeList);
			model.getLisObjectId().add(codeList.getId());
		}else{
			for(int i=0 ;i< model.getTransferValueSetIDs().size();i++){
				if(codeList.getId() == model.getTransferValueSetIDs().get(i).getId()){
					model.getTransferValueSetIDs().remove(i);
					model.getLisObjectId().remove(i);
				}
			}
		}
	}
	
	private void showSearchingBusy(boolean busy){
		if(busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
		((Button)searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox)(searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	void refreshSearch() {
		int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
		search(lastSearchText, lastStartIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter);
	}
	public Widget getWidget() {
		return panel;
	}
		
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
	
	public String getSortKey(int columnIndex) {
		String[] sortKeys = new String[] { "name","last modifed", "st.orgName", "c.description", "cs.description", "history"};
		return sortKeys[columnIndex];
	}
	
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
	
	private void resetPanel(Widget w, String heading){
		panel.setHeading(heading, heading);
		panel.setContent(w);
	}
	

}
