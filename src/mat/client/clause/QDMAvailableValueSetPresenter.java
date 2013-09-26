package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class QDMAvailableValueSetPresenter  implements MatPresenter{

	private SearchDisplay searchDisplay;
	private int startIndex = 1;
	private String currentSortColumn = getSortKey(0);
	private boolean sortIsAscending = true;
	private boolean showdefaultCodeList = true;
	private String lastSearchText;
	private int lastStartIndex;
	private QDSCodeListSearchModel currentCodeListResults;
	private MatValueSet currentMatValueSet ;//To Do Sowmaya to Set this value set.
	MeasureServiceAsync measureService = MatContext.get().getMeasureService();
	ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	QualityDataSetDTO  modifyValueSetDTO;
	mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay;
	VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	
	public static interface SearchDisplay extends mat.client.shared.search.SearchDisplay{
		public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption();
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		public void buildQDSDataTable(QDSCodeListSearchModel results,Boolean isTableDisabled);
		public HasClickHandlers getAddToMeasureButton();
		public void setAddToMeasureButtonEnabled(boolean visible);
		public Widget getDataTypeWidget();
		public ListBoxMVP getDataTypeInput();
		public CustomCheckBox getSpecificOccurrenceInput();
		public Button getApplyToMeasure();
		public void scrollToBottom();
		//public FocusableWidget getMsgFocusWidget();
		public String getDataTypeValue();
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public void setDataTypeOptions(List<? extends HasListBox> texts);
		public String getDataTypeText();
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		public void setEnabled(boolean enabled);
		public Button getCancel();
		public String getDataTypeText(ListBoxMVP inputListBox);
		public DisclosurePanel getDisclosurePanel();
		public Button getPsuedoQDMToMeasure();
		/*public Button getPsuedoQDMCancel();*/
		public TextBox getUserDefinedInput();
		public ListBoxMVP getAllDataTypeInput();
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		public DisclosurePanel getDisclosurePanelCellTable();
		public SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		public ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		public Button getUserDefinedCancel();
		
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
		public void resetVSACValueSetWidget();
		public Button getCloseButton();
	}
	
	
	public QDMAvailableValueSetPresenter(SearchDisplay sDisplayArg , QualityDataSetDTO dataSetDTO, mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay){
		this.searchDisplay = sDisplayArg;
		this.modifyValueSetDTO = dataSetDTO;
		this.qdsAppliedListPresenterDisplay = qdsAppliedListPresenterDisplay;
		this.appliedQDMList = (ArrayList<QualityDataSetDTO>) qdsAppliedListPresenterDisplay.getAllAppliedQDMList();
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
		
		
		
		searchDisplay.getDisclosurePanel().addEventHandler(new DisclosureHandler()
	    {

	        public void onClose(DisclosureEvent event)
	        {
	        	searchDisplay.getUserDefinedInput().setText("");
	        	searchDisplay.getAllDataTypeInput().setItemSelected(0, true);
	        	searchDisplay.buildQDSDataTable(currentCodeListResults, true);
	        	displaySearch();
	        	searchDisplay.getDisclosurePanelCellTable().setOpen(true);
	        }

	        public void onOpen(DisclosureEvent event)
	        {
	        	populateAllDataType();
	        	searchDisplay.buildQDSDataTable(currentCodeListResults, true);
	            displaySearch();
	            searchDisplay.getDisclosurePanelCellTable().setOpen(false);
	        }
	    });
		
		
		searchDisplay.getDisclosurePanelCellTable().addEventHandler(new DisclosureHandler()
	    {

	        public void onClose(DisclosureEvent event)
	        {
	        	searchDisplay.getUserDefinedInput().setText("");
	        	//searchDisplay.getAllDataTypeInput().setItemSelected(0, true);
	        	searchDisplay.buildQDSDataTable(currentCodeListResults, true);
	        	displaySearch();
	        	searchDisplay.getDisclosurePanel().setOpen(true);
	        }

	        public void onOpen(DisclosureEvent event)
	        {
	        	/*populateAllDataType();*/
	        	searchDisplay.buildQDSDataTable(currentCodeListResults, true);
	            displaySearch();
	            searchDisplay.getDisclosurePanel().setOpen(false);
	        }
	    });
		
		
		/*searchDisplay.getPsuedoQDMCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getDisclosurePanel().setOpen(false);
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});*/
		
		
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
		
		searchDisplay.getPsuedoQDMToMeasure().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modifyQDM(true);
			}
		});
		
		searchDisplay.getApplyToMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				searchDisplay.scrollToBottom();
				MatContext.get().getMeasureService().getMeasureXMLForAppliedQDM(MatContext.get().getCurrentMeasureId(),true, new AsyncCallback<ArrayList<QualityDataSetDTO>>(){

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(ArrayList<QualityDataSetDTO> result) {
						appliedQDMList = result;
						currentMatValueSet = searchDisplay.getCurrentMatValueSet();
						modifyQDM(false);
					}
					
				});
				
			}
		});
		
		searchDisplay.getCloseButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ModifyQDMDialogBox.dialogBox.hide();
				//This is to reload applied QDM List.
				reloadAppliedQDMList();
			}
		});
		
		searchDisplay.getUserDefinedCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ModifyQDMDialogBox.dialogBox.hide();
				//This is to reload applied QDM List.
				reloadAppliedQDMList();
			}
		});
					
		searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(), searchDisplay.getVersionInput().getValue());				
			}
		});
		
	}
	
	private void searchValueSetInVsac(String oid, String version){				
		//OID validation.
		if (oid==null || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			searchDisplay.resetVSACValueSetWidget();
			return;
		}
				
		vsacapiService.getValueSetBasedOIDAndVersion(oid, new AsyncCallback<VsacApiResult>() {			
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.resetVSACValueSetWidget();
			}

			@Override
			public void onSuccess(VsacApiResult result) {
				if(result.isSuccess()) {
					searchDisplay.buildValueSetDetailsWidget(result.getVsacResponse());
					searchDisplay.getValueSetDetailsPanel().setVisible(true);
				}else{					
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getErrorMessageDisplay().setMessage(message);
					searchDisplay.resetVSACValueSetWidget();
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
	
	/***
	 * Method to find if selected Available value set is a valid modifiable selection. If yes, then call to updateAppliedQDMList method is made. 
	 * 
	 * */
	protected void modifyQDM(boolean isUserDefined) {		
		if(!isUserDefined){//Normal Available QDM Flow
			modifyValueSetQDM();
		}else{//Pseudo QDM Flow
			modifyQDMWithOutValueSet();		
		}
	}
	
	private void modifyValueSetQDM(){
		//Normal Available QDM Flow
		//CodeListSearchDTO modifyWithDTO = currentCodeListResults.getLastSelectedCodeList();
		MatValueSet modifyWithDTO = currentMatValueSet;
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		if(modifyValueSetDTO!=null && modifyWithDTO!=null ){
			String dataType;
			Boolean isSpecificOccurrence=false;
		
			if(modifyWithDTO.getCodeSystemName().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
		    	   dataType = ConstantMessages.ATTRIBUTE;
		     }else{
		    	   /*populateQDSDataType(modifyWithDTO.getCategoryCode());*/
		    	   dataType = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
		     }
		     isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
		     	
			 if(modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE) || dataType.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
				if(dataType.equalsIgnoreCase(modifyValueSetDTO.getDataType())){
					updateAppliedQDMList(modifyWithDTO,null, modifyValueSetDTO,dataType,isSpecificOccurrence,false);
				}else{
					if(ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataType)){
						searchDisplay.getErrorMessageDisplay().setMessage("A value set with a non-Attribute category must be used for this data element.");
					}else{
						searchDisplay.getErrorMessageDisplay().setMessage("A value set with an Attribute category must be used for this data element.");
					}
				setEnabled(true);
				}
			}else{
				updateAppliedQDMList(modifyWithDTO,null, modifyValueSetDTO,dataType,isSpecificOccurrence,false);
			
			}
		}else{
			searchDisplay.getErrorMessageDisplay().setMessage("Please select atleast one applied QDM to modify.");
			setEnabled(true);
		
		}
	}
	
	private void modifyQDMWithOutValueSet(){
		//Pseudo QDM Flow
		
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		if((searchDisplay.getUserDefinedInput().getText().trim().length()>0) && !searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()).equalsIgnoreCase(MatContext.PLEASE_SELECT))
		{
			
			CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
			modifyWithDTO.setName(searchDisplay.getUserDefinedInput().getText());
			String dataType = searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput());
			if(modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE) || dataType.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
				if(dataType.equalsIgnoreCase(modifyValueSetDTO.getDataType())){
					updateAppliedQDMList(null,modifyWithDTO, modifyValueSetDTO,dataType,false,true);
				}else{
					if(ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataType)){
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage("A value set with a non-Attribute category must be used for this data element.");
					}else{
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage("A value set with an Attribute category must be used for this data element.");
					}
				}
			}else{
				updateAppliedQDMList(null,modifyWithDTO, modifyValueSetDTO,dataType,false,true);
			}
		}else{
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage("Please enter Value Set name and select a data type associated with it.");
		}
		
	}
	
	/**
	 * This method is used to update QDM element selected for modification. All check's for attributes and non attributes , Occurrence and non occurences
	 * are done in this method. This method returns modified and ordered list of all applied QDM elements.This method also makes call to updateMeasureXML method.
	 * 
	 * **/
	
	private void updateAppliedQDMList(final MatValueSet matValueSet ,final CodeListSearchDTO codeListSearchDTO , final QualityDataSetDTO  qualityDataSetDTO, String dataType,  Boolean isSpecificOccurrence,final boolean isUSerDefined){
		MatContext.get().getCodeListService().updateCodeListToMeasure(MatContext.get().getCurrentMeasureId(),dataType, matValueSet, codeListSearchDTO,qualityDataSetDTO, isSpecificOccurrence,appliedQDMList,isUSerDefined, new AsyncCallback<SaveUpdateCodeListResult>(){
			@Override
			public void onFailure(Throwable caught) {
				if(!isUSerDefined){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					setEnabled(true);
				}else{
					searchDisplay.getErrorMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				
			}
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				if(result.getFailureReason()==7){
					if(!isUSerDefined){
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
						setEnabled(true);
					}else{
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					}
				}
				else{
					appliedQDMList = result.getAppliedQDMList();
					updateMeasureXML( result.getDataSetDTO() , qualityDataSetDTO,isUSerDefined);
				}
			}
		});
		
	}
	
	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under Population Node and Stratification Node, SupplementDataElements. It also removes attributes nodes if
	 * there is mismatch in data types of newly selected QDM and already applied QDM.
	 * 
	 * **/
	
	private void updateMeasureXML(QualityDataSetDTO qualityDataSetDTO2, QualityDataSetDTO qualityDataSetDTO,final boolean isUserDefined){
		MatContext.get().getMeasureService().updateMeasureXML(qualityDataSetDTO2, qualityDataSetDTO, MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if(!isUserDefined){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}else{
					searchDisplay.getErrorMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
			}

			@Override
			public void onSuccess(Void result) {
				if(!isUserDefined){
					searchDisplay.getApplyToMeasureSuccessMsg().setMessage(MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					setEnabled(true);
				}else{
					searchDisplay.getSuccessMessageUserDefinedPanel().setMessage(MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					searchDisplay.getUserDefinedInput().setText("");
					searchDisplay.getAllDataTypeInput().setSelectedIndex(0);
				}
			}
		});
		
	}
	
	/**
	 * This method is used to reload Applied QDM List.
	 * 
	 * */
	private void reloadAppliedQDMList(){
		QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
		appliedListModel.setAppliedQDMs(appliedQDMList);
		qdsAppliedListPresenterDisplay.buildCellList(appliedListModel);
	}
	
	/**
	 * This method is used in searching all available Value sets for pop up.
	 * */
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
	
	/**
	 * This method shows AvailableValueSet Widget in pop up.
	 * */
	private void displaySearch() {
		ModifyQDMDialogBox.showModifyDialogBox(searchDisplay.asWidget(),modifyValueSetDTO);
		populateDataTypesListBox();
		searchDisplay.resetVSACValueSetWidget();
		searchDisplay.clearVSACValueSetMessages();
	}
	
	private void populateQDSDataType(String category){
		MatContext.get().getListBoxCodeProvider().getQDSDataTypeForCategory(category, new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setDataTypeOptions(result);
			}
       });
		
	}
	
	
	private void populateAllDataType(){
		MatContext.get().getListBoxCodeProvider().getAllDataType(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setAllDataTypeOptions(result);
			}
       });
		
	}
	
	public Widget getWidget() {
		return searchDisplay.asWidget();
	}
	
	public String getSortKey(int columnIndex) {
		String[] sortKeys = new String[] { "name", "taxnomy", "category"};
		return sortKeys[columnIndex];
	}
	
	public void resetQDSFields(){
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getSearchString().setValue("");
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getApplyToMeasure().setEnabled(false);
		searchDisplay.getDataTypeInput().setEnabled(false);
		searchDisplay.getDisclosurePanel().setOpen(false);
		searchDisplay.getDisclosurePanelCellTable().setOpen(true);
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
	
	void loadCodeListData(){
		searchDisplay.getValueSetSearchFilterPanel().resetFilter();
		int filter = searchDisplay.getValueSetSearchFilterPanel().getDefaultFilter();
		search("", 1, searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
	}
	
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadCodeListData();
		
	}

	@Override
	public void beforeClosingDisplay() {
	
		
	}

}
