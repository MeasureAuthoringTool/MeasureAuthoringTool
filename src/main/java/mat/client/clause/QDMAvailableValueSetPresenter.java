package mat.client.clause;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataSetDTO;
import mat.model.MatValueSetTransferObject;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.Button;
import mat.vsacmodel.ValueSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QDMAvailableValueSetPresenter class.
 */
public class QDMAvailableValueSetPresenter  implements MatPresenter {
	
	/**
	 * QDMAvailableValueSetPresenter's view interface.
	 */
	interface SearchDisplay {
		/**
		 * As widget.
		 *
		 * @return {@link Widget}
		 */
		Widget asWidget();
		
		/**
		 * Builds the value set details widget.
		 *
		 * @param ValueSets
		 *            - ArrayList of {@link ValueSet}
		 */
		void buildValueSetDetailsWidget(List<ValueSet> ValueSets);
		
		/**
		 * Remove all Success and failure messages.
		 */
		void clearVSACValueSetMessages();
		
		/**
		 * Gets the all data type input.
		 *
		 * @return {@link ListBoxMVP}
		 */
		ListBoxMVP getAllDataTypeInput();
		
		/**
		 * Gets the apply to measure button.
		 *
		 * @return {@link Button}
		 */
		Button getApplyToMeasureButton();
		
		/**
		 * Gets the apply to measure success msg.
		 *
		 * @return {@link SuccessMessageDisplayInterface}
		 */
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		
		/**
		 * Gets the current mat value set.
		 *
		 * @return {@link ValueSet}
		 */
		ValueSet getCurrentValueSet();
		
		/**
		 * Gets the data types list box.
		 *
		 * @return {@link ListBoxMVP}
		 */
		ListBoxMVP getDataTypesListBox();
		
		/**
		 * Gets the data type text.
		 *
		 * @param inputListBox
		 *            - {@link ListBoxMVP}
		 * @return {@link String}
		 */
		String getDataTypeText(ListBoxMVP inputListBox);
		
		/**
		 * Gets the data type value.
		 *
		 * @param inputListBox
		 *            - {@link ListBoxMVP}
		 * @return {@link String}
		 */
		String getDataTypeValue(ListBoxMVP inputListBox);
		
		/**
		 * Gets the date input.
		 *
		 * @return the date input {@link DateBoxWithCalendar}
		 */
		DateBoxWithCalendar getDateInput();
		
		/**
		 * Gets the disclosure panel.
		 *
		 * @return {@link DisclosurePanel}
		 */
		DisclosurePanel getDisclosurePanel();
		
		/**
		 * Gets the disclosure panel mat.vsac.
		 *
		 * @return {@link DisclosurePanel}
		 */
		DisclosurePanel getDisclosurePanelVSAC();
		
		/**
		 * Gets the effective date.
		 *
		 * @return the effective date
		 */
		CustomCheckBox getEffectiveDate();
		
		/**
		 * Gets the error message display.
		 *
		 * @return {@link ErrorMessageDisplayInterface}
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the error message user defined panel.
		 *
		 * @return {@link ErrorMessageDisplay}
		 */
		ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		
		/**
		 * Gets the oID input.
		 *
		 * @return {@link TextBox}
		 */
		TextBox getOIDInput();
		
		/**
		 * Gets the psuedo qdm to measure.
		 *
		 * @return {@link Button}
		 */
		Button getPsuedoQDMToMeasure();
		
		/**
		 * Gets the retrieve button.
		 *
		 * @return {@link Button}
		 */
		Button getRetrieveButton();
		
		/**
		 * Gets the specific occurrence input.
		 *
		 * @return {@link CustomCheckBox}
		 */
		CustomCheckBox getSpecificOccurrenceInput();
		
		/**
		 * Gets the success message display.
		 *
		 * @return {@link SuccessMessageDisplay}
		 */
		SuccessMessageDisplay getSuccessMessageDisplay();
		
		/**
		 * Gets the success message user defined panel.
		 *
		 * @return {@link SuccessMessageDisplay}
		 */
		SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		
		/**
		 * Gets the user defined input.
		 *
		 * @return {@link TextBox}
		 */
		TextBox getUserDefinedInput();
		
		/**
		 * Gets the value set details panel.
		 *
		 * @return {@link VerticalPanel}
		 */
		VerticalPanel getValueSetDetailsPanel();
		
		/**
		 * Gets the version.
		 *
		 * @return the version
		 */
		CustomCheckBox getVersion();
		
		/**
		 * Reset VSACValueSetWidget - Clear's OID and version input's.
		 */
		void resetVSACValueSetWidget();
		
		/**
		 * Sets the all data type options.
		 *
		 * @param texts
		 *            List of {@link HasListBox}
		 */
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		
		/**
		 * Sets the data types list box options.
		 *
		 * @param texts
		 *            - {@link List} of {@link HasListBox}
		 */
		void setDataTypesListBoxOptions(List<? extends HasListBox> texts);
	}
	
	/**
	 * QualityDataSet List.
	 */
	private List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	
	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed.
	 * busyLoading is set true when retrieving value set from VSAC otherwise it is set false.
	 */
	private boolean busyLoading;
	/**
	 * ValueSet instance.
	 */
	private ValueSet currentValueSet;
	
	/**
	 * Measure Service instance.
	 */
	private final MeasureServiceAsync measureService = MatContext.get()
			.getMeasureService();
	
	/** The modify value set dto. {@link QualityDataSetDTO} instance. */
	private QualityDataSetDTO modifyValueSetDTO;
	
	/**
	 * The qds applied list presenter display. {@link QDSAppliedListPresenter}
	 * search display instance.
	 */
	private final mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay;
	/**
	 * SearchDisplay instance.
	 */
	private final SearchDisplay searchDisplay;
	
	/**
	 * Constructor.
	 * @param sDisplayArg
	 *            - {@link SearchDisplay}
	 * @param dataSetDTO
	 *            - {@link QualityDataSetDTO}
	 * @param qdsAppliedListPresenterDisplay
	 *            S-
	 *            {@link mat.client.clause.QDSAppliedListPresenter.SearchDisplay}
	 */
	public QDMAvailableValueSetPresenter(SearchDisplay sDisplayArg , QualityDataSetDTO dataSetDTO,
			final mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay) {
		searchDisplay = sDisplayArg;
		modifyValueSetDTO = dataSetDTO;
		this.qdsAppliedListPresenterDisplay = qdsAppliedListPresenterDisplay;
		appliedQDMList = (ArrayList<QualityDataSetDTO>) qdsAppliedListPresenterDisplay.getAllAppliedQDMList();
		
		//Element without VSAC value set - OPEN Handler
		searchDisplay.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				if (isBusyLoading()) {
					event.getTarget().setOpen(false);
				} else {
					displaySearch();
					searchDisplay.getDisclosurePanelVSAC().setOpen(false);
				}
			}
		});
		
		//Element without VSAC value set - CLOSE Handler
		searchDisplay.getDisclosurePanel().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				if (!isBusyLoading()) {
					searchDisplay.getUserDefinedInput().setText("");
					searchDisplay.getAllDataTypeInput().setItemSelected(0, true);
					displaySearch();
					searchDisplay.getDisclosurePanelVSAC().setOpen(true);
				}
			}
		});
		
		//Element with VSAC value set - OPEN Handler.
		searchDisplay.getDisclosurePanelVSAC().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				if (!isBusyLoading()) {
					displaySearch();
					searchDisplay.getDisclosurePanel().setOpen(false);
				}
			}
		});
		
		//Element with VSAC value set - CLOSE Handler.
		searchDisplay.getDisclosurePanelVSAC().addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				if (isBusyLoading()) {
					event.getTarget().setOpen(true);
				} else {
					searchDisplay.getUserDefinedInput().setText("");
					displaySearch();
					searchDisplay.getDisclosurePanel().setOpen(true);
				}
			}
		});
		
		searchDisplay.getUserDefinedInput().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});
		
		searchDisplay.getAllDataTypeInput().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(final FocusEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
				
			}
		});
		searchDisplay.getPsuedoQDMToMeasure().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				modifyQDM(true);
			}
		});
		
		searchDisplay.getApplyToMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				currentValueSet = searchDisplay.getCurrentValueSet();
				modifyQDM(false);
			}
		});
		
		searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.clearVSACValueSetMessages();
				String version = null;
				String effectiveDate = null;
				if (searchDisplay.getVersion().getValue().equals(Boolean.TRUE)) {
					version = searchDisplay.getDateInput().getValue();
				} else if (searchDisplay.getEffectiveDate().getValue().equals(Boolean.TRUE)) {
					effectiveDate = searchDisplay.getDateInput().getValue();
				}
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(), version, effectiveDate);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		displaySearch();
	}
	
	/**
	 * This method shows AvailableValueSet Widget in pop up.
	 * */
	private void displaySearch() {
		ModifyQDMDialogBox.showModifyDialogBox(searchDisplay.asWidget(), modifyValueSetDTO, this);
		populateAllDataType();
		searchDisplay.resetVSACValueSetWidget();
		searchDisplay.clearVSACValueSetMessages();
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
	}
	
	/**
	 * Filter timing qdms.
	 *
	 * @param result
	 *            - {@link ArrayList} of {@link QualityDataSetDTO}
	 */
	private void filterTimingQDMsAndUnsetDTOModfiyAttributes(
			List<QualityDataSetDTO> result) {
		List<QualityDataSetDTO> timingQDMs = new ArrayList<QualityDataSetDTO>();
		for (QualityDataSetDTO qdsDTO : result) {
			if ("Timing Element".equals(qdsDTO
					.getDataType()) || ConstantMessages.DEAD_OID.equals(qdsDTO
							.getDataType()) || ConstantMessages.BIRTHDATE_OID.equals(qdsDTO
									.getDataType()))  {
				timingQDMs.add(qdsDTO);
			} else {
				qdsDTO.setHasModifiedAtVSAC(false);
				qdsDTO.setNotFoundInVSAC(false);
			}
		}
		result.removeAll(timingQDMs);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return searchDisplay.asWidget();
	}
	
	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed.
	 * @return true if "Loading Please Wait..." message is displaying(In other words, when retrieving value set from VSAC)
	 * 	    else returns false;
	 */
	public final boolean isBusyLoading() {
		return busyLoading;
	}
	
	/**
	 * Method to find if selected Available value set is a valid modifiable selection.
	 *  If yes, then call to updateAppliedQDMList method is made.
	 * @param isUserDefined - Boolean.
	 **/
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
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		String userDefinedInput = searchDisplay.getUserDefinedInput().getText().trim();
		boolean isValidUserDefinedInput = validateUserDefinedInput(userDefinedInput);
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()).
				equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			if(isValidUserDefinedInput){
				CodeListSearchDTO modifyWithDTO = new CodeListSearchDTO();
				modifyWithDTO.setName(searchDisplay.getUserDefinedInput().getText());
				String dataType = searchDisplay.getDataTypeValue(searchDisplay.getAllDataTypeInput());
				String dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput());
				if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
						|| dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
					if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
						updateAppliedQDMList(null, modifyWithDTO, modifyValueSetDTO, dataType, false, true);
					} else {
						if (ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataTypeText)) {
							searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
									MatContext.get().
									getMessageDelegate().getMODIFY_QDM_NON_ATTRIBUTE_VALIDATION());
						} else {
							searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
									MatContext.get().
									getMessageDelegate().getMODIFY_QDM_ATTRIBUTE_VALIDATION());
						}
					}
				} else {
					updateAppliedQDMList(null, modifyWithDTO, modifyValueSetDTO, dataType, false, true);
				}
			}else{
				searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
						MatContext.get().getMessageDelegate()
						.getINVALID_CHARACTER_VALIDATION_ERROR());
			}
		} else {
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
					MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
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
	 * Server call to modify QDM with VSAC value set.
	 */
	private void modifyValueSetQDM() {
		//Normal Available QDM Flow
		ValueSet modifyWithDTO = currentValueSet;
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			String dataType;
			String dataTypeText;
			Boolean isSpecificOccurrence = false;
			
			dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
			isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
			
			if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
					|| dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
				if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
					if (modifyWithDTO.getID().equalsIgnoreCase(modifyValueSetDTO.getOid())
							&& (modifyValueSetDTO.isSpecificOccurrence() && isSpecificOccurrence)) {
						searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
								MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
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
					searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
							MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
				} else {
					updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
				}
			}
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().
					getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}
	
	/**
	 * Get All data types from DB and populates in AllDataTypeOptions and
	 * DataTypeListBoxOptions.
	 */
	private void populateAllDataType() {
		MatContext.get().getListBoxCodeProvider().getAllDataType(new AsyncCallback<List<? extends HasListBox>>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				
			}
			
			@Override
			public void onSuccess(final List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				searchDisplay.setAllDataTypeOptions(result);
				searchDisplay.setDataTypesListBoxOptions(result);
			}
		});
	}
	
	/**
	 * This method is used to reload Applied QDM List.
	 **/
	public final void reloadAppliedQDMList() {
		QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
		filterTimingQDMsAndUnsetDTOModfiyAttributes(appliedQDMList);
		appliedListModel.setAppliedQDMs(appliedQDMList);
		qdsAppliedListPresenterDisplay.buildCellTable(appliedListModel);
		
		/*
		 * Setting appliedQDMList in qdsAppliedListPresenterDisplay. Whenever
		 * this modify pop up is opened this.appliedQDMList is set with
		 * qdsAppliedListPresenterDisplay.appliedQDMList in this presenter. So,
		 * qdsAppliedListPresenterDisplay.appliedQDMList is updated here.
		 */
		qdsAppliedListPresenterDisplay.setAppliedQDMList(appliedQDMList);
	}
	
	/**
	 * Search value set in mat.vsac.
	 *
	 * @param oid
	 *            - {@link String}
	 * @param version
	 *            - {@link String}
	 * @param effectiveDate
	 *            - {@link String}
	 */
	private void searchValueSetInVsac(String oid, String version, String effectiveDate) {
		
		searchDisplay.getValueSetDetailsPanel().setVisible(false);
		
		if (!MatContext.get().isUMLSLoggedIn()) { //UMLS Login Validation
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			return;
		}
		
		//Version and EffectiveDate validation
		if ((searchDisplay.getVersion().getValue().equals(Boolean.TRUE)
				&& ((version == null) || version.trim().isEmpty()))
				|| (searchDisplay.getEffectiveDate().getValue().equals(Boolean.TRUE)
						&& ((effectiveDate == null) || effectiveDate.trim().isEmpty()))) {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate()
					.getVSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED());
			return;
		}
		
		/*showSearchingBusy(true);
		vsacapiService.getValueSetByOIDAndVersionOrExpansionId(oid, version, effectiveDate, new AsyncCallback<VsacApiResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				showSearchingBusy(false);
			}
			
			@Override
			public void onSuccess(final VsacApiResult result) {
				if (result.isSuccess()) {
					searchDisplay.buildValueSetDetailsWidget(result.getVsacResponse());
					searchDisplay.getValueSetDetailsPanel().setVisible(true);
				} else {
					String message = convertMessage(result.getFailureReason());
					searchDisplay.getErrorMessageDisplay().setMessage(message);
				}
				showSearchingBusy(false);
			}
		});*/
	}
	
	/**
	 * This method is used to update QDM element selected for modification. All
	 * check's for attributes and non attributes , Occurrence and non
	 * occurrences. are done in this method. This method returns modified and
	 * ordered list of all applied QDM elements. This method also makes call to
	 * updateMeasureXML method.
	 * @param ValueSet
	 *            - {@link ValueSet}
	 * @param codeListSearchDTO
	 *            - {@link CodeListSearchDTO}
	 * @param qualityDataSetDTO
	 *            - {@link QualityDataSetDTO}
	 * @param dataType
	 *            - {@link String}
	 * @param isSpecificOccurrence
	 *            - {@link Boolean}
	 * @param isUSerDefined
	 *            - {@link Boolean}
	 */
	private void updateAppliedQDMList(final ValueSet ValueSet , final CodeListSearchDTO codeListSearchDTO ,
									  final QualityDataSetDTO qualityDataSetDTO, final String dataType, final Boolean isSpecificOccurrence,
									  final boolean isUSerDefined) {
		modifyQDMList(qualityDataSetDTO);
		MatValueSetTransferObject ValueSetTransferObject = new MatValueSetTransferObject();
		ValueSetTransferObject.setDatatype(dataType);
		ValueSetTransferObject.setValueSet(ValueSet);
		ValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		ValueSetTransferObject.setQualityDataSetDTO(qualityDataSetDTO);
		ValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		ValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if ((searchDisplay.getDateInput().getValue() != null) && !searchDisplay.getDateInput().getValue().trim().isEmpty()) {
			if (searchDisplay.getVersion().getValue().equals(Boolean.TRUE)) {
				ValueSetTransferObject.setVersionDate(true);
				ValueSetTransferObject.setEffectiveDate(false);
			} else if (searchDisplay.getEffectiveDate().getValue().equals(Boolean.TRUE)) {
				ValueSetTransferObject.setEffectiveDate(true);
				ValueSetTransferObject.setVersionDate(false);
			} else {
				ValueSetTransferObject.setEffectiveDate(false);
				ValueSetTransferObject.setVersionDate(false);
			}
			ValueSetTransferObject.setQueryDate(searchDisplay.getDateInput().getValue());
		}
		MatContext.get().getCodeListService().updateCodeListToMeasure(ValueSetTransferObject,
				new AsyncCallback<SaveUpdateCodeListResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				if (!isUSerDefined) {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				} else {
					searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
			}
			@Override
			public void onSuccess(final SaveUpdateCodeListResult result) {
				if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
					if (!isUSerDefined) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCodeListName()));
					} else {
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
								MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCodeListName()));
					}
				} else {
					appliedQDMList = result.getAppliedQDMList();
					updateMeasureXML(result.getDataSetDTO() , qualityDataSetDTO, isUSerDefined);
				}
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
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under
	 * Population Node and Stratification Node, SupplementDataElements. It also
	 * removes attributes nodes if there is mismatch in data types of newly
	 * selected QDM and already applied QDM. *
	 *
	 * @param modifyWithDTO
	 *            - {@link QualityDataSetDTO}
	 * @param modifyableDTO
	 *            - {@link QualityDataSetDTO}
	 * @param isUserDefined
	 *            - {@link Boolean}
	 */
	private void updateMeasureXML(final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyableDTO, final boolean isUserDefined) {
		measureService.updateMeasureXML(modifyWithDTO, modifyableDTO,
				MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				if (!isUserDefined) {
					searchDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				} else {
					searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				//modifyValueSetDTO = modifyWithDTO;
			}
			
			@Override
			public void onSuccess(final Void result) {
				if (!isUserDefined) {
					searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
							MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
				} else {
					searchDisplay.getSuccessMessageUserDefinedPanel().setMessage(
							MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
					searchDisplay.getUserDefinedInput().setText("");
					searchDisplay.getAllDataTypeInput().setSelectedIndex(0);
				}
				modifyValueSetDTO = modifyWithDTO;
			}
		});
	}
}
