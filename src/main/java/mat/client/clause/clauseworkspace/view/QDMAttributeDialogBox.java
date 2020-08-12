package mat.client.clause.clauseworkspace.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import mat.client.ImageResources;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.JSONAttributeModeUtility;
import mat.client.shared.LabelBuilder;
import mat.model.clause.QDSAttributes;
import mat.shared.ConstantMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class QDMAttributeDialogBox {
	private static final String NEGATION_RATIONALE = "negation rationale";
	
	private static final String INSTANCE = "instance";

	static final String DATATYPE = "datatype";
	
	private static final String GREATER_THAN_OR_EQUAL_TO = "Greater Than Or Equal To";
	
	private static final String LESS_THAN_OR_EQUAL_TO = "Less Than Or Equal To";
	
	private static final String EQUAL_TO = "Equal To";
	
	private static final String GREATER_THAN = "Greater Than";
	
	private static final String LESS_THAN = "Less Than";
	
	private static final String UNIT = "unit";
	
	private static final String COMPARISON_VALUE = "comparisonValue";
	
	private static final String QDM_UUID = "qdmUUID";
	
	private static final String MODE = "mode";
	
	private static final String VALUE_SET = "Value Set";
	
	private static final String CHECK_IF_PRESENT = "Check if Present";
	
	private static final String NAME = "name";
	
	static final String ATTRIBUTE = "attribute";
	
	private static final String ATTRIBUTES = "attributes";
	
	private static final String QDM_ATTRIBUTES_TITLE = "Edit Attribute";
	
	private static final String SELECT = "--Select--";
	
	private static final String UUID = "uuid";
	
	private static QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT
			.create(QDSAttributesService.class);
	
	private static final List<String> unitNames = new ArrayList<String>();
	
	private static final List<String> attributeList = new ArrayList<String>();
	
	private static  TextBox quantityTextBox = new TextBox();
	
	private static  ListBox unitsListBox = new ListBox();
	
	private static  ListBox qdmListBox = new ListBox();
	
	private static DateBoxWithCalendar  qdmAttributeDate = new DateBoxWithCalendar();
	
	private static final String ATTRIBUTE_DATE = "attrDate";
	
	private static ListBox modeListBox  = new ListBox(false);

	private static final class DigitsOnlyKeyPressHandler implements
	KeyPressHandler {

		@Override
		public void onKeyPress(KeyPressEvent event) {
			TextBox sender = (TextBox) event.getSource();
			if (sender.isReadOnly() || !sender.isEnabled()) {
				return;
			}
			
			Character charCode = event.getCharCode();
			int unicodeCharCode = event.getUnicodeCharCode();
			// allow digits, and non-characters
			if (!(Character.isDigit(charCode) || (unicodeCharCode == 0) || (charCode == '.'))) {
				sender.cancelKey();
			}
		}
	}

	public static void showQDMAttributeDialogBox(XmlTreeDisplay xmlTreeDisplay,
			CellTreeNode cellTreeNode) {
		// If the CellTreeNode type isn't CellTreeNode.ELEMENT_REF_NODE then
		// return without doing anything.
		if (cellTreeNode.getNodeType() != CellTreeNode.ELEMENT_REF_NODE) {
			return;
		}
		unitNames.clear();
		attributeList.clear();
		
		String qdmName = PopulationWorkSpaceConstants.getElementLookUpName().get(
				cellTreeNode.getUUID());
		Node qdmNode = PopulationWorkSpaceConstants.getElementLookUpNode().get(
				qdmName + "~" + cellTreeNode.getUUID());
		// Could not find the qdm node in elemenentLookup tag
		if (qdmNode == null) {
			return;
		}
		String qdmDataType = qdmNode.getAttributes().getNamedItem(DATATYPE)
				.getNodeValue();
		boolean isOccuranceQDM = false;
		if (qdmNode.getAttributes().getNamedItem(INSTANCE) != null) {
			isOccuranceQDM = true;
		}
		
		// unitNames.addAll(getUnitNameList());
		unitNames.add("");
		unitNames.addAll(PopulationWorkSpaceConstants.units);
		
		List<String> mode = getModeList();
		if(qdmDataType.equalsIgnoreCase("Patient characteristic Birthdate") || qdmDataType.equalsIgnoreCase("Patient characteristic Expired")){
			Node oid = qdmNode.getAttributes().getNamedItem("oid");
			String  oidValue = oid.getNodeValue().trim();
			if(oidValue.equalsIgnoreCase(ConstantMessages.DEAD_OID) || oidValue.equalsIgnoreCase(ConstantMessages.BIRTHDATE_OID)){
				findAttributesForDataType(qdmDataType, isOccuranceQDM, mode,
						xmlTreeDisplay, cellTreeNode);
			}else{
				buildAndDisplayDialogBox(qdmDataType, mode,
						xmlTreeDisplay, cellTreeNode, true);
			}
		}else{
			findAttributesForDataType(qdmDataType, isOccuranceQDM, mode,
					xmlTreeDisplay, cellTreeNode);
		}
	}
	

	private static void buildAndDisplayDialogBox(final String qdmDataType,
			List<String> mode, final XmlTreeDisplay xmlTreeDisplay,
			final CellTreeNode cellTreeNode, boolean checkForRemovedDataType) {
		
		final DialogBox qdmAttributeDialogBox = new DialogBox(false, true);
		
		qdmAttributeDialogBox.getElement().setId("qdmAttributeDialog");
		qdmAttributeDialogBox.setGlassEnabled(true);
		qdmAttributeDialogBox.setAnimationEnabled(true);
		qdmAttributeDialogBox.setText(QDM_ATTRIBUTES_TITLE);
		
		
		// Create a table to layout the content
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("qdmAttributeDialog_hPanel");
		final VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("qdmAttributeDialog_dialogContents");
		dialogContents.setWidth("21em");
		dialogContents.setSpacing(5);
		qdmAttributeDialogBox.setWidget(dialogContents);
		
		final ListBox attributeListBox = new ListBox(false);
		attributeListBox.getElement().setId("qdmAttributeDialog_attributeListBox");
		attributeListBox.setVisibleItemCount(1);
		attributeListBox.setWidth("200px");
		attributeListBox.addItem("");
		
		for (String attribName : attributeList) {
			attributeListBox.addItem(attribName);
		}
		setToolTipForEachElementInListbox(attributeListBox);
		if(attributeListBox.getSelectedIndex() > -1){
			SelectElement selectElement = SelectElement.as(attributeListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			
			OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
			attributeListBox.setTitle(optionElement.getTitle());
		}
		hPanel.clear();
		dialogContents.add(hPanel);
		Label attributeLabel = (Label) LabelBuilder.buildLabel(attributeListBox, "Attribute");
		dialogContents.add(attributeLabel);
		dialogContents.setCellHorizontalAlignment(attributeLabel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.setCellHorizontalAlignment(attributeListBox, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(attributeListBox);
		
		
		//for ModeList in Attribute Workflow
		modeListBox.clear();
		modeListBox.getElement().setId("qdmAttributeDialog_modeListBox");
		modeListBox.setVisibleItemCount(1);
		modeListBox.setWidth("200px");
		modeListBox.addItem(QDMAttributeDialogBox.SELECT);
		@SuppressWarnings("unchecked")
		final List<CellTreeNode> attributeNodeList = (List<CellTreeNode>) cellTreeNode
				.getExtraInformation(ATTRIBUTES);
		final int rows = (attributeNodeList == null) ? 0 : attributeNodeList
				.size();
		String attributeName = "";
		if(rows > 0){
			CellTreeNode attributeNode = attributeNodeList.get(0);
			attributeName = (String)attributeNode.getExtraInformation(NAME);
		}
		List<String> modeList = JSONAttributeModeUtility.getAttrModeList(attributeName);
		modifyModeList(modeList);
		Label opearorLabel = (Label) LabelBuilder.buildLabel(attributeListBox, "Mode");
		dialogContents.add(opearorLabel);
		dialogContents.setCellHorizontalAlignment(opearorLabel, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.setCellHorizontalAlignment(modeListBox, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(modeListBox);
		
		final VerticalPanel dialogContents1 = new VerticalPanel();
		dialogContents1.getElement().setId("qdmAttributeDialog_dialogContents1");
		dialogContents.add(dialogContents1);
		if(rows > 0){
			CellTreeNode attributeNode = attributeNodeList.get(0);
			setExistingAttributeInPopup(attributeNode,attributeListBox,modeListBox,dialogContents1);
		}
		if(checkForRemovedDataType){
			attributeListBox.clear();
			attributeListBox.setEnabled(false);
			modeListBox.clear();
			modeListBox.setEnabled(false);
			unitsListBox.clear();
			unitsListBox.setEnabled(false);
			quantityTextBox.setValue("");
			quantityTextBox.setEnabled(false);
			qdmAttributeDate.setValue("");
			qdmAttributeDate.setEnabled(false);
			hPanel.clear();
			getWidget(hPanel,"Attributes may only be added to valid datatypes");
		}
		
		attributeListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				hPanel.clear();
				hPanel.removeStyleName("alertMessageDialogBox");
				attributeListBox.removeStyleName("gwt-TextBoxRed");
				if(attributeListBox.getSelectedIndex() > -1){
					SelectElement selectElement = SelectElement.as(attributeListBox.getElement());
					com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
							.getOptions();
					
					OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
					attributeListBox.setTitle(optionElement.getTitle());
				}
				if(attributeListBox.getSelectedIndex() == 0){
					dialogContents1.clear();
					modeListBox.setSelectedIndex(0);
					modeListBox.removeStyleName("gwt-TextBoxRed");
					if(modeListBox.getSelectedIndex() > -1){
						SelectElement selectElement = SelectElement.as(modeListBox.getElement());
						com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
								.getOptions();
						
						OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
						modeListBox.setTitle(optionElement.getTitle());
					}
					modeListBox.setEnabled(false);
				}
				else{
					SelectElement selectElement = SelectElement.as(attributeListBox.getElement());
					com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
							.getOptions();
					
					OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
					modifyModeList(JSONAttributeModeUtility.getAttrModeList(optionElement.getTitle()));
					modeListBox.setEnabled(true);
					qdmAttributeDate.setValue("");
					quantityTextBox.setValue("");
					//set Unit Names:
					unitsListBox.clear();
					for (String unitName : unitNames) {
						unitsListBox.addItem(unitName);
					}
					setEnabled(attributeListBox);
				}
			}
		});
		
		
		modeListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				hPanel.clear();
				hPanel.removeStyleName("alertMessageDialogBox");
				modeListBox.removeStyleName("gwt-TextBoxRed");
				if(modeListBox.getSelectedIndex() > -1){
					SelectElement selectElement = SelectElement.as(modeListBox.getElement());
					com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
							.getOptions();
					
					OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
					modeListBox.setTitle(optionElement.getTitle());
				}
				String selectedMode = modeListBox.getItemText(modeListBox.getSelectedIndex());
				if (QDMAttributeDialogBox.SELECT.equalsIgnoreCase(selectedMode)
						|| CHECK_IF_PRESENT.equalsIgnoreCase(selectedMode)) {
					dialogContents1.clear();
				} else if (VALUE_SET.equals(selectedMode)) {
					dialogContents1.clear();
					qdmListBox = createQdmListBox();
					qdmListBox.getElement().setId("valueSet_qdmListBox");
					qdmListBox.getElement().setTitle("valueSet_qdmListBox");
					if(qdmListBox.getSelectedIndex()>-1){
						setToolTipForEachElementInQdmListBox(qdmListBox);
						SelectElement selectElement = SelectElement.as(qdmListBox.getElement());
						com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
								.getOptions();
						OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
						qdmListBox.setTitle(optionElement.getTitle());
					}
					qdmListBox.addChangeHandler(new ChangeHandler() {
						
						@Override
						public void onChange(ChangeEvent event) {
							if(qdmListBox.getSelectedIndex()>-1){
								SelectElement selectElement = SelectElement.as(qdmListBox.getElement());
								com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
										.getOptions();
								
								OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
								qdmListBox.setTitle(optionElement.getTitle());
							}
						}
					});
					
					Label valueSet = (Label) LabelBuilder.buildLabel(qdmListBox, "Value Set");
					dialogContents1.add(valueSet);
					valueSet.getElement().setId("valueSet_Label");
					dialogContents1.setCellHorizontalAlignment(valueSet, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.setCellHorizontalAlignment(qdmListBox, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.add(qdmListBox);
				} else {
					dialogContents1.clear();
					//add Date Text Box in QDMAttributeListBox
					Label qdmAttributeDateLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Date");
					qdmAttributeDate.getElement().setId("qdmAttributeDialog_qdmAttributeDate");
					KeyDownHandler keyDownHandler = new KeyDownHandler() {
						
						@Override
						public void onKeyDown(KeyDownEvent event) {
							qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
							
						}
					};
					
					ClickHandler clickHandler = new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
							
						}
					};
					qdmAttributeDate.getDateBox().addKeyDownHandler(keyDownHandler);
					qdmAttributeDate.getCalendar().addClickHandler(clickHandler);
					qdmAttributeDate.setWidth("200px");
					qdmAttributeDate.setHeight("19");
					qdmAttributeDate.setValue("");
					dialogContents1.add(qdmAttributeDateLabel);
					dialogContents1.setCellHorizontalAlignment(qdmAttributeDateLabel, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.setCellHorizontalAlignment(qdmAttributeDate, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.add(qdmAttributeDate);
					
					//quantity CheckBox
					quantityTextBox = new TextBox();
					Label quantityLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Quantity");
					quantityTextBox.getElement().setId("qdmAttributeDialog_quantityTextBox");
					quantityTextBox.addKeyPressHandler(new DigitsOnlyKeyPressHandler());
					quantityTextBox.setWidth("200px");
					quantityTextBox.setHeight("19");
					dialogContents1.add(quantityLabel);
					dialogContents1.setCellHorizontalAlignment(quantityLabel, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.setCellHorizontalAlignment(quantityTextBox, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.add(quantityTextBox);
					
					quantityTextBox.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							quantityTextBox.removeStyleName("gwt-TextBoxRed");
							
						}
					});
					
					unitsListBox = new ListBox(false);
					unitsListBox.getElement().setId("qdmAttributeDialog_unitsListBox");
					unitsListBox.setVisibleItemCount(1);
					unitsListBox.setWidth("200px");
					
					Label unitsLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Units");
					dialogContents1.add(unitsLabel);
					dialogContents1.setCellHorizontalAlignment(unitsLabel, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.setCellHorizontalAlignment(unitsListBox, HasHorizontalAlignment.ALIGN_LEFT);
					dialogContents1.add(unitsListBox);
					
					for (String unitName : unitNames) {
						unitsListBox.addItem(unitName);
					}
					setToolTipForEachElementInListbox(unitsListBox);
					if(unitsListBox.getSelectedIndex() > -1){
						SelectElement selectElement = SelectElement.as(unitsListBox.getElement());
						com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
								.getOptions();
						
						OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
						unitsListBox.setTitle(optionElement.getTitle());
					}
					unitsListBox.addChangeHandler(new ChangeHandler() {
						
						@Override
						public void onChange(ChangeEvent event) {
							if(unitsListBox.getSelectedIndex() > -1){
								SelectElement selectElement = SelectElement.as(unitsListBox.getElement());
								com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
										.getOptions();
								
								OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
								unitsListBox.setTitle(optionElement.getTitle());
							}
						}
					});
					
				}
				setEnabled(attributeListBox);
			}
		});
		
		
		Button okButton = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (attributeListBox.getSelectedIndex() > 0) {
					if (!isValidMode(modeListBox)) {
						hPanel.clear();
						getWidget(hPanel , "Please select Mode");
					} else if (modeListBox.getItemText(modeListBox.getSelectedIndex()).equalsIgnoreCase("Value Set")) {
						if (qdmListBox.getSelectedIndex() == -1) {
							hPanel.clear();
							qdmListBox.setStyleName("gwt-TextBoxRed");
							getWidget(hPanel, "Please select Value Set");
						} else {
							saveToModel(xmlTreeDisplay, attributeListBox, modeListBox
									, qdmListBox, quantityTextBox, unitsListBox, qdmAttributeDate);
							xmlTreeDisplay.editNode(cellTreeNode.getName(), cellTreeNode.getName());
							xmlTreeDisplay.setDirty(true);
							qdmAttributeDialogBox.hide();
						}
					} //else if ((modeListBox.getSelectedIndex() < 7)  && (modeListBox.getSelectedIndex() >1)) {
					else if(!modeListBox.getItemText(modeListBox.getSelectedIndex()).equalsIgnoreCase("Value Set")
							&& !modeListBox.getItemText(modeListBox.getSelectedIndex()).equalsIgnoreCase("Check if Present")){
						/*String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());*/
						if (!isValidQuantity(quantityTextBox, attributeListBox)) {
							hPanel.clear();
							getWidget(hPanel, "Please enter Quantity");
						} else if (!isValidDate(qdmAttributeDate, attributeListBox)) {
							hPanel.clear();
							getWidget(hPanel, "Please enter Date");
						} else {
							saveToModel(xmlTreeDisplay, attributeListBox, modeListBox
									, qdmListBox, quantityTextBox, unitsListBox, qdmAttributeDate);
							xmlTreeDisplay.editNode(cellTreeNode.getName(), cellTreeNode.getName());
							xmlTreeDisplay.setDirty(true);
							qdmAttributeDialogBox.hide();
						}
					} else {
						saveToModel(xmlTreeDisplay, attributeListBox, modeListBox
								, qdmListBox, quantityTextBox, unitsListBox, qdmAttributeDate);
						xmlTreeDisplay.editNode(cellTreeNode.getName(), cellTreeNode.getName());
						xmlTreeDisplay.setDirty(true);
						qdmAttributeDialogBox.hide();
					}
				} else {
					List<CellTreeNode> attributeList = new ArrayList<CellTreeNode>();
					xmlTreeDisplay.getSelectedNode().setExtraInformation(ATTRIBUTES,
							attributeList);
					xmlTreeDisplay.editNode(cellTreeNode.getName(), cellTreeNode.getName());
					xmlTreeDisplay.setDirty(true);
					qdmAttributeDialogBox.hide();
				}
			}
		});
		okButton.getElement().setId("qdmAttributeDialog_okButton");
		
		Button cancelButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modeListBox.removeStyleName("gwt-TextBoxRed");
				qdmAttributeDialogBox.hide();
			}
		});
		cancelButton.getElement().setId("qdmAttributeDialog_cancelButton");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.getElement().setId("qdmAttributeDialog_buttonPanel");
		buttonPanel.setSpacing(10);
		buttonPanel.add(okButton);
		buttonPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(cancelButton);
		buttonPanel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_RIGHT);
		dialogContents.add(buttonPanel);
		qdmAttributeDialogBox.center();
	}
	
	private static void modifyModeList(List<String> modeList){
		modeListBox.clear();
		modeListBox.addItem(QDMAttributeDialogBox.SELECT);
		for (String modeName : modeList) {
			String modeValue = (modeName.startsWith("--")) ? modeName
					.substring(2).trim() : modeName;
					modeListBox.addItem(modeName, modeValue);
		}
		setToolTipForEachElementInListbox(modeListBox);
		if(modeListBox.getSelectedIndex() > -1){
			SelectElement selectElement = SelectElement.as(modeListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			
			OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
			modeListBox.setTitle(optionElement.getTitle());
		}
		modeListBox.setEnabled(false);
	}
	
	/**
	 * Save to model.
	 *
	 * @param xmlTreeDisplay            the xml tree display
	 * @param attributeListBox the attribute list box
	 * @param modeListBox the mode list box
	 * @param qdmListBox the qdm list box
	 * @param quantityTextBox the quantity text box
	 * @param unitsListBox the units list box
	 * @param qdmAttributeDate the qdm attribute date
	 */
	@SuppressWarnings("unchecked")
	protected static void saveToModel(XmlTreeDisplay xmlTreeDisplay,ListBox attributeListBox,ListBox modeListBox,ListBox qdmListBox,
			TextBox quantityTextBox,ListBox unitsListBox, DateBoxWithCalendar qdmAttributeDate) {
		List<CellTreeNode> attributeList = (List<CellTreeNode>) xmlTreeDisplay
				.getSelectedNode().getExtraInformation(ATTRIBUTES);
		if (attributeList == null) {
			attributeList = new ArrayList<CellTreeNode>();
		}
		attributeList.clear();
		
		CellTreeNode attributeNode = new CellTreeNodeImpl();
		attributeNode.setName(ATTRIBUTE);
		attributeNode.setNodeType(CellTreeNode.ATTRIBUTE_NODE);
		
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		attributeNode.setExtraInformation(NAME, attributeName);
		
		String modeName = modeListBox.getValue(modeListBox.getSelectedIndex());
		
		if (CHECK_IF_PRESENT.equals(modeName) || VALUE_SET.equals(modeName)) {
			attributeNode.setExtraInformation(MODE, modeName);
			if (VALUE_SET.equals(modeName)) {
				String uuid = qdmListBox.getValue(qdmListBox.getSelectedIndex());
				attributeNode.setExtraInformation(QDM_UUID, uuid);
			}
		} else if(attributeName.contains("date")){
			attributeNode.setExtraInformation(MODE, modeName);
			attributeNode.setExtraInformation(ATTRIBUTE_DATE,qdmAttributeDate.getValue());
			
		} else {
			attributeNode.setExtraInformation(MODE, modeName);
			attributeNode.setExtraInformation(COMPARISON_VALUE,quantityTextBox.getText());
			String unitName = unitsListBox.getItemText(unitsListBox.getSelectedIndex());
			if (unitName.trim().length() > 0) {
				attributeNode.setExtraInformation(UNIT, unitName);
			}
		}
		attributeList.add(attributeNode);
		xmlTreeDisplay.getSelectedNode().setExtraInformation(ATTRIBUTES,
				attributeList);
		
	}
	
	/**
	 * Sets the existing attribute in popup.
	 *
	 * @param attributeNode the attribute node
	 * @param attributeListBox the attribute list box
	 * @param modeListBox the mode list box
	 * @param dialogContents1 the dialog contents1
	 */
	private static void setExistingAttributeInPopup(CellTreeNode  attributeNode,ListBox attributeListBox,ListBox modeListBox,
			VerticalPanel dialogContents1){
		if (attributeNode == null) {
			return;
		}
		
		// Set the attribute name
		String attributeName = (String) attributeNode.getExtraInformation(NAME);
		attributeListBox.setEnabled(true);
		setEnabled(attributeListBox);
		for (int j = 0; j < attributeListBox.getItemCount(); j++) {
			if (attributeListBox.getItemText(j).equals(attributeName)) {
				attributeListBox.setSelectedIndex(j);
				break;
			}
		}
		
		if(attributeListBox.getSelectedIndex() > -1){
			SelectElement selectElement = SelectElement.as(attributeListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			
			OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
			attributeListBox.setTitle(optionElement.getTitle());
		}
		// Set the mode name
		String modeName = (String) attributeNode.getExtraInformation(MODE);
		modeListBox.setEnabled(true);
		setEnabled(attributeListBox);
		for (int j = 0; j < modeListBox.getItemCount(); j++) {
			if (modeListBox.getItemText(j).equalsIgnoreCase(modeName)) {
				modeListBox.setSelectedIndex(j);
				break;
			}
		}
		if(modeListBox.getSelectedIndex() > -1){
			SelectElement selectElement = SelectElement.as(modeListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			
			OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
			modeListBox.setTitle(optionElement.getTitle());
		}
		if (!CHECK_IF_PRESENT.equalsIgnoreCase(modeName)) {
			if (VALUE_SET.equalsIgnoreCase(modeName)) {
				qdmListBox = createQdmListBox();
				
				String qdmId = (String) attributeNode
						.getExtraInformation(QDM_UUID);
				if (PopulationWorkSpaceConstants.getElementLookUpName().containsKey(qdmId)) {
					for (int r = 0; r < qdmListBox.getItemCount(); r++) {
						if (qdmId.equals(qdmListBox.getValue(r))) {
							qdmListBox.setSelectedIndex(r);
							break;
						}
					}
				}
				if(qdmListBox.getSelectedIndex() > -1){
					setToolTipForEachElementInQdmListBox(qdmListBox);
					SelectElement selectElement = SelectElement.as(qdmListBox.getElement());
					com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
							.getOptions();
					OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
					qdmListBox.setTitle(optionElement.getTitle());
				}
				qdmListBox.addChangeHandler(new ChangeHandler() {
					
					@Override
					public void onChange(ChangeEvent event) {
						if(qdmListBox.getSelectedIndex() > -1){
							SelectElement selectElement = SelectElement.as(qdmListBox.getElement());
							com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
									.getOptions();
							
							OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
							qdmListBox.setTitle(optionElement.getTitle());
						}
					}
				});
				dialogContents1.clear();
				Label valueSet = (Label) LabelBuilder.buildLabel(qdmListBox, "Value Set");
				dialogContents1.add(valueSet);
				dialogContents1.setCellHorizontalAlignment(valueSet, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.setCellHorizontalAlignment(qdmListBox, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.add(qdmListBox);
				qdmListBox.getElement().setId("qdmListBox_ListBox");
			}
			// If this is a Comparison operator
			else {
				dialogContents1.clear();
				
				//add Date Text Box in QDMAttributeListBox
				Label qdmAttributeDateLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Date");
				qdmAttributeDate.getElement().setId("qdmAttributeDialog_qdmAttributeDate");
				KeyDownHandler keyDownHandler = new KeyDownHandler() {
					
					@Override
					public void onKeyDown(KeyDownEvent event) {
						qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
					}
				};
				
				ClickHandler clickHandler = new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
					}
				};
				qdmAttributeDate.getDateBox().addKeyDownHandler(keyDownHandler);
				qdmAttributeDate.getCalendar().addClickHandler(clickHandler);
				qdmAttributeDate.setWidth("200px");
				qdmAttributeDate.setHeight("19");
				qdmAttributeDate.setValue((String) attributeNode
						.getExtraInformation(ATTRIBUTE_DATE));
				dialogContents1.add(qdmAttributeDateLabel);
				dialogContents1.setCellHorizontalAlignment(qdmAttributeDateLabel, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.setCellHorizontalAlignment(qdmAttributeDate, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.add(qdmAttributeDate);
				
				//quantity TextBox
				Label quantityLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Quantity");
				quantityTextBox  = new TextBox();
				quantityTextBox.getElement().setId("qdmAttributeDialog_quantityTextBox");
				quantityTextBox.addKeyPressHandler(new DigitsOnlyKeyPressHandler());
				quantityTextBox.setWidth("200px");
				quantityTextBox.setHeight("19");
				quantityTextBox.setValue((String) attributeNode
						.getExtraInformation(COMPARISON_VALUE));
				dialogContents1.add(quantityLabel);
				dialogContents1.setCellHorizontalAlignment(quantityLabel, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.setCellHorizontalAlignment(quantityTextBox, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.add(quantityTextBox);
				
				quantityTextBox.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						quantityTextBox.removeStyleName("gwt-TextBoxRed");
						
					}
				});
				
				Label unitsLabel = (Label) LabelBuilder.buildLabel(quantityTextBox, "Units");
				unitsListBox = new ListBox(false);
				unitsListBox.getElement().setId("qdmAttributeDialog_unitsListBox");
				unitsListBox.setVisibleItemCount(1);
				unitsListBox.setWidth("200px");
				
				for (String unitName : unitNames) {
					unitsListBox.addItem(unitName);
				}
				//setToolTipForEachElementInListbox(units);
				String unit = (String) attributeNode.getExtraInformation(UNIT);
				for (int g = 0; g < unitsListBox.getItemCount(); g++) {
					if (unitsListBox.getItemText(g).equals(unit)) {
						unitsListBox.setSelectedIndex(g);
						break;
					}
				}
				
				setToolTipForEachElementInListbox(unitsListBox);
				if(unitsListBox.getSelectedIndex() > -1){
					SelectElement selectElement = SelectElement.as(unitsListBox.getElement());
					com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
							.getOptions();
					
					OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
					unitsListBox.setTitle(optionElement.getTitle());
				}
				unitsListBox.addChangeHandler(new ChangeHandler() {
					
					@Override
					public void onChange(ChangeEvent event) {
						if(unitsListBox.getSelectedIndex() > -1){
							SelectElement selectElement = SelectElement.as(unitsListBox.getElement());
							com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
									.getOptions();
							
							OptionElement optionElement = options.getItem(selectElement.getSelectedIndex());
							unitsListBox.setTitle(optionElement.getTitle());
						}
					}
				});
				
				dialogContents1.add(unitsLabel);
				dialogContents1.setCellHorizontalAlignment(unitsLabel, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.setCellHorizontalAlignment(unitsListBox, HasHorizontalAlignment.ALIGN_LEFT);
				dialogContents1.add(unitsListBox);
			}
		}
		setEnabled(attributeListBox);
	}
	
	/**
	 * Creates the qdm list box.
	 * 
	 * @return the list box
	 */
	private static ListBox createQdmListBox() {
		ListBox qdmListBox = new ListBox();
		qdmListBox.setVisibleItemCount(1);
		qdmListBox.setWidth("15em");
		for (Entry<String, Node> qdm : PopulationWorkSpaceConstants.getElementLookUpNode()
				.entrySet()) {
			Node qdmNode = qdm.getValue();
			String dataType = qdmNode.getAttributes().getNamedItem(DATATYPE)
					.getNodeValue();
			String uuid = qdmNode.getAttributes().getNamedItem(UUID)
					.getNodeValue();
			if (ATTRIBUTE.equals(dataType)) {
				qdmListBox.addItem(
						PopulationWorkSpaceConstants.getElementLookUpName().get(uuid), uuid);
			}
		}
		return qdmListBox;
	}
	
	/**
	 * Gets the mode list.
	 * 
	 * @return the mode list
	 */
	private static List<String> getModeList() {
		List<String> modeList = new ArrayList<String>();
		
		modeList.add(CHECK_IF_PRESENT);
		modeList.add(VALUE_SET);
		modeList.add(LESS_THAN);
		modeList.add(GREATER_THAN);
		modeList.add(EQUAL_TO);
		modeList.add(LESS_THAN_OR_EQUAL_TO);
		modeList.add(GREATER_THAN_OR_EQUAL_TO);
		return modeList;
	}
	
	/**
	 * This method will check all the QDM elements in ElementLookup node and
	 * return the names of QDM elements of datatype 'attribute'.
	 * 
	 * @param dataType
	 *            the data type
	 * @param isOccuranceQDM
	 *            the is occurance qdm
	 * @param mode2
	 *            the mode2
	 * @param xmlTreeDisplay
	 *            the xml tree display
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	/*
	 * private static List<String> getQDMElementNames(){ List<String>
	 * qdmNameList = new ArrayList<String>(); Set<String> qdmNames =
	 * ClauseConstants.getElementLookUpNode().keySet(); for(String
	 * qdmName:qdmNames){ com.google.gwt.xml.client.Node qdmNode =
	 * ClauseConstants.getElementLookUpNode().get(qdmName); String dataType =
	 * qdmNode.getAttributes().getNamedItem(DATATYPE).getNodeValue();
	 * if(ATTRIBUTE.equals(dataType)){ String uuid =
	 * qdmName.substring(qdmName.lastIndexOf("~") + 1);
	 * qdmNameList.add(ClauseConstants.getElementLookUpName().get(uuid)); } }
	 * return qdmNameList; }
	 */
	
	private static void findAttributesForDataType(final String dataType,
			final boolean isOccuranceQDM, final List<String> mode2,
			final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		attributeService.checkIfQDMDataTypeIsPresent(dataType, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					
					attributeService.getAllAttributesByDataType(dataType,
							new AsyncCallback<List<QDSAttributes>>() {
						
						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							System.out
							.println("Error retrieving data type attributes. "
									+ caught.getMessage());
						}
						
						@Override
						public void onSuccess(List<QDSAttributes> result) {
							for (QDSAttributes qdsAttributes : result) {
								if (isOccuranceQDM
										&& NEGATION_RATIONALE.equals(qdsAttributes
												.getName())) {
									continue;
								}
								attributeList.add(qdsAttributes.getName());
							}
							buildAndDisplayDialogBox(dataType, mode2,
									xmlTreeDisplay, cellTreeNode, false);
						}
					});
				} else {
					buildAndDisplayDialogBox(dataType, mode2,
							xmlTreeDisplay, cellTreeNode, true);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private static void setToolTipForEachElementInListbox(ListBox listBox) {
		// Set tooltips for each element in listbox
		SelectElement selectElement = SelectElement.as(listBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
				.getOptions();
		for (int i = 0; i < options.getLength(); i++) {
			OptionElement optionElement = options.getItem(i);
			optionElement.setTitle(optionElement.getInnerText());
		}
	}
	

	private static void setToolTipForEachElementInQdmListBox(ListBox listBox) {
		SelectElement selectElement = SelectElement.as(listBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
				.getOptions();
		for (int i = 0; i < options.getLength(); i++) {
			String text = options.getItem(i).getText();
			String uuid = options.getItem(i).getAttribute("value");
			String oid = "";
			String title = text;
			if (PopulationWorkSpaceConstants.getElementLookUpNode()
					.get(text + "~" + uuid) != null) {
				oid = PopulationWorkSpaceConstants.getElementLookUpNode()
						.get(text + "~" + uuid).getAttributes().getNamedItem("oid")
						.getNodeValue();
				title = title.concat(" (" + oid + ")");
			}
			OptionElement optionElement = options.getItem(i);
			optionElement.setTitle(title);
		}
	}
	
	
	/**
	 * Checks if is valid mode.
	 *
	 * @param modeListBox the mode list box
	 * @return true, if is valid mode
	 */
	private static boolean isValidMode(ListBox modeListBox){
		boolean isValid = true;
		if(modeListBox.getSelectedIndex() == 0){
			modeListBox.setStyleName("gwt-TextBoxRed");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Checks if is valid quantity.
	 *
	 * @param quantityTextBox the quantity text box
	 * @param attributeListBox the attribute list box
	 * @return true, if is valid quantity
	 */
	private static boolean isValidQuantity(TextBox quantityTextBox,
			ListBox attributeListBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		boolean isValid = true;
		if(quantityTextBox.isEnabled() && quantityTextBox.getValue().equals("") &&
				!attributeName.contains("date")){
			quantityTextBox.setStyleName("gwt-TextBoxRed");
			qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Checks if is valid date.
	 *
	 * @param qdmAttributeDate the qdm attribute date
	 * @param attributeListBox the attribute list box
	 * @return true, if is valid date
	 */
	private static boolean isValidDate(DateBoxWithCalendar qdmAttributeDate,
			ListBox attributeListBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		boolean isValid = true;
		if(qdmAttributeDate.getValue().equals("") &&
				attributeName.contains("date")){
			qdmAttributeDate.setStyleName("gwt-TextBoxRed");
			quantityTextBox.removeStyleName("gwt-TextBoxRed");
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Sets the focus.
	 *
	 * @param hPanel the new focus
	 */
	private static void setFocus(HorizontalPanel hPanel){
		try{
			hPanel.getElement().focus();
			hPanel.getElement().setAttribute("id", "ErrorMessage");
			hPanel.getElement().setAttribute("aria-role", "image");
			hPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
			hPanel.getElement().setAttribute("aria-live", "assertive");
			hPanel.getElement().setAttribute("aria-atomic", "true");
			hPanel.getElement().setAttribute("aria-relevant", "all");
			hPanel.getElement().setAttribute("role", "alert");
		}catch(JavaScriptException e){
			//This try/catch block is needed for IE7 since it is throwing exception "cannot move
			//focus to the invisible control."
			//do nothing.
		}
	}
	
	
	/**
	 * Gets the widget.
	 *
	 * @param hPanel the h panel
	 * @param errorMessage the error message
	 * @return the widget
	 */
	private static Widget getWidget(HorizontalPanel hPanel,String errorMessage){
		hPanel.clear();
		FlowPanel imagePanel = new FlowPanel();
		FlowPanel msgPanel = new FlowPanel();
		Image errorIcon = new Image(ImageResources.INSTANCE.msg_error());
		Label label = new Label(errorMessage);
		errorIcon.getElement().setAttribute("alt", "ErrorMessage");
		imagePanel.getElement().setId("imagePanel_FlowPanel");
		imagePanel.setTitle("Error");
		imagePanel.add(errorIcon);
		msgPanel.getElement().setId("msgPanel_FlowPanel");
		msgPanel.add(label);
		hPanel.clear();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		hPanel.setStyleName("alertMessageDialogBox");
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		setFocus(hPanel);
		return hPanel;
	}
	
	/**
	 * Sets the enabled.
	 *
	 * @param attributeListBox the new enabled
	 */
	private static void setEnabled(ListBox attributeListBox){
		String attributeName = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
		qdmAttributeDate.removeStyleName("gwt-TextBoxRed");
		quantityTextBox.removeStyleName("gwt-TextBoxRed");
		if(attributeName.contains("date")){
			qdmAttributeDate.setEnabled(true);
			unitsListBox.setEnabled(false);
			quantityTextBox.setEnabled(false);
		} else {
			qdmAttributeDate.setEnabled(false);
			unitsListBox.setEnabled(true);
			quantityTextBox.setEnabled(true);
		}
	}
	
	
}