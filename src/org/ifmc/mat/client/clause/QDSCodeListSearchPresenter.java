package org.ifmc.mat.client.clause;

import java.util.Collections;
import java.util.List;

import org.ifmc.mat.client.Mat;
import org.ifmc.mat.client.clause.event.QDSElementCreatedEvent;
import org.ifmc.mat.client.codelist.HasListBox;
import org.ifmc.mat.client.codelist.ManageCodeListSearchModel;
import org.ifmc.mat.client.codelist.ValueSetSearchFilterPanel;
import org.ifmc.mat.client.codelist.events.OnChangeOptionsEvent;
import org.ifmc.mat.client.codelist.service.SaveUpdateCodeListResult;
import org.ifmc.mat.client.measure.metadata.CustomCheckBox;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.FocusableWidget;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.SuccessMessageDisplayInterface;
import org.ifmc.mat.client.shared.search.PageSelectionEvent;
import org.ifmc.mat.client.shared.search.PageSelectionEventHandler;
import org.ifmc.mat.client.shared.search.PageSizeSelectionEvent;
import org.ifmc.mat.client.shared.search.PageSizeSelectionEventHandler;
import org.ifmc.mat.client.shared.search.SearchResultUpdate;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.shared.ConstantMessages;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QDSCodeListSearchPresenter {
	
	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	private int startIndex = 1;
	private String currentSortColumn = getSortKey(0);
	private boolean sortIsAscending = true;
	private boolean showdefaultCodeList = true;
	private String lastSearchText;
	private int lastStartIndex;
	
	private QDSCodeListSearchModel currentCodeListResults;

	public static interface SearchDisplay extends org.ifmc.mat.client.shared.search.SearchDisplay{
		public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption();
		public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement();
		public void buildQDSDataTable(SearchResults<CodeListSearchDTO> results);
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

	public QDSCodeListSearchPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		

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
				addSelectedCodeListtoMeasure();
			}
		});
		
		
		
	}
	
	private void search(String searchText, int startIndex, int pageSize,
			String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		lastSearchText = searchText;
		lastStartIndex = startIndex;
		showSearchingBusy(true);
		MatContext.get().getCodeListService().search(searchText,
				startIndex, pageSize, 
				sortColumn, isAsc, defaultCodeList, filter,
				new AsyncCallback<ManageCodeListSearchModel>() {
			@Override
			public void onSuccess(ManageCodeListSearchModel result) {
				if(result.getData().isEmpty() && !lastSearchText.isEmpty()){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoCodeListsMessage());
				}else{
					resetQDSFields();
				}
				QDSCodeListSearchModel QDSSearchResult = new QDSCodeListSearchModel();
				QDSSearchResult.setData(result.getData());
				QDSSearchResult.setResultsTotal(result.getResultsTotal());
				currentCodeListResults = QDSSearchResult;
				
				SearchResultUpdate sru = new SearchResultUpdate();
				sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
				sru = null;
				
				searchDisplay.buildQDSDataTable(QDSSearchResult);
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
		searchDisplay.getValueSetSearchFilterPanel().resetFilter();
		int filter = searchDisplay.getValueSetSearchFilterPanel().getDefaultFilter();
		search("", 1, searchDisplay.getPageSize(), currentSortColumn, sortIsAscending,showdefaultCodeList,filter);
	}
	
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		searchDisplay.setAddToMeasureButtonEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
	}
	
	private void addSelectedCodeListtoMeasure(){
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
		    	   dataType = searchDisplay.getDataTypeValue();
		       }
		      
		       if(searchDisplay.getDataTypeText().equalsIgnoreCase("--Select--")){
		    	   dataTypeText = dataType;
		       }else{
		    	   dataTypeText = searchDisplay.getDataTypeText();
		       }
		        isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
		        String measureID = MatContext.get().getCurrentMeasureId();
				if(!dataType.isEmpty() && !dataType.equals("")){
					MatContext.get().getCodeListService().addCodeListToMeasure(measureID,dataType, codeList, isSpecificOccurrence, new AsyncCallback<SaveUpdateCodeListResult>(){
					@Override
					public void onSuccess(SaveUpdateCodeListResult result) {
						String message="";
						searchDisplay.getSpecificOccurrenceInput().setValue(false);//OnSuccess() uncheck the specific occurrence and deselect the radio options 
						if(result.isSuccess()) {
							if(result.getOccurrenceMessage()!= null && !result.getOccurrenceMessage().equals("")){
								message = MatContext.get().getMessageDelegate().getQDMOcurrenceSuccessMessage(codeList.getName(), dataTypeText, result.getOccurrenceMessage());
							}else{
								message = MatContext.get().getMessageDelegate().getQDMSuccessMessage(codeList.getName(), dataTypeText);
							}
							MatContext.get().getEventBus().fireEvent(new QDSElementCreatedEvent(codeList.getName()));
							searchDisplay.getApplyToMeasureSuccessMsg().setMessage(message);
							searchDisplay.getMsgFocusWidget().getElement().setAttribute("role", "alert");//This line adds the aria-alert 
							searchDisplay.getMsgFocusWidget().setFocus(true);
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage("problem while saving the QDM to Measure");
					}
				});
				}
		   
		}
			
		
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

}
