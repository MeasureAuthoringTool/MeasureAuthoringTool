package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Collections;
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

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.shared.CQLIdentifierObject;
import mat.shared.ConstantMessages;

public class InsertIntoAceEditorDialogBox {
	
	private static final String INVALID_ITEM_AND_DATATYPE_COMBINATOIN_ERROR = "Invalid Item Name and Datatype Combination.";
	
	private static final String ITEM_NAME_DATA_TYPE_ALERT = "Item Name dropdown is enabled. Data Type dropdown now available.";

	private static final String ITEM_NAME_ALERT = "Item Name dropdown is now enabled.";

	private static List<String> availableInsertItemList = CQLWorkSpaceConstants.getAvailableItem();

	private static List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();

	private static List<String> allAttributes = MatContext.get().getCqlConstantContainer().getCqlAttributeList();
	
	private static List<String> allTimings = MatContext.get().getCqlConstantContainer().getCqlTimingList();
	
	private static FormGroup messageHelpFormGroup = new FormGroup(); 
	
	private static HelpBlock messageHelpBlock = new HelpBlock(); 

	/** The attribute service. */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);

	private static ArrayList<CQLCode> cqlCodesList = new ArrayList<>();
	
	private static List<String> cqlFunctionsList = MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlFunctionsList();
	
	private static AceEditor curEditor; 

	/**
	 * Public static method to build Pop up for Insert into Ace Editor.
	 * @param searchDisplay - ViewDisplay.
	 * @param currentSection - String.
	 */
	public static  void showListOfItemAvailableForInsertDialogBox(final CQLLeftNavBarPanelView cqlNavBarView, final AceEditor editor) {
		final Modal dialogModal = new Modal();
		dialogModal.getElement().setAttribute("role", "dialog");
		dialogModal.setTitle("Insert Item into CQL Editor");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setSize(ModalSize.MEDIUM);
		dialogModal.setRemoveOnHide(true);
		
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
		
		messageHelpBlock.setHeight("0px");
		messageHelpBlock = new HelpBlock(); 
		messageHelpFormGroup = new FormGroup(); 
		messageHelpFormGroup.setHeight("0px");
		messageHelpFormGroup.add(messageHelpBlock);
		messageHelpFormGroup.getElement().setAttribute("role", "alert");
		
		
		// CQL Data Type Drop down Form group
		final FormGroup availableItemTypeFormGroup = new FormGroup();
		FormLabel availableParamFormLabel = new FormLabel();
		availableParamFormLabel.setText("Item Type");
		availableParamFormLabel.setTitle("Select Item type to insert");
		availableParamFormLabel.setFor("availableItemToInsert_ListBox");
		availableItemTypeFormGroup.add(availableParamFormLabel);
		availableItemTypeFormGroup.add(availableItemToInsert);
		
		final FormGroup selectItemListFormGroup = new FormGroup();
		FormLabel selectItemListFormLabel = new FormLabel();
		selectItemListFormLabel.setText("Item Name");
		selectItemListFormLabel.setTitle("Select Item Name to insert");
		selectItemListFormLabel.setFor("listAllItemNames_ListBox");
		selectItemListFormGroup.add(selectItemListFormLabel);
		selectItemListFormGroup.add(listAllItemNames);
		
		final FormGroup dataTypeListFormGroup = new FormGroup();
		FormLabel dataTypeListFormLabel = new FormLabel();
		dataTypeListFormLabel.setText("Datatype");
		dataTypeListFormLabel.setTitle("Select Datatype to insert");
		dataTypeListFormLabel.setFor("allQDMDatatypes_ListBox");
		dataTypeListFormGroup.add(dataTypeListFormLabel);
		dataTypeListFormGroup.add(allQDMDatatypes);
		dataTypeListFormGroup.setVisible(false);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageHelpFormGroup);
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
					if (!itemTypeName.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
						//For Attributes
						if(itemTypeName.equalsIgnoreCase("Attributes")) {
							
							int selectedIndex = availableAttributesToInsert.getSelectedIndex();
							if (selectedIndex != 0) {
								String attributeNameToBeInserted = availableAttributesToInsert.getValue(selectedIndex);
								
								if (attributeNameToBeInserted.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
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
								if (itemNameToBeInserted.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
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
											if(!allQDMDatatypes.getValue(selectedDatatypeIndex).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
												dataType = allQDMDatatypes.getValue(selectedDatatypeIndex);
											}
										}

										CQLCode code = cqlCodesList.get(selectedIndex);
										if(isValidPair(dataType, code)) {
											StringBuilder sb = new StringBuilder();
											if(dataType != null){
												sb = sb.append("[\"" + dataType + "\": ");
												sb = sb.append(itemNameToBeInserted).append("]");
												itemNameToBeInserted = sb.toString();
	
	
											} else {
												sb= sb.append(itemNameToBeInserted);
												itemNameToBeInserted = sb.toString();	
											}
											
											if(!itemNameToBeInserted.isEmpty()){
												editor.insertAtCursor(itemNameToBeInserted);
												editor.focus();
												dialogModal.hide();
											}
										} else {
											selectItemListFormGroup.setValidationState(ValidationState.ERROR);
											dataTypeListFormGroup.setValidationState(ValidationState.ERROR);
											helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
											helpBlock.setText(InsertIntoAceEditorDialogBox.INVALID_ITEM_AND_DATATYPE_COMBINATOIN_ERROR);
											messageFormgroup.setValidationState(ValidationState.ERROR);
										}
										
									} else if(itemTypeName.equalsIgnoreCase("definitions") || itemTypeName.equalsIgnoreCase("parameters") ||
											itemTypeName.equalsIgnoreCase("functions")) {
										StringBuilder sb = new StringBuilder(); 
										sb.append(itemNameToBeInserted);
										itemNameToBeInserted = sb.toString(); 
										
										if(!itemNameToBeInserted.isEmpty()){
											editor.insertAtCursor(itemNameToBeInserted);
											editor.focus();
											dialogModal.hide();
										}
									} 
									
								}
							} else {
								if (itemTypeName.equalsIgnoreCase("Applied Value Sets/Codes")) {
									int selectedDatatypeIndex = allQDMDatatypes.getSelectedIndex();
									String dataType =null;
									if (selectedDatatypeIndex != 0) {
										if(!allQDMDatatypes.getValue(selectedDatatypeIndex).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
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
	private static void addChangeHandlerIntoLists(final Modal dialogModal, final CQLLeftNavBarPanelView cqlNavBarView,
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
						alertUserItemNameFieldEnabled(listAllItemNames);
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
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
						alertUserItemNameFieldEnabled(listAllItemNames);
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
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
						alertUserItemNameFieldEnabled(listAllItemNames);
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
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
						alertUserItemNameFieldEnabled(listAllItemNames);
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
						for (int i = 0; i < cqlFunctionsList.size(); i++) {
							listAllItemNames.addItem(cqlFunctionsList.get(i));
						}
					} else if (itemTypeSelected.equalsIgnoreCase("Applied Value Sets/Codes")) {
						messageHelpBlock.setColor("transparent");
						messageHelpBlock.setText(ITEM_NAME_DATA_TYPE_ALERT);
						dataTypeFormGroup.setVisible(true);
						listAllItemNames.clear();
						availableDatatypes.clear();
						
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
						
						List<CQLIdentifierObject> terminologies = new ArrayList<>(); 
						terminologies.addAll(MatContext.get().getValuesets());
						terminologies.addAll(MatContext.get().getIncludedValueSetNames());
						terminologies.addAll(MatContext.get().getIncludedCodeNames());
						
						//Add null as first value in cqlCodes list so that 'selectedIndex' variable for ListBoxMVP
						//lines up with array list index
						cqlCodesList.add(null);
						
						
						CQLModel cqlModel = MatContext.get().getCQLModel();
						for(CQLIdentifierObject terminology : terminologies) {
							String displayName = terminology.getDisplay();
							if(displayName.length() > 65) {
								String firstPart = displayName.substring(0, 55);
								String secondPart = displayName.substring(displayName.length() - 7); 
								displayName = firstPart + "..." + secondPart;
							}
							
							listAllItemNames.addItem(displayName, terminology.toString());
							
							String alias = terminology.getAliasName();
							String libraryName = null;
							String version = null;
							String codeName = terminology.getDisplay();
							CQLCode code = null;
							
							if(alias==null) {
								code = cqlModel.getCodeByName(codeName);
								cqlCodesList.add(code);
							} else {
								for(CQLIncludeLibrary lib : cqlModel.getCqlIncludeLibrarys()) {
									
									if(lib.getAliasName().equalsIgnoreCase(alias)){
										version = lib.getVersion();
										libraryName = lib.getCqlLibraryName();
										
										//changes codeName from 'Library.Name' to 'Name'
										int indexOfFirstPeriod = codeName.indexOf('.');
										codeName = codeName.substring(indexOfFirstPeriod+1);
										
										code = cqlModel.getCodeByName(libraryName + "-" + version +"|" + alias + "|" + codeName);
										cqlCodesList.add(code);
									}
								}
							}
						}
						
					} else if (itemTypeSelected.equalsIgnoreCase("Attributes")) {
						//open new popup/dialogBox
						dialogModal.clear();
						dialogModal.hide();
						InsertAttributeBuilderDialogBox.showAttributesDialogBox(cqlNavBarView,curEditor);
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
						alertUserItemNameFieldEnabled(listAllItemNames);
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.PLEASE_SELECT);
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
	
	private static void alertUserItemNameFieldEnabled(final ListBoxMVP listAllItemNames) {
		messageHelpBlock.setColor("transparent");
		
		if(!listAllItemNames.isEnabled()) {
			messageHelpBlock.setText(ITEM_NAME_ALERT);
		}
		
	}
private static void getAllAttibutesByDataType(final ListBoxMVP availableAttributesToInsert, String dataType){
		
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
		
			}
		});
	}
	
	
	/**
	 * This method populates first drop down of the pop up.
	 *
	 * @param availableItemToInsert - ListBoxMVP
	 * @param availableInsertItemList the available insert item list
	 */
	private static void addAvailableItems(ListBoxMVP availableItemToInsert, 
			List<String> availableInsertItemList) {
		availableItemToInsert.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < availableInsertItemList.size(); i++) {
			if(!availableInsertItemList.get(i).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				availableItemToInsert.addItem(availableInsertItemList.get(i));
			}
		}
		
	}

	private static String convertToCamelCase(String str){
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
	
	//returns true if given code is the birthday code; false otherwise
	private static boolean isBirthdate(CQLCode code) {
		if(code.getCodeOID().equals(ConstantMessages.BIRTHDATE_OID) && code.getCodeSystemOID().equalsIgnoreCase(ConstantMessages.BIRTHDATE_CODE_SYSTEM_OID)) {
			return true;
		}
		
		return false;
	}
	
	//returns true if given code is the dead code; false otherwise
	private static boolean isDead(CQLCode code) {
		if(code.getCodeOID().equals(ConstantMessages.DEAD_OID) && code.getCodeSystemOID().equalsIgnoreCase(ConstantMessages.DEAD_CODE_SYSTEM_OID)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param dataType	String
	 * @param code	CQLCode
	 * @return
	 * 	true if 'Patient Characteristic Birthdate' is paired with birthdate code
	 *  true if 'Patient Characteristic Expired' is paired with dead code
	 *  false if datatype = PATIENT_CHARACTERISTIC_BIRTHDATE or datatype = PATIENT_CHARACTERISTIC_EXPIRED,
	 *  	or if code = dead or code = birthdate and the combination is not PATIENT_CHARACTERISTIC_EXPIRED 
	 *  	and dead, or PATIENT_CHARACTERISTIC_BIRTHDATE and birthdate
	 *  true otherwise
	 */
	private static boolean isValidPair(String dataType, CQLCode code) {
		
		//valid pair
		if(dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE) && isBirthdate(code)) {
			return true;
		
		//valid pair
		} else if(dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED) && isDead(code)) {
			return true;
		
		//by elimination of above, must be invalid pair
		}else if(dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED) ||dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE) ||
				isDead(code) || isBirthdate(code)) {
			return false;
		
		//valid pair consisting of none of the datatypes or codes we are considering in the scope of this method
		} else {
			return true;
		}
	}
}
