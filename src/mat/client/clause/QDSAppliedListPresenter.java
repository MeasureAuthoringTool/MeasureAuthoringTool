package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class QDSAppliedListPresenter implements MatPresenter {

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	ArrayList<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();

	MeasureServiceAsync service = MatContext.get().getMeasureService();

	List<String> codeListString = new ArrayList<String>();

	public static interface SearchDisplay {
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		Widget asWidget();
		void buildCellList(QDSAppliedListModel appliedListModel);
		Button getRemoveButton();
		Button getModifyButton();
		QualityDataSetDTO getSelectedElementToRemove();
		List<QualityDataSetDTO> getAllAppliedQDMList();
		void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList);
	}

	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		getXMLForAppliedQDM(true);
		searchDisplay.getRemoveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				if (searchDisplay.getSelectedElementToRemove() != null) {
						service.getMeasureXMLForAppliedQDM(MatContext.get().getCurrentMeasureId(),
								false, new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

							@Override
							public void onFailure(final Throwable caught) {
								Window.alert(MatContext.get().getMessageDelegate()
										.getGenericErrorMessage());

							}
							@Override
							public void onSuccess(final ArrayList<QualityDataSetDTO> result) {
								allQdsList = result;
								if (allQdsList.size() > 0) {
										Iterator<QualityDataSetDTO> iterator =
														allQdsList.iterator();
										while (iterator.hasNext()) {
											QualityDataSetDTO dataSetDTO = iterator.next();
											if (dataSetDTO.getUuid().equals(searchDisplay.
													getSelectedElementToRemove().getUuid())) {
												iterator.remove();
											}
										}
									saveMeasureXML(allQdsList);
								}

							}

						});
					} else {
						searchDisplay.getErrorMessageDisplay().setMessage(
								"Please select at least one unused value set to delete.");
					}
				}

		});

		searchDisplay.getModifyButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.getApplyToMeasureSuccessMsg().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				QualityDataSetDTO dataSetDTO = searchDisplay.getSelectedElementToRemove();
				if (dataSetDTO != null) {
					QDMAvailableValueSetWidget availableValueSetWidget = new QDMAvailableValueSetWidget();
					QDMAvailableValueSetPresenter availableValueSetPresenter =
						new QDMAvailableValueSetPresenter(availableValueSetWidget, dataSetDTO, searchDisplay);
					availableValueSetPresenter.beforeDisplay();
				}
			}
		});
	}

	private void saveMeasureXML(final ArrayList<QualityDataSetDTO> list) {
		service.createAndSaveElementLookUp(list, MatContext.get().getCurrentMeasureId(),  new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());

			}

			@Override
			public void onSuccess(final Void result) {
				allQdsList.removeAll(allQdsList);
				resetQDSFields();
				loadAppliedListData();
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage("Successfully removed selected QDM element.");

			}
		});
	}

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
		if (measureId != null && measureId != "") {
			service.getMeasureXMLForAppliedQDM(measureId , checkForSupplementData ,
					new AsyncCallback<ArrayList<QualityDataSetDTO>>() {
 
				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}

				@Override
				public void onSuccess(final ArrayList<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					appliedListModel.setAppliedQDMs(result);
					searchDisplay.buildCellList(appliedListModel);
					searchDisplay.setAppliedQDMList(result);
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
