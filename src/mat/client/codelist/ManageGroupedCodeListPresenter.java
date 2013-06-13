package mat.client.codelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.DTO.HasListBoxDTO;
import mat.client.Mat;
import mat.client.codelist.events.CancelEditCodeListEvent;
import mat.client.codelist.events.CreateNewCodeListEvent;
import mat.client.codelist.events.CreateNewGroupedCodeListEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.SelectAllEvent;
import mat.client.shared.search.SelectAllEventHandler;
import mat.model.CodeListSearchDTO;
import mat.model.GroupedCodeListDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageGroupedCodeListPresenter extends BaseDetailPresenter {
	private int default_page_number = 1;
	/*USTod*/
	private static final String MY_VALUE_SETS_GROUPED_VALUE_SET = "My Value Sets  > Grouped Value Set";
	private static final String MY_VALUE_SETS_GROUPED_VALUE_SET_CREATE = "My Value Sets  > Create a Grouped Value Set";
	private static final String MY_VALUE_SETS_GROUPED_VALUE_SET_UPDATE = "My Value Sets  > Update a Grouped Value Set";
	private static final String MY_VALUE_SETS_GROUPED_VALUE_SET_MANAGE = "My Value Sets  > Update a Grouped Value Set  > Manage Value Sets";
	private static final String MY_VALUE_SETS_GROUPED_VALUE_SET_ADD = "My Value Sets  > Add Value Sets";

	
	
	public static interface GroupedCodeListDisplay extends BaseDisplay {
		public HasClickHandlers getCreateNewButton();
		public HasClickHandlers getCreateNewGroupedButton();
		public HasClickHandlers getAddCodeListButton();
		public void setAddCodeListButtonEnabled(boolean b);
		void setCodeLists(ManageGroupedCodeListsSummaryModel sModel,int pageCount,int total,int currentPage);
		public void DisableAnchors();
		public void EnableAnchors();
		public CodeListsSummaryWidget getCodeListsSummaryWidget();
	}
	
	public static interface CodeListsSummaryDisplay {
		public void buildSummaryDataTable(ManageGroupedCodeListsSummaryModel codes,int totalPagesCount,int total,int currentPage);
		public HasPageSelectionHandler getPageSelectionTool();
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);	
	}
	public static interface AddCodeListDisplay extends BaseAddDisplay<GroupedCodeListDTO> {
		public void setCodeListName(String name);
		public ListBoxMVP getCodeListInput();
		public Button getSearchButton();
		public HasValue<String> getCodeId();
		public String getCodeListId();
		public String getCodeListName();
		public HasValue<String> getCodeDescription();
		void setCodeListOptions(List<? extends HasListBox> texts);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);
		public String getCodeListOid();
		public ListBox getValueSetSearchFilterPanel();
	}
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	private GroupedCodeListDisplay detailDisplay;
	private ManageCodeListDetailModel currentDetails;
	private ManageGroupCodeListsModel currentCodeLists;
	private ListBoxCodeProvider listBoxProvider;
	private AddCodeListDisplay addCodeListDisplay;
	private boolean codeListExists = false;
	
	protected BaseDisplay getDetailDisplay() {
		return detailDisplay;
	}
	
	public ManageGroupedCodeListPresenter(GroupedCodeListDisplay dDisplayArg, AddCodeListDisplay addCodeListArg, ListBoxCodeProvider lbcp) {
		super(dDisplayArg, lbcp);
		
		this.detailDisplay = dDisplayArg;
		listBoxProvider = lbcp;
		addCodeListDisplay = addCodeListArg;
		
		addCodeListDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				if(pageNumber == -1){ // if next button clicked
					if(addCodeListDisplay.getCurrentPage() == currentDetails.getCodeListsPageCount()){
						pageNumber = addCodeListDisplay.getCurrentPage();
					}else{
						pageNumber = addCodeListDisplay.getCurrentPage() + 1;
					}
				}else if(pageNumber == 0){ // if first button clicked
					pageNumber = 1;
				}else if(pageNumber == -9){ // if prev button clicked
					if(addCodeListDisplay.getCurrentPage() == 1){
						pageNumber = addCodeListDisplay.getCurrentPage();
					}else{
						pageNumber = addCodeListDisplay.getCurrentPage() - 1;
					}
				}
				addCodeListDisplay.setCurrentPage(pageNumber);//Setting it to the display.
				currentCodeLists.setCurrentPage(pageNumber);
				displayCodeListsForTheSelectedPage(currentDetails.getID());
			}
		});
		
		
		
		addCodeListDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCodeListDisplay.getSuccessMessageDisplay().clear();
				addCodeListDisplay.getErrorMessageDisplay().clear();
				int startIndex = 1;
				String searchString = "";
				int filter =addCodeListDisplay.getValueSetSearchFilterPanel().getSelectedIndex();				
				String currentSortColumn = "name"; 
				boolean sortIsAscending =true;			
				boolean defaultCodeList =true;
				search(searchString,
						startIndex, currentSortColumn, sortIsAscending,defaultCodeList, filter );
			}
		});
						
		addCodeListDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCodeListDisplay.getSuccessMessageDisplay().clear();
				addCodeListDisplay.getErrorMessageDisplay().clear();
				if(isValidCodeList())
					addCodeList();
			}
		});
		
		addCodeListDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCodeListDisplay.getSuccessMessageDisplay().clear();
				addCodeListDisplay.getErrorMessageDisplay().clear();
				addCodeListDisplay.getCodeId().setValue("");
				addCodeListDisplay.getCodeDescription().setValue("");
				
			}
		});
		addCodeListDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                clearMessages();
				removeSelectedCodeListsFromGroupCodeList();
			}
		});
		addCodeListDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				displayDetail(MY_VALUE_SETS_GROUPED_VALUE_SET_UPDATE);
				resetToFirstPage();
			}
		});
		addCodeListDisplay.getSelectAllTool().addSelectAllHandler(new SelectAllEventHandler() {
			
			@Override
			public void onSelectAll(SelectAllEvent event) {
				 setModelCodesToView(event.isChecked());//This line will reconstruct the entire table with checked/Not checked value
			}
		});
		
		detailDisplay.getAddCodeListButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				
				addCodeListDisplay.setCurrentPage(default_page_number);
				displayCodeList();
				addCodeListDisplay.getValueSetSearchFilterPanel().setSelectedIndex(0);
				addCodeListDisplay.getSearchButton().fireEvent(new GwtEvent<ClickHandler>() {
						@Override
						public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
							return ClickEvent.getType();
						}
						@Override
						protected void dispatch(ClickHandler handler) {
							handler.onClick(null);
						}
				});

			}
		});
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentDetails.setDraft(true);
				update(MessageDelegate.DRAFT);
			}
		});
		
		detailDisplay.getSaveCompleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentDetails.setDraft(false);
				update(MessageDelegate.COMPLETE);
			}
		});
		
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.getCodeListsSummaryWidget().setCurrentPage(default_page_number);//Need to reset to first Page when the cancel button has been clicked.
				MatContext.get().getEventBus().fireEvent(new CancelEditCodeListEvent());
			}
		});
		
		 detailDisplay.getCreateNewButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					MatContext.get().restartTimeoutWarning();
					MatContext.get().getEventBus().fireEvent(new CreateNewCodeListEvent());
				}
			});
			
			detailDisplay.getCreateNewGroupedButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					MatContext.get().getEventBus().fireEvent(new CreateNewGroupedCodeListEvent());
				}
			});
		
		detailDisplay.getCategory().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String category = event.getValue();
				boolean populate = false;
				populateCodeSystemOptions(category,populate);
			}
		});
		
		detailDisplay.getGenerateOidButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getCodeListService().generateUniqueOid(currentDetails, new AsyncCallback<String>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(String result) {
						detailDisplay.getOid().setValue(result);
						((TextAreaWithMaxLength)detailDisplay.getOid()).setFocus(true);
						Element element = ((TextAreaWithMaxLength)detailDisplay.getOid()).getElement();
						element.setAttribute("aria-label", "OID is generated by system");
						element.setAttribute("aria-role", "input");
						element.setAttribute("aria-labelledby", "LiveRegion");
						element.setAttribute("aria-live", "assertive");
						element.setAttribute("aria-atomic", "true");
						element.setAttribute("aria-relevant", "all");
						element.setAttribute("role", "alert");
					}});
				
			}
		});
		
		((TextAreaWithMaxLength)detailDisplay.getOid()).addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				((TextAreaWithMaxLength)detailDisplay.getOid()).getElement().setAttribute("aria-label", "");
				((TextAreaWithMaxLength)detailDisplay.getOid()).getElement().removeAttribute("role");
			}
		});
		

		//US 413. Value change listener to show or remove Steward Other text box.
		detailDisplay.getOrganisation().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {				
				String value = detailDisplay.getStewardValue();
				if(value.startsWith("Other")){
					detailDisplay.showOtherTextBox();
					}
				else{
					detailDisplay.hideOtherTextBox();
				}
				
			}
		});

		detailDisplay.getLastModifiedDate().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				detailDisplay.asWidget().getElement().setAttribute("role", "alert");
				detailDisplay.setSaveCompleteButtonEnabled(currentDetails.isMyValueSet());
			}
		});
		
		if(detailDisplay.getCodeListsSummaryWidget().getPageSelectionTool() != null){
			detailDisplay.getCodeListsSummaryWidget().getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
				
				@Override
				public void onPageSelection(PageSelectionEvent event) {
					int pageNumber = event.getPageNumber();
					if(pageNumber == -1){ // if next button clicked
						if(detailDisplay.getCodeListsSummaryWidget().getCurrentPage() == currentDetails.getCodeListsPageCount()){
							pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage();
						}else{
							pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage() + 1;
						}
					}else if(pageNumber == 0){ // if first button clicked
						pageNumber = 1;
					}else if(pageNumber == -9){ // if first button clicked
						if(detailDisplay.getCodeListsSummaryWidget().getCurrentPage() == 1){
							pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage();
						}else{
							pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage() - 1;
						}
					}
					detailDisplay.getCodeListsSummaryWidget().setCurrentPage(pageNumber);
					currentCodeLists.setCurrentPage(pageNumber);
					displayCodeListsSummary(currentDetails.getID());
				}
			});
		}
		
		/*USTod*/
		setPanelContentAndHeading(detailDisplay.asWidget(), MY_VALUE_SETS_GROUPED_VALUE_SET);
	}
	
	private void displayCodeListsSummary(String codeListId){
		final int pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage();
		int pageSize = detailDisplay.getCodeListsSummaryWidget().getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		MatContext.get().getCodeListService().getGroupedCodeList(codeListId,startIndex,pageSize, new AsyncCallback<ManageCodeListDetailModel>() {
			@Override
			public void onSuccess(ManageCodeListDetailModel result) {
				ManageGroupedCodeListsSummaryModel summaryModel = new ManageGroupedCodeListsSummaryModel();
				summaryModel.setData(result.getCodeLists());
				detailDisplay.setCodeLists(summaryModel, currentDetails.getCodeListsPageCount(), currentDetails.getTotalCodeList(), detailDisplay.getCodeListsSummaryWidget().getCurrentPage());
			}
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private void removeSelectedCodeListsFromGroupCodeList() {
		List<GroupedCodeListDTO> selectedCodeList = currentCodeLists.getSelected();
		for(GroupedCodeListDTO dto : selectedCodeList){
			currentDetails.getRemoveValueSetsMap().put(dto.getId(), dto);
		}
		MatContext.get().getCodeListService().saveorUpdateGroupedCodeList(currentDetails, new AsyncCallback<SaveUpdateCodeListResult>() {
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				if(result.isSuccess()) {
					currentDetails = result.getCodeListDetailModel();
				    int pageCount = currentDetails.getCodeListsPageCount();
				    if(pageCount < addCodeListDisplay.getCurrentPage()){
				    	addCodeListDisplay.setCurrentPage(pageCount);
				    }
					currentDetails.setID(result.getId());
					displayCodeListsForTheSelectedPage(currentDetails.getID());
				}
				
			}
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
			}
		
		});
			
		
	}
	private void setCodeListOptions(String category) {
		if(category != null && category.length() > 0) {
			MatContext.get().getCodeListService().getCodeListsForCategory(category,
					new AsyncCallback<List<? extends HasListBox>>() {
	
				@Override
				public void onFailure(Throwable caught) {
					addCodeListDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				}
	
				@Override
				public void onSuccess(List<? extends HasListBox> result) {
					addCodeListDisplay.setCodeListOptions(result);
				}
			
			});
		}
		else {
			addCodeListDisplay.setCodeListOptions(new ArrayList<HasListBoxDTO>());
		}
	}
	
	private void displayDetail(String heading) {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		/*USTod*/
		setPanelContentAndHeading(detailDisplay.asWidget(), heading);
//		panel.setContent(detailDisplay.asWidget());
		
		Mat.focusSkipLists("MainContent");
	}
	private void displayCodeList() {
		setCodeListOptions(detailDisplay.getCategory().getValue());
		addCodeListDisplay.setReturnToLink("Return to  "+currentDetails.getName());
		resetAddCodeListScreen();
		
		/*USTod*/
		setPanelContentAndHeading(addCodeListDisplay.asWidget(), MY_VALUE_SETS_GROUPED_VALUE_SET_MANAGE+" for "+currentDetails.getName());
		
		Mat.focusSkipLists("MainContent");
	}
	
	protected List<String> buildValidationMessages(ManageCodeListDetailModel model) {
		List<String> message = super.buildValidationMessages(model);

		return message;
	}
	
	private void displayCodeListsForTheSelectedPage(String codeListId) {
		final int pageNumber = addCodeListDisplay.getCurrentPage();
		int pageSize =   addCodeListDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		MatContext.get().getCodeListService().getGroupedCodeList(codeListId,startIndex,pageSize, new AsyncCallback<ManageCodeListDetailModel>() {
			@Override
			public void onSuccess(ManageCodeListDetailModel result) {
				currentCodeLists = new ManageGroupCodeListsModel(result.getCodeLists(),false);
				currentCodeLists.setPageSize(addCodeListDisplay.getPageSize());
				currentDetails.setCodeLists(result.getCodeLists());//Need to set this to keep in sync with the current Page codesList
				addCodeListDisplay.buildDataTable(currentCodeLists, false, currentDetails.getTotalCodeList(), currentDetails.getCodeListsPageCount(), addCodeListDisplay.getCurrentPage());
			}
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}

	private void update(final int source) {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		updateModelDetailsFromView();
		if(isValid(currentDetails)) {
			MatContext.get().getCodeListService().saveorUpdateGroupedCodeList(currentDetails, new AsyncCallback<SaveUpdateCodeListResult>() {
				@Override
				public void onSuccess(SaveUpdateCodeListResult result) {
					if(result.isSuccess()) {
						currentDetails.setExistingCodeList(true);//Need to set this flag, after a sucessful save operation. Otherwise when Adding ValueSets, throws NULL Exception
						currentDetails.setID(result.getId());
						detailDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGroupedValueSetChangedSavedMessage(source));
						detailDisplay.EnableAnchors();
						String lmd = result.getLastModifiedDate();
						if(lmd!=null){
							currentDetails.setLastModifiedDate(lmd);
							if(source == MessageDelegate.COMPLETE)
								detailDisplay.getLastModifiedDate().setEnabled(true);
						}
						populateFields();
						
						if(source == MessageDelegate.DRAFT){
							//category should be disabled on successfull draft save
							detailDisplay.getCategoryListBox().setEnabled(false);
							detailDisplay.setSaveCompleteButtonEnabled(true);
						}
					}
					else {
						String message = null;
						switch(result.getFailureReason()) {
						
							case SaveUpdateCodeListResult.NOT_UNIQUE:
								message = MatContext.get().getMessageDelegate().getGroupedCodeListExistsMessage(currentDetails.getName());
								break;
							case SaveUpdateCodeListResult.OID_NOT_UNIQUE:
								message = MatContext.get().getMessageDelegate().getOIDInUseMessage();
								break;
							case SaveUpdateCodeListResult.INVALID_LAST_MODIFIED_DATE:
								message = MatContext.get().getMessageDelegate().getInvalidLastModifiedDateMessage();
								break;
							case SaveUpdateCodeListResult.LAST_MODIFIED_DATE_DUPLICATE:
								message = MatContext.get().getMessageDelegate().getLastModifiedDateNotUniqueMessage();
								break;
							default:
								message = MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason());
						}
						detailDisplay.getErrorMessageDisplay().setMessage(message);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getIdentityMessage(caught.getLocalizedMessage()));
				}
			
			});

		}
	}
	
	public Widget getWidget() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		/*USTod*/
		return panel;
	}

	//Changed the method name for consistency. 
	private void populateFields() {
		detailDisplay.getName().setValue(currentDetails.getName());
		
		//US 413. Populate the value for both the Steward List box and the Steward Other if any.
		detailDisplay.getOrganisation().setValue(currentDetails.getSteward());
		String stewardOther = currentDetails.getStewardOther();
		if(stewardOther != null && !stewardOther.equalsIgnoreCase("")){
			detailDisplay.showOtherTextBox();			
			detailDisplay.getStewardOther().setValue(currentDetails.getStewardOther());
			
		}else{
			detailDisplay.hideOtherTextBox();
		}

		detailDisplay.getCategory().setValue(currentDetails.getCategory());		
		detailDisplay.getRationale().setValue(currentDetails.getRationale());
		detailDisplay.getComments().setValue(currentDetails.getComments());
		detailDisplay.getCodeSystem().setValue(currentDetails.getCodeSystem());
		detailDisplay.getCodeSystemVersion().setValue(currentDetails.getCodeSystemVersion());
		detailDisplay.getErrorMessageDisplay().clear();
		
		//US210 prefixing the oid widget title with the oid itself so it can be viewed by a user
		String oid = currentDetails.getOid();
		detailDisplay.getOid().setValue(oid);
		String oidTitle = oid == null ? detailDisplay.getOidTitle() : oid+" - "+detailDisplay.getOidTitle();
		((TextAreaWithMaxLength)detailDisplay.getOid()).setTitle(oidTitle);
		
		detailDisplay.getLastModifiedDate().setValue(currentDetails.getLastModifiedDate());
		setModelCodesToView(false);
		
		enableOrDisableFields();
				
	}
	
	private void setModelCodesToView(boolean value) {
		if(currentDetails.getCodeLists() != null){
			Collections.sort(currentDetails.getCodeLists(), new GroupedCodeListDTO.Comparator());
			addCodeListDisplay.setCodeListName(currentDetails.getName());
			currentCodeLists = new ManageGroupCodeListsModel(currentDetails.getCodeLists(),value);
			currentCodeLists.setPageSize(addCodeListDisplay.getPageSize());
			addCodeListDisplay.buildDataTable(currentCodeLists,value,currentDetails.getTotalCodeList(),currentDetails.getCodeListsPageCount(),addCodeListDisplay.getCurrentPage());
		}
		ManageGroupedCodeListsSummaryModel codeListsSummaryModel = new ManageGroupedCodeListsSummaryModel();
		codeListsSummaryModel.setData(currentDetails.getCodeLists());
		detailDisplay.setCodeLists(codeListsSummaryModel,currentDetails.getCodeListsPageCount(),currentDetails.getTotalCodeList(),detailDisplay.getCodeListsSummaryWidget().getCurrentPage());
		if(!isNewCodeList){
			if(currentDetails.isDraft())
				ReadOnlyHelper.setReadOnlyForCurrentMeasure(addCodeListDisplay.asWidget(),currentDetails.isMyValueSet());
			else
				ReadOnlyHelper.setReadOnlyForCurrentMeasure(addCodeListDisplay.asWidget(),false);
		} else {
			ReadOnlyHelper.setReadOnlyForCurrentMeasure(addCodeListDisplay.asWidget(),isNewCodeList);
		} 
	}
	
	private void updateModelDetailsFromView() {
		currentDetails.setName(detailDisplay.getName().getValue());
		
		//US 413. Update Steward and Steward Other value.		
		currentDetails.setSteward(detailDisplay.getOrganisation().getValue());
		currentDetails.setStewardValue(detailDisplay.getStewardValue());
		currentDetails.setStewardOther(detailDisplay.getStewardOtherValue());
		currentDetails.setCategory(detailDisplay.getCategory().getValue());
		currentDetails.setRationale(detailDisplay.getRationale().getValue());
		currentDetails.setComments(detailDisplay.getComments().getValue());
		//no need to update code system for a grouped value set
		//this will need to be commented US 216
		//currentDetails.setCodeSystem(detailDisplay.getCodeSystemValue());
		//US 216. The codeSystemVersion should be "Grouping" for GroupedCodeList.
		currentDetails.setCodeSystemVersion(ConstantMessages.GROUPED_CODE_LIST_CS);
		currentDetails.setOid(detailDisplay.getOid().getValue());
		/*US537*/
		currentDetails.setLastModifiedDate(detailDisplay.getLastModifiedDate().getValue());
	}
	
	public void createNewGroupedCodeList() {
		/*USTod*/
//		panel.setHeading(MY_VALUE_SETS_GROUPED_VALUE_SET_CREATE, MY_VALUE_SETS_GROUPED_VALUE_SET_CREATE);
		
		isNewCodeList = true;
		currentDetails = new ManageCodeListDetailModel();
		currentDetails.setIsGrouped(true);
		currentDetails.setMeasureId(MatContext.get().getCurrentMeasureId());
		detailDisplay.getCategory().setValue("");
		detailDisplay.getOrganisation().setValue("");
		populateFields();
		detailDisplay.DisableAnchors();
		detailDisplay.getOid().setValue("");
		displayDetail(MY_VALUE_SETS_GROUPED_VALUE_SET_CREATE);
	}
	public void editCodeList(String key) {
		/*USTod*/
//		panel.setHeading(MY_VALUE_SETS_GROUPED_VALUE_SET_UPDATE, MY_VALUE_SETS_GROUPED_VALUE_SET_UPDATE);
		
		isNewCodeList = false;
		final int pageNumber = detailDisplay.getCodeListsSummaryWidget().getCurrentPage();
		int pageSize = detailDisplay.getCodeListsSummaryWidget().getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		MatContext.get().getCodeListService().getGroupedCodeList(key,startIndex,pageSize, new AsyncCallback<ManageCodeListDetailModel>() {
			@Override
			public void onSuccess(ManageCodeListDetailModel result) {
				currentDetails = result;
				String category = currentDetails.getCategory();
				populateCodeSystemOptions(category,true);
				detailDisplay.EnableAnchors();
				displayDetail(MY_VALUE_SETS_GROUPED_VALUE_SET_UPDATE);
			}
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert(caught.getMessage());
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
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
	public Widget getAddCodeLists(){
		/*USTod*/
		panel.setHeading(MY_VALUE_SETS_GROUPED_VALUE_SET_ADD, MY_VALUE_SETS_GROUPED_VALUE_SET_ADD);
		
		addCodeListDisplay.getSuccessMessageDisplay().clear();
		return addCodeListDisplay.asWidget();
	}
	
	private void addCodeList() {
		addCodeListDisplay.getErrorMessageDisplay().clear();
		final GroupedCodeListDTO dto = new GroupedCodeListDTO();
		    MatContext.get().getCodeListService().getGroupedCodeList(currentDetails.getID(),new AsyncCallback<ManageCodeListDetailModel>(){

				@Override
				public void onFailure(Throwable caught) {
					addCodeListDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(ManageCodeListDetailModel result) {
					/* 
					 * setting to false before running the check below
					 * not sure why this is a class level variable
					 */
					currentDetails = result;
					codeListExists = false;
					dto.setCodeSystem(result.getCodeSystem());
				
					List<GroupedCodeListDTO> existingCodeLists = currentDetails.getCodeLists();
					if(existingCodeLists != null && !existingCodeLists.isEmpty()){
						for(GroupedCodeListDTO existingCodeList : existingCodeLists){

							String existingOID = existingCodeList.getOid();
							String selectedOID = addCodeListDisplay.getCodeListOid();
							if(existingOID != null && selectedOID != null && existingOID.trim().equalsIgnoreCase(selectedOID.trim())){
					    	   codeListExists = true;
							   addCodeListDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getCodeListAlreadyExistsForGroupedMessage(currentDetails.getName()));
							   return;
							}
						}
					}
					if(!codeListExists){
						HasValue<String> tmp = addCodeListDisplay.getCodeId();
						dto.setId(addCodeListDisplay.getCodeId().getValue());
						dto.setName(addCodeListDisplay.getCodeListName());
						dto.setDescription(addCodeListDisplay.getCodeDescription().getValue());
						dto.setOid(addCodeListDisplay.getCodeListOid());
						currentDetails.getAddValueSetsMap().put(dto.getId(), dto);
						addCodeListtoGroupedCodeList(currentDetails);
					}
					
				}
			});

	}
	
	
	
	private boolean isValidCodeList(){
		List<String> messages = new ArrayList<String>();
		String codeId = addCodeListDisplay.getCodeId().getValue();
		if(codeId == null || codeId.equals("") ){
		   messages.add(MatContext.get().getMessageDelegate().getCodeListRequiredMessage());	
		}
		
		String codeDesc = addCodeListDisplay.getCodeDescription().getValue();
		
		if( codeDesc == null || codeDesc.equals("")  ){
		   messages.add(MatContext.get().getMessageDelegate().getDescriptionRequiredMeassage());
		}

		boolean valid = messages.size() == 0;
		if(!valid){
			addCodeListDisplay.getErrorMessageDisplay().setMessages(messages);
		}else{
			addCodeListDisplay.getErrorMessageDisplay().clear();
		}
		return valid;
	}
	
	private void addCodeListtoGroupedCodeList(ManageCodeListDetailModel currentGDetails) {
		 MatContext.get().getCodeListService().saveorUpdateGroupedCodeList(currentGDetails, new AsyncCallback<SaveUpdateCodeListResult>() {
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				if(result.isSuccess()) {
					currentDetails.setID(result.getId());
					addCodeListDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getCodeListAddedGroupMessage());
					addCodeListDisplay.getCodeListInput().setValue("");
					addCodeListDisplay.getCodeDescription().setValue("");
					//US 600 updating the model after the new codeList has been added to the grouped one.
					currentDetails = result.getCodeListDetailModel();
					resetAddCodeListScreen();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				addCodeListDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
			}
		
		});
	}
	
	private void resetAddCodeListScreen() {
		addCodeListDisplay.setCurrentPage(default_page_number);//Resetting to first page.
		addCodeListDisplay.getCodeDescription().setValue("");
		setModelCodesToView(false);
	}
	
	private void search(String searchText, int startIndex, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {

		final boolean isAscending = isAsc;
	
		MatContext.get().getCodeListService().search(searchText,
				startIndex, 9999 , 
				sortColumn, isAsc,defaultCodeList, filter, currentDetails.getCategory(), 
				new AsyncCallback<ManageCodeListSearchModel>() {
			@Override
			public void onSuccess(ManageCodeListSearchModel result) {
			
				//populate list box
				addCodeListDisplay.getCodeListInput().clear();
				addCodeListDisplay.getErrorMessageDisplay().clear();

				int i=0;
				for(CodeListSearchDTO dto : result.getData() ) {
					String label = dto.getName();
					String oid = dto.getOid();
					String value = dto.getId();
					//insertItem(text, value, title)
					addCodeListDisplay.getCodeListInput().insertItem(label,value, label+"-"+oid, i++, true);
				}			
			}
			@Override
			public void onFailure(Throwable caught) {
				addCodeListDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
	}
	
	private void populateCodeSystemOptions(String category,final boolean populate){
		listBoxProvider.getCodeSystemListForCategory(category, new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				detailDisplay.setCodeSystemOptions(result);
				if(populate){
					populateFields();
				}
			}
       });
		
	}
	
	public void clearMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		addCodeListDisplay.getErrorMessageDisplay().clear();
		addCodeListDisplay.getSuccessMessageDisplay().clear();
	}
	
	private void enableOrDisable(boolean editable) {
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(detailDisplay.asWidget(),editable);
//		ReadOnlyHelper.setReadOnlyForCurrentMeasure(addCodeListDisplay.asWidget(),editable);
	}
	
	private void enableOrDisableFields(){
		boolean canEditValueSet = currentDetails.isMyValueSet();
		boolean isValueSetDraft = currentDetails.isDraft();
		
		enableOrDisable(canEditValueSet);
		
		if(canEditValueSet){
			detailDisplay.enableValueSetWidgetsBasedOnDraft(isNewCodeList || isValueSetDraft);
			detailDisplay.getCategoryListBox().setEnabled(isNewCodeList);
			detailDisplay.getLastModifiedDate().setEnabled(!isValueSetDraft && !isNewCodeList);
			detailDisplay.setSaveCompleteButtonEnabled(!isNewCodeList && isValueSetDraft);
		}else{
			detailDisplay.getCategoryListBox().setEnabled(false);
			detailDisplay.getLastModifiedDate().setEnabled(false);
			detailDisplay.enableValueSetWidgetsBasedOnDraft(false);
			detailDisplay.setSaveCompleteButtonEnabled(false);
		}
	}
	
	private void setPanelContentAndHeading(Widget w, String heading){
		panel.setHeading(heading, heading);
		panel.setContent(w);
	}
	
	public void resetToFirstPage(){
		detailDisplay.getCodeListsSummaryWidget().setCurrentPage(default_page_number);
		if(currentDetails != null){
			editCodeList(currentDetails.getID());
		}
	}
}
