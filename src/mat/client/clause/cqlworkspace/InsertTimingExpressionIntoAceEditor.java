package mat.client.clause.cqlworkspace;

import java.util.List;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
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
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;

// TODO: Auto-generated Javadoc
/**
 * The Class InsertTimingExpressionIntoAceEditor.
 */
public class InsertTimingExpressionIntoAceEditor {
	
	/** The timing expression obj. */
	private static TimingExpressionObj timingExpressionObj = null;
	
	/** The Constant dialogModal. */
	private static Modal dialogModal;

		/** The primary timing sub menu. */
	private static DropDownSubMenu primaryTimingSubMenu;
	
	/** The date time precision sub menu. */
	private static DropDownSubMenu dateTimePrecisionSubMenu; 
	
	/** The primary timing list. */
	private static List<String> primaryTimingList =  CQLWorkSpaceConstants.getPrimaryTimings();
	
	/** The timing precision list. */
	private static List<String> timingPrecisionList =  CQLWorkSpaceConstants.getTimingPrecisions();
	
	
	/**
	 * Show timing expression dialog box.
	 *
	 * @param searchDisplay            the search display
	 * @param currentSection the current section
	 */

	public static void showTimingExpressionDialogBox(final ViewDisplay searchDisplay, String currentSection) {
		
		dialogModal = new Modal();
        primaryTimingSubMenu = new DropDownSubMenu();
	    dateTimePrecisionSubMenu = new DropDownSubMenu();
		Button insertButton = new Button();
	    Button cancelButton = new Button();
	    final HelpBlock helpBlock = new HelpBlock();
	    final AnchorListItem timingQualifierItem = new AnchorListItem();
		final AnchorListItem quantityOffsetItem = new AnchorListItem();
		final AnchorListItem timePrecisionItem = new AnchorListItem();
		timePrecisionItem.setText("Time Precision");
		
		dialogModal.setTitle("Insert Timing Expression into CQL Editor");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		//dialogModal.setSize("375px","350px");
		dialogModal.setId("InsertItemToAceEditor_Modal");
		//dialogModal.setSize(ModalSize.MEDIUM);
		dialogModal.setWidth("400px");
		ModalBody modalBody = new ModalBody();
		modalBody.clear();
		
        //modalBody.setSize("375px","350px");
		final Button mainButton = new Button();
		final Button anchorButton = new Button();
		
		mainButton.setWidth("300px");
		createPrimaryTimingsSubMenu(mainButton, "Primary Timings", primaryTimingList);
		mainButton.setEnabled(false);
		mainButton.setText(CQLWorkSpaceConstants.CQL_TIMING_EXPRESSION);
		mainButton.setTitle(CQLWorkSpaceConstants.CQL_TIMING_EXPRESSION);
		mainButton.setType(ButtonType.DEFAULT);
		
		final AceEditor editor = getAceEditorBasedOnCurrentSection(searchDisplay, currentSection);
		Label cqlTimingExpressionLabel = new Label(LabelType.INFO, "Cql Timing Expression");
		cqlTimingExpressionLabel.setMarginTop(5);
		cqlTimingExpressionLabel.setId("cqlTimingExpresionLabel");
		final DropDown dropDown = new DropDown();		
		anchorButton.setDataToggle(Toggle.DROPDOWN);
		anchorButton.setIcon(IconType.ELLIPSIS_H);
			
		dropDown.add(mainButton);
		dropDown.add(anchorButton);
		final DropDownMenu mainDropDownMenu = new DropDownMenu(); 
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
				createQuantityModal();	
			}
		});
		
		dropDown.add(mainDropDownMenu);
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		
		insertButton.setType(ButtonType.PRIMARY);
		insertButton.getElement().setId("cqlInsertButton_Button");
		insertButton.setMarginTop(10);
		insertButton.setTitle("Insert");
		insertButton.setText("Insert");
		
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.getElement().setId("cqlCancelButton_Button");
		cancelButton.setMarginTop(10);
		cancelButton.setTitle("Cancel");
		cancelButton.setText("Cancel");
		
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
				timingExpressionObj = JSONCQLTimingExpressionUtility.getAvaialableTimingQualifierList(mainButton.getText());
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
				} else {
					editor.insertAtCursor(" " + mainButton.getText());
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
		
		DropDownMenu dropDownMenu = new DropDownMenu();
		
		ClickHandler clkHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				Anchor menuItem = (Anchor)event.getSource(); 
				mainButton.setText(menuItem.getText());
				mainButton.setTitle(menuItem.getText());
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
		DropDownMenu dropDownMenu = new DropDownMenu();
		
		ClickHandler clkHandler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				Anchor menuItem = (Anchor)event.getSource(); 
				String replaceStr = timingExpressionObj.getDateTimePrecOffset().replace("$", menuItem.getText());
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
		Button closeButton = new Button();
		closeButton.setText("Cancel");
		closeButton.setTitle("Cancel");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
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
	private static void createQuantityModal(){
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Add Quantity");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertItemToAceEditor_Modal");
		dialogModal.setSize(ModalSize.SMALL);
		ModalBody modalBody = new ModalBody();
		final ListBoxMVP unitList = new ListBoxMVP();
		unitList.clear();
		unitList.setWidth("250px");
		List<String> units = PopulationWorkSpaceConstants.units;
		unitList.addItem(MatContext.get().PLEASE_SELECT);
		for(int i=0;i< units.size();i++){
			unitList.addItem(units.get(i));
		}
		Form bodyForm = new Form();
		FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		
		FormGroup quantForm = new FormGroup();
		FormLabel quantityValueLabel = new FormLabel();
		quantityValueLabel.setText("Quantity Value");
		quantityValueLabel.setTitle("Quantity Value");
		final TextBox quantText = new TextBox();
		quantForm.add(quantityValueLabel);
		quantForm.add(quantText);
		
		FormGroup unitForm = new FormGroup();
		FormLabel unitsValueLabel = new FormLabel();
		unitsValueLabel.setText("Units");
		unitsValueLabel.setTitle("Units");
		unitForm.add(unitsValueLabel);
		unitForm.add(unitList);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(quantForm);
		formFieldSet.add(unitForm);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		
		
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		Button addButton = new Button();
		addButton.setText("Apply");
		addButton.setTitle("Apply");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		Button closeButton = new Button();
		closeButton.setText("Cancel");
		closeButton.setTitle("Cancel");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				StringBuilder displayName = new StringBuilder();
				if((quantText.getText() != null) || !quantText.getText().isEmpty()){
					displayName = displayName.append(quantText.getText());
				}
				int selectedItemIndex = unitList.getSelectedIndex();
				if(selectedItemIndex !=0){
					String unitName = unitList.getItemText(selectedItemIndex);
					if(!unitName.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
						displayName = displayName.append(unitName);
					}
				}
				if(displayName.length() >0){
//					xmlTreeDisplay.addNode(displayName.toString(),displayName.toString(),CellTreeNode.CQL_TIMING_NODE);
				}
				dialogModal.hide();
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

}
