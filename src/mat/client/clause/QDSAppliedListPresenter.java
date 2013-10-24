package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mat.client.Mat;
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

/**
 * QDSAppliedListPresenter.java.
 */
public class QDSAppliedListPresenter implements MatPresenter {

	/**
	 * SimplePanel Instance.
	 */
	private SimplePanel panel = new SimplePanel();
	/**
	 * SearchDisplay instance.
	 */
	private SearchDisplay searchDisplay;
	/**
	 * List of all Applied QDM's.
	 */
	private ArrayList<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();

	/**
	 * Measure Service Instance.
	 */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	/**
	 * VSACAPIService Instance.
	 */
	private VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get().getVsacapiServiceAsync();


	/**
	 * Search Display inner interface which is implemented by QDSAppliedListView
	 * class.
	 */
	public static interface SearchDisplay {
		/**
		 * @return {@link SuccessMessageDisplayInterface}
		 */
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();

		/**
		 * @return {@link ErrorMessageDisplayInterface}
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();

		/**
		 * @return {@link Widget}
		 */
		Widget asWidget();

		/**
		 * @param appliedListModel
		 *            - {@link QDSAppliedListModel}
		 */
		void buildCellList(QDSAppliedListModel appliedListModel);

		/**
		 * @return {@link Button}
		 */
		Button getRemoveButton();

		/**
		 * @return {@link Button}
		 */
		Button getModifyButton();

		/**
		 * @return {@link Button}
		 */
		Button getUpdateVsacButton();

		/**
		 * @return {@link QualityDataSetDTO}
		 */
		QualityDataSetDTO getSelectedElementToRemove();

		/**
		 * @return {@link List} of {@link QualityDataSetDTO}
		 */
		List<QualityDataSetDTO> getAllAppliedQDMList();

		/**
		 * @param appliedQDMList
		 *            {@link ArrayList} of {@link QualityDataSetDTO}
		 */
		void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList);
	}

	/**
	 * @param sDisplayArg
	 *            {@link SearchDisplay}
	 */
	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		searchDisplay = sDisplayArg;
		getAppliedQDMList(true);
		searchDisplay.getRemoveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				if (searchDisplay.getSelectedElementToRemove() != null) {
					service.getAppliedQDMFromMeasureXml(MatContext.get()
							.getCurrentMeasureId(), false,
							new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get()
									.getMessageDelegate()
									.getGenericErrorMessage());

						}

						@Override
						public void onSuccess(
								final ArrayList<QualityDataSetDTO> result) {
							allQdsList = result;
							if (allQdsList.size() > 0) {
								Iterator<QualityDataSetDTO> iterator = allQdsList
										.iterator();
								while (iterator.hasNext()) {
									QualityDataSetDTO dataSetDTO = iterator
											.next();
									if (dataSetDTO
											.getUuid()
											.equals(searchDisplay
													.getSelectedElementToRemove()
													.getUuid())) {
										iterator.remove();
									}
								}
								searchDisplay
								.setAppliedQDMList(allQdsList);
								saveMeasureXML(allQdsList);
							}

						}

					});
				} else {
					searchDisplay
					.getErrorMessageDisplay()
					.setMessage(
							"Please select at least one unused value set to delete.");
				}
			}

		});

		searchDisplay.getModifyButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.getApplyToMeasureSuccessMsg().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				QualityDataSetDTO dataSetDTO = searchDisplay
						.getSelectedElementToRemove();
				if (dataSetDTO != null) {
					QDMAvailableValueSetWidget availableValueSetWidget = new QDMAvailableValueSetWidget();
					QDMAvailableValueSetPresenter availableValueSetPresenter = new QDMAvailableValueSetPresenter(
							availableValueSetWidget, dataSetDTO, searchDisplay);
					availableValueSetPresenter.beforeDisplay();
				}
			}
		});

		searchDisplay.getUpdateVsacButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				updateVSACValueSets();
			}
		});
	}

	/**
	 * Service Call to updateVsacValueSet.
	 */
	private void updateVSACValueSets() {
		showSearchingBusy(true);
		vsacapiServiceAsync.updateVSACValueSets(MatContext.get().getCurrentMeasureId(), new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				showSearchingBusy(false);
			}

			@Override
			public void onSuccess(final VsacApiResult result) {
				showSearchingBusy(false);
				if (result.isSuccess()) {
					searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
							MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
					getAppliedQDMList(true);
				} else {
					searchDisplay.getErrorMessageDisplay().setMessage(convertMessage(result.getFailureReason()));
				}
			}
		});
	}

	/**
	 * Method to get Message against failure reason.
	 * @param id
	 *            - {@link Integer}
	 * @return {@link String}
	 */
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

	/**
	 * Service Call to create and save QDM's in Element Look Up tag.
	 * @param list
	 *            - {@link ArrayList} of {@link QualityDataSetDTO}
	 */
	private void saveMeasureXML(final ArrayList<QualityDataSetDTO> list) {
		service.createAndSaveElementLookUp(list, MatContext.get()
				.getCurrentMeasureId(), new AsyncCallback<Void>() {

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
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
						"Successfully removed selected QDM element.");

			}
		});
	}

	/**
	 * Method to load Applied QDM's List in to view.
	 */
	final void loadAppliedListData() {
		panel.clear();
		getAppliedQDMList(true);
		displaySearch();
	}

	/**
	 * Method to reset Success and Error Messages.
	 */
	public final void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();

	}

	/**
	 * Method to put view on Main Panel.
	 */
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
	 * @param checkForSupplementData
	 *            - {@link Boolean}
	 */
	public final void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId,
					checkForSupplementData,
					new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}

				@Override
				public void onSuccess(
						final ArrayList<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					filterTimingQDMs(result);
					appliedListModel.setAppliedQDMs(result);
					searchDisplay.buildCellList(appliedListModel);
					searchDisplay.setAppliedQDMList(result);
				}

				private void filterTimingQDMs(
						ArrayList<QualityDataSetDTO> result) {
					List<QualityDataSetDTO> timingQDMs = new ArrayList<QualityDataSetDTO>();
					for (QualityDataSetDTO qdsDTO : result) {
						if ("Timing Element".equals(qdsDTO
								.getDataType())) {
							timingQDMs.add(qdsDTO);
						}
					}
					result.removeAll(timingQDMs);
				}
			});

		}

	}

	/**
	 * Method to show/hide Loading please wait message.
	 * @param busy
	 *            - {@link Boolean}
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getUpdateVsacButton().setEnabled(!busy);
	}

	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadAppliedListData();

	}

	@Override
	public void beforeClosingDisplay() {
	}

	@Override
	public Widget getWidget() {
		return panel;
	}

}
