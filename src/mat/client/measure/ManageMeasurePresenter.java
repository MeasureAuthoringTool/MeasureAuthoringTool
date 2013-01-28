package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.codelist.HasListBox;
import mat.client.codelist.TransferOwnerShipModel;
import mat.client.codelist.ManageCodeListSearchPresenter.TransferDisplay;
import mat.client.codelist.events.OnChangeMeasureDraftOptionsEvent;
import mat.client.codelist.events.OnChangeMeasureVersionOptionsEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.history.HistoryModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.measure.service.MeasureCloningService;
import mat.client.measure.service.MeasureCloningServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.PageSizeSelectionEvent;
import mat.client.shared.search.PageSizeSelectionEventHandler;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
import mat.model.CodeListSearchDTO;
import mat.model.clause.MeasureShareDTO;
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
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasurePresenter implements MatPresenter {
	final String currentUserRole = MatContext.get().getLoggedInUserRole();
	public static interface BaseDisplay{
		public Widget asWidget();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
	}
	public static interface SearchDisplay extends BaseDisplay {
		public HasClickHandlers getCreateButton();
		public void clearSelections();
		public String getSelectedOption();
		public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool();
		public void buildDataTable(SearchResults<ManageMeasureSearchModel.Result> results);
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public int getPageSize();
		public HasClickHandlers getSearchButton();
		public HasValue<String> getSearchString();
		public HasClickHandlers getBulkExportButton();
		public FormPanel getForm();
		public ErrorMessageDisplayInterface getErrorMessageDisplayForBulkExport();
		public ErrorMessageDisplayInterface getErrorMessagesForTransferOS();
		public Grid508 getMeasureDataTable();
		public Button getExportSelectedButton();
		public void clearBulkExportCheckBoxes(Grid508 dataTable);
		public HasClickHandlers getTransferButton();
		
		
	}
	public static interface DetailDisplay extends BaseDisplay {
		public HasValue<String> getName();
		public HasValue<String> getShortName();
		//US 421. Measure scoring choice is now part of measure creation process.
		public ListBoxMVP getMeasScoringChoice();
		public String getMeasScoringValue();
		public HasValue<String> getMeasureVersion();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void setMeasureName(String name);
		//US 195 showingCautionMsg
		public void showCautionMsg(boolean show);
		public void showMeasureName(boolean show);
		//US 421. Measure scoring choice is now part of measure creation process.
		void setScoringChoices(List<? extends HasListBox> texts);
		public void clearFields();
	}
	public static interface ShareDisplay extends BaseDisplay{
		public HasClickHandlers getShareButton();
		public HasClickHandlers getCancelButton();
		public void setMeasureName(String name);
		public void buildDataTable(SearchResults<MeasureShareDTO> results);
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public int getPageSize();
	}
	public static interface ExportDisplay extends BaseDisplay{
		public void setMeasureName(String name);
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public HasClickHandlers getOpenButton();
		public boolean isSimpleXML();
		public boolean isEMeasure();
		public boolean isCodeList();
		boolean isEMeasurePackage();
	}
	
	public static interface TransferDisplay{
		public Widget asWidget();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public String getSelectedValue();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public int getPageSize();
		void clearAllRadioButtons(Grid508 dataTable);
		public Grid508 getDataTable();
		void buildDataTable(SearchResults<TransferMeasureOwnerShipModel.Result> results);
		void buildHTMLForMeasures(List<ManageMeasureSearchModel.Result> measureList);
	}
	
	
	public static interface HistoryDisplay extends BaseDisplay{
		public void setMeasureName(String name);
		public String getMeasureName();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getClearButton();
		public HasValue<String> getUserComment(); 
		public void buildDataTable(SearchResults<AuditLogDTO> results,int pageCount,long totalResults,int currentPage,int pageSize);
		public void setMeasureId(String id);
		public String getMeasureId();
		public void setErrorMessage(String s);
		public void clearErrorMessage();
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);		
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public HasPageSelectionHandler getPageSelectionTool();
		public void setReturnToLinkText(String s);
		public HasClickHandlers getReturnToLink();
		public void setUserCommentsReadOnly(boolean readOnly);
	}
	
	public static interface DraftDisplay extends BaseDisplay{
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void buildDataTable(SearchResults<ManageMeasureSearchModel.Result> results,int pageCount,long totalResults,int currentPage,int pageSize);
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);		
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	}
	public static interface VersionDisplay extends BaseDisplay{
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void buildDataTable(SearchResults<ManageMeasureSearchModel.Result> results,int pageCount,long totalResults,int currentPage,int pageSize);
		public int getPageSize();
		public RadioButton getMajorRadioButton();
		public RadioButton getMinorRadioButton();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);		
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	}
	
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	private SearchDisplay searchDisplay;
	private DetailDisplay detailDisplay;
	private HistoryDisplay historyDisplay;
	private ShareDisplay shareDisplay;
	private ExportDisplay exportDisplay;
	private VersionDisplay versionDisplay;
	private DraftDisplay draftDisplay;
	private TransferDisplay transferDisplay;
	private ManageMeasureDetailModel currentDetails;
	private ManageMeasureShareModel currentShareDetails;
	private String currentExportId;
	
	private ManageDraftMeasureModel draftMeasureResults;
	private ManageVersionMeasureModel versionMeasureResults;
	private UserShareInfoAdapter userShareInfo = new UserShareInfoAdapter();
	private int startIndex = 1;
	private int shareStartIndex = 1;
	List<ManageMeasureSearchModel.Result> listofMeasures = new ArrayList<ManageMeasureSearchModel.Result>();
	private boolean isClone;
	private List<String> bulkExportMeasureIds;
	private ManageMeasureSearchModel manageMeasureSearchModel;
	private TransferMeasureOwnerShipModel model = null;
	
	private HistoryModel historyModel;
	private ClickHandler cancelClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			detailDisplay.getName().setValue("");
			detailDisplay.getShortName().setValue("");
			searchDisplay.clearSelections();
			displaySearch();
		}
	};
	
	public ManageMeasurePresenter(SearchDisplay sDisplayArg, DetailDisplay dDisplayArg,
			ShareDisplay shareDisplayArg, ExportDisplay exportDisplayArg, HistoryDisplay hDisplay,VersionDisplay vDisplay,DraftDisplay dDisplay, final TransferDisplay transferDisplay) {
		this.searchDisplay = sDisplayArg;
		this.detailDisplay = dDisplayArg;
		this.historyDisplay = hDisplay;
		this.shareDisplay = shareDisplayArg;
		this.exportDisplay = exportDisplayArg;
		this.draftDisplay = dDisplay;
		this.versionDisplay = vDisplay;
		this.transferDisplay = transferDisplay;
		displaySearch();
		
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(new SelectionHandler<ManageMeasureSearchModel.Result>() {
			
			@Override
			public void onSelection(SelectionEvent<ManageMeasureSearchModel.Result> event) {
				//TODO synchronize this method call
				if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
					final String mid = event.getSelectedItem().getId();
					Result result = event.getSelectedItem();
				
					final String name = result.getName();
					final String version = result.getVersion();
					final String shortName = result.getShortName();
					final String scoringType = result.getScoringType();
					final boolean isEditable = result.isEditable();
					final boolean isMeasureLocked = result.isMeasureLocked();
					final String userId = result.getLockedUserId(result.getLockedUserInfo());
				
					MatContext.get().getMeasureLockService().isMeasureLocked(mid);
					Command waitForLockCheck = new Command(){
						@Override
						public void execute() {
							SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
							if(!synchDel.isCheckingLock()){
								if(!synchDel.measureIsLocked()){
									fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, isEditable, isMeasureLocked, userId);
									if(isEditable)
										MatContext.get().getMeasureLockService().setMeasureLock();
								}else{
									fireMeasureSelectedEvent(mid, version, name, shortName, scoringType, false, isMeasureLocked, userId);
									if(isEditable)
										MatContext.get().getMeasureLockService().setMeasureLock();
								}
							}else{
								DeferredCommand.addCommand(this);
							}
						}
					};
					waitForLockCheck.execute();
				}
			}
		});
		
		searchDisplay.getCreateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_MEASURE)){
					createNew();
				}else if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_DRAFT)){
					createDraft();
				}else if(searchDisplay.getSelectedOption().equalsIgnoreCase(ConstantMessages.CREATE_NEW_VERSION)){
					createVersion();
				}else{
					searchDisplay.getErrorMessageDisplayForBulkExport().clear();
					searchDisplay.getErrorMessageDisplay().setMessage("Please select an option from the Create list box.");
				}
			}
		});
		searchDisplay.getTransferButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.clearBulkExportCheckBoxes(searchDisplay.getMeasureDataTable());
				displayTransferView(startIndex,transferDisplay.getPageSize());
				for(int i=0;i<manageMeasureSearchModel.getSelectedTransferIds().size();i++){
					System.out.println("getSelectedTransferIds ======" + manageMeasureSearchModel.getSelectedTransferIds().get(i));
					System.out.println("getSelectedTransferResults ======" + manageMeasureSearchModel.getSelectedTransferResults().get(i));
				}
				
			}
		});
		searchDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				startIndex = searchDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				search(searchDisplay.getSearchString().getValue(), startIndex, searchDisplay.getPageSize());
			}
		});
		searchDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				search(searchDisplay.getSearchString().getValue(), startIndex, searchDisplay.getPageSize());
			}
		});
		
		shareDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				shareStartIndex = shareDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				getShareDetails(currentShareDetails.getMeasureId(), shareStartIndex);
			}
		});
		shareDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				getShareDetails(currentShareDetails.getMeasureId(), shareStartIndex);
			}
		});
		shareDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isClone = false;
				displaySearch();
			}
		});
		shareDisplay.getShareButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getMeasureService().updateUsersShare(currentShareDetails, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						shareDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(Void result) {
						displaySearch();
					}
				});
			}
		});
		
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				 update();
			}
		});
		
		detailDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		exportDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		versionDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		draftDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
		exportDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveExport();
			}
		});
		exportDisplay.getOpenButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openExport();
			}
		});
		
		historyDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				if(historyDisplay.getUserComment().getValue().length() > 2000){
					String s = historyDisplay.getUserComment().getValue();
					historyDisplay.getUserComment().setValue(
							s.substring(0, 2000));
				}
				historyDisplay.clearErrorMessage();
				String measureId = historyDisplay.getMeasureId();
				String eventType = ConstantMessages.USER_COMMENT;
				String additionalInfo = historyDisplay.getUserComment().getValue();
				MatContext.get().getAuditService().recordMeasureEvent(measureId, eventType, additionalInfo, false, new AsyncCallback<Boolean>(){
					@Override
					public void onSuccess(Boolean result) {
						
						if(result){
							//add user message
							historyDisplay.getUserComment().setValue("");
						    displayHistory(historyDisplay.getMeasureId(),historyDisplay.getMeasureName());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						historyDisplay.setErrorMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
						//set error message
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
				displayHistory(historyDisplay.getMeasureId(),historyDisplay.getMeasureName());
			}
		});
		
		historyDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
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
				displayHistory(historyDisplay.getMeasureId(),historyDisplay.getMeasureName());
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
		
		TextBox searchWidget = (TextBox)(searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					((Button)searchDisplay.getSearchButton()).click();
	            }
			}
		});
		
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				search(searchDisplay.getSearchString().getValue(),
						startIndex, searchDisplay.getPageSize());
			}
		});
		
		searchDisplay.getBulkExportButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				versionDisplay.getErrorMessageDisplay().clear();
				draftDisplay.getErrorMessageDisplay().clear();
				detailDisplay.getErrorMessageDisplay().clear();
				historyDisplay.getErrorMessageDisplay().clear();
				exportDisplay.getErrorMessageDisplay().clear();
				shareDisplay.getErrorMessageDisplay().clear();
				if(manageMeasureSearchModel.getSelectedExportIds().isEmpty()){
					searchDisplay.getErrorMessageDisplayForBulkExport().setMessage(MatContext.get().getMessageDelegate().getMeasureSelectionError());
				}else{
					searchDisplay.clearBulkExportCheckBoxes(searchDisplay.getMeasureDataTable());
					bulkExport(manageMeasureSearchModel.getSelectedExportIds());
				}
			}
		});
			FormPanel form = searchDisplay.getForm();
		    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
		      public void onSubmitComplete(SubmitCompleteEvent event) {
		    	  String errorMsg = event.getResults();
		    	  if(null != errorMsg && errorMsg.contains("Exceeded Limit")){
		    		  List<String> err = new ArrayList<String>();
			    	  err.add("Export file size is " + errorMsg);
			    	  err.add("File size limit is 100 MB");
			    	  searchDisplay.getErrorMessageDisplayForBulkExport().setMessages(err);  
		    	  }else{
		    		  Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		    	  }
		      }
		    });

		
		versionDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
			     ManageMeasureSearchModel.Result selectedMeasure =  versionMeasureResults.getSelectedMeasure();
			     if(selectedMeasure.getId() != null && (versionDisplay.getMajorRadioButton().getValue() || versionDisplay.getMinorRadioButton().getValue()))
			    	 saveFinalizedVersion(selectedMeasure.getId(),versionDisplay.getMajorRadioButton().getValue(),selectedMeasure.getVersion());
			     else
			    	  versionDisplay.getErrorMessageDisplay().setMessage("Please select a Measure Name to version and select a version type of Major or Minor.");
			}
		});
		
		versionDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				if(pageNumber == -1){ // if next button clicked
					if(versionDisplay.getCurrentPage() == versionMeasureResults.getTotalpages()){
						pageNumber = versionDisplay.getCurrentPage();
					}else{
						pageNumber = versionDisplay.getCurrentPage() + 1;
					}
				}else if(pageNumber == 0){ // if first button clicked
					pageNumber = 1;
				}else if(pageNumber == -19){ // if first button clicked
					if(versionDisplay.getCurrentPage() == 1){
						pageNumber = versionDisplay.getCurrentPage();
					}else{
						pageNumber = versionDisplay.getCurrentPage() - 1;
					}
				}else if(pageNumber == -9){ // if first button clicked
					if(versionDisplay.getCurrentPage() == 1){
						pageNumber = versionDisplay.getCurrentPage();
					}else{
						pageNumber = versionDisplay.getCurrentPage() - 1;
					}
				}
				versionDisplay.setCurrentPage(pageNumber);
				int pageSize = versionDisplay.getPageSize();
				int startIndex = pageSize * (pageNumber - 1);
				searchMeasuresForVersion(startIndex,versionDisplay.getPageSize());
			}
		});
		versionDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
				
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				versionDisplay.setPageSize(event.getPageSize());
				searchMeasuresForVersion(startIndex,versionDisplay.getPageSize());
			}
		});
		
		draftDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				if(pageNumber == -1){ // if next button clicked
					if(draftDisplay.getCurrentPage() == draftMeasureResults.getTotalpages()){
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
				searchMeasuresForDraft(startIndex,draftDisplay.getPageSize());
			}
		});
		draftDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				draftDisplay.setPageSize(event.getPageSize());
				searchMeasuresForDraft(startIndex,draftDisplay.getPageSize());
			}
		});
		draftDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				//O&M 17
				((Button)draftDisplay.getSaveButton()).setEnabled(false);
				
				ManageMeasureSearchModel.Result selectedMeasure =  draftMeasureResults.getSelectedMeasure();
				if(selectedMeasure.getId() != null){
					MatContext.get().getMeasureService().getMeasure(selectedMeasure.getId(), new AsyncCallback<ManageMeasureDetailModel>() {
						
						@Override
						public void onSuccess(ManageMeasureDetailModel result) {
							currentDetails = result;
							createDraftOfSelectedVersion(currentDetails);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							//O&M 17
							((Button)draftDisplay.getSaveButton()).setEnabled(true);
							
							draftDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
						}
					});
				}else{
					//O&M 17
					((Button)draftDisplay.getSaveButton()).setEnabled(true);
					draftDisplay.getErrorMessageDisplay().setMessage("Please select a Measure Version to create a Draft.");
				}
				
			}
		});
		transferDisplayHandlers(transferDisplay);
		HandlerManager eventBus = MatContext.get().getEventBus(); 
		eventBus.addHandler(OnChangeMeasureDraftOptionsEvent.TYPE, new OnChangeMeasureDraftOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(OnChangeMeasureDraftOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) draftDisplay.getSaveButton();
				button.setFocus(true);
			}
		});
		
		eventBus.addHandler(OnChangeMeasureVersionOptionsEvent.TYPE, new OnChangeMeasureVersionOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(OnChangeMeasureVersionOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) versionDisplay.getSaveButton();
				button.setFocus(true);
			}
		});
		
		//US 421. Retrieve the Measure scoring choices from db.
		MatContext.get().getListBoxCodeProvider().getScoringList(new AsyncCallback<List<? extends HasListBox>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				detailDisplay.setScoringChoices(result);
			}
		});

	}
	
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
						
						MatContext.get().getMeasureService().transferOwnerShipToUser(manageMeasureSearchModel.getSelectedTransferIds(), emailTo,
								new AsyncCallback<Void>(){
									@Override
									public void onFailure(Throwable caught) {
										Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
										manageMeasureSearchModel.getSelectedTransferIds().clear();;
										manageMeasureSearchModel.getSelectedTransferResults().clear();
										model.getData().get(rowIndex).setSelected(false);
									}
									@Override
									public void onSuccess(Void result) {
										transferDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getTransferOwnershipSuccess()+emailTo);
										manageMeasureSearchModel.getSelectedTransferIds().clear();
										manageMeasureSearchModel.getSelectedTransferResults().clear();
										model.getData().get(rowIndex).setSelected(false);
									}
						});
					}
				}
				if(userSelected==false){
					transferDisplay.getSuccessMessageDisplay().clear();
				}
					
				transferDisplay.clearAllRadioButtons(transferDisplay.getDataTable());
			}
			
		});
		transferDisplay.getCancelButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				manageMeasureSearchModel.getSelectedTransferIds().clear();
				manageMeasureSearchModel.getSelectedTransferResults().clear();
				transferDisplay.getSuccessMessageDisplay().clear();
				transferDisplay.getErrorMessageDisplay().clear();
				displaySearch();
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
	private void displayTransferView(int startIndex, int pageSize){
		final ArrayList<ManageMeasureSearchModel.Result> transferMeasureResults = (ArrayList<Result>) manageMeasureSearchModel.getSelectedTransferResults();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessagesForTransferOS().clear();
		transferDisplay.getErrorMessageDisplay().clear();
		if(transferMeasureResults.size() !=0){
			showSearchingBusy(true);
			MatContext.get().getMeasureService().searchUsers(startIndex, pageSize, new AsyncCallback<TransferMeasureOwnerShipModel>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					showSearchingBusy(false);
				}

				@Override
				public void onSuccess(TransferMeasureOwnerShipModel result) {
					transferDisplay.buildHTMLForMeasures(transferMeasureResults);
					transferDisplay.buildDataTable(result);
					panel.setHeading("Measure Libray Ownership >  Measure Ownership Transfer","MainContent");
			 	    panel.setContent(transferDisplay.asWidget());
			 	   showSearchingBusy(false);
			 	   model = result;
				}
			});
		}else{
			searchDisplay.getErrorMessagesForTransferOS().setMessage(MatContext.get().getMessageDelegate().getTransferCheckBoxError());
		}
		
	}
	
	
	private void  saveFinalizedVersion(final String measureId,final boolean isMajor, final String version){
		MatContext.get().getMeasureService().saveFinalizedVersion(measureId,isMajor,version, new AsyncCallback<SaveMeasureResult>() {
			@Override
			public void onSuccess(SaveMeasureResult result) {
				if(result.isSuccess()){
					searchDisplay.clearSelections();
					displaySearch();					
					String versionStr = result.getVersionStr();
					MatContext.get().getAuditService().recordMeasureEvent(measureId, "Measure Versioned", "Measure Version "+versionStr+" created",false, new AsyncCallback<Boolean>(){
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(Boolean result) {
							// TODO Auto-generated method stub	
						}
					});
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				versionDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);	
			}
		});
	}
	
	private void  createDraftOfSelectedVersion(ManageMeasureDetailModel currentDetails){
		cloneMeasure(currentDetails,true);
		
	}
	
	@Override
	public void beforeDisplay() {
		Command waitForUnlock = new Command(){
			public void execute() {
	 		  if(!MatContext.get().getMeasureLockService().isResettingLock()){
	 			 displaySearch();
	 		  }else{
	 			  DeferredCommand.addCommand(this);
	 		  }
	 	   }
	 	};
	 	if(MatContext.get().getMeasureLockService().isResettingLock())
	 		waitForUnlock.execute();
	 	else{
	 		 displaySearch();
	 	}
	 	Mat.hideLoadingMessage();
		Mat.focusSkipLists("MainContent");
	}
	@Override 
	public void beforeClosingDisplay() {
		
	}
	
	private void createVersion(){
		int pageNumber = versionDisplay.getCurrentPage();
		int pageSize = versionDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		searchMeasuresForVersion(startIndex,versionDisplay.getPageSize());
		versionDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		panel.setHeading("My Measures > Create Measure Version of Draft","MainContent");
 	    panel.setContent(versionDisplay.asWidget());
    	Mat.focusSkipLists("MainContent");
    	clearRadioButtonSelection();
	}
	
    private void createDraft(){
    	int pageNumber = draftDisplay.getCurrentPage();
		int pageSize = draftDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
    	searchMeasuresForDraft(startIndex,draftDisplay.getPageSize());
    	draftDisplay.getErrorMessageDisplay().clear();
    	searchDisplay.getErrorMessageDisplayForBulkExport().clear();
    	panel.setHeading("My Measures > Create Draft of Existing Measure","MainContent");
 	    panel.setContent(draftDisplay.asWidget());
    	Mat.focusSkipLists("MainContent");
	}
  	private void cloneMeasure(final ManageMeasureDetailModel currentDetails, final boolean isDraftCreation) {
		String loggedinUserId = MatContext.get().getLoggedinUserId();
		MeasureCloningServiceAsync mcs = (MeasureCloningServiceAsync) GWT.create(MeasureCloningService.class);
		
		mcs.clone(currentDetails,loggedinUserId,isDraftCreation, new AsyncCallback<ManageMeasureSearchModel.Result>() {
			public void onSuccess(ManageMeasureSearchModel.Result result) {
				fireMeasureSelectedEvent(result.getId(), result.getVersion(),
						result.getName(), result.getShortName(), result.getScoringType(), result.isEditable(),result.isMeasureLocked(),
						result.getLockedUserId(result.getLockedUserInfo()));
				//fireMeasureEditEvent();
				Mat.hideLoadingMessage();
				
				//LOGIT
				if(isDraftCreation){					
					MatContext.get().getAuditService().recordMeasureEvent(result.getId(), "Draft Created", "Draft created based on Version "+result.getVersionValue(),false, new AsyncCallback<Boolean>(){

					@Override
					public void onFailure(Throwable caught) {
						//O&M 17
						((Button)draftDisplay.getSaveButton()).setEnabled(true);
					}

					@Override
					public void onSuccess(Boolean result) {
						//O&M 17
						((Button)draftDisplay.getSaveButton()).setEnabled(true);
					}
				});
				
			}
			}
			public void onFailure(Throwable caught) {
				//O&M 17
				((Button)draftDisplay.getSaveButton()).setEnabled(true);
				
				Mat.hideLoadingMessage();
				shareDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}	
		});
	}
	
	private void fireMeasureEditEvent() {
		MeasureEditEvent evt = new MeasureEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	private void fireMeasureSelectedEvent(String id, String version, String name, String shortName, String scoringType, boolean isEditable,boolean isLocked,String lockedUserId) {
		MeasureSelectedEvent evt = 
			new MeasureSelectedEvent(id, version, name, shortName, scoringType, isEditable,isLocked,lockedUserId);
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	private void getShareDetails(String id, int startIndex) {
		shareStartIndex = startIndex;
		MatContext.get().getMeasureService().getUsersForShare(id, startIndex, 
				shareDisplay.getPageSize(), new AsyncCallback<ManageMeasureShareModel>() {
					@Override
					public void onFailure(Throwable caught) {
						shareDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
					}

					@Override
					public void onSuccess(ManageMeasureShareModel result) {
						currentShareDetails = result;
						userShareInfo.setData(result);
						shareDisplay.buildDataTable(userShareInfo);
					}
				});
	}
	
	private void displayShare(String id, String name) {
		getShareDetails(id, 1);
		shareDisplay.setMeasureName(name);
		panel.setHeading("My Measures > Measure Sharing","MainContent");
		panel.setContent(shareDisplay.asWidget());
		Mat.focusSkipLists("MainContent");
	}
	
	private void displaySearch() {
		search(searchDisplay.getSearchString().getValue(), 1, searchDisplay.getPageSize());
		String heading ="My Measures";
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			heading ="Measures";
		}
		panel.setHeading(heading,"MainContent");
		//panel.setEmbeddedLink("MainContent");
		panel.setContent(searchDisplay.asWidget());		
		Mat.focusSkipLists("MainContent");
	}
	
	private void displayDetailForAdd() {
		panel.setHeading("My Measures > Create New Measure","MainContent");
		setDetailsToView();
		detailDisplay.showMeasureName(false);
		detailDisplay.showCautionMsg(false);
		panel.setContent(detailDisplay.asWidget());
	}
	
	private void displayHistory(String measureId,String measureName){
		
		int pageNumber = historyDisplay.getCurrentPage();
		int pageSize = historyDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		String heading ="My Measures > History";
		if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())){
			heading ="Measures > History";
		}
		
		panel.setHeading(heading,"MainContent");
		searchHistory(measureId, startIndex, pageSize);
		historyDisplay.setMeasureId(measureId);
		historyDisplay.setMeasureName(measureName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("MainContent");
	}
	
	private void displayDetailForEdit() {
		panel.setHeading("My Measures > Edit Measure","MainContent");
		detailDisplay.showMeasureName(false);
		detailDisplay.showCautionMsg(true);
		setDetailsToView();
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists("MainContent");
	}
	private void displayDetailForClone() {
		detailDisplay.clearFields();
		detailDisplay.setMeasureName(currentDetails.getName());
		detailDisplay.showMeasureName(true);
		panel.setHeading("My Measures > Clone Measure","MainContent");
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists("MainContent");
	}
	
	private void update() {
		//TODO seperate the clone from the edit update
		
		//exit if there is something already being saved
		if(!MatContext.get().getLoadingQueue().isEmpty())
			return;
		
		Mat.showLoadingMessage();
		updateDetailsFromView();
		
		if (isClone && isValid(currentDetails)) {
			cloneMeasure(currentDetails,false);
			isClone = false;
		} else if(isValid(currentDetails)) {
			final boolean isInsert = currentDetails.getId() == null;
			final String name = currentDetails.getName();
			final String shortName = currentDetails.getShortName();
			final String scoringType = currentDetails.getMeasScoring();
			final String version = currentDetails.getVersionNumber();
			MatContext.get().getMeasureService().save(currentDetails, new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					if(result.isSuccess()) {
						if(isInsert) {
							fireMeasureSelectedEvent(result.getId(), version, name, shortName, scoringType, true,false,null);//Need to revisit this, since don't know how this will affect
							fireMeasureEditEvent();
						}
						else {
							displaySearch();
						}
					}
					else {
						String message = null;
						switch(result.getFailureReason()) {
							default:
								message = "Unknown Code " + result.getFailureReason();
						}
						detailDisplay.getErrorMessageDisplay().setMessage(message);
					}
					Mat.hideLoadingMessage();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
					Mat.hideLoadingMessage();
				}
			});
		}
	}
	private void createNew() {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		currentDetails = new ManageMeasureDetailModel();
		displayDetailForAdd();
		Mat.focusSkipLists("MainContent");
	}
	
	
	
	private void export(String id, String name) {
		//US 170
		MatContext.get().getAuditService().recordMeasureEvent(id, ConstantMessages.EXPORT, name, true, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
			}
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage("Error while adding export comment");
			}
			
		});
		currentExportId = id;
		exportDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		panel.setHeading("My Measures > Export","MainContent");
		panel.setContent(exportDisplay.asWidget());
		exportDisplay.setMeasureName(name);
		Mat.focusSkipLists("MainContent");
	}
	
	
	private void searchHistory(String measureId, int startIndex, int pageSize) {
		List<String> filterList = new ArrayList<String>();
	    filterList.add("Export");
		MatContext.get().getAuditService().executeMeasureLogSearch(measureId, startIndex, pageSize,filterList, new AsyncCallback<SearchHistoryDTO>() {		
			@Override
			public void onSuccess(SearchHistoryDTO data) {
				historyModel = new HistoryModel(data.getLogs());
				historyModel.setPageSize(historyDisplay.getPageSize());
				historyModel.setTotalPages(data.getPageCount());
				historyDisplay.buildDataTable(historyModel,data.getPageCount(),data.getTotalResults(), historyDisplay.getCurrentPage(),historyDisplay.getPageSize());
			}
			@Override
			public void onFailure(Throwable caught) {
				//historyDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	private void edit(String name) {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		MatContext.get().getMeasureService().getMeasure(name, new AsyncCallback<ManageMeasureDetailModel>() {
			
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				currentDetails = result;
				displayDetailForEdit();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	private void editClone(String id) {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		MatContext.get().getMeasureService().getMeasure(id, new AsyncCallback<ManageMeasureDetailModel>() {
			
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				currentDetails = result;
				displayDetailForClone();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	private void searchMeasuresForDraft(int startIndex, int pageSize){
		
		MatContext.get().getMeasureService().searchMeasuresForDraft(startIndex, pageSize, 
				new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				draftMeasureResults = new ManageDraftMeasureModel(result.getData());
				draftMeasureResults.setPageSize(result.getData().size());
				draftMeasureResults.setTotalPages(result.getPageCount());
				draftDisplay.buildDataTable(draftMeasureResults, result.getPageCount(), result.getResultsTotal(), draftDisplay.getCurrentPage(), draftDisplay.getPageSize());
			}
			@Override
			public void onFailure(Throwable caught) {
				draftDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		
	}
	
	private void searchMeasuresForVersion(int startIndex, int pageSize){
		
		MatContext.get().getMeasureService().searchMeasuresForVersion(startIndex, pageSize, 
				new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				versionMeasureResults = new ManageVersionMeasureModel(result.getData());
				versionMeasureResults.setPageSize(result.getData().size());
				versionMeasureResults.setTotalPages(result.getPageCount());
				versionDisplay.buildDataTable(versionMeasureResults, result.getPageCount(), result.getResultsTotal(), versionDisplay.getCurrentPage(), versionDisplay.getPageSize());
			}
			@Override
			public void onFailure(Throwable caught) {
				versionDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		
	}
	
	private void search(String searchText, int startIndex, int pageSize) {
		final String lastSearchText = searchText;
		showSearchingBusy(true);
		
		MatContext.get().getMeasureService().search(searchText, startIndex, pageSize, 
				new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
					MeasureSearchResultsAdapter searchResults = new MeasureSearchResultsAdapter();
					addHandlersToAdaptor(searchResults);
					searchResults.setData(result);
					result.setSelectedExportIds(new ArrayList<String>());
					manageMeasureSearchModel = result;
					MatContext.get().setManageMeasureSearchModel(manageMeasureSearchModel);
				
					if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoMeasuresMessage());
					}else{
						searchDisplay.getErrorMessageDisplay().clear();
						searchDisplay.getErrorMessageDisplayForBulkExport().clear();
					}
					SearchResultUpdate sru = new SearchResultUpdate();
					sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
					searchDisplay.buildDataTable(searchResults);
					showSearchingBusy(false);
				}else{
					result.setSelectedTransferIds(new ArrayList<String>());
					result.setSelectedTransferResults(new ArrayList<Result>());
					manageMeasureSearchModel = result;
					AdminMeasureSearchResultAdaptor searchAdminResults = new AdminMeasureSearchResultAdaptor();
					searchAdminResults.setObserver(new AdminMeasureSearchResultAdaptor.Observer() {

						@Override
						public void onHistoryClicked(Result result) {
							historyDisplay.setReturnToLinkText("<< Return to Measure Library");
							if(!result.isEditable()){
								historyDisplay.setUserCommentsReadOnly(true);
							}else{
								historyDisplay.setUserCommentsReadOnly(false);
							}
								
							displayHistory(result.getId(),result.getName());
						}
						@Override
						public void onTransferSelectedClicked(Result result) {
							updateTransferID(result);
						}
						
					});
					searchAdminResults.setData(result);
					result.setSelectedExportIds(new ArrayList<String>());
					manageMeasureSearchModel = result;
					MatContext.get().setManageMeasureSearchModel(manageMeasureSearchModel);
				
					if(result.getResultsTotal() == 0 && !lastSearchText.isEmpty()){
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoMeasuresMessage());
					}else{
						searchDisplay.getErrorMessageDisplay().clear();
						searchDisplay.getErrorMessageDisplayForBulkExport().clear();
					}
					SearchResultUpdate sru = new SearchResultUpdate();
					sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
					searchDisplay.buildDataTable(searchAdminResults);
					showSearchingBusy(false);
					
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				showSearchingBusy(false);
			}
		});
	}
	
	/**
	 * Method to update Transfer List in case of Admin
	 * 
	 * */
	
	private void updateTransferID(Result result){
		System.out.println("==================Transfer CLicked========" + result.getId());
		if(result.isTransferable()){
			manageMeasureSearchModel.getSelectedTransferIds().add(result.getId());
			manageMeasureSearchModel.getSelectedTransferResults().add(result);
		}else{
			for(int i=0 ;i< manageMeasureSearchModel.getSelectedTransferIds().size();i++){
				if(result.getId() == manageMeasureSearchModel.getSelectedTransferIds().get(i)){
					manageMeasureSearchModel.getSelectedTransferIds().remove(i);
					manageMeasureSearchModel.getSelectedTransferResults().remove(i);
				}
			}
		}
	}
	
	/**
	 * 
	 * Handlers for Non Admin Search Results
	 * 
	 * */
	private void addHandlersToAdaptor(MeasureSearchResultsAdapter searchResults){
		
		searchResults.setObserver(new MeasureSearchResultsAdapter.Observer() {
			@Override
			public void onShareClicked(ManageMeasureSearchModel.Result result) {
				displayShare(result.getId(), result.getName());
			}
			@Override
			public void onExportClicked(ManageMeasureSearchModel.Result result) {
				export(result.getId(), result.getName());
			}
			
			@Override
			public void onHistoryClicked(ManageMeasureSearchModel.Result result) {
				historyDisplay.setReturnToLinkText("<< Return to Measure Library");
				if(!result.isEditable()){
					historyDisplay.setUserCommentsReadOnly(true);
				}else{
					historyDisplay.setUserCommentsReadOnly(false);
				}
					
				displayHistory(result.getId(),result.getName());
			}
			
			
			@Override
			public void onEditClicked(ManageMeasureSearchModel.Result result) {
			    //When edit has been clicked, no need to fire measureSelected Event.
//				fireMeasureSelectedEvent(result.getId(), 
//						result.getName(), result.getShortName(), result.getScoringType(),result.isEditable(),result.isMeasureLocked(),
//						result.getLockedUserId(result.getLockedUserInfo()));
					edit(result.getId());
			}
			
			@Override
			public void onCloneClicked(ManageMeasureSearchModel.Result result) {
				isClone = true;
				editClone(result.getId());
			}

			@Override
			public void onExportSelectedClicked(CustomCheckBox checkBox){
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				if(checkBox.getValue()){
					if(manageMeasureSearchModel.getSelectedExportIds().size() > 89){
						searchDisplay.getErrorMessageDisplayForBulkExport().setMessage("Export file has a limit of 90 measures");
						searchDisplay.getExportSelectedButton().setFocus(true);
						checkBox.setValue(false);
					}else{
						manageMeasureSearchModel.getSelectedExportIds().add(checkBox.getFormValue());
					}					
				}else{
					manageMeasureSearchModel.getSelectedExportIds().remove(checkBox.getFormValue());
				}
			}
		});

		
	}
	private void showSearchingBusy(boolean busy){
		if(busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
		((Button)searchDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox)(searchDisplay.getSearchString())).setEnabled(!busy);
	}
	
	public Widget getWidget() {
		return panel;
	}

	private void setDetailsToView() {
		detailDisplay.getName().setValue(currentDetails.getName());
		detailDisplay.getShortName().setValue(currentDetails.getShortName());
		
		//US 421. Measure scoring choice is now part of measure creation process.
		detailDisplay.getMeasScoringChoice().setValueMetadata(currentDetails.getMeasScoring());
	}
	
	
	private void updateDetailsFromView() {
		currentDetails.setName(detailDisplay.getName().getValue().trim());
		currentDetails.setShortName(detailDisplay.getShortName().getValue().trim());
		String measureScoring = detailDisplay.getMeasScoringValue(); 
		
		//US 421. Update the Measure scoring choice from the UI.
		if(isValidValue(measureScoring)){
			currentDetails.setMeasScoring(measureScoring);
		}
		
		MatContext.get().setCurrentMeasureName(detailDisplay.getName().getValue().trim());
		MatContext.get().setCurrentShortName(detailDisplay.getShortName().getValue().trim());
		MatContext.get().setCurrentMeasureScoringType(detailDisplay.getMeasScoringValue());
		//MatContext.get().setCurrentMeasureVersion(detailDisplay.getMeasureVersion().getValue().trim());
	}
	
	public boolean isValid(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();
		if("".equals(model.getName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getMeasureNameRequiredMessage());
		}
		if("".equals(model.getShortName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getAbvNameRequiredMessage());
		}
		
		//US 421. Validate Measure Scoring choice
		String scoring = model.getMeasScoring();
		if(scoring == null || !isValidValue(model.getMeasScoring())) {
			message.add(MessageDelegate.s_ERR_MEASURE_SCORE_REQUIRED);
		}
		
		//TODO handle clobbering of measure names and abbreviated measure names. 
		
		boolean valid = message.size() == 0;
		if(!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
		}
		else {
			detailDisplay.getErrorMessageDisplay().clear();
			searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		}
		return valid;
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
	
	private void saveExport() {
		Window.open(buildExportURL() + "&type=save", "_self", "");
	}
	private void openExport() {
		Window.open(buildExportURL() + "&type=open", "_blank", "");
	}
	private String buildExportURL() {
		String url = GWT.getModuleBaseURL() + "export?id=" + currentExportId + "&format=";
		url += (exportDisplay.isEMeasure() ? "emeasure" : exportDisplay.isSimpleXML() ? "simplexml" : exportDisplay.isCodeList() ? "codelist" : "zip");
		return url;
	}
	
	private void bulkExport(List<String> selectedMeasureIds){
		String measureId = "";
		for (String id : selectedMeasureIds) {			
			measureId += id+"&id="; 
		}
		measureId = measureId.substring(0,measureId.lastIndexOf("&"));
		String url = GWT.getModuleBaseURL() + "bulkExport?id=" + measureId; 
		url += "&type=open";
		manageMeasureSearchModel.getSelectedExportIds().clear();
		FormPanel form = searchDisplay.getForm();
		form.setAction(url);
		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_POST);
		form.submit();
	}
	
	
	/**
	 * Verifies the valid value required for the list box
	 * @param value
	 * @return
	 */
	private boolean isValidValue(String value){
		return  !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
	
	private void clearRadioButtonSelection(){
		versionDisplay.getMajorRadioButton().setValue(false);
		versionDisplay.getMinorRadioButton().setValue(false);
	}

	/**
	 * @return the bulkExportMeasureIds
	 */
	public List<String> getBulkExportMeasureIds() {
		return bulkExportMeasureIds;
	}

	/**
	 * @param bulkExportMeasureIds the bulkExportMeasureIds to set
	 */
	public void setBulkExportMeasureIds(List<String> bulkExportMeasureIds) {
		this.bulkExportMeasureIds = bulkExportMeasureIds;
	}
	
	
}
