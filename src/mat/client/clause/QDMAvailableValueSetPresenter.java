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
import com.google.gwt.user.client.Window;
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
		
	}
	
	
	public QDMAvailableValueSetPresenter(SearchDisplay sDisplayArg , QualityDataSetDTO dataSetDTO, ArrayList<QualityDataSetDTO> allQdsList){
		this.searchDisplay = sDisplayArg;
		this.modifyValueSetDTO = dataSetDTO;
		this.appliedQDMList = allQdsList;
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
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ArrayList<QualityDataSetDTO> result) {
						appliedQDMList = result;
						modifyQDM();
					}
					
				});
				
			}
		});
	}
	
	protected void modifyQDM() {
		CodeListSearchDTO modifyWithDTO = currentCodeListResults.getLastSelectedCodeList();
		if(modifyValueSetDTO!=null){
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
					//Window.alert("Valid Modification");
					updateAppliedQDMList(modifyWithDTO, modifyValueSetDTO,dataType,dataTypeText,isSpecificOccurrence);
					
				}else{
					Window.alert("Attribute Can only be modified with Attribute");
					ModifyQDMDialogBox.dialogBox.hide();
				}
			}else{
				//Window.alert("Valid Modification");
				updateAppliedQDMList(modifyWithDTO, modifyValueSetDTO,dataType,dataTypeText,isSpecificOccurrence);
				
			
			}
		}else{
			Window.alert("No QDM selected for modification");
			ModifyQDMDialogBox.dialogBox.hide();
		
		}
		
	}
	
	private void updateAppliedQDMList(final CodeListSearchDTO codeListSearchDTO , final QualityDataSetDTO  qualityDataSetDTO, String dataType, String dataTypeText, Boolean isSpecificOccurrence){
		MatContext.get().getCodeListService().updateCodeListToMeasure(MatContext.get().getCurrentMeasureId(),dataType, codeListSearchDTO,qualityDataSetDTO, isSpecificOccurrence,appliedQDMList, new AsyncCallback<SaveUpdateCodeListResult>(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("failure");
				ModifyQDMDialogBox.dialogBox.hide();
				
			}
			@Override
			public void onSuccess(SaveUpdateCodeListResult result) {
				if(result.getFailureReason()==7){
					searchDisplay.getErrorMessageDisplay().setMessage("Duplicate QDM");
				}
				else{
					Window.alert("Success" + result.getAppliedQDMList().size());
					updateMeasureXML(result.getAppliedQDMList(), codeListSearchDTO ,   qualityDataSetDTO);
					ModifyQDMDialogBox.dialogBox.hide();
				}
			}
		});
		
	}
	
	private void updateMeasureXML(ArrayList<QualityDataSetDTO> updatedQDMList, CodeListSearchDTO codeListSearchDTO, QualityDataSetDTO qualityDataSetDTO){
		MatContext.get().getMeasureService().updateMeasureXML(updatedQDMList, codeListSearchDTO, qualityDataSetDTO, MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				
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
	
	private void displaySearch() {
		ModifyQDMDialogBox.showModifyDialogBox(searchDisplay.asWidget());
		searchDisplay.setAddToMeasureButtonEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
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
