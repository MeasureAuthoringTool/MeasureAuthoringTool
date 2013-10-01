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
public class QDSCodeListSearchPresenter implements MatPresenter {

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;	
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	ArrayList<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	boolean isUSerDefined = false;
	ArrayList<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();
	boolean isCheckForSDE = false;
	List<String> codeListString = new ArrayList<String>();
	VSACAPIServiceAsync vsacapiService = MatContext.get()
			.getVsacapiServiceAsync();

	public static interface SearchDisplay {		
		public Widget getDataTypeWidget();
		public CustomCheckBox getSpecificOccurrenceInput();
		public String getDataTypeValue(ListBoxMVP inputListBox);
		public ErrorMessageDisplay getErrorMessageDisplay();
		public String getDataTypeText(ListBoxMVP inputListBox);
		public DisclosurePanel getDisclosurePanel();
		public Button getPsuedoQDMToMeasure();
		public Button getPsuedoQDMCancel();
		public TextBox getUserDefinedInput();
		public ListBoxMVP getAllDataTypeInput();
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		public DisclosurePanel getDisclosurePanelCellTable();
		public SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		public ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		public TextBox getOIDInput();
		public DateBoxWithCalendar getVersionInput();
		public Button getRetrieveButton();
		public VerticalPanel getValueSetDetailsPanel();
		public ListBoxMVP getDataTypesListBox();
		public SuccessMessageDisplay getSuccessMessageDisplay();
		public void setDataTypesListBoxOptions(List<? extends HasListBox> texts);
		public void clearVSACValueSetMessages();
		public void buildValueSetDetailsWidget(
				ArrayList<MatValueSet> matValueSets);
		public Button getApplyToMeasureButton();
		public MatValueSet getCurrentMatValueSet();
		public void resetVSACValueSetWidget();		
		public Widget asWidget();
	}

	@SuppressWarnings("deprecation")
	public QDSCodeListSearchPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;

		// TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanel().addEventHandler(
				new DisclosureHandler() {
					// TODO: Need to replace the DisclosureEvent with CloseEvent
					public void onClose(DisclosureEvent event) {
						searchDisplay.getUserDefinedInput().setText("");
						searchDisplay.getAllDataTypeInput().setItemSelected(0,
								true);						
						displaySearch();
						searchDisplay.getDisclosurePanelCellTable().setOpen(
								true);
					}

					// TODO: Need to replace the DisclosureEvent with OpenEvent
					public void onOpen(DisclosureEvent event) {
						populateAllDataType();						
						displaySearch();
						searchDisplay.getDisclosurePanelCellTable().setOpen(
								false);
					}
				});

		// TODO: Need to replace the use of addEventHandler with addOpenHandler.
		searchDisplay.getDisclosurePanelCellTable().addEventHandler(
				new DisclosureHandler() {

					// TODO: Need to replace the DisclosureEvent with CloseEvent
					public void onClose(DisclosureEvent event) {
						searchDisplay.getUserDefinedInput().setText("");						
						displaySearch();
						searchDisplay.getDisclosurePanel().setOpen(true);
					}

					// TODO: Need to replace the DisclosureEvent with OpenEvent
					public void onOpen(DisclosureEvent event) {
						displaySearch();
						searchDisplay.getDisclosurePanel().setOpen(false);
					}
				});

		searchDisplay.getPsuedoQDMCancel().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getDisclosurePanel().setOpen(false);
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});

		searchDisplay.getUserDefinedInput().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();
			}
		});

		searchDisplay.getAllDataTypeInput().addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				searchDisplay.getSuccessMessageUserDefinedPanel().clear();
				searchDisplay.getErrorMessageUserDefinedPanel().clear();

			}
		});

		searchDisplay.getPsuedoQDMToMeasure().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						isUSerDefined = true;
						getListOfAppliedQDMs(isUSerDefined);
					}
				});

		searchDisplay.getRetrieveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				isUSerDefined = false;
				searchValueSetInVsac(searchDisplay.getOIDInput().getValue(),
						searchDisplay.getVersionInput().getValue());
			}
		});

		searchDisplay.getApplyToMeasureButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						MatContext.get().clearDVIMessages();
						isUSerDefined = false;
						getListOfAppliedQDMs(isUSerDefined);
					}
				});
	}

	private void searchValueSetInVsac(String oid, String version) {

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
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate()
										.getVSAC_RETRIEVE_FAILED());
						searchDisplay.getValueSetDetailsPanel().setVisible(
								false);
						showSearchingBusy(false);
					}

					@Override
					public void onSuccess(VsacApiResult result) {
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

	private String convertMessage(int id) {
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

	private void getListOfAppliedQDMs(final boolean isUSerDefined) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if (measureId != null && measureId != "") {
			service.getMeasureXMLForAppliedQDM(measureId, true,
					new AsyncCallback<ArrayList<QualityDataSetDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate()
									.getGenericErrorMessage());
						}

						@Override
						public void onSuccess(
								ArrayList<QualityDataSetDTO> result) {
							appliedQDMList = result;
							addSelectedCodeListtoMeasure(isUSerDefined);
						}
					});

		}
	}

	private void populateAllDataType() {
		MatContext
				.get()
				.getListBoxCodeProvider()
				.getAllDataType(
						new AsyncCallback<List<? extends HasListBox>>() {

							@Override
							public void onFailure(Throwable caught) {
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
									List<? extends HasListBox> result) {
								Collections.sort(result,
										new HasListBox.Comparator());
								searchDisplay.setAllDataTypeOptions(result);
							}
						});

	}

	private void populateDataTypesListBox() {
		MatContext
				.get()
				.getListBoxCodeProvider()
				.getAllDataType(
						new AsyncCallback<List<? extends HasListBox>>() {

							@Override
							public void onFailure(Throwable caught) {
								searchDisplay.clearVSACValueSetMessages();
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
									List<? extends HasListBox> result) {
								Collections.sort(result,
										new HasListBox.Comparator());
								searchDisplay
										.setDataTypesListBoxOptions(result);
							}
						});

	}	

	private void showSearchingBusy(boolean busy) {
		if (busy)
			Mat.showLoadingMessage();
		else
			Mat.hideLoadingMessage();
	}

	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
		populateDataTypesListBox();
		searchDisplay.resetVSACValueSetWidget();
		searchDisplay.clearVSACValueSetMessages();
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		// searchDisplay.setAddToMeasureButtonEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
	}

	private void addSelectedCodeListtoMeasure(boolean isUserDefinedQDM) {
		if (!isUserDefinedQDM) {
			addQDSWithValueSet();
		} else {
			addQDSWithOutValueSet();
		}
	}

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
										SaveUpdateCodeListResult result) {
									String message = "";
									if (result.getXmlString() != null)
										saveMeasureXML(result.getXmlString());
									searchDisplay.getSpecificOccurrenceInput()
											.setValue(false);// OnSuccess()
																// uncheck the
																// specific
																// occurrence
																// and deselect
																// the radio
																// options
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
										MatContext
												.get()
												.getEventBus()
												.fireEvent(
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
								public void onFailure(Throwable caught) {
									if (appliedQDMList.size() > 0)
										appliedQDMList
												.removeAll(appliedQDMList);
									searchDisplay
											.getErrorMessageDisplay()
											.setMessage(
													"problem while saving the QDM to Measure");
								}
							});
		}
	}
	
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
								public void onFailure(Throwable caught) {
									if (appliedQDMList.size() > 0)
										appliedQDMList
												.removeAll(appliedQDMList);
									Window.alert(MatContext.get()
											.getMessageDelegate()
											.getGenericErrorMessage());
								}

								@Override
								public void onSuccess(
										SaveUpdateCodeListResult result) {
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
									} else if (result.getFailureReason() == 7) {
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

	private void saveMeasureXML(String qdmXMLString) {
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
					public void onFailure(Throwable caught) {
						searchDisplay.getErrorMessageDisplay().setMessage(
								MatContext.get().getMessageDelegate()
										.getGenericErrorMessage());
					}

					@Override
					public void onSuccess(Void result) {

					}
				});
	}

	public Widget getWidget() {
		return panel;
	}

	public String getSortKey(int columnIndex) {
		String[] sortKeys = new String[] { "name", "taxnomy", "category" };
		return sortKeys[columnIndex];
	}

	@Override
	public void beforeDisplay() {		
		displaySearch();
	}

	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		searchDisplay.getDisclosurePanel().setOpen(false);
		searchDisplay.getDisclosurePanelCellTable().setOpen(true);
	}
}
