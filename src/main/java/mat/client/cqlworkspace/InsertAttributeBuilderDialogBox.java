package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.CancelButton;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.inapphelp.message.InAppHelpMessages;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.client.shared.datetime.DateTimeHelper;
import mat.client.shared.datetime.DateTimeWidget;
import mat.model.ModeDetailModel;
import mat.model.clause.QDSAttributes;

public class InsertAttributeBuilderDialogBox {
	private static final String ATTR_LABEL = "attr-Label";
	private static final String FORM_CONTROL = "form-control";
	private static final String MODE_DETAILS_ALERT = "Mode Details dropdown is now enabled.";
	private static final String MODE_ALERT = "Mode dropdown is now enabled.";
	private static final String DATE_TIME_ALERT = "Date/Time fields are now enabled.";
	private static final String QUANTITY_UNIT_DATE_TIME_ALERT = "Date/Time fields are now enabled. Quantity field is now enabled. Units dropdown is now enabled.";
	private static final String QUANTITY_UNIT_ALERT = "Quantity field is now enabled. Units dropdown is now enabled.";
	private static final String FACILITY_LOCATIONS = "facilityLocations";
	private static final String ATTR_ID = "id";
	private static final String PRESCRIBER_ID = "prescriberId";
	private static final String DISPENSER_ID = "dispenserId";
	private static final String DIAGNOSES = "diagnoses";
	private static final String COMPONENTS = "components";
	private static final String VALUE_SETS = "Value Sets";
	private static final String CODES = "Codes";
	private static final String NULLABLE = "Nullable";

	private static Map<String, String> allCqlUnits = MatContext.get().getCqlConstantContainer().getCqlUnitMap();

	private static HashSet<String> nonQuoteUnits = MatContext.get().getNonQuotesUnits();

	private static QDSAttributesServiceAsync attributeService = GWT.create(QDSAttributesService.class);

	private static List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();
	private static List<String> allAttributes = MatContext.get().getCqlConstantContainer().getCqlAttributeList();

	private static final ListBoxMVP dtAttriblistBox = new ListBoxMVP();
	private static final ListBoxMVP attriblistBox = new ListBoxMVP();
	private static final ListBoxMVP modelistBox = new ListBoxMVP();
	private static final ListBoxMVP modeDetailslistBox = new ListBoxMVP();
	private static final ListBoxMVP unitslistBox = new ListBoxMVP();

	private static final FormGroup dtFormGroup = new FormGroup();
	private static final FormGroup attrFormGroup = new FormGroup();
	private static final FormGroup modeFormGroup = new FormGroup();
	private static final FormGroup modeDetailsFormGroup = new FormGroup();
	private static final FormGroup quantityFormGroup = new FormGroup();
	private static final FormGroup unitFormGroup = new FormGroup();

	private static final HorizontalPanel dtPanel = new HorizontalPanel();
	private static final HorizontalPanel attrPanel = new HorizontalPanel();
	private static final HorizontalPanel modePanel = new HorizontalPanel();
	private static final HorizontalPanel modeDetailPanel = new HorizontalPanel();
	private static final HorizontalPanel quantityPanel = new HorizontalPanel();
	private static final HorizontalPanel unitPanel = new HorizontalPanel();

	private static final CustomQuantityTextBox quantityTextBox = new CustomQuantityTextBox(30);

	private static HelpBlock messageHelpBlock = new HelpBlock();

	private static DateTimeWidget dtw = new DateTimeWidget(false);
	
	private static HTML heading = new HTML();
	
	private static InAppHelp inAppHelp;
	private static Modal dialogModal;
	private static ClickHandler handler;
	private static AceEditor curEditor;

	public static void showAttributesDialogBox(final AceEditor editor) {
		dialogModal = new Modal();
		dialogModal.getElement().setAttribute("role", "dialog");
		
		ModalHeader dialogHeader = new ModalHeader();
		heading.setHTML("<h4><b>Insert Options for Attributes</b></h4>");
		heading.addStyleName("leftAligned");
		
		inAppHelp  = new InAppHelp(InAppHelpMessages.CQL_LIBRARY_ATTRIBUTE_MODAL);
		dialogHeader.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		
		inAppHelp.getHelpModal().addHideHandler(event -> handleClose());
		inAppHelp.getInAppHelpButton().addClickHandler(event -> showModal());
		dialogModal.add(dialogHeader);
		
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertAttrToAceEditor_Modal");
		dialogModal.setWidth("50%");
		dialogModal.setRemoveOnHide(true);
		
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		dialogModal.addDomHandler(handler, ClickEvent.getType());
		
		final ModalBody modalBody = new ModalBody();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");

		final FormGroup helpMessageFormGroup = new FormGroup();
		messageHelpBlock = new HelpBlock();
		helpMessageFormGroup.add(messageHelpBlock);
		helpMessageFormGroup.getElement().setAttribute("role", "alert");
		messageHelpBlock.setColor("transparent");
		messageHelpBlock.setHeight("0px");
		helpMessageFormGroup.setHeight("0px");

		curEditor = editor;

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

		final FormLabel dateTimeLabel = new FormLabel();
		dateTimeLabel.setText(DateTimeHelper.DATE_TIME);
		dateTimeLabel.setTitle(DateTimeHelper.ENTER_DATE_TIME);
		dateTimeLabel.setStyleName(ATTR_LABEL);

		final HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.getElement().setId("HorizontalPanel_DatePanel");
		datePanel.add(dtw.createDateWidget());

		final HorizontalPanel timePanel = new HorizontalPanel();
		timePanel.getElement().setId("HorizontalPanel_TimePanel");
		timePanel.add(dtw.createTimeWidget());

		dtw.setDateTimeEnabled(false);

		final Grid queryGrid = new Grid(6,2);
		queryGrid.setWidget(0, 0, dtFormGroup);
		queryGrid.setWidget(0, 1, attrFormGroup);
		queryGrid.setWidget(1, 0, modeFormGroup);
		queryGrid.setWidget(1, 1, modeDetailsFormGroup);
		queryGrid.setWidget(2, 0, dateTimeLabel);
		queryGrid.setWidget(3, 0, datePanel);
		queryGrid.setWidget(3, 1, timePanel);
		queryGrid.setWidget(4, 0, quantityFormGroup);
		queryGrid.setWidget(4, 1, unitFormGroup);
		queryGrid.setStyleName("attr-grid");

		modalBody.add(messageFormgroup);
		modalBody.add(helpMessageFormGroup);
		modalBody.add(queryGrid);

		final ModalFooter modalFooter = new ModalFooter();
		final ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		addButton.setId("addButton_Button");
		final Button closeButton = new CancelButton("InsertAttributeBox");
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		closeButton.setId("Cancel_button");
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);

		Collections.sort(allAttributes);
		addAvailableItems(dtAttriblistBox, allDataTypes);
		addAvailableItems(attriblistBox, allAttributes);

		dtAttriblistBox.addChangeHandler(event -> selectAttributesByDataType(messageFormgroup, helpBlock));

		attriblistBox.addChangeHandler(event -> selectAttributes(messageFormgroup, helpBlock));

		modelistBox.addChangeHandler(event -> selectMode(messageFormgroup, helpBlock));

		modeDetailslistBox.addChangeHandler(event -> selectModeDetails(messageFormgroup, helpBlock));

		quantityTextBox.addChangeHandler(event -> enterQuantity(messageFormgroup, helpBlock));

		unitslistBox.addChangeHandler(event -> selectUnits(messageFormgroup, helpBlock));

		addButton.addClickHandler(event -> clickInsertButton(dialogModal, messageFormgroup, helpBlock, curEditor));

		dialogModal.show();
	}
	
	private static void showModal() {
		removeModalFromParent();
		inAppHelp.getHelpModal().show();
		inAppHelp.getHelpModal().getElement().setTabIndex(-1);
		inAppHelp.getMessageFocusPanel().getElement().focus();
	}

	private static void handleClose() {
		inAppHelp.getHelpModal().removeFromParent();
		removeModalFromParent();
		dialogModal.show();
	}
	
	private static void removeModalFromParent() {
		dialogModal.removeFromParent();
	}

	private static void clickInsertButton(final Modal dialogModal, final FormGroup messageFormgroup,
			final HelpBlock helpBlock, final AceEditor curEditor) {
		final int selectedIndex = attriblistBox.getSelectedIndex();
		if (selectedIndex != 0) {
			helpBlock.setText("");
			messageFormgroup.setValidationState(ValidationState.NONE);
			if (modelistBox.getSelectedIndex() != 0) {
				if (modeDetailslistBox.getSelectedIndex() != 0) {
					if (quantityTextBox.isEnabled() && !dtw.getYyyyTxtBox().isEnabled()) {
						if (!validateQuantity(quantityTextBox.getText())) {
							quantityFormGroup.setValidationState(ValidationState.ERROR);
							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_QUANTITY());
							messageFormgroup.setValidationState(ValidationState.ERROR);
						} else {
							curEditor.insertAtCursor(attributeStringBuilder());
							curEditor.focus();
							dialogModal.hide();
						}
					} else if (dtw.getYyyyTxtBox().isEnabled() && !quantityTextBox.isEnabled()) {

						validateDateTimeWidget(curEditor, helpBlock, messageFormgroup, dialogModal);

					} else if (quantityTextBox.isEnabled() && dtw.getYyyyTxtBox().isEnabled()) {
						// this scenario both time widget and Quantity
						// are available for result

						if ((quantityTextBox.getText().isEmpty() && unitslistBox.getSelectedIndex() == 0)
								&& (dtw.getYyyyTxtBox().getText().isEmpty() && dtw.getMmTxtBox().getText().isEmpty()
										&& dtw.getDdTxtBox().getText().isEmpty() && dtw.getHhTextBox().getText().isEmpty()
										&& dtw.getMinTxtBox().getText().isEmpty() && dtw.getSsTxtBox().getText().isEmpty()
										&& dtw.getMsTxtBox().getText().isEmpty())) {

							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_ENTER_DATE_TIME_AND_QUANTITY());
							messageFormgroup.setValidationState(ValidationState.ERROR);

						} else if ((!quantityTextBox.getText().isEmpty() || unitslistBox.getSelectedIndex() != 0)
								&& (dtw.getYyyyTxtBox().getText().isEmpty() && dtw.getMmTxtBox().getText().isEmpty()
										&& dtw.getDdTxtBox().getText().isEmpty() && dtw.getHhTextBox().getText().isEmpty()
										&& dtw.getMinTxtBox().getText().isEmpty() && dtw.getSsTxtBox().getText().isEmpty()
										&& dtw.getMsTxtBox().getText().isEmpty())) {

							if (!validateQuantity(quantityTextBox.getText())) {
								quantityFormGroup.setValidationState(ValidationState.ERROR);
								helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
								helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_QUANTITY());
								messageFormgroup.setValidationState(ValidationState.ERROR);
							} else {
								curEditor.insertAtCursor(attributeStringBuilder());
								curEditor.focus();
								dialogModal.hide();
							}
						} else if ((quantityTextBox.getText().isEmpty() && unitslistBox.getSelectedIndex() == 0)
								&& (!dtw.getYyyyTxtBox().getText().isEmpty() || !dtw.getMmTxtBox().getText().isEmpty()
										|| !dtw.getDdTxtBox().getText().isEmpty() || !dtw.getHhTextBox().getText().isEmpty()
										|| !dtw.getMinTxtBox().getText().isEmpty() || !dtw.getSsTxtBox().getText().isEmpty()
										|| !dtw.getMsTxtBox().getText().isEmpty())) {

							validateDateTimeWidget(curEditor, helpBlock, messageFormgroup, dialogModal);

						} else {

							helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
							helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME_QUANTITY());
							messageFormgroup.setValidationState(ValidationState.ERROR);
						}

					} else {
						curEditor.insertAtCursor(attributeStringBuilder());
						curEditor.focus();
						dialogModal.hide();
					}
				} else {
					modeDetailsFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_MODE_DETAILS());
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
			} else {
				curEditor.insertAtCursor(attributeStringBuilder());
				curEditor.focus();
				dialogModal.hide();
			}
		} else {
			helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
			helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_SELECT_ATTRIBUTE_TO_INSERT());
			messageFormgroup.setValidationState(ValidationState.ERROR);
			attrFormGroup.setValidationState(ValidationState.ERROR);
		}
	}

	private static void selectUnits(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		unitFormGroup.setValidationState(ValidationState.NONE);
		messageFormgroup.setValidationState(ValidationState.NONE);
	}

	private static void enterQuantity(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		quantityFormGroup.setValidationState(ValidationState.NONE);
		messageFormgroup.setValidationState(ValidationState.NONE);
	}

	private static void selectModeDetails(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		messageFormgroup.setValidationState(ValidationState.NONE);
		final int selectedIndex = modeDetailslistBox.getSelectedIndex();
		if (selectedIndex != 0) {
			setWidgetEnabled(attriblistBox, modelistBox);
		} else {
			setEnabled(false);
		}
		defaultFrmGrpValidations();
		clearAllBoxes();
	}

	private static void selectMode(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		modeDetailslistBox.clear();
		messageFormgroup.setValidationState(ValidationState.NONE);
		final int selectedIndex = modelistBox.getSelectedIndex();
		if (selectedIndex != 0) {
			final String modeSelected = modelistBox.getItemText(selectedIndex);
			messageHelpBlock.setText(MODE_DETAILS_ALERT);
			modeDetailslistBox.setEnabled(true);

			addModeDetailslist(modeDetailslistBox,JSONAttributeModeUtility.getModeDetailsList(modeSelected));
			modeDetailslistBox.setSelectedIndex(0);
		} else {
			modeDetailslistBox.setEnabled(false);
			modeDetailslistBox.addItem(MatContext.PLEASE_SELECT);
		}
		setEnabled(false);
		defaultFrmGrpValidations();
		clearAllBoxes();
	}

	private static void selectAttributes(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		modelistBox.clear();
		modeDetailslistBox.clear();
		modelistBox.setEnabled(false);
		modeDetailslistBox.setEnabled(false);
		messageFormgroup.setValidationState(ValidationState.NONE);
		final int selectedIndex = attriblistBox.getSelectedIndex();
		if (selectedIndex != 0) {
			final String attrSelected = attriblistBox.getItemText(selectedIndex);
			messageHelpBlock.setText(MODE_ALERT);
			modelistBox.setEnabled(true);
			addModelist(modelistBox,JSONAttributeModeUtility.getAttrModeList(attrSelected));
			modelistBox.setSelectedIndex(0);
			if(isModeDisabledEntry(attrSelected)){
				modelistBox.setEnabled(false);
				modeDetailslistBox.setEnabled(false);
			}
		} else {
			modelistBox.setEnabled(false);
			modeDetailslistBox.setEnabled(false);
			modelistBox.addItem(MatContext.PLEASE_SELECT);
		}
		setEnabled(false);
		defaultFrmGrpValidations();
		clearAllBoxes();
	}

	private static void selectAttributesByDataType(final FormGroup messageFormgroup, final HelpBlock helpBlock) {
		helpBlock.setText("");
		dtFormGroup.setValidationState(ValidationState.NONE);
		modelistBox.clear();
		modeDetailslistBox.clear();
		modeDetailslistBox.setEnabled(false);
		modelistBox.setEnabled(false);
		messageFormgroup.setValidationState(ValidationState.NONE);
		final int selectedIndex = dtAttriblistBox.getSelectedIndex();
		if (selectedIndex != 0) {
			final String dataTypeSelected = dtAttriblistBox.getItemText(selectedIndex);
			getAllAttributesByDataType(attriblistBox, dataTypeSelected);
		} else {
			attriblistBox.clear();
			unitslistBox.setSelectedIndex(0);
			addAvailableItems(attriblistBox, allAttributes);
		}
		setEnabled(false);
		defaultFrmGrpValidations();
		clearAllBoxes();
	}

	private static boolean isModeDisabledEntry(final String attrSelected) {
		return attrSelected.equalsIgnoreCase(COMPONENTS) || attrSelected.equalsIgnoreCase(DIAGNOSES)
				|| attrSelected.equalsIgnoreCase(FACILITY_LOCATIONS) || attrSelected.equalsIgnoreCase(ATTR_ID)
				|| attrSelected.equalsIgnoreCase(PRESCRIBER_ID) || attrSelected.equalsIgnoreCase(DISPENSER_ID);
	}

	private static void clearAllFormGroups() {

		dtFormGroup.clear();
		attrFormGroup.clear();
		modeFormGroup.clear();
		modeDetailsFormGroup.clear();
		dtw.clearFormGroup();
		quantityFormGroup.clear();
		unitFormGroup.clear();
	}

	private static void createUnitWidget(final HorizontalPanel unitPanel) {
		unitPanel.clear();
		unitPanel.getElement().setId("HorizontalPanel_UnitsPanel");
		unitslistBox.clear();
		unitslistBox.setWidth("18em");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		unitslistBox.setVisibleItemCount(1);
		unitslistBox.setStyleName(FORM_CONTROL);
		unitslistBox.getElement().setId("Units_listBox");

		final Set<String> allUnits = allCqlUnits.keySet();
		for(final String unit : allUnits) {
			unitslistBox.addItem(unit, unit);
		}

		final FormLabel unitsLabel = new FormLabel();
		unitsLabel.setText("Units");
		unitsLabel.setTitle("Select Units");
		unitsLabel.setStyleName(ATTR_LABEL);
		unitsLabel.setFor("Units_listBox");

		unitFormGroup.clear();
		unitFormGroup.add(unitsLabel);
		unitFormGroup.add(unitslistBox);

		final Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, unitFormGroup);
		unitPanel.add(queryGrid);
	}

	private static void createQuantityWidget(final HorizontalPanel quantityPanel) {
		quantityPanel.clear();
		quantityPanel.getElement().setId("HorizontalPanel_QuantityPanel");
		quantityTextBox.clear();
		quantityTextBox.setWidth("18em");
		quantityTextBox.getElement().setId("Qantity_TextBox");

		final FormLabel quantityLabel = new FormLabel();
		quantityLabel.setText("Quantity");
		quantityLabel.setTitle("Select Quantity");
		quantityLabel.setStyleName(ATTR_LABEL);
		quantityLabel.setFor("Quantity");

		quantityFormGroup.clear();
		quantityFormGroup.add(quantityLabel);
		quantityFormGroup.add(quantityTextBox);

		final Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, quantityFormGroup);
		quantityPanel.add(queryGrid);
	}

	private static void createDataTypeWidget(final HorizontalPanel dtPanel) {
		dtPanel.clear();
		dtPanel.getElement().setId("HorizontalPanel_DtPanel");
		dtAttriblistBox.clear();
		dtAttriblistBox.setWidth("18em");
		dtAttriblistBox.setVisibleItemCount(10);
		dtAttriblistBox.getElement().setId("DataTypeBtAtrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		dtAttriblistBox.setVisibleItemCount(1);
		dtAttriblistBox.setStyleName(FORM_CONTROL);

		final FormLabel attrDataTypeLabel = new FormLabel();
		attrDataTypeLabel.setText("Attributes By DataType");
		attrDataTypeLabel.setTitle("Select Attributes By DataType");
		attrDataTypeLabel.setStyleName(ATTR_LABEL);
		attrDataTypeLabel.setFor("DataTypeBtAtrr_listBox");

		dtFormGroup.clear();
		dtFormGroup.add(attrDataTypeLabel);
		dtFormGroup.add(dtAttriblistBox);

		final Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, dtFormGroup);
		dtPanel.add(queryGrid);
	}

	private static void createAttributeWidget(final HorizontalPanel attrPanel) {
		attrPanel.clear();
		attrPanel.getElement().setId("HorizontalPanel_AttrPanel");
		attriblistBox.clear();
		attriblistBox.setWidth("18em");
		attriblistBox.setVisibleItemCount(10);
		attriblistBox.getElement().setId("Atrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		attriblistBox.setVisibleItemCount(1);
		attriblistBox.setStyleName(FORM_CONTROL);

		final FormLabel attributeLabel = new FormLabel();
		attributeLabel.setText("Attributes");
		attributeLabel.setTitle("Select Attributes");
		attributeLabel.setStyleName(ATTR_LABEL);
		attributeLabel.setFor("Atrr_listBox");

		attrFormGroup.clear();
		attrFormGroup.add(attributeLabel);
		attrFormGroup.add(attriblistBox);

		final Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, attrFormGroup);
		attrPanel.add(queryGrid);

	}

	private static void createModeWidget(final HorizontalPanel modePanel) {
		modePanel.clear();
		modePanel.getElement().setId("HorizontalPanel_ModePanel");
		modelistBox.clear();
		modelistBox.setWidth("18em");
		modelistBox.setVisibleItemCount(10);
		modelistBox.getElement().setId("Mode_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		modelistBox.setVisibleItemCount(1);
		modelistBox.setStyleName(FORM_CONTROL);
		modelistBox.setEnabled(false);

		final FormLabel modeLabel = new FormLabel();
		modeLabel.setText("Mode");
		modeLabel.setTitle("Select Mode");
		modeLabel.setStyleName(ATTR_LABEL);
		modeLabel.setFor("Mode_listBox");

		modeFormGroup.clear();
		modeFormGroup.add(modeLabel);
		modeFormGroup.add(modelistBox);

		final Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, modeFormGroup);
		modePanel.add(queryGrid);

	}

	private static void createModeDetailsWidget(final HorizontalPanel modeDetailPanel) {
		modeDetailPanel.clear();
		modeDetailPanel.getElement().setId("HorizontalPanel_ModeDetailsPanel");
		modeDetailslistBox.clear();
		modeDetailslistBox.setWidth("18em");
		modeDetailslistBox.setVisibleItemCount(10);
		modeDetailslistBox.getElement().setId("ModeDetails_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		modeDetailslistBox.setVisibleItemCount(1);
		modeDetailslistBox.setStyleName(FORM_CONTROL);
		modeDetailslistBox.setEnabled(false);

		final FormLabel modeDetailsLabel = new FormLabel();
		modeDetailsLabel.setText("Mode Details");
		modeDetailsLabel.setTitle("Select Mode Details");
		modeDetailsLabel.setStyleName(ATTR_LABEL);
		modeDetailsLabel.setFor("ModeDetails_listBox");

		modeDetailsFormGroup.clear();
		modeDetailsFormGroup.add(modeDetailsLabel);
		modeDetailsFormGroup.add(modeDetailslistBox);

		final Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, modeDetailsFormGroup);
		modeDetailPanel.add(queryGrid);
	}

	private static boolean validateQuantity(final String text) {
		if(text.isEmpty()){
			return false;
		}

		final char ch = text.charAt(text.length()-1);
		if(ch=='.' || ch=='-'){
			return false;
		}

		return true;
	}


	private static void validateDateTimeWidget(final AceEditor editor, final HelpBlock helpBlock, final FormGroup messageFormgroup, final Modal dialogModal) {
		if (dtw.getYyyyTxtBox().getText().isEmpty() && dtw.getMmTxtBox().getText().isEmpty() && dtw.getDdTxtBox().getText().isEmpty()
				&& dtw.getHhTextBox().getText().isEmpty() && dtw.getMinTxtBox().getText().isEmpty() && dtw.getSsTxtBox().getText().isEmpty()
				&& dtw.getMsTxtBox().getText().isEmpty()) {
			helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
			helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
			messageFormgroup.setValidationState(ValidationState.ERROR);

			// check if either date and time fields are not null
		} else if ((!dtw.getYyyyTxtBox().getText().isEmpty() || !dtw.getMmTxtBox().getText().isEmpty() || !dtw.getDdTxtBox().getText().isEmpty())
				&& (!dtw.getHhTextBox().getText().isEmpty() || !dtw.getMinTxtBox().getText().isEmpty() || !dtw.getSsTxtBox().getText().isEmpty()
						|| !dtw.getMsTxtBox().getText().isEmpty())) {

			final StringBuilder sb = new StringBuilder();
			sb.append(dtw.getYyyyTxtBox().getText()).append("/").append(dtw.getMmTxtBox().getText()).append("/").append(dtw.getDdTxtBox().getText());

			if (!dtw.getYyyyTxtBox().getText().isEmpty() && !dtw.getMmTxtBox().getText().isEmpty() && !dtw.getDdTxtBox().getText().isEmpty()
					&& DateTimeHelper.isValidDate(sb.toString())) {
				// validate
				if (dtw.isValidTime()) {
					editor.insertAtCursor(buildDateTimeString());
					editor.focus();
					dialogModal.hide();
				} else {
					dtw.getHourFormGroup().setValidationState(ValidationState.ERROR);
					dtw.getMinFormGroup().setValidationState(ValidationState.ERROR);
					dtw.getSecondsFormGroup().setValidationState(ValidationState.ERROR);
					dtw.getMillisecFormGroup().setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}

			} else {
				dtw.getYearFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getMmFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getDdFormGroup().setValidationState(ValidationState.ERROR);
				helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
				messageFormgroup.setValidationState(ValidationState.ERROR);
			}

		} else if ((!dtw.getYyyyTxtBox().getText().isEmpty() || !dtw.getMmTxtBox().getText().isEmpty() || !dtw.getDdTxtBox().getText().isEmpty())
				&& (dtw.getHhTextBox().getText().isEmpty() && dtw.getMinTxtBox().getText().isEmpty() && dtw.getSsTxtBox().getText().isEmpty()
						&& dtw.getMsTxtBox().getText().isEmpty())) {

			if (dtw.isValidDate()) {
				editor.insertAtCursor(buildDateTimeString());
				editor.focus();
				dialogModal.hide();
			} else {
				dtw.getYearFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getMmFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getDdFormGroup().setValidationState(ValidationState.ERROR);
				helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
				messageFormgroup.setValidationState(ValidationState.ERROR);
			}
		} else if ((dtw.getYyyyTxtBox().getText().isEmpty() && dtw.getMmTxtBox().getText().isEmpty() && dtw.getDdTxtBox().getText().isEmpty())
				&& (!dtw.getHhTextBox().getText().isEmpty() || !dtw.getMinTxtBox().getText().isEmpty() || !dtw.getSsTxtBox().getText().isEmpty()
						|| !dtw.getMsTxtBox().getText().isEmpty())) {
			if (dtw.isValidTime()) {
				editor.insertAtCursor(buildDateTimeString());
				editor.focus();
				dialogModal.hide();
			} else {
				dtw.getHourFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getMinFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getSecondsFormGroup().setValidationState(ValidationState.ERROR);
				dtw.getMillisecFormGroup().setValidationState(ValidationState.ERROR);
				helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
				messageFormgroup.setValidationState(ValidationState.ERROR);
			}
		}
	}

	private static void clearAllBoxes(){
		quantityTextBox.clear();
		unitslistBox.setSelectedIndex(0);
		dtw.clearDateTime();
	}

	private static void setWidgetEnabled(final ListBox attributeListBox, final ListBoxMVP modelistBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		String modeName = modelistBox.getItemText(modelistBox.getSelectedIndex());
		attributeName = attributeName.toLowerCase();
		modeName = modeName.toLowerCase();
		if (modeName.equalsIgnoreCase("comparison") || modeName.equalsIgnoreCase("computative")) {
			if (attributeName.contains("datetime") && modeName.equalsIgnoreCase("comparison")) {
				// the date time field is about to be enabled. If the it is currently disabled,
				// alert the user about the change.
				if(!dtw.getYyyyTxtBox().isEnabled() && !dtw.getMmTxtBox().isEnabled() && !dtw.getDdTxtBox().isEnabled()
						&& !dtw.getHhTextBox().isEnabled() && !dtw.getMinTxtBox().isEnabled() && !dtw.getSsTxtBox().isEnabled()
						&& !dtw.getMsTxtBox().isEnabled()) {
					messageHelpBlock.setText(DATE_TIME_ALERT);
				}

				dtw.setDateTimeEnabled(true);
				quantityTextBox.setEnabled(false);
				unitslistBox.setEnabled(false);
			} else if (attributeName.equalsIgnoreCase("result")) {
				setEnabled(true);
			} else {
				dtw.setDateTimeEnabled(false);

				// the quantity field and units dropdown is about to be enabled. If it is currently disabled,
				// alert the user about the change.
				if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled()) {
					messageHelpBlock.setText(QUANTITY_UNIT_ALERT);
				}

				quantityTextBox.setEnabled(true);
				unitslistBox.setEnabled(true);
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
		dtw.setDefaultValidationState();
		quantityFormGroup.setValidationState(ValidationState.NONE);
		unitFormGroup.setValidationState(ValidationState.NONE);
	}



	private static void setEnabled(final boolean enabled){
		// if all of them are currently disabled and are becoming enabled, alert the user that all of them are changing
		if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled()
				&& !dtw.getYyyyTxtBox().isEnabled() && !dtw.getMmTxtBox().isEnabled() && !dtw.getDdTxtBox().isEnabled()
				&& !dtw.getHhTextBox().isEnabled() && !dtw.getMinTxtBox().isEnabled() && !dtw.getSsTxtBox().isEnabled()
				&& !dtw.getMsTxtBox().isEnabled() && enabled) {
			messageHelpBlock.setText(QUANTITY_UNIT_DATE_TIME_ALERT);
		}

		// if only the quantity text boxes are currently disabled and are becoming enabled, alert the user that the quantity/units
		// are changing
		else if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled() && enabled) {
			messageHelpBlock.setText(QUANTITY_UNIT_ALERT);
		}

		// if only the date time fields are becoming enabled, alerrt the user that the date time field are changing.
		else if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled()
				&& !dtw.getYyyyTxtBox().isEnabled() && !dtw.getMmTxtBox().isEnabled() && !dtw.getDdTxtBox().isEnabled()
				&& !dtw.getHhTextBox().isEnabled() && !dtw.getMinTxtBox().isEnabled() && !dtw.getSsTxtBox().isEnabled() && enabled) {
			messageHelpBlock.setText(DATE_TIME_ALERT);
		}

		quantityTextBox.setEnabled(enabled);
		unitslistBox.setEnabled(enabled);
		dtw.setDateTimeEnabled(enabled);
	}

	private static void addModelist(final ListBoxMVP availableItemToInsert, final List<String> attrModeList) {
		availableItemToInsert.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < attrModeList.size(); i++) {
			if(!attrModeList.get(i).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				availableItemToInsert.addItem(attrModeList.get(i));
			}
		}
	}

	private static void addModeDetailslist(final ListBoxMVP availableItemToInsert, final List<ModeDetailModel> list) {
		final List<NameValuePair> retList = new ArrayList<>();
		for(int i=0; i < list.size();i++){
			final ModeDetailModel mode = list.get(i);
			final NameValuePair nvp = new NameValuePair();
			nvp.setName(mode.getModeName());
			nvp.setValue(mode.getModeValue());
			retList.add(nvp);
		}
		availableItemToInsert.setDropdownOptions(retList,true);
	}

	private static String attributeStringBuilder(){
		final StringBuilder sb = new StringBuilder();
		String selectedAttrItem = "";
		String selectedMode = "";
		String selectedMDetailsItem = "";
		final String selectedQuantity = quantityTextBox.getText();
		final String selectedUnit = allCqlUnits.get(unitslistBox.getItemText(unitslistBox.getSelectedIndex()));

		if(attriblistBox.getSelectedIndex()>0){
			selectedAttrItem = attriblistBox.getItemText(attriblistBox.getSelectedIndex());
		}

		if(modelistBox.getSelectedIndex()>0){
			selectedMode = modelistBox.getItemText(modelistBox.getSelectedIndex());
		}

		if(modeDetailslistBox.getSelectedIndex()>0){
			selectedMDetailsItem = modeDetailslistBox.getItemText(modeDetailslistBox.getSelectedIndex());
		}

		if(selectedMode.isEmpty() && selectedMDetailsItem.isEmpty()){
			sb.append(".").append(selectedAttrItem);
		}else if(selectedMode.equalsIgnoreCase(NULLABLE)){
			sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem);
		}else if(selectedMode.equalsIgnoreCase(VALUE_SETS) ||selectedMode.equalsIgnoreCase(CODES) ){
			final String[] valueArray = modeDetailslistBox.getValue().split(":", 2);
			String type="";
			String value = "";
			if(valueArray.length > 0){
				type = valueArray[0];
				value = valueArray[1];
				sb.append(".").append(selectedAttrItem);
				if(selectedAttrItem.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_ATTRIBUTE_RESULT)
						|| selectedAttrItem.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_ATTRIBUTE_TARGET_OUTCOME)){
					if(type.equalsIgnoreCase("valueset")) { // For Value Set
						sb.append(CQLWorkSpaceConstants.CQL_INSERT_AS_CODE_IN);
					} else { // For Code
						sb.append(CQLWorkSpaceConstants.CQL_INSERT_AS_CODE);
					}
				}else{
					if(type.equalsIgnoreCase("valueset")) { // For Value Set
						sb.append(CQLWorkSpaceConstants.CQL_INSERT_IN);
					} else { // For Code
						sb.append(CQLWorkSpaceConstants.CQL_CODE_EQUALS);
					}
				}

				sb.append(value);
			}

		}else if(quantityTextBox.isEnabled()){
			sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem).append(" ").append(selectedQuantity).append(" ");
			if(nonQuoteUnits.contains(selectedUnit)){
				sb.append(selectedUnit);
			} else if(!selectedUnit.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				sb.append("'").append(selectedUnit).append("'");
			}
		}
		return sb.toString();

	}

	private static String buildDateTimeString(){
		final StringBuilder sb = new StringBuilder();
		sb.append(".").append(attriblistBox.getItemText(attriblistBox.getSelectedIndex()));
		sb.append(" ").append(modeDetailslistBox.getItemText(modeDetailslistBox.getSelectedIndex()));
		sb.append(" ").append(dtw.buildDateTimeString());
		return sb.toString();
	}


	private static void addAvailableItems(final ListBoxMVP availableItemToInsert,  final List<String> availableInsertItemList) {
		availableItemToInsert.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < availableInsertItemList.size(); i++) {
			if(!availableInsertItemList.get(i).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				availableItemToInsert.addItem(availableInsertItemList.get(i));
			}
		}

	}

	private static void getAllAttributesByDataType(final ListBoxMVP availableAttributesToInsert, final String dataType){
		attributeService.getAllAttributesByDataType(dataType, new AsyncCallback<List<QDSAttributes>>() {

			@Override
			public void onSuccess(final List<QDSAttributes> result) {
				final List<String> filterAttrByDataTypeList = new ArrayList<>();
				for (final QDSAttributes qdsAttributes : result) {
					filterAttrByDataTypeList.add(qdsAttributes.getName());
				}
				Collections.sort(filterAttrByDataTypeList);
				availableAttributesToInsert.clear();
				addAvailableItems(availableAttributesToInsert, filterAttrByDataTypeList);
			}

			@Override
			public void onFailure(final Throwable caught) {}
		});
	}
}
