package mat.client.clause;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
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
import mat.model.MatValueSet;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import org.gwtbootstrap3.client.ui.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
/*
 * This class is for code that is non maintained anymore. It should not be changed. 
 */
public class QDSCodeListSearchPresenter implements MatPresenter {
	
	public  interface SearchDisplay {
		
		Widget asWidget();
		
		void buildValueSetDetailsWidget(
				List<MatValueSet> list);
		
		void clearVSACValueSetMessages();
		
		ListBoxMVP getAllDataTypeInput();
		
		Button getApplyToMeasureButton();
		
		MatValueSet getCurrentMatValueSet();
		
		ListBoxMVP getDataTypesListBox();
		
		String getDataTypeText(ListBoxMVP inputListBox);
		
		String getDataTypeValue(ListBoxMVP inputListBox);
		
		Widget getDataTypeWidget();
		
		DateBoxWithCalendar getDateInput();
		
		DisclosurePanel getDisclosurePanel();
		
		DisclosurePanel getDisclosurePanelVSAC();
		
		CustomCheckBox getEffectiveDate();
		
		ErrorMessageDisplay getErrorMessageDisplay();
		
		ErrorMessageDisplay getErrorMessageUserDefinedPanel();
		
		TextBox getOIDInput();
		
		Button getPsuedoQDMToMeasure();
		
		Button getRetrieveButton();
		
		CustomCheckBox getSpecificOccurrenceInput();
		
		SuccessMessageDisplay getSuccessMessageDisplay();
		
		SuccessMessageDisplay getSuccessMessageUserDefinedPanel();
		
		TextBox getUserDefinedInput();
		
		VerticalPanel getValueSetDetailsPanel();
		
		CustomCheckBox getVersion();
		
		void resetVSACValueSetWidget();
		
		void setAllDataTypeOptions(List<? extends HasListBox> texts);
		
		void setDataTypesListBoxOptions(List<? extends HasListBox> texts);
	}
	
	
	private List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();
	
	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed.
	 * busyLoading is set true when retrieving value set from VSAC otherwise it is set false.
	 */
	private boolean busyLoading;
	
	private boolean isUSerDefined = false;
	
	private SimplePanel panel = new SimplePanel();
	
	private SearchDisplay searchDisplay;
	
	private final MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	public QDSCodeListSearchPresenter(final SearchDisplay sDisplayArg) {
		searchDisplay = sDisplayArg;
		
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
	 * Service call to add User defined QDS into Measure xml.
	 */
	private void addQDSWithOutValueSet() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		String userDefinedInput = searchDisplay.getUserDefinedInput().getText().trim();
		boolean isValidUserDefinedInput = QDMAvailableValueSetPresenter.validateUserDefinedInput(userDefinedInput);
		if ((searchDisplay.getUserDefinedInput().getText().trim().length() > 0)
				&& !searchDisplay.getDataTypeText(
						searchDisplay.getAllDataTypeInput()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			if(isValidUserDefinedInput){
				String dataType = searchDisplay.getDataTypeValue(searchDisplay.getAllDataTypeInput());
				MatValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(dataType, false,
						MatContext.get().getCurrentMeasureId());
				MatContext.get().getCodeListService().saveUserDefinedQDStoMeasure(
						matValueSetTransferObject, new AsyncCallback<SaveUpdateCodeListResult>() {
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
								if (result.getXmlString() != null && result.getnewXmlString() != null) {
									saveMeasureXML(result.getXmlString(), result.getnewXmlString());
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
											.getDuplicateAppliedValueSetMsg(result.getCodeListName()));
								}
							}
						});
			}else{
				searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
						MatContext.get().getMessageDelegate()
						.getINVALID_CHARACTER_VALIDATION_ERROR());
			}
			
		} else {
			if (appliedQDMList.size() > 0) {
				appliedQDMList.removeAll(appliedQDMList);
			}
			
			searchDisplay.getErrorMessageUserDefinedPanel().setMessage(
					MatContext.get().getMessageDelegate()
					.getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
		}
		
		
	}
	
	/**
	 * Service call to add VSAC QDS into Measure xml.
	 */
	private void addQDSWithValueSet() {
		// clear the successMessage
		searchDisplay.getSuccessMessageDisplay().clear();
		final String dataType;
		final String dataTypeText;
		final boolean isSpecificOccurrence;
		
		dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
		
		if (searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox()).equalsIgnoreCase("--Select--")) {
			dataTypeText = dataType;
		} else {
			dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
		}
		
		isSpecificOccurrence = searchDisplay.getSpecificOccurrenceInput().getValue();
		String measureID = MatContext.get().getCurrentMeasureId();
		
		if (!dataType.isEmpty() && !dataType.equals("")) {
			MatValueSetTransferObject matValueSetTransferObject = createValueSetTransferObject(dataType, isSpecificOccurrence,
					measureID);
			MatContext.get().getCodeListService().saveQDStoMeasure(matValueSetTransferObject,
					new AsyncCallback<SaveUpdateCodeListResult>() {
				@Override
				public void onFailure(final Throwable caught) {
					if (appliedQDMList.size() > 0) {
						appliedQDMList
						.removeAll(appliedQDMList);
					}
					searchDisplay
					.getErrorMessageDisplay()
					.setMessage(
							MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				
				@Override
				public void onSuccess(final SaveUpdateCodeListResult result) {
					String message = "";
					if (result.getXmlString() != null && result.getnewXmlString() != null) {
						saveMeasureXML(result.getXmlString(), result.getnewXmlString());
					}
					// OnSuccess() un check the specific
					// occurrence  and de select
					// the radio options
					searchDisplay.getSpecificOccurrenceInput().setValue(false);
					if (result.isSuccess()) {
						if ((result.getOccurrenceMessage() != null)
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
					} else {
						if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
							searchDisplay.getErrorMessageDisplay().setMessage(
									MatContext.get().getMessageDelegate()
									.getDuplicateAppliedValueSetMsg(result.getCodeListName()));
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
	 * Based on isUserDefined boolean - Service call to Add value set with VSAC QDS or
	 * User defined Value set is made.
	 * @param isUserDefinedQDM - {@link Boolean}
	 */
	private void addSelectedCodeListtoMeasure(final boolean isUserDefinedQDM) {
		if (!isUserDefinedQDM) {
			addQDSWithValueSet();
		} else {
			addQDSWithOutValueSet();
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		searchDisplay.getSuccessMessageUserDefinedPanel().clear();
		searchDisplay.getErrorMessageUserDefinedPanel().clear();
		searchDisplay.getDisclosurePanel().setOpen(false);
		searchDisplay.getDisclosurePanelVSAC().setOpen(true);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public  void beforeDisplay() {
		displaySearch();
		setWidgetsReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
		MeasureComposerPresenter.setSubSkipEmbeddedLink("subContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
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
		MatValueSetTransferObject matValueSetTransferObject = new MatValueSetTransferObject();
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setDatatype(dataType);
		matValueSetTransferObject.setMatValueSet(searchDisplay.getCurrentMatValueSet());
		matValueSetTransferObject.setAppliedQDMList(appliedQDMList);
		matValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
		if ((searchDisplay.getDateInput().getValue() != null) && !searchDisplay.getDateInput().getValue().trim().isEmpty()) {
			if (searchDisplay.getVersion().getValue().equals(Boolean.TRUE)) {
				matValueSetTransferObject.setVersionDate(true);
				matValueSetTransferObject.setEffectiveDate(false);
			} else if (searchDisplay.getEffectiveDate().getValue().equals(Boolean.TRUE)) {
				matValueSetTransferObject.setEffectiveDate(true);
				matValueSetTransferObject.setVersionDate(false);
			} else {
				matValueSetTransferObject.setEffectiveDate(false);
				matValueSetTransferObject.setVersionDate(false);
			}
			matValueSetTransferObject.setQueryDate(searchDisplay.getDateInput().getValue());
		}
		matValueSetTransferObject.setMeasureId(measureID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}
	
	/**
	 * Method to show Widget on Create Element Tab.
	 */
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
	 * Gets the list of applied qdm.
	 *
	 * @param isUserDefined            - {@link Boolean}.
	 * @return the list of applied qd ms
	 */
	private void getListOfAppliedQDMs(final boolean isUserDefined) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId, true,
					new AsyncCallback<QualityDataModelWrapper>() {
				
				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
				
				@Override
				public void onSuccess(
						final QualityDataModelWrapper result) {
					appliedQDMList = result.getQualityDataDTO();
					addSelectedCodeListtoMeasure(isUserDefined);
					System.out.println("getListOfAppliedQDMs invoked");
				}
			});
		}
	}
	
	/**
	 * Gets the widget.
	 *
	 * @return {@link Widget}.
	 */
	@Override
	public final Widget getWidget() {
		return panel;
	}
	
	/**
	 * When retrieving value set from VSAC, "Loading Please Wait..." message is displayed.
	 * @return true if "Loading Please Wait..." message is displaying(In other words, when retrieving value set from VSAC)
	 * 	    else returns false;
	 */
	public boolean isBusyLoading() {
		return busyLoading;
	}
	
	/**
	 * Populates all data types from DB for Element's with and Element's without VSAC drop down.
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
						searchDisplay.setAllDataTypeOptions(result);
						searchDisplay
						.setDataTypesListBoxOptions(result);
					}
				});
	}

	private void saveMeasureXML(final String qdmXMLString, final String valuesetXMLString) {
		final String nodeName = "qdm";
		
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setMeasureModel(MatContext.get().getCurrentMeasureModel());
		exportModal.setParentNode("/measure/elementLookUp");
		exportModal.setToReplaceNode("qdm");
		
		MeasureXmlModel newExportModal = new MeasureXmlModel();
		newExportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		newExportModal.setMeasureModel(MatContext.get().getCurrentMeasureModel());
		newExportModal.setParentNode("/measure/cqlLookUp/valuesets");
		newExportModal.setToReplaceNode("valueset");
		
		System.out.println("XML " + qdmXMLString);
		exportModal.setXml(qdmXMLString);
		System.out.println("NEW XML " + valuesetXMLString);
		newExportModal.setXml(valuesetXMLString);
		
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
	
	private void searchValueSetInVsac(final String oid, final String version, final String effectiveDate) {
		
		searchDisplay.getValueSetDetailsPanel().setVisible(false);
		
		if (!MatContext.get().isUMLSLoggedIn()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN());
			return;
		}
		
		// OID validation.
		if ((oid == null) || oid.trim().isEmpty()) {
			searchDisplay.getErrorMessageDisplay().setMessage(
					MatContext.get().getMessageDelegate()
					.getUMLS_OID_REQUIRED());
			return;
		}
		
		// Version and EffectiveDate validation
		if ((searchDisplay.getVersion().getValue().equals(Boolean.TRUE)
				&& ((version == null) || version.trim().isEmpty()))
				|| (searchDisplay.getEffectiveDate().getValue().equals(Boolean.TRUE)
						&& ((effectiveDate == null) || effectiveDate.trim().isEmpty()))) {
			searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate()
					.getVSAC_VERSION_OR_EFFECTIVE_DATE_REQUIRED());
			return;
		}
	}

	private void setWidgetsReadOnly(final boolean editable) {
		// Widgets in "Element with VSAC Value Set" panel.
		searchDisplay.getRetrieveButton().setEnabled(editable);
		searchDisplay.getOIDInput().setEnabled(editable);
		searchDisplay.getVersion().setEnabled(editable);
		searchDisplay.getEffectiveDate().setEnabled(editable);
		// Widgets in "Element without VSAC Value Set" panel.
		searchDisplay.getUserDefinedInput().setEnabled(editable);
		searchDisplay.getAllDataTypeInput().setEnabled(editable);
		searchDisplay.getPsuedoQDMToMeasure().setEnabled(editable);
	}
}
