package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.PageSizeSelectionEvent;
import mat.client.shared.search.PageSizeSelectionEventHandler;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.server.util.UMLSSessionTicket;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class QDSCodeListSearchPresenter implements MatPresenter{

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	private int startIndex = 1;
	private String currentSortColumn = getSortKey(0);
	private boolean sortIsAscending = true;
	private boolean showdefaultCodeList = true;
	private String lastSearchText;
	private int lastStartIndex;
	private QDSCodeListSearchModel currentCodeListResults;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	boolean isUSerDefined = false;
	ArrayList<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();
	boolean isCheckForSDE=false;
	List<String> codeListString = new ArrayList<String>();
	VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	public static interface SearchDisplay extends mat.client.shared.search.SearchDisplay{
		public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption();
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		public void buildQDSDataTable(QDSCodeListSearchModel results,boolean isTableEnabled);
		public HasClickHandlers getAddToMeasureButton();
		public void setAddToMeasureButtonEnabled(boolean visible);
		public Widget getDataTypeWidget();
		public ListBoxMVP getDataTypeInput();
		public CustomCheckBox getSpecificOccurrenceInput();
		public Button getApplyToMeasure();
		public void scrollToBottom();
		//public FocusableWidget getMsgFocusWidget();
		public String getDataTypeValue(ListBoxMVP inputListBox);
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplay getErrorMessageDisplay();
		public void setDataTypeOptions(List<? extends HasListBox> texts);
		public String getDataTypeText(ListBoxMVP inputListBox);
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		public void setEnabled(boolean enabled);
		public DisclosurePanel getDisclosurePanel();
		public Button getPsuedoQDMToMeasure();
		public Button getPsuedoQDMCancel();
		public TextBox getUserDefinedInput();
		public ListBoxMVP getAllDataTypeInput();
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		public DisclosurePanel getDisclosurePanelCellTable();
		public SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		public ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		public TextBox getOIDInput();
		public DateBoxWithCalendar getVersionInput();
		public Button getRetrieveButton();
		public VerticalPanel getValueSetDetailsPanel();
		public ListBoxMVP getDataTypesListBox();
		public SuccessMessageDisplay getSuccessMessageDisplay();
		public void setDataTypesListBoxOptions(List<? extends HasListBox> texts);
		public void clearVSACValueSetMessages();
		public void buildValueSetDetailsWidget(ArrayList<MatValueSet> matValueSets);
		public Button getApplyToMeasureButton();
		public MatValueSet getCurrentMatValueSet();
	}

	@SuppressWarnings("deprecation")
	public QDSCodeListSearchPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;

		//TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanel().addEventHandler(new DisclosureHandler()
		{
			//TODO: Need to replace the DisclosureEvent with CloseEvent
			public void onClose(DisclosureEvent event)
			{
				searchDisplay.getUserDefinedInput().setText("");
				searchDisplay.getAllDataTypeInput().setItemSelected(0, true);
				searchDisplay.buildQDSDataTable(currentCodeListResults, true);
				displaySearch();
				searchDisplay.getDisclosurePanelCellTable().setOpen(true);
			}

			//TODO: Need to replace the DisclosureEvent with OpenEvent
			public void onOpen(DisclosureEvent event)
			{
				populateAllDataType();
				searchDisplay.buildQDSDataTable(currentCodeListResults, true);
				displaySearch();
				searchDisplay.getDisclosurePanelCellTable().setOpen(false);
			}
		});

		//TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanelCellTable().addEventHandler(new DisclosureHandler()
		{

			//TODO: Need to replace the DisclosureEvent with CloseEvent
			public void onClose(DisclosureEvent event)
			{
				searchDisplay.getUserDefinedInput().setText("");
				//searchDisplay.getAllDataTypeInput().setItemSelected(0, true);
				//searchDisplay.buildQDSDataTable(currentCodeListResults, true);
				displaySearch();
				searchDisplay.getDisclosurePanel().setOpen(true);
			}

			//TODO: Need to replace the DisclosureEvent with OpenEvent
			public void onOpen(DisclosureEvent event)
			{
				/*populateAllDataType();*/
				searchDisplay.buildQDSDataTable(currentCodeListResults, true);
				displaySearch();
				searchDisplay.getDisclosurePanel().setOpen(false);
			}
		});


		searchDisplay.getPsuedoQDMCancel().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getDisclosurePanel().setOpen(false);
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});

		searchDisplay.getUserDefinedInput().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});

		searchDisplay.getAllDataTypeInput().addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();

			}
		});
		searchDisplay.getPageSelectionTool().addPageSelectionHandler(new PageSelectionEventHandler() {
			@Override
			public void onPageSelection(PageSelectionEvent event) {
				int startIndex = searchDisplay.getPageSize() * (event.getPageNumber() - 1) + 1;
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				search(lastSearchText, startIndex,searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
			}
		});

		searchDisplay.getPageSizeSelectionTool().addPageSizeSelectionHandler(new PageSizeSelectionEventHandler() {
			@Override
			public void onPageSizeSelection(PageSizeSelectionEvent event) {
				searchDisplay.getSearchString().setValue("");
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				search(searchDisplay.getSearchString().getValue(), lastStartIndex,searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
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

		MatContext.get().getEventBus().addHandler(OnChangeOptionsEvent.TYPE, new OnChangeOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(OnChangeOptionsEvent event) {
				final CodeListSearchDTO codeList = currentCodeListResults.getSelectedCodeList();
				searchDisplay.scrollToBottom();
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage("");
				searchDisplay.getSpecificOccurrenceInput().setValue(false);//Unchecking the specific occurrence checkbox on change of radio options.
				if(codeList != null){
					String codeListCategory = codeList.getCategoryDisplay();
					if(codeListCategory.equalsIgnoreCase(ConstantMessages.ATTRIBUTE) || 
							codeListCategory.equalsIgnoreCase(ConstantMessages.MEASUREMENT_TIMING)){
						searchDisplay.getDataTypeInput().setEnabled(false);
						searchDisplay.getSpecificOccurrenceInput().setEnabled(false);
						searchDisplay.getApplyToMeasure().setEnabled(true);//enable only the ApplyToMeasure Button in these scenario
					}else{
						populateQDSDataType(codeList.getCategoryCode());
						searchDisplay.getDataTypeInput().setEnabled(true);//Enable both the datatype dropdown and specific occurence.
						searchDisplay.getSpecificOccurrenceInput().setEnabled(true);
						searchDisplay.getDataTypeInput().setFocus(true);
						searchDisplay.getApplyToMeasure().setEnabled(false);
					}
				}
			}
		});

		searchDisplay.getPsuedoQDMToMeasure().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isUSerDefined = true;
				getListOfAppliedQDMs(isUSerDefined);
			}
		});

		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				startIndex = 1;
				currentSortColumn = getSortKey(0);
				sortIsAscending = true;
				int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
				search(searchDisplay.getSearchString().getValue(),
						startIndex, searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
			}
		});

		searchDisplay.getAddToMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				searchDisplay.scrollToBottom();
				isUSerDefined = false;
				getListOfAppliedQDMs(isUSerDefined);
				
			}
		});
		
		searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				isUSerDefined = false;
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(), searchDisplay.getVersionInput().getValue());				
			}
		});
		
		searchDisplay.getApplyToMeasureButton().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				searchDisplay.scrollToBottom();
				isUSerDefined = false;
				getListOfAppliedQDMs(isUSerDefined);
			}
		});
	}
	
	private void searchValueSetInVsac(String oid, String version){				
		//OID validation.
		if (oid==null || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			return;
		}
				
		vsacapiService.getValueSetBasedOIDAndVersion(oid, new AsyncCallback<VsacApiResult>() {			
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
			}

			@Override
			public void onSuccess(VsacApiResult result) {
				if(result.isSuccess()) {
					searchDisplay.buildValueSetDetailsWidget(result.getVsacResponse());
					searchDisplay.getValueSetDetailsPanel().setVisible(true);
				}else{					
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getErrorMessageDisplay().setMessage(message);
				}
			}
		});	
	}
	
	private String convertMessage(int id) {
		String message;
		switch(id) {
			case VsacApiResult.UMLS_NOT_LOGGEDIN:
				message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
				break;
			case VsacApiResult.OID_REQUIRED:
				message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
				break;
			
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
	}
	
	private void getListOfAppliedQDMs(final boolean isUSerDefined){
		String measureId = MatContext.get().getCurrentMeasureId();
		if (measureId != null && measureId != "") {
			service.getMeasureXMLForAppliedQDM(measureId,true, new AsyncCallback<ArrayList<QualityDataSetDTO>>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}

				@Override
				public void onSuccess(ArrayList<QualityDataSetDTO> result) {
					appliedQDMList = result;
					addSelectedCodeListtoMeasure(isUSerDefined);
				}
			});

		}
	}

	private void populateAllDataType(){
		MatContext.get().getListBoxCodeProvider().getAllDataType(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setAllDataTypeOptions(result);
			}
		});

	}

	private void populateDataTypesListBox(){
		MatContext.get().getListBoxCodeProvider().getAllDataType(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.clearVSACValueSetMessages();
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setDataTypesListBoxOptions(result);
			}
		});

	}
	
	private void search(String searchText, int startIndex, final int pageSize,
			String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		lastSearchText = (!searchText.equals(null))? searchText.trim() : null;
		lastStartIndex = startIndex;
		showSearchingBusy(true);
		displaySearch();
		MatContext.get().getCodeListService().search(lastSearchText,
				startIndex, Integer.MAX_VALUE, 
				sortColumn, isAsc, defaultCodeList, filter,
				new AsyncCallback<ManageCodeListSearchModel>() {
			@Override
			public void onSuccess(ManageCodeListSearchModel result) {
				if(result.getData().isEmpty() && !lastSearchText.isEmpty()){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoCodeListsMessage());
				}else{
					resetQDSFields();
					searchDisplay.getSearchString().setValue(lastSearchText);
				}
				QDSCodeListSearchModel QDSSearchResult = new QDSCodeListSearchModel();
				QDSSearchResult.setData(result.getData());
				QDSSearchResult.setResultsTotal(result.getResultsTotal());


				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
				sru = null;
				searchDisplay.buildQDSDataTable(QDSSearchResult,true);
				currentCodeListResults = QDSSearchResult;
				displaySearch();
				searchDisplay.getErrorMessageDisplay().setFocus();
				showSearchingBusy(false);
			}
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage("Problem while performing a search");
				showSearchingBusy(false);
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

	void loadCodeListData(){
		//		int filter = searchDisplay.getValueSetSearchFilterPanel().getSelectedIndex();
		//reverting to default search filter when navigating to Clause Workspace 
		panel.clear();
		//getXMLForAppliedQDM(true);
		searchDisplay.getValueSetSearchFilterPanel().resetFilter();
		int filter = searchDisplay.getValueSetSearchFilterPanel().getDefaultFilter();
		search("", 1, searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
		//panel.add(searchDisplay.asWidget());
	}

	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		populateDataTypesListBox();
		searchDisplay.getOIDInput().setValue(StringUtils.EMPTY);
		searchDisplay.getVersionInput().setValue(StringUtils.EMPTY);
		searchDisplay.getValueSetDetailsPanel().setVisible(false);
		searchDisplay.clearVSACValueSetMessages();
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		//searchDisplay.setAddToMeasureButtonEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
	}

	private void addSelectedCodeListtoMeasure(boolean isUserDefinedQDM){
		if(!isUserDefinedQDM){
			addQDSWithValueSet();
		}else{
			addQDSWithOutValueSet();
		}
	}

	private void addQDSWithValueSet(){
		//clear the successMessage
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
			final String dataType;
			final  String dataTypeText;
			final boolean isSpecificOccurrence;
			
			dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());

			if(searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox()).equalsIgnoreCase("--Select--")){
				dataTypeText = dataType;
			}else{
				dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
			}
			isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
			String measureID = MatContext.get().getCurrentMeasureId();
			if(!dataType.isEmpty() && !dataType.equals("")){
				MatContext.get().getCodeListService().saveQDStoMeasure(measureID,dataType, searchDisplay.getCurrentMatValueSet(), isSpecificOccurrence,appliedQDMList, new AsyncCallback<SaveUpdateCodeListResult>(){
					@Override
					public void onSuccess(SaveUpdateCodeListResult result) {
						String message="";
						if(result.getXmlString() !=null)
							saveMeasureXML(result.getXmlString());
						searchDisplay.getSpecificOccurrenceInput().setValue(false);//OnSuccess() uncheck the specific occurrence and deselect the radio options 
						if(result.isSuccess()) {
							if(result.getOccurrenceMessage()!= null && !result.getOccurrenceMessage().equals("")){
								message = MatContext.get().getMessageDelegate().getQDMOcurrenceSuccessMessage(searchDisplay.getCurrentMatValueSet().getDisplayName(), dataTypeText, result.getOccurrenceMessage());
							}else{
								message = MatContext.get().getMessageDelegate().getQDMSuccessMessage(searchDisplay.getCurrentMatValueSet().getDisplayName(), dataTypeText);
							}
							MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(searchDisplay.getCurrentMatValueSet().getDisplayName()));
							searchDisplay.getApplyToMeasureSuccessMsg().setMessage(message);
							//searchDisplay.getMsgFocusWidget().setFocus(true);

						}
					}
					@Override
					public void onFailure(Throwable caught) {
						if(appliedQDMList.size()>0)
							appliedQDMList.removeAll(appliedQDMList);
						searchDisplay.getErrorMessageDisplay().setMessage("problem while saving the QDM to Measure");
					}
				});
			}
	}
	
	/*private void addQDSWithValueSet(){
		//clear the successMessage
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		final CodeListSearchDTO codeList = currentCodeListResults.getSelectedCodeList();
		if(codeList != null){
			final String dataType;
			final  String dataTypeText;
			final boolean isSpecificOccurrence;
			if(codeList.getCategoryDisplay().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
				dataType = ConstantMessages.ATTRIBUTE;
			}else if(codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_PERIOD)){
				dataType = ConstantMessages.TIMING_ELEMENT;
			}else if(codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_START_DATE)){
				dataType = ConstantMessages.TIMING_ELEMENT;
			}else if(codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_END_DATE)){
				dataType = ConstantMessages.TIMING_ELEMENT;
			}
			else{
				populateQDSDataType(codeList.getCategoryCode());
				dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypeInput());
			}

			if(searchDisplay.getDataTypeText(searchDisplay.getDataTypeInput()).equalsIgnoreCase("--Select--")){
				dataTypeText = dataType;
			}else{
				dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypeInput());
			}
			isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
			String measureID = MatContext.get().getCurrentMeasureId();
			if(!dataType.isEmpty() && !dataType.equals("")){
				MatContext.get().getCodeListService().addCodeListToMeasure(measureID,dataType, codeList, isSpecificOccurrence,appliedQDMList, new AsyncCallback<SaveUpdateCodeListResult>(){
					@Override
					public void onSuccess(SaveUpdateCodeListResult result) {
						String message="";
						if(result.getXmlString() !=null)
							saveMeasureXML(result.getXmlString());
						searchDisplay.getSpecificOccurrenceInput().setValue(false);//OnSuccess() uncheck the specific occurrence and deselect the radio options 
						if(result.isSuccess()) {
							if(result.getOccurrenceMessage()!= null && !result.getOccurrenceMessage().equals("")){
								message = MatContext.get().getMessageDelegate().getQDMOcurrenceSuccessMessage(codeList.getName(), dataTypeText, result.getOccurrenceMessage());
							}else{
								message = MatContext.get().getMessageDelegate().getQDMSuccessMessage(codeList.getName(), dataTypeText);
							}
							MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeList.getName()));
							searchDisplay.getApplyToMeasureSuccessMsg().setMessage(message);
							//searchDisplay.getMsgFocusWidget().setFocus(true);

						}
					}
					@Override
					public void onFailure(Throwable caught) {
						if(appliedQDMList.size()>0)
							appliedQDMList.removeAll(appliedQDMList);
						searchDisplay.getErrorMessageDisplay().setMessage("problem while saving the QDM to Measure");
					}
				});
			}

		}
	}
*/
	private void addQDSWithOutValueSet(){
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		if((searchDisplay.getUserDefinedInput().getText().trim().length()>0) && !searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
			MatContext.get().getCodeListService().saveUserDefinedQDStoMeasure(MatContext.get().getCurrentMeasureId(), searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()), searchDisplay.getUserDefinedInput().getText(), appliedQDMList, 
					new AsyncCallback<SaveUpdateCodeListResult>() {
				@Override
				public void onFailure(Throwable caught) {
					if(appliedQDMList.size()>0)
						appliedQDMList.removeAll(appliedQDMList);
					Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());				
				}
				@Override
				public void onSuccess(SaveUpdateCodeListResult result) {
					if(result.getXmlString() !=null){
						saveMeasureXML(result.getXmlString());
						String message = MatContext.get().getMessageDelegate().getQDMSuccessMessage(searchDisplay.getUserDefinedInput().getText(), searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()));
						searchDisplay.getSuccessMessageUserDefinedPanel().setMessage(message);
						searchDisplay.getUserDefinedInput().setText("");
						searchDisplay.getAllDataTypeInput().setSelectedIndex(0);
					}else if(result.getFailureReason()==7){
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					}
				}
			})	;
		}else{
			if(appliedQDMList.size()>0){
				appliedQDMList.removeAll(appliedQDMList);
			}
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getVALUE_SET_NAME_DATATYPE_REQD());

		}
	}

	private void saveMeasureXML(String qdmXMLString){
		final String nodeName ="qdm";
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setParentNode("/measure/elementLookUp");
		exportModal.setToReplaceNode("qdm");
		System.out.println("XML "+qdmXMLString);
		exportModal.setXml(qdmXMLString);

		service.appendAndSaveNode(exportModal,nodeName,
				new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Void result) {

			}
		});
	}

	private void populateQDSDataType(String category){
		MatContext.get().getListBoxCodeProvider().getQDSDataTypeForCategory(category, new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setDataTypeOptions(result);
			}
		});

	}


	public Widget getWidget() {
		return panel;
	}

	public String getSortKey(int columnIndex) {
		String[] sortKeys = new String[] { "name", "taxnomy", "category"};
		return sortKeys[columnIndex];
	}

	public void resetQDSFields(){
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getSearchString().setValue("");
		searchDisplay.getApplyToMeasure().setEnabled(true);
		searchDisplay.getDataTypeInput().setEnabled(false);
	}

	public void setEnabled(boolean enabled){
		searchDisplay.setEnabled(enabled);

		//determine which of the "Select Data Type" drop down, the "Specific Occurrence" check box, and the "Apply to Measure" button are to be enabled 
		boolean applyToMeasure = false;
		boolean specificOccurrence = false;
		boolean dataTypeInput = false;
		final CodeListSearchDTO codeList = currentCodeListResults == null ? null : currentCodeListResults.getSelectedCodeList();
		if(enabled && codeList != null){
			boolean dataTypeSelected = searchDisplay.getDataTypeInput().getSelectedIndex()>0;
			if(!codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_PERIOD) && 
					!codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_START_DATE) &&
					!codeList.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_END_DATE) &&
					!codeList.getCategoryDisplay().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
				//(1) can have data type and data type is selected
				// dti: true soc: true atm: true
				//(2) can have data type and data type is not selected
				// dti: true soc: true atm: false
				dataTypeInput = true;
				specificOccurrence = true;
				if(dataTypeSelected){
					applyToMeasure = true;
				}
			}else{
				//(3) cannot have data type
				// dti: false soc: false am: true
				applyToMeasure = true;
			}
		}

		//apply to measure
		((Button)searchDisplay.getAddToMeasureButton()).setEnabled(applyToMeasure);		
		//specific occurrence
		searchDisplay.getSpecificOccurrenceInput().setEnabled(specificOccurrence);
		//select data type
		searchDisplay.getDataTypeInput().setEnabled(dataTypeInput);
	}
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadCodeListData();

	}

	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		searchDisplay.getDisclosurePanel().setOpen(false);
		searchDisplay.getDisclosurePanelCellTable().setOpen(true);

	}
}
