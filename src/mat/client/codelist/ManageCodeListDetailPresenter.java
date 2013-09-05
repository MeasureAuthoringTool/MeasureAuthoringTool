package mat.client.codelist;

import java.util.ArrayList;
import java.util.List;

import mat.client.admin.service.SaveUpdateUserResult;
import mat.client.codelist.events.AddCodeToCodeListEvent;
import mat.client.codelist.events.CancelAddCodeEvent;
import mat.client.codelist.events.CancelEditCodeListEvent;
import mat.client.codelist.events.CreateNewCodeListEvent;
import mat.client.codelist.events.CreateNewGroupedCodeListEvent;
import mat.client.codelist.events.ExternalViewerEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.TextAreaWithMaxLength;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.SelectAllEvent;
import mat.client.shared.search.SelectAllEventHandler;
import mat.model.Code;
import mat.shared.ListObjectModelValidator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class ManageCodeListDetailPresenter extends BaseDetailPresenter {
	
	private int default_page_number = 1;
	public static interface CodeListDetailDisplay extends BaseDisplay {
		public HasClickHandlers getCreateNewButton();
		public HasClickHandlers getCreateNewGroupedButton();
		public HasClickHandlers getAddCodeButton();
		public String getTitle();
		public String getCategoryValue();
		public ListBoxMVP getCategoryListBox();
		public void DisableAnchors();
		public void EnableAnchors(ManageCodesSummaryModel codes,int pageCount,int total,int currentPage);
		public void setAddCodeButtonEnabled(boolean enabled);
		public CodesSummaryWidget getCodesSummary();
	}
	
	public static interface CodesSummaryDisplay {
		public void buildSummaryDataTable(ManageCodesSummaryModel codes,int totalPagesCount,int total,int currentPage);
		public HasPageSelectionHandler getPageSelectionTool();
		public int getPageSize();
		public void setPageSize(int pageNumber);
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);	
	}
	
	public static interface AddCodeDisplay extends BaseAddDisplay<Code> {
		public void setCodeListDetailModel(ManageCodeListDetailModel currentDetails);
		public HasValue<String> getCodeName();
		public HasValue<String> getCodeDescription();
		public void buildUploadForm(ManageCodeListDetailModel currentDetails);
		public void buildImportTab();
		public HasClickHandlers getExternalViewerLink();
		public SuccessMessageDisplayInterface getUploadSuccessMessageDisplay();
		public ErrorMessageDisplayInterface getUploadErrorMessageDisplay();
		public int getCurrentPage();
		public void setCurrentPage(int pageNumber);
	}

	public static interface ExternalLinkDisclaimerDisplay{
		public Widget asWidget();
		public HasClickHandlers getYesButton();
		public HasClickHandlers getNoButton();
	}
	public static interface AddQDSDisplay {
		public Widget asWidget();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void setCodeListDetailModel(ManageCodeListDetailModel currentDetails);
		public HasValue<String> getDataType();
		public void setDataType(int value);
		public String getQDSDataTypeValue();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		/*USTod*/
		public void setTitle(String title);
		void setDataTypeOptions(List<? extends HasListBox> texts);
		
	}
	
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	private CodeListDetailDisplay detailDisplay;
	private AddCodeDisplay addCodeDisplay;
	private AddQDSDisplay addQDSDisplay;
	private ManageCodeListDetailModel currentDetails;
	private ManageCodesModel currentCodes;
	private ListBoxCodeProvider listBoxProvider;
	private ExternalLinkDisclaimerDisplay disclaimerDisplay;
	
	
	protected BaseDisplay getDetailDisplay() {
		return detailDisplay;
	}
	public ManageCodeListDetailPresenter(CodeListDetailDisplay dDisplayArg, AddCodeDisplay addCodeArg,ExternalLinkDisclaimerDisplay disclaimerDsp,AddQDSDisplay addQDSArg, ListBoxCodeProvider lpArg) {
		super(dDisplayArg, lpArg);
		
		this.detailDisplay = dDisplayArg;
		this.addCodeDisplay = addCodeArg;
		this.addQDSDisplay = addQDSArg;
		this.disclaimerDisplay = disclaimerDsp;
		listBoxProvider = lpArg;
		addCodeDisplay.buildImportTab();
		addCodeDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int pageNumber = event.getPageNumber();
				if(pageNumber == -1){ // if next button clicked
					if(addCodeDisplay.getCurrentPage() == currentDetails.getCodesPageCount()){
						pageNumber = addCodeDisplay.getCurrentPage();
					}else{
						pageNumber = addCodeDisplay.getCurrentPage() + 1;
					}
				}else if(pageNumber == 0){ // if first button clicked
					pageNumber = 1;
				}else if(pageNumber == -9){ // if prev button clicked
					if(addCodeDisplay.getCurrentPage() == 1){
						pageNumber = addCodeDisplay.getCurrentPage();
					}else{
						pageNumber = addCodeDisplay.getCurrentPage() - 1;
					}
				}
				addCodeDisplay.setCurrentPage(pageNumber);//Setting it to the display.
				displayCodesForTheSelectedPage(currentDetails.getID());
			}

		
		});
		
		
		detailDisplay.getCategory().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String category = event.getValue();
				boolean populate = false;
				PopulateCodeSystemOptions(category,populate);
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

		
		detailDisplay.getCategoryListBox().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				detailDisplay.getCodeSystemListBox().setFocus(true);
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
				Element element = detailDisplay.getLastModifiedDate().calendar.getElement();
				if(detailDisplay.getLastModifiedDate().isEnabled()){
					element.focus();
				}
				element.setAttribute("aria-role", "command");
				element.setAttribute("aria-labelledby", "LiveRegion");
				element.setAttribute("aria-live", "assertive");
				element.setAttribute("aria-atomic", "true");
				element.setAttribute("aria-relevant", "all");
				element.setAttribute("role", "alert");
			}
		});
		
		detailDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailDisplay.getCodesSummary().setCurrentPage(default_page_number);//Need to reset to first Page when the cancel button has been clicked.
				MatContext.get().getEventBus().fireEvent(new CancelEditCodeListEvent());
			}
		});
		
       detailDisplay.getCreateNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getEventBus().fireEvent(new CreateNewCodeListEvent());
			}
		});
		
		detailDisplay.getCreateNewGroupedButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getEventBus().fireEvent(new CreateNewGroupedCodeListEvent());
			}
		});
		detailDisplay.getAddCodeButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				 addCodeDisplay.setCurrentPage(default_page_number);
				 MatContext.get().getEventBus().fireEvent(new AddCodeToCodeListEvent());
				
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
		
		if(detailDisplay.getCodesSummary().getPageSelectionTool() != null){
			detailDisplay.getCodesSummary().getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
				
				@Override
				public void onPageSelection(PageSelectionEvent event) {
					int pageNumber = event.getPageNumber();
					if(pageNumber == -1){ // if next button clicked
						if(detailDisplay.getCodesSummary().getCurrentPage() == currentDetails.getCodesPageCount()){
							pageNumber = detailDisplay.getCodesSummary().getCurrentPage();
						}else{
							pageNumber = detailDisplay.getCodesSummary().getCurrentPage() + 1;
						}
					}else if(pageNumber == 0){ // if first button clicked
						pageNumber = 1;
					}else if(pageNumber == -9){ 
						if(detailDisplay.getCodesSummary().getCurrentPage() == 1){
							pageNumber = detailDisplay.getCodesSummary().getCurrentPage();
						}else{
							pageNumber = detailDisplay.getCodesSummary().getCurrentPage() - 1;
						}
					}
					detailDisplay.getCodesSummary().setCurrentPage(pageNumber);
					currentCodes.setCurrentPage(pageNumber);
					displayCodesSummary(currentDetails.getID());
				}
			});
		}
		addCodeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				addCode();
			}
		});
		
		addCodeDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCodeDisplay.getSuccessMessageDisplay().clear();
				addCodeDisplay.getErrorMessageDisplay().clear();
				addCodeDisplay.getCodeName().setValue("");
				addCodeDisplay.getCodeDescription().setValue("");
			}
		});
		addCodeDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                clearMessages();
				removeSelectedCodesFromCodeList();
			}
		});
		addCodeDisplay.getSelectAllTool().addSelectAllHandler(new SelectAllEventHandler() {
			
			@Override
			public void onSelectAll(SelectAllEvent event) {
				 setCodesOnView(event.isChecked());//This line reconstruct the table with checked/NotChecked
			}
		});
		
		addCodeDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				MatContext.get().getEventBus().fireEvent(new CancelAddCodeEvent());
				resetToFirstPage();
			}
		});
		
		
		addCodeDisplay.getExternalViewerLink().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getEventBus().fireEvent(new ExternalViewerEvent());
				
			}
			
		});
		
		addQDSDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currentDetails.setExistingCodeList(true);
				currentDetails.setDataType(addQDSDisplay.getQDSDataTypeValue());
			}
		});
		
		addQDSDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().getEventBus().fireEvent(new CancelAddCodeEvent());
			}
		});
		
		disclaimerDisplay.getYesButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://www.microsoft.com/downloads/en/details.aspx?familyid=1cd6acf9-ce06-4e1c-8dcf-f33f669dbc3a", "_blank", "");
				
			}
			
		});
		
		disclaimerDisplay.getNoButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				 MatContext.get().getEventBus().fireEvent(new AddCodeToCodeListEvent());
			}
			
		});
		
		detailDisplay.getLastModifiedDate().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				detailDisplay.asWidget().getElement().setAttribute("role", "alert");
				detailDisplay.setSaveCompleteButtonEnabled(currentDetails.isMyValueSet());
			}
		});
	}
	
	private void displayCodesForTheSelectedPage(String codeListId) {
		int pageNumber = addCodeDisplay.getCurrentPage();
		int pageSize =   addCodeDisplay.getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		//TODO:- do we need to pass pageSize? Since pageSize is always 50. Left it as it if they want to change their mind in future.
		MatContext.get().getCodeListService().getCodes(codeListId,startIndex,pageSize, new AsyncCallback<List<Code>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(List<Code> codesResults) {
				currentCodes = new ManageCodesModel(codesResults, false);
				currentCodes.setPageSize(addCodeDisplay.getPageSize());
				currentDetails.setCodes(codesResults);//Need to set this to keep in sync with the current Page codesList
				addCodeDisplay.buildDataTable(currentCodes,false,currentDetails.getTotalCodes(),currentDetails.getCodesPageCount(),addCodeDisplay.getCurrentPage());
			}
		});
	}
	
	private void displayCodesSummary(String codeListId){
		int pageNumber = detailDisplay.getCodesSummary().getCurrentPage();
		int pageSize = detailDisplay.getCodesSummary().getPageSize();
		int startIndex = pageSize * (pageNumber - 1);
		MatContext.get().getCodeListService().getCodes(codeListId,startIndex,pageSize, new AsyncCallback<List<Code>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(List<Code> codesResults) {
				ManageCodesSummaryModel codesSummaryModel = new ManageCodesSummaryModel();
				codesSummaryModel.setData(codesResults);
				detailDisplay.EnableAnchors(codesSummaryModel,currentDetails.getCodesPageCount(),currentDetails.getTotalCodes(),detailDisplay.getCodesSummary().getCurrentPage());
			}
			
		});
	}
	
	private void addCode() {
		final Code code = createCode();
		if(isValidCode(code)) {
			//Need to check codesExists against the entire list of codes not just  the first 50.
			MatContext.get().getCodeListService().isCodeAlreadyExists(currentDetails.getID(), code, new AsyncCallback<Boolean>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(Boolean isCodeAlreadyExists) {
					if(!isCodeAlreadyExists){
						currentDetails.setCode(code);
						currentDetails.setExistingCodeList(true);
						addCodetoCodeList(currentDetails);
					}else{
						addCodeDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getCodeAlreadyExistsForMessage(currentDetails.getName()));
					}
						
				}
			});
		}
	}
	
	private boolean isValidCode(Code code) {
		ListObjectModelValidator.CodeModelValidator validator = new ListObjectModelValidator.CodeModelValidator();
		List<String> message = validator.validate(code);
		boolean valid = message.size() == 0;
		if(!valid) {
			addCodeDisplay.getErrorMessageDisplay().setMessages(message);
		}
		else {
			addCodeDisplay.getErrorMessageDisplay().clear();
		}
		return valid;	
	}
	private Code createCode(){
		Code code = new Code();
		code.setCode(addCodeDisplay.getCodeName().getValue());
		code.setDescription(addCodeDisplay.getCodeDescription().getValue());
		return code;
	}
	
	private void update(final int source) {
		clearMessages();
		updateModelDetailsFromView();
		if(isValid(currentDetails)) {
			MatContext.get().getCodeListService().saveorUpdateCodeList(currentDetails, new AsyncCallback<SaveUpdateCodeListResult>() {
				
				@Override
				public void onSuccess(SaveUpdateCodeListResult result) {
					if(result.isSuccess()) {
						detailDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getValueSetChangedSavedMessage(source));
						ManageCodesSummaryModel codesSummaryModel = new ManageCodesSummaryModel();
						codesSummaryModel.setData(currentDetails.getCodes());
						detailDisplay.EnableAnchors(codesSummaryModel,currentDetails.getCodesPageCount(),currentDetails.getTotalCodes(),detailDisplay.getCodesSummary().getCurrentPage());//Always set only 10 codes results by default
						currentDetails.setExistingCodeList(true);
						currentDetails.setID(result.getId());
						String lmd = result.getLastModifiedDate();
						/*
						 * on initial complete, this field gets autopopulated on the server side, 
						 * retrieve and set it
						 */
						if(lmd!=null){
							currentDetails.setLastModifiedDate(lmd);
							if(source == MessageDelegate.COMPLETE)
								detailDisplay.getLastModifiedDate().setEnabled(true);
						}
						
						populateFields();
						
						if(source == MessageDelegate.DRAFT){
							detailDisplay.setSaveCompleteButtonEnabled(true);
						}
					}
					else {
						String message = null;
						switch(result.getFailureReason()) {
							case SaveUpdateUserResult.ID_NOT_UNIQUE:
								message = MatContext.get().getMessageDelegate().getCodeListAlreadyExistsMessage(currentDetails.getName());
								break;
							case SaveUpdateCodeListResult.OID_NOT_UNIQUE:
								message = MatContext.get().getMessageDelegate().getOIDInUseMessage();
								break;
							case SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION:
								message = MatContext.get().getMessageDelegate().getServerSideValidationMessage();
								break;
							case SaveUpdateCodeListResult.INVALID_LAST_MODIFIED_DATE:
								message = MatContext.get().getMessageDelegate().getInvalidLastModifiedDateMessage();
								break;
							case SaveUpdateCodeListResult.LAST_MODIFIED_DATE_DUPLICATE:
								message = MatContext.get().getMessageDelegate().getLastModifiedDateNotUniqueMessage();
								break;
							default:
								message =  MatContext.get().getMessageDelegate().getUnknownCodeMessage(result.getFailureReason());
						}
						detailDisplay.getErrorMessageDisplay().setMessage(message);
					}
					detailDisplay.getOtherSpecify().getElement().setAttribute("role","");
					detailDisplay.getStewardLabel().getElement().setAttribute("role", "");
				}
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
			});
		}
	}
	
	
	public Widget getWidget(String heading) {
		/*USTod*/
		setPanelContentAndHeading(detailDisplay.asWidget(), heading);
		return panel;
//		return detailDisplay.asWidget();
	}

	private void setCodesOnView(boolean value) {
		//Collections.sort(currentDetails.getCodes(), new Code.Comparator());
		if(currentDetails.getCodes()!= null){
			currentCodes = new ManageCodesModel(currentDetails.getCodes(),value);
			currentCodes.setPageSize(addCodeDisplay.getPageSize());
			addCodeDisplay.buildDataTable(currentCodes,value,currentDetails.getTotalCodes(),currentDetails.getCodesPageCount(),addCodeDisplay.getCurrentPage());
		}
		ManageCodesSummaryModel codesSummaryModel = new ManageCodesSummaryModel();
		codesSummaryModel.setData(currentDetails.getCodes());
		detailDisplay.EnableAnchors(codesSummaryModel,currentDetails.getCodesPageCount(),currentDetails.getTotalCodes(),detailDisplay.getCodesSummary().getCurrentPage());
		
		enableOrDisableFields();
	}
	
	public Widget getAddCodesToCodeList(){
		/*USTod*/
//		addCodeDisplay.setTitle("Manage codes for "+ currentDetails.getName());
		
		addCodeDisplay.setReturnToLink("Return to "+ currentDetails.getName());
		addCodeDisplay.getUploadSuccessMessageDisplay().clear();
		addCodeDisplay.getUploadErrorMessageDisplay().clear();
		clearCodesFields();
		addCodeDisplay.setCodeListDetailModel(currentDetails);
		setCodesOnView(false);
		addCodeDisplay.buildUploadForm(currentDetails);
		
		/*USTod*/
		//OVERFLOW
		setPanelContentAndHeading(addCodeDisplay.asWidget(), CodeListController.MY_VALUE_SETS_VALUE_SET_MANAGE+" for "+currentDetails.getName());
		//return addCodeDisplay.asWidget();
		addCodeDisplay.getSuccessMessageDisplay().setFocus();
		return panel;
	}
	
	public Widget getExternalLinkDisclaimer(){
	    return disclaimerDisplay.asWidget();
	}
	
	public Widget refreshUploadedCodes(){
		/*USTod*/
//		addCodeDisplay.setTitle("Manage codes for "+ currentDetails.getName());
		
		addCodeDisplay.setReturnToLink("Return to "+ currentDetails.getName());
		addCodeDisplay.getUploadErrorMessageDisplay().clear();
		clearCodesFields();
		MatContext.get().getCodeListService().getCodeList(currentDetails.getID(), new AsyncCallback<ManageCodeListDetailModel>() {
			@Override
			public void onSuccess(ManageCodeListDetailModel result) {
				currentDetails = result;
				addCodeDisplay.setCodeListDetailModel(currentDetails);
				setCodesOnView(false);
				addCodeDisplay.buildUploadForm(currentDetails);
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		/*USTod*/
		//OVERFLOW
		String heading = CodeListController.MY_VALUE_SETS_VALUE_SET_MANAGE+" for "+currentDetails.getName();
		setPanelContentAndHeading(addCodeDisplay.asWidget(), heading);
		//return addCodeDisplay.asWidget();
		return panel;
	}
	
	private void removeSelectedCodesFromCodeList(){
		List<Code> selectedCodes = currentCodes.getSelectedCodes();
		if(!selectedCodes.isEmpty()){
			MatContext.get().getCodeListService().deleteCodes(currentDetails.getID(), selectedCodes, new AsyncCallback<ManageCodeListDetailModel>() {
	
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
	
				@Override
				public void onSuccess(ManageCodeListDetailModel result) {
					currentDetails = result;
					int pageCount = currentDetails.getCodesPageCount();
					if(pageCount < addCodeDisplay.getCurrentPage()){
					    	addCodeDisplay.setCurrentPage(pageCount);
					}
					displayCodesForTheSelectedPage(currentDetails.getID());
					addCodeDisplay.getSuccessMessageDisplay().setMessage("Selected Code(s) removed successfully");
				}
			});
		}
	}
	
	private void clearCodesFields() {
		addCodeDisplay.getCodeName().setValue("");
		addCodeDisplay.getCodeDescription().setValue("");
	}
	
	public Widget getAddQDSDisplay(){
		/*USTod*/
		//addQDSDisplay.setTitle("Add QDM to Value Set");
		
		addQDSDisplay.getSuccessMessageDisplay().clear();
		clearQDSFields();
		addQDSDisplay.setCodeListDetailModel(currentDetails);
		String category = currentDetails.getCategory();
		populateQDSDataType(category);
		return addQDSDisplay.asWidget();
	}	
	
	private void clearQDSFields(){
		addQDSDisplay.getDataType().setValue("");
	}
	
	private void populateQDSDataType(String category){
		listBoxProvider.getQDSDataTypeForCategory(category, new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(
					List<? extends HasListBox> result) {
				addQDSDisplay.setDataTypeOptions(result);
				
			}
       });
		
	}
	
	private void updateModelDetailsFromView() {
		currentDetails.setName(detailDisplay.getName().getValue());
		
		//US 413
		//The following code sets the value for Steward key, value and the other value if the value is "Other"				
		currentDetails.setSteward(detailDisplay.getOrganisation().getValue());
		currentDetails.setStewardValue(detailDisplay.getStewardValue());
		currentDetails.setStewardOther(detailDisplay.getStewardOtherValue());
		currentDetails.setCategory(detailDisplay.getCategoryValue());
		currentDetails.setCodeSystem(detailDisplay.getCodeSystemValue());
		currentDetails.setCodeSystemVersion(detailDisplay.getCodeSystemVersion().getValue());
		currentDetails.setRationale(detailDisplay.getRationale().getValue());
		currentDetails.setComments(detailDisplay.getComments().getValue());
		currentDetails.setOid(detailDisplay.getOid().getValue());
		/*US537*/
		currentDetails.setLastModifiedDate(detailDisplay.getLastModifiedDate().getValue());
	}
	
	private void populateFields(){
		detailDisplay.getName().setValue(currentDetails.getName());
		
		//US 413. Populate the Steward list box selection and the Steward Other value if exists.		
		detailDisplay.getOrganisation().setValue(currentDetails.getSteward());
		String stewardOther = currentDetails.getStewardOther();
		if(stewardOther != null && !stewardOther.equalsIgnoreCase("")){
			detailDisplay.showOtherTextBox();			
			detailDisplay.getStewardOther().setValue(currentDetails.getStewardOther());
			
		}else{
			detailDisplay.hideOtherTextBox();
		}
		
		detailDisplay.getCategory().setValue(currentDetails.getCategory());
		
		detailDisplay.getCodeSystem().setValue(currentDetails.getCodeSystem());
		detailDisplay.getCodeSystemVersion().setValue(currentDetails.getCodeSystemVersion());
		detailDisplay.getRationale().setValue(currentDetails.getRationale());
		detailDisplay.getComments().setValue(currentDetails.getComments());
		
		//US210 prefixing the oid widget title with the oid itself so it can be viewed by a user
		String oid = currentDetails.getOid();
		detailDisplay.getOid().setValue(oid);
		String oidTitle = oid == null ? detailDisplay.getOidTitle() : oid+" - "+detailDisplay.getOidTitle();
		((TextAreaWithMaxLength)detailDisplay.getOid()).setTitle(oidTitle);
		
		
		enableOrDisableFields();
		detailDisplay.getLastModifiedDate().setValue(currentDetails.getLastModifiedDate());
	}
	
	private void PopulateCodeSystemOptions(String category,final boolean populate){
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
	
	private void addCodetoCodeList(ManageCodeListDetailModel currentCodeList){
		final Code newCode = currentCodeList.getCode();
		MatContext.get().getCodeListService().saveorUpdateCodeList(currentCodeList, new AsyncCallback<SaveUpdateCodeListResult>() {
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				addCodeDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getCodeListAddedMessage());
			     addCodeDisplay.getCodeName().setValue("");
			     addCodeDisplay.getCodeDescription().setValue("");
			     newCode.setId(result.getCodeId());
			     //US 600 , updating the client model to reflect with the new codes.
			     currentDetails = result.getCodeListDetailModel();
			     setCodesOnView(false);
			}
			@Override
			public void onFailure(Throwable caught) {
				addCodeDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getIdentityMessage(caught.getLocalizedMessage()));
			}
		});
		currentCodeList.setCode(null);
	}


	
	public void createNewCodeList() {
			
		currentDetails = new ManageCodeListDetailModel();
		currentDetails.setIsGrouped(false);
		currentDetails.setMeasureId(MatContext.get().getCurrentMeasureId());
		currentCodes = new ManageCodesModel(new ArrayList<Code>(),false);
		clearAllFields();
		isNewCodeList = true;
		populateFields();
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		//detailDisplay.setTitle("Create New Value Set");
		detailDisplay.DisableAnchors();
		
		setPanelContentAndHeading(detailDisplay.asWidget(), CodeListController.MY_VALUE_SETS_VALUE_SET_CREATE);
	}
	
	private void clearAllFields() {
		detailDisplay.getName().setValue("");
		detailDisplay.getOrganisation().setValue("");
		detailDisplay.getStewardOther().setValue("");
		detailDisplay.getCategory().setValue("");
		detailDisplay.getCodeSystem().setValue("");
		detailDisplay.getCodeSystemVersion().setValue("");
		detailDisplay.getOid().setValue("");
		detailDisplay.getRationale().setValue("");
		detailDisplay.getComments().setValue("");
		
	}

	public void editCodeList(String key) {
		/*USTod*/
		//detailDisplay.setTitle("Update a Value Set");
		isNewCodeList = false;
		MatContext.get().getCodeListService().getCodeList(key, new AsyncCallback<ManageCodeListDetailModel>() {
			@Override
			public void onSuccess(ManageCodeListDetailModel result) {
				currentDetails = result;
				detailDisplay.getErrorMessageDisplay().clear();
				detailDisplay.getSuccessMessageDisplay().clear();
				String category = currentDetails.getCategory();
				boolean populate = true;
				PopulateCodeSystemOptions(category,populate);
				setCodesOnView(false);
				/*USTod*/
				setPanelContentAndHeading(detailDisplay.asWidget(), CodeListController.MY_VALUE_SETS_VALUE_SET_UPDATE);
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	
	public Widget getWidgetWithHeading(Widget widget, String heading) {
		FlowPanel vPanel = new FlowPanel();
		//Label h = new Label(heading);
//		h.addStyleName("myAccountHeader");
//		h.addStyleName("leftAligned");
//		vPanel.add(h);
		vPanel.add(widget);
		vPanel.addStyleName("myAccountPanel");
		widget.addStyleName("myAccountPanelContent");
		return vPanel;
	}
	
	@Override
	protected List<String> buildValidationMessages(
			ManageCodeListDetailModel model) {
		List<String> messages = super.buildValidationMessages(model);
		ListObjectModelValidator validator = new ListObjectModelValidator();
		for(String message : validator.validatecodeListonlyFields(model)){
			messages.add(message);
		}
		return messages;
	}

	public void clearMessages() {
		detailDisplay.getErrorMessageDisplay().clear();
		detailDisplay.getSuccessMessageDisplay().clear();
		addCodeDisplay.getErrorMessageDisplay().clear();
		addCodeDisplay.getSuccessMessageDisplay().clear();
		addCodeDisplay.getUploadSuccessMessageDisplay().clear();
		addCodeDisplay.getUploadErrorMessageDisplay().clear();
	}
	
	private void enableOrDisable(boolean editable) {
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(detailDisplay.asWidget(),editable);
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(addCodeDisplay.asWidget(),editable);
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
	
	private void setHeading(String heading){
		panel.setHeading(heading, heading);
	}
	
	/*USTod*/
	private void setPanelContentAndHeading(Widget w, String heading){
		setHeading(heading);
		panel.setContent(w);
	}
	
	public void resetToFirstPage(){
		detailDisplay.getCodesSummary().setCurrentPage(default_page_number);
		if(currentDetails != null){
			editCodeList(currentDetails.getID());
		}
	}
	
}
