package mat.client.clause.cqlworkspace;

import java.util.List;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
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

public class InsertIntoAceEditorDialogBox {
	public static List<String> availableInsertItemList = CQLWorkSpaceConstants.getAvailableItem();
	public static List<String> timingList = MatContext.get().getCqlGrammarDataType().getCqlTimingList();
	public static List<String> cqlFunctionsList = MatContext.get().getCqlGrammarDataType().getCqlFunctionsList();
	
	/**
	 * @param searchDisplay
	 */
	public static  void showListOfParametersDialogBox(final ViewDisplay searchDisplay){
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Insert Item into Editor");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setSize(ModalSize.MEDIUM);
		ModalBody modalBody = new ModalBody();
		
		final ListBoxMVP availableItemToInsert = new ListBoxMVP();
		availableItemToInsert.clear();
		availableItemToInsert.setWidth("350px");
		addAvailableItems(availableItemToInsert);
		
		final ListBoxMVP listAllItemNames = new ListBoxMVP();
		listAllItemNames.setWidth("350px");
		listAllItemNames.clear();
		listAllItemNames.setEnabled(false);
		
		// main form
		Form bodyForm = new Form();
		// message Form group
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		// CQL Data Type Drop down Form group
		final FormGroup availableItemTypeFormGroup = new FormGroup();
		FormLabel availableParamFormLabel = new FormLabel();
		availableParamFormLabel.setText("Select Item type to insert");
		availableParamFormLabel.setTitle("Select Item type to insert");
		availableParamFormLabel.setFor("listAvailableItemType");
		availableItemTypeFormGroup.add(availableParamFormLabel);
		availableItemTypeFormGroup.add(availableItemToInsert);
		
		final FormGroup selectItemListFormGroup = new FormGroup();
		FormLabel selectItemListFormLabel = new FormLabel();
		selectItemListFormLabel.setText("Select Item Name to insert");
		selectItemListFormLabel.setTitle("Select Item Name to insert");
		selectItemListFormLabel.setFor("listItemType");
		selectItemListFormGroup.add(selectItemListFormLabel);
		selectItemListFormGroup.add(listAllItemNames);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(availableItemTypeFormGroup);
		formFieldSet.add(selectItemListFormGroup);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
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
		availableItemToInsert.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = availableItemToInsert.getSelectedIndex();
				if (selectedIndex != 0) {
					String itemTypeSelected = availableItemToInsert.getItemText(selectedIndex);
					if (itemTypeSelected.equalsIgnoreCase("parameters")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getViewParameterList().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getViewParameterList().get(i)
									.getParameterName());
						}
					} else if (itemTypeSelected.equalsIgnoreCase("functions")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getViewFunctions().size(); i++) {
							CQLFunctions functions = searchDisplay.getViewFunctions().get(i);
							StringBuilder functionNameBuilder = new StringBuilder(functions.getFunctionName());
							functionNameBuilder.append("(");
							StringBuilder argumentType = new StringBuilder();
							if(functions.getArgumentList() != null){
								for(int j=0;j<functions.getArgumentList().size();j++){
									CQLFunctionArgument argument = functions.getArgumentList().get(j);
									argumentType = argumentType.append(argument.getArgumentName() + " ");
									/*if (argument.getArgumentType().toString()
											.equalsIgnoreCase("QDM Datatype")) {
										if (argument.getAttributeName() != null) {
											argumentType = argumentType.append("\"").append(
													argument.getQdmDataType());
											argumentType = argumentType.append(".")
													.append(argument.getAttributeName())
													.append("\" ");
											
										}
									} else if (argument
											.getArgumentType()
											.toString()
											.equalsIgnoreCase(
													CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
										argumentType = argumentType.append(argument.getOtherType());
									} else {
										argumentType = argumentType.append(argument
												.getArgumentType());
									}*/
									if(j <  (functions.getArgumentList().size()-1)){
										argumentType.append(",");
									}
								}
							}
							functionNameBuilder.append(argumentType + ")");
							listAllItemNames.addItem(functionNameBuilder.toString());
						}
					} else if (itemTypeSelected.equalsIgnoreCase("definitions")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getViewDefinitions().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getViewDefinitions().get(i)
									.getDefinitionName());
						}
					} else if (itemTypeSelected.equalsIgnoreCase("timing")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						
						for (int i = 0; i < timingList.size(); i++) {
							listAllItemNames.addItem(timingList.get(i));
						}
					} else if (itemTypeSelected.equalsIgnoreCase("CQL Functions")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < cqlFunctionsList.size(); i++) {
							listAllItemNames.addItem(cqlFunctionsList.get(i));
						}
					} else if (itemTypeSelected.equalsIgnoreCase("Applied QDM")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getAppliedQdmList().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getAppliedQdmList().get(i).getCodeListName()
									+ "." + searchDisplay.getAppliedQdmList().get(i).getDataType());
						}
					} else {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(false);
					}
				} else {
					listAllItemNames.clear();
					listAllItemNames.setEnabled(false);
				}
			}
		});
		listAllItemNames.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
			
		});
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int selectedItemIndex = availableItemToInsert.getSelectedIndex();
				if(selectedItemIndex != 0){
					String itemTypeName = availableItemToInsert.getItemText(selectedItemIndex);
					if(!itemTypeName.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
						int selectedIndex = listAllItemNames.getSelectedIndex();
						if (selectedIndex != 0) {
							String itemNameToBeInserted = listAllItemNames.getItemTitle(selectedIndex);
							if(itemNameToBeInserted.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
								selectItemListFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText("Please Select Valid Parameter name to insert into Editor");
								messageFormgroup.setValidationState(ValidationState.ERROR);
							} else {
								int columnIndex = searchDisplay.getDefineAceEditor().getCursorPosition().getColumn();
								System.out.println(columnIndex);
								if(itemTypeName.equalsIgnoreCase("Applied QDM")){
									itemNameToBeInserted = " \"" + itemNameToBeInserted + "\"";
								}
								searchDisplay.getDefineAceEditor().insertAtCursor(" " + itemNameToBeInserted);
								dialogModal.hide();
							}
						} else {
							selectItemListFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Please Select Item name to insert into Editor");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
					} else {
						availableItemTypeFormGroup.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please Select Item Type to insert into Editor");
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
					
				}
			}
			
		});
		
		dialogModal.show();
	}
	
	private static void addAvailableItems(ListBoxMVP availableItemToInsert) {
		availableItemToInsert.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < availableInsertItemList.size(); i++) {
			availableItemToInsert.addItem(availableInsertItemList.get(i));
		}
		
	}
}
