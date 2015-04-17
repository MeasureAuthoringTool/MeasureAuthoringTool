package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.QDMAppliedSelectionView.Observer;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.MatTextBox;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.VSACProfile;
import mat.model.VSACVersion;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class VSACProfileSelectionPresenter.
 */
public class QDMAppliedSelectionPresenter implements MatPresenter {

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
		ListBoxMVP getVSACExpansionProfileListBox();

		/**
		 * Sets the vsac profile list box.
		 */
		void setVSACExpansionProfileListBox();

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
		 * Builds the cell table.
		 *
		 * @param appliedListModel            the applied list model
		 * @param isEditable the is editable
		 */
		void buildAppliedQDMCellTable(QDSAppliedListModel appliedListModel, boolean isEditable);

		/**
		 * Sets the observer.
		 * 
		 * @param observer
		 *            the new observer
		 */
		void setObserver(Observer observer);

		
		/**
		 * Gets the adds the new qdm button.
		 * 
		 * @return the adds the new qdm button
		 */
		Button getCancelQDMButton();

		/**
		 * Sets the profile list.
		 * 
		 * @param profileList
		 *            the new profile list
		 */
		void setProfileList(List<String> profileList);

		/**
		 * Gets the selected element to remove.
		 *
		 * @return the selected element to remove
		 */
		QualityDataSetDTO getSelectedElementToRemove();

		/**
		 * Gets the apply button.
		 *
		 * @return the apply button
		 */
		Button getApplyButton();
		
		/**
		 * Gets the retrieve from vsac button.
		 *
		 * @return the retrieve from vsac button
		 */
		Button getRetrieveFromVSACButton();
		
		/**
		 * Gets the save button.
		 *
		 * @return the save button
		 */
		Button getSaveButton();

		/**
		 * Gets the data types list box.
		 *
		 * @return the data types list box
		 */
		ListBoxMVP getDataTypesListBox();

		/**
		 * Gets the version list box.
		 *
		 * @return the version list box
		 */
		ListBoxMVP getVersionListBox();

		/**
		 * Gets the expansion profile list box.
		 *
		 * @return the expansion profile list box
		 */
		ListBoxMVP getVSACProfileListBox();

		/**
		 * Gets the OID input.
		 *
		 * @return the OID input
		 */
		MatTextBox getOIDInput();

		/**
		 * Gets the user defined input.
		 *
		 * @return the user defined input
		 */
		MatTextBox getUserDefinedInput();

		/**
		 * Sets the data types list box options.
		 *
		 * @param texts the new data types list box options
		 */
		void setDataTypesListBoxOptions(List<? extends HasListBox> texts);

		/**
		 * Sets the VSAC version list box options.
		 *
		 * @param texts the new VSAC version list box options
		 */
		void setVSACVersionListBoxOptions(List<? extends HasListBox> texts);

		/**
		 * Sets the VSAC profile list box options.
		 *
		 * @param texts the new VSAC profile list box options
		 */
		void setVSACProfileListBox(List<? extends HasListBox> texts);

		/**
		 * Gets the update from vsac button.
		 *
		 * @return the update from vsac button
		 */
		Button getUpdateFromVSACButton();

		/**
		 * Gets the specific occ chk box.
		 *
		 * @return the specific occ chk box
		 */
		CustomCheckBox getSpecificOccChkBox();

		/**
		 * Gets the data type text.
		 *
		 * @param inputListBox the input list box
		 * @return the data type text
		 */
		String getDataTypeText(ListBoxMVP inputListBox);
		
		/**
		 * Gets the data type value.
		 *
		 * @param inputListBox the input list box
		 * @return the data type value
		 */
		String getDataTypeValue(ListBoxMVP inputListBox);

		/**
		 * Gets the version value.
		 *
		 * @param inputListBox the input list box
		 * @return the version value
		 */
		String getVersionValue(ListBoxMVP inputListBox);

		/**
		 * Gets the expansion profile value.
		 *
		 * @param inputListBox the input list box
		 * @return the expansion profile value
		 */
		String getExpansionProfileValue(ListBoxMVP inputListBox);

		/**
		 * Gets the in progress message display.
		 *
		 * @return the in progress message display
		 */
		InProgressMessageDisplay getInProgressMessageDisplay();

		/**
		 * Gets the search header.
		 *
		 * @return the search header
		 */
		Label getSearchHeader();

		/**
		 * Gets the success message display.
		 *
		 * @return the success message display
		 */
		SuccessMessageDisplay getSuccessMessageDisplay();
		
		/**
		 * Gets the list data provider.
		 *
		 * @return the list data provider
		 */
		ListDataProvider<QualityDataSetDTO> getListDataProvider();

		/**
		 * Gets the simple pager.
		 *
		 * @return the simple pager
		 */
		MatSimplePager getSimplePager();

		/**
		 * Gets the celltable.
		 *
		 * @return the celltable
		 */
		CellTable<QualityDataSetDTO> getCelltable();

		/**
		 * Gets the pager.
		 *
		 * @return the pager
		 */
		MatSimplePager getPager();

		/**
		 * Gets the update vsac error message panel.
		 *
		 * @return the update vsac error message panel
		 */
		ErrorMessageDisplay getUpdateVSACErrorMessagePanel();

		/**
		 * Gets the update vsac success message panel.
		 *
		 * @return the update vsac success message panel
		 */
		SuccessMessageDisplay getUpdateVSACSuccessMessagePanel();
	}

	/** The panel. */
	SimplePanel panel = new SimplePanel();

	/** The search display. */
	SearchDisplay searchDisplay;

	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();

	/** The vsacapi service. */
	private final VSACAPIServiceAsync vsacapiService = MatContext.get()
			.getVsacapiServiceAsync();
	
	/** The is u ser defined. */
	private boolean isUSerDefined = false;
	
	/** The applied qdm list. */
	private List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	
	/** The current mat value set. */
	private MatValueSet currentMatValueSet;
	
	/** The exp profile to all qdm. */
	private String expProfileToAllQDM = "";
	
	/** The is modfied. */
	private boolean isModified = false;
	
	/** The modify value set dto. */
	private QualityDataSetDTO modifyValueSetDTO;
	
	/** The is expansion profile. */
	private boolean isExpansionProfile = false;

	/**
	 * Instantiates a new VSAC profile selection presenter.
	 * 
	 * @param srchDisplay
	 *            the srch display
	 */
	public QDMAppliedSelectionPresenter(SearchDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		MatContext.get().getAllDataType();
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
					new AsyncCallback<QualityDataModelWrapper>() {

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
								final QualityDataModelWrapper result) {
							QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
							filterTimingQDMs(result.getQualityDataDTO());
							appliedListModel.setAppliedQDMs(result.getQualityDataDTO());
							appliedQDMList = result.getQualityDataDTO();
							searchDisplay.buildAppliedQDMCellTable(
									appliedListModel, MatContext.get().getMeasureLockService()
											.checkForEditPermission());
							//if UMLS is not logged in 
							if (!MatContext.get().isUMLSLoggedIn()) {
								if(result.getVsacProfile()!=null){
									searchDisplay.getVSACExpansionProfileListBox().setEnabled(false);
									searchDisplay.getVSACExpansionProfileListBox().clear();
								    searchDisplay.getVSACExpansionProfileListBox().addItem(result.getVsacProfile());
									CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getVSACProfileInput();
									chkBox.setValue(true);
									chkBox.setEnabled(false);
									isExpansionProfile = true;
									expProfileToAllQDM = result.getVsacProfile();
								} else {
									expProfileToAllQDM = "";
									isExpansionProfile = false;
								}
							} else {
								if(result.getVsacProfile()!=null){
									searchDisplay.getVSACExpansionProfileListBox().setEnabled(true);
									searchDisplay.setProfileList(MatContext.get()
											.getProfileList());
									searchDisplay.setVSACExpansionProfileListBox();
									for(int i = 0; i < searchDisplay.getVSACExpansionProfileListBox().getItemCount(); i++){
										if(searchDisplay.getVSACExpansionProfileListBox().getItemText(i)
												.equalsIgnoreCase(result.getVsacProfile())) {
											searchDisplay.getVSACExpansionProfileListBox().setSelectedIndex(i);
											break;
										}
									}
									CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getVSACProfileInput();
									chkBox.setValue(true);
									chkBox.setEnabled(true);
									expProfileToAllQDM = result.getVsacProfile();
									isExpansionProfile = true;
								} else {
									expProfileToAllQDM = "";
									isExpansionProfile = false;
								}
							}
							
						}
					});
		}
	}

	/**
	 * Save measure xml.
	 *
	 * @param qdmXMLString the qdm xml string
	 */
	private void saveMeasureXML(final String qdmXMLString) {
		final String nodeName = "qdm";
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setParentNode("/measure/elementLookUp");
		exportModal.setToReplaceNode("qdm");
		System.out.println("XML " + qdmXMLString);
		exportModal.setXml(qdmXMLString);
		
		service.appendAndSaveNode(exportModal, nodeName,
				new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(final Void result) {
				getAppliedQDMList(true);
			}
		});
	}
	
	/**
	 * Save measure xml.
	 *
	 * @param list            the list
	 * @param indexOf the index of
	 */
	private void deleteAndSaveMeasureXML(final List<QualityDataSetDTO> list , final int indexOf) {
		service.createAndSaveElementLookUp(list, MatContext.get()
				.getCurrentMeasureId(), expProfileToAllQDM, new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
			}

			@Override
			public void onSuccess(final Void result) {
				searchDisplay.getCelltable().setVisibleRangeAndClearData(searchDisplay
						.getCelltable().getVisibleRange(), true);
				searchDisplay.getListDataProvider().getList().remove(indexOf);
				if(searchDisplay.getListDataProvider().getList().size()>0){
					searchDisplay.getListDataProvider().refresh();
					searchDisplay.getPager().setPageStart(searchDisplay.getCelltable().getVisibleRange().getStart(), 
							searchDisplay.getListDataProvider().getList().size());	
				} else {
					searchDisplay.buildAppliedQDMCellTable(new QDSAppliedListModel(), isModified);
				}
 				
				searchDisplay.getSuccessMessageDisplay().setMessage(
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
	private void resetQDSMsgPanel() {
		searchDisplay.getSuccessMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getUpdateVSACSuccessMessagePanel().clear();
		searchDisplay.getUpdateVSACErrorMessagePanel().clear();
	}


	/**
	 * Search value set in vsac.
	 *
	 * @param version            the version
	 * @param expansionProfile the expansion profile
	 */
	private void searchValueSetInVsac(String version, String expansionProfile) {
       
		final String oid = searchDisplay.getOIDInput().getValue();
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
				expansionProfile, new AsyncCallback<VsacApiResult>() {
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
						//to get the VSAC version list corresponding the OID
						if(result.isSuccess()){
							List<MatValueSet> matValueSets = result.getVsacResponse();
							if (matValueSets != null) {
								MatValueSet matValueSet = matValueSets.get(0);
								currentMatValueSet = matValueSet;
								}
							searchDisplay.getUserDefinedInput().setValue(matValueSets.get(0).getDisplayName());
							searchDisplay.getVSACProfileListBox().setEnabled(true);
							searchDisplay.getVersionListBox().setEnabled(true);
							searchDisplay.getDataTypesListBox().setEnabled(true);
							searchDisplay.getSpecificOccChkBox().setEnabled(true);
							searchDisplay.getSpecificOccChkBox().setValue(false);
							getVSACVersionListByOID(oid);
							} else {
								String message = convertMessage(result.getFailureReason());
								searchDisplay.getErrorMessageDisplay().setMessage(message);
							}
						showSearchingBusy(false);
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
							searchDisplay.setVSACVersionListBoxOptions(getVersionList(result
									.getVsacVersionResp()));
							searchDisplay.setVSACProfileListBox(getProfileList(
									MatContext.get().getVsacProfList()));
							searchDisplay.getSaveButton().setEnabled(true);

							if(searchDisplay.getDataTypeText(
									searchDisplay.getDataTypesListBox()).equalsIgnoreCase("attribute")) {
								searchDisplay.getSpecificOccChkBox().setEnabled(false);
							} else {
								searchDisplay.getSpecificOccChkBox().setEnabled(true);
							}
							if(isExpansionProfile){
								searchDisplay.getVSACProfileListBox().setEnabled(false);
								searchDisplay.getVersionListBox().setEnabled(false);
								searchDisplay.getVSACProfileListBox().clear();
								searchDisplay.getVSACProfileListBox().addItem(expProfileToAllQDM, 
										expProfileToAllQDM);
							} else {
								searchDisplay.getVSACProfileListBox().setEnabled(true);
								searchDisplay.getVersionListBox().setEnabled(true);
							}
							
//							if(!expProfileToAllQDM.isEmpty()){
//								searchDisplay.getVSACProfileListBox().clear();
//								searchDisplay.getVSACProfileListBox().addItem(expProfileToAllQDM, 
//										expProfileToAllQDM);
//							}
							
						}
					}
					@Override
					public void onFailure(Throwable caught) {
                       searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate()
						.getVSAC_RETRIEVE_FAILED());
					}
				});

	}
	
//	private void populateVersionAndProfileList(QualityDataSetDTO modifyDTO){
//	
//		if(modifyDTO!=null){
//			if(modifyDTO.getVersion()!=null){
//				for(int i = 0; i < searchDisplay.getVersionListBox().getItemCount(); i++){
//					if(searchDisplay.getVersionListBox().getItemText(i)
//							.equalsIgnoreCase(modifyDTO.getVersion())) {
//						searchDisplay.getVersionListBox().setSelectedIndex(i);
//						break;
//					}
//				}
//			} 
//			
//			if(modifyDTO.getExpansionProfile()!=null){
//				for(int i = 0; i < searchDisplay.getVSACProfileListBox().getItemCount(); i++){
//					if(searchDisplay.getVSACProfileListBox().getItemText(i)
//							.equalsIgnoreCase(modifyDTO.getExpansionProfile())) {
//						searchDisplay.getVSACProfileListBox().setSelectedIndex(i);
//						break;
//					}
//				}
//			}
//		}
//	}
	
	/**
	 * Gets the profile list.
	 *
	 * @param list the list
	 * @return the profile list
	 */
	private List<? extends HasListBox> getProfileList(List<VSACProfile> list){
		return list;
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
							searchDisplay.getVSACExpansionProfileListBox().setEnabled(
									true);
							searchDisplay.setProfileList(MatContext.get()
									.getProfileList());
							searchDisplay.setVSACExpansionProfileListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getVSACExpansionProfileListBox().setEnabled(
									false);
							searchDisplay.setVSACExpansionProfileListBox();
						}

					}
				});
		
		searchDisplay.getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetQDSMsgPanel();
				isModified = false;
				searchDisplay.getSearchHeader().setText("Search");
				searchDisplay.getOIDInput().setEnabled(true);
				searchDisplay.getOIDInput().setValue("");
				searchDisplay.getUserDefinedInput().setEnabled(true);
				searchDisplay.getUserDefinedInput().setValue("");
				searchDisplay.getVSACProfileListBox().clear();
				searchDisplay.getVersionListBox().clear();
				searchDisplay.getDataTypesListBox().setSelectedIndex(0);
				searchDisplay.getSpecificOccChkBox().setValue(false);
				searchDisplay.getVSACProfileListBox().setEnabled(false);
				searchDisplay.getVersionListBox().setEnabled(false);
				searchDisplay.getSpecificOccChkBox().setEnabled(false);
				searchDisplay.getDataTypesListBox().setEnabled(false);
				
			}
		});
		
		searchDisplay.getApplyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// code for adding profile to List to applied QDM
				resetQDSMsgPanel();
				if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
					// Login
					// Validation
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getUMLS_NOT_LOGGEDIN());
					return;
					}
				searchDisplay.getSearchHeader().setText("Search");
				CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getVSACProfileInput();
				if(!searchDisplay.getVSACExpansionProfileListBox().getValue().equalsIgnoreCase("--Select--")){
					expProfileToAllQDM = searchDisplay.getVSACExpansionProfileListBox().getValue();
					updateAllQDMsWithExpProfile();
				} else if(!chkBox.getValue()){
					expProfileToAllQDM = "";
					updateAllQDMsWithExpProfile();
				} else {
					searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getVsacExpansionProfileSelection());
				}
				
			}
		});
		
		searchDisplay.getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetQDSMsgPanel();
				String version = null;
				String expansionProfile = null;
				searchValueSetInVsac(version, expansionProfile);
			}
		});
		
		searchDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				resetQDSMsgPanel();
				if(isModified){
					modifyQDM(isUSerDefined);
				} else {
					addSelectedCodeListtoMeasure(isUSerDefined);
				}
				
			}
		});
		
		searchDisplay.getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getInProgressMessageDisplay().setMessage("Loading Please Wait...");
				resetQDSMsgPanel();
				updateVSACValueSets();
				
			}
		});
		
		searchDisplay.getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				validateUserDefinedInput();
			}
		});
		
      searchDisplay.getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				validateOIDInput();
			}
		});
		
		searchDisplay.setObserver(new QDMAppliedSelectionView.Observer() {

			@Override
			public void onModifyClicked(QualityDataSetDTO result) {
				resetQDSMsgPanel();
				isModified = true;
				modifyValueSetDTO = result;
				String displayName = null;
				if ((result.getOccurrenceText() != null)
						&& !result.getOccurrenceText().equals("")) {
					displayName = result.getOccurrenceText() + " of "
							+ result.getCodeListName();
				} else {
					displayName = result.getCodeListName();
				}

				searchDisplay.getSearchHeader().setText("Modify Applied QDM ( "+displayName
						+" : "+result.getDataType()+" )");
				
				if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
					isUSerDefined = true;
					//set UserDefined value in Modify Panel
					searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
					searchDisplay.getUserDefinedInput().setEnabled(true);
//					searchDisplay.getOIDInput().setValue(result.getOid());
					searchDisplay.getVersionListBox().setEnabled(false);
					searchDisplay.getVSACProfileListBox().setEnabled(false);
					searchDisplay.getRetrieveFromVSACButton().setEnabled(false);
					searchDisplay.getSpecificOccChkBox().setEnabled(false);
					searchDisplay.getSaveButton().setEnabled(true);
					searchDisplay.getOIDInput().setEnabled(false);
					searchDisplay.getOIDInput().setValue("");
					searchDisplay.getVersionListBox().clear();
					searchDisplay.getVSACProfileListBox().clear();
				
				} else {
					
					//set OID Based QDM's in Modify Panel
					isUSerDefined = false;
					searchDisplay.getUserDefinedInput().setEnabled(false);
					searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
					searchDisplay.getOIDInput().setEnabled(true);
					searchDisplay.getOIDInput().setValue(result.getOid());
					searchDisplay.getSaveButton().setEnabled(false);
					searchDisplay.getSpecificOccChkBox().setEnabled(true);
					
					searchDisplay.getSpecificOccChkBox().setValue(result.isSpecificOccurrence());
					searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
					searchDisplay.getVersionListBox().clear();
					searchDisplay.getVSACProfileListBox().clear(); 
					searchDisplay.getVSACProfileListBox().setEnabled(false);
					searchDisplay.getVersionListBox().setEnabled(false);
//					if(result.getExpansionProfile()!=null){
//						searchDisplay.getVersionListBox().setEnabled(false);
//						searchDisplay.getVSACProfileListBox().addItem(result.getExpansionProfile(), 
//								result.getExpansionProfile());
//					} else if (result.getVersion()!=null) {
//						searchDisplay.getVersionListBox().setEnabled(true);
//						searchDisplay.getVSACProfileListBox().setEnabled(false);
//						if(result.getVersion().equals("1.0") || result.getVersion().equals("1")){
//							//searchDisplay.getVersionListBox().setValue("Most Recent");
//							searchDisplay.getVersionListBox().addItem("Most Recent", "Most Recent");
//						} else {
//							searchDisplay.getVersionListBox().addItem(result.getVersion(), result.getVersion());
//						}
//					}
					
					
						
//					} else {
//						searchDisplay.getVSACProfileListBox().setEnabled(true);
//					}
				}
				
				searchDisplay.getDataTypesListBox().setEnabled(true);
				for(int i = 0; i < searchDisplay.getDataTypesListBox().getItemCount(); i++){
					if(searchDisplay.getDataTypesListBox().getItemText(i)
							.equalsIgnoreCase(result.getDataType())) {
						searchDisplay.getDataTypesListBox().setSelectedIndex(i);
						break;
					}
				}
				
				if(!expProfileToAllQDM.isEmpty()){
					searchDisplay.getVSACProfileListBox().clear();
					searchDisplay.getVSACProfileListBox().addItem(expProfileToAllQDM, 
							expProfileToAllQDM);
				}
			}

			@Override
			public void onDeleteClicked(QualityDataSetDTO result, final int index) {
				resetQDSMsgPanel();
				service.getAppliedQDMFromMeasureXml(MatContext.get()
						.getCurrentMeasureId(), false,
						new AsyncCallback<QualityDataModelWrapper>() {

							@Override
							public void onSuccess(QualityDataModelWrapper result) {
								appliedQDMList = result.getQualityDataDTO();
								if (appliedQDMList.size() > 0) {
									Iterator<QualityDataSetDTO> iterator = appliedQDMList
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
									deleteAndSaveMeasureXML(appliedQDMList, index);
								}

							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});

			}

		});
		
		searchDisplay.getDataTypesListBox().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				if(searchDisplay.getDataTypeText(
						searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
					searchDisplay.getSaveButton().setEnabled(false);
				} else if(searchDisplay.getDataTypeText(
						searchDisplay.getDataTypesListBox()).equalsIgnoreCase("attribute")){
					searchDisplay.getSpecificOccChkBox().setEnabled(false);
					searchDisplay.getSaveButton().setEnabled(true);
				} else {
					searchDisplay.getSaveButton().setEnabled(true);
				}
			}
		});
		
		searchDisplay.getVSACProfileListBox().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				if(!searchDisplay.getExpansionProfileValue(
						searchDisplay.getVSACProfileListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				   searchDisplay.getVersionListBox().setSelectedIndex(0);
				}
			}
			
		});
		
		 searchDisplay.getVersionListBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				if(!searchDisplay.getVersionValue(
						searchDisplay.getVersionListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				   searchDisplay.getVSACProfileListBox().setSelectedIndex(0);
				}
				
			}
		});
	}
	
	
	/**
	 * Update all qd ms with exp profile.
	 */
	private void updateAllQDMsWithExpProfile() {
		List<QualityDataSetDTO> modifiedQDMList = new ArrayList<QualityDataSetDTO>();
		for (QualityDataSetDTO qualityDataSetDTO : appliedQDMList) {
			if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
				qualityDataSetDTO.setVersion("1.0");
				if(!expProfileToAllQDM.isEmpty()){
					qualityDataSetDTO.setExpansionProfile(expProfileToAllQDM);
					}
				CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getVSACProfileInput();
				if(chkBox.getValue()){
					modifiedQDMList.add(qualityDataSetDTO);
				}
				}
			}
		updateAllInMeasureXml(modifiedQDMList);
	}
	

	
	
	/**
	 * Update all in measure xml.
	 *
	 * @param modifiedQDMList the modified qdm list
	 */
	private void updateAllInMeasureXml(List<QualityDataSetDTO> modifiedQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.updateMeasureXMLForExpansionProfile(modifiedQDMList, measureId, expProfileToAllQDM, 
				new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				
				getAppliedQDMList(true);
				searchDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
						.getMessageDelegate().getVsacProfileAppliedToQdmElements());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	/**
	 * Validate user defined input.
	 */
	private void validateUserDefinedInput(){
		if(searchDisplay.getUserDefinedInput().getValue().length()>0){
			isUSerDefined = true;
			searchDisplay.getOIDInput().setEnabled(true);	
			searchDisplay.getVSACProfileListBox().setEnabled(false);
			searchDisplay.getVersionListBox().setEnabled(false);
			searchDisplay.getSpecificOccChkBox().setEnabled(false);
			searchDisplay.getDataTypesListBox().setEnabled(true);
			searchDisplay.getRetrieveFromVSACButton().setEnabled(false);
		} else {
			isUSerDefined = false;
			searchDisplay.getOIDInput().setEnabled(true);
			searchDisplay.getDataTypesListBox().setEnabled(false);
			searchDisplay.getDataTypesListBox().setSelectedIndex(0);
			searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
			}
	}
	
	
	/**
	 * Validate oid input.
	 */
	private void validateOIDInput(){
		if (searchDisplay.getOIDInput().getValue().length()>0) {
			isUSerDefined = false;
			searchDisplay.getUserDefinedInput().setEnabled(false);
			searchDisplay.getSaveButton().setEnabled(false);
		} else if(searchDisplay.getUserDefinedInput().getValue().length()>0){
			isUSerDefined = true;
			searchDisplay.getSpecificOccChkBox().setEnabled(false);
			searchDisplay.getUserDefinedInput().setEnabled(true);
		} else {
			searchDisplay.getUserDefinedInput().setEnabled(true);	
		}
	}
	

	/**
	 * Adds the selected code listto measure.
	 *
	 * @param isUserDefinedQDM the is user defined qdm
	 */
	private void addSelectedCodeListtoMeasure(final boolean isUserDefinedQDM) {
		if (!isUserDefinedQDM) {
			addQDSWithValueSet();
		} else {
			addQDSWithOutValueSet();
		}
	}
	
	/**
	 * Adds the qds with out value set.
	 */
	private void addQDSWithOutValueSet() {
		String userDefinedInput = searchDisplay.getUserDefinedInput().getText().trim();
		boolean isValidUserDefinedInput = QDMAvailableValueSetPresenter.validateUserDefinedInput(userDefinedInput);
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(
						searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			if(isValidUserDefinedInput){
				String dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
				MatValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(dataType, false,
						MatContext.get().getCurrentMeasureId());
				MatContext.get().getCodeListService().saveUserDefinedQDStoMeasure(
						matValueSetTransferObject, new AsyncCallback<SaveUpdateCodeListResult>() {
							@Override
							public void onFailure(final Throwable caught) {
								Window.alert(MatContext.get()
										.getMessageDelegate()
										.getGenericErrorMessage());
							}
							
							@SuppressWarnings("static-access")
							@Override
							public void onSuccess(
									final SaveUpdateCodeListResult result) {
								if (result.getXmlString() != null) {
									saveMeasureXML(result.getXmlString());
									String message = MatContext
											.get()
											.getMessageDelegate()
											.getQDMSuccessMessage(
													searchDisplay
													.getUserDefinedInput()
													.getText(),
													searchDisplay
													.getDataTypeText(searchDisplay
															.getDataTypesListBox()));
									searchDisplay
									.getSuccessMessageDisplay()
									.setMessage(message);
									searchDisplay.getUserDefinedInput()
									.setText("");
									searchDisplay.getDataTypesListBox()
									.setSelectedIndex(0);
									
								} else if (result.getFailureReason() == result.ALREADY_EXISTS) {
									searchDisplay
									.getErrorMessageDisplay()
									.setMessage(
											MatContext
											.get()
											.getMessageDelegate()
											.getDuplicateAppliedQDMMsg());
								}
							}
						});
			} else {
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getINVALID_CHARACTER_VALIDATION_ERROR());
			}
			
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
					.getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
			
	}
	
	
	/**
	 * Adds the qds with value set.
	 */
	private void addQDSWithValueSet() {
		final String dataType;
		final String dataTypeText;
		final boolean isSpecificOccurrence;
		dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
		
		if (searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox()).equalsIgnoreCase("--Select--")) {
			dataTypeText = dataType;
		} else {
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
		}
		
		isSpecificOccurrence = searchDisplay.getSpecificOccChkBox().getValue();
		String measureID = MatContext.get().getCurrentMeasureId();
		if (!dataType.isEmpty() && !dataType.equals("")) {
			
			MatValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(dataType, isSpecificOccurrence,
					measureID);
			MatContext.get().getCodeListService().saveQDStoMeasure(matValueSetTransferObject, new AsyncCallback<SaveUpdateCodeListResult>() {

				@Override
				public void onFailure(Throwable caught) {
					if (appliedQDMList.size() > 0) {
						appliedQDMList
						.removeAll(appliedQDMList);
					}
				}
				
				@Override
				public void onSuccess(SaveUpdateCodeListResult result) {
					String message = "";
					if (result.getXmlString() != null) {
						saveMeasureXML(result.getXmlString());
					}
					searchDisplay.getSpecificOccChkBox().getValue();
					if (result.isSuccess()) {
						if ((result.getOccurrenceMessage() != null)
								&& !result
								.getOccurrenceMessage()
								.equals("")) {
							message = MatContext
									.get()
									.getMessageDelegate()
									.getQDMOcurrenceSuccessMessage(
											currentMatValueSet
											.getDisplayName(),
											dataTypeText,
											result.getOccurrenceMessage());
						} else {
							message = MatContext
									.get()
									.getMessageDelegate()
									.getQDMSuccessMessage(
											currentMatValueSet
											.getDisplayName(),
											dataTypeText);
						}
						MatContext.get()
						.getEventBus().fireEvent(
								new QDSElementCreatedEvent(
										currentMatValueSet
										.getDisplayName()));
						searchDisplay.getSearchHeader().setText("Search");
						searchDisplay.getOIDInput().setEnabled(true);
						searchDisplay.getOIDInput().setValue("");
						searchDisplay.getUserDefinedInput().setEnabled(true);
						searchDisplay.getUserDefinedInput().setValue("");
						searchDisplay.getVSACProfileListBox().clear();
						searchDisplay.getVersionListBox().clear();
						searchDisplay.getDataTypesListBox().setSelectedIndex(0);
						searchDisplay.getSpecificOccChkBox().setValue(false);
						searchDisplay.getVSACProfileListBox().setEnabled(false);
						searchDisplay.getVersionListBox().setEnabled(false);
						searchDisplay.getSpecificOccChkBox().setEnabled(false);
						searchDisplay.getDataTypesListBox().setEnabled(false);
						searchDisplay
						.getSuccessMessageDisplay()
						.setMessage(message);
					} else {
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate()
									.getDuplicateAppliedQDMMsg());
						}
					}
					
				}
			});
		}
	}
	
	
	/**
	 * Creates the value set transfer object.
	 *
	 * @param dataType the data type
	 * @param isSpecificOccurrence the is specific occurrence
	 * @param measureID the measure id
	 * @return the mat value set transfer object
	 */
	private MatValueSetTransferObject createValueSetTransferObject(final String dataType, final boolean isSpecificOccurrence,
			String measureID) {
		String version = searchDisplay.getVersionValue(searchDisplay.getVersionListBox());
		String expansionProfile = searchDisplay.getExpansionProfileValue(
				searchDisplay.getVSACProfileListBox());
		MatValueSetTransferObject matValueSetTransferObject = new MatValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setDatatype(dataType);
		matValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		matValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if(version != null || expansionProfile != null ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersionDate(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getVSACProfileListBox().getValue());
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersionDate(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getVersionListBox().getValue());
			}
		}
		
		
		if (!expProfileToAllQDM.isEmpty() && !isUSerDefined) {
			currentMatValueSet.setExpansionProfile(expProfileToAllQDM);
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersionDate(false);
		}
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}
	
	
	/**
	 * Modify qdm.
	 *
	 * @param isUserDefined the is user defined
	 */
	protected final void modifyQDM(final boolean isUserDefined) {
		if (!isUserDefined) { //Normal Available QDM Flow
			modifyValueSetQDM();
		} else { //Pseudo QDM Flow
			modifyQDMWithOutValueSet();
		}
	}
	
	/**
	 * Server call to modify QDM without VSAC value set.
	 */
	private void modifyQDMWithOutValueSet() {
		//Pseudo QDM Flow
		//resetQDSMsgPanel();
		String userDefinedInput = searchDisplay.getUserDefinedInput().getText().trim();
		boolean isValidUserDefinedInput = validateUserDefinedInput(userDefinedInput);
		modifyValueSetDTO.setExpansionProfile("");
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox()).
				equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			if(isValidUserDefinedInput){
				CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
				modifyWithDTO.setName(searchDisplay.getUserDefinedInput().getText());
				String dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
				String dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
				if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
						|| dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
					if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
						updateAppliedQDMList(null, modifyWithDTO, modifyValueSetDTO, dataType, false, true);
					} else {
						if (ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataTypeText)) {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().
									getMessageDelegate().getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION());
						} else {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().
									getMessageDelegate().getMODIFY_QDM_ATTRIBUTE_VALIDATION());
						}
					}
				} else {
					updateAppliedQDMList(null, modifyWithDTO, modifyValueSetDTO, dataType, false, true);
				}
			}else{
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getINVALID_CHARACTER_VALIDATION_ERROR());
			}
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}
	
	/**
	 * Update applied qdm list.
	 *
	 * @param matValueSet the mat value set
	 * @param codeListSearchDTO the code list search dto
	 * @param qualityDataSetDTO the quality data set dto
	 * @param dataType the data type
	 * @param isSpecificOccurrence the is specific occurrence
	 * @param isUSerDefined the is u ser defined
	 */
	private void updateAppliedQDMList(final MatValueSet matValueSet , final CodeListSearchDTO codeListSearchDTO ,
			final QualityDataSetDTO qualityDataSetDTO, final String dataType, final Boolean isSpecificOccurrence,
			final boolean isUSerDefined) {
		modifyQDMList(qualityDataSetDTO);
		String version = searchDisplay.getVersionValue(searchDisplay.getVersionListBox());
		String expansionProfile = searchDisplay.getExpansionProfileValue(
				searchDisplay.getVSACProfileListBox());
		MatValueSetTransferObject matValueSetTransferObject = new MatValueSetTransferObject();
		matValueSetTransferObject.setDatatype(dataType);
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		matValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if(version != null || expansionProfile != null ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersionDate(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getVSACProfileListBox().getValue());
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersionDate(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getVersionListBox().getValue());
			}
		}
		
		if(!expProfileToAllQDM.isEmpty()){
			currentMatValueSet.setExpansionProfile(expProfileToAllQDM);
			currentMatValueSet.setVersion("1.0");
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersionDate(false);
		}
		MatContext.get().getCodeListService().updateCodeListToMeasure(matValueSetTransferObject,
				new AsyncCallback<SaveUpdateCodeListResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				if (!isUSerDefined) {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				} else {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
			}
			@Override
			public void onSuccess(final SaveUpdateCodeListResult result) {
				if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
					if (!isUSerDefined) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					} else {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					}
				} else {
					isModified = false;
					appliedQDMList = result.getAppliedQDMList();
					updateMeasureXML(result.getDataSetDTO() , qualityDataSetDTO, isUSerDefined);
				}
			}
		});
		
	}
	
	/**
	 * Validate user defined input.
	 *
	 * @param userDefinedInput the user defined input
	 * @return true, if successful
	 */
	public static boolean validateUserDefinedInput(String userDefinedInput) {
		boolean flag = true;
		for(int i = 0; i< userDefinedInput.length(); i++){
			if((userDefinedInput.charAt(i) == '+')
					|| (userDefinedInput.charAt(i) == '*')
					|| (userDefinedInput.charAt(i) == '?')
					|| (userDefinedInput.charAt(i) == ':')
					|| (userDefinedInput.charAt(i) == '-')
					|| (userDefinedInput.charAt(i) == '|')
					|| (userDefinedInput.charAt(i) == '!')
					|| (userDefinedInput.charAt(i) == '"')
					|| (userDefinedInput.charAt(i) == ';')
					|| (userDefinedInput.charAt(i) == '%')){
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * Update measure xml.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param modifyableDTO the modifyable dto
	 * @param isUserDefined the is user defined
	 */
	private void updateMeasureXML(final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyableDTO, final boolean isUserDefined) {
		    service.updateMeasureXML(modifyWithDTO, modifyableDTO,
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				if (!isUserDefined) {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				} else {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
			}
			
			@Override
			public void onSuccess(final Void result) {
				searchDisplay.getSuccessMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
				modifyValueSetDTO = modifyWithDTO;
				getAppliedQDMList(true);
			}
		});
	}

	
	/**
	 * Modify qdm list.
	 *
	 * @param qualityDataSetDTO the quality data set dto
	 */
	private void modifyQDMList(QualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedQDMList.size(); i++) {
			if (qualityDataSetDTO.getOid().equals(appliedQDMList.get(i).getOid())
					&& qualityDataSetDTO.getDataType().equals(appliedQDMList.get(i).getDataType())) {
				if ((qualityDataSetDTO.getOccurrenceText() != null)
						&& (appliedQDMList.get(i).getOccurrenceText() != null)) {
					if (qualityDataSetDTO.getOccurrenceText().equals(
							appliedQDMList.get(i).getOccurrenceText())) {
						appliedQDMList.remove(i);
						break;
					}
				} else if ((qualityDataSetDTO.getOccurrenceText() == null)
						&& (appliedQDMList.get(i).getOccurrenceText() == null)) {
					appliedQDMList.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Server call to modify QDM with VSAC value set.
	 */
	private void modifyValueSetQDM() {
		//Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			String dataType;
			String dataTypeText;
			Boolean isSpecificOccurrence = false;
			
			dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
			isSpecificOccurrence = searchDisplay.getSpecificOccChkBox().getValue();
			if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
					|| dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
				
				if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
					if (modifyWithDTO.getID().equalsIgnoreCase(modifyValueSetDTO.getOid())
							&& (modifyValueSetDTO.isSpecificOccurrence() && isSpecificOccurrence)) {
						searchDisplay.getSuccessMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					} else {
						updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
					}
				} else {
					if (ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataTypeText)) {
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().
								getMessageDelegate().getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION()
								);
					} else {
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().
								getMessageDelegate().getMODIFY_QDM_ATTRIBUTE_VALIDATION());
					}
				}
				
			} else {
				if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())
						&& modifyWithDTO.getID().equalsIgnoreCase(modifyValueSetDTO.getOid())
						&& (modifyValueSetDTO.isSpecificOccurrence() && isSpecificOccurrence)){
					searchDisplay.getSuccessMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
				} else {
					updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
				}
			}
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().
					getMessageDelegate().getMODIFY_QDM_SELECT_ATLEAST_ONE());
		}
	}
	
	
	/**
	 * Gets the version list.
	 *
	 * @param list the list
	 * @return the version list
	 */
	private List<? extends HasListBox> getVersionList(List<VSACVersion> list) {
		return list;
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
		return panel;
	}

	/**
	 * Display search.
	 */
	public void displaySearch() {
		panel.clear();
		resetQDSMsgPanel();
		setWidgetsReadOnly(MatContext.get().getMeasureLockService()
				.checkForEditPermission());
		searchDisplay.resetVSACValueSetWidget();
		populateAllDataType();
		getAppliedQDMList(true);
		setWidgetToDefault();
		panel.add(searchDisplay.asWidget());
	}
	
	/**
	 * Sets the widget to default.
	 */
	private void setWidgetToDefault() {
		searchDisplay.getVersionListBox().clear();
		searchDisplay.getVSACProfileListBox().clear();
		searchDisplay.getOIDInput().setValue("");
		searchDisplay.getUserDefinedInput().setValue("");
		CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getSpecificOccChkBox();
		chkBox.setEnabled(false);
		chkBox.setValue(false);
		//will enable the apply button once done 
		//with the functionality
		//searchDisplay.getApplyButton().setEnabled(false);
	}

	/**
	 * Populate all data type.
	 */
	private void populateAllDataType() {
		MatContext
		.get()
		.getListBoxCodeProvider()
		.getAllDataType(
				new AsyncCallback<List<? extends HasListBox>>() {
					
					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay
						.getErrorMessageDisplay()
						.setMessage(
								MatContext
								.get()
								.getMessageDelegate()
								.getGenericErrorMessage());
					}
					
					@Override
					public void onSuccess(
							final List<? extends HasListBox> result) {
						Collections.sort(result,
								new HasListBox.Comparator());
						searchDisplay
						.setDataTypesListBoxOptions(result);
					}
				});
	}

	
	/**
	 * Sets the widgets read only.
	 *
	 * @param editable the new widgets read only
	 */
	private void setWidgetsReadOnly(boolean editable){
		searchDisplay.getVSACProfileListBox().setEnabled(editable);
		searchDisplay.getVersionListBox().setEnabled(editable);
		searchDisplay.getDataTypesListBox().setEnabled(editable);
		searchDisplay.getOIDInput().setEnabled(editable);
		searchDisplay.getUserDefinedInput().setEnabled(editable);
		searchDisplay.getApplyButton().setEnabled(editable);
		searchDisplay.getCancelQDMButton().setEnabled(editable);
		searchDisplay.getRetrieveFromVSACButton().setEnabled(editable);
		searchDisplay.getSaveButton().setEnabled(editable);
		searchDisplay.getUpdateFromVSACButton().setEnabled(editable);
		CustomCheckBox chkBox = (CustomCheckBox)searchDisplay.getVSACProfileInput();
		chkBox.setEnabled(editable);
		CustomCheckBox specificChkBox = (CustomCheckBox)searchDisplay.getSpecificOccChkBox();
		specificChkBox.setEnabled(editable);
	}
	
	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		vsacapiService.updateVSACValueSets(MatContext.get().getCurrentMeasureId(), new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getInProgressMessageDisplay().clear();
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(final VsacApiResult result) {
				searchDisplay.getInProgressMessageDisplay().clear();
				if (result.isSuccess()) {
					searchDisplay.getUpdateVSACSuccessMessagePanel().setMessage(
							MatContext.get().getMessageDelegate().getVSAC_UPDATE_SUCCESSFULL());
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					appliedListModel.setAppliedQDMs(result.getUpdatedQualityDataDTOLIst());
					searchDisplay.buildAppliedQDMCellTable(appliedListModel, MatContext.get().getMeasureLockService()
							.checkForEditPermission()
							);
				} else {
					searchDisplay.getUpdateVSACErrorMessagePanel().setMessage(convertMessage(result.getFailureReason()));
				}
			}
		});
	}
	
	//
	/**
	 * Show searching busy.
	 *
	 * @param busy the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
	}

}
