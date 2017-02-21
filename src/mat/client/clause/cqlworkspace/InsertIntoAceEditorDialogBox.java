package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
import mat.client.shared.CustomDateTimeTextBox;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.JSONAttributeModeUtility;
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
	
	private static List<String> allQdmDataTypes = MatContext.get().getQdmDataTypeList();
	
	/** The all attributes. */
	private static List<String> allAttributes = MatContext.get().getAllAttributeList();
	
	private static List<String> allUnits = MatContext.get().getAllUnitsList(); 
	
	private static List<String> allCqlUnits = MatContext.get().getAllCQLUnits();
	
	private static Map<String,String> cqlUnitMap = new LinkedHashMap<String,String>();
	
	private static HashSet<String> nonQuoteUnits = new HashSet<String>();
	
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
	
	final static ListBoxMVP DtAttriblistBox = new ListBoxMVP();
	final static ListBoxMVP AttriblistBox = new ListBoxMVP();
	final static ListBoxMVP ModelistBox = new ListBoxMVP();
	final static ListBoxMVP ModeDetailslistBox = new ListBoxMVP();
	
	final static FormGroup dtFormGroup = new FormGroup();
	final static FormGroup attrFormGroup = new FormGroup();
	final static FormGroup modeFormGroup = new FormGroup();
	final static FormGroup modeDetailsFormGroup = new FormGroup();
	final static FormGroup quantityFormGroup = new FormGroup();
	final static FormGroup unitFormGroup = new FormGroup();
	
	final static HorizontalPanel dtPanel = new HorizontalPanel();
	final static HorizontalPanel attrPanel = new HorizontalPanel();
	final static HorizontalPanel modePanel = new HorizontalPanel();
	final static HorizontalPanel modeDetailPanel = new HorizontalPanel();
	final static HorizontalPanel quantityPanel = new HorizontalPanel();
	final static HorizontalPanel unitPanel = new HorizontalPanel();
	
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
	
    final static CustomQuantityTextBox QuantityTextBox = new CustomQuantityTextBox(30);
	final static ListBoxMVP UnitslistBox = new ListBoxMVP();
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
		addChangeHandlerIntoLists(dialogModal, searchDisplay, availableItemToInsert, listAllItemNames,availableDatatypes,allQDMDatatypes,
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
									if (itemTypeName.equalsIgnoreCase("Applied Value Sets/Codes")) {
										int selectedDatatypeIndex = allQDMDatatypes.getSelectedIndex();
										String dataType =null;
										if (selectedDatatypeIndex != 0) {
											if(!allQDMDatatypes.getValue(selectedDatatypeIndex).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
												dataType = allQDMDatatypes.getValue(selectedDatatypeIndex);
											}
											
										}
										String name = itemNameToBeInserted;
										if(dataType != null){
											StringBuilder sb = new StringBuilder();
											sb = sb.append("[\"" + dataType + "\"");
											sb = sb.append(": \"").append(name + "\"]");
											itemNameToBeInserted = sb.toString();
										} else {
											StringBuilder sb = new StringBuilder();
											sb = sb.append("\"" + name + "\"");
											itemNameToBeInserted = sb.toString();
										}
										
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
										helpBlock.setText("Please select Item Name or Datatype.");
										messageFormgroup.setValidationState(ValidationState.ERROR);
									}
								} else{
									selectItemListFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("Please select Item Name.");
									messageFormgroup.setValidationState(ValidationState.ERROR);
								}
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

		clearAllFormGroups();
		defaultFrmGrpValidations();
		setEnabled(false);
		clearAllBoxes();
		createDataTypeWidget(dtPanel);
		createAttributeWidget(attrPanel);
		createModeWidget(modePanel);
		createModeDetailsWidget(modeDetailPanel);
		createQuantityWidget(quantityPanel);
		createUnitWidget(unitPanel);
		
		//All the Labels
		FormLabel DateTimeLabel = new FormLabel();
		DateTimeLabel.setText("Date/Time");
		DateTimeLabel.setTitle("Select DateTime");
		DateTimeLabel.setStyleName("attr-Label");
		
		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_DatePanel");
		createDateWidget(datePanel);
		
		HorizontalPanel timePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_TimePanel");
		createTimeWidget(timePanel);
		
		
		final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
		
		Grid queryGrid = new Grid(6,2);
		queryGrid.setWidget(0, 0, dtFormGroup);
		queryGrid.setWidget(0, 1, attrFormGroup);
		queryGrid.setWidget(1, 0, modeFormGroup);
		queryGrid.setWidget(1, 1, modeDetailsFormGroup);
		queryGrid.setWidget(2, 0, DateTimeLabel);
		queryGrid.setWidget(3, 0, datePanel);
		queryGrid.setWidget(3, 1, timePanel);
		queryGrid.setWidget(4, 0, quantityFormGroup);
		queryGrid.setWidget(4, 1, unitFormGroup);
		
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
		
		Collections.sort(allAttributes);
		Collections.sort(allAttributes);
		addAvailableItems(DtAttriblistBox, allDataTypes);
		addAvailableItems(AttriblistBox, allAttributes);
		
		DtAttriblistBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				dtFormGroup.setValidationState(ValidationState.NONE);
				ModelistBox.clear();
				ModeDetailslistBox.clear();
				ModeDetailslistBox.setEnabled(false);
				ModelistBox.setEnabled(false);
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
				setEnabled(false);
				defaultFrmGrpValidations();
				clearAllBoxes();
			}
		});
		
		
		AttriblistBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				ModelistBox.clear();
				ModeDetailslistBox.clear();
				ModelistBox.setEnabled(false);
				ModeDetailslistBox.setEnabled(false);
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = AttriblistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String attrSelected = AttriblistBox.getItemText(selectedIndex);
					ModelistBox.setEnabled(true);
					addModelist(ModelistBox,JSONAttributeModeUtility.getAttrModeList(attrSelected));
					ModelistBox.setSelectedIndex(0);
				} else {
					ModelistBox.setEnabled(false);
					ModeDetailslistBox.setEnabled(false);
					ModelistBox.addItem(MatContext.get().PLEASE_SELECT);
				}
				setEnabled(false);
				defaultFrmGrpValidations();
				clearAllBoxes();
			}

		});
		
		ModelistBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				ModeDetailslistBox.clear();
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = ModelistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String modeSelected = ModelistBox.getItemText(selectedIndex);
					ModeDetailslistBox.setEnabled(true);
					addModeDetailslist(ModeDetailslistBox,JSONAttributeModeUtility.getModeDetailsList(modeSelected)); 
					ModeDetailslistBox.setSelectedIndex(0);
				} else {
					ModeDetailslistBox.setEnabled(false);
					ModeDetailslistBox.addItem(MatContext.get().PLEASE_SELECT);
				}
				setEnabled(false);
				defaultFrmGrpValidations();
				clearAllBoxes();
			}
		});
		
		ModeDetailslistBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = ModeDetailslistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					setWidgetEnabled(AttriblistBox, ModelistBox);	
				} else {
					setEnabled(false);
				}
				defaultFrmGrpValidations();
				clearAllBoxes();
			}
		});
		
		QuantityTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				quantityFormGroup.setValidationState(ValidationState.NONE);
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		UnitslistBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				unitFormGroup.setValidationState(ValidationState.NONE);
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				int selectedIndex = AttriblistBox.getSelectedIndex();

				if (selectedIndex != 0) {

					helpBlock.setText("");
					messageFormgroup.setValidationState(ValidationState.NONE);

					if (ModelistBox.getSelectedIndex() != 0) {

						if (ModeDetailslistBox.getSelectedIndex() != 0) {

							if (QuantityTextBox.isEnabled() && !yyyyTxtBox.isEnabled()) {

								if (!validateQuantity(QuantityTextBox.getText())) {
									quantityFormGroup.setValidationState(ValidationState.ERROR);
									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("Please Enter valid Quantity.");
									messageFormgroup.setValidationState(ValidationState.ERROR);
								} else {
									editor.insertAtCursor(attributeStringBuilder());
									editor.focus();
									dialogModal.hide();
								}

							} else if (yyyyTxtBox.isEnabled() && !QuantityTextBox.isEnabled()) {

								validateDateTimeWidget(editor, helpBlock, messageFormgroup, dialogModal);

							} else if (QuantityTextBox.isEnabled() && yyyyTxtBox.isEnabled()) {
								// this scenario both time widget and Quantity
								// are available for result

								if ((QuantityTextBox.getText().isEmpty() && UnitslistBox.getSelectedIndex() == 0)
										&& (yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty()
												&& ddTxtBox.getText().isEmpty() && hhTextBox.getText().isEmpty()
												&& minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
												&& msTxtBox.getText().isEmpty())) {

									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("Please enter DateTime or Quantity/units.");
									messageFormgroup.setValidationState(ValidationState.ERROR);

								} else if ((!QuantityTextBox.getText().isEmpty()
										|| UnitslistBox.getSelectedIndex() != 0)
										&& (yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty()
												&& ddTxtBox.getText().isEmpty() && hhTextBox.getText().isEmpty()
												&& minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
												&& msTxtBox.getText().isEmpty())) {

									if (!validateQuantity(QuantityTextBox.getText())) {
										quantityFormGroup.setValidationState(ValidationState.ERROR);
										helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
										helpBlock.setText("Please Enter valid Quantity.");
										messageFormgroup.setValidationState(ValidationState.ERROR);
									} else {
										editor.insertAtCursor(attributeStringBuilder());
										editor.focus();
										dialogModal.hide();
									}
								} else if ((QuantityTextBox.getText().isEmpty() && UnitslistBox.getSelectedIndex() == 0)
										&& (!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty()
												|| !ddTxtBox.getText().isEmpty() || !hhTextBox.getText().isEmpty()
												|| !minTxtBox.getText().isEmpty() || !ssTxtBox.getText().isEmpty()
												|| !msTxtBox.getText().isEmpty())) {

									validateDateTimeWidget(editor, helpBlock, messageFormgroup, dialogModal);

								} else {

									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText("You can not enter both DateTime and Quantity.");
									messageFormgroup.setValidationState(ValidationState.ERROR);
								}

							} else {
								editor.insertAtCursor(attributeStringBuilder());
								editor.focus();
								dialogModal.hide();
							}

						} else {
							modeDetailsFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText("Please Select valid Mode Details.");
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}

					} else {
						editor.insertAtCursor(attributeStringBuilder());
						editor.focus();
						dialogModal.hide();
					}

				} else {
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please Select Attribute to insert into Editor");
					messageFormgroup.setValidationState(ValidationState.ERROR);
					attrFormGroup.setValidationState(ValidationState.ERROR);
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
	private static void addChangeHandlerIntoLists(final Modal dialogModal, final ViewDisplay searchDisplay,
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
						for (int i = 0; i < searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getCqlLeftNavBarPanelView().getViewParameterList().get(i)
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
						for (int i = 0; i < searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions().size(); i++) {
							CQLFunctions functions = searchDisplay.getCqlLeftNavBarPanelView().getViewFunctions().get(i);
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
						for (int i = 0; i < searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions().size(); i++) {
							listAllItemNames.addItem(searchDisplay.getCqlLeftNavBarPanelView().getViewDefinitions().get(i)
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
					} else if (itemTypeSelected.equalsIgnoreCase("Applied Value Sets/Codes")) {
						dataTypeFormGroup.setVisible(true);
						listAllItemNames.clear();
						availableDatatypes.clear();
						
						availableAttributesToInsert.clear();
						listAllItemNames.setEnabled(true);
						availableDatatypes.setEnabled(false);
						availableAttributesToInsert.setEnabled(false);
						listAllItemNames.addItem(MatContext.get().PLEASE_SELECT);
						for (int i = 0; i < searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmList().size(); i++) {
							/*if(!searchDisplay.getAppliedQdmList().get(i)
									.getDataType().equalsIgnoreCase("attribute")){
								listAllItemNames.addItem(searchDisplay.getAppliedQdmList().get(i).getCodeListName()
										+ "." + searchDisplay.getAppliedQdmList().get(i).getDataType());
							}
							if(searchDisplay.getAppliedQdmList().get(i).getDataType() != null
									&& !searchDisplay.getAppliedQdmList().get(i)
									.getDataType().equalsIgnoreCase("attribute")){
								listAllItemNames.addItem(searchDisplay.getAppliedQdmList().get(i).getCodeListName()
										+ "." + searchDisplay.getAppliedQdmList().get(i).getDataType());
							} else if(searchDisplay.getAppliedQdmList().get(i).getDataType() == null){*/
								/*if(searchDisplay.getAppliedQdmList().get(i).getDisplayName() != null){
									listAllItemNames.addItem(searchDisplay.getAppliedQdmList().get(i).getDisplayName());
								} else {*/
									listAllItemNames.addItem(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmList().get(i).getCodeListName());
								//}
								
							//}
							
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
						
					} else if (itemTypeSelected.equalsIgnoreCase("Timing")) {
						//open new popup/dialogBox
						dialogModal.clear();
						dialogModal.hide();
						searchDisplay.resetMessageDisplay();
						InsertTimingExpressionIntoAceEditor.showTimingExpressionDialogBox(searchDisplay, currention_Section);
						searchDisplay.getCqlLeftNavBarPanelView().setIsPageDirty(true);
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
	
	private static void addModelist(ListBoxMVP availableItemToInsert, List<String> attrModeList) {
		availableItemToInsert.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < attrModeList.size(); i++) {
			if(!attrModeList.get(i).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
				availableItemToInsert.addItem(attrModeList.get(i));
			}
		}
	}
	
	private static void addModeDetailslist(ListBoxMVP availableItemToInsert, List<String> modeDetailsList) {
		availableItemToInsert.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < modeDetailsList.size(); i++) {
			if(!modeDetailsList.get(i).equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
				availableItemToInsert.addItem(modeDetailsList.get(i));
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
	 * Builds the quantity units string.
	 *
	 * @return the string
	 */
	private static String attributeStringBuilder(){
		nonQuoteUnits = getNonQuotesUnits();
		cqlUnitMap = getCqlUnitMap();
		StringBuilder sb = new StringBuilder();
		String selectedAttrItem = "";
		String selectedMode = "";
		String selectedMDetailsItem = "";
		String selectedQuantity = QuantityTextBox.getText();
		String selectedUnit = cqlUnitMap.get(UnitslistBox.getItemText(UnitslistBox.getSelectedIndex()));
		
		if(AttriblistBox.getSelectedIndex()>0){
			selectedAttrItem = AttriblistBox.getItemText(AttriblistBox.getSelectedIndex());
		}
		
		if(ModelistBox.getSelectedIndex()>0){
			selectedMode = ModelistBox.getItemText(ModelistBox.getSelectedIndex());
		}
		
		if(ModeDetailslistBox.getSelectedIndex()>0){
			selectedMDetailsItem = ModeDetailslistBox.getItemText(ModeDetailslistBox.getSelectedIndex());
		}
		 
		if(selectedMode.isEmpty() && selectedMDetailsItem.isEmpty()){
			sb.append(".").append(selectedAttrItem);
		}else if(selectedMode.equalsIgnoreCase("Nullable")){
			sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem);
		}else if(selectedMode.equalsIgnoreCase("Value Sets")){
			sb.append(".").append(selectedAttrItem).append(" in \"").append(selectedMDetailsItem).append("\"");
		}else if(QuantityTextBox.isEnabled()){
			
			sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem).append(" ").append(selectedQuantity).append(" ");
			if(nonQuoteUnits.contains(selectedUnit)){
				 sb.append(selectedUnit);
			} else if(!selectedUnit.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)) {
				sb.append("'").append(selectedUnit).append("'");
			}
		} 
		return sb.toString();
		
	}
	
	private static Map<String, String> getCqlUnitMap() {
		 Map<String, String> map = new LinkedHashMap<String,String>(); ;
		Iterator<String> i1 = allUnits.iterator();
		Iterator<String> i2 = allCqlUnits.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			map.put(i1.next(), i2.next());
		}
		return map;
	}

	private static HashSet<String> getNonQuotesUnits(){
		 HashSet<String> hset = 
	               new HashSet<String>();
		hset.add("millisecond");
		hset.add("milliseconds");
		hset.add("second");
		hset.add("seconds");
		hset.add("minute");
		hset.add("minutes");
		hset.add("hour");
		hset.add("hours");
		hset.add("day");
		hset.add("days");
		hset.add("week");
		hset.add("weeks");
		hset.add("month");
		hset.add("months");
		hset.add("year");
		hset.add("years");
		
		return hset;
	}
	/**
	 * Builds the date time string.
	 *
	 * @return the string
	 */
	private static String buildDateTimeString(){
		StringBuilder sb = new StringBuilder();
		String selectedAttrItem = AttriblistBox.getItemText(AttriblistBox.getSelectedIndex());
		String selectedMDetailsItem = ModeDetailslistBox.getItemText(ModeDetailslistBox.getSelectedIndex());
		sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem).append(" @");
		if(!yyyyTxtBox.getText().isEmpty()){
			
			if(yyyyTxtBox.getText().length()<4){
				sb.append(appendZeroString(4-yyyyTxtBox.getText().length()))
				.append(yyyyTxtBox.getText());
				
			} else {
				sb.append(yyyyTxtBox.getText());
			}
		} 
		if(!mmTxtBox.getText().isEmpty()){
			if(mmTxtBox.getText().length()<2){
				sb.append("-").append(appendZeroString(2-mmTxtBox.getText().length()))
				.append(mmTxtBox.getText());
				
			} else {
				sb.append("-"+mmTxtBox.getText());
			}
		}
		if(!ddTxtBox.getText().isEmpty()){
			if(ddTxtBox.getText().length()<2){
				sb.append("-").append(appendZeroString(2-ddTxtBox.getText().length()))
				.append(ddTxtBox.getText());
				
			} else {
				sb.append("-"+ddTxtBox.getText());
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
	
	
	private static void setWidgetEnabled(ListBox attributeListBox, ListBoxMVP ModelistBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		String modeName = ModelistBox.getItemText(ModelistBox.getSelectedIndex());
		attributeName = attributeName.toLowerCase();
		modeName = modeName.toLowerCase(); 
		
		if (modeName.equalsIgnoreCase("comparison") 
				|| modeName.equalsIgnoreCase("computative")) {

			if (attributeName.contains("datetime") && modeName.equalsIgnoreCase("comparison")) {
				setDateTimeEnabled(true);
				QuantityTextBox.setEnabled(false);
				UnitslistBox.setEnabled(false);
			} else if (attributeName.equalsIgnoreCase("result")) {
				setEnabled(true);
			} else {
				setDateTimeEnabled(false);
				QuantityTextBox.setEnabled(true);
				UnitslistBox.setEnabled(true);
			}
		} else {
			setEnabled(false);
		}
		
	}
private static void defaultFrmGrpValidations(){
		
		dtFormGroup.setValidationState(ValidationState.NONE);
		attrFormGroup.setValidationState(ValidationState.NONE);
		modeFormGroup.setValidationState(ValidationState.NONE);
		modeDetailsFormGroup.setValidationState(ValidationState.NONE);
		yearFormGroup.setValidationState(ValidationState.NONE);
		mmFormGroup.setValidationState(ValidationState.NONE);
		ddFormGroup.setValidationState(ValidationState.NONE);
		hourFormGroup.setValidationState(ValidationState.NONE);
		minFormGroup.setValidationState(ValidationState.NONE);
		secondsFormGroup .setValidationState(ValidationState.NONE);
		millisecFormGroup.setValidationState(ValidationState.NONE);
		quantityFormGroup.setValidationState(ValidationState.NONE);
		unitFormGroup.setValidationState(ValidationState.NONE);
	}
	
	private static void clearAllFormGroups() {
		
		dtFormGroup.clear();
		attrFormGroup.clear();
		modeFormGroup.clear();
		modeDetailsFormGroup.clear();
		yearFormGroup.clear();
		mmFormGroup.clear();
		ddFormGroup.clear();
		hourFormGroup.clear();
		minFormGroup.clear();
		secondsFormGroup.clear(); 
		millisecFormGroup.clear();
		quantityFormGroup.clear();
		unitFormGroup.clear();
	}

	private static void createUnitWidget(HorizontalPanel unitPanel) {
		unitPanel.clear();
		unitPanel.getElement().setId("HorizontalPanel_UnitsPanel");
		UnitslistBox.clear();
		UnitslistBox.setWidth("18em");
		UnitslistBox.setVisibleItemCount(10);
		UnitslistBox.getElement().setId("Units_listBox");
		for(String unit : allUnits) {
			UnitslistBox.addItem(unit);
		}
		//setting itemcount value to 1 turns listbox into a drop-down list.
		UnitslistBox.setVisibleItemCount(1);
		UnitslistBox.setStyleName("form-control");
		
		FormLabel UnitsLabel = new FormLabel();
		UnitsLabel.setText("Units");
		UnitsLabel.setTitle("Select Units");
		UnitsLabel.setStyleName("attr-Label");
		
		unitFormGroup.clear();
		unitFormGroup.add(UnitsLabel);
		unitFormGroup.add(UnitslistBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, unitFormGroup);
		
		//queryGrid.setStyleName("attr-grid");
		
		unitPanel.add(queryGrid);
	}

	private static void createQuantityWidget(HorizontalPanel quantityPanel) {
		quantityPanel.clear();
		quantityPanel.getElement().setId("HorizontalPanel_QuantityPanel");
		QuantityTextBox.clear();
		QuantityTextBox.setWidth("18em");
		QuantityTextBox.getElement().setId("Qantity_TextBox");
		
		FormLabel QuantityLabel = new FormLabel();
		QuantityLabel.setText("Quantity");
		QuantityLabel.setTitle("Select Quantity");
		QuantityLabel.setStyleName("attr-Label");
		
		quantityFormGroup.clear();
		quantityFormGroup.add(QuantityLabel);
		quantityFormGroup.add(QuantityTextBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, quantityFormGroup);
		
		//queryGrid.setStyleName("attr-grid");
		
		quantityPanel.add(queryGrid);
	}

	private static void createDataTypeWidget(HorizontalPanel dtPanel) {
		dtPanel.clear();
		dtPanel.getElement().setId("HorizontalPanel_DtPanel");
		//final ListBoxMVP DtAttriblistBox = new ListBoxMVP();
		DtAttriblistBox.clear();
		DtAttriblistBox.setWidth("18em");
		DtAttriblistBox.setVisibleItemCount(10);
		DtAttriblistBox.getElement().setId("DataTypeBtAtrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		DtAttriblistBox.setVisibleItemCount(1);
		DtAttriblistBox.setStyleName("form-control");

		FormLabel AttrDataTypeLabel = new FormLabel();
		AttrDataTypeLabel.setText("Attributes By DataType");
		AttrDataTypeLabel.setTitle("Select Attributes By DataType");
		AttrDataTypeLabel.setStyleName("attr-Label");

		dtFormGroup.clear();
		dtFormGroup.add(AttrDataTypeLabel);
		dtFormGroup.add(DtAttriblistBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, dtFormGroup);

		//queryGrid.setStyleName("attr-grid");

		dtPanel.add(queryGrid);
	}

	private static void createAttributeWidget(HorizontalPanel attrPanel) {
		attrPanel.clear();
		attrPanel.getElement().setId("HorizontalPanel_AttrPanel");
		//final ListBoxMVP AttriblistBox = new ListBoxMVP();
		AttriblistBox.clear();
		AttriblistBox.setWidth("18em");
		AttriblistBox.setVisibleItemCount(10);
		AttriblistBox.getElement().setId("Atrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		AttriblistBox.setVisibleItemCount(1);
		AttriblistBox.setStyleName("form-control");

		FormLabel AttributeLabel = new FormLabel();
		AttributeLabel.setText("Attributes");
		AttributeLabel.setTitle("Select Attributes");
		AttributeLabel.setStyleName("attr-Label");

		attrFormGroup.clear();
		attrFormGroup.add(AttributeLabel);
		attrFormGroup.add(AttriblistBox);

		Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, attrFormGroup);

		//queryGrid.setStyleName("attr-grid");

		attrPanel.add(queryGrid);

	}

	private static void createModeWidget(HorizontalPanel modePanel) {
		modePanel.clear();
		modePanel.getElement().setId("HorizontalPanel_ModePanel");
		//final ListBoxMVP ModelistBox = new ListBoxMVP();
		ModelistBox.clear();
		ModelistBox.setWidth("18em");
		ModelistBox.setVisibleItemCount(10);
		ModelistBox.getElement().setId("Mode_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		ModelistBox.setVisibleItemCount(1);
		ModelistBox.setStyleName("form-control");
		//Disabled by Default and enabled by other selections made.
		ModelistBox.setEnabled(false);
		
		FormLabel ModeLabel = new FormLabel();
		ModeLabel.setText("Mode");
		ModeLabel.setTitle("Select Mode");
		ModeLabel.setStyleName("attr-Label");
		
		modeFormGroup.clear();
		modeFormGroup.add(ModeLabel);
		modeFormGroup.add(ModelistBox);
		
		Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, modeFormGroup);
		
		//queryGrid.setStyleName("attr-grid");
		
		modePanel.add(queryGrid);
		
	}

	private static void createModeDetailsWidget(HorizontalPanel modeDetailPanel) {
		modeDetailPanel.clear();
		modeDetailPanel.getElement().setId("HorizontalPanel_ModeDetailsPanel");
		//final ListBoxMVP ModeDetailslistBox = new ListBoxMVP();
		ModeDetailslistBox.clear();
		ModeDetailslistBox.setWidth("18em");
		ModeDetailslistBox.setVisibleItemCount(10);
		ModeDetailslistBox.getElement().setId("ModeDetails_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		ModeDetailslistBox.setVisibleItemCount(1);
		ModeDetailslistBox.setStyleName("form-control");
		//Disabled by Default and enabled by other selections made.
		ModeDetailslistBox.setEnabled(false);
		
		FormLabel ModeDetailsLabel = new FormLabel();
		ModeDetailsLabel.setText("Mode Details");
		ModeDetailsLabel.setTitle("Select Mode Details");
		ModeDetailsLabel.setStyleName("attr-Label");
		
		modeDetailsFormGroup.clear();
		modeDetailsFormGroup.add(ModeDetailsLabel);
		modeDetailsFormGroup.add(ModeDetailslistBox);
		
		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, modeDetailsFormGroup);
		
		//queryGrid.setStyleName("attr-grid");
		
		modeDetailPanel.add(queryGrid);
	}
	
	private static boolean validateQuantity(String text) {
		
		if(text.isEmpty()){
			return false;
		}
		
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
		} else if(!inRange(yyyyTxtBox.getText(), "0000", "9999") || !inRange(mmTxtBox.getText(), "01", "12") || !inRange(ddTxtBox.getText(), "01", "31")){
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
		yyyyTxtBox.setEnabled(false);
		yearFormGroup.clear();
		yearFormGroup.add(yyyyTxtBox);
		
		mmTxtBox.clear();
		mmTxtBox.setWidth("50px");
		mmTxtBox.setEnabled(false);
		mmFormGroup.clear();
		mmFormGroup.add(mmTxtBox);
		
		ddTxtBox.clear();
		ddTxtBox.setWidth("50px");
		ddTxtBox.setEnabled(false);
		ddFormGroup.clear();
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
					if(!inRange(year, "0000", "9999")){
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
	
	private static void validateDateTimeWidget(AceEditor editor, HelpBlock helpBlock, FormGroup messageFormgroup,
			Modal dialogModal)

	{
		if (yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty() && ddTxtBox.getText().isEmpty()
				&& hhTextBox.getText().isEmpty() && minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
				&& msTxtBox.getText().isEmpty()) {
			helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
			helpBlock.setText("Please Enter a valid Date/Time.");
			messageFormgroup.setValidationState(ValidationState.ERROR);

			// check if either date and time fields are not null
		} else if ((!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty() || !ddTxtBox.getText().isEmpty())
				&& (!hhTextBox.getText().isEmpty() || !minTxtBox.getText().isEmpty() || !ssTxtBox.getText().isEmpty()
						|| !msTxtBox.getText().isEmpty())) {

			StringBuilder sb = new StringBuilder();
			sb.append(yyyyTxtBox.getText()).append("/").append(mmTxtBox.getText()).append("/")
					.append(ddTxtBox.getText());

			if (!yyyyTxtBox.getText().isEmpty() && !mmTxtBox.getText().isEmpty() && !ddTxtBox.getText().isEmpty()
					&& isValidate(sb.toString())) {
				// validate
				if (validateTime()) {
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

		} else if ((!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty() || !ddTxtBox.getText().isEmpty())
				&& (hhTextBox.getText().isEmpty() && minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
						&& msTxtBox.getText().isEmpty())) {

			if (validateDate()) {
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
		} else if ((yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty() && ddTxtBox.getText().isEmpty())
				&& (!hhTextBox.getText().isEmpty() || !minTxtBox.getText().isEmpty() || !ssTxtBox.getText().isEmpty()
						|| !msTxtBox.getText().isEmpty())) {
			if (validateTime()) {
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
	}

	/**
	 * Creates the time widget.
	 *
	 * @param timePanel
	 *            the time panel
	 */
	private static void createTimeWidget(HorizontalPanel timePanel) {
		
		hhTextBox.clear();
		hhTextBox.setWidth("50px");
		hhTextBox.setEnabled(false);
		hourFormGroup.clear();
		hourFormGroup.add(hhTextBox);
		
		minTxtBox.clear();
		minTxtBox.setWidth("50px");
		minTxtBox.setEnabled(false);
		minFormGroup.clear();
		minFormGroup.add(minTxtBox);
		
		ssTxtBox.clear();
		ssTxtBox.setWidth("50px");
		ssTxtBox.setEnabled(false);
		secondsFormGroup.clear();
		secondsFormGroup.add(ssTxtBox);
		
		msTxtBox.clear();
		msTxtBox.setWidth("50px");
		msTxtBox.setEnabled(false);
		millisecFormGroup.clear();
		millisecFormGroup.add(msTxtBox);
		
		final FormLabel hourFormLabel = new FormLabel();
		hourFormLabel.setText("hh");
		hourFormLabel.setTitle("Hour(s)");
		hourFormLabel.setStyleName("hour-Label");
		
		final FormLabel minutesFormLabel = new FormLabel();
		minutesFormLabel.setText("mm");
		minutesFormLabel.setTitle("Minute(s)");
		minutesFormLabel.setStyleName("minute-Label");
		
		final FormLabel secondsFormLabel = new FormLabel();
		secondsFormLabel.setText("ss");
		secondsFormLabel.setTitle("Second(s)");
		secondsFormLabel.setStyleName("seconds-Label");
		
		final FormLabel millisecFormLabel = new FormLabel();
		millisecFormLabel.setText("fff");
		millisecFormLabel.setTitle("Millisecond(s)");
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

	
	private static void clearAllBoxes(){
		QuantityTextBox.clear();
		UnitslistBox.setSelectedIndex(0);
		yyyyTxtBox.clear();
		mmTxtBox.clear();
		ddTxtBox.clear();
		hhTextBox.clear();
		minTxtBox.clear();
		ssTxtBox.clear();
		msTxtBox.clear();
	}
	
	private static void setDateTimeEnabled(boolean enabled){
		yyyyTxtBox.setEnabled(enabled);
		mmTxtBox.setEnabled(enabled);
		ddTxtBox.setEnabled(enabled);
		hhTextBox.setEnabled(enabled);
		minTxtBox.setEnabled(enabled);
		ssTxtBox.setEnabled(enabled);
		msTxtBox.setEnabled(enabled);
		
	}
	
	private static void setEnabled(boolean enabled){
		
		QuantityTextBox.setEnabled(enabled);
		UnitslistBox.setEnabled(enabled);
		setDateTimeEnabled(enabled);
	}
}
