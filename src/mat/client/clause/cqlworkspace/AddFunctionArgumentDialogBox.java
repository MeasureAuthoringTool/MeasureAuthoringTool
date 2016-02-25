package mat.client.clause.cqlworkspace;

import java.util.List;
import mat.client.shared.ListBoxMVP;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLGrammarDataType;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;



public class AddFunctionArgumentDialogBox {
	private static List<String> argumentDataTypeList = CQLGrammarDataType.getDataTypeName();
	
	public static  void showArgumentDialogBox(final CQLModel currentModel, final CQLFunctionArgument functionArg, final boolean isEdit){
		String saveButtonText = "Add";
		String modalText = "Add Argument";
		if (isEdit) {
			saveButtonText = "Edit";
			modalText = "Edit Argument " + functionArg.getArgumentName();
		}
		Modal dialogModal = new Modal();
		dialogModal.setTitle(modalText);
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("AddEditArgument_Modal");
		dialogModal.setSize(ModalSize.SMALL);
		
		ModalBody modalBody = new ModalBody();
		
		final ListBoxMVP listAllDataTypes = new ListBoxMVP();
		listAllDataTypes.setWidth("250px");
		listAllDataTypes.addItem("-- Select--");
		for (int i = 0; i < argumentDataTypeList.size(); i++) {
			listAllDataTypes.addItem(argumentDataTypeList.get(i));
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
		argumentNameTextArea.setWidth("250px");
		argumentNameTextArea.setHeight("38px");
		
		
		argumentNameFormGroup.add(argumentNameFormLabel);
		argumentNameFormGroup.add(argumentNameTextArea);
		
		final FormGroup selectFormGroup = new FormGroup();
		FormLabel selectFormLabel = new FormLabel();
		selectFormLabel.setText("Select");
		selectFormLabel.setTitle("Select");
		selectFormLabel.setFor("listSelectItem");
		
		final ListBoxMVP listSelectItem = new ListBoxMVP();
		listSelectItem.setWidth("250px");
		listSelectItem.setEnabled(false);
		
		selectFormGroup.add(selectFormLabel);
		selectFormGroup.add(listSelectItem);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(dataTypeFormGroup);
		formFieldSet.add(argumentNameFormGroup);
		formFieldSet.add(selectFormGroup);
		bodyForm.add(formFieldSet);
		
		
		
		if(isEdit){
			String selectedDataType = null;
			for(int i=0;i<listAllDataTypes.getItemCount();i++){
				if(listAllDataTypes.getItemText(i).equalsIgnoreCase(functionArg.getArgumentType())){
					listAllDataTypes.setSelectedIndex(i);
					selectedDataType = functionArg.getArgumentType();
					break;
				}
			}
			if(selectedDataType.equalsIgnoreCase("definition")
					|| functionArg.getArgumentType().equalsIgnoreCase("parameter")){
				argumentNameTextArea.setEnabled(false);
				argumentNameTextArea.clear();
				if(selectedDataType.equalsIgnoreCase("definition")){
					addDefinitionInList(listSelectItem, currentModel.getDefinitionList());
					for(int i=0;i<listSelectItem.getItemCount();i++){
						if(listSelectItem.getItemText(i).equalsIgnoreCase(functionArg.getArgumentName())){
							listSelectItem.setSelectedIndex(i);
							break;
						}
					}
				} else if(selectedDataType.equalsIgnoreCase("parameter")){
					addParamInList(listSelectItem, currentModel.getCqlParameters());
					for(int i=0;i<listSelectItem.getItemCount();i++){
						if(listSelectItem.getItemText(i).equalsIgnoreCase(functionArg.getArgumentName())){
							listSelectItem.setSelectedIndex(i);
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
		
		listAllDataTypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				messageFormgroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setValidationState(ValidationState.NONE);
				argumentNameFormGroup.setValidationState(ValidationState.NONE);
				selectFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				if (listAllDataTypes.getValue().equalsIgnoreCase("definition")) {
					addDefinitionInList(listSelectItem, currentModel.getDefinitionList());
					listSelectItem.setEnabled(true);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(false);
				} else if (listAllDataTypes.getValue().equalsIgnoreCase("Parameter")) {
					addParamInList(listSelectItem, currentModel.getCqlParameters());
					listSelectItem.setEnabled(true);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(false);
				} else {
					listSelectItem.clear();
					listSelectItem.setEnabled(false);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(true);
				}
				
			}
			
		});
		
		listSelectItem.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				if (selectedIndex != 0) {
					String argumentDataType = listAllDataTypes.getItemText(selectedIndex);
					if (argumentDataType.equalsIgnoreCase("definition")
							|| argumentDataType.equalsIgnoreCase("parameter")) {
						int selectedItemIndex = listSelectItem.getSelectedIndex();
						if (selectedItemIndex != 0) {
							argumentName = listSelectItem.getItemText(selectedItemIndex);
						} else {
							selectFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Select definition/Parameter.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
							
						}
						
					} else {
						if (!argumentDataType.contains("Select")) {
							if (!argumentNameTextArea.getText().isEmpty()) {
								argumentName = argumentNameTextArea.getText();
							} else {
								argumentNameFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText("Argument name is required.");
								messageFormgroup.setValidationState(ValidationState.ERROR);
								
							}
						} else {
							dataTypeFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Datatype is required.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
						
					}
				} else {
					dataTypeFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Datatype is required.");
					messageFormgroup.setValidationState(ValidationState.ERROR);
					
				}
			}
		});
		
		dialogModal.show();
		
	}
	/**
	 * Method to add CQLParameter content in listSelectItem
	 * @param listSelectItem - ListMVP
	 * @param cqlParameters - List
	 */
	protected static void addParamInList(ListBoxMVP listSelectItem, List<CQLParameter> cqlParameters) {
		listSelectItem.clear();
		listSelectItem.addItem("-- Select--");
		for (CQLParameter param : cqlParameters) {
			listSelectItem.addItem(param.getParameterName());
		}
		
	}
	
	/**
	 * Method to add CQLDefinition content in listSelectItem
	 * @param listSelectItem - ListMVP
	 * @param definitionList - List
	 */
	protected static void addDefinitionInList(ListBoxMVP listSelectItem, List<CQLDefinition> definitionList) {
		listSelectItem.clear();
		listSelectItem.addItem("-- Select--");
		for (CQLDefinition define : definitionList) {
			listSelectItem.addItem(define.getDefinitionName());
		}
		
	}
	
}
