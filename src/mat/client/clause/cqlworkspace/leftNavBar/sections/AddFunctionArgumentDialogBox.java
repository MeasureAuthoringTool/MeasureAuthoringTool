package mat.client.clause.cqlworkspace.leftNavBar.sections;

import java.util.List;

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
import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.clause.cqlworkspace.CQLWorkSpaceView;
import mat.client.clause.cqlworkspace.leftNavBar.CQLLeftNavBarPanelView;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.CQLModelValidator;
import mat.shared.UUIDUtilClient;



/**
 * @author jnarang
 *
 */
public class AddFunctionArgumentDialogBox {
	/**
	 * String Message - Argument name is not unique.
	 */
	private static final String ARGUMENT_NAME_IS_NOT_UNIQUE = "Argument name already exists.";

	/**
	 * String Message - CQL datatype is required.
	 */
	private static final String MESSAGE_CQL_DATATYPE_IS_REQUIRED = "CQL datatype is required.";
	/**
	 * String Message - Argument name is required.
	 */
	private static final String MESSAGE_ARGUMENT_NAME_IS_REQUIRED = "Argument name is required.";
	/**
	 * String Message - Select QDM datatype..
	 */
	private static final String MESSAGE_SELECT_QDM_DATATYPE = "Select QDM datatype.";
	
	/**
	 * Constant - SELECT
	 */
//	private static final String SELECT = "Select";
	
	/**
	 * List of Attributes.
	 */
	//private static final List<String> attributeList = new ArrayList<String>();
	/**
	 * AttributeService Instance.
	 */
	/*private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);*/
	/**
	 * @param functionArg - CQLFunctionArgument
	 * @param isEdit - {@link Boolean}
	 * @param searchDisplay - {@link CQLWorkSpaceView}
	 */
	public static  void showArgumentDialogBox(final CQLFunctionArgument functionArg,
			final boolean isEdit, final CQLFunctionsView cqlFunctionsView, final CQLLeftNavBarPanelView cqlLeftNavBarPanelView, final boolean isEditable) {
		List<String> allCqlDataType = MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlDataTypeList();
		final List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();
		
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
		ModalBody modalBody = new ModalBody();
		final ListBoxMVP listAllDataTypes = new ListBoxMVP();
		listAllDataTypes.setWidth("290px");
		listAllDataTypes.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < allCqlDataType.size(); i++) {
			listAllDataTypes.addItem(allCqlDataType.get(i));
		}
		// main form
		Form bodyForm = new Form();
		// message Form group
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		helpBlock.setId("helpBlock");
		// CQL Data Type Drop down Form group
		final FormGroup dataTypeFormGroup = new FormGroup();
		FormLabel availableDataFormLabel = new FormLabel();
		availableDataFormLabel.setText("Available Datatypes");
		availableDataFormLabel.setTitle("Available Datatypes");
		availableDataFormLabel.setFor("listDataType");
		availableDataFormLabel.setId("availableDataFormLabel");
		listAllDataTypes.getElement().setId("listDataType");
		dataTypeFormGroup.add(availableDataFormLabel);
		dataTypeFormGroup.add(listAllDataTypes);
		// Argument Name Form group
		final FormGroup argumentNameFormGroup = new FormGroup();
		FormLabel argumentNameFormLabel = new FormLabel();
		argumentNameFormLabel.setText("Argument Name");
		argumentNameFormLabel.setTitle("Argument Name");
		argumentNameFormLabel.setFor("inputArgumentName");
		argumentNameFormLabel.setId("ArgumentNameLanel");
		final TextBox argumentNameTextArea = new TextBox();
		//argumentNameTextArea.setPlaceholder("Enter Argument Name");
		argumentNameTextArea.setWidth("290px");
		argumentNameTextArea.setHeight("38px");
		argumentNameTextArea.getElement().setId("inputArgumentName");
		argumentNameFormGroup.add(argumentNameFormLabel);
		argumentNameFormGroup.add(argumentNameTextArea);
		// Other Type Form group
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
		// QDM Data types drop down Form group
		final FormGroup selectFormGroup = new FormGroup();
		FormLabel selectFormLabel = new FormLabel();
		selectFormLabel.setText("Select QDM Datatype Object");
		selectFormLabel.setTitle("Select QDM Datatype Object");
		selectFormLabel.setFor("listSelectItem");
		selectFormLabel.setId("selectFormLabel");
		final ListBoxMVP listSelectItem = new ListBoxMVP();
		listSelectItem.setWidth("290px");
		listSelectItem.getElement().setId("listSelectItem");
		listSelectItem.setEnabled(false);
		selectFormGroup.add(selectFormLabel);
		selectFormGroup.add(listSelectItem);
		// Main form field Set
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(argumentNameFormGroup);
		formFieldSet.add(dataTypeFormGroup);
		formFieldSet.add(otherTypeFormGroup);
		formFieldSet.add(selectFormGroup);
		//formFieldSet.add(selectAttributeFormGroup);
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
		// Set values for Edit flow.
		if (isEdit) {
			String selectedDataType = null;
			argumentNameTextArea.setText(functionArg.getArgumentName());
			for (int i = 0; i < listAllDataTypes.getItemCount(); i++) {
				if (listAllDataTypes.getItemText(i).equalsIgnoreCase(functionArg.getArgumentType())) {
					listAllDataTypes.setSelectedIndex(i);
					selectedDataType = functionArg.getArgumentType();
					break;
				}
			}
			if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
				otherTypeTextArea.setEnabled(false);
				populateAllDataType(listSelectItem, allDataTypes);
				listSelectItem.setEnabled(true);
				for (int i = 0; i < listSelectItem.getItemCount(); i++) {
					String itemValue = listSelectItem.getItemText(i);
					if (itemValue.equalsIgnoreCase(functionArg.getQdmDataType())) {
						listSelectItem.setSelectedIndex(i);
						//	attributeListBox.setEnabled(true);
						break;
					}
				}
			} else if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
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
		// CQL Data Types List box change handler.
		listAllDataTypes.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				messageFormgroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setValidationState(ValidationState.NONE);
				argumentNameFormGroup.setValidationState(ValidationState.NONE);
				selectFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					otherTypeTextArea.setEnabled(false);
					otherTypeTextArea.clear();
					populateAllDataType(listSelectItem, allDataTypes);
					listSelectItem.setEnabled(true);
					/*attributeListBox.clear();
					attributeListBox.setEnabled(false);*/
					addButton.setEnabled(true);
				} else if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
					otherTypeTextArea.setEnabled(true);
					otherTypeTextArea.clear();
					listSelectItem.clear();
					listSelectItem.setEnabled(false);
					/*attributeListBox.clear();
					attributeListBox.setEnabled(false);*/
				} else {
					listSelectItem.clear();
					listSelectItem.setEnabled(false);
					/*attributeListBox.clear();
					attributeListBox.setEnabled(false);*/
					addButton.setEnabled(true);
					otherTypeTextArea.setEnabled(false);
					otherTypeTextArea.clear();
				}
			}
		});
		// QDM Data Types List Box change handler.
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
		//Argument Text Area click handler
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
		// Add Button Click Handler
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex = listAllDataTypes.getSelectedIndex();
				boolean isValid = true;
				String argumentName = null;
				String argumentDataType  = null;
				String attributeName = null;
				String qdmDataType = null;
				String otherType = null;
				if (!argumentNameTextArea.getText().isEmpty()) {
					argumentName = argumentNameTextArea.getText();
					if (selectedIndex != 0) {
						argumentDataType = listAllDataTypes.getItemText(selectedIndex);
						// Selected data type is QDM Data Type
						if (argumentDataType.contains(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
							int selectedItemIndex = listSelectItem.getSelectedIndex();
							if (selectedItemIndex != 0) {
								qdmDataType =  listSelectItem.getItemText(selectedItemIndex);
								//	int selectedAttributeIndex = attributeListBox.getSelectedIndex();
								//if (selectedAttributeIndex != 0) {
								/*	attributeName = attributeListBox.
											getItemText(selectedAttributeIndex);*/
								//}
							} else {
								isValid = false;
								selectFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText(MESSAGE_SELECT_QDM_DATATYPE);
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
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
				// Argument Name Validation Check
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
					boolean isValidName = validator.validateForAliasNameSpecialChar(argumentName);
					if (isValidName && !checkIfDuplicate) {
						//&& !searchDisplay.getFuncNameTxtArea().getText().equalsIgnoreCase(argumentName)){
						isValid = true;
						helpBlock.setText("");
						messageFormgroup.setValidationState(ValidationState.NONE);
					} else {
						isValid = false;
						helpBlock.setText("");
						argumentNameFormGroup.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						if(checkIfDuplicate){
							helpBlock.setText(ARGUMENT_NAME_IS_NOT_UNIQUE);
						} else if(!isValidName){
							helpBlock.setText(MatContext.get().getMessageDelegate().getCqlFunctionArgumentNameError());
						}
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
				}
				// Add or Modify Argument if valid.
				if (isValid && (argumentName != null) && (argumentDataType != null)) {
					if (isEdit) { // Existing Function Argument.
						for (int i = cqlFunctionsView.getFunctionArgumentList().size()-1; i >= 0; i--) {
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
								cqlFunctionsView.createAddArgumentViewForFunctions(cqlFunctionsView.getFunctionArgumentList(),isEditable);
								cqlFunctionsView.getFunctionArgNameMap().put(argumentName.toLowerCase(), argument);
								cqlLeftNavBarPanelView.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_ARG_MODIFY(argumentName));
								break;
							}
						}
					} else { // New Argument
						functionArg.setArgumentName(argumentName);
						functionArg.setArgumentType(argumentDataType);
						functionArg.setId(UUIDUtilClient.uuid(16));
						functionArg.setAttributeName(attributeName);
						functionArg.setQdmDataType(qdmDataType);
						functionArg.setOtherType(otherType);
						cqlFunctionsView.getFunctionArgumentList().add(functionArg);
						cqlFunctionsView.createAddArgumentViewForFunctions(cqlFunctionsView.getFunctionArgumentList(),isEditable);
						cqlFunctionsView.getFunctionArgNameMap().put(argumentName.toLowerCase(), functionArg);
						cqlLeftNavBarPanelView.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getSUCESS_FUNCTION_ARG_ADD(argumentName));
					}
					dialogModal.hide();
				}
				//508 change for Function Argument section
				cqlFunctionsView.getAddNewArgument().setFocus(true);
			}
		});
		dialogModal.show();
	}
	/**
	 * Populate all data type.
	 * @param listDataType - listBox
	 * @param allDataTypeList - List of all available QDM Data types.
	 */
	private static void populateAllDataType(final ListBoxMVP listDataType, List<String> allDataTypeList) {
		listDataType.clear();
		listDataType.addItem(MatContext.PLEASE_SELECT);
		for (String dataType : allDataTypeList) {
			listDataType.addItem(dataType);
		}
	}
	
}
