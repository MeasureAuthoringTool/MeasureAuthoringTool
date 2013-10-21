package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.client.Mat;
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
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class QDMAvailableValueSetPresenter  implements MatPresenter {

    private SearchDisplay searchDisplay;
	private MatValueSet currentMatValueSet;
	MeasureServiceAsync measureService = MatContext.get().getMeasureService();
	ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	QualityDataSetDTO  modifyValueSetDTO;
	mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay;
	VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed.
	 * busyLoading is set true when retrieving value set from VSAC otherwise it is set false.
	 */
	private boolean busyLoading;
	
	public static interface SearchDisplay {
		CustomCheckBox getSpecificOccurrenceInput();
		String getDataTypeValue(ListBoxMVP inputListBox);
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		String getDataTypeText(ListBoxMVP inputListBox);
		DisclosurePanel getDisclosurePanel();
		Button getPsuedoQDMToMeasure();
		TextBox getUserDefinedInput();
		ListBoxMVP getAllDataTypeInput();
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		DisclosurePanel getDisclosurePanelVSAC();
		SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		
		TextBox getOIDInput();
		DateBoxWithCalendar getVersionInput();
		Button getRetrieveButton();
		VerticalPanel getValueSetDetailsPanel();
		ListBoxMVP getDataTypesListBox();
		SuccessMessageDisplay getSuccessMessageDisplay();
		void setDataTypesListBoxOptions(List<? extends HasListBox> texts);
		void clearVSACValueSetMessages();
		void buildValueSetDetailsWidget(ArrayList<MatValueSet> matValueSets);
		Button getApplyToMeasureButton();
		MatValueSet getCurrentMatValueSet();
		void resetVSACValueSetWidget();
		Widget asWidget();
	}

	public QDMAvailableValueSetPresenter(SearchDisplay sDisplayArg , QualityDataSetDTO dataSetDTO,
			final mat.client.clause.QDSAppliedListPresenter.SearchDisplay qdsAppliedListPresenterDisplay) {
		this.searchDisplay = sDisplayArg;
		this.modifyValueSetDTO = dataSetDTO;
		this.qdsAppliedListPresenterDisplay = qdsAppliedListPresenterDisplay;
		this.appliedQDMList = (ArrayList<QualityDataSetDTO>) qdsAppliedListPresenterDisplay.getAllAppliedQDMList();

		//Element without VSAC value set - OPEN Handler
		searchDisplay.getDisclosurePanel().addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				if (isBusyLoading()) {
					event.getTarget().setOpen(false);
				} else {
					//populateAllDataType();
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
				currentMatValueSet = searchDisplay.getCurrentMatValueSet();				
				modifyQDM(false);
				/*MatContext.get().getMeasureService().getAppliedQDMFromMeasureXml(MatContext.get().getCurrentMeasureId(),
						true, new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate().getGenericErrorMessage());
					}

					@Override
					public void onSuccess(final ArrayList<QualityDataSetDTO> result) {
						appliedQDMList = result;
						currentMatValueSet = searchDisplay.getCurrentMatValueSet();
						modifyQDM(false);
					}

				});*/
			}
		});

        searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.clearVSACValueSetMessages();
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(),
						searchDisplay.getVersionInput().getValue());
			}
		});
	}

	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed. 
	 * @return true if "Loading Please Wait..." message is displaying(In other words, when retrieving value set from VSAC)
	 * 	    else returns false;
	 */
	public boolean isBusyLoading() {
		return busyLoading;
	}
	
	private void searchValueSetInVsac(String oid, String version) {
		if (!MatContext.get().isUMLSLoggedIn()) { //UMLS Login Validation
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			return;
		}
		//OID validation.
		if (oid == null || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED());
			searchDisplay.getValueSetDetailsPanel().setVisible(false);
			return;
		}
	    showSearchingBusy(true);
		vsacapiService.getValueSetByOIDAndVersion(oid, new AsyncCallback<VsacApiResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED());
				searchDisplay.getValueSetDetailsPanel().setVisible(false);
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
					searchDisplay.getValueSetDetailsPanel().setVisible(false);
				}
				showSearchingBusy(false);
			}
		});
	}

	private String convertMessage(int id) {
		String message;
		switch(id) {
			case VsacApiResult.UMLS_NOT_LOGGEDIN:
				message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
				break;
			case VsacApiResult.OID_REQUIRED:
				message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
				break;
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
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

	private void modifyValueSetQDM() {
		//Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		if (modifyValueSetDTO != null && modifyWithDTO != null) {
			String dataType;
			String dataTypeText;
			Boolean isSpecificOccurrence = false;

			dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
		    isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();

			if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
					|| dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
				if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
					updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
				} else {
					if (ConstantMessages.ATTRIBUTE.equalsIgnoreCase(dataTypeText)) {
						searchDisplay.getErrorMessageDisplay().setMessage(
						"A value set with a non-Attribute category must be used for this data element.");
					} else {
						searchDisplay.getErrorMessageDisplay().setMessage(
							"A value set with an Attribute category must be used for this data element.");
					}
				}
			} else {
				updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
			}
		} else {
			searchDisplay.getErrorMessageDisplay().setMessage("Please select atleast one applied QDM to modify.");
		}
	}

	private void modifyQDMWithOutValueSet(){
		//Pseudo QDM Flow
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(searchDisplay.getAllDataTypeInput()).
				equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
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
						"A value set with a non-Attribute category must be used for this data element.");
					} else {
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
						"A value set with an Attribute category must be used for this data element.");
					}
				}
			} else {
				updateAppliedQDMList(null, modifyWithDTO, modifyValueSetDTO, dataType, false, true);
			}
		} else {
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
				MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
	}

	/**
	 * This method is used to update QDM element selected for modification.
	 *  All check's for attributes and non attributes , Occurrence and non occurrences.
	 * are done in this method. This method returns modified and ordered list of all applied QDM elements.
	 * This method also makes call to updateMeasureXML method.
	 *
	 **/
	private void updateAppliedQDMList(final MatValueSet matValueSet , final CodeListSearchDTO codeListSearchDTO ,
			final QualityDataSetDTO  qualityDataSetDTO, final String dataType,  final Boolean isSpecificOccurrence,
			final boolean isUSerDefined) {
		MatContext.get().getCodeListService().updateCodeListToMeasure(dataType, matValueSet, codeListSearchDTO,
				qualityDataSetDTO, isSpecificOccurrence, appliedQDMList, new AsyncCallback<SaveUpdateCodeListResult>() {
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
								MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					} else {
						searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
							MatContext.get().getMessageDelegate().getDuplicateAppliedQDMMsg());
					}
				} else {
					appliedQDMList = result.getAppliedQDMList();
					updateMeasureXML(result.getDataSetDTO() , qualityDataSetDTO, isUSerDefined);
				}
			}
		});

	}

	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under Population Node and Stratification Node,
	 *  SupplementDataElements. It also removes attributes nodes if
	 * there is mismatch in data types of newly selected QDM and already applied QDM.	 *
	 **/
	private void updateMeasureXML(final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyableDTO, final boolean isUserDefined) {
		MatContext.get().getMeasureService().updateMeasureXML(modifyWithDTO, modifyableDTO,
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
			}

			@Override
			public void onSuccess(final Void result) {
				if (!isUserDefined) {
					searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
							MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
				} else {
					searchDisplay.getSuccessMessageUserDefinedPanel().setMessage(
						MatContext.get().getMessageDelegate().getSuccessfulModifyQDMMsg());
					searchDisplay.getUserDefinedInput().setText("");
					searchDisplay.getAllDataTypeInput().setSelectedIndex(0);
				}
			}
		});

	}

	/**
	 * This method is used to reload Applied QDM List.
	 **/
	public void reloadAppliedQDMList() {
		QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
		filterTimingQDMs(appliedQDMList);
		appliedListModel.setAppliedQDMs(appliedQDMList);
		qdsAppliedListPresenterDisplay.buildCellList(appliedListModel);
		
		/*
		 * Setting appliedQDMList in qdsAppliedListPresenterDisplay. 
		 * Whenever this modify popup is opened this.appliedQDMList is set with qdsAppliedListPresenterDisplay.appliedQDMList in this presenter.
		 * So, qdsAppliedListPresenterDisplay.appliedQDMList is updated here.
		 */
		qdsAppliedListPresenterDisplay.setAppliedQDMList(appliedQDMList);
	}
	
	private final void filterTimingQDMs(
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

	/**
	 * This method is used in searching all available Value sets for pop up.
	 * @param  busy - Boolean busy status.
	 **/
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		busyLoading = busy;
		searchDisplay.getRetrieveButton().setEnabled(!busy);
		searchDisplay.getOIDInput().setEnabled(!busy);
		searchDisplay.getVersionInput().setEnabled(!busy);
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

	private void populateAllDataType(){
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
	public Widget getWidget() {
		return searchDisplay.asWidget();
	}

	@Override
	public void beforeDisplay() {
		displaySearch();
	}
	@Override
	public void beforeClosingDisplay() {

	}
}
