package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.view.QDMAttributeDialogBox;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.measure.metadata.DeleteMeasureConfirmationBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResultUpdate;
import mat.model.QualityDataSetDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class QDSAppliedListPresenter implements MatPresenter {

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	ArrayList<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();
	boolean isCheckForSDE=false;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	List<String> codeListString = new ArrayList<String>();
	
	public static interface SearchDisplay {
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public Widget asWidget();
		void buildCellList(QDSAppliedListModel appliedListModel);
		Button getRemoveButton();
		Button getModifyButton();
		QualityDataSetDTO getSelectedElementToRemove();
	}

	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		getXMLForAppliedQDM(true);
		searchDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetQDSFields();
				if(searchDisplay.getSelectedElementToRemove()!=null){
					
						service.getMeasureXMLForAppliedQDM(MatContext.get().getCurrentMeasureId(),false, new AsyncCallback<ArrayList<QualityDataSetDTO>>(){

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate()
										.getGenericErrorMessage());	
								
							}
							@Override
							public void onSuccess(ArrayList<QualityDataSetDTO> result) {
								allQdsList=result;
								if(allQdsList.size()>0){
										Iterator<QualityDataSetDTO> iterator = allQdsList.iterator();
										while(iterator.hasNext()){
											QualityDataSetDTO dataSetDTO = iterator.next();
											if(dataSetDTO.getUuid().equals(searchDisplay.getSelectedElementToRemove().getUuid())){
												iterator.remove();
											}
										}
									saveMeasureXML(allQdsList);
								}
								
							}
							
						});
					}else{
						searchDisplay.getErrorMessageDisplay().setMessage("Please select at least one unused value set to delete.");
					}
				}			
					
		});
		
		searchDisplay.getModifyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getApplyToMeasureSuccessMsg().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				QDMAvailableValueSetWidget availableValueSetWidget = new QDMAvailableValueSetWidget();
				QDMAvailableValueSetPresenter availableValueSetPresenter = new QDMAvailableValueSetPresenter(availableValueSetWidget);
				availableValueSetPresenter.beforeDisplay();
			}
		});
	}
	
	private void saveMeasureXML(ArrayList<QualityDataSetDTO> list){
		service.createAndSaveElementLookUp(list,MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());	
				
			}

			@Override
			public void onSuccess(Void result) {
				allQdsList.removeAll(allQdsList);
				resetQDSFields();
				loadAppliedListData();
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage("Successfully removed selected QDM element(s).");
				
			}
		});
	}
	
	
	/*private void search(String searchText, int startIndex, final int pageSize,
			String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		//lastSearchText = (!searchText.equals(null))? searchText.trim() : null;
		//lastStartIndex = startIndex;
		//showSearchingBusy(true);
		displaySearch();
		MatContext.get().getCodeListService().search("",
				startIndex, Integer.MAX_VALUE, 
				sortColumn, isAsc, defaultCodeList, filter,
				new AsyncCallback<ManageCodeListSearchModel>() {
			@Override
			public void onSuccess(ManageCodeListSearchModel result) {
				if(result.getData().isEmpty() && !"".isEmpty()){
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getNoCodeListsMessage());
				}else{
					resetQDSFields();
					//searchDisplay.getSearchString().setValue(lastSearchText);
				}
				QDSCodeListSearchModel QDSSearchResult = new QDSCodeListSearchModel();
				QDSSearchResult.setData(result.getData());
				QDSSearchResult.setResultsTotal(result.getResultsTotal());
			//	ModifyQDMDialogBox.showModifyDialogBox(QDSSearchResult);
				
				//SearchResultUpdate sru = new SearchResultUpdate();
				//sru.update(result, (TextBox)searchDisplay.getSearchString(), lastSearchText);
				//sru = null;
				//searchDisplay.buildQDSDataTable(QDSSearchResult);
				//currentCodeListResults = QDSSearchResult;
				//displaySearch();
				//searchDisplay.getErrorMessageDisplay().setFocus();
				//showSearchingBusy(false);
			}
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage("Problem while performing a search");
				//showSearchingBusy(false);
			}
		});
	}*/
	

	void loadAppliedListData() {
		panel.clear();
		getXMLForAppliedQDM(true);
		displaySearch();
	}

	public Widget getWidget() {
		return panel;
	}

	public void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
	}

	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
	}
	/**
	 * Method for fetching all applied Value Sets in a measure which is loaded
	 * in context.
	 * 
	 * */
	public void getXMLForAppliedQDM(boolean checkForSupplementData){
		String measureId = MatContext.get().getCurrentMeasureId();
		isCheckForSDE = checkForSupplementData;
		if (measureId != null && measureId != "") {
			service.getMeasureXMLForAppliedQDM(measureId,checkForSupplementData, new AsyncCallback<ArrayList<QualityDataSetDTO>>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}

				@Override
				public void onSuccess(ArrayList<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					appliedListModel.setAppliedQDMs(result);
					searchDisplay.buildCellList(appliedListModel);
				}
		});

		}

	}
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadAppliedListData();

	}

	@Override
	public void beforeClosingDisplay() {
	}


}
