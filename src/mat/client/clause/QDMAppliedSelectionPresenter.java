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
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.QDMInputValidator;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.MatTextBox;
import mat.model.CodeListSearchDTO;
import mat.model.GlobalCopyPasteObject;
import mat.model.MatValueSet;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.VSACExpansionIdentifier;
import mat.model.VSACVersion;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;
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
import com.google.gwt.user.client.ui.VerticalPanel;
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
		HasValueChangeHandlers<Boolean> getDefaultExpIDInput();
		
		/**
		 * Gets the VSAC profile list box.
		 * 
		 * @return the VSAC profile list box
		 */
		ListBoxMVP getVSACExpansionIdentifierListBox();
		
		/**
		 * Sets the vsac profile list box.
		 */
		void setDefaultExpansionIdentifierListBox();
		
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
		void setExpIdentifierList(List<String> profileList);
		
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
		Button getApplyDefaultExpansionIdButton();
		
		/**
		 * Gets the retrieve from vsac button.
		 *
		 * @return the retrieve from vsac button
		 */
		PrimaryButton getRetrieveFromVSACButton();
		
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
		ListBoxMVP getQDMExpIdentifierListBox();
		
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
		void setQDMVersionListBoxOptions(List<? extends HasListBox> texts);
		
		/**
		 * Sets the VSAC profile list box options.
		 *
		 * @param texts the new VSAC profile list box options
		 */
		void setQDMExpIdentifierListBox(List<? extends HasListBox> texts);
		
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
		CheckBox getSpecificOccChkBox();
		
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
		String getExpansionIdentifierValue(ListBoxMVP inputListBox);
		
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
		
		/**
		 * Gets the main panel.
		 *
		 * @return the main panel
		 */
		VerticalPanel getMainPanel();
		
		/**
		 * Gets the QDM copy top button.
		 *
		 * @return the QDM copy top button
		 */
		CustomButton getQDMCopyTopButton();
		
		/**
		 * Gets the QDM paste top button.
		 *
		 * @return the QDM paste top button
		 */
		CustomButton getQDMPasteTopButton();
		
		/**
		 * Gets the QDM clear top button.
		 *
		 * @return the QDM clear top button
		 */
		CustomButton getQDMClearTopButton();
		
		/**
		 * Clear qdm check boxes.
		 */
		void clearQDMCheckBoxes();
		
		/**
		 * Gets the qdm selected list.
		 *
		 * @return the qdm selected list
		 */
		List<QualityDataSetDTO> getQdmSelectedList();
		
		/**
		 * Gets the QDM copy bottom button.
		 *
		 * @return the QDM copy bottom button
		 */
		CustomButton getQDMCopyBottomButton();
		
		/**
		 * Gets the QDM paste bottom button.
		 *
		 * @return the QDM paste bottom button
		 */
		CustomButton getQDMPasteBottomButton();
		
		/**
		 * Gets the QDM clear bottom button.
		 *
		 * @return the QDM clear bottom button
		 */
		CustomButton getQDMClearBottomButton();
		
		/**
		 * Builds the paste top panel.
		 *
		 * @param isEditable the is editable
		 * @return the widget
		 */
		Widget buildPasteTopPanel(boolean isEditable);
		
		/**
		 * Builds the paste bottom panel.
		 *
		 * @param isEditable the is editable
		 * @return the widget
		 */
		Widget buildPasteBottomPanel(boolean isEditable);
		
		ToggleSwitch getToggleSwitch();
		
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
	private boolean isUserDefined = false;
	
	/** The applied qdm list. */
	private List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	
	/** The current mat value set. */
	private MatValueSet currentMatValueSet;
	
	/** The exp profile to all qdm. */
	private String expIdentifierToAllQDM = "";
	
	/** The is modfied. */
	private boolean isModified = false;
	
	/** The modify value set dto. */
	private QualityDataSetDTO modifyValueSetDTO;
	
	/** The is expansion profile. */
	private boolean isExpansionIdentifier = false;
	
	/** The is all oi ds updated. */
	private boolean  isAllOIDsUpdated = false;
	
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
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(false);
							searchDisplay.getVSACExpansionIdentifierListBox().clear();
							searchDisplay.getVSACExpansionIdentifierListBox().addItem(result.getVsacExpIdentifier());
							//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
							//ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
							searchDisplay.getToggleSwitch().setValue(true);
							searchDisplay.getToggleSwitch().setEnabled(false);
							isExpansionIdentifier = true;
							expIdentifierToAllQDM = result.getVsacExpIdentifier();
						} else {
							expIdentifierToAllQDM = "";
							isExpansionIdentifier = false;
						}
					} else {
						if(result.getVsacExpIdentifier()!=null){
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(true);
							searchDisplay.setExpIdentifierList(MatContext.get()
									.getExpIdentifierList());
							searchDisplay.setDefaultExpansionIdentifierListBox();
							for(int i = 0; i < searchDisplay.getVSACExpansionIdentifierListBox().getItemCount(); i++){
								if(searchDisplay.getVSACExpansionIdentifierListBox().getItemText(i)
										.equalsIgnoreCase(result.getVsacExpIdentifier())) {
									searchDisplay.getVSACExpansionIdentifierListBox().setSelectedIndex(i);
									break;
								}
							}
							//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
							//ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
							searchDisplay.getToggleSwitch().setEnabled(true);
							searchDisplay.getToggleSwitch().setValue(true);
							
							expIdentifierToAllQDM = result.getVsacExpIdentifier();
							isExpansionIdentifier = true;
						} else {
							searchDisplay.getToggleSwitch().setEnabled(true);
							expIdentifierToAllQDM = "";
							isExpansionIdentifier = false;
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
	private void saveMeasureXML(final String qdmXMLString, final String valuesetXMLString) {
		final String nodeName = "qdm";
		final String newNodeName = "valueset";
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setParentNode("/measure/elementLookUp");
		exportModal.setToReplaceNode("qdm");
		MeasureXmlModel newExportModal = new MeasureXmlModel();
		newExportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		newExportModal.setParentNode("/measure/cqlLookUp/valuesets");
		newExportModal.setToReplaceNode("valueset");
		
		System.out.println("XML " + qdmXMLString);
		exportModal.setXml(qdmXMLString);
		System.out.println("NEW XML " + valuesetXMLString);
		newExportModal.setXml(valuesetXMLString);
		
		
		service.appendAndSaveNode(exportModal, nodeName, newExportModal, newNodeName,
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
				.getCurrentMeasureId(), expIdentifierToAllQDM, new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(final Void result) {
				modifyValueSetDTO = null;
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
						MatContext.get().getMessageDelegate().getSUCCESSFUL_QDM_REMOVE_MSG());
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
	 * Resets the  message panel.
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
		showSearchingBusy(true);
		
		if(expIdentifierToAllQDM.isEmpty()){
			expansionProfile = null;
		} else {
			expansionProfile = expIdentifierToAllQDM;
		}
		
		vsacapiService.getMostRecentValueSetByOID(oid,
				expansionProfile, new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getVSAC_RETRIEVE_FAILED());
				showSearchingBusy(false);
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
					searchDisplay.getOIDInput().setTitle(oid);
					searchDisplay.getUserDefinedInput().setValue(matValueSets.get(0).getDisplayName());
					searchDisplay.getUserDefinedInput().setTitle(matValueSets.get(0).getDisplayName());
					searchDisplay.getQDMExpIdentifierListBox().setEnabled(true);
					searchDisplay.getVersionListBox().setEnabled(true);
					searchDisplay.getDataTypesListBox().setEnabled(true);
					searchDisplay.getSpecificOccChkBox().setEnabled(true);
					searchDisplay.getSpecificOccChkBox().setValue(false);
					
					searchDisplay.getSaveButton().setEnabled(true);
					
					if(searchDisplay.getDataTypeText(
							searchDisplay.getDataTypesListBox()).equalsIgnoreCase("attribute")) {
						searchDisplay.getSpecificOccChkBox().setEnabled(false);
					} else {
						searchDisplay.getSpecificOccChkBox().setEnabled(true);
					}
					if(isExpansionIdentifier){
						searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
						searchDisplay.getVersionListBox().setEnabled(false);
						searchDisplay.getQDMExpIdentifierListBox().clear();
						searchDisplay.getQDMExpIdentifierListBox().addItem(expIdentifierToAllQDM,
								expIdentifierToAllQDM);
					} else {
						searchDisplay.setQDMExpIdentifierListBox(getProfileList(
								MatContext.get().getVsacExpIdentifierList()));
						getVSACVersionListByOID(oid);
						searchDisplay.getQDMExpIdentifierListBox().setEnabled(true);
						searchDisplay.getVersionListBox().setEnabled(true);
					}
					showSearchingBusy(false);
					searchDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getVSAC_RETRIEVAL_SUCCESS());
					
				} else {
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getErrorMessageDisplay().setMessage(message);
					showSearchingBusy(false);
				}
			}
		});
	}
	
	/**
	 * Gets the VSAC version list by oid.
	 * if the default Expansion Identifier is present then
	 * we are not making this VSAC call.
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
					searchDisplay.setQDMVersionListBoxOptions(getVersionList(result
							.getVsacVersionResp()));
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate()
						.getVSAC_RETRIEVE_FAILED());
				showSearchingBusy(false);
			}
		});
		
	}
	
	
	/**
	 * Gets the profile list.
	 *
	 * @param list the list
	 * @return the profile list
	 */
	private List<? extends HasListBox> getProfileList(List<VSACExpansionIdentifier> list){
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
	 * Add all the handlers.
	 */
	private void addAllHandlers() {
		
		addSearchPanelHandlers();
		addExpIdentifierHandlers();
		addUpdateFromVSACHandler();
		addObserverHandler();
		
		
		searchDisplay.getQDMClearTopButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clear();
			}
		});
		
		//Global Copying of QDM Elements
		searchDisplay.getQDMCopyTopButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				copy();
			}
		});
		
		searchDisplay.getQDMClearBottomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clear();
			}
		});
		
		//Global Copying of QDM Elements
		searchDisplay.getQDMCopyBottomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				copy();
			}
		});
		
	}
	
	/**
	 * Adding observer  for Modify, Delete, Copy, Paste and Clear buttons.
	 */
	
	private void addObserverHandler() {
		
		searchDisplay.setObserver(new QDMAppliedSelectionView.Observer() {
			
			/**
			 * this functionality is used to modify the existing QDM Element with
			 *  and without value set.
			 *  depending on the type of QDM Element the fields in the search Panel
			 *  are disabled for User defined and Value Set QDM elements.
			 * */
			
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
				searchDisplay.getMainPanel().getElement().focus();
				if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
					isUserDefined = true;
					//set UserDefined value in Modify Panel
					onModifyUserDefined(result);
					
				} else {
					
					//set OID Based QDM's in Modify Panel
					isUserDefined = false;
					onModifyValueSetQDM(result);
				}
				
				/**
				 * to display dataType value in the selection list box
				 */
				for(int i = 0; i < searchDisplay.getDataTypesListBox().getItemCount(); i++){
					if(searchDisplay.getDataTypesListBox().getItemText(i)
							.equalsIgnoreCase(result.getDataType())) {
						searchDisplay.getDataTypesListBox().setSelectedIndex(i);
						break;
					}
				}
				
				/**
				 * if the datatype value is "attribute" then we are disabling specific occurrence
				 * checkbox field on the Search Panel and enabling it for other datatypes.
				 * */
				if(searchDisplay.getDataTypeText(
						searchDisplay.getDataTypesListBox()).equalsIgnoreCase("attribute")){
					searchDisplay.getSpecificOccChkBox().setEnabled(false);
					searchDisplay.getSpecificOccChkBox().setValue(false);
				}
				
			}
			
			
			/**
			 * this functionality is to delete the existing QDM element
			 * from the Applied QDM elements
			 * */
			@Override
			public void onDeleteClicked(QualityDataSetDTO result, final int index) {
				resetQDSMsgPanel();
				if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
					isModified = false;
				}
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
						
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}
				});
				
			}
			
			@Override
			public void onTopQDMPasteClicked() {
				paste();
			}
			
			@Override
			public void onBottomQDMPasteClicked() {
				paste();
			}
			
		});
		
	}
	
	/**
	 * To update all the value sets in the applied QDM elements from VSAC.
	 * */
	private void addUpdateFromVSACHandler() {
		
		searchDisplay.getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					searchDisplay.getInProgressMessageDisplay().setMessage("Loading Please Wait...");
					resetQDSMsgPanel();
					updateVSACValueSets();

				}
			}
		});
		
	}
	
	/**
	 * Adds the search panel handlers.
	 */
	private void addSearchPanelHandlers() {
		
		/**
		 * this functionality is to clear the content on the Search Panel.
		 * */
		searchDisplay.getCancelQDMButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				resetQDSMsgPanel();
				isModified = false;
				searchDisplay.getSearchHeader().setText("Search");
				searchDisplay.getOIDInput().setEnabled(true);
				searchDisplay.getOIDInput().setValue("");
				searchDisplay.getOIDInput().setTitle("Enter OID");
				searchDisplay.getUserDefinedInput().setEnabled(true);
				searchDisplay.getUserDefinedInput().setTitle("Enter Name");
				searchDisplay.getUserDefinedInput().setValue("");
				searchDisplay.getQDMExpIdentifierListBox().clear();
				searchDisplay.getVersionListBox().clear();
				searchDisplay.getDataTypesListBox().setSelectedIndex(0);
				searchDisplay.getSpecificOccChkBox().setValue(false);
				searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
				searchDisplay.getVersionListBox().setEnabled(false);
				searchDisplay.getSpecificOccChkBox().setEnabled(false);
				searchDisplay.getDataTypesListBox().setEnabled(false);
				searchDisplay.getSaveButton().setEnabled(false);
				
			}
		});
		
		/**
		 * this functionality is to retrieve the value set from VSAC with latest information which
		 * consists of Expansion Identifier list and Version List.
		 * */
		searchDisplay.getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					resetQDSMsgPanel();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
				}
			}
		});
		
		
		/**
		 * this handler is invoked when apply button is clicked on search Panel in
		 * QDM elements tab and this is to add new value set or user Defined QDM to the
		 * Applied QDM list.
		 * */
		searchDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					MatContext.get().clearDVIMessages();
					resetQDSMsgPanel();
					if(isModified && (modifyValueSetDTO != null)){
						modifyQDM(isUserDefined);
					} else {
						addSelectedCodeListtoMeasure(isUserDefined);
					}

				}
			}
		});
		
		
		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in QDM Elements Tab
		 * 
		 * */
		searchDisplay.getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				validateUserDefinedInput();
			}
		});
		
		/**
		 *  Adding value change handler for OID input in Search Panel in QDM elements Tab
		 */
		
		searchDisplay.getOIDInput().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				validateOIDInput();
			}
		});
		
		/**
		 * value change Handler for DataType Listbox in Search Panel in QDM Elemetns Tab
		 * depending on the selection of the listbox we are enabling and disabling the
		 * fields in Search Panel.
		 * */
		
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
					searchDisplay.getSpecificOccChkBox().setValue(false);
					searchDisplay.getSaveButton().setEnabled(true);
				} else {
					searchDisplay.getSaveButton().setEnabled(true);
					searchDisplay.getSpecificOccChkBox().setValue(false);
					if(!isUserDefined){
						searchDisplay.getSpecificOccChkBox().setEnabled(true);
					}
				}
			}
		});
		
		
		/**
		 * value change handler for Expansion Identifier in Search Panel in QDM Elements Tab
		 * */
		searchDisplay.getQDMExpIdentifierListBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				if(!searchDisplay.getExpansionIdentifierValue(
						searchDisplay.getQDMExpIdentifierListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
					searchDisplay.getVersionListBox().setSelectedIndex(0);
				}
			}
			
		});
		
		/**
		 * value Change Handler for Version listBox in Search Panel
		 * */
		searchDisplay.getVersionListBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				resetQDSMsgPanel();
				if(!searchDisplay.getVersionValue(
						searchDisplay.getVersionListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
					searchDisplay.getQDMExpIdentifierListBox().setSelectedIndex(0);
				}
			}
		});
	}
	
	
	/**
	 * click Handlers for ExpansioN Identifier Panel in new QDM Elements Tab.
	 */
	private void addExpIdentifierHandlers() {
		searchDisplay.getApplyDefaultExpansionIdButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					// code for adding profile to List to applied QDM
					resetQDSMsgPanel();
					if (!MatContext.get().isUMLSLoggedIn()) { // UMLS
						// Login
						// Validation
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getUMLS_NOT_LOGGEDIN());
						return ;
					}
					searchDisplay.getSearchHeader().setText("Search");
					//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
					//ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
					if(!searchDisplay.getVSACExpansionIdentifierListBox().getValue().equalsIgnoreCase("--Select--")){
						expIdentifierToAllQDM = searchDisplay.getVSACExpansionIdentifierListBox().getValue();
						updateAllQDMsWithExpProfile(appliedQDMList);
					} else if(!searchDisplay.getToggleSwitch().getValue()){
						expIdentifierToAllQDM = "";
						updateAllQDMsWithExpProfile(appliedQDMList);
					} else {
						searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getVsacExpansionIdentifierSelection());
					}
				}
			}
		});
		
		/**
		 * click handler for check box in apply Expansion Identifier Panel to fetch Expansion
		 * Identifier List when Umls is active.
		 */
		/*searchDisplay.getDefaultExpIDInput().addValueChangeHandler(
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
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(
									true);
							searchDisplay.setExpIdentifierList(MatContext.get()
									.getExpIdentifierList());
							searchDisplay.setDefaultExpansionIdentifierListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(
									false);
							searchDisplay.setDefaultExpansionIdentifierListBox();
						}
						
					}
				});*/
		searchDisplay.getToggleSwitch().addValueChangeHandler(
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
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(
									true);
							searchDisplay.setExpIdentifierList(MatContext.get()
									.getExpIdentifierList());
							searchDisplay.setDefaultExpansionIdentifierListBox();
						} else if (event.getValue().toString().equals("false")) {
							searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(
									false);
							searchDisplay.setDefaultExpansionIdentifierListBox();
						}
						
					}
				});
		
		searchDisplay.getQDMClearBottomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clear();
			}
		});
		
		//Global Copying of QDM Elements
		searchDisplay.getQDMCopyBottomButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				copy();
			}
		});
	}
	
	/**
	 * Update Expansion Identifier in Default Four SDE's.
	 */
	protected void updateAllSuppleDataElementsWithExpIdentifier(final List<QualityDataSetDTO> modifiedQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.getDefaultSDEFromMeasureXml(measureId, new AsyncCallback<QualityDataModelWrapper>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			@Override
			public void onSuccess(QualityDataModelWrapper result) {
				for (QualityDataSetDTO qualityDataSetDTO : result.getQualityDataDTO()) {
					if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
						qualityDataSetDTO.setVersion("1.0");
						qualityDataSetDTO.setExpansionIdentifier(expIdentifierToAllQDM);
						modifiedQDMList.add(qualityDataSetDTO);
					}
				}
				updateAllInMeasureXml(modifiedQDMList);
			}
		});
	}
	
	/**
	 * Update all applied QDM Elements with default Expansion Identifier.
	 *
	 * @param list the list
	 */
	private void updateAllQDMsWithExpProfile(List<QualityDataSetDTO> list) {
		List<QualityDataSetDTO> modifiedQDMList = new ArrayList<QualityDataSetDTO>();
		for (QualityDataSetDTO qualityDataSetDTO : list) {
			if (!ConstantMessages.USER_DEFINED_QDM_OID.equalsIgnoreCase(qualityDataSetDTO.getOid())) {
				qualityDataSetDTO.setVersion("1.0");
				if (!expIdentifierToAllQDM.isEmpty()) {
					qualityDataSetDTO.setExpansionIdentifier(expIdentifierToAllQDM);
				}
				//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox) searchDisplay.getDefaultExpIDInput();
				//ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
				if (searchDisplay.getToggleSwitch().getValue()) {
					modifiedQDMList.add(qualityDataSetDTO);
				}
			}
		}
		//Updating all SDE
		updateAllSuppleDataElementsWithExpIdentifier(modifiedQDMList);
	}
	
	
	
	/**
	 * Update all in measure xml.
	 *
	 * @param modifiedQDMList the modified qdm list
	 */
	private void updateAllInMeasureXml(List<QualityDataSetDTO> modifiedQDMList) {
		String measureId =  MatContext.get().getCurrentMeasureId();
		service.updateMeasureXMLForExpansionIdentifier(modifiedQDMList, measureId, expIdentifierToAllQDM,
				new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				getAppliedQDMList(true);
				//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
				//ToggleSwitch useDefaultExpToggelSwitch = searchDisplay.getToggleSwitch();
				if (!searchDisplay.getToggleSwitch().getValue()) {
					searchDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getDefaultExpansionIdRemovedMessage());
					
				} else {
					searchDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getVsacProfileAppliedToQdmElements());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				
			}
		});
	}
	
	
	
	
	/**
	 * Validate user defined input.
	 * In this functionality we are disabling all the fields in Search Panel
	 * except Name and DataType Selection List Box which are required to create new
	 * UserDefined QDM Element.
	 */
	private void validateUserDefinedInput(){
		if(searchDisplay.getUserDefinedInput().getValue().length()>0){
			isUserDefined = true;
			searchDisplay.getOIDInput().setEnabled(true);
			searchDisplay.getUserDefinedInput().setTitle(searchDisplay.getUserDefinedInput().getValue());
			searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
			searchDisplay.getVersionListBox().setEnabled(false);
			searchDisplay.getSpecificOccChkBox().setEnabled(false);
			searchDisplay.getDataTypesListBox().setEnabled(true);
			searchDisplay.getRetrieveFromVSACButton().setEnabled(false);
			searchDisplay.getSaveButton().setEnabled(true);
		} else {
			isUserDefined = false;
			searchDisplay.getUserDefinedInput().setTitle("Enter Name");
			searchDisplay.getOIDInput().setEnabled(true);
			searchDisplay.getDataTypesListBox().setEnabled(false);
			searchDisplay.getDataTypesListBox().setSelectedIndex(0);
			searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
			searchDisplay.getSaveButton().setEnabled(false);
		}
	}
	
	
	/**
	 * Validate oid input.
	 * depending on the OID input we are disabling and
	 * enabling the fields in Search Panel
	 */
	private void validateOIDInput(){
		if (searchDisplay.getOIDInput().getValue().length()>0) {
			isUserDefined = false;
			searchDisplay.getUserDefinedInput().setEnabled(false);
			searchDisplay.getSaveButton().setEnabled(false);
			searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
		} else if(searchDisplay.getUserDefinedInput().getValue().length()>0){
			isUserDefined = true;
			searchDisplay.getSpecificOccChkBox().setEnabled(false);
			searchDisplay.getSpecificOccChkBox().setValue(false);
			searchDisplay.getQDMExpIdentifierListBox().clear();
			searchDisplay.getVersionListBox().clear();
			searchDisplay.getUserDefinedInput().setEnabled(true);
			if(!searchDisplay.getDataTypeText(
					searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				searchDisplay.getSaveButton().setEnabled(true);
			}
		} else {
			searchDisplay.getUserDefinedInput().setEnabled(true);
		}
	}
	
	
	/**
	 * Adds the selected code list to measure.
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
		
		MatValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(null, false,
				MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.scrubForMarkUp();
		
		if ((matValueSetTransferObject.getUserDefinedText().length() > 0)
				&& !searchDisplay.getDataTypeText(
						searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			QDMInputValidator qdmInputValidator = new QDMInputValidator();
			List<String> messList = qdmInputValidator.validate(matValueSetTransferObject);
			if (messList.size() == 0) {
				String dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
				matValueSetTransferObject.setDatatype(dataType);
				final String userDefinedInput = matValueSetTransferObject.getUserDefinedText();
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
								if(result.isSuccess()) {
									if (result.getXmlString() != null && result.getnewXmlString() != null) {
										saveMeasureXML(result.getXmlString(), result.getnewXmlString());
										String message = MatContext
												.get()
												.getMessageDelegate()
												.getQDMSuccessMessage(
														userDefinedInput,
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
										
									}
								} else {
									if (result.getFailureReason() == result.ALREADY_EXISTS) {
										searchDisplay
										.getErrorMessageDisplay()
										.setMessage(
												MatContext
												.get()
												.getMessageDelegate()
												.getDuplicateAppliedQDMMsg());
									} else if (result.getFailureReason() == result.SERVER_SIDE_VALIDATION) {
										searchDisplay
										.getErrorMessageDisplay()
										.setMessage("Invalid input data.");
									}
								}
							}
						});
			} else {
				searchDisplay.getErrorMessageDisplay().setMessages(messList);
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
			matValueSetTransferObject.scrubForMarkUp();
			final String codeListName = matValueSetTransferObject.getMatValueSet().getDisplayName();
			MatContext.get().getCodeListService().saveQDStoMeasure(
					matValueSetTransferObject, new AsyncCallback<SaveUpdateCodeListResult>() {
						
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
							if (result.getXmlString() != null && result.getnewXmlString() != null) {
								saveMeasureXML(result.getXmlString(), result.getnewXmlString());
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
													codeListName,
													dataTypeText,
													result.getOccurrenceMessage());
								} else {
									message = MatContext
											.get()
											.getMessageDelegate()
											.getQDMSuccessMessage(
													codeListName,
													dataTypeText);
								}
								MatContext.get()
								.getEventBus().fireEvent(
										new QDSElementCreatedEvent(
												codeListName));
								resetQDMSearchPanel();
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
		} else {
			searchDisplay
			.getErrorMessageDisplay()
			.setMessage(MatContext.get().getMessageDelegate()
					.getVALIDATION_MSG_DATA_TYPE_VSAC());
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
		String expansionProfile = searchDisplay.getExpansionIdentifierValue(
				searchDisplay.getQDMExpIdentifierListBox());
		MatValueSetTransferObject matValueSetTransferObject = new MatValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setDatatype(dataType);
		matValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		matValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if((version != null) || (expansionProfile != null) ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersionDate(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getQDMExpIdentifierListBox().getValue());
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersionDate(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getVersionListBox().getValue());
			}
		}
		
		
		if (!expIdentifierToAllQDM.isEmpty() && !isUserDefined) {
			currentMatValueSet.setExpansionProfile(expIdentifierToAllQDM);
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
		modifyValueSetDTO.setExpansionIdentifier("");
		modifyValueSetDTO.setVersion("");
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox()).
				equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			MatValueSetTransferObject object = new MatValueSetTransferObject();
			object.setUserDefinedText(searchDisplay.getUserDefinedInput().getText());
			object.scrubForMarkUp();
			QDMInputValidator qdmInputValidator = new QDMInputValidator();
			List<String> meStrings = qdmInputValidator.validate(object);
			if (meStrings.size() == 0) {
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
			} else {
				searchDisplay.getErrorMessageDisplay().setMessages(meStrings);
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
		String expansionProfile = searchDisplay.getExpansionIdentifierValue(
				searchDisplay.getQDMExpIdentifierListBox());
		MatValueSetTransferObject matValueSetTransferObject = new MatValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(MatContext.get().getCurrentMeasureId());
		matValueSetTransferObject.setDatatype(dataType);
		matValueSetTransferObject.setMatValueSet(matValueSet);
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setQualityDataSetDTO(qualityDataSetDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		matValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if((version != null) || (expansionProfile != null) ){
			if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !expansionProfile.equalsIgnoreCase("")) {
				matValueSetTransferObject.setExpansionProfile(true);
				matValueSetTransferObject.setVersionDate(false);
				currentMatValueSet.setExpansionProfile(searchDisplay
						.getQDMExpIdentifierListBox().getValue());
				
			} else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
					&& !version.equalsIgnoreCase("")){
				matValueSetTransferObject.setVersionDate(true);
				matValueSetTransferObject.setExpansionProfile(false);
				currentMatValueSet.setVersion(searchDisplay.getVersionListBox().getValue());
			}
		}
		
		if(!expIdentifierToAllQDM.isEmpty() && !isUSerDefined){
			currentMatValueSet.setExpansionProfile(expIdentifierToAllQDM);
			currentMatValueSet.setVersion("1.0");
			matValueSetTransferObject.setExpansionProfile(true);
			matValueSetTransferObject.setVersionDate(false);
		}
		matValueSetTransferObject.scrubForMarkUp();
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
				
				if(result.isSuccess()){
					isModified = false;
					appliedQDMList = result.getAppliedQDMList();
					isAllOIDsUpdated = result.isAllOIDsUpdated();
					updateMeasureXML(result.getDataSetDTO() , qualityDataSetDTO, isUSerDefined);
					resetQDMSearchPanel();
				} else{
					
					if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
						if (!isUSerDefined) {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
						} else {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
						}
					} else if (result.getFailureReason() == SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION) {
						searchDisplay.getErrorMessageDisplay().setMessage("Invalid Input data.");
					}
				}
			}
		});
		
	}
	
	
	/**
	 * Reset qdm search panel.
	 */
	private void resetQDMSearchPanel(){
		searchDisplay.getSearchHeader().setText("Search");
		searchDisplay.getOIDInput().setEnabled(true);
		searchDisplay.getOIDInput().setValue("");
		searchDisplay.getUserDefinedInput().setEnabled(true);
		searchDisplay.getUserDefinedInput().setValue("");
		searchDisplay.getQDMExpIdentifierListBox().clear();
		searchDisplay.getVersionListBox().clear();
		searchDisplay.getDataTypesListBox().setSelectedIndex(0);
		searchDisplay.getSpecificOccChkBox().setValue(false);
		searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
		searchDisplay.getVersionListBox().setEnabled(false);
		searchDisplay.getSpecificOccChkBox().setEnabled(false);
		searchDisplay.getDataTypesListBox().setEnabled(false);
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
				List<String> messages = new ArrayList<String>();
				messages.add(MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
				if(isAllOIDsUpdated){
					messages.add(MatContext.get().getMessageDelegate().getSUCCESSFULLY_MODIFIED_ALL_OIDS());
				}
				searchDisplay.getSuccessMessageDisplay().setMessages(messages);
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
			String expansionId;
			String version;
			Boolean isSpecificOccurrence = false;
			
			dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
			expansionId = searchDisplay.getExpansionIdentifierValue(searchDisplay.getQDMExpIdentifierListBox());
			version = searchDisplay.getVersionValue(searchDisplay.getVersionListBox());
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
					if (!expansionId.isEmpty() && expansionId.equalsIgnoreCase(
							modifyValueSetDTO.getExpansionIdentifier())) {
						resetQDMSearchPanel();
						searchDisplay.getSuccessMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					} else if(!version.isEmpty() && version.equalsIgnoreCase(
							modifyValueSetDTO.getVersion())){
						resetQDMSearchPanel();
						searchDisplay.getSuccessMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					} else if((modifyValueSetDTO.getVersion().equals("1.0") ||
							modifyValueSetDTO.getVersion().equals("1"))
							&& expansionId.isEmpty() && version.isEmpty()
							&& (modifyValueSetDTO.getExpansionIdentifier() == null)){
						resetQDMSearchPanel();
						searchDisplay.getSuccessMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					} else {
						updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
					}
					
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
		searchDisplay.clearQDMCheckBoxes();
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
		searchDisplay.getQDMExpIdentifierListBox().clear();
		searchDisplay.getOIDInput().setValue("");
		searchDisplay.getUserDefinedInput().setValue("");
		CheckBox chkBox = searchDisplay.getSpecificOccChkBox();
		chkBox.setEnabled(false);
		chkBox.setValue(false);
		searchDisplay.getSaveButton().setEnabled(false);
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
		searchDisplay.getQDMExpIdentifierListBox().setEnabled(editable);
		searchDisplay.getVersionListBox().setEnabled(editable);
		searchDisplay.getDataTypesListBox().setEnabled(editable);
		searchDisplay.getOIDInput().setEnabled(editable);
		searchDisplay.getUserDefinedInput().setEnabled(editable);
		searchDisplay.getApplyDefaultExpansionIdButton().setEnabled(editable);
		
		searchDisplay.getCancelQDMButton().setEnabled(editable);
		searchDisplay.getRetrieveFromVSACButton().setEnabled(editable);
		searchDisplay.getSaveButton().setEnabled(editable);
		searchDisplay.getUpdateFromVSACButton().setEnabled(editable);
		//CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
		//ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
		//searchDisplay.getToggleSwitch().setEnabled(editable);
		CheckBox specificChkBox = searchDisplay.getSpecificOccChkBox();
		specificChkBox.setEnabled(editable);
		searchDisplay.buildPasteBottomPanel(editable);
		searchDisplay.buildPasteTopPanel(editable);
		
	}
	
	/**
	 * Update vsac value sets.
	 */
	private void updateVSACValueSets() {
		
		String expansionId = null;
		if(expIdentifierToAllQDM.isEmpty()){
			expansionId = null;
		} else {
			expansionId = expIdentifierToAllQDM;
		}
		vsacapiService.updateVSACValueSets(MatContext.get().getCurrentMeasureId(), expansionId,
				new AsyncCallback<VsacApiResult>() {
			
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
							.checkForEditPermission());
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
		searchDisplay.getUpdateFromVSACButton().setEnabled(!busy);
		searchDisplay.getRetrieveFromVSACButton().setEnabled(!busy);
	}
	
	
	/**
	 * Copy. This is to copy selected QDM elements from Applied QDM Elements and can be
	 * available to paste it globally for multiple measures.
	 * the copied instance remains through out the session until or unless we make
	 * a new copy or the session is out.
	 */
	private void copy() {
		resetQDSMsgPanel();
		if(searchDisplay.getQdmSelectedList().size() > 0){
			mat.model.GlobalCopyPasteObject gbCopyPaste = new GlobalCopyPasteObject();
			gbCopyPaste.setCopiedQDMList(searchDisplay.getQdmSelectedList());
			gbCopyPaste.setCurrentMeasureId(MatContext.get().getCurrentMeasureId());
			MatContext.get().setGlobalCopyPaste(gbCopyPaste);
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getCOPY_QDM_SELECT_ATLEAST_ONE());
		}
	}
	
	
	/**
	 * Paste. This functionality is to paste all the QDM elements that have been copied
	 * from any Measure and can be pasted to any measure.
	 */
	private void paste() {
		
		resetQDSMsgPanel();
		GlobalCopyPasteObject gbCopyPaste = MatContext.get().getGlobalCopyPaste();
		if( (gbCopyPaste != null) && (gbCopyPaste.getCopiedQDMList().size()>0) ){
			gbCopyPaste.setMatValueSetListFromQDS(expIdentifierToAllQDM);
			MatContext.get().getCodeListService().saveCopiedQDMListToMeasure(gbCopyPaste, appliedQDMList,MatContext.get().getCurrentMeasureId(),
					new AsyncCallback<SaveUpdateCodeListResult>() {
				
				@Override
				public void onFailure(Throwable caught) {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
				
				@Override
				public void onSuccess(
						SaveUpdateCodeListResult result) {
					
					getAppliedQDMList(true);
					searchDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
							.getMessageDelegate().getSUCCESSFULLY_PASTED_QDM_ELEMENTS_IN_MEASURE());
				}
			});
			
		}
	}
	
	/**
	 * This functionality is to clear all the checkBoxes in Applied
	 * QDM Elements Cell Table.
	 */
	private void clear() {
		resetQDSMsgPanel();
		searchDisplay.clearQDMCheckBoxes();
	}
	
	
	/**
	 * On modify user defined.
	 *
	 * @param result the result
	 */
	private void onModifyUserDefined(QualityDataSetDTO result){
		
		searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
		searchDisplay.getUserDefinedInput().setTitle(result.getCodeListName());
		searchDisplay.getUserDefinedInput().setEnabled(true);
		searchDisplay.getVersionListBox().setEnabled(false);
		searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
		searchDisplay.getRetrieveFromVSACButton().setEnabled(false);
		searchDisplay.getSpecificOccChkBox().setEnabled(false);
		searchDisplay.getSaveButton().setEnabled(true);
		searchDisplay.getOIDInput().setEnabled(true);
		searchDisplay.getOIDInput().setValue("");
		searchDisplay.getVersionListBox().clear();
		searchDisplay.getQDMExpIdentifierListBox().clear();
		searchDisplay.getDataTypesListBox().setEnabled(true);
	}
	
	//
	/**
	 * On modify value set qdm.
	 *
	 * @param result the result
	 */
	private void onModifyValueSetQDM(QualityDataSetDTO result){
		
		searchDisplay.getUserDefinedInput().setEnabled(false);
		searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
		searchDisplay.getUserDefinedInput().setTitle(result.getCodeListName());
		searchDisplay.getOIDInput().setEnabled(true);
		searchDisplay.getOIDInput().setValue(result.getOid());
		searchDisplay.getOIDInput().setTitle(result.getOid());
		searchDisplay.getSaveButton().setEnabled(false);
		searchDisplay.getSpecificOccChkBox().setEnabled(true);
		searchDisplay.getDataTypesListBox().setEnabled(false);
		searchDisplay.getSpecificOccChkBox().setValue(result.isSpecificOccurrence());
		searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
		searchDisplay.getVersionListBox().clear();
		searchDisplay.getQDMExpIdentifierListBox().clear();
		searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
		searchDisplay.getVersionListBox().setEnabled(false);
		
		if(!expIdentifierToAllQDM.isEmpty()){
			searchDisplay.getQDMExpIdentifierListBox().clear();
			searchDisplay.getQDMExpIdentifierListBox().addItem(expIdentifierToAllQDM,
					expIdentifierToAllQDM);
		}
	}
	
}
