package mat.client.clause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.event.QDSElementCreatedEvent;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
/**QDSCodeListSearchPresenter.java.**/
public class QDSCodeListSearchPresenter implements MatPresenter {

	/**Simple Panel Object.**/
	private SimplePanel panel = new SimplePanel();
	/**SearchDisplay Object.**/
	private SearchDisplay searchDisplay;
	/**ArrayList of All appliedQDM's.**/
	private ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	/**Boolean isUserDefined.**/
	private boolean isUSerDefined = false;
    /**VSACService object.**/
	private VSACAPIServiceAsync vsacapiService = MatContext.get()
			.getVsacapiServiceAsync();
	  /**MeasureService object.**/
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
/** QDSCodeListSearchView Implements this SearchDisplay.**/
	public  interface SearchDisplay {
		Widget getDataTypeWidget();
		CustomCheckBox getSpecificOccurrenceInput();
		String getDataTypeValue(ListBoxMVP inputListBox);
		ErrorMessageDisplay getErrorMessageDisplay();
		String getDataTypeText(ListBoxMVP inputListBox);
		DisclosurePanel getDisclosurePanel();
		Button getPsuedoQDMToMeasure();
		TextBox getUserDefinedInput();
		ListBoxMVP getAllDataTypeInput();
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		DisclosurePanel getDisclosurePanelCellTable();
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
		void buildValueSetDetailsWidget(
				ArrayList<MatValueSet> matValueSets);
		Button getApplyToMeasureButton();
		MatValueSet getCurrentMatValueSet();
		void resetVSACValueSetWidget();
		Widget asWidget();
	}

	public QDSCodeListSearchPresenter(final SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;

		// TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanel().addEventHandler(
				new DisclosureHandler() {
					// TODO: Need to replace the DisclosureEvent with CloseEvent.
					public void onClose(final DisclosureEvent event) {
						searchDisplay.getUserDefinedInput().setText("");
						searchDisplay.getAllDataTypeInput().setItemSelected(0,
								true);
						displaySearch();
						searchDisplay.getDisclosurePanelCellTable().setOpen(
								true);
					}

					// TODO: Need to replace the DisclosureEvent with OpenEvent
					public void onOpen(final DisclosureEvent event) {
						displaySearch();
						searchDisplay.getDisclosurePanelCellTable().setOpen(
								false);
					}
				});

		// TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanelCellTable().addEventHandler(
				new DisclosureHandler() {

					// TODO: Need to replace the DisclosureEvent with CloseEvent
					public void onClose(final DisclosureEvent event) {
						searchDisplay.getUserDefinedInput().setText("");
						displaySearch();
						searchDisplay.getDisclosurePanel().setOpen(true);
					}

					// TODO: Need to replace the DisclosureEvent with OpenEvent.
					public void onOpen(final DisclosureEvent event) {
						displaySearch();
						searchDisplay.getDisclosurePanel().setOpen(false);
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

		searchDisplay.getPsuedoQDMToMeasure().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						isUSerDefined = true;
						getListOfAppliedQDMs(isUSerDefined);
					}
				});

		searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				MatContext.get().clearDVIMessages();
				isUSerDefined = false;
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(),
						searchDisplay.getVersionInput().getValue());
			}
		});

		searchDisplay.getApplyToMeasureButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						MatContext.get().clearDVIMessages();
						isUSerDefined = false;
						getListOfAppliedQDMs(isUSerDefined);
					}
				});
	}

	/**
	 *@param oid - {@link String}.
	 * @param version - {@link String}.
	 * **/
	private void searchValueSetInVsac(final String oid, final String version) {

		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
							.getUMLS_NOT_LOGGEDIN());
			return;
		} else {
		// OID validation.
		if (oid == null || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
							.getUMLS_OID_REQUIRED());
			searchDisplay.getValueSetDetailsPanel().setVisible(false);
			return;
		}

		showSearchingBusy(true);
		vsacapiService.getValueSetByOIDAndVersion(oid,
				new AsyncCallback<VsacApiResult>() {
					@Override
					public void onFailure(final Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate()
										.getVSAC_RETRIEVE_FAILED());
						searchDisplay.getValueSetDetailsPanel().setVisible(
								false);
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(final VsacApiResult result) {
						if (result.isSuccess()) {
							searchDisplay.buildValueSetDetailsWidget(result
									.getVsacResponse());
							searchDisplay.getValueSetDetailsPanel().setVisible(
									true);
						} else {
							String message = convertMessage(result
									.getFailureReason());
							searchDisplay.getErrorMessageDisplay().setMessage(
									message);
							searchDisplay.getValueSetDetailsPanel().setVisible(
									false);
						}
						showSearchingBusy(false);
					}
				});
		}
	}
/**
 *@param id - {@link Integer}.
 *@return String - {@link String}.
 * **/
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
					.getUnknownFailMessage();
		}
		return message;
	}

	/**
	 *@param isUserDefined - {@link Boolean}.
	 *
	 * **/
	private void getListOfAppliedQDMs(final boolean isUserDefined) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if (measureId != null && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId, true,
					new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate()
									.getGenericErrorMessage());
						}

						@Override
						public void onSuccess(
								final ArrayList<QualityDataSetDTO> result) {
							appliedQDMList = result;
							addSelectedCodeListtoMeasure(isUserDefined);
						}
					});

		}
	}
/***
 *Populates all data types from DB for Element's with and Element's without VSAC drop down.
 * */
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
                            	searchDisplay.setAllDataTypeOptions(result);
								searchDisplay
								.setDataTypesListBoxOptions(result);
							}
						});

	}

	/**
	 *Method to show loading message and disable Retrieve button , OID and version Input boxes.
	 *@param busy - {@link Boolean}.
	 * **/
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getRetrieveButton().setEnabled(!busy);
		searchDisplay.getOIDInput().setEnabled(!busy);
		searchDisplay.getVersionInput().setEnabled(!busy);
	}

	/**
	 *Method to show Widget on Create Element Tab.
	 * **/
	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		populateAllDataType();
		searchDisplay.resetVSACValueSetWidget();
		searchDisplay.clearVSACValueSetMessages();
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
	}

	/**
	 *Based on isUserDefined boolean - Service call to Add value set with VSAC QDS or
	 *User defined Value set is made.
	 *@param isUserDefinedQDM - {@link Boolean}
	 * **/
	private void addSelectedCodeListtoMeasure(final boolean isUserDefinedQDM) {
		if (!isUserDefinedQDM) {
			addQDSWithValueSet();
		} else {
			addQDSWithOutValueSet();
		}
	}

	/**
	 *Service call to add VSAC QDS into Measure xml.
	 * **/
	private void addQDSWithValueSet() {
		// clear the successMessage
		searchDisplay.getSuccessMessageDisplay().clear();
		final String dataType;
		final String dataTypeText;
		final boolean isSpecificOccurrence;

		dataType = searchDisplay.getDataTypeValue(searchDisplay
				.getDataTypesListBox());

		if (searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox())
				.equalsIgnoreCase("--Select--")) {
			dataTypeText = dataType;
		} else {
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay
					.getDataTypesListBox());
		}
		isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput()
				.getValue();
		String measureID = MatContext.get().getCurrentMeasureId();
		if (!dataType.isEmpty() && !dataType.equals("")) {
			MatContext
					.get()
					.getCodeListService()
					.saveQDStoMeasure(measureID, dataType,
							searchDisplay.getCurrentMatValueSet(),
							isSpecificOccurrence, appliedQDMList,
							new AsyncCallback<SaveUpdateCodeListResult>() {
								@Override
								public void onSuccess(
										final SaveUpdateCodeListResult result) {
									String message = "";
									if (result.getXmlString() != null) {
										saveMeasureXML(result.getXmlString());
									}
									// OnSuccess() un check the specific
									// occurrence  and de select
									// the radio options
									searchDisplay.getSpecificOccurrenceInput()
											.setValue(false);
									if (result.isSuccess()) {
										if (result.getOccurrenceMessage() != null
												&& !result
														.getOccurrenceMessage()
														.equals("")) {
											message = MatContext
													.get()
													.getMessageDelegate()
													.getQDMOcurrenceSuccessMessage(
															searchDisplay
														.getCurrentMatValueSet()
															.getDisplayName(),
															dataTypeText,
													result.getOccurrenceMessage());
										} else {
											message = MatContext
													.get()
													.getMessageDelegate()
													.getQDMSuccessMessage(
													searchDisplay
													.getCurrentMatValueSet()
													.getDisplayName(),
															dataTypeText);
										}
										MatContext.get()
										 .getEventBus().fireEvent(
										new QDSElementCreatedEvent(
												searchDisplay
												.getCurrentMatValueSet()
												.getDisplayName()));
										searchDisplay
												.getSuccessMessageDisplay()
												.setMessage(message);
									}
								}

								@Override
								public void onFailure(final Throwable caught) {
									if (appliedQDMList.size() > 0) {
										appliedQDMList
												.removeAll(appliedQDMList);
									}
									searchDisplay
											.getErrorMessageDisplay()
											.setMessage(
											"problem while saving the QDM to Measure");
								}
							});
		} else {
			searchDisplay
			.getErrorMessageDisplay()
			.setMessage(
					"Please select data type from drop down list.");
		}
	}

	/**
	 *Service call to add User defined QDS into Measure xml.
	 * **/
	private void addQDSWithOutValueSet() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(
						searchDisplay.getAllDataTypeInput()).equalsIgnoreCase(
						MatContext.PLEASE_SELECT)) {
			MatContext
					.get()
					.getCodeListService()
					.saveUserDefinedQDStoMeasure(
							MatContext.get().getCurrentMeasureId(),
							searchDisplay.getDataTypeText(searchDisplay
									.getAllDataTypeInput()),
							searchDisplay.getUserDefinedInput().getText(),
							appliedQDMList,
							new AsyncCallback<SaveUpdateCodeListResult>() {
								@Override
								public void onFailure(final Throwable caught) {
									if (appliedQDMList.size() > 0) {
										appliedQDMList
												.removeAll(appliedQDMList);
									}
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
														.getAllDataTypeInput()));
										searchDisplay
												.getSuccessMessageUserDefinedPanel()
												.setMessage(message);
										searchDisplay.getUserDefinedInput()
												.setText("");
										searchDisplay.getAllDataTypeInput()
												.setSelectedIndex(0);
									} else if (result.getFailureReason() == result.ALREADY_EXISTS) {
										searchDisplay
												.getErrorMessageUserDefinedPanel()
												.setMessage(
														MatContext
														.get()
														.getMessageDelegate()
													.getDuplicateAppliedQDMMsg());
									}
								}
							});
		} else {
			if (appliedQDMList.size() > 0) {
				appliedQDMList.removeAll(appliedQDMList);
			}
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
					MatContext.get().getMessageDelegate()
							.getVALUE_SET_NAME_DATATYPE_REQD());

		}
	}

	/**
	 *@param qdmXMLString - {@link String}.
	 * **/
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

					}
				});
	}

	/**
	 *@return {@link Widget}.
	 * **/
	public final Widget getWidget() {
		return panel;
	}

	/**
	 *@param columnIndex - {@link Integer}.
	 *@return String - {@link String}.
	 * **/
	public final String getSortKey(final int columnIndex) {
		String[] sortKeys = new String[] {"name", "taxnomy", "category" };
		return sortKeys[columnIndex];
	}

	/**
	 * This method is used to set all the widgets to read only mode.
	 * @param editable - {@link Boolean}
	 */
	private void setWidgetsReadOnly(final boolean editable) {
		//Widgets in "Element with VSAC Value Set" panel.
		searchDisplay.getRetrieveButton().setEnabled(editable);
		searchDisplay.getOIDInput().setEnabled(editable);
		searchDisplay.getVersionInput().setEnabled(editable);
		//Widgets in "Element without VSAC Value Set" panel.
		searchDisplay.getUserDefinedInput().setEnabled(editable);
		searchDisplay.getAllDataTypeInput().setEnabled(editable);
		searchDisplay.getPsuedoQDMToMeasure().setEnabled(editable);
	}

	@Override
	public  void beforeDisplay() {
		displaySearch();
		setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
	}

	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		searchDisplay.getDisclosurePanel().setOpen(false);
		searchDisplay.getDisclosurePanelCellTable().setOpen(true);
	}
}
