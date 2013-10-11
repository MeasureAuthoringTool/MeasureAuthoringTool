package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasurePackagePresenter implements MatPresenter {
	private SimplePanel emptyPanel = new SimplePanel();
	private SimplePanel panel = new SimplePanel();

	public static interface MeasurePackageSelectionHandler {
		public void onSelection(MeasurePackageDetail detail);
	}

	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();

	public static interface View {
		void setClauses(List<MeasurePackageClauseDetail> clauses);
		void setClausesInPackage(List<MeasurePackageClauseDetail> list);
		List<MeasurePackageClauseDetail> getClausesInPackage();
		void setPackageName(String name);
		HasClickHandlers getAddClausesToPackageButton();
		HasClickHandlers getCreateNewButton();
		void setMeasurePackages(List<MeasurePackageDetail> packages);
		HasClickHandlers getPackageMeasureButton();
		ErrorMessageDisplayInterface getPackageErrorMessageDisplay();
		ErrorMessageDisplayInterface getMeasureErrorMessageDisplay();
		SuccessMessageDisplayInterface getPackageSuccessMessageDisplay();
		SuccessMessageDisplayInterface getSuppDataSuccessMessageDisplay();
		SuccessMessageDisplayInterface getMeasurePackageSuccessMsg();
		Widget asWidget();
		void setSelectionHandler(MeasurePackageSelectionHandler handler);
		void setDeletionHandler(MeasurePackageSelectionHandler handler);
		void setViewIsEditable(boolean b,
				List<MeasurePackageDetail> packages);
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		void setQDMElementsInSuppElements(List<QualityDataSetDTO> list);
		void setQDMElements(List<QualityDataSetDTO> clauses);
		List<QualityDataSetDTO> getQDMElementsInSuppElements();
		List<QualityDataSetDTO> getQDMElements();
		HasClickHandlers getAddQDMElementsToMeasureButton();
		ErrorMessageDisplayInterface getQDMErrorMessageDisplay();
		WarningMessageDisplay getMeasurePackageWarningMsg();
	}

	private View view;
	private MeasurePackageDetail currentDetail;
	private ManageMeasureDetailModel model;
	private MeasurePackageOverview overview;

	/**
	 *Constructor.
	 *@param viewArg - View.
	 * */
	public MeasurePackagePresenter(final View viewArg) {
		this.view = viewArg;
		addAllHandlers();
	}

	/**
	 * All Handlers for View.
	 */
	private void addAllHandlers() {
		view.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				setNewMeasurePackage();
			}
		});
		view.getPackageMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				// O&M 17
				((Button) view.getPackageMeasureButton()).setEnabled(false);

				clearMessages();

				MeasureSelectedEvent mse = MatContext.get().getCurrentMeasureInfo();
				String msg = " [measure] " + mse.getMeasureName() + " [version] " + mse.getMeasureVersion()
						+ " [package date] " + new Date();
				String mid = mse.getMeasureId();
				MatContext.get().recordTransactionEvent(mid, null,
						"MEASURE_PACKAGE_EVENT", msg, ConstantMessages.DB_LOG);
				model.setValueSetDate(null);
				MatContext.get().getMeasureService()
						.save(model, new AsyncCallback<SaveMeasureResult>() {

							@Override
							public void onSuccess(final SaveMeasureResult result) {
								if (result.isSuccess()) {
									Mat.showLoadingMessage();
									String measureId = MatContext.get()
											.getCurrentMeasureId();
									updateValueSetsBeforePackaging(measureId);
								} else {
									((Button) view.getPackageMeasureButton())
											.setEnabled(true);
									if (result.getFailureReason()
											== SaveMeasureResult.INVALID_VALUE_SET_DATE) {
										String message = MatContext.get()
												.getMessageDelegate()
												.getValueSetDateInvalidMessage();
										view.getErrorMessageDisplay().setMessage(message);
									}
								}
							}
							@Override
							public void onFailure(final Throwable caught) {
								((Button) view.getPackageMeasureButton())
										.setEnabled(true);

								view.getErrorMessageDisplay().setMessage(
										MatContext.get().getMessageDelegate()
												.getGenericErrorMessage());
								MatContext.get().recordTransactionEvent(
										null , null , null ,
										"Unhandled Exception: "
										+ caught.getLocalizedMessage() , 0);
							}
						});
			}
		});
		view.getAddClausesToPackageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				updateDetailsFromView();
				if (isValid()) {
					MatContext.get().getPackageService()
							.save(currentDetail, new AsyncCallback<Void>() {
								@Override
								public void onFailure(final Throwable caught) {
									view.getQDMErrorMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate().getUnableToProcessMessage());
								}
								@Override
								public void onSuccess(final Void result) {
									if (!overview.getPackages().contains(
											currentDetail)) {
										overview.getPackages().add(
												currentDetail);
										setOverview(overview);
									}
									view.getPackageSuccessMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate().getGroupingSavedMessage());
								}
							});
				}
			}
		});
		view.getAddQDMElementsToMeasureButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						clearMessages();
						updateSuppDataDetailsFromView();
						MatContext
								.get()
								.getPackageService()
								.saveQDMData(currentDetail,
										new AsyncCallback<Void>() {
											@Override
											public void onFailure(final Throwable caught) {
												view.getPackageErrorMessageDisplay().
												setMessage(MatContext.get().
												getMessageDelegate().
												getUnableToProcessMessage());
											}

											@Override
											public void onSuccess(final Void result) {
												if (!overview.getPackages().contains(
														currentDetail)) {
													if (currentDetail
														.getPackageClauses() != null
														&& currentDetail.
														getPackageClauses()
															.size() > 0) {
														overview.getPackages()
														.add(currentDetail);
													}
													overview.setQdmElements(
															currentDetail
															.getQdmElements());
													overview.
													setSuppDataElements(currentDetail
													.getSuppDataElements());
													setOverview(overview);
												}
												view.getSuppDataSuccessMessageDisplay()
												.setMessage(MatContext.get()
													.getMessageDelegate()
													.getSuppDataSavedMessage());
											}
										});
					}
				});
	}

	/**
	 *Service call to VSAC to update Measure Xml before invoking simple xml and value set sheet generation.
	 *@param measureId - String.
	 * **/
	private void updateValueSetsBeforePackaging(final String measureId) {
		vsacapiServiceAsync.updateAllVSACValueSetsAtPackage(measureId,
				new AsyncCallback<VsacApiResult>() {

					@Override
					public void onFailure(final Throwable caught) {
					}

					@Override
					public void onSuccess(final VsacApiResult result) {
						validateMeasureAndExport(measureId,
								result);
					}
				});
	}

	/**
	 *Service Call to generate Simple Xml and value set sheet.
	 *@param measureId - String.
	 *@param updateVsacResult - VsacApiResult.
	 * **/
	private void validateMeasureAndExport(final String measureId,
			final VsacApiResult updateVsacResult) {
		MatContext
				.get()
				.getMeasureService()
				.validateMeasureForExport(measureId, updateVsacResult.getVsacResponse(),
						new AsyncCallback<ValidateMeasureResult>() {
							@Override
							public void onFailure(final Throwable caught) {
								// O&M 17
								((Button) view.getPackageMeasureButton())
										.setEnabled(true);

								Mat.hideLoadingMessage();
								view.getPackageErrorMessageDisplay()
										.setMessage(
												MatContext
														.get()
														.getMessageDelegate()
													.getUnableToProcessMessage());
							}

							@Override
							public void onSuccess(
									final ValidateMeasureResult result) {
								// O&M 17
								((Button) view.getPackageMeasureButton())
										.setEnabled(true);

								if (result.isValid() && updateVsacResult.isSuccess()) {
									Mat.hideLoadingMessage();
									view.getMeasurePackageSuccessMsg()
											.setMessage(
													MatContext
															.get()
														.getMessageDelegate()
													.getPackageSuccessMessage());

								} else if (result.isValid() && !updateVsacResult.isSuccess()) {
									Mat.hideLoadingMessage();
									if (updateVsacResult.getFailureReason()
											== VsacApiResult.UMLS_NOT_LOGGEDIN) {
										view.getMeasurePackageWarningMsg().setMessage(
												MatContext.get().getMessageDelegate()
												.getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN());
										}
								} else {
									Mat.hideLoadingMessage();
									view.getMeasureErrorMessageDisplay()
											.setMessages(
													result.getValidationMessages());
								}
							}
						});
	}

	/**
	 * Method to clear messaged from widget.
	 * **/
	private void clearMessages() {
		view.getPackageSuccessMessageDisplay().clear();
		view.getSuppDataSuccessMessageDisplay().clear();
		view.getPackageErrorMessageDisplay().clear();
		view.getMeasureErrorMessageDisplay().clear();
		view.getMeasurePackageSuccessMsg().clear();
		view.getErrorMessageDisplay().clear();
		view.getMeasurePackageWarningMsg().clear();
	}

	@Override
	public void beforeDisplay() {
		Mat.hideLoadingMessage();
		clearMessages();

		if (MatContext.get().getCurrentMeasureId() != null
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			getMeasure(MatContext.get().getCurrentMeasureId());
		} else {
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MeasurePackage");
		Mat.focusSkipLists("MeasureComposer");
	}

	@Override
	public void beforeClosingDisplay() {

	}

	/**
	 *@param measurePackageId - String.
	 * **/
	private void setMeasurePackage(final String measurePackageId) {
		for (MeasurePackageDetail detail : overview.getPackages()) {
			if (detail.getSequence().equals(measurePackageId)) {
				currentDetail = detail;
				setMeasurePackageDetailsOnView();
				break;
			}
		}
	}

	/**
	 *Valid grouping check.
	 *@return boolean.
	 * **/
	private boolean isValid() {
		List<MeasurePackageClauseDetail> detailList = view
				.getClausesInPackage();
		List<String> messages = new ArrayList<String>();

		String scoring = MatContext.get().getCurrentMeasureScoringType();

		// TODO refactor this into a common shared class so the server can use
		// it for validation also
		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING
				.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableWrongNumMessage());
			}

			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableMayNotContainMessage());
			}

		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) { /*
			 * PROPORTION at least one and only one Population, Denominator at least
			 * one or more Numerator zero or one Denominator Exclusions Denominator
			 * Exceptions and no Numerator Exclusions, Measure Population, Measure
			 * Observations
			 */
			/*
			 * at least one and only one Population, Denominator
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionWrongNumMessage());
			}
			/*
			 * at least one or more Numerator
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_CONTEXT_ID) < 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionTooFewMessage());
			}
			/*
			 * zero or one Denominator Exclusions, Denominator Exceptions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionTooManyMessage());
			}
			/* no Numerator Exclusions, Measure Population, Measure Observations */
			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionMayNotContainMessage());
			}
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) { /*
			 * at least one and only one Population, Denominator, Numerator, zero or
			 * one Denominator Exclusions and no Denominator Exceptions, Measure
			 * Observation, Measure Population
			 */

			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioTooManyMessage());
			}

			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)

			) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioMayNotContainMessage());
			}
		}

		if (messages.size() > 0) {
			view.getPackageErrorMessageDisplay().setMessages(messages);
		} else {
			view.getPackageErrorMessageDisplay().clear();
		}
		return messages.size() == 0;
	}

	/**
	 * updateDetailsFromView.
	 * **/
	private void updateDetailsFromView() {
		currentDetail.setPackageClauses(view.getClausesInPackage());
		currentDetail.setValueSetDate(null);
	}

	/**
	 * updateSuppDataDetailsFromView.
	 * **/
	private void updateSuppDataDetailsFromView() {
		currentDetail.setSuppDataElements(view.getQDMElementsInSuppElements());
		currentDetail.setQdmElements(view.getQDMElements());
	}

	/**
	 * countDetailsWithType.
	 * @param detailList - List of MeasurePackageClauseDetail.
	 * @param type - String.
	 *
	 * @return Integer.
	 * **/
	private int countDetailsWithType(
			final List<MeasurePackageClauseDetail> detailList, final String type) {
		int count = 0;
		for (MeasurePackageClauseDetail detail : detailList) {
			if (type.equals(detail.getType())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * setMeasurePackageDetailsOnView.
	 * **/
	private void setMeasurePackageDetailsOnView() {
		List<MeasurePackageClauseDetail> packageClauses = currentDetail
				.getPackageClauses();
		List<MeasurePackageClauseDetail> remainingClauses = removeClauses(
				overview.getClauses(), packageClauses);

		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setClauses(remainingClauses);
		view.setQDMElementsInSuppElements(overview.getSuppDataElements());
		view.setQDMElements(overview.getQdmElements());
	}

	/**
	 * remove clauses from Package.
	 * @param master - List of MeasurePackageClauseDetail.
	 * @param toRemove - List of MeasurePackageClauseDetail.
	 *
	 * @return MeasurePackageClauseDetail.
	 * **/
	private List<MeasurePackageClauseDetail> removeClauses(
			final List<MeasurePackageClauseDetail> master,
			final List<MeasurePackageClauseDetail> toRemove) {
		List<MeasurePackageClauseDetail> newList = new ArrayList<MeasurePackageClauseDetail>();
		newList.addAll(master);
		for (MeasurePackageClauseDetail remove : toRemove) {
			for (int i = 0; i < newList.size(); i++) {
				if (newList.get(i).getId().equals(remove.getId())) {
					newList.remove(i);
					break;
				}
			}
		}
		return newList;
	}

	/**
	 * set Overview.
	 * @param result - MeasurePackageOverview.
	 * **/
	private void setOverview(final MeasurePackageOverview result) {
		this.overview = result;
		view.setClauses(result.getClauses());
		// QDM elements
		view.setQDMElements(result.getQdmElements());

		view.setMeasurePackages(result.getPackages());

		if (result.getPackages().size() > 0) {
			if (currentDetail != null) {
				for (int i = 0; i < result.getPackages().size(); i++) {
					MeasurePackageDetail mpDetail = result.getPackages().get(i);
					if (mpDetail.getSequence().equalsIgnoreCase(
							currentDetail.getSequence())) {
						setMeasurePackage(result.getPackages().get(i)
								.getSequence());
					}
				}
			} else {
				setMeasurePackage(result.getPackages().get(0).getSequence());
			}

		} else {
			setNewMeasurePackage();
		}

		ReadOnlyHelper.setReadOnlyForCurrentMeasure(view.asWidget(),
				isEditable());
		view.setViewIsEditable(isEditable(), result.getPackages());
	}

	/**
	 * get Measure Package Overview.
	 * @param measureId - String.
	 * **/
	private void getMeasurePackageOverview(final String measureId) {
		MatContext
				.get()
				.getPackageService()
				.getClausesAndPackagesForMeasure(measureId,
						new AsyncCallback<MeasurePackageOverview>() {
							@Override
							public void onSuccess(final MeasurePackageOverview result) {
								if (currentDetail != null
										&& !currentDetail.getMeasureId()
												.equalsIgnoreCase(measureId)) {
									currentDetail = null; // This will make sure
														  // the package
														  // information are not
														  // cached across
														  // measures.
								}
								setOverview(result);
							}

							@Override
							public void onFailure(final Throwable caught) {
								view.getPackageErrorMessageDisplay()
										.setMessage(
												MatContext
														.get()
														.getMessageDelegate()
														.getGenericErrorMessage());
							}
						});
		view.setSelectionHandler(new MeasurePackageSelectionHandler() {
			@Override
			public void onSelection(final MeasurePackageDetail detail) {
				currentDetail = detail;
				clearMessages();
				setMeasurePackageDetailsOnView();
			}
		});
		view.setDeletionHandler(new MeasurePackageSelectionHandler() {

			@Override
			public void onSelection(final MeasurePackageDetail detail) {
				clearMessages();
				deleteMeasurePackage(detail);
			}
		});
	}

	/**
	 * get Measure.
	 * @param measureId - String.
	 * **/
	private void getMeasure(final String measureId) {
		MatContext
				.get()
				.getMeasureService()
				.getMeasure(measureId,
						new AsyncCallback<ManageMeasureDetailModel>() {
							@Override
							public void onSuccess(
									final ManageMeasureDetailModel result) {
								model = result;
								getMeasurePackageOverview(MatContext.get()
										.getCurrentMeasureId());
								displayMeasurePackageWorkspace();
							}

							@Override
							public void onFailure(final Throwable caught) {
								view.getPackageErrorMessageDisplay()
										.setMessage(
												MatContext
														.get()
														.getMessageDelegate()
														.getGenericErrorMessage());
							}
						});

	}

	/**
	 * Delete Measure Package.
	 * @param pkg - MeasurePackageDetail.
	 * **/
	private void deleteMeasurePackage(final MeasurePackageDetail pkg) {
		MatContext.get().getPackageService()
				.delete(pkg, new AsyncCallback<Void>() {

					@Override
					public void onFailure(final Throwable caught) {
						view.getPackageErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate()
										.getGenericErrorMessage());
					}

					@Override
					public void onSuccess(final Void result) {
						overview.getPackages().remove(pkg);
						if (currentDetail.getSequence().equals(
								pkg.getSequence())) {
							currentDetail = null;
						}
						setOverview(overview);
					}

				});
	}

	/**
	 * Get Max Sequence.
	 * @param measurePackageOverview - MeasurePackageOverview.
	 * @return Integer.
	 * **/
	private int getMaxSequence(final MeasurePackageOverview measurePackageOverview) {
		int max = 0;
		for (MeasurePackageDetail detail : measurePackageOverview.getPackages()) {
			int seqInt = Integer.parseInt(detail.getSequence());
			if (seqInt > max) {
				max = seqInt;
			}
		}
		return max;
	}

	/**
	 * set New MeasurePackage.
	 * **/
	private void setNewMeasurePackage() {
		currentDetail = new MeasurePackageDetail();
		currentDetail.setMeasureId(MatContext.get().getCurrentMeasureId());
		currentDetail.setSequence(Integer
				.toString(getMaxSequence(overview) + 1));
		view.setMeasurePackages(overview.getPackages());
		setMeasurePackageDetailsOnView();
	}

	/**
	 * Get Widget.
	 * @return Panel.
	 * **/
	public final Widget getWidget() {
		panel.clear();
		panel.add(view.asWidget());
		return panel;
	}

	/**
	 * Display Empty.
	 * **/
	private void displayEmpty() {
		panel.clear();
		panel.add(emptyPanel);
	}

	/**
	 * Display MeasurePackage Workspace.
	 * **/
	private void displayMeasurePackageWorkspace() {
		panel.clear();
		panel.add(view.asWidget());
		// view.setTabIndex();
	}

	/**
	 * Check if Measure is Editable.
	 * @return boolean.
	 * **/
	private boolean isEditable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}
}
