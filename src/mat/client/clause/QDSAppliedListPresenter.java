package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
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
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get().getVsacapiServiceAsync();

	List<String> codeListString = new ArrayList<String>();

	public static interface SearchDisplay {
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		Widget asWidget();
		void buildCellList(QDSAppliedListModel appliedListModel);
		Button getRemoveButton();
		Button getModifyButton();
		Button getUpdateVsacButton();
		QualityDataSetDTO getSelectedElementToRemove();
		List<QualityDataSetDTO> getAllAppliedQDMList();
		void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList);
	}

	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		getAppliedQDMList(true);
		searchDisplay.getRemoveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				if (searchDisplay.getSelectedElementToRemove() != null) {
						service.getAppliedQDMFromMeasureXml(MatContext.get().getCurrentMeasureId(),
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
									searchDisplay.setAppliedQDMList(allQdsList);
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

		searchDisplay.getUpdateVsacButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				vsacapiServiceAsync.updateAllVSACValueSets(
						MatContext.get().getCurrentMeasureId(), new AsyncCallback<VsacApiResult>() {

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate()
								.getGenericErrorMessage());
					}

					@Override
					public void onSuccess(final VsacApiResult result) {
						if (result.isSuccess()) {
							searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
								MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
							getAppliedQDMList(true);
						} else {
							searchDisplay.getErrorMessageDisplay().setMessage(
									convertMessage(result.getFailureReason()));
						}
					}
				});
			}
		});
	}

	private String convertMessage(int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate()
					.getUMLS_NOT_LOGGEDIN();
			break;
		default:
			message = MatContext.get().getMessageDelegate()
					.getUnknownFailMessage();
		}
		return message;
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
		getAppliedQDMList(true);
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
		if (MatContext.get().isCurrentMeasureEditable()) {
			searchDisplay.getUpdateVsacButton().setEnabled(true);
		} else {
			searchDisplay.getUpdateVsacButton().setEnabled(false);
		}
		panel.add(searchDisplay.asWidget());
	}
	/**
	 * Method for fetching all applied Value Sets in a measure which is loaded
	 * in context.
	 *
	 * */
	public void getAppliedQDMList(boolean checkForSupplementData){
		String measureId = MatContext.get().getCurrentMeasureId();
		if (measureId != null && measureId != "") {
			service.getAppliedQDMFromMeasureXml(measureId , checkForSupplementData ,
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
