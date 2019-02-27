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
import mat.client.buttons.CancelButton;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.CustomDateTimeTextBox;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.model.ModeDetailModel;
import mat.model.clause.QDSAttributes;

public class InsertAttributeBuilderDialogBox {
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
	private static AceEditor curEditor; 
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
	private static List<String> allDataTypes = MatContext.get().getCqlConstantContainer().getCqlDatatypeList();
	private static List<String> allAttributes = MatContext.get().getCqlConstantContainer().getCqlAttributeList();

	final private static ListBoxMVP dtAttriblistBox = new ListBoxMVP();
	final private static ListBoxMVP attriblistBox = new ListBoxMVP();
	final private static ListBoxMVP modelistBox = new ListBoxMVP();
	final private static ListBoxMVP modeDetailslistBox = new ListBoxMVP();

	final private static FormGroup dtFormGroup = new FormGroup();
	final private static FormGroup attrFormGroup = new FormGroup();
	final private static FormGroup modeFormGroup = new FormGroup();
	final private static FormGroup modeDetailsFormGroup = new FormGroup();
	final private static FormGroup quantityFormGroup = new FormGroup();
	final private static FormGroup unitFormGroup = new FormGroup();

	final private static HorizontalPanel dtPanel = new HorizontalPanel();
	final private static HorizontalPanel attrPanel = new HorizontalPanel();
	final private static HorizontalPanel modePanel = new HorizontalPanel();
	final private static HorizontalPanel modeDetailPanel = new HorizontalPanel();
	final private static HorizontalPanel quantityPanel = new HorizontalPanel();
	final private static HorizontalPanel unitPanel = new HorizontalPanel();

	final private static CustomDateTimeTextBox yyyyTxtBox = new CustomDateTimeTextBox(4);
	final private static CustomDateTimeTextBox mmTxtBox = new CustomDateTimeTextBox(2);
	final private static CustomDateTimeTextBox ddTxtBox = new CustomDateTimeTextBox(2);
	final private static CustomDateTimeTextBox hhTextBox = new CustomDateTimeTextBox(2);
	final private static CustomDateTimeTextBox minTxtBox = new CustomDateTimeTextBox(2);
	final private static CustomDateTimeTextBox ssTxtBox = new CustomDateTimeTextBox(2);
	final private static CustomDateTimeTextBox msTxtBox = new CustomDateTimeTextBox(3);

	final private static FormGroup yearFormGroup = new FormGroup();
	final private static FormGroup mmFormGroup = new FormGroup();
	final private static FormGroup ddFormGroup = new FormGroup();
	final private static FormGroup hourFormGroup = new FormGroup();
	final private static FormGroup minFormGroup = new FormGroup();
	final private static FormGroup secondsFormGroup = new FormGroup();
	final private static FormGroup millisecFormGroup = new FormGroup();

	final private static CustomQuantityTextBox quantityTextBox = new CustomQuantityTextBox(30);
	final private static ListBoxMVP unitslistBox = new ListBoxMVP();

	private static FormGroup helpMessageFormGroup = new FormGroup(); 
	private static HelpBlock messageHelpBlock = new HelpBlock(); 

	public static void showAttributesDialogBox(final AceEditor editor) {
		final Modal dialogModal = new Modal();
		dialogModal.getElement().setAttribute("role", "dialog");
		dialogModal.setTitle("Insert Options for Attributes");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("InsertAttrToAceEditor_Modal");
		dialogModal.setWidth("50%");
		dialogModal.setRemoveOnHide(true);
		ModalBody modalBody = new ModalBody();
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");


		helpMessageFormGroup = new FormGroup(); 
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
		modalBody.add(helpMessageFormGroup);
		modalBody.add(queryGrid);

		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		addButton.setId("addButton_Button");
		Button closeButton = new CancelButton("InsertAttributeBox");
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

		dtAttriblistBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				dtFormGroup.setValidationState(ValidationState.NONE);
				modelistBox.clear();
				modeDetailslistBox.clear();
				modeDetailslistBox.setEnabled(false);
				modelistBox.setEnabled(false);
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = dtAttriblistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String dataTypeSelected = dtAttriblistBox.getItemText(selectedIndex);
					getAllAttibutesByDataType(attriblistBox, dataTypeSelected);
				} else {
					attriblistBox.clear();
					unitslistBox.setSelectedIndex(0);
					addAvailableItems(attriblistBox, allAttributes);
				}
				setEnabled(false);
				defaultFrmGrpValidations();
				clearAllBoxes();
			}
		});


		attriblistBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				modelistBox.clear();
				modeDetailslistBox.clear();
				modelistBox.setEnabled(false);
				modeDetailslistBox.setEnabled(false);
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = attriblistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String attrSelected = attriblistBox.getItemText(selectedIndex);
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

		});

		modelistBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				modeDetailslistBox.clear();
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = modelistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					String modeSelected = modelistBox.getItemText(selectedIndex);
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
		});

		modeDetailslistBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				int selectedIndex = modeDetailslistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					setWidgetEnabled(attriblistBox, modelistBox);	
				} else {
					setEnabled(false);
				}
				defaultFrmGrpValidations();
				clearAllBoxes();
			}
		});

		quantityTextBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				helpBlock.setText("");
				quantityFormGroup.setValidationState(ValidationState.NONE);
				messageFormgroup.setValidationState(ValidationState.NONE);
			}
		});

		unitslistBox.addChangeHandler(new ChangeHandler() {
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

				int selectedIndex = attriblistBox.getSelectedIndex();
				if (selectedIndex != 0) {
					helpBlock.setText("");
					messageFormgroup.setValidationState(ValidationState.NONE);
					if (modelistBox.getSelectedIndex() != 0) {
						if (modeDetailslistBox.getSelectedIndex() != 0) {
							if (quantityTextBox.isEnabled() && !yyyyTxtBox.isEnabled()) {
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
							} else if (yyyyTxtBox.isEnabled() && !quantityTextBox.isEnabled()) {

								validateDateTimeWidget(curEditor, helpBlock, messageFormgroup, dialogModal);

							} else if (quantityTextBox.isEnabled() && yyyyTxtBox.isEnabled()) {
								// this scenario both time widget and Quantity
								// are available for result

								if ((quantityTextBox.getText().isEmpty() && unitslistBox.getSelectedIndex() == 0)
										&& (yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty()
												&& ddTxtBox.getText().isEmpty() && hhTextBox.getText().isEmpty()
												&& minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
												&& msTxtBox.getText().isEmpty())) {

									helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
									helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_ENTER_DATE_TIME_AND_QUANTITY());
									messageFormgroup.setValidationState(ValidationState.ERROR);

								} else if ((!quantityTextBox.getText().isEmpty()
										|| unitslistBox.getSelectedIndex() != 0)
										&& (yyyyTxtBox.getText().isEmpty() && mmTxtBox.getText().isEmpty()
												&& ddTxtBox.getText().isEmpty() && hhTextBox.getText().isEmpty()
												&& minTxtBox.getText().isEmpty() && ssTxtBox.getText().isEmpty()
												&& msTxtBox.getText().isEmpty())) {

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
										&& (!yyyyTxtBox.getText().isEmpty() || !mmTxtBox.getText().isEmpty()
												|| !ddTxtBox.getText().isEmpty() || !hhTextBox.getText().isEmpty()
												|| !minTxtBox.getText().isEmpty() || !ssTxtBox.getText().isEmpty()
												|| !msTxtBox.getText().isEmpty())) {

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

		});
		dialogModal.show();
	}

	private static boolean isModeDisabledEntry(String attrSelected) {
		return attrSelected.equalsIgnoreCase(COMPONENTS) || attrSelected.equalsIgnoreCase(DIAGNOSES)
				|| attrSelected.equalsIgnoreCase(FACILITY_LOCATIONS) || attrSelected.equalsIgnoreCase(ATTR_ID)
				|| attrSelected.equalsIgnoreCase(PRESCRIBER_ID) || attrSelected.equalsIgnoreCase(DISPENSER_ID);
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
		unitslistBox.clear();
		unitslistBox.setWidth("18em");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		unitslistBox.setVisibleItemCount(1);
		unitslistBox.setStyleName("form-control");
		unitslistBox.getElement().setId("Units_listBox");

		Set<String> allUnits = allCqlUnits.keySet();
		for(String unit : allUnits) {
			unitslistBox.addItem(unit, unit);
		}

		FormLabel unitsLabel = new FormLabel();
		unitsLabel.setText("Units");
		unitsLabel.setTitle("Select Units");
		unitsLabel.setStyleName("attr-Label");
		unitsLabel.setFor("Units_listBox");

		unitFormGroup.clear();
		unitFormGroup.add(unitsLabel);
		unitFormGroup.add(unitslistBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, unitFormGroup);
		unitPanel.add(queryGrid);
	}

	private static void createQuantityWidget(HorizontalPanel quantityPanel) {
		quantityPanel.clear();
		quantityPanel.getElement().setId("HorizontalPanel_QuantityPanel");
		quantityTextBox.clear();
		quantityTextBox.setWidth("18em");
		quantityTextBox.getElement().setId("Qantity_TextBox");

		FormLabel quantityLabel = new FormLabel();
		quantityLabel.setText("Quantity");
		quantityLabel.setTitle("Select Quantity");
		quantityLabel.setStyleName("attr-Label");
		quantityLabel.setFor("Quantity");

		quantityFormGroup.clear();
		quantityFormGroup.add(quantityLabel);
		quantityFormGroup.add(quantityTextBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, quantityFormGroup);
		quantityPanel.add(queryGrid);
	}

	private static void createDataTypeWidget(HorizontalPanel dtPanel) {
		dtPanel.clear();
		dtPanel.getElement().setId("HorizontalPanel_DtPanel");
		dtAttriblistBox.clear();
		dtAttriblistBox.setWidth("18em");
		dtAttriblistBox.setVisibleItemCount(10);
		dtAttriblistBox.getElement().setId("DataTypeBtAtrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		dtAttriblistBox.setVisibleItemCount(1);
		dtAttriblistBox.setStyleName("form-control");

		FormLabel attrDataTypeLabel = new FormLabel();
		attrDataTypeLabel.setText("Attributes By DataType");
		attrDataTypeLabel.setTitle("Select Attributes By DataType");
		attrDataTypeLabel.setStyleName("attr-Label");
		attrDataTypeLabel.setFor("DataTypeBtAtrr_listBox");

		dtFormGroup.clear();
		dtFormGroup.add(attrDataTypeLabel);
		dtFormGroup.add(dtAttriblistBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, dtFormGroup);
		dtPanel.add(queryGrid);
	}

	private static void createAttributeWidget(HorizontalPanel attrPanel) {
		attrPanel.clear();
		attrPanel.getElement().setId("HorizontalPanel_AttrPanel");
		attriblistBox.clear();
		attriblistBox.setWidth("18em");
		attriblistBox.setVisibleItemCount(10);
		attriblistBox.getElement().setId("Atrr_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		attriblistBox.setVisibleItemCount(1);
		attriblistBox.setStyleName("form-control");

		FormLabel attributeLabel = new FormLabel();
		attributeLabel.setText("Attributes");
		attributeLabel.setTitle("Select Attributes");
		attributeLabel.setStyleName("attr-Label");
		attributeLabel.setFor("Atrr_listBox");

		attrFormGroup.clear();
		attrFormGroup.add(attributeLabel);
		attrFormGroup.add(attriblistBox);

		Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, attrFormGroup);
		attrPanel.add(queryGrid);

	}

	private static void createModeWidget(HorizontalPanel modePanel) {
		modePanel.clear();
		modePanel.getElement().setId("HorizontalPanel_ModePanel");
		modelistBox.clear();
		modelistBox.setWidth("18em");
		modelistBox.setVisibleItemCount(10);
		modelistBox.getElement().setId("Mode_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		modelistBox.setVisibleItemCount(1);
		modelistBox.setStyleName("form-control");
		modelistBox.setEnabled(false);

		FormLabel modeLabel = new FormLabel();
		modeLabel.setText("Mode");
		modeLabel.setTitle("Select Mode");
		modeLabel.setStyleName("attr-Label");
		modeLabel.setFor("Mode_listBox");

		modeFormGroup.clear();
		modeFormGroup.add(modeLabel);
		modeFormGroup.add(modelistBox);

		Grid queryGrid = new Grid(1, 1);
		queryGrid.setWidget(0, 0, modeFormGroup);
		modePanel.add(queryGrid);

	}

	private static void createModeDetailsWidget(HorizontalPanel modeDetailPanel) {
		modeDetailPanel.clear();
		modeDetailPanel.getElement().setId("HorizontalPanel_ModeDetailsPanel");
		modeDetailslistBox.clear();
		modeDetailslistBox.setWidth("18em");
		modeDetailslistBox.setVisibleItemCount(10);
		modeDetailslistBox.getElement().setId("ModeDetails_listBox");
		//setting itemcount value to 1 turns listbox into a drop-down list.
		modeDetailslistBox.setVisibleItemCount(1);
		modeDetailslistBox.setStyleName("form-control");
		modeDetailslistBox.setEnabled(false);

		FormLabel modeDetailsLabel = new FormLabel();
		modeDetailsLabel.setText("Mode Details");
		modeDetailsLabel.setTitle("Select Mode Details");
		modeDetailsLabel.setStyleName("attr-Label");
		modeDetailsLabel.setFor("ModeDetails_listBox");

		modeDetailsFormGroup.clear();
		modeDetailsFormGroup.add(modeDetailsLabel);
		modeDetailsFormGroup.add(modeDetailslistBox);

		Grid queryGrid = new Grid(1,1);
		queryGrid.setWidget(0, 0, modeDetailsFormGroup);
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

	private static void createDateWidget(HorizontalPanel datePanel) {
		yyyyTxtBox.clear();
		yyyyTxtBox.setWidth("50px");
		yyyyTxtBox.setEnabled(false);
		yyyyTxtBox.setTitle("Year");
		yearFormGroup.clear();
		yearFormGroup.add(yyyyTxtBox);

		mmTxtBox.clear();
		mmTxtBox.setWidth("50px");
		mmTxtBox.setEnabled(false);
		mmTxtBox.setTitle("Month");
		mmFormGroup.clear();
		mmFormGroup.add(mmTxtBox);

		ddTxtBox.clear();
		ddTxtBox.setWidth("50px");
		ddTxtBox.setEnabled(false);
		ddTxtBox.setTitle("Date");
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

		Grid queryGrid = new Grid(2, 5);
		queryGrid.setWidget(0, 0, yearFormLabel);
		queryGrid.setWidget(1, 0, yearFormGroup);
		queryGrid.setWidget(1, 1, new HTML("<h2 style=\"margin:0px 5px 10px 5px\">/</h2>"));
		queryGrid.setWidget(0, 2, monthFormLabel);
		queryGrid.setWidget(1, 2, mmFormGroup);
		queryGrid.setWidget(1, 3, new HTML("<h2 style=\"margin:0px 5px 10px 5px\">/</h2>"));
		queryGrid.setWidget(0, 4, dayFormLabel);
		queryGrid.setWidget(1, 4, ddFormGroup);
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
			helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
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
					helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}

			} else {
				yearFormGroup.setValidationState(ValidationState.ERROR);
				mmFormGroup.setValidationState(ValidationState.ERROR);
				ddFormGroup.setValidationState(ValidationState.ERROR);
				helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
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
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
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
				helpBlock.setText(MatContext.get().getMessageDelegate().getERROR_INVALID_DATE_TIME());
				messageFormgroup.setValidationState(ValidationState.ERROR);
			}
		}
	}

	private static void createTimeWidget(HorizontalPanel timePanel) {	
		hhTextBox.clear();
		hhTextBox.setWidth("50px");
		hhTextBox.setEnabled(false);
		hhTextBox.setTitle("Hours");
		hourFormGroup.clear();
		hourFormGroup.add(hhTextBox);

		minTxtBox.clear();
		minTxtBox.setWidth("50px");
		minTxtBox.setEnabled(false);
		minTxtBox.setTitle("Minutes");
		minFormGroup.clear();
		minFormGroup.add(minTxtBox);

		ssTxtBox.clear();
		ssTxtBox.setWidth("50px");
		ssTxtBox.setEnabled(false);
		ssTxtBox.setTitle("Seconds");
		secondsFormGroup.clear();
		secondsFormGroup.add(ssTxtBox);

		msTxtBox.clear();
		msTxtBox.setWidth("50px");
		msTxtBox.setEnabled(false);
		msTxtBox.setTitle("MilliSeconds");
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

		//milliseconds range validator
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
		quantityTextBox.clear();
		unitslistBox.setSelectedIndex(0);
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

	private static void setWidgetEnabled(ListBox attributeListBox, ListBoxMVP ModelistBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		String modeName = ModelistBox.getItemText(ModelistBox.getSelectedIndex());
		attributeName = attributeName.toLowerCase();
		modeName = modeName.toLowerCase(); 
		if (modeName.equalsIgnoreCase("comparison") || modeName.equalsIgnoreCase("computative")) {
			if (attributeName.contains("datetime") && modeName.equalsIgnoreCase("comparison")) {
				// the date time field is about to be enabled. If the it is currently disabled, 
				// alert the user about the change.
				if(!yyyyTxtBox.isEnabled() && !mmTxtBox.isEnabled() && !ddTxtBox.isEnabled() 
						&& !hhTextBox.isEnabled() && !minTxtBox.isEnabled() && !ssTxtBox.isEnabled()
						&& !msTxtBox.isEnabled()) {
					messageHelpBlock.setText(DATE_TIME_ALERT);
				}

				setDateTimeEnabled(true);
				quantityTextBox.setEnabled(false);
				unitslistBox.setEnabled(false);
			} else if (attributeName.equalsIgnoreCase("result")) {
				setEnabled(true);
			} else {
				setDateTimeEnabled(false);

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



	private static void setEnabled(boolean enabled){
		// if all of them are currently disabled and are becoming enabled, alert the user that all of them are changing 
		if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled() 
				&& !yyyyTxtBox.isEnabled() && !mmTxtBox.isEnabled() && !ddTxtBox.isEnabled() 
				&& !hhTextBox.isEnabled() && !minTxtBox.isEnabled() && !ssTxtBox.isEnabled()
				&& !msTxtBox.isEnabled() && enabled) {
			messageHelpBlock.setText(QUANTITY_UNIT_DATE_TIME_ALERT);
		}

		// if only the quantity text boxes are currently disabled and are becoming enabled, alert the user that the quantity/units
		// are changing
		else if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled() && enabled) {
			messageHelpBlock.setText(QUANTITY_UNIT_ALERT); 
		}

		// if only the date time fields are becoming enabled, alerrt the user that the date time field are changing. 
		else if(!quantityTextBox.isEnabled() && !unitslistBox.isEnabled() 
				&& !yyyyTxtBox.isEnabled() && !mmTxtBox.isEnabled() && !ddTxtBox.isEnabled() 
				&& !hhTextBox.isEnabled() && !minTxtBox.isEnabled() && !ssTxtBox.isEnabled() && enabled) {
			messageHelpBlock.setText(DATE_TIME_ALERT);
		}

		quantityTextBox.setEnabled(enabled);
		unitslistBox.setEnabled(enabled);
		setDateTimeEnabled(enabled);
	}

	private static void addModelist(ListBoxMVP availableItemToInsert, List<String> attrModeList) {
		availableItemToInsert.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < attrModeList.size(); i++) {
			if(!attrModeList.get(i).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				availableItemToInsert.addItem(attrModeList.get(i));
			}
		}
	}

	private static void addModeDetailslist(ListBoxMVP availableItemToInsert, List<ModeDetailModel> list) {
		List<NameValuePair> retList = new ArrayList<NameValuePair>();
		for(int i=0; i < list.size();i++){
			ModeDetailModel mode = list.get(i);
			NameValuePair nvp = new NameValuePair();
			nvp.setName(mode.getModeName());
			nvp.setValue(mode.getModeValue());
			retList.add(nvp);
		}
		availableItemToInsert.setDropdownOptions(retList,true);
	}

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
	}

	private static String attributeStringBuilder(){
		StringBuilder sb = new StringBuilder();
		String selectedAttrItem = "";
		String selectedMode = "";
		String selectedMDetailsItem = "";
		String selectedQuantity = quantityTextBox.getText();
		String selectedUnit = allCqlUnits.get(unitslistBox.getItemText(unitslistBox.getSelectedIndex()));

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
			String valueArray[] = modeDetailslistBox.getValue().split(":", 2);
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
		StringBuilder sb = new StringBuilder();
		String selectedAttrItem = attriblistBox.getItemText(attriblistBox.getSelectedIndex());
		String selectedMDetailsItem = modeDetailslistBox.getItemText(modeDetailslistBox.getSelectedIndex());
		sb.append(".").append(selectedAttrItem).append(" ").append(selectedMDetailsItem).append(" @");
		if(!yyyyTxtBox.getText().isEmpty()){

			if(yyyyTxtBox.getText().length()<4){
				sb.append(appendZeroString(4-yyyyTxtBox.getText().length())).append(yyyyTxtBox.getText());
			} else {
				sb.append(yyyyTxtBox.getText());
			}
		} 
		if(!mmTxtBox.getText().isEmpty()){
			if(mmTxtBox.getText().length()<2){
				sb.append("-").append(appendZeroString(2-mmTxtBox.getText().length())).append(mmTxtBox.getText());
			} else {
				sb.append("-"+mmTxtBox.getText());
			}
		}
		if(!ddTxtBox.getText().isEmpty()){
			if(ddTxtBox.getText().length()<2){
				sb.append("-").append(appendZeroString(2-ddTxtBox.getText().length())).append(ddTxtBox.getText());
			} else {
				sb.append("-"+ddTxtBox.getText());
			}
		}
		if(!hhTextBox.getText().isEmpty()){
			if(hhTextBox.getText().length()<2){
				sb.append("T").append(appendZeroString(2-hhTextBox.getText().length())).append(hhTextBox.getText());
			} else {
				sb.append("T"+hhTextBox.getText());
			}
		} 
		if(!minTxtBox.getText().isEmpty()){
			if(minTxtBox.getText().length()<2){
				sb.append(":").append(appendZeroString(2-minTxtBox.getText().length())).append(minTxtBox.getText());
			} else {
				sb.append(":"+minTxtBox.getText());
			}
		} 
		if(!ssTxtBox.getText().isEmpty()){
			if(ssTxtBox.getText().length()<2){
				sb.append(":").append(appendZeroString(2-ssTxtBox.getText().length())).append(ssTxtBox.getText());
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

	private static void addAvailableItems(ListBoxMVP availableItemToInsert, 
			List<String> availableInsertItemList) {
		availableItemToInsert.addItem(MatContext.PLEASE_SELECT);
		for (int i = 0; i < availableInsertItemList.size(); i++) {
			if(!availableInsertItemList.get(i).equalsIgnoreCase(MatContext.PLEASE_SELECT)){
				availableItemToInsert.addItem(availableInsertItemList.get(i));
			}
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
			public void onFailure(Throwable caught) {}
		});
	}
}
