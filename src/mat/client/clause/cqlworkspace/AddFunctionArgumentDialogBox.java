package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.List;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.UUIDUtilClient;
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
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;



/**
 * @author jnarang
 *
 */
public class AddFunctionArgumentDialogBox {
	
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
	private static final String SELECT = "Select";
	
	/**
	 * List of Attributes.
	 */
	private static final List<String> attributeList = new ArrayList<String>();
	/**
	 * AttributeService Instance.
	 */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	
	/**
	 * @param functionArg - CQLFunctionArgument
	 * @param isEdit - {@link Boolean}
	 * @param searchDisplay - {@link CQLWorkSpaceView}
	 */
	public static  void showArgumentDialogBox( final CQLFunctionArgument functionArg,
			final boolean isEdit, final ViewDisplay searchDisplay) {
		List<String> allCqlDataType = MatContext.get().getCqlGrammarDataType().getCqlDataTypeList();
		final List<String> allDataTypes = MatContext.get().getDataTypeList();
		final ListBox attributeListBox = new ListBox(false);
		String saveButtonText = "Add";
		String modalText = "Add Argument";
		if (isEdit) {
			saveButtonText = "Edit";
			modalText = "Edit Argument: " + functionArg.getArgumentName();
			if (functionArg.getArgumentType().equalsIgnoreCase(
					CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
				attributeListBox.clear();
				attributeListBox.addItem(MatContext.get().PLEASE_SELECT);
				for (int i = 0; i < searchDisplay.getAvailableQDSAttributeList().size(); i++) {
					attributeListBox.addItem(searchDisplay.getAvailableQDSAttributeList().get(i).getName());
				}
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
		ModalBody modalBody = new ModalBody();
		final ListBoxMVP listAllDataTypes = new ListBoxMVP();
		listAllDataTypes.setWidth("290px");
		listAllDataTypes.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < allCqlDataType.size(); i++) {
			listAllDataTypes.addItem(allCqlDataType.get(i));
		}
		Form bodyForm = new Form();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		final FormGroup dataTypeFormGroup = new FormGroup();
		FormLabel availableDataFormLabel = new FormLabel();
		availableDataFormLabel.setText("Available Datatypes");
		availableDataFormLabel.setTitle("Available Datatypeser ID");
		availableDataFormLabel.setFor("listDataType");
		dataTypeFormGroup.add(availableDataFormLabel);
		dataTypeFormGroup.add(listAllDataTypes);
		final FormGroup argumentNameFormGroup = new FormGroup();
		FormLabel argumentNameFormLabel = new FormLabel();
		argumentNameFormLabel.setText("Argument Name");
		argumentNameFormLabel.setTitle("Argument Name");
		argumentNameFormLabel.setFor("inputArgumentName");
		final TextArea argumentNameTextArea = new TextArea();
		argumentNameTextArea.setPlaceholder("Enter Argument Name");
		argumentNameTextArea.setWidth("290px");
		argumentNameTextArea.setHeight("38px");
		argumentNameFormGroup.add(argumentNameFormLabel);
		argumentNameFormGroup.add(argumentNameTextArea);
		final FormGroup selectFormGroup = new FormGroup();
		FormLabel selectFormLabel = new FormLabel();
		selectFormLabel.setText("Select QDM Datatype Object");
		selectFormLabel.setTitle("Select QDM Datatype Object");
		selectFormLabel.setFor("listSelectItem");
		final ListBoxMVP listSelectItem = new ListBoxMVP();
		listSelectItem.setWidth("290px");
		listSelectItem.setEnabled(false);
		selectFormGroup.add(selectFormLabel);
		selectFormGroup.add(listSelectItem);
		final FormGroup selectAttributeFormGroup = new FormGroup();
		FormLabel selectAttributeFormGroupLabel = new FormLabel();
		selectAttributeFormGroupLabel.setText("Select Attribute");
		selectAttributeFormGroupLabel.setTitle("Select Attribute");
		selectAttributeFormGroupLabel.setFor("qdmAttributeDialog_attributeListBox");
		attributeListBox.getElement().setId("qdmAttributeDialog_attributeListBox");
		attributeListBox.setVisibleItemCount(1);
		attributeListBox.setWidth("290px");
		attributeListBox.addItem("");
		attributeListBox.setEnabled(false);
		selectAttributeFormGroup.add(selectAttributeFormGroupLabel);
		selectAttributeFormGroup.add(attributeListBox);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(dataTypeFormGroup);
		formFieldSet.add(argumentNameFormGroup);
		formFieldSet.add(selectFormGroup);
		formFieldSet.add(selectAttributeFormGroup);
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		addButton.setText(saveButtonText);
		addButton.setTitle(saveButtonText);
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		Button closeButton = new Button();
		closeButton.setText("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		if (isEdit) {
			String selectedDataType = null;
			for (int i = 0; i < listAllDataTypes.getItemCount(); i++) {
				if (listAllDataTypes.getItemText(i).equalsIgnoreCase(functionArg.getArgumentType())) {
					listAllDataTypes.setSelectedIndex(i);
					selectedDataType = functionArg.getArgumentType();
					break;
				}
			}
			if (selectedDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
				argumentNameTextArea.setEnabled(false);
				argumentNameTextArea.clear();
				populateAllDataType(listSelectItem, allDataTypes);
				listSelectItem.setEnabled(true);
				for (int i = 0; i < listSelectItem.getItemCount(); i++) {
					String itemValue = listSelectItem.getItemText(i);
					if (itemValue.equalsIgnoreCase(functionArg.getArgumentName())) {
						listSelectItem.setSelectedIndex(i);
						attributeListBox.setEnabled(true);
						break;
					}
				}
				if (functionArg.getAttributeName() != null) {
					for (int j = 0; j < attributeListBox.getItemCount(); j++) {
						if (attributeListBox.getItemText(j).equalsIgnoreCase(
								functionArg.getAttributeName())) {
							attributeListBox.setSelectedIndex(j);
							break;
						}
					}
				}
			} else {
				argumentNameTextArea.setEnabled(true);
				argumentNameTextArea.clear();
				argumentNameTextArea.setText(functionArg.getArgumentName());
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
				if (listAllDataTypes.getValue().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
					populateAllDataType(listSelectItem, allDataTypes);
					listSelectItem.setEnabled(true);
					attributeListBox.clear();
					attributeListBox.setEnabled(false);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(false);
					argumentNameTextArea.clear();
				} else {
					listSelectItem.clear();
					listSelectItem.setEnabled(false);
					attributeListBox.clear();
					attributeListBox.setEnabled(false);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(true);
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
				attributeListBox.clear();
				int selectedIndex = listSelectItem.getSelectedIndex();
				String itemValue = new String();
				if (selectedIndex != -1) {
					itemValue = listSelectItem.getItemText(selectedIndex);
				}
				if (!itemValue.contains(SELECT)) {
					getAttributesForDataType(itemValue, attributeListBox);
					attributeListBox.setEnabled(true);
				} else {
					attributeListBox.setEnabled(false);
					attributeListBox.clear();
				}
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
				String argumentName = null;
				String argumentDataType  = null;
				String attributeName = null;
				if (selectedIndex != 0) {
					argumentDataType = listAllDataTypes.getItemText(selectedIndex);
					if (argumentDataType.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
						int selectedItemIndex = listSelectItem.getSelectedIndex();
						if (selectedItemIndex != 0) {
							argumentName = listSelectItem.getItemText(selectedItemIndex);
						} else {
							selectFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText(MESSAGE_SELECT_QDM_DATATYPE);
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
						int selectedAttributeIndex = attributeListBox.getSelectedIndex();
						if (selectedAttributeIndex != 0) {
							attributeName = attributeListBox.getItemText(selectedAttributeIndex);
						}
					} else {
						if (!argumentDataType.contains(SELECT)) {
							if (!argumentNameTextArea.getText().isEmpty()) {
								argumentName = argumentNameTextArea.getText();
							} else {
								argumentNameFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText(MESSAGE_ARGUMENT_NAME_IS_REQUIRED);
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
						} else {
							dataTypeFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText(MESSAGE_CQL_DATATYPE_IS_REQUIRED);
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
					}
				} else {
					dataTypeFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText(MESSAGE_CQL_DATATYPE_IS_REQUIRED);
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				if ((argumentName != null) && (argumentDataType != null)) {
					if (isEdit) {
						for (int i = 0; i < searchDisplay.getFunctionArgumentList().size(); i++) {
							CQLFunctionArgument currentFunctionArgument = searchDisplay.
									getFunctionArgumentList().get(i);
							if (currentFunctionArgument.getId().equalsIgnoreCase(
									functionArg.getId())) {
								currentFunctionArgument.setArgumentName(argumentName);
								currentFunctionArgument.setArgumentType(argumentDataType);
								currentFunctionArgument.setAttributeName(attributeName);
								searchDisplay.createAddArgumentViewForFunctions(
										searchDisplay.getFunctionArgumentList());
								break;
							}
						}
					} else {
						functionArg.setArgumentName(argumentName);
						functionArg.setArgumentType(argumentDataType);
						functionArg.setId(UUIDUtilClient.uuid(16));
						functionArg.setAttributeName(attributeName);
						searchDisplay.getFunctionArgumentList().add(functionArg);
						searchDisplay.createAddArgumentViewForFunctions(searchDisplay.getFunctionArgumentList());
					}
					dialogModal.hide();
				}
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
		for (String dataType : allDataTypeList) {
			listDataType.addItem(dataType);
		}
	}
	/**
	 * Get All Attributes for selected datatype. This is for Non Edit flow.
	 * @param dataType - Selected Data type
	 * @param attributeListBox - Attribute ListBox
	 * */
	private static void getAttributesForDataType(String dataType, final ListBox attributeListBox) {
		attributeService.getAllAttributesByDataType(dataType,
				new AsyncCallback<List<QDSAttributes>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out
				.println("Error retrieving data type attributes. "
						+ caught.getMessage());
			}
			@Override
			public void onSuccess(List<QDSAttributes> result) {
				attributeList.clear();
				for (QDSAttributes qdsAttributes : result) {
					attributeList.add(qdsAttributes.getName());
				}
				attributeListBox.addItem(MatContext.get().PLEASE_SELECT);
				for (String attribName : attributeList) {
					attributeListBox.addItem(attribName);
				}
			}
		});
	}
}
