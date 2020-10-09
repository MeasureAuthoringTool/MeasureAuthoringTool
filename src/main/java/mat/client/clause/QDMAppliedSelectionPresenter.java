package mat.client.clause;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.buttons.CustomButton;
import mat.client.clause.QDMAppliedSelectionView.Observer;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.util.MatTextBox;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.MatValueSetTransferObject;
import mat.shared.ConstantMessages;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import mat.vsac.model.ValueSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Deprecated
/*
 * This class is for code that is non maintained anymore. It should not be changed.
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
         * Sets the mat.vsac profile list box.
         */
        void setDefaultExpansionIdentifierListBox();

        /**
         * Gets the error message display.
         *
         * @return the error message display
         */
        ErrorMessageDisplayInterface getErrorMessageDisplay();

        /**
         * Reset mat.vsac value set widget.
         */
        void resetVSACValueSetWidget();

        /**
         * Builds the cell table.
         *
         * @param appliedListModel the applied list model
         * @param isEditable       the is editable
         */
        void buildAppliedQDMCellTable(QDSAppliedListModel appliedListModel, boolean isEditable);

        /**
         * Sets the observer.
         *
         * @param observer the new observer
         */
        void setObserver(Observer observer);


        /**
         * Gets the adds the new qdm button.
         *
         * @return the adds the new qdm button
         */
        //Button getCancelQDMButton();

        /**
         * Sets the profile list.
         *
         * @param profileList the new profile list
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
         * Gets the retrieve from mat.vsac button.
         *
         * @return the retrieve from mat.vsac button
         */
        //PrimaryButton getRetrieveFromVSACButton();

        /**
         * Gets the save button.
         *
         * @return the save button
         */
        //Button getSaveButton();

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
        TextBox getOIDInput();

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
         * Gets the update from mat.vsac button.
         *
         * @return the update from mat.vsac button
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
         * Gets the update mat.vsac error message panel.
         *
         * @return the update mat.vsac error message panel
         */
        ErrorMessageDisplay getUpdateVSACErrorMessagePanel();

        /**
         * Gets the update mat.vsac success message panel.
         *
         * @return the update mat.vsac success message panel
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

        CheckBox getDefaultExpIdentifierSel();

    }

    /**
     * The panel.
     */
    SimplePanel panel = new SimplePanel();

    /**
     * The search display.
     */
    SearchDisplay searchDisplay;

    /**
     * The service.
     */
    private MeasureServiceAsync service = MatContext.get().getMeasureService();

    /**
     * The applied qdm list.
     */
    private List<QualityDataSetDTO> appliedQDMList = new ArrayList<QualityDataSetDTO>();

    /**
     * The current mat value set.
     */
    private ValueSet currentValueSet;

    /**
     * The exp profile to all qdm.
     */
    private String expIdentifierToAllQDM = "";

    /**
     * The is modfied.
     */
    private boolean isModified = false;

    /**
     * The modify value set dto.
     */
    private QualityDataSetDTO modifyValueSetDTO;


    /**
     * The is all oids updated.
     */
    private boolean isAllOIDsUpdated = false;

    /**
     * Instantiates a new VSAC profile selection presenter.
     *
     * @param srchDisplay the srch display
     */
    public QDMAppliedSelectionPresenter(SearchDisplay srchDisplay) {
        searchDisplay = srchDisplay;
        addAllHandlers();
    }

    /**
     * Gets the applied qdm list.
     *
     * @param checkForSupplementData the check for supplement data
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
                                        || ConstantMessages.DEAD_OID
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
                                if (result.getVsacExpIdentifier() != null) {
                                    searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(false);
                                    searchDisplay.getVSACExpansionIdentifierListBox().clear();
                                    searchDisplay.getVSACExpansionIdentifierListBox().addItem(result.getVsacExpIdentifier());
                                    //CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
                                    //ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
                                    searchDisplay.getDefaultExpIdentifierSel().setValue(true);
                                    searchDisplay.getDefaultExpIdentifierSel().setEnabled(false);
                                    expIdentifierToAllQDM = result.getVsacExpIdentifier();
                                } else {
                                    expIdentifierToAllQDM = "";
                                }
                            } else {
                                if (result.getVsacExpIdentifier() != null) {
                                    searchDisplay.getVSACExpansionIdentifierListBox().setEnabled(true);
							/*searchDisplay.setExpIdentifierList(MatContext.get()
									.getExpProfileList());*/
                                    searchDisplay.setDefaultExpansionIdentifierListBox();
                                    for (int i = 0; i < searchDisplay.getVSACExpansionIdentifierListBox().getItemCount(); i++) {
                                        if (searchDisplay.getVSACExpansionIdentifierListBox().getItemText(i)
                                                .equalsIgnoreCase(result.getVsacExpIdentifier())) {
                                            searchDisplay.getVSACExpansionIdentifierListBox().setSelectedIndex(i);
                                            break;
                                        }
                                    }
                                    //CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
                                    //ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
                                    searchDisplay.getDefaultExpIdentifierSel().setEnabled(true);
                                    searchDisplay.getDefaultExpIdentifierSel().setValue(true);

                                    expIdentifierToAllQDM = result.getVsacExpIdentifier();
                                } else {
                                    searchDisplay.getDefaultExpIdentifierSel().setEnabled(true);
                                    expIdentifierToAllQDM = "";
                                }
                            }
                        }
                    });
        }
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

                searchDisplay.getSearchHeader().setText("Modify Applied QDM ( " + displayName
                        + " : " + result.getDataType() + " )");
                searchDisplay.getMainPanel().getElement().focus();
                if (result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
                    onModifyUserDefined(result);

                } else {
                    //set OID Based QDM's in Modify Panel
                    onModifyValueSetQDM(result);
                }

                /**
                 * to display dataType value in the selection list box
                 */
                for (int i = 0; i < searchDisplay.getDataTypesListBox().getItemCount(); i++) {
                    if (searchDisplay.getDataTypesListBox().getItemText(i)
                            .equalsIgnoreCase(result.getDataType())) {
                        searchDisplay.getDataTypesListBox().setSelectedIndex(i);
                        break;
                    }
                }

                /**
                 * if the datatype value is "attribute" then we are disabling specific occurrence
                 * checkbox field on the Search Panel and enabling it for other datatypes.
                 * */
                if (searchDisplay.getDataTypeText(
                        searchDisplay.getDataTypesListBox()).equalsIgnoreCase("attribute")) {
					/*searchDisplay.getSpecificOccChkBox().setEnabled(false);
					searchDisplay.getSpecificOccChkBox().setValue(false);*/
                }

            }


            /**
             * this functionality is to delete the existing QDM element
             * from the Applied QDM elements
             * */
            @Override
            public void onDeleteClicked(QualityDataSetDTO result, final int index) {
                resetQDSMsgPanel();
                if ((modifyValueSetDTO != null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())) {
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
                                    //Disabling the delete functionality from 5.0 onwards
                                    //deleteAndSaveMeasureXML(appliedQDMList, index);
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
     */
    private void addUpdateFromVSACHandler() {

        searchDisplay.getUpdateFromVSACButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
                    searchDisplay.getInProgressMessageDisplay().setMessage("Loading Please Wait...");
                    resetQDSMsgPanel();
                    //updateVSACValueSets();

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
		/*searchDisplay.getCancelQDMButton().addClickHandler(new ClickHandler() {
			
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
				searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
				searchDisplay.getVersionListBox().setEnabled(false);
				searchDisplay.getDataTypesListBox().setEnabled(false);
				searchDisplay.getSaveButton().setEnabled(false);
				
			}
		});
		*/
        /**
         * this functionality is to retrieve the value set from VSAC with latest information which
         * consists of Expansion Identifier list and Version List.
         * */
		/*searchDisplay.getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					resetQDSMsgPanel();
					String version = null;
					String expansionProfile = null;
					searchValueSetInVsac(version, expansionProfile);
				}
			}
		});*/


        /**
         * this handler is invoked when apply button is clicked on search Panel in
         * QDM elements tab and this is to add new value set or user Defined QDM to the
         * Applied QDM list.
         * */
		/*searchDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			
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
		});*/


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
                if (searchDisplay.getDataTypeText(
                        searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
                    //searchDisplay.getSaveButton().setEnabled(false);
                } else {
                    //searchDisplay.getSaveButton().setEnabled(true);
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
                if (!searchDisplay.getExpansionIdentifierValue(
                        searchDisplay.getQDMExpIdentifierListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
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
                if (!searchDisplay.getVersionValue(
                        searchDisplay.getVersionListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
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
                if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
                    //CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
                    //ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
                    if (!searchDisplay.getVSACExpansionIdentifierListBox().getValue().equalsIgnoreCase("--Select--")) {
                        expIdentifierToAllQDM = searchDisplay.getVSACExpansionIdentifierListBox().getValue();
                        updateAllQDMsWithExpProfile(appliedQDMList);
                    } else if (!searchDisplay.getDefaultExpIdentifierSel().getValue()) {
                        expIdentifierToAllQDM = "";
                        updateAllQDMsWithExpProfile(appliedQDMList);
                    } else {
                        searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
                                .getMessageDelegate().getVsacExpansionProfileSelection());
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
        searchDisplay.getDefaultExpIdentifierSel().addValueChangeHandler(
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
							/*searchDisplay.setExpIdentifierList(MatContext.get()
									.getExpProfileList());*/
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
        String measureId = MatContext.get().getCurrentMeasureId();
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
                if (searchDisplay.getDefaultExpIdentifierSel().getValue()) {
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
        String measureId = MatContext.get().getCurrentMeasureId();
        service.updateMeasureXMLForExpansionIdentifier(modifiedQDMList, measureId, expIdentifierToAllQDM,
                new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void result) {
                        getAppliedQDMList(true);
                        //CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
                        //ToggleSwitch useDefaultExpToggelSwitch = searchDisplay.getToggleSwitch();
                        if (!searchDisplay.getDefaultExpIdentifierSel().getValue()) {
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
    private void validateUserDefinedInput() {
        if (searchDisplay.getUserDefinedInput().getValue().length() > 0) {
            searchDisplay.getOIDInput().setEnabled(true);
            searchDisplay.getUserDefinedInput().setTitle(searchDisplay.getUserDefinedInput().getValue());
            searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
            searchDisplay.getVersionListBox().setEnabled(false);
            searchDisplay.getDataTypesListBox().setEnabled(true);
        } else {
            searchDisplay.getUserDefinedInput().setTitle("Enter Name");
            searchDisplay.getOIDInput().setEnabled(true);
            searchDisplay.getDataTypesListBox().setEnabled(false);
            searchDisplay.getDataTypesListBox().setSelectedIndex(0);
        }
    }


    /**
     * Validate oid input.
     * depending on the OID input we are disabling and
     * enabling the fields in Search Panel
     */
    private void validateOIDInput() {
        if (searchDisplay.getOIDInput().getValue().length() > 0) {
            searchDisplay.getUserDefinedInput().setEnabled(false);
            //searchDisplay.getSaveButton().setEnabled(false);
            //searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
        } else if (searchDisplay.getUserDefinedInput().getValue().length() > 0) {
            searchDisplay.getQDMExpIdentifierListBox().clear();
            searchDisplay.getVersionListBox().clear();
            searchDisplay.getUserDefinedInput().setEnabled(true);
            if (!searchDisplay.getDataTypeText(
                    searchDisplay.getDataTypesListBox()).equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
                //searchDisplay.getSaveButton().setEnabled(true);
            }
        } else {
            searchDisplay.getUserDefinedInput().setEnabled(true);
        }
    }


    /*
     * checks if existing QDMList already has the name you are about to save.
     */
    private boolean CheckNameInQDMList(String userDefinedInput, String oidCode, String DataTypeName, String expId, String version, boolean isQDMModified) {
        if (appliedQDMList.size() > 0) {
            Iterator<QualityDataSetDTO> iterator = appliedQDMList
                    .iterator();
            while (iterator.hasNext()) {
                QualityDataSetDTO dataSetDTO = iterator
                        .next();
                if (dataSetDTO.getExpansionIdentifier() == null) {
                    dataSetDTO.setExpansionIdentifier("");
                }
                if (dataSetDTO.getVersion() == null) {
                    dataSetDTO.setVersion("");
                }
                if (isQDMModified) {
                    if (modifyValueSetDTO.getCodeListName().equalsIgnoreCase(userDefinedInput)) {
                        if (nameCountInQDMList(userDefinedInput) > 1) {
                            if (!modifyValueSetDTO.getOid().equalsIgnoreCase(oidCode)) {
                                searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
                                        .getMessageDelegate().getERROR_IN_SAVING_QDM_ELEMENTS());
                                return true;
                            }
                        } else {
                            if (modifyValueSetDTO.getDataType().equalsIgnoreCase(DataTypeName)) {
                                if (modifyValueSetDTO.getExpansionIdentifier().equalsIgnoreCase(expId) && modifyValueSetDTO.getVersion().equalsIgnoreCase(version)) {
                                    searchDisplay.getErrorMessageDisplay().setMessage(
                                            MatContext.get().getMessageDelegate()
                                                    .getDuplicateAppliedValueSetMsg(modifyValueSetDTO.getCodeListName()));
                                    return true;
                                }
                            }
                        }
                    } else {
                        if (dataSetDTO.getCodeListName().equalsIgnoreCase(userDefinedInput)) {
                            if (!dataSetDTO.getOid().equalsIgnoreCase(oidCode)) {
                                searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
                                        .getMessageDelegate().getERROR_IN_SAVING_QDM_ELEMENTS());
                                return true;
                            } else {
                                if (dataSetDTO.getDataType().equalsIgnoreCase(DataTypeName)) {
                                    if (dataSetDTO.getExpansionIdentifier().equalsIgnoreCase(expId) && dataSetDTO.getVersion().equalsIgnoreCase(version)) {
                                        searchDisplay.getErrorMessageDisplay().setMessage(
                                                MatContext.get().getMessageDelegate()
                                                        .getDuplicateAppliedValueSetMsg(dataSetDTO.getCodeListName()));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (dataSetDTO.getCodeListName().equalsIgnoreCase(userDefinedInput)) {
                        if (!dataSetDTO.getOid().equalsIgnoreCase(oidCode)) {
                            searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
                                    .getMessageDelegate().getERROR_IN_SAVING_QDM_ELEMENTS());
                            return true;
                        } else {
                            if (dataSetDTO.getDataType().equalsIgnoreCase(DataTypeName)) {
                                if (dataSetDTO.getExpansionIdentifier().equalsIgnoreCase(expId) || dataSetDTO.getVersion().equalsIgnoreCase(version)) {
                                    searchDisplay.getErrorMessageDisplay().setMessage(
                                            MatContext.get().getMessageDelegate()
                                                    .getDuplicateAppliedValueSetMsg(dataSetDTO.getCodeListName()));
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int nameCountInQDMList(String userDefinedInput) {
        int count = 0;
        Iterator<QualityDataSetDTO> qdmIterator = appliedQDMList
                .iterator();
        while (qdmIterator.hasNext()) {
            if (qdmIterator.next().getCodeListName().equals(userDefinedInput)) {
                count++;
            }
        }
        return count;
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
            String usrDefinedDataTypeName = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
            final String usrDefDisplayName = searchDisplay.getUserDefinedInput().getText();
            final String userDefinedOID = ConstantMessages.USER_DEFINED_QDM_OID;
            String expIdentifier = searchDisplay.getExpansionIdentifierValue(searchDisplay.getDataTypesListBox());
            String version = searchDisplay.getVersionValue(searchDisplay.getDataTypesListBox());
            if (expIdentifier == null) {
                expIdentifier = "";
            }
            if (version == null) {
                version = "";
            }
            if (!CheckNameInQDMList(usrDefDisplayName, userDefinedOID, usrDefinedDataTypeName, expIdentifier, version, isModified)) {
                MatValueSetTransferObject object = new MatValueSetTransferObject();
                object.setUserDefinedText(searchDisplay.getUserDefinedInput().getText());
                object.scrubForMarkUp();
                List<String> meStrings = new ArrayList<String>();
                //qdmInputValidator.validate(object);
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
            }
        } else {
            searchDisplay.getErrorMessageDisplay().setMessage(
                    MatContext.get().getMessageDelegate().getVALIDATION_MSG_ELEMENT_WITHOUT_VSAC());
        }
    }

    /**
     * Update applied qdm list.
     *
     * @param ValueSet          the mat value set
     * @param codeListSearchDTO    the code list search dto
     * @param qualityDataSetDTO    the quality data set dto
     * @param dataType             the data type
     * @param isSpecificOccurrence the is specific occurrence
     * @param isUSerDefined        the is u ser defined
     */
    private void updateAppliedQDMList(final ValueSet ValueSet, final CodeListSearchDTO codeListSearchDTO,
                                      final QualityDataSetDTO qualityDataSetDTO, final String dataType, final Boolean isSpecificOccurrence,
                                      final boolean isUSerDefined) {

        modifyQDMList(qualityDataSetDTO);
        String version = searchDisplay.getVersionValue(searchDisplay.getVersionListBox());
        String expansionProfile = searchDisplay.getExpansionIdentifierValue(
                searchDisplay.getQDMExpIdentifierListBox());
        MatValueSetTransferObject ValueSetTransferObject = new MatValueSetTransferObject();
        ValueSetTransferObject.setMeasureId(MatContext.get().getCurrentMeasureId());
        ValueSetTransferObject.setDatatype(dataType);
        ValueSetTransferObject.setValueSet(ValueSet);
        ValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
        ValueSetTransferObject.setQualityDataSetDTO(qualityDataSetDTO);
        ValueSetTransferObject.setAppliedQDMList(appliedQDMList);
        ValueSetTransferObject.setSpecificOccurrence(isSpecificOccurrence);
        if ((version != null) || (expansionProfile != null)) {
            if (!expansionProfile.equalsIgnoreCase(MatContext.PLEASE_SELECT)
                    && !expansionProfile.equalsIgnoreCase("")) {
                ValueSetTransferObject.setExpansionProfile(true);
                ValueSetTransferObject.setVersionDate(false);
                currentValueSet.setExpansionProfile(searchDisplay
                        .getQDMExpIdentifierListBox().getValue());

            } else if (!version.equalsIgnoreCase(MatContext.PLEASE_SELECT)
                    && !version.equalsIgnoreCase("")) {
                ValueSetTransferObject.setVersionDate(true);
                ValueSetTransferObject.setExpansionProfile(false);
                currentValueSet.setVersion(searchDisplay.getVersionListBox().getValue());
            }
        }

        if (!expIdentifierToAllQDM.isEmpty() && !isUSerDefined) {
            currentValueSet.setExpansionProfile(expIdentifierToAllQDM);
            currentValueSet.setVersion("1.0");
            ValueSetTransferObject.setExpansionProfile(true);
            ValueSetTransferObject.setVersionDate(false);
        }
        ValueSetTransferObject.scrubForMarkUp();
        MatContext.get().getCodeListService().updateCodeListToMeasure(ValueSetTransferObject,
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

                        if (result.isSuccess()) {
                            isModified = false;
                            appliedQDMList = result.getAppliedQDMList();
                            isAllOIDsUpdated = result.isAllOIDsUpdated();
                            updateMeasureXML(result.getDataSetDTO(), qualityDataSetDTO, isUSerDefined);
                            resetQDMSearchPanel();
                        } else {

                            if (result.getFailureReason() == SaveUpdateCodeListResult.ALREADY_EXISTS) {
                                if (!isUSerDefined) {
                                    searchDisplay.getErrorMessageDisplay().setMessage(
                                            MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCodeListName()));
                                } else {
                                    searchDisplay.getErrorMessageDisplay().setMessage(
                                            MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(result.getCodeListName()));
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
    private void resetQDMSearchPanel() {
        searchDisplay.getSearchHeader().setText("Search");
        searchDisplay.getOIDInput().setEnabled(true);
        searchDisplay.getOIDInput().setValue("");
        searchDisplay.getUserDefinedInput().setEnabled(true);
        searchDisplay.getUserDefinedInput().setValue("");
        searchDisplay.getQDMExpIdentifierListBox().clear();
        searchDisplay.getVersionListBox().clear();
        searchDisplay.getDataTypesListBox().setSelectedIndex(0);
        searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
        searchDisplay.getVersionListBox().setEnabled(false);
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
                        messages.add(MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
                        if (isAllOIDsUpdated) {
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
        ValueSet modifyWithDTO = currentValueSet;
        if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
            String dataType;
            String dataTypeText;
            String expansionId;
            String version;
            Boolean isSpecificOccurrence = false;
            String displayName = searchDisplay.getUserDefinedInput().getText();
            String oidNumber = searchDisplay.getOIDInput().getText();
            dataType = searchDisplay.getDataTypeValue(searchDisplay.getDataTypesListBox());
            dataTypeText = searchDisplay.getDataTypeText(searchDisplay.getDataTypesListBox());
            expansionId = searchDisplay.getExpansionIdentifierValue(searchDisplay.getQDMExpIdentifierListBox());
            version = searchDisplay.getVersionValue(searchDisplay.getVersionListBox());
            if (expansionId == null) {
                expansionId = "";
            }
            if (version == null) {
                version = "";
            }
            if (modifyValueSetDTO.getExpansionIdentifier() == null) {
                modifyValueSetDTO.setExpansionIdentifier("");
            }
            if (modifyValueSetDTO.getVersion() == null) {
                modifyValueSetDTO.setVersion("");
            }
            if (!CheckNameInQDMList(displayName, oidNumber, dataTypeText, expansionId, version, isModified)) {
                if (modifyValueSetDTO.getDataType().equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
                        || dataTypeText.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {

                    if (dataTypeText.equalsIgnoreCase(modifyValueSetDTO.getDataType())) {
                        if (modifyWithDTO.getID().equalsIgnoreCase(modifyValueSetDTO.getOid())
                                && (modifyValueSetDTO.isSpecificOccurrence() && isSpecificOccurrence)) {
                            searchDisplay.getSuccessMessageDisplay().setMessage(
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
                            && (modifyValueSetDTO.isSpecificOccurrence() && isSpecificOccurrence)) {
                        if (!expansionId.isEmpty() && expansionId.equalsIgnoreCase(
                                modifyValueSetDTO.getExpansionIdentifier())) {
                            resetQDMSearchPanel();
                            searchDisplay.getSuccessMessageDisplay().setMessage(
                                    MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
                        } else if (!version.isEmpty() && version.equalsIgnoreCase(
                                modifyValueSetDTO.getVersion())) {
                            resetQDMSearchPanel();
                            searchDisplay.getSuccessMessageDisplay().setMessage(
                                    MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
                        } else if ((modifyValueSetDTO.getVersion().equals("1.0") ||
                                modifyValueSetDTO.getVersion().equals("1"))
                                && expansionId.isEmpty() && version.isEmpty()
                                && (modifyValueSetDTO.getExpansionIdentifier() == null)) {
                            resetQDMSearchPanel();
                            searchDisplay.getSuccessMessageDisplay().setMessage(
                                    MatContext.get().getMessageDelegate().getSUCCESSFUL_MODIFY_APPLIED_VALUESET());
                        } else {
                            updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
                        }

                    } else {
                        updateAppliedQDMList(modifyWithDTO, null, modifyValueSetDTO, dataType, isSpecificOccurrence, false);
                    }
                }
            }
        } else {
            searchDisplay.getErrorMessageDisplay().setMessage(MatContext.get().
                    getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
        }
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
        MeasureComposerPresenter
                .setSubSkipEmbeddedLink("subQDMAPPliedListContainerPanel");
        //Mat.focusSkipLists("MeasureComposure");

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
        setWidgetsReadOnly(false);
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
        //searchDisplay.getSaveButton().setEnabled(false);
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
    private void setWidgetsReadOnly(boolean editable) {
        searchDisplay.getQDMExpIdentifierListBox().setEnabled(editable);
        searchDisplay.getVersionListBox().setEnabled(editable);
        searchDisplay.getDataTypesListBox().setEnabled(editable);
        searchDisplay.getOIDInput().setEnabled(editable);
        searchDisplay.getUserDefinedInput().setEnabled(editable);
        searchDisplay.getApplyDefaultExpansionIdButton().setEnabled(editable);

        //searchDisplay.getCancelQDMButton().setEnabled(editable);
        //searchDisplay.getRetrieveFromVSACButton().setEnabled(editable);
        //searchDisplay.getSaveButton().setEnabled(editable);
        searchDisplay.getUpdateFromVSACButton().setEnabled(editable);
        //CustomBootStrapCheckBox chkBox = (CustomBootStrapCheckBox)searchDisplay.getDefaultExpIDInput();
        //ToggleSwitch chkBox = searchDisplay.getToggleSwitch();
        //searchDisplay.getToggleSwitch().setEnabled(editable);
        searchDisplay.buildPasteBottomPanel(editable);
        searchDisplay.buildPasteTopPanel(editable);
        searchDisplay.getDefaultExpIdentifierSel().setEnabled(editable);

    }


    /**
     * Copy. This is to copy selected QDM elements from Applied QDM Elements and can be
     * available to paste it globally for multiple measures.
     * the copied instance remains through out the session until or unless we make
     * a new copy or the session is out.
     */
    private void copy() {
        resetQDSMsgPanel();
    }


    /**
     * Paste. This functionality is to paste all the QDM elements that have been copied
     * from any Measure and can be pasted to any measure.
     */
    private void paste() {

        resetQDSMsgPanel();
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
    private void onModifyUserDefined(QualityDataSetDTO result) {

        searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
        searchDisplay.getUserDefinedInput().setTitle(result.getCodeListName());
        searchDisplay.getUserDefinedInput().setEnabled(true);
        searchDisplay.getVersionListBox().setEnabled(false);
        searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
        //	searchDisplay.getRetrieveFromVSACButton().setEnabled(false);
        //searchDisplay.getSaveButton().setEnabled(true);
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
    private void onModifyValueSetQDM(QualityDataSetDTO result) {

        searchDisplay.getUserDefinedInput().setEnabled(false);
        searchDisplay.getUserDefinedInput().setValue(result.getCodeListName());
        searchDisplay.getUserDefinedInput().setTitle(result.getCodeListName());
        searchDisplay.getOIDInput().setEnabled(true);
        searchDisplay.getOIDInput().setValue(result.getOid());
        searchDisplay.getOIDInput().setTitle(result.getOid());
        //	searchDisplay.getSaveButton().setEnabled(false);
        searchDisplay.getDataTypesListBox().setEnabled(false);
        //	searchDisplay.getRetrieveFromVSACButton().setEnabled(true);
        searchDisplay.getVersionListBox().clear();
        searchDisplay.getQDMExpIdentifierListBox().clear();
        searchDisplay.getQDMExpIdentifierListBox().setEnabled(false);
        searchDisplay.getVersionListBox().setEnabled(false);

        if (!expIdentifierToAllQDM.isEmpty()) {
            searchDisplay.getQDMExpIdentifierListBox().clear();
            searchDisplay.getQDMExpIdentifierListBox().addItem(expIdentifierToAllQDM,
                    expIdentifierToAllQDM);
        }
    }

}
