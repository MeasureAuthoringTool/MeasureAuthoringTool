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
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResultUpdate;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QDMAvailableValueSetPresenter  implements MatPresenter{

	private SearchDisplay searchDisplay;
	private int startIndex = 1;
	private String currentSortColumn = getSortKey(0);
	private boolean sortIsAscending = true;
	private boolean showdefaultCodeList = true;
	private String lastSearchText;
	private int lastStartIndex;
	private QDSCodeListSearchModel currentCodeListResults;
	MeasureServiceAsync measureService = MatContext.get().getMeasureService();
	ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	QualityDataSetDTO  modifyValueSetDTO;
	mat.client.clause.QDSAppliedListPresenter.SearchDisplay searchDisplay2;
	
	
	public static interface SearchDisplay extends mat.client.shared.search.SearchDisplay{
		public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption();
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		public void buildQDSDataTable(QDSCodeListSearchModel results);
		public HasClickHandlers getAddToMeasureButton();
		public void setAddToMeasureButtonEnabled(boolean visible);
		public Widget getDataTypeWidget();
		public ListBoxMVP getDataTypeInput();
		public CustomCheckBox getSpecificOccurrenceInput();
		public Button getApplyToMeasure();
		public void scrollToBottom();
		public FocusableWidget getMsgFocusWidget();
		public String getDataTypeValue();
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public void setDataTypeOptions(List<? extends HasListBox> texts);
		public String getDataTypeText();
		public ValueSetSearchFilterPanel getValueSetSearchFilterPanel();
		public void setEnabled(boolean enabled);
		public Button getCancel();
	}
	
	
	public QDMAvailableValueSetPresenter(SearchDisplay sDisplayArg , QualityDataSetDTO dataSetDTO, mat.client.clause.QDSAppliedListPresenter.SearchDisplay searchDisplay2){
		this.searchDisplay = sDisplayArg;
		this.modifyValueSetDTO = dataSetDTO;
		this.searchDisplay2 = searchDisplay2;
		this.appliedQDMList = (ArrayList<QualityDataSetDTO>) searchDisplay2.getAllAppliedQDMList();
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
				MatContext.get().getMeasureService().getMeasureXMLForAppliedQDM(MatContext.get().getCurrentMeasureId(),true, new AsyncCallback<ArrayList<QualityDataSetDTO>>(){

					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(ArrayList<QualityDataSetDTO> result) {
						appliedQDMList = result;
						modifyQDM();
					}
					
				});
				
			}
		});
		
		searchDisplay.getCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ModifyQDMDialogBox.dialogBox.hide();
				//This is to reload applied QDM List.
				reloadAppliedQDMList();
			}
		});
		
	}
	
	/***
	 * Method to find if selected Available value set is a valid modifiable selection. If yes, then call to updateAppliedQDMList method is made. 
	 * 
	 * */
	protected void modifyQDM() {
		CodeListSearchDTO modifyWithDTO = currentCodeListResults.getLastSelectedCodeList();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		if(modifyValueSetDTO!=null && modifyWithDTO!=null ){
			String dataType , dataTypeText;
			Boolean isSpecificOccurrence=false;
		
			if(modifyWithDTO.getCategoryDisplay().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
		    	   dataType = ConstantMessages.ATTRIBUTE;
		     }else if(modifyWithDTO.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_PERIOD)){
		    	   dataType = ConstantMessages.TIMING_ELEMENT;
		     }else if(modifyWithDTO.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_START_DATE)){
		    	   dataType = ConstantMessages.TIMING_ELEMENT;
		     }else if(modifyWithDTO.getName().equalsIgnoreCase(ConstantMessages.MEASUREMENT_END_DATE)){
		    	   dataType = ConstantMessages.TIMING_ELEMENT;
		     }else{
		    	   populateQDSDataType(modifyWithDTO.getCategoryCode());
		    	   dataType = searchDisplay.getDataTypeValue();
		     }
		     if(searchDisplay.getDataTypeText().equalsIgnoreCase("--Select--")){
		    	   dataTypeText = dataType;
		      }else{
		    	   dataTypeText = searchDisplay.getDataTypeText();
		       }
		       isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
		       
				
			if(modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE) || dataType.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)){
				if(dataType.equalsIgnoreCase(modifyValueSetDTO.getDataType())){
					updateAppliedQDMList(modifyWithDTO, modifyValueSetDTO,dataType,dataTypeText,isSpecificOccurrence);
					
				}else{
					searchDisplay.getErrorMessageDisplay().setMessage("Attribute can only be modified with Attribute.");
					setEnabled(true);
				}
			}else{
				updateAppliedQDMList(modifyWithDTO, modifyValueSetDTO,dataType,dataTypeText,isSpecificOccurrence);
			
			}
		}else{
			searchDisplay.getErrorMessageDisplay().setMessage("Please select atleast one applied QDM to modify.");
			setEnabled(true);
		
		}
		
	}
	
	/**
	 * This method is used to update QDM element selected for modification. All check's for attributes and non attributes , Occurrence and non occurences
	 * are done in this method. This method returns modified and ordered list of all applied QDM elements.This method also makes call to updateMeasureXML method.
	 * 
	 * **/
	
	private void updateAppliedQDMList(final CodeListSearchDTO codeListSearchDTO , final QualityDataSetDTO  qualityDataSetDTO, String dataType, String dataTypeText, Boolean isSpecificOccurrence){
		MatContext.get().getCodeListService().updateCodeListToMeasure(MatContext.get().getCurrentMeasureId(),dataType, codeListSearchDTO,qualityDataSetDTO, isSpecificOccurrence,appliedQDMList, new AsyncCallback<SaveUpdateCodeListResult>(){
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				setEnabled(true);
				
			}
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				if(result.getFailureReason()==7){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					setEnabled(true);
				}
				else{
					appliedQDMList = result.getAppliedQDMList();
					updateMeasureXML( result.getDataSetDTO() ,   qualityDataSetDTO);
				}
			}
		});
		
	}
	
	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under Population Node and Stratification Node, SupplementDataElements. It also removes attributes nodes if
	 * there is mismatch in data types of newly selected QDM and already applied QDM.
	 * 
	 * **/
	
	private void updateMeasureXML(QualityDataSetDTO qualityDataSetDTO2, QualityDataSetDTO qualityDataSetDTO){
		MatContext.get().getMeasureService().updateMeasureXML(qualityDataSetDTO2, qualityDataSetDTO, MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Void result) {
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage(MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
				setEnabled(true);
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
		searchDisplay2.buildCellList(appliedListModel);
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
				searchDisplay.buildQDSDataTable(QDSSearchResult);
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
		searchDisplay.getApplyToMeasure().setEnabled(false);
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
