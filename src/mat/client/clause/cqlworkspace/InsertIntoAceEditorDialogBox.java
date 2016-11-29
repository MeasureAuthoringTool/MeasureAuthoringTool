package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
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
import org.gwtbootstrap3.client.ui.TextBox;
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
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
import mat.client.shared.CustomDateTimeTextBox;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;

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
	
	private static List<String> allUnits = MatContext.get().getAllUnitsList(); 
	
	/** The attribute service. */
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	
	/** The Constant NEGATION_RATIONALE. */
	private static final String NEGATION_RATIONALE = "negation rationale";
	
	/** The currention section. */
	private static String currention_Section; 
	
	
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
	
	
	/** The Constant yyyyTxtBox. */
	final static CustomDateTimeTextBox yyyyTxtBox = new CustomDateTimeTextBox(4);
	
	/** The Constant mmTxtBox. */
	final static CustomDateTimeTextBox mmTxtBox = new CustomDateTimeTextBox(2);
	
	/** The Constant ddTxtBox. */
	final static CustomDateTimeTextBox ddTxtBox = new CustomDateTimeTextBox(2);
	
	/** The Constant hhTextBox. */
	final static CustomDateTimeTextBox hhTextBox = new CustomDateTimeTextBox(2);
	
	/** The Constant minTxtBox. */
	final static CustomDateTimeTextBox minTxtBox = new CustomDateTimeTextBox(2);
	
	/** The Constant ssTxtBox. */
	final static CustomDateTimeTextBox ssTxtBox = new CustomDateTimeTextBox(2);
	
	/** The Constant msTxtBox. */
	final static CustomDateTimeTextBox msTxtBox = new CustomDateTimeTextBox(3);
	
	final static FormGroup yearFormGroup = new FormGroup();
	final static FormGroup mmFormGroup = new FormGroup();
	final static FormGroup ddFormGroup = new FormGroup();
	final static FormGroup hourFormGroup = new FormGroup();
	final static FormGroup minFormGroup = new FormGroup();
	final static FormGroup secondsFormGroup = new FormGroup();
	final static FormGroup millisecFormGroup = new FormGroup();
	
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
		currention_Section = currentSection;
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
	
	/**
	 * Show attributes dialog box.
	 *
	 * @param searchDisplay the search display
	 * @param currentSection the current section
	 */
	private static void showAttributesDialogBox(final ViewDisplay searchDisplay, String currentSection) {
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

		
		final CustomQuantityTextBox QuantityTextBox = new CustomQuantityTextBox(20);
		QuantityTextBox.setWidth("18em");
		QuantityTextBox.getElement().setId("Qantity_TextBox");
		
		final ListBoxMVP UnitslistBox = new ListBoxMVP();
		UnitslistBox.setWidth("18em");
		UnitslistBox.setVisibleItemCount(10);
		UnitslistBox.getElement().setId("Units_listBox");
		for(String unit : allUnits) {
			UnitslistBox.addItem(unit);
		}
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
		DateTimeLabel.setText("Date/Time");
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

		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_DatePanel");
		createDateWidget(datePanel);
		
		HorizontalPanel timePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_TimePanel");
		createTimeWidget(timePanel);
		
		
		final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
		
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
		queryGrid.setWidget(5, 0, datePanel);
		queryGrid.setWidget(5, 1, timePanel);
		queryGrid.setWidget(6, 0, QuantityLabel);
		queryGrid.setWidget(7, 0, QuantityTextBox);
		queryGrid.setWidget(6, 1, UnitsLabel);
		queryGrid.setWidget(7, 1, UnitslistBox);
		
		queryGrid.setStyleName("attr-grid");
		
		modalBody.add(messageFormgroup);
		modalBody.add(queryGrid);
		

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
					UnitslistBox.setSelectedIndex(0);
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
					
					//Quantity TextBox Validation
					/*if(validateQuantity(QuantityTextBox.getText())){
						
					}*/
					
					//check if all fields are null
					if(yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty() && ddTxtBox.getText().isEmpty() 
							&& hhTextBox.getText().isEmpty() && minTxtBox.getText().isEmpty() 
							&& ssTxtBox.getText().isEmpty() && msTxtBox.getText().isEmpty()){
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please Enter a valid Date/Time.");
						messageFormgroup.setValidationState(ValidationState.ERROR);
						 
						//check if either date and time fields are not null
					} else if((!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty() || !ddTxtBox.getText().isEmpty()) 
							&& (!hhTextBox.getText().isEmpty() || !minTxtBox.getText().isEmpty() 
							|| !ssTxtBox.getText().isEmpty() || !msTxtBox.getText().isEmpty())){
						
						StringBuilder sb = new StringBuilder();
						sb.append(yyyyTxtBox.getText()).append("/").append(mmTxtBox.getText()).append("/").append(ddTxtBox.getText());
					    
						if(!yyyyTxtBox.getText().isEmpty() && !mmTxtBox.getText().isEmpty() && !ddTxtBox.getText().isEmpty() && 
								isValidate(sb.toString())){
							//validate 
							if(validateTime()){
								int columnIndex = editor.getCursorPosition().getColumn();
								System.out.println(columnIndex);
								editor.insertAtCursor(buildDateTimeString());
								editor.focus();
								dialogModal.hide();
							} else {
								hourFormGroup.setValidationState(ValidationState.ERROR);
								minFormGroup.setValidationState(ValidationState.ERROR);
								secondsFormGroup.setValidationState(ValidationState.ERROR);
								millisecFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText("Please Enter a valid Date/Time.");
								messageFormgroup.setValidationState(ValidationState.ERROR);
							}
							
						} else {
							yearFormGroup.setValidationState(ValidationState.ERROR);
							mmFormGroup.setValidationState(ValidationState.ERROR);
							ddFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Please Enter a valid Date/Time.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
						
						
						
					} else if((!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty() || !ddTxtBox.getText().isEmpty()) 
							&& (hhTextBox.getText().isEmpty() && minTxtBox.getText().isEmpty() 
							&& ssTxtBox.getText().isEmpty() && msTxtBox.getText().isEmpty())){
						
						if(validateDate()){
							int columnIndex = editor.getCursorPosition().getColumn();
							System.out.println(columnIndex);
							editor.insertAtCursor(buildDateTimeString());
							editor.focus();
							dialogModal.hide();
						} else {
							yearFormGroup.setValidationState(ValidationState.ERROR);
							mmFormGroup.setValidationState(ValidationState.ERROR);
							ddFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Please Enter a valid Date/Time.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
					} else if((yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty() && ddTxtBox.getText().isEmpty()) 
							&& (!hhTextBox.getText().isEmpty() || !minTxtBox.getText().isEmpty() 
							|| !ssTxtBox.getText().isEmpty() || !msTxtBox.getText().isEmpty())){
						if(validateTime()){
							int columnIndex = editor.getCursorPosition().getColumn();
							System.out.println(columnIndex);
							editor.insertAtCursor(buildDateTimeString());
							editor.focus();
							dialogModal.hide();
						} else {
							hourFormGroup.setValidationState(ValidationState.ERROR);
							minFormGroup.setValidationState(ValidationState.ERROR);
							secondsFormGroup.setValidationState(ValidationState.ERROR);
							millisecFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Please Enter a valid Date/Time.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}
					}
										
				} else {
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please Select Item name to insert into Editor");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				
			}

		});
		dialogModal.show();
		}
	
	
	private static boolean validateQuantity(String text) {
		
		char ch = text.charAt(text.length()-1); 
		if(ch=='.' || ch=='-'){
			return false;
		}
		return true;
	}


	private static boolean validateDate() {
		
			
		if(!mmTxtBox.getText().isEmpty() && yyyyTxtBox.getText().isEmpty()){
			return false;
		} else if(!ddTxtBox.getText().isEmpty() && (mmTxtBox.getText().isEmpty()|| yyyyTxtBox.getText().isEmpty())){
			return false;
		} else if(!yyyyTxtBox.getText().isEmpty() && !mmTxtBox.getText().isEmpty() && !ddTxtBox.getText().isEmpty()){
			StringBuilder sb = new StringBuilder();
			sb.append(yyyyTxtBox.getText()).append("/").append(mmTxtBox.getText()).append("/").append(ddTxtBox.getText());
			if(!isValidate(sb.toString())){
				return false;
			}	
		} else if(!inRange(yyyyTxtBox.getText(), "0001", "9999") || !inRange(mmTxtBox.getText(), "01", "12") || !inRange(ddTxtBox.getText(), "01", "31")){
			return false;
		}
		return true;
	}
	
	
	private static boolean validateTime() {
		
		if(!minTxtBox.getText().isEmpty() && hhTextBox.getText().isEmpty()){
			return false;
		} else if(!ssTxtBox.getText().isEmpty() && (hhTextBox.getText().isEmpty()|| minTxtBox.getText().isEmpty())){
			return false;
		} else if(!msTxtBox.getText().isEmpty() && (hhTextBox.getText().isEmpty()|| ssTxtBox.getText().isEmpty() || minTxtBox.getText().isEmpty())){
			return false;
		} else if(!inRange(hhTextBox.getText(), "00", "24") || !inRange(minTxtBox.getText(), "00", "59") || !inRange(ssTxtBox.getText(), "00", "59")
				|| !inRange(msTxtBox.getText(), "000", "999")){
			return false;
		}
		return true;
	}
	
	
	/**
	 * Creates the date widget.
	 *
	 * @param datePanel the date panel
	 */
	private static void createDateWidget(HorizontalPanel datePanel) {
		
		yyyyTxtBox.clear();
		yyyyTxtBox.setWidth("50px");
		yearFormGroup.add(yyyyTxtBox);
		
		mmTxtBox.clear();
		mmTxtBox.setWidth("50px");
		mmFormGroup.add(mmTxtBox);
		
		ddTxtBox.clear();
		ddTxtBox.setWidth("50px");
		ddFormGroup.add(ddTxtBox);
		
		final FormLabel yearFormLabel = new FormLabel();
		yearFormLabel.setText("YYYY");
		yearFormLabel.setTitle("Year");
		yearFormLabel.setStyleName("year-Label");
		
		final FormLabel monthFormLabel = new FormLabel();
		monthFormLabel.setText("MM");
		monthFormLabel.setTitle("Month");
		monthFormLabel.setStyleName("month-Label");
		
		final FormLabel dayFormLabel = new FormLabel();
		dayFormLabel.setText("DD");
		dayFormLabel.setTitle("Day");
		dayFormLabel.setStyleName("day-Label");
		
		/*datePanel.add(yearFormGroup);
		datePanel.add(mmFormGroup);
		datePanel.add(ddFormGroup);*/
		 
		Grid queryGrid = new Grid(2, 5);
		queryGrid.setWidget(0, 0, yearFormLabel);
		queryGrid.setWidget(1, 0, yearFormGroup);
		queryGrid.setWidget(1, 1, new HTML("<h2 style=\"margin:0px 5px 10px 5px\">/</h2>"));
		queryGrid.setWidget(0, 2, monthFormLabel);
		queryGrid.setWidget(1, 2, mmFormGroup);
		queryGrid.setWidget(1, 3, new HTML("<h2 style=\"margin:0px 5px 10px 5px\">/</h2>"));
		queryGrid.setWidget(0, 4, dayFormLabel);
		queryGrid.setWidget(1, 4, ddFormGroup);
		
		//queryGrid.setStyleName("dateTime-grid");
		
		datePanel.add(queryGrid);
		
		//year range validator
		yyyyTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String year = yyyyTxtBox.getText();
				if(!year.isEmpty()){
					if(!inRange(year, "0001", "9999")){
						yearFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						yearFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
		//month range validator
		mmTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String month = mmTxtBox.getText();
				if(!month.isEmpty()){
					if(!inRange(month, "01", "12")){
						mmFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						mmFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
		//day range validator
		ddTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String day = ddTxtBox.getText();
				if(!day.isEmpty()){
					if(!inRange(day, "01", "31")){
						ddFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						ddFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
	}
	
	
	/**
	 * Creates the time widget.
	 *
	 * @param timePanel the time panel
	 */
	private static void createTimeWidget(HorizontalPanel timePanel) {
		
		hhTextBox.clear();
		hhTextBox.setWidth("50px");
		hourFormGroup.add(hhTextBox);
		
		minTxtBox.clear();
		minTxtBox.setWidth("50px");
		minFormGroup.add(minTxtBox);
		
		ssTxtBox.clear();
		ssTxtBox.setWidth("50px");
		secondsFormGroup.add(ssTxtBox);
		
		msTxtBox.clear();
		msTxtBox.setWidth("50px");
		millisecFormGroup.add(msTxtBox);
		
		final FormLabel hourFormLabel = new FormLabel();
		hourFormLabel.setText("HH");
		hourFormLabel.setTitle("Hour");
		hourFormLabel.setStyleName("hour-Label");
		
		final FormLabel minutesFormLabel = new FormLabel();
		minutesFormLabel.setText("mm");
		minutesFormLabel.setTitle("minute(s)");
		minutesFormLabel.setStyleName("minute-Label");
		
		final FormLabel secondsFormLabel = new FormLabel();
		secondsFormLabel.setText("ss");
		secondsFormLabel.setTitle("second(s)");
		secondsFormLabel.setStyleName("seconds-Label");
		
		final FormLabel millisecFormLabel = new FormLabel();
		millisecFormLabel.setText("fff");
		millisecFormLabel.setTitle("millisecond(s)");
		millisecFormLabel.setStyleName("millisec-Label");
		
		/*datePanel.add(yearFormGroup);
		datePanel.add(mmFormGroup);
		datePanel.add(ddFormGroup);*/
		 
		Grid queryGrid = new Grid(2, 7);
		queryGrid.setWidget(0, 0, hourFormLabel);
		queryGrid.setWidget(1, 0, hourFormGroup);
		queryGrid.setWidget(1, 1, new HTML("<h2 style=\"margin:0px 5px 15px 5px\">:</h2>"));
		queryGrid.setWidget(0, 2, minutesFormLabel);
		queryGrid.setWidget(1, 2, minFormGroup);
		queryGrid.setWidget(1, 3, new HTML("<h2 style=\"margin:0px 5px 15px 5px\">:</h2>"));
		queryGrid.setWidget(0, 4, secondsFormLabel);
		queryGrid.setWidget(1, 4, secondsFormGroup);
		queryGrid.setWidget(1, 5, new HTML("<h2 style=\"margin:0px 5px 15px 5px\">.</h2>"));
		queryGrid.setWidget(0, 6, millisecFormLabel);
		queryGrid.setWidget(1, 6, millisecFormGroup);
		
		//queryGrid.setStyleName("dateTime-grid");
		
		timePanel.add(queryGrid);
		
		//hour range validator
		hhTextBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String hour = hhTextBox.getText();
				if(!hour.isEmpty()){
					if(!inRange(hour, "01", "24")){
						hourFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						hourFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
		//minute range validator
		minTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String minute = minTxtBox.getText();
				if(!minute.isEmpty()){
					if(!inRange(minute, "00", "59")){
						minFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						minFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
		//seconds range validator
		ssTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String seconds = ssTxtBox.getText();
				if(!seconds.isEmpty()){
					if(!inRange(seconds, "00", "59")){
						secondsFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						secondsFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
		
		//milli seconds range validator
		msTxtBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String millisec = msTxtBox.getText();
				if(!millisec.isEmpty()){
					if(!inRange(millisec, "000", "999")){
						millisecFormGroup.setValidationState(ValidationState.ERROR);
					} else {
						millisecFormGroup.setValidationState(ValidationState.NONE);
					}
				}
			}
		});
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
	private static void addChangeHandlerIntoLists(final Modal dialogModal, final ViewDisplay searchDisplay,
			final ListBoxMVP availableItemToInsert, final ListBoxMVP listAllItemNames, final ListBoxMVP availableDatatypes,
			final ListBoxMVP availableAttributesToInsert, final FormGroup messageFormgroup,
			final HelpBlock helpBlock, final FormGroup availableItemTypeFormGroup, final FormGroup selectItemListFormGroup) {
		availableItemToInsert.addChangeHandler(new ChangeHandler() {
			
			/* (non-Javadoc)
			 * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
			 */
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
						showAttributesDialogBox(searchDisplay, currention_Section);
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
	
	/**
	 * In range.
	 *
	 * @param s the s
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @return true, if successful
	 */
	private static boolean inRange(String s, String lowerBound, String upperBound) {
		if(s.isEmpty()){
			return true;
		}
		int lower = Integer.parseInt(lowerBound);
		int upper = Integer.parseInt(upperBound);
		int value = Integer.parseInt(s);
		if(lower<=value && upper>=value){
			return true;
		}
		return false;
		//return  s.compareToIgnoreCase(lowerBound) >= 0 && s.compareToIgnoreCase(upperBound) <= 0;
	}
	
	
	/**
	 * Builds the date time string.
	 *
	 * @return the string
	 */
	private static String buildDateTimeString(){
		StringBuilder sb = new StringBuilder();
		
		if(!yyyyTxtBox.getText().isEmpty()){
			
			if(yyyyTxtBox.getText().length()<4){
				sb.append("@").append(appendZeroString(4-yyyyTxtBox.getText().length()))
				.append(yyyyTxtBox.getText());
				
			} else {
				sb.append("@"+yyyyTxtBox.getText());
			}
		} 
		if(!mmTxtBox.getText().isEmpty()){
			if(mmTxtBox.getText().length()<2){
				sb.append("//").append(appendZeroString(2-mmTxtBox.getText().length()))
				.append(mmTxtBox.getText());
				
			} else {
				sb.append("//"+mmTxtBox.getText());
			}
		}
		if(!ddTxtBox.getText().isEmpty()){
			if(ddTxtBox.getText().length()<2){
				sb.append("//").append(appendZeroString(2-ddTxtBox.getText().length()))
				.append(ddTxtBox.getText());
				
			} else {
				sb.append("//"+ddTxtBox.getText());
			}
		}
		if(!hhTextBox.getText().isEmpty()){
			
			if(hhTextBox.getText().length()<2){
				sb.append("T").append(appendZeroString(2-hhTextBox.getText().length()))
				.append(hhTextBox.getText());
			} else {
				sb.append("T"+hhTextBox.getText());
			}
		} 
		if(!minTxtBox.getText().isEmpty()){
			if(minTxtBox.getText().length()<2){
				sb.append(":").append(appendZeroString(2-minTxtBox.getText().length()))
				.append(minTxtBox.getText());
				
			} else {
				sb.append(":"+minTxtBox.getText());
			}
		} 
		if(!ssTxtBox.getText().isEmpty()){
			if(ssTxtBox.getText().length()<2){
				sb.append(":").append(appendZeroString(2-ssTxtBox.getText().length()))
				.append(ssTxtBox.getText());
				
			} else {
				sb.append(":"+ssTxtBox.getText());
			}
		} 
		if(!msTxtBox.getText().isEmpty()){
			sb.append("."+msTxtBox.getText());
		}
		
		return sb.toString();
	}
	
	private static String appendZeroString(int count){
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<count;i++){
			sb.append("0");
		}
		return sb.toString();
	}
	
	public static boolean isValidate(String inDate){
		DateTimeFormat format = DateTimeFormat.getFormat("yyyy/MM/dd");
		try {
			format.parseStrict(inDate);
		} catch (IllegalArgumentException pe) {
		      return false; 
		    }
		return true;
	}
}
