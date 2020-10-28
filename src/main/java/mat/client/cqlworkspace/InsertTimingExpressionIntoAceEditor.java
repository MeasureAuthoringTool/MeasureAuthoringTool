package mat.client.cqlworkspace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.CancelButton;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.DropDownSubMenu;
import mat.client.shared.JSONCQLTimingExpressionUtility;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.DropDown;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;

public class InsertTimingExpressionIntoAceEditor {
	
	/** The timing expression obj. */
	private static TimingExpressionObj timingExpressionObj;
	
	/** The Constant dialogModal. */
	private static Modal dialogModal;

		/** The primary timing sub menu. */
	private static DropDownSubMenu primaryTimingSubMenu;
	
	/** The date time precision sub menu. */
	private static DropDownSubMenu dateTimePrecisionSubMenu; 
	
	/** The primary timing list. */
	private static List<String> primaryTimingList = MatContext.get().getCqlConstantContainer().getCqlTimingList();
	
	/** The timing precision list. */
	private static List<String> timingPrecisionList =  CQLWorkSpaceConstants.getTimingPrecisions();
	
	private static String timingExpStr;
	
	private static String dateTimePrecisonExp;
	
	private static String quantityOffsetExp;
	
	private static ClickHandler handler;
	
	/**
	 * Show timing expression dialog box.
	 *
	 * @param searchDisplay            the search display
	 * @param currentSection the current section
	 */

	public static void showTimingExpressionDialogBox(final CQLLeftNavBarPanelView cqlNavBarView, final AceEditor editor) {
		
		dialogModal = new Modal();
        primaryTimingSubMenu = new DropDownSubMenu();
        primaryTimingSubMenu.setId("primaryTimingSubMenu_Id");
	    dateTimePrecisionSubMenu = new DropDownSubMenu();
	    dateTimePrecisionSubMenu.setId("dateTimePrecisionSubMenu_Id");
	    timingExpressionObj = null;
	    timingExpStr = null;
	    dateTimePrecisonExp = null;
	    quantityOffsetExp = null;
		Button insertButton = new Button();
	    Button cancelButton = new CancelButton("InsertTimingIntoAceEditor");
	    final HelpBlock helpBlock = new HelpBlock();
	    final AnchorListItem timingQualifierItem = new AnchorListItem();
		final AnchorListItem quantityOffsetItem = new AnchorListItem();
		final AnchorListItem timePrecisionItem = new AnchorListItem();
		timingQualifierItem.setId("timingQualifierItem_Id");
		quantityOffsetItem.setId("quantityOffsetItem_Id");
		timePrecisionItem.setId("timePrecisionItem_Id");
		timePrecisionItem.setText("Time Precision");
		
		dialogModal.setTitle("Insert Timing Expression into CQL Editor");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setWidth("400px");
		dialogModal.setRemoveOnHide(true);
		
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		dialogModal.addDomHandler(handler, ClickEvent.getType());
		
		ModalBody modalBody = new ModalBody();
		modalBody.clear();
		
		final Button mainButton = new Button();
		final Button anchorButton = new Button();
		
		mainButton.setWidth("300px");
		createPrimaryTimingsSubMenu(mainButton, "Primary Timings", primaryTimingList);
		mainButton.setEnabled(false);
		mainButton.setText(CQLWorkSpaceConstants.CQL_TIMING_EXPRESSION);
		mainButton.setTitle(CQLWorkSpaceConstants.CQL_TIMING_EXPRESSION);
		mainButton.setType(ButtonType.DEFAULT);
		mainButton.setId("mainButton_id");
		
		Label cqlTimingExpressionLabel = new Label(LabelType.INFO, "Cql Timing Expression");
		cqlTimingExpressionLabel.setMarginTop(5);
		cqlTimingExpressionLabel.setId("cqlTimingExpresionLabel");
		final DropDown dropDown = new DropDown();		
		anchorButton.setDataToggle(Toggle.DROPDOWN);
		anchorButton.setIcon(IconType.ELLIPSIS_H);
		anchorButton.setId("anchorButton_id");
			
		dropDown.add(mainButton);
		dropDown.add(anchorButton);
		final DropDownMenu mainDropDownMenu = new DropDownMenu(); 
		mainDropDownMenu.setId("mainDropDownMenu_Id");
		timingQualifierItem.setText("Time Qualifiers");
		timingQualifierItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Anchor anchor = (Anchor)event.getSource();
				createAvailableTimingQualifiersModal(mainButton, anchor.getText());
				
			}
		});
		
		quantityOffsetItem.setText("Quantity Offset");
		quantityOffsetItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				createQuantityModal(mainButton);	
			}
		});
		
		dropDown.add(mainDropDownMenu);
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		
		insertButton.setType(ButtonType.PRIMARY);
		insertButton.getElement().setId("cqlInsertButton_Button");
		insertButton.setMarginTop(10);
		insertButton.setTitle("Insert");
		insertButton.setText("Insert");
		insertButton.setId("InsertButton_Id");
		cancelButton.setMarginTop(10);
		buttonToolBar.add(insertButton);
		buttonToolBar.add(cancelButton);
	
		Form bodyForm = new Form();
		
		final FormGroup messageFormgroup = new FormGroup();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		
		final FormGroup timingExpressionFormgroup = new FormGroup();
		timingExpressionFormgroup.add(cqlTimingExpressionLabel);
		timingExpressionFormgroup.add(new SpacerWidget());
		timingExpressionFormgroup.add(new SpacerWidget());
		timingExpressionFormgroup.add(dropDown);
		timingExpressionFormgroup.add(new SpacerWidget());
		timingExpressionFormgroup.add(new SpacerWidget());
	
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(timingExpressionFormgroup);
		bodyForm.add(formFieldSet);
		
		ModalFooter modalFooter = new ModalFooter();
		modalFooter.add(buttonToolBar);
		modalBody.setHeight("240px");
		modalBody.add(bodyForm);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		dialogModal.show();
		
		anchorButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mainDropDownMenu.clear();
				mainDropDownMenu.setWidth("300px");
				mainDropDownMenu.add(primaryTimingSubMenu);
				mainDropDownMenu.add(timingQualifierItem);
				mainDropDownMenu.add(quantityOffsetItem);
				timingExpressionObj = JSONCQLTimingExpressionUtility.getAvaialableTimingQualifierList(timingExpStr);
				if(timingExpressionObj != null){
	
					if(timingExpressionObj.getOptionList() != null 
							&& timingExpressionObj.getOptionList().size()>0){
						timingQualifierItem.setEnabled(true);
					} else {
						timingQualifierItem.setEnabled(false);
					}
					
					if(timingExpressionObj.isQuantity() && timingExpressionObj.isUnits()){
						quantityOffsetItem.setEnabled(true);
					} else {
						quantityOffsetItem.setEnabled(false);
					}
					if(timingExpressionObj.isDateTimePrecesion()){
						createTimePrecisionSubMenu(mainButton, "Time Precision", timingPrecisionList);
						mainDropDownMenu.add(dateTimePrecisionSubMenu);
					} else {
						timePrecisionItem.setEnabled(false);
						mainDropDownMenu.add(timePrecisionItem);
					}
				} else {
					timingQualifierItem.setEnabled(false);
					quantityOffsetItem.setEnabled(false);
					timePrecisionItem.setEnabled(false);
					mainDropDownMenu.add(timePrecisionItem);
				}
				
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});
	
		
		
		insertButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(mainButton.getText().equalsIgnoreCase(
						CQLWorkSpaceConstants.CQL_TIMING_EXPRESSION)){
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please select CQL Timing Expression");
					messageFormgroup.setValidationState(ValidationState.ERROR);
					anchorButton.setFocus(true);
					/*dropDown.setStyleName("dropdown-error");*/
				} else if(primaryTimingList.contains(mainButton.getText())){
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("QuantityOffset is required for the following CQL Timing Expression");
					messageFormgroup.setValidationState(ValidationState.ERROR);
					anchorButton.setFocus(true);
				} else {
					editor.insertAtCursor(mainButton.getText().replace("  ", " "));
					editor.focus();
					dialogModal.hide();
				}
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogModal.hide();
			}
		});
		
	}
	
	
	/**
	 * Creates the primary timings sub menu.
	 *
	 * @param mainButton the main button
	 * @param menuStr the menu str
	 * @param items the items
	 */
	public static void createPrimaryTimingsSubMenu(final Button mainButton, String menuStr, List<String> items){
		primaryTimingSubMenu.clear();
		Anchor anchor = new Anchor();
		anchor.setText(menuStr);
		anchor.setId("PrimaryTimingSubMenu_achor");
		
		DropDownMenu dropDownMenu = new DropDownMenu();
		dropDownMenu.setId("PrimaryTimingsSubMenu_DropDownMenu");
		ClickHandler clkHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				Anchor menuItem = (Anchor)event.getSource(); 
				mainButton.setText(menuItem.getText());
				mainButton.setTitle(menuItem.getText());
				timingExpStr = menuItem.getText();
				dateTimePrecisonExp = null;
				quantityOffsetExp = null;
			}
		};
		
		for(int i =0; i<items.size(); i++){
			AnchorListItem anchorListItem = new AnchorListItem();
			anchorListItem.setText(items.get(i));
			anchorListItem.addClickHandler(clkHandler);
			dropDownMenu.add(anchorListItem);
		}
		
		primaryTimingSubMenu.add(anchor);
		primaryTimingSubMenu.add(dropDownMenu);
	}
	
	
	
	/**
	 * Creates the time precision sub menu.
	 *
	 * @param mainButton the main button
	 * @param menuStr the menu str
	 * @param items the items
	 */
	public static void createTimePrecisionSubMenu(final Button mainButton, String menuStr, List<String> items){
		dateTimePrecisionSubMenu.clear();
		Anchor timePrecisionAnchor = new Anchor();
		timePrecisionAnchor.setText(menuStr);
		timePrecisionAnchor.setId("timePrecisionAnchor_Id");
		DropDownMenu dropDownMenu = new DropDownMenu();
		dropDownMenu.setId("TimePrecisionSubMenu_Anchor");
		ClickHandler clkHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				Anchor menuItem = (Anchor)event.getSource(); 
				dateTimePrecisonExp = menuItem.getText();
				String replaceStr = timingExpressionObj.getDateTimePrecOffset().replace("$", menuItem.getText());
				if(quantityOffsetExp!=null){
					replaceStr = replaceStr.replace("@", quantityOffsetExp);
				} {
					replaceStr = replaceStr.replace("@", "");
				}
				mainButton.setText(replaceStr);
				mainButton.setTitle(replaceStr);
			}
		};
		
		for(int i =0; i<items.size(); i++){
			AnchorListItem anchorListItem = new AnchorListItem();
			anchorListItem.setText(items.get(i));
			anchorListItem.addClickHandler(clkHandler);
			dropDownMenu.add(anchorListItem);
		}
		
		dateTimePrecisionSubMenu.add(timePrecisionAnchor);
		dateTimePrecisionSubMenu.add(dropDownMenu);
	}
	
	
	/**
	 * Creates the more viable option modal.
	 *
	 * @param mainAnchor the main anchor
	 * @param modalName the modal name
	 */
	private static void createAvailableTimingQualifiersModal(final Button mainAnchor, String modalName){
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("More Options for " + modalName);
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertCQLTimingToAceEditor_Modal");
		dialogModal.setRemoveOnHide(true);
		//dialogModal.setSize("375px","350px");
		//dialogModal.setSize(ModalSize.SMALL);
		dialogModal.setWidth("400px");
		ModalBody modalBody = new ModalBody();
		//modalBody.setSize("375px", "350px");
		Form bodyForm = new Form();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		
		final ListBox listBox = new ListBox();
		listBox.getElement().setId("listBox_ListBox");
		listBox.setWidth("18em");
		listBox.setVisibleItemCount(10);
		listBox.getElement().setId("TimingQualifier_listBox");
		List<String> moreOptionsList = timingExpressionObj.getOptionList();
		for(int i=0;i<moreOptionsList.size();i++){
			listBox.addItem(moreOptionsList.get(i));
		}
		
		FormGroup unitForm = new FormGroup();
		FormLabel viableValueLabel = new FormLabel();
		viableValueLabel.setText("Available Timing Qualifiers");
		viableValueLabel.setTitle("Available Timing Qualifiers");
		unitForm.add(viableValueLabel);
		unitForm.add(listBox);
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(unitForm);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		Button addButton = new Button();
		addButton.setText("Replace");
		addButton.setTitle("Replace");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		addButton.setId("addButton_Button");
		Button closeButton = new CancelButton("TimingInAceEditor");
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		closeButton.setId("Cancel_button");
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex = listBox.getSelectedIndex();
				if(selectedIndex !=-1){
					String selectedItem = listBox.getItemText(selectedIndex);
					mainAnchor.setText(selectedItem);
					timingExpStr = selectedItem;
					dateTimePrecisonExp = null;
					quantityOffsetExp = null;
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
		dialogModal.show();
	}
	
	/**
	 * Creates the quantity modal.
	 */
	private static void createQuantityModal(final Button mainButton){
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Add Quantity");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setWidth("400px");
		dialogModal.setRemoveOnHide(true);
		ModalBody modalBody = new ModalBody();
		modalBody.setHeight("250px");
		final ListBoxMVP unitList = new ListBoxMVP();
		unitList.getElement().setId("ListBox_unitList");
		unitList.clear();
		List<String> units = CQLWorkSpaceConstants.getQuantityOffsetUnits();
		unitList.addItem(MatContext.PLEASE_SELECT);
		for(int i=0;i< units.size();i++){
			unitList.addItem(units.get(i));
		}
		unitList.setEnabled(timingExpressionObj.isUnits());
		
		
		final ListBoxMVP relativeQualifier = new ListBoxMVP();
		relativeQualifier.getElement().setId("ListBox_relativeQualifier");
		relativeQualifier.clear();
		List<String> relativeQualifierList = primaryTimingList;
		relativeQualifier.addItem(MatContext.PLEASE_SELECT);
		for(int i=0;i< relativeQualifierList.size();i++){
			relativeQualifier.addItem(relativeQualifierList.get(i));
		}
		relativeQualifier.setEnabled(timingExpressionObj.isRelativeQualifier());
		
		Form bodyForm = new Form();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		
		final FormGroup quantForm = new FormGroup();
		FormLabel quantityValueLabel = new FormLabel();
		quantityValueLabel.setText("Quantity Value");
		quantityValueLabel.setTitle("Quantity Value");
		final TextBox quantText = new TextBox();
		quantText.setId("Textbox_Quantity");
		quantText.setEnabled(timingExpressionObj.isQuantity());
		quantForm.add(quantityValueLabel);
		quantForm.add(quantText);
		
		final FormGroup unitForm = new FormGroup();
		FormLabel unitsValueLabel = new FormLabel();
		unitsValueLabel.setText("Units");
		unitsValueLabel.setTitle("Units");
		unitForm.add(unitsValueLabel);
		unitForm.add(unitList);
		
		final FormGroup relativeQualifierForm = new FormGroup();
		FormLabel relativeQualifierLabel = new FormLabel();
		relativeQualifierLabel.setText("Relative Qualifier");
		relativeQualifierLabel.setTitle("Relative Qualifier");
		//final TextBox relativeQualifierText = new TextBox();
		relativeQualifierForm.add(relativeQualifierLabel);
		relativeQualifierForm.add(relativeQualifier);
		
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(quantForm);
		formFieldSet.add(unitForm);
		formFieldSet.add(relativeQualifierForm);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		
		
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		Button addButton = new Button();
		addButton.setText("Apply");
		addButton.setTitle("Apply");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		Button closeButton = new CancelButton("InsertTimingExpressionIntoAceEditor");
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		
		quantText.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				messageFormgroup.setValidationState(ValidationState.NONE);
				quantForm.setValidationState(ValidationState.NONE);
				helpBlock.setText("");	
			}
		});
		
		
		unitList.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				messageFormgroup.setValidationState(ValidationState.NONE);
				unitForm.setValidationState(ValidationState.NONE);
				helpBlock.setText("");	
			}
		});
		
		relativeQualifier.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				messageFormgroup.setValidationState(ValidationState.NONE);
				relativeQualifierForm.setValidationState(ValidationState.NONE);
				helpBlock.setText("");		
			}
		});
	
		
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				StringBuilder displayName = new StringBuilder();
				boolean isInValid = false;
				boolean isEmpty = !quantText.getText().isEmpty();
				if(quantText.getText() != null && isEmpty){
					displayName = displayName.append(quantText.getText()).append(" ");
				} else {
					isInValid = true;
					quantForm.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please enter the following fields");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				//Units
				int selectedItemIndex = unitList.getSelectedIndex();
				if(selectedItemIndex !=0){
					String unitName = unitList.getItemText(selectedItemIndex);
					if(!unitName.equalsIgnoreCase(MatContext.PLEASE_SELECT)){
						displayName = displayName.append(unitName).append(" ");
					} else {
						isInValid = true;
						unitForm.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please enter the following fields");
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
				} else {
					isInValid = true;
					unitForm.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please enter the following fields");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				//relativeQualifier
				int selectedRelativeQualifierIndex = relativeQualifier.getSelectedIndex();
				if(selectedRelativeQualifierIndex !=0){
					String relativeQualifierName = relativeQualifier.getItemText(selectedRelativeQualifierIndex);
					if(!relativeQualifierName.equalsIgnoreCase(MatContext.PLEASE_SELECT)){
						displayName = displayName.append(relativeQualifierName).append(" ");
					} else {
						
					}
				} else {
					if(timingExpressionObj.isRelativeQualifier()){
						relativeQualifierForm.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please Enter Following Fields");
						messageFormgroup.setValidationState(ValidationState.ERROR);
					}
				}
				
				if(displayName.length() >0 && !isInValid) {
					quantityOffsetExp = displayName.toString();
					String replaceStr = timingExpressionObj.getDateTimePrecOffset().replace("@", displayName.toString());
					if(dateTimePrecisonExp!=null){
						replaceStr = replaceStr.replace("$", dateTimePrecisonExp);
					} {
						replaceStr = replaceStr.replace("$ of", "");
					}
					mainButton.setText(replaceStr);
					mainButton.setTitle(replaceStr);
					dialogModal.hide();
				}
				
			}
			
		});
		dialogModal.show();
	}
	
	/**
	 * Gets the ace editor based on current section.
	 *
	 * @param searchDisplay the search display
	 * @param currentSection the current section
	 * @return the ace editor based on current section
	 *//*
	private static AceEditor getAceEditorBasedOnCurrentSection(ViewDisplay searchDisplay, String currentSection) {
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

}
