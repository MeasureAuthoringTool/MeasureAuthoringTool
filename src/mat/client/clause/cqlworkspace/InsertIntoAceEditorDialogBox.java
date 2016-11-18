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
import org.gwtbootstrap3.client.ui.TextBox;
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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;

// TODO: Auto-generated Javadoc
/**
 * The Class InsertIntoAceEditorDialogBox.
 */
public class InsertIntoAceEditorDialogBox {
	/**
	 * List of availableInsertItemList.
	 */
	private static List<String> availableInsertItemList = CQLWorkSpaceConstants.getAvailableItem();
	
	/** The all data types. */
	private static List<String> allDataTypes = MatContext.get().getDataTypeList();
	
	/** The all attributes. */
	private static List<String> allAttributes = MatContext.get().getAllAttributeList();
	
	/** The attribute service. */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	
	/** The Constant NEGATION_RATIONALE. */
	private static final String NEGATION_RATIONALE = "negation rationale";
	
	
	/**
	 * List of timingList.
	 */
	//private static List<String> timingList = MatContext.get().getCqlGrammarDataType().getCqlTimingList();
	/**
	 * List of cqlFunctionsList.
	 */
	private static List<String> cqlFunctionsList = MatContext.get().getCqlGrammarDataType().getCqlFunctionsList();
	
	/** The Constant INSERT_AT_END. */
	private static final int INSERT_AT_END = -1;
	
	/** The Constant attrModal. */
	private static Modal attrModal;
	
	/**
	 * Public static method to build Pop up for Insert into Ace Editor.
	 * @param searchDisplay - ViewDisplay.
	 * @param currentSection - String.
	 */
	public static  void showListOfItemAvailableForInsertDialogBox(final ViewDisplay searchDisplay, String currentSection) {
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Insert Item into CQL Editor");
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
		
		final ListBoxMVP availableAttributesToInsert = new ListBoxMVP();
		availableAttributesToInsert.clear();
		availableAttributesToInsert.setWidth("350px");
		availableAttributesToInsert.setEnabled(false);
		availableAttributesToInsert.getElement().setId("availableAttributesToInsert_ListBox");
		
		
		// Based on Current Section this method will reterive instance of Ace Editor.
		final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
		
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
		
		//MAT 8222 Removing Attributes by Datatype and Attributes fields from Insert Icon and later added to new pop up screen.
		/*final FormGroup availableDataTypesFormGroup = new FormGroup();
		FormLabel availableDataTypesFormLabel = new FormLabel();
		availableDataTypesFormLabel.setText("Attributes by Datatype");
		availableDataTypesFormLabel.setTitle("Select DataType to Filter");
		availableDataTypesFormLabel.setFor("availableDatatype");
		availableDataTypesFormGroup.add(availableDataTypesFormLabel);
		availableDataTypesFormGroup.add(availableDatatypes);
		
		final FormGroup availableAttributesFormGroup = new FormGroup();
		FormLabel availableAttributesFormLabel = new FormLabel();
		availableAttributesFormLabel.setText("Attributes");
		availableAttributesFormLabel.setTitle("Select Attribute to Insert");
		availableAttributesFormLabel.setFor("availableAttributeToInsert");
		availableAttributesFormGroup.add(availableAttributesFormLabel);
		availableAttributesFormGroup.add(availableAttributesToInsert);*/
		
		
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(availableItemTypeFormGroup);
		formFieldSet.add(selectItemListFormGroup);
		//MAT 8222 Removing Attributes by Datatype and Attributes fields from Insert Icon and later added to new pop up screen.
		/*formFieldSet.add(availableDataTypesFormGroup);
		formFieldSet.add(availableAttributesFormGroup);*/
		
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
		addChangeHandlerIntoLists(dialogModal, searchDisplay, availableItemToInsert, listAllItemNames, availableDatatypes,
				availableAttributesToInsert, messageFormgroup, helpBlock, 
				availableItemTypeFormGroup, selectItemListFormGroup);
		
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
									//availableAttributesFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("Please select Attribute Name.");
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
								//availableAttributesFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText("Please select Attribute Name.");
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
						} else {
							int selectedIndex = listAllItemNames.getSelectedIndex();
							if (selectedIndex != 0) {
								String itemNameToBeInserted = listAllItemNames.getValue(selectedIndex);
								if (itemNameToBeInserted.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)) {
									selectItemListFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("Please select Item Name.");
									messageFormgroup.setValidationState(ValidationState.ERROR);
								} else {
									int columnIndex = editor.getCursorPosition().getColumn();
									System.out.println(columnIndex);
									if (itemTypeName.equalsIgnoreCase("Applied QDM")) {
										String[] str = itemNameToBeInserted.split("\\.");
										StringBuilder sb = new StringBuilder();
										sb = sb.append("[\"" + str[1] + "\"");
										sb = sb.append(": \"").append(str[0] + "\"]");
										itemNameToBeInserted = sb.toString();
									} else if(itemTypeName.equalsIgnoreCase("definitions") || itemTypeName.equalsIgnoreCase("parameters")) {
										StringBuilder sb = new StringBuilder(); 
										sb = sb.append("\""); 
										sb = sb.append(itemNameToBeInserted);
										sb = sb.append("\""); 
										itemNameToBeInserted = sb.toString(); 
									} else if(itemTypeName.equalsIgnoreCase("functions")) {
										StringBuilder sb = new StringBuilder(); 
										sb = sb.append("\""); 
										itemNameToBeInserted = itemNameToBeInserted.replace("()", "");
										sb = sb.append(itemNameToBeInserted); 
										sb = sb.append("\"()");
										itemNameToBeInserted = sb.toString(); 
									}
									editor.insertAtCursor(itemNameToBeInserted);
									editor.focus();
									dialogModal.hide();
								}
							} else {
								selectItemListFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText("Please select Item Name.");
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
						}
					} else {
						availableItemTypeFormGroup.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please select Item Type.");
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
				} else {
					availableItemTypeFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please select Item Type.");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
			}
		});
		dialogModal.show();
	}
	
	private static void showAttributesDialogBox() {
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Insert Options for Attributes");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertAttrToAceEditor_Modal");
		dialogModal.setWidth("50%");
		ModalBody modalBody = new ModalBody();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");

		final ListBoxMVP DtAttriblistBox = new ListBoxMVP();
		DtAttriblistBox.setWidth("18em");
		DtAttriblistBox.setVisibleItemCount(10);
		DtAttriblistBox.getElement().setId("DataTypeBtAtrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		DtAttriblistBox.setVisibleItemCount(1);

		final ListBoxMVP AttriblistBox = new ListBoxMVP();
		AttriblistBox.setWidth("18em");
		AttriblistBox.setVisibleItemCount(10);
		AttriblistBox.getElement().setId("Atrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		AttriblistBox.setVisibleItemCount(1);

		final ListBoxMVP ModelistBox = new ListBoxMVP();
		ModelistBox.setWidth("18em");
		ModelistBox.setVisibleItemCount(10);
		ModelistBox.getElement().setId("Mode_listBox");
		ModelistBox.addItem("Select");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		ModelistBox.setVisibleItemCount(1);

		final ListBoxMVP ModeDetailslistBox = new ListBoxMVP();
		ModeDetailslistBox.setWidth("18em");
		ModeDetailslistBox.setVisibleItemCount(10);
		ModeDetailslistBox.getElement().setId("ModeDetails_listBox");
		ModeDetailslistBox.addItem("Select");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		ModeDetailslistBox.setVisibleItemCount(1);

		// Define date format
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL);
		final DateBox dateBox = new DateBox();
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

		
		final TextBox QuantityTextBox = new TextBox();
		QuantityTextBox.setWidth("18em");
		QuantityTextBox.getElement().setId("Qantity_TextBox");

		final ListBoxMVP UnitslistBox = new ListBoxMVP();
		UnitslistBox.setWidth("18em");
		UnitslistBox.setVisibleItemCount(10);
		UnitslistBox.getElement().setId("Units_listBox");
		UnitslistBox.addItem("Select");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		UnitslistBox.setVisibleItemCount(1);

		//All the Labels
		FormLabel AttrDataTypeLabel = new FormLabel();
		AttrDataTypeLabel.setText("Attributes By DataType");
		AttrDataTypeLabel.setTitle("Select Attributes By DataType");
		AttrDataTypeLabel.setStyleName("attr-Label");
		FormLabel AttributeLabel = new FormLabel();
		AttributeLabel.setText("Attributes");
		AttributeLabel.setTitle("Select Attributes");
		AttributeLabel.setStyleName("attr-Label");
		FormLabel ModeLabel = new FormLabel();
		ModeLabel.setText("Mode");
		ModeLabel.setTitle("Select Mode");
		ModeLabel.setStyleName("attr-Label");
		FormLabel ModeDetailsLabel = new FormLabel();
		ModeDetailsLabel.setText("Mode Details");
		ModeDetailsLabel.setTitle("Select Mode Details");
		ModeDetailsLabel.setStyleName("attr-Label");
		FormLabel DateTimeLabel = new FormLabel();
		DateTimeLabel.setText("DateTime");
		DateTimeLabel.setTitle("Select DateTime");
		DateTimeLabel.setStyleName("attr-Label");
		FormLabel QuantityLabel = new FormLabel();
		QuantityLabel.setText("Quantity");
		QuantityLabel.setTitle("Select Quantity");
		QuantityLabel.setStyleName("attr-Label");
		FormLabel UnitsLabel = new FormLabel();
		UnitsLabel.setText("Units");
		UnitsLabel.setTitle("Select Units");
		UnitsLabel.setStyleName("attr-Label");

		Grid queryGrid = new Grid(8, 2);
		queryGrid.setWidget(0, 0, AttrDataTypeLabel);
		queryGrid.setWidget(1, 0, DtAttriblistBox);
		queryGrid.setWidget(0, 1, AttributeLabel);
		queryGrid.setWidget(1, 1, AttriblistBox);
		queryGrid.setWidget(2, 0, ModeLabel);
		queryGrid.setWidget(3, 0, ModelistBox);
		queryGrid.setWidget(2, 1, ModeDetailsLabel);
		queryGrid.setWidget(3, 1, ModeDetailslistBox);
		queryGrid.setWidget(4, 0, DateTimeLabel);
		queryGrid.setWidget(5, 0, dateBox);
		queryGrid.setWidget(6, 0, QuantityLabel);
		queryGrid.setWidget(7, 0, QuantityTextBox);
		queryGrid.setWidget(6, 1, UnitsLabel);
		queryGrid.setWidget(7, 1, UnitslistBox);
		
		queryGrid.setStyleName("attr-grid");
		
		modalBody.add(queryGrid);
		modalBody.add(messageFormgroup);

		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		addButton.setId("addButton_Button");
		Button closeButton = new Button();
		closeButton.setText("Cancel");
		closeButton.setTitle("Cancel");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		closeButton.setId("Cancel_button");
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		
		addAvailableItems(DtAttriblistBox, allDataTypes);
		addAvailableItems(AttriblistBox, allAttributes);
		
		DtAttriblistBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = DtAttriblistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String dataTypeSelected = DtAttriblistBox.getItemText(selectedIndex);
					getAllAttibutesByDataType(AttriblistBox, dataTypeSelected);
				} else {
					AttriblistBox.clear();
					addAvailableItems(AttriblistBox, allAttributes);
				}
			}
		});
		
		
		AttriblistBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//TODO:This is not a working code but a sample one which needs to be replaced once we implement insert button functionality.
				int selectedIndex = DtAttriblistBox.getSelectedIndex();
				if(selectedIndex !=-1){
					String selectedItem = DtAttriblistBox.getItemText(selectedIndex);
					helpBlock.setText("");
					messageFormgroup.setValidationState(ValidationState.NONE);
				} else {
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please Select Item name to insert into Editor");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				dialogModal.hide();
			}

		});
		dialogModal.show();}

	/**
	 * This method add's addChangeHandler event to 'availableItemToInsert' and 'listAllItemNames' ListBox.
	 * @param dialogModal 
	 *
	 * @param searchDisplay -ViewDisplay.
	 * @param availableItemToInsert - ListBoxMVP.
	 * @param listAllItemNames - ListBoxMVP.
	 * @param availableDatatypes the available datatypes
	 * @param availableAttributesToInsert the available attributes to insert
	 * @param messageFormgroup - FormGroup.
	 * @param helpBlock - HelpBlock.
	 * @param availableItemTypeFormGroup - FormGroup.
	 * @param selectItemListFormGroup - FormGroup.
	 * @param availableDataTypesFormGroup the available data types form group
	 * @param availableAttributesFormGroup the available attributes form group
	 */
	private static void addChangeHandlerIntoLists(final Modal dialogModal, final ViewDisplay searchDisplay,
			final ListBoxMVP availableItemToInsert, final ListBoxMVP listAllItemNames, final ListBoxMVP availableDatatypes,
			final ListBoxMVP availableAttributesToInsert, final FormGroup messageFormgroup,
			final HelpBlock helpBlock, final FormGroup availableItemTypeFormGroup, final FormGroup selectItemListFormGroup) {
		availableItemToInsert.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				availableItemTypeFormGroup.setValidationState(ValidationState.NONE);
				selectItemListFormGroup.setValidationState(ValidationState.NONE);
				/*availableDataTypesFormGroup.setValidationState(ValidationState.NONE);
				availableAttributesFormGroup.setValidationState(ValidationState.NONE);*/
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
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
						for (int i = 0; i < searchDisplay.getViewParameterList().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getViewParameterList().get(i)
									.getParameterName());
						}
					} else if (itemTypeSelected.equalsIgnoreCase("functions")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getViewFunctions().size(); i++) {
							CQLFunctions functions = searchDisplay.getViewFunctions().get(i);
							String funcArg = getFunctionArgumentValueBuilder(functions);
							String funcArgToolTip = getFunctionArgumentToolTipBuilder(functions);
							listAllItemNames.insertItem(funcArg, funcArg, funcArgToolTip, INSERT_AT_END);
						}
					} else if (itemTypeSelected.equalsIgnoreCase("definitions")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getViewDefinitions().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getViewDefinitions().get(i)
									.getDefinitionName());
						}
					} /*else if (itemTypeSelected.equalsIgnoreCase("timing")) {
						listAllItemNames.clear();
						listAllItemNames.setEnabled(true);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < timingList.size(); i++) {
							listAllItemNames.addItem(timingList.get(i));
						}
					}*/ else if (itemTypeSelected.equalsIgnoreCase("Pre-Defined Functions")) {
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
					} else if (itemTypeSelected.equalsIgnoreCase("Applied QDM")) {
						listAllItemNames.clear();
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getAppliedQdmList().size(); i++) {
							if(!searchDisplay.getAppliedQdmList().get(i)
									.getDataType().equalsIgnoreCase("attribute")){
								listAllItemNames.addItem(searchDisplay.getAppliedQdmList().get(i).getCodeListName()
										+ "." + searchDisplay.getAppliedQdmList().get(i).getDataType());
							}
						}
					} else if (itemTypeSelected.equalsIgnoreCase("Attributes")) {
						//open new popup/dialogBox
						dialogModal.clear();
						dialogModal.hide();
						showAttributesDialogBox();
						listAllItemNames.setEnabled(false);
						availableDatatypes.setEnabled(true);
						availableAttributesToInsert.setEnabled(true);
						availableDatatypes.clear();
						availableAttributesToInsert.clear();
						addAvailableItems(availableDatatypes, allDataTypes);
						addAvailableItems(availableAttributesToInsert, allAttributes);
						
					} else {
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
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		availableDatatypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				//availableDataTypesFormGroup.setValidationState(ValidationState.NONE);
				//availableAttributesFormGroup.setValidationState(ValidationState.NONE);
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
				
				//availableAttributesFormGroup.setValidationState(ValidationState.NONE);
				//availableAttributesFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		
		
	}
	
	/**
	 * Gets the all attibutes by data type.
	 *
	 * @param availableAttributesToInsert the available attributes to insert
	 * @param dataType the data type
	 * @return the all attibutes by data type
	 */
	private static void getAllAttibutesByDataType(final ListBoxMVP availableAttributesToInsert, String dataType){
		
		attributeService.getAllAttributesByDataType(dataType, new AsyncCallback<List<QDSAttributes>>() {
			
			@Override
			public void onSuccess(List<QDSAttributes> result) {
				List<String> filterAttrByDataTypeList = new ArrayList<String>();
				for (QDSAttributes qdsAttributes : result) {
					filterAttrByDataTypeList.add(qdsAttributes.getName());
				}
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
	 */
	private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
		AceEditor editor = null;
		switch(currentSection) {
			case CQLWorkSpaceConstants.CQL_DEFINE_MENU:
				editor = searchDisplay.getDefineAceEditor();
				break;
			case CQLWorkSpaceConstants.CQL_FUNCTION_MENU:
				editor = searchDisplay.getFunctionBodyAceEditor();
				break;
			default:
				editor = searchDisplay.getDefineAceEditor();
				break;
		}
		return editor;
	}
	
	/**
	 * This method populates first drop down of the pop up.
	 *
	 * @param availableItemToInsert - ListBoxMVP
	 * @param availableInsertItemList the available insert item list
	 */
	private static void addAvailableItems(ListBoxMVP availableItemToInsert, 
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
	private static String getFunctionArgumentValueBuilder(CQLFunctions functions){
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
	private static String getFunctionArgumentToolTipBuilder(CQLFunctions functions){
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
	
}
