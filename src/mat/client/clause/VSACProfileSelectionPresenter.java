package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mat.client.MatPresenter;
import mat.client.clause.VSACProfileSelectionView.Observer;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.VSACVersion;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class VSACProfileSelectionPresenter.
 */
public class VSACProfileSelectionPresenter implements MatPresenter {

	/**
	 * The Interface SearchDisplay.
	 */
	interface SearchDisplay {

		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();

		/**
		 * Gets the VSAC profile input.
		 * 
		 * @return the VSAC profile input
		 */
		HasValueChangeHandlers<Boolean> getVSACProfileInput();

		/**
		 * Gets the VSAC profile list box.
		 * 
		 * @return the VSAC profile list box
		 */
		ListBoxMVP getVSACProfileListBox();

		/**
		 * Sets the vsac profile selection list.
		 * 
		 * @param vsacProfileSelectionList
		 *            the new vsac profile selection list
		 */
		void setVsacProfileSelectionList(List<String> vsacProfileSelectionList);

		/**
		 * Sets the vsac profile list box.
		 */
		void setVSACProfileListBox();

		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();

		/**
		 * Reset vsac value set widget.
		 */
		void resetVSACValueSetWidget();

		/**
		 * Builds the custom data grid.
		 * 
		 * @param appliedListModel
		 *            the applied list model
		 */
		// void buildCustomDataGrid(QDSAppliedListModel appliedListModel);

		/**
		 * Builds the cell table.
		 * 
		 * @param appliedListModel
		 *            the applied list model
		 */
		void buildAppliedQDMCellTable(QDSAppliedListModel appliedListModel);

		/**
		 * Sets the observer.
		 * 
		 * @param observer
		 *            the new observer
		 */
		void setObserver(Observer observer);

		/**
		 * Gets the list data provider.
		 * 
		 * @return the list data provider
		 */
		ListDataProvider<QualityDataSetDTO> getListDataProvider();

		/**
		 * Builds the add by modify qdm cell table.
		 * 
		 * @param qdsAppliedListModel
		 *            the qds applied list model
		 */
		void buildAddByModifyQDMCellTable(
				QDSAppliedListModel qdsAppliedListModel);

		/**
		 * Sets the version list.
		 * 
		 * @param versionList
		 *            the new version list
		 */
		void setVersionList(List<String> versionList);

		/**
		 * Gets the cell table.
		 * 
		 * @return the cell table
		 */
		CellTable<QualityDataSetDTO> getCellTable();

		/**
		 * Gets the adds the new qdm button.
		 * 
		 * @return the adds the new qdm button
		 */
		HasClickHandlers getAddNewQDMButton();

		/**
		 * Sets the profile list.
		 * 
		 * @param profileList
		 *            the new profile list
		 */
		void setProfileList(List<String> profileList);

		/**
		 * Gets the apply to measure success msg.
		 * 
		 * @return the apply to measure success msg
		 */
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();

		/**
		 * Gets the selected element to remove.
		 *
		 * @return the selected element to remove
		 */
		QualityDataSetDTO getSelectedElementToRemove();
	}

	/** The panel. */
	SimplePanel panel = new SimplePanel();

	/** The search display. */
	SearchDisplay searchDisplay;

	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();

	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();

	/** The vsacapi service. */
	private final VSACAPIServiceAsync vsacapiService = MatContext.get()
			.getVsacapiServiceAsync();

	/** The dto. */
	private QualityDataSetDTO dto = new QualityDataSetDTO();

	/** The all qds list. */
	private List<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();

	/**
	 * Instantiates a new VSAC profile selection presenter.
	 * 
	 * @param srchDisplay
	 *            the srch display
	 */
	public VSACProfileSelectionPresenter(SearchDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		MatContext.get().getAllDataType();
		getAppliedQDMList(true);
		addAllHandlers();
	}

	/**
	 * Gets the applied qdm list.
	 * 
	 * @param checkForSupplementData
	 *            the check for supplement data
	 * @return the applied qdm list
	 */
	private void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId,
					checkForSupplementData,
					new AsyncCallback<List<QualityDataSetDTO>>() {

						private void filterTimingQDMs(
								List<QualityDataSetDTO> result) {
							List<QualityDataSetDTO> timingQDMs = new ArrayList<QualityDataSetDTO>();
							for (QualityDataSetDTO qdsDTO : result) {
								if ("Timing Element".equals(qdsDTO
										.getDataType())
										|| ConstantMessages.BIRTHDATE_OID
												.equals(qdsDTO.getOid())
										|| ConstantMessages.EXPIRED_OID
												.equals(qdsDTO.getOid())) {
									timingQDMs.add(qdsDTO);
								}
							}
							result.removeAll(timingQDMs);
						}

						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate()
									.getGenericErrorMessage());
						}

						@Override
						public void onSuccess(
								final List<QualityDataSetDTO> result) {
							QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
							filterTimingQDMs(result);
							appliedListModel.setAppliedQDMs(result);
							searchDisplay
									.buildAppliedQDMCellTable(appliedListModel);
						}
					});
		}

		searchDisplay.setObserver(new VSACProfileSelectionView.Observer() {

			@Override
			public void onOIDEditClicked(QualityDataSetDTO result, String value) {
				String version = null;
				String effectiveDate = null;
				searchValueSetInVsac(
						value,
						version,
						effectiveDate,
						searchDisplay.getListDataProvider().getList()
								.indexOf(result), result.getId(),
						result.getDataType());
			}

			@Override
			public void onNameEditClicked(QualityDataSetDTO result, String value) {

				QualityDataSetDTO qualityDataSetDTO = new QualityDataSetDTO();
				qualityDataSetDTO.setId(result.getId());
				qualityDataSetDTO.setCodeListName(value);
				qualityDataSetDTO.setOid(ConstantMessages.USER_DEFINED_CONTEXT_DESC);
				searchDisplay.getListDataProvider().getList()
						.set(0, qualityDataSetDTO);
				searchDisplay.getListDataProvider().refresh();

			}

			@Override
			public void onSaveClicked(QualityDataSetDTO result) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifyClicked(QualityDataSetDTO result) {
				dto = result;
				List<String> versionList = new ArrayList<String>();
				List<String> profileList = new ArrayList<String>();
				if (dto.getExpansionProfile() != null) {
					profileList.add(dto.getExpansionProfile());
				}
				if (!result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_DESC)) {
					versionList.add(dto.getVersion());
				}
				searchDisplay.setVersionList(versionList);
				searchDisplay.setProfileList(profileList);
				updateModifyCellTable();
			}

			@Override
			public void onDeleteClicked(QualityDataSetDTO result) {
				service.getAppliedQDMFromMeasureXml(MatContext.get()
						.getCurrentMeasureId(), false,
						new AsyncCallback<List<QualityDataSetDTO>>() {

							@Override
							public void onSuccess(List<QualityDataSetDTO> result) {
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
									saveMeasureXML(allQdsList);
								}

							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});

			}

		});

	}

	/**
	 * Save measure xml.
	 * 
	 * @param list
	 *            the list
	 */
	private void saveMeasureXML(final List<QualityDataSetDTO> list) {
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
				displaySearch();
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
						"Successfully removed selected QDM element.");
			}

		});
	}

	/**
	 * Load applied list data.
	 */
	public final void loadAppliedListData() {
		getAppliedQDMList(true);
		displaySearch();
	}

	/**
	 * Reset qds fields.
	 */
	private void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
	}

	/**
	 * Update modify cell table.
	 */
	private void updateModifyCellTable() {
		QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
		List<QualityDataSetDTO> modifyList = new ArrayList<QualityDataSetDTO>();
		modifyList.add(dto);
		appliedListModel.setAppliedQDMs(modifyList);
		searchDisplay.buildAddByModifyQDMCellTable(appliedListModel);

	}

	/**
	 * Search value set in vsac.
	 * 
	 * @param oid
	 *            the oid
	 * @param version
	 *            the version
	 * @param effectiveDate
	 *            the effective date
	 * @param index
	 *            the index
	 * @param id
	 *            the id
	 * @param dataType
	 *            the data type
	 */
	private void searchValueSetInVsac(final String oid, String version,
			String effectiveDate, final int index, final String id,
			final String dataType) {

		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
							.getUMLS_NOT_LOGGEDIN());
			return;
		}

		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
							.getUMLS_OID_REQUIRED());
			return;
		}

		vsacapiService.getValueSetByOIDAndVersionOrEffectiveDate(oid, version,
				effectiveDate, new AsyncCallback<VsacApiResult>() {
					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate()
										.getVSAC_RETRIEVE_FAILED());
					}

					/**
					 * On success.
					 * 
					 * @param result
					 *            the result
					 */
					@Override
					public void onSuccess(final VsacApiResult result) {
						int indeOf = index;
						if (result.isSuccess()) {
							List<MatValueSet> list = result.getVsacResponse();
							if (list != null && list.size() >= 0) {
								setQualityDataSetDto(list.get(0), id, dataType);
							}
							searchDisplay.getListDataProvider().getList()
									.set(indeOf, getQualityDataSetDto());
							searchDisplay.getListDataProvider().refresh();

						} else {
							String message = convertMessage(result
									.getFailureReason());
							searchDisplay.getErrorMessageDisplay().setMessage(
									message);
						}
						//to get the VSAC version list corresponding the OID
						getVSACVersionListByOID(oid);
					}

				});

	}

	/**
	 * Gets the VSAC version list by oid.
	 * 
	 * @param oid
	 *            the oid
	 * @return the VSAC version list by oid
	 */
	private void getVSACVersionListByOID(String oid) {
		vsacapiService.getAllVersionListByOID(oid,
				new AsyncCallback<VsacApiResult>() {

					@Override
					public void onSuccess(VsacApiResult result) {
						if (result.getVsacVersionResp() != null) {
							searchDisplay.setVersionList(getVersionList(result
									.getVsacVersionResp()));
							searchDisplay.setProfileList(MatContext.get()
									.getProfileList());
							List<String> versionList = new ArrayList<String>();
							List<String> profileList = new ArrayList<String>();
							List<QualityDataSetDTO> list = new ArrayList<QualityDataSetDTO>();
							profileList.addAll(MatContext.get()
									.getProfileList());
							versionList.addAll(getVersionList(result.getVsacVersionResp()));
							searchDisplay.setVersionList(versionList);
							searchDisplay.setProfileList(profileList);
							QDSAppliedListModel qdsAppliedListModel = new QDSAppliedListModel();
							list.add(dto);
							qdsAppliedListModel.setAppliedQDMs(list);
							searchDisplay
									.buildAddByModifyQDMCellTable(qdsAppliedListModel);
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});

	}

	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	private String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate()
					.getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate()
					.getUMLS_OID_REQUIRED();
			break;
		default:
			message = MatContext.get().getMessageDelegate()
					.getVSAC_RETRIEVE_FAILED();
		}
		return message;
	}

	/**
	 * Adds the all handlers.
	 */
	private void addAllHandlers() {
		searchDisplay.getVSACProfileInput().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue().toString().equals("true")) {
							if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
																		// Login
																		// Validation
								searchDisplay
										.getErrorMessageDisplay()
										.setMessage(
												MatContext.get()
														.getMessageDelegate()
														.getUMLS_NOT_LOGGEDIN());
								return;
							}
							searchDisplay.getVSACProfileListBox().setEnabled(
									true);
							searchDisplay.setProfileList(MatContext.get()
									.getProfileList());
							searchDisplay.setVSACProfileListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getErrorMessageDisplay().clear();
							searchDisplay.getVSACProfileListBox().setEnabled(
									false);
							searchDisplay.setVSACProfileListBox();
						}

					}
				});
		searchDisplay.getAddNewQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				QDSAppliedListModel qdsAppliedListModel = new QDSAppliedListModel();
				List<String> versionList = new ArrayList<String>();
				List<String> profileList = new ArrayList<String>();
				searchDisplay.setVersionList(versionList);
				searchDisplay.setProfileList(profileList);
				searchDisplay.buildAddByModifyQDMCellTable(qdsAppliedListModel);

			}
		});
	}

	/**
	 * Gets the profile list.
	 * 
	 * @param list
	 *            the list
	 * @return the profile list
	 */
	private List<String> getVersionList(List<VSACVersion> list) {
		List<String> versionList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			versionList.add(list.get(i).getName());
		}
		return versionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		return panel;
	}

	/**
	 * Display search.
	 */
	public void displaySearch() {
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.resetVSACValueSetWidget();
		searchDisplay.buildAddByModifyQDMCellTable(new QDSAppliedListModel());
		getAppliedQDMList(true);
		MatContext.get().getAllProfileList();
	}

	/**
	 * Sets the quality data set dto.
	 * 
	 * @param matValueSet
	 *            the mat value set
	 * @param id
	 *            the id
	 * @param dataType
	 *            the data type
	 */
	private void setQualityDataSetDto(MatValueSet matValueSet, String id,
			String dataType) {
		QualityDataSetDTO dto = new QualityDataSetDTO();
		dto.setCodeListName(matValueSet.getDisplayName());
		dto.setOid(matValueSet.getID());
		dto.setId(id);
		dto.setVersion(matValueSet.getRevisionDate());
		dto.setDataType(dataType);
		this.dto = dto;
	}
	

	/**
	 * Gets the quality data set dto.
	 * 
	 * @return the quality data set dto
	 */
	private QualityDataSetDTO getQualityDataSetDto() {
		return dto;
	}

}
