package mat.client.cqlworkspace;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import mat.client.buttons.NoButton;
import mat.client.buttons.YesButton;
import mat.client.cqlworkspace.functions.CQLFunctionsView;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MessagePanel;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.CQLModelValidator;
import mat.shared.UUIDUtilClient;


public class AddFunctionArgumentDialogBox {
    private static Logger log = Logger.getLogger(AddFunctionArgumentDialogBox.class.getSimpleName());
    private static final String ARGUMENT_NAME_IS_NOT_UNIQUE = "Argument name already exists.";
    private static final String MESSAGE_CQL_DATATYPE_IS_REQUIRED = "CQL datatype is required.";
    private static final String MESSAGE_ARGUMENT_NAME_IS_REQUIRED = "Argument name is required.";
    private static final String MESSAGE_SELECT_QDM_DATATYPE = "Select QDM datatype.";
    private static ClickHandler handler;

    public static void showArgumentDialogBox(final CQLFunctionArgument functionArg,
                                             final boolean isEdit,
                                             final CQLFunctionsView cqlFunctionsView,
                                             final MessagePanel messagePanel,
                                             final boolean isEditable,
                                             String currentModelType) {
        List<String> allCqlDataType = MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlDataTypeList();
        final List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();
        final List<String> fhirDataTypes = MatContext.get().getCqlConstantContainer().getFhirCqlDataTypeList();

        log.log(Level.INFO,"fhirDataTypes: " +fhirDataTypes);

        final TextArea otherTypeTextArea = new TextArea();
        otherTypeTextArea.setEnabled(false);
        String saveButtonText = "Add";
        String modalText = "Add Argument";
        if (isEdit) {
            saveButtonText = "Apply";
            modalText = "Edit Argument: " + functionArg.getArgumentName();

            if (functionArg.getArgumentType().equalsIgnoreCase(
                    CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                otherTypeTextArea.setText(functionArg.getOtherType());
                otherTypeTextArea.setEnabled(true);
            }
        }
        final Modal dialogModal = new Modal();
        dialogModal.setTitle(modalText);
        dialogModal.setClosable(true);
        dialogModal.setFade(true);
        dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
        dialogModal.setDataKeyboard(true);
        dialogModal.setId("AddEditArgument_Modal");
        dialogModal.setSize(ModalSize.SMALL);
        dialogModal.setRemoveOnHide(true);

        if (handler == null) {
            handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
        }

        dialogModal.addDomHandler(handler, ClickEvent.getType());

        ModalBody modalBody = new ModalBody();
        final ListBoxMVP listAllDataTypes = new ListBoxMVP();
        listAllDataTypes.setWidth("290px");
        listAllDataTypes.addItem(MatContext.PLEASE_SELECT);
        for (int i = 0; i < allCqlDataType.size(); i++) {
            if (ModelTypeHelper.FHIR.equalsIgnoreCase(currentModelType) &&
                    CQLWorkSpaceConstants.CQL_QDM_DATA_TYPE.equalsIgnoreCase(allCqlDataType.get(i))) {
                //Overwrite FHIR data type with QDM.
                //This isn't ideal but its the easiest way to add in fhir at this point.
                listAllDataTypes.addItem(CQLWorkSpaceConstants.CQL_FHIR_DATA_TYPE);
            } else {
                listAllDataTypes.addItem(allCqlDataType.get(i));
            }
        }

        Form bodyForm = new Form();
        final FormGroup messageFormgroup = new FormGroup();
        final HelpBlock helpBlock = new HelpBlock();
        messageFormgroup.add(helpBlock);
        messageFormgroup.getElement().setAttribute("role", "alert");
        helpBlock.setId("helpBlock");
        final FormGroup dataTypeFormGroup = new FormGroup();
        FormLabel availableDataFormLabel = new FormLabel();
        availableDataFormLabel.setText("Available Datatypes");
        availableDataFormLabel.setTitle("Available Datatypes");
        availableDataFormLabel.setFor("listDataType");
        availableDataFormLabel.setId("availableDataFormLabel");
        listAllDataTypes.getElement().setId("listDataType");
        dataTypeFormGroup.add(availableDataFormLabel);
        dataTypeFormGroup.add(listAllDataTypes);
        final FormGroup argumentNameFormGroup = new FormGroup();
        FormLabel argumentNameFormLabel = new FormLabel();
        argumentNameFormLabel.setText("Argument Name");
        argumentNameFormLabel.setTitle("Argument Name");
        argumentNameFormLabel.setFor("inputArgumentName");
        argumentNameFormLabel.setId("ArgumentNameLanel");
        final TextBox argumentNameTextArea = new TextBox();
        argumentNameTextArea.setWidth("290px");
        argumentNameTextArea.setHeight("38px");
        argumentNameTextArea.getElement().setId("inputArgumentName");
        argumentNameFormGroup.add(argumentNameFormLabel);
        argumentNameFormGroup.add(argumentNameTextArea);
        final FormGroup otherTypeFormGroup = new FormGroup();
        FormLabel otherTypeFormLabel = new FormLabel();
        otherTypeFormLabel.setText("Other Type");
        otherTypeFormLabel.setTitle("Other Type");
        otherTypeFormLabel.setId("OtherTypeLable");
        otherTypeFormLabel.setFor("inputOtherType");
        otherTypeTextArea.setPlaceholder("Enter Other Type here.");
        otherTypeTextArea.setWidth("290px");
        otherTypeTextArea.setHeight("38px");
        otherTypeTextArea.setId("inputOtherType");
        otherTypeFormGroup.add(otherTypeFormLabel);
        otherTypeFormGroup.add(otherTypeTextArea);

        final FormGroup selectFormGroup = new FormGroup();
        FormLabel selectFormLabel = new FormLabel();
        if (ModelTypeHelper.FHIR.equalsIgnoreCase(currentModelType)) {
            selectFormLabel.setText("Select FHIR Datatype Object");
            selectFormLabel.setTitle("Select FHIR Datatype Object");
        } else {
            selectFormLabel.setText("Select QDM Datatype Object");
            selectFormLabel.setTitle("Select QDM Datatype Object");
        }
        selectFormLabel.setFor("listSelectItem");
        selectFormLabel.setId("selectFormLabel");
        final ListBoxMVP listSelectItem = new ListBoxMVP();
        listSelectItem.setWidth("290px");
        listSelectItem.getElement().setId("listSelectItem");
        listSelectItem.setEnabled(false);
        selectFormGroup.add(selectFormLabel);
        selectFormGroup.add(listSelectItem);

        FieldSet formFieldSet = new FieldSet();
        formFieldSet.add(messageFormgroup);
        formFieldSet.add(argumentNameFormGroup);
        formFieldSet.add(dataTypeFormGroup);
        formFieldSet.add(otherTypeFormGroup);
        formFieldSet.add(selectFormGroup);

        bodyForm.add(formFieldSet);
        modalBody.add(bodyForm);
        ModalFooter modalFooter = new ModalFooter();
        ButtonToolBar buttonToolBar = new ButtonToolBar();
        final Button addButton = new YesButton("addFxnArgsBox");
        addButton.setText(saveButtonText);
        addButton.setTitle(saveButtonText);
        Button closeButton = new NoButton("addFxnArgsBox");
        closeButton.setText("Close");
        closeButton.setTitle("Close");
        closeButton.setDataDismiss(ButtonDismiss.MODAL);
        buttonToolBar.add(addButton);
        buttonToolBar.add(closeButton);
        modalFooter.add(buttonToolBar);
        dialogModal.add(modalBody);
        dialogModal.add(modalFooter);

        if (isEdit) {
            log.log(Level.INFO,"isEdit: " +isEdit);
            String selectedDataType = null;
            argumentNameTextArea.setText(functionArg.getArgumentName());
            for (int i = 0; i < listAllDataTypes.getItemCount(); i++) {
                if (listAllDataTypes.getItemText(i).equalsIgnoreCase(functionArg.getArgumentType())) {
                    listAllDataTypes.setSelectedIndex(i);
                    selectedDataType = functionArg.getArgumentType();
                    break;
                }
            }
            if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_QDM_DATA_TYPE)) {
                log.log(Level.INFO,"CQL_MODEL_DATA_TYPE ");
                otherTypeTextArea.setEnabled(false);
                populateAllDataType(listSelectItem, allDataTypes);
                listSelectItem.setEnabled(true);
                for (int i = 0; i < listSelectItem.getItemCount(); i++) {
                    String itemValue = listSelectItem.getItemText(i);
                    if (itemValue.equalsIgnoreCase(functionArg.getQdmDataType())) {
                        listSelectItem.setSelectedIndex(i);
                        break;
                    }
                }
            } else if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FHIR_DATA_TYPE)) {
                log.log(Level.INFO,"CQL_FHIR_DATA_TYPE ");
                otherTypeTextArea.setEnabled(false);
                populateAllDataType(listSelectItem, fhirDataTypes);
                listSelectItem.setEnabled(true);
                for (int i = 0; i < listSelectItem.getItemCount(); i++) {
                    String itemValue = listSelectItem.getItemText(i);
                    if (itemValue.equalsIgnoreCase(functionArg.getQdmDataType())) {
                        listSelectItem.setSelectedIndex(i);
                        break;
                    }
                }
            } else if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                log.log(Level.INFO,"CQL_OTHER_DATA_TYPE ");
                listSelectItem.clear();
                listSelectItem.setEnabled(false);
                otherTypeTextArea.setText(functionArg.getOtherType());
                otherTypeTextArea.setEnabled(true);
            } else {
                argumentNameTextArea.setEnabled(true);
                otherTypeTextArea.setEnabled(false);
                listSelectItem.clear();
                listSelectItem.setEnabled(false);
            }
        }

        listAllDataTypes.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                messageFormgroup.setValidationState(ValidationState.NONE);
                dataTypeFormGroup.setValidationState(ValidationState.NONE);
                argumentNameFormGroup.setValidationState(ValidationState.NONE);
                selectFormGroup.setValidationState(ValidationState.NONE);
                helpBlock.setText("");
                if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_QDM_DATA_TYPE)) {
                    log.log(Level.INFO,"onChange  CQL_MODEL_DATA_TYPE");
                    otherTypeTextArea.setEnabled(false);
                    otherTypeTextArea.clear();
                    populateAllDataType(listSelectItem, allDataTypes);
                    listSelectItem.setEnabled(true);
                    addButton.setEnabled(true);
                } else if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FHIR_DATA_TYPE)) {
                    log.log(Level.INFO,"onChange  CQL_FHIR_DATA_TYPE");
                    otherTypeTextArea.setEnabled(false);
                    otherTypeTextArea.clear();
                    populateAllDataType(listSelectItem, fhirDataTypes);
                    listSelectItem.setEnabled(true);
                    addButton.setEnabled(true);
                } else if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                    log.log(Level.INFO,"onChange  CQL_OTHER_DATA_TYPE");
                    otherTypeTextArea.setEnabled(true);
                    otherTypeTextArea.clear();
                    listSelectItem.clear();
                    listSelectItem.setEnabled(false);
                } else {
                    listSelectItem.clear();
                    listSelectItem.setEnabled(false);
                    addButton.setEnabled(true);
                    otherTypeTextArea.setEnabled(false);
                    otherTypeTextArea.clear();
                }
            }
        });

        listSelectItem.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                messageFormgroup.setValidationState(ValidationState.NONE);
                dataTypeFormGroup.setValidationState(ValidationState.NONE);
                argumentNameFormGroup.setValidationState(ValidationState.NONE);
                selectFormGroup.setValidationState(ValidationState.NONE);
                helpBlock.setText("");
            }
        });

        argumentNameTextArea.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                messageFormgroup.setValidationState(ValidationState.NONE);
                dataTypeFormGroup.setValidationState(ValidationState.NONE);
                argumentNameFormGroup.setValidationState(ValidationState.NONE);
                selectFormGroup.setValidationState(ValidationState.NONE);
                helpBlock.setText("");
            }
        });

        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIndex = listAllDataTypes.getSelectedIndex();
                boolean isValid = true;
                String argumentName = null;
                String argumentDataType = null;
                String attributeName = null;
                String qdmDataType = null;
                String otherType = null;
                if (!argumentNameTextArea.getText().isEmpty()) {
                    argumentName = argumentNameTextArea.getText();
                    if (selectedIndex != 0) {
                        argumentDataType = listAllDataTypes.getItemText(selectedIndex);

                        if (argumentDataType.contains(CQLWorkSpaceConstants.CQL_QDM_DATA_TYPE) ||
                                argumentDataType.contains(CQLWorkSpaceConstants.CQL_FHIR_DATA_TYPE)) {
                            int selectedItemIndex = listSelectItem.getSelectedIndex();
                            if (selectedItemIndex != 0) {
                                //Reuse qdmDataType for FHIR data type as well.
                                //It's not idea, but it saves us from changing MAT xml which is a real pain.
                                qdmDataType = listSelectItem.getItemText(selectedItemIndex);
                            } else {
                                isValid = false;
                                selectFormGroup.setValidationState(ValidationState.ERROR);
                                helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
                                helpBlock.setText(MESSAGE_SELECT_QDM_DATATYPE);
                                messageFormgroup.setValidationState(ValidationState.ERROR);
                            }
                            log.log(Level.INFO,"Adding: qdmDataType=" + qdmDataType);
                        } else if (argumentDataType.equalsIgnoreCase(// Selected Data Type is Others
                                CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                            if (otherTypeTextArea.getText().isEmpty()) {
                                isValid = false;
                                otherTypeFormGroup.setValidationState(ValidationState.ERROR);
                                helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
                                helpBlock.setText("Other Type is not defined.");
                                messageFormgroup.setValidationState(ValidationState.ERROR);
                            } else {
                                otherType = otherTypeTextArea.getText();
                            }
                        }
                    } else {
                        isValid = false;
                        dataTypeFormGroup.setValidationState(ValidationState.ERROR);
                        helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
                        helpBlock.setText(MESSAGE_CQL_DATATYPE_IS_REQUIRED);
                        messageFormgroup.setValidationState(ValidationState.ERROR);
                    }
                } else {
                    isValid = false;
                    argumentNameFormGroup.setValidationState(ValidationState.ERROR);
                    helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
                    helpBlock.setText(MESSAGE_ARGUMENT_NAME_IS_REQUIRED);
                    messageFormgroup.setValidationState(ValidationState.ERROR);
                }

                if (isValid) {
                    boolean checkIfDuplicate = cqlFunctionsView.getFunctionArgNameMap().containsKey(argumentName.toLowerCase());
                    if (checkIfDuplicate && isEdit) {
                        CQLFunctionArgument tempArgObj = cqlFunctionsView.getFunctionArgNameMap().get(argumentName.toLowerCase());
                        if (tempArgObj.getId().equalsIgnoreCase(functionArg.getId())) {
                            // this means same object is modified.
                            checkIfDuplicate = false;
                        }
                    }
                    CQLModelValidator validator = new CQLModelValidator();
                    boolean isValidName = validator.doesAliasNameFollowCQLAliasNamingConvention(argumentName);
                    if (isValidName && !checkIfDuplicate) {
                        isValid = true;
                        helpBlock.setText("");
                        messageFormgroup.setValidationState(ValidationState.NONE);
                    } else {
                        isValid = false;
                        helpBlock.setText("");
                        argumentNameFormGroup.setValidationState(ValidationState.ERROR);
                        helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
                        if (checkIfDuplicate) {
                            helpBlock.setText(ARGUMENT_NAME_IS_NOT_UNIQUE);
                        } else if (!isValidName) {
                            helpBlock.setText(MessageDelegate.CQL_FUNCTION_ARGUMENT_NAME_ERROR);
                        }
                        messageFormgroup.setValidationState(ValidationState.ERROR);
                    }
                }

                if (isValid && (argumentName != null) && (argumentDataType != null)) {
                    if (isEdit) {
                        for (int i = cqlFunctionsView.getFunctionArgumentList().size() - 1; i >= 0; i--) {
                            CQLFunctionArgument currentFunctionArgument = cqlFunctionsView.
                                    getFunctionArgumentList().get(i);
                            if (currentFunctionArgument.getId().equalsIgnoreCase(
                                    functionArg.getId())) {
                                CQLFunctionArgument argument = currentFunctionArgument.clone();
                                argument.setArgumentName(argumentName);
                                argument.setArgumentType(argumentDataType);
                                argument.setAttributeName(attributeName);
                                argument.setQdmDataType(qdmDataType);
                                argument.setOtherType(otherType);
                                cqlFunctionsView.getFunctionArgNameMap().remove(
                                        currentFunctionArgument.getArgumentName());
                                cqlFunctionsView.getFunctionArgumentList().remove(i);
                                cqlFunctionsView.getFunctionArgumentList().add(argument);
                                cqlFunctionsView.createAddArgumentViewForFunctions(
                                        cqlFunctionsView.getFunctionArgumentList(), isEditable);
                                cqlFunctionsView.getFunctionArgNameMap().put(argumentName.toLowerCase(), argument);
                                messagePanel.getSuccessMessageAlert().createAlert(
                                        MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_ARG_MODIFY(argumentName));
                                break;
                            }
                        }
                    } else {
                        functionArg.setArgumentName(argumentName);
                        functionArg.setArgumentType(argumentDataType);
                        functionArg.setId(UUIDUtilClient.uuid(16));
                        functionArg.setAttributeName(attributeName);
                        functionArg.setQdmDataType(qdmDataType);
                        functionArg.setOtherType(otherType);
                        cqlFunctionsView.getFunctionArgumentList().add(functionArg);
                        cqlFunctionsView.createAddArgumentViewForFunctions(cqlFunctionsView.getFunctionArgumentList(), isEditable);
                        cqlFunctionsView.getFunctionArgNameMap().put(argumentName.toLowerCase(), functionArg);
                        messagePanel.getSuccessMessageAlert().createAlert(
                                MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_ARG_ADD(argumentName));
                    }
                    dialogModal.hide();
                }
                cqlFunctionsView.getAddNewArgument().setFocus(true);
            }
        });
        dialogModal.show();
    }

    private static void populateAllDataType(final ListBoxMVP listDataType, List<String> allDataTypeList) {
        listDataType.clear();
        listDataType.addItem(MatContext.PLEASE_SELECT);
        for (String dataType : allDataTypeList) {
            listDataType.addItem(dataType);
        }
    }
}
