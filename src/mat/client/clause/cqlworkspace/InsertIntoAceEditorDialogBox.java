package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.shared.CustomDateTimeTextBox;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.model.ModeDetailModel;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.shared.CQLIdentifierObject;

// TODO: Auto-generated Javadoc
/**
 * The Class InsertIntoAceEditorDialogBox.
 */
public class InsertIntoAceEditorDialogBox {
	
	/**
	 * List of availableInsertItemList.
	 */
	private  List<String> availableInsertItemList = CQLWorkSpaceConstants.getAvailableItem();
	
	/** The all data types. */
	private  List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();
		
	/** The all attributes. */
	private  List<String> allAttributes = MatContext.get().getCqlConstantContainer().getCqlAttributeList();
	
	private  List<String> allTimings = MatContext.get().getCqlConstantContainer().getCqlTimingList();


	
	/** The attribute service. */
	private  QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	

	
	
	/**
	 * List of timingList.
	 */
	//private  List<String> timingList = MatContext.get().getCqlGrammarDataType().getCqlTimingList();
	/**
	 * List of cqlFunctionsList.
	 */
	private  List<String> cqlFunctionsList = MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlFunctionsList();
	
	/** The Constant INSERT_AT_END. */
	private  final int INSERT_AT_END = -1;
	
	private  AceEditor curEditor; 
	
	private  final String BIRTH_DATE = "Birthdate";
	private  final String DEAD = "Dead";
	private  final String PATIENT_CHARACTERISTIC_BIRTHDATE = "Patient Characteristic Birthdate";
	private  final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";

	/**
	 * Public  method to build Pop up for Insert into Ace Editor.
	 * @param searchDisplay - ViewDisplay.
	 * @param currentSection - String.
	 */
	public   void showListOfItemAvailableForInsertDialogBox(final CQLLeftNavBarPanelView cqlNavBarView, final AceEditor editor) {
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Insert Item into CQL Editor");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setSize(ModalSize.MEDIUM);
		ModalBody modalBody = new ModalBody();
		curEditor = editor;
		final ListBoxMVP availableItemToInsert = new ListBoxMVP();
		availableItemToInsert.clear();
		availableItemToInsert.setWidth("350px");
		availableItemToInsert.getElement().setId("availableItemToInsert_ListBox");
		addAvailableItems(availableItemToInsert, availableInsertItemList);
		
		final ListBoxMVP listAllItemNames = new ListBoxMVP();
		listAllItemNames.setWidth("350px");
		listAllItemNames.clear();
		listAllItemNames.setEnabled(false);
		listAllItemNames.getElement().setId("listAllItemNames_ListBox");
		
		final ListBoxMVP availableDatatypes = new ListBoxMVP();
		availableDatatypes.clear();
		availableDatatypes.setWidth("350px");
		availableDatatypes.setEnabled(false);
		availableDatatypes.getElement().setId("availableDatatypes_ListBox");
		
		
		
		final ListBoxMVP allQDMDatatypes = new ListBoxMVP();
		allQDMDatatypes.clear();
		allQDMDatatypes.setWidth("350px");
		allQDMDatatypes.setEnabled(true);
		allQDMDatatypes.getElement().setId("allQDMDatatypes_ListBox");
		addAvailableItems(allQDMDatatypes, allDataTypes);
		
		final ListBoxMVP availableAttributesToInsert = new ListBoxMVP();
		availableAttributesToInsert.clear();
		availableAttributesToInsert.setWidth("350px");
		availableAttributesToInsert.setEnabled(false);
		availableAttributesToInsert.getElement().setId("availableAttributesToInsert_ListBox");
		
		
		// Based on Current Section this method will reterive instance of Ace Editor.
		//final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
		
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
		availableParamFormLabel.setText("Item Type");
		availableParamFormLabel.setTitle("Select Item type to insert");
		availableParamFormLabel.setFor("listAvailableItemType");
		availableItemTypeFormGroup.add(availableParamFormLabel);
		availableItemTypeFormGroup.add(availableItemToInsert);
		
		final FormGroup selectItemListFormGroup = new FormGroup();
		FormLabel selectItemListFormLabel = new FormLabel();
		selectItemListFormLabel.setText("Item Name");
		selectItemListFormLabel.setTitle("Select Item Name to insert");
		selectItemListFormLabel.setFor("listItemType");
		selectItemListFormGroup.add(selectItemListFormLabel);
		selectItemListFormGroup.add(listAllItemNames);
		
		final FormGroup dataTypeListFormGroup = new FormGroup();
		FormLabel dataTypeListFormLabel = new FormLabel();
		dataTypeListFormLabel.setText("Datatype");
		dataTypeListFormLabel.setTitle("Select Datatype to insert");
		dataTypeListFormLabel.setFor("listItemType");
		dataTypeListFormGroup.add(dataTypeListFormLabel);
		dataTypeListFormGroup.add(allQDMDatatypes);
		dataTypeListFormGroup.setVisible(false);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(availableItemTypeFormGroup);
		formFieldSet.add(selectItemListFormGroup);
		formFieldSet.add(dataTypeListFormGroup);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		addButton.setId("addButton_Insert");
		Button closeButton = new Button();
		closeButton.setText("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		closeButton.setId("closeButton_Cancel");
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		addChangeHandlerIntoLists(dialogModal, cqlNavBarView, availableItemToInsert, listAllItemNames,availableDatatypes,allQDMDatatypes,
				availableAttributesToInsert, messageFormgroup, helpBlock, 
				availableItemTypeFormGroup, selectItemListFormGroup,dataTypeListFormGroup);
		
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int selectedItemIndex = availableItemToInsert.getSelectedIndex();
				if (selectedItemIndex != 0) {
					String itemTypeName = availableItemToInsert.getItemText (selectedItemIndex);
					if (!itemTypeName.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)) {
						//For Attributes
						if(itemTypeName.equalsIgnoreCase("Attributes")) {
							
							int selectedIndex = availableAttributesToInsert.getSelectedIndex();
							if (selectedIndex != 0) {
								String attributeNameToBeInserted = availableAttributesToInsert.getValue(selectedIndex);
								
								if (attributeNameToBeInserted.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)) {
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ATTRIBUTE_NAME());
									messageFormgroup.setValidationState(ValidationState.ERROR);
								} else {
									int columnIndex = editor.getCursorPosition().getColumn();
									System.out.println(columnIndex);
									//convertToCamelCase(attributeNameToBeInserted);
									editor.insertAtCursor(convertToCamelCase(attributeNameToBeInserted));
									editor.focus();
									dialogModal.hide();
								}
								
								
							} else {
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ATTRIBUTE_NAME());
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
						} else {
							int selectedIndex = listAllItemNames.getSelectedIndex();
							if (selectedIndex != 0) {
								String itemNameToBeInserted = listAllItemNames.getValue(selectedIndex);
								if (itemNameToBeInserted.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)) {
									selectItemListFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ITEM_NAME());
									messageFormgroup.setValidationState(ValidationState.ERROR);
								} else {
									int columnIndex = editor.getCursorPosition().getColumn();
									System.out.println(columnIndex);
									if (itemTypeName.equalsIgnoreCase("Applied Value Sets/Codes")) {
										int selectedDatatypeIndex = allQDMDatatypes.getSelectedIndex();
										String dataType =null;
										if (selectedDatatypeIndex != 0) {
											if(!allQDMDatatypes.getValue(selectedDatatypeIndex).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
												dataType = allQDMDatatypes.getValue(selectedDatatypeIndex);
											}
										}

										StringBuilder sb = new StringBuilder();
										boolean isNotValidCode = false;
										if(dataType != null){
											String modifiedItemNameToBeInserted = modifyQuotesInString(itemNameToBeInserted);
											if(modifiedItemNameToBeInserted.equalsIgnoreCase(DEAD) 
													&& !dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_EXPIRED)){
												isNotValidCode = true;											
											} else if(modifiedItemNameToBeInserted.equalsIgnoreCase(BIRTH_DATE) 
													&& !dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_BIRTHDATE)){
												isNotValidCode = true;
											} else if(dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_BIRTHDATE)
												&& !modifiedItemNameToBeInserted.equalsIgnoreCase(BIRTH_DATE)){
												isNotValidCode = true;
											} else if(dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_EXPIRED)
												&& !modifiedItemNameToBeInserted.equalsIgnoreCase(DEAD)){
												isNotValidCode = true;
											}
											if(isNotValidCode){
												helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
												helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_CODE_DATA_TYPE());
												messageFormgroup.setValidationState(ValidationState.ERROR);
												itemNameToBeInserted = "";
											} else {
												sb = sb.append("[\"" + dataType + "\": ");
												sb= sb.append(itemNameToBeInserted).append("]");
												itemNameToBeInserted = sb.toString();
											}
										
										} else {
											sb= sb.append(itemNameToBeInserted);
											itemNameToBeInserted = sb.toString();	
										}
										
									} else if(itemTypeName.equalsIgnoreCase("definitions") || itemTypeName.equalsIgnoreCase("parameters") ||
											itemTypeName.equalsIgnoreCase("functions")) {
										StringBuilder sb = new StringBuilder(); 
										sb.append(itemNameToBeInserted);
										itemNameToBeInserted = sb.toString(); 
									} if(!itemNameToBeInserted.isEmpty()){
										editor.insertAtCursor(itemNameToBeInserted);
										editor.focus();
										dialogModal.hide();
									}
									
								}
							} else {
								if (itemTypeName.equalsIgnoreCase("Applied Value Sets/Codes")) {
									int selectedDatatypeIndex = allQDMDatatypes.getSelectedIndex();
									String dataType =null;
									if (selectedDatatypeIndex != 0) {
										if(!allQDMDatatypes.getValue(selectedDatatypeIndex).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
											dataType = allQDMDatatypes.getValue(selectedDatatypeIndex);
											StringBuilder sb = new StringBuilder();
											sb = sb.append("[\"" + dataType + "\"]");
											editor.insertAtCursor(sb.toString());
											editor.focus();
											dialogModal.hide();
										} 
										
									} else {
										selectItemListFormGroup.setValidationState(ValidationState.ERROR);
										dataTypeListFormGroup.setValidationState(ValidationState.ERROR);
										helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
										helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ITEM_NAME_OR_DATA_TYPE());
										messageFormgroup.setValidationState(ValidationState.ERROR);
									}
								} else{
									selectItemListFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ITEM_NAME());
									messageFormgroup.setValidationState(ValidationState.ERROR);
								}
							}
						}
					} else {
						availableItemTypeFormGroup.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ITEM_TYPE());
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
				} else {
					availableItemTypeFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ITEM_TYPE());
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
			}

		});
		dialogModal.show();
	}
	
	private  String modifyQuotesInString(String textToBeModified) {
		return textToBeModified.replaceAll("\"", "");
	}
	
	
	/**
	 * This method add's addChangeHandler event to 'availableItemToInsert' and 'listAllItemNames' ListBox.
	 *
	 * @param dialogModal the dialog modal
	 * @param searchDisplay -ViewDisplay.
	 * @param availableItemToInsert - ListBoxMVP.
	 * @param listAllItemNames - ListBoxMVP.
	 * @param availableDatatypes the available datatypes
	 * @param availableAttributesToInsert the available attributes to insert
	 * @param messageFormgroup - FormGroup.
	 * @param helpBlock - HelpBlock.
	 * @param availableItemTypeFormGroup - FormGroup.
	 * @param selectItemListFormGroup - FormGroup.
	 */
	private  void addChangeHandlerIntoLists(final Modal dialogModal, final CQLLeftNavBarPanelView cqlNavBarView,
			final ListBoxMVP availableItemToInsert, final ListBoxMVP listAllItemNames,final ListBoxMVP availableDatatypes,final ListBoxMVP availableQDMDatatypes,
			final ListBoxMVP availableAttributesToInsert, final FormGroup messageFormgroup,
			final HelpBlock helpBlock, final FormGroup availableItemTypeFormGroup, final FormGroup selectItemListFormGroup, final FormGroup dataTypeFormGroup) {
	
		availableItemToInsert.addChangeHandler(new ChangeHandler() {
			/* (non-Javadoc)
			 * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
			 */
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setVisible(false);
				int selectedIndex = availableItemToInsert.getSelectedIndex();
				if (selectedIndex != 0) {
					String itemTypeSelected = availableItemToInsert.getItemText(selectedIndex);
					if (itemTypeSelected.equalsIgnoreCase("parameters")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						List<CQLIdentifierObject> parameters = new ArrayList<>(); 
						parameters.addAll(MatContext.get().getParameters());
						parameters.addAll(MatContext.get().getIncludedParamNames());
						for (CQLIdentifierObject parameter : parameters) {
							listAllItemNames.addItem(parameter.toString().replaceAll("\"", ""), parameter.toString());
						}
						
					} else if (itemTypeSelected.equalsIgnoreCase("functions")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						List<CQLIdentifierObject> functions = new ArrayList<>(); 
						functions.addAll(MatContext.get().getFuncs());
						functions.addAll(MatContext.get().getIncludedFuncNames());
						for(CQLIdentifierObject function : functions) {
							listAllItemNames.addItem(function.toString().replace("\"", ""), function.toString() + "()");
						}
						
						
						
					} else if (itemTypeSelected.equalsIgnoreCase("definitions")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						ArrayList<CQLIdentifierObject> definitions = new ArrayList<>(); 
						definitions.addAll(MatContext.get().getDefinitions());
						definitions.addAll(MatContext.get().getIncludedDefNames());
						for(CQLIdentifierObject definition : definitions) {
							listAllItemNames.addItem(definition.toString().replaceAll("\"", ""), definition.toString());
						}
						
					} else if (itemTypeSelected.equalsIgnoreCase("Pre-Defined Functions")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < cqlFunctionsList.size(); i++) {
							listAllItemNames.addItem(cqlFunctionsList.get(i));
						}
					} else if (itemTypeSelected.equalsIgnoreCase("Applied Value Sets/Codes")) {
						dataTypeFormGroup.setVisible(true);
						listAllItemNames.clear();
						availableDatatypes.clear();
						
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						
						List<CQLIdentifierObject> terminologies = new ArrayList<>(); 
						terminologies.addAll(MatContext.get().getValuesets());
						terminologies.addAll(MatContext.get().getIncludedValueSetNames());
						terminologies.addAll(MatContext.get().getIncludedCodeNames());
						
						for(CQLIdentifierObject terminology : terminologies) {
							listAllItemNames.addItem(terminology.toString().replaceAll("\"", ""), terminology.toString());
						}
						
					} else if (itemTypeSelected.equalsIgnoreCase("Attributes")) {
						//open new popup/dialogBox
						dialogModal.clear();
						dialogModal.hide();
						InsertAttributeBuilderDialogBox AttrBuildBox = new InsertAttributeBuilderDialogBox();
						AttrBuildBox.showAttributesDialogBox(cqlNavBarView,curEditor);
						listAllItemNames.setEnabled(false);
						availableDatatypes.setEnabled(true);
						availableAttributesToInsert.setEnabled(true);
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						addAvailableItems(availableDatatypes, allDataTypes);
						addAvailableItems(availableAttributesToInsert, allAttributes);
						
					} else if (itemTypeSelected.equalsIgnoreCase("Timing")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for(int i = 0; i < allTimings.size(); i++) {
							listAllItemNames.addItem(allTimings.get(i));
						}
					}else {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(false);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
					}
				} else {
					listAllItemNames.clear();
					availableDatatypes.clear();
					availableAttributesToInsert.clear();
					listAllItemNames.setEnabled(false);
					availableDatatypes.setEnabled(false);
					availableAttributesToInsert.setEnabled(false);
				}
			}
		});
		
		
		listAllItemNames.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		availableQDMDatatypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				dataTypeFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				
			}
		});
		
		availableDatatypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = availableDatatypes.getSelectedIndex();
				if (selectedIndex != 0) {
					String dataTypeSelected = availableDatatypes.getItemText(selectedIndex);
					getAllAttibutesByDataType(availableAttributesToInsert, dataTypeSelected);
				} else {
					availableAttributesToInsert.clear();
					addAvailableItems(availableAttributesToInsert, allAttributes);
				}
			}
		});
		
		
		
       availableAttributesToInsert.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		
		
	}
private  void getAllAttibutesByDataType(final ListBoxMVP availableAttributesToInsert, String dataType){
		
		attributeService.getAllAttributesByDataType(dataType, new AsyncCallback<List<QDSAttributes>>() {
			
			@Override
			public void onSuccess(List<QDSAttributes> result) {
				List<String> filterAttrByDataTypeList = new ArrayList<String>();
				for (QDSAttributes qdsAttributes : result) {
					filterAttrByDataTypeList.add(qdsAttributes.getName());
				}
				Collections.sort(filterAttrByDataTypeList);
				availableAttributesToInsert.clear();
				addAvailableItems(availableAttributesToInsert, filterAttrByDataTypeList);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	/**
	 * Method to Reterive Instance of Ace Editor based on current Section in CQL WorkSpace.
	 * @param searchDisplay - ViewDisplay.
	 * @param currentSection - String.
	 * @return AceEditor.
	 *//*
	private  AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
		AceEditor editor = null;
		switch(currentSection) {
			case CQLWorkSpaceConstants.CQL_DEFINE_MENU:
				editor = searchDisplay.getCQlDefinitionsView().getDefineAceEditor();
				break;
			case CQLWorkSpaceConstants.CQL_FUNCTION_MENU:
				editor = searchDisplay.getCqlFunctionsView().getFunctionBodyAceEditor();
				break;
			default:
				editor = searchDisplay.getCQlDefinitionsView().getDefineAceEditor();
				break;
		}
		return editor;
	}*/
	
	/**
	 * This method populates first drop down of the pop up.
	 *
	 * @param availableItemToInsert - ListBoxMVP
	 * @param availableInsertItemList the available insert item list
	 */
	private  void addAvailableItems(ListBoxMVP availableItemToInsert, 
			List<String> availableInsertItemList) {
		availableItemToInsert.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < availableInsertItemList.size(); i++) {
			if(!availableInsertItemList.get(i).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
				availableItemToInsert.addItem(availableInsertItemList.get(i));
			}
		}
		
	}
	
	
	
	/**
	 * Gets the function argument value builder.
	 *
	 * @param functions the functions
	 * @return the function argument value builder
	 */
	private  String getFunctionArgumentValueBuilder(CQLFunctions functions){
		StringBuilder functionNameBuilder = new StringBuilder(functions.getFunctionName());
		functionNameBuilder.append("(");
		StringBuilder argumentType = new StringBuilder();
		if (functions.getArgumentList() != null) {
			for (int j = 0; j < functions.getArgumentList().size(); j++) {
				CQLFunctionArgument argument = functions.getArgumentList().get(j);
				argumentType = argumentType.append(argument.getArgumentName());
				if (j <  (functions.getArgumentList().size() - 1)) {
					argumentType.append(",");
				}
			}
		}
		functionNameBuilder.append(argumentType + ")");
		return functionNameBuilder.toString();
	}
	
	/**
	 * Gets the function argument tool tip builder.
	 *
	 * @param functions the functions
	 * @return the function argument tool tip builder
	 */
	private  String getFunctionArgumentToolTipBuilder(CQLFunctions functions){
		StringBuilder functionNameBuilder = new StringBuilder(functions.getFunctionName());
		functionNameBuilder.append("(");
		StringBuilder argumentType = new StringBuilder();
		if (functions.getArgumentList() != null) {
			for (int j = 0; j < functions.getArgumentList().size(); j++) {
				CQLFunctionArgument argument = functions.getArgumentList().get(j);
				argumentType = argumentType.append(argument.getArgumentName());
				if(argument.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)){
					argumentType = argumentType.append(" " +'"' + argument.getQdmDataType() + '"');
				}
				else if(argument.getArgumentType().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)){
					argumentType = argumentType.append(" " + argument.getOtherType());
				}
				else{
					argumentType = argumentType.append(" " +argument.getArgumentType());
				}
				if (j <  (functions.getArgumentList().size() - 1)) {
					argumentType.append(",");
				}
			}
		}
		functionNameBuilder.append(argumentType + ")");
		return functionNameBuilder.toString();
	}
	
	/**
	 * Convert to camel case.
	 *
	 * @param str the str
	 * @return the string
	 */
	private  String convertToCamelCase(String str){
        String result = "";
        char firstChar = str.charAt(0);
        result = result + Character.toLowerCase(firstChar);
        for (int i = 1; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            char previousChar = str.charAt(i - 1);
            if (previousChar == ' ') {
                result = result + Character.toUpperCase(currentChar);
            } else {
                result = result + currentChar;
            }
        }
        return result.replaceAll(" ", "");
	}
	
	
	
	
	
}
