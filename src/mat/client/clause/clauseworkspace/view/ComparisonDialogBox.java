package mat.client.clause.clauseworkspace.view;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mat.client.ImageResources;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
// TODO: Auto-generated Javadoc
//import com.google.gwt.user.client.DOM;


// TODO: Auto-generated Javadoc
/**
 * The Class ComparisonDialogBox.
 */
public class ComparisonDialogBox {
	
	/** The dialog box. */
	public static DialogBox dialogBox = new DialogBox(true,true);
	
	
	/**
	 * Show comparison dialog box.
	 * 
	 * @param xmlTreeDisplay
	 *            the xml tree display
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	public static void showComparisonDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit");
		dialogBox.setTitle("Edit");
		dialogBox.getElement().setAttribute("id", "ComparisonDialogBox");
		final HorizontalPanel hPanel = new HorizontalPanel();
		final VerticalPanel dialogContents = new VerticalPanel();
		// Create a table to layout the content
		dialogContents.clear();
		dialogContents.setWidth("21em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		//- POC to change width and height for dialogBox. - Success
		//dialogContents.setWidth("50em");
		/*DOM.setStyleAttribute(dialogBox.getElement(), "width", "950px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "950px");
		DOM.setStyleAttribute(dialogBox.getElement(), "top", "155px");*/
		dialogBox.setWidget(dialogContents);
		@SuppressWarnings("unchecked")
		HashMap<String, String> extraAttributesMap = (HashMap<String, String>) cellTreeNode
		.getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
		
		final ListBoxMVP listAllTimeOrFunction = new ListBoxMVP();
		String timingOrFuncMethod = "--Select--";
		String operatorMethod = "--Select--";
		String quantityValue = "";
		String unitType = "--Select--";
		if (extraAttributesMap != null) {
			timingOrFuncMethod =  extraAttributesMap.get(PopulationWorkSpaceConstants.TYPE);
			timingOrFuncMethod =  MatContext.get().operatorMapKeyShort.containsKey(timingOrFuncMethod)
					? MatContext.get().operatorMapKeyShort.get(timingOrFuncMethod) : "--Select--";
					operatorMethod = extraAttributesMap.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)
							? extraAttributesMap.get(PopulationWorkSpaceConstants.OPERATOR_TYPE) : operatorMethod;
							quantityValue = extraAttributesMap.containsKey(PopulationWorkSpaceConstants.QUANTITY)
									? extraAttributesMap.get(PopulationWorkSpaceConstants.QUANTITY) : quantityValue;
									unitType = extraAttributesMap.containsKey(PopulationWorkSpaceConstants.UNIT)
											? extraAttributesMap.get(PopulationWorkSpaceConstants.UNIT) : unitType;
											if (operatorMethod.trim().length() == 0) {
												operatorMethod = "--Select--";
											}
											if (unitType.trim().length() == 0) {
												unitType = "--Select--";
											}
		} else {
			timingOrFuncMethod = cellTreeNode.getLabel();
		}
		String labelForListBox = null;
		List<String> keys = null;
		//List for Timing or Function based on Node Type.
		if (cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE) {
			keys = MatContext.get().timings;
			labelForListBox = "Timing";
		} else {
			keys = MatContext.get().functions;
			labelForListBox = "Functions";
		}
		
		if (timingOrFuncMethod.contains("Select")) { //added is null check for Functions which are not in recent list but exists in prod like COUNTDISTINCT
			listAllTimeOrFunction.addItem(timingOrFuncMethod);
		}
		
		for (int i = 0; i < keys.size(); i++) {
			listAllTimeOrFunction.addItem(keys.get(i));
			if (keys.get(i).equalsIgnoreCase(timingOrFuncMethod)) {
				listAllTimeOrFunction.setSelectedIndex(i);
			}
		}
		
		listAllTimeOrFunction.setWidth("150px");
		hPanel.clear();
		dialogContents.add(hPanel);
		Label lableListBoxTimingOrFunction = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, labelForListBox);
		dialogContents.add(lableListBoxTimingOrFunction);
		dialogContents.setCellHorizontalAlignment(lableListBoxTimingOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllTimeOrFunction);
		dialogContents.setCellHorizontalAlignment(listAllTimeOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		
		//List of Operators.
		final ListBoxMVP listAllOperator = new ListBoxMVP();
		// listAllOperator.addItem("--Select--");
		List<String> comparisonOpKeys = MatContext.get().comparisonOps;
		if (operatorMethod.contains("Select")) {
			listAllOperator.addItem(operatorMethod);
		} else {
			listAllOperator.addItem("--Select--");
		}
		for (int i = 0; i < comparisonOpKeys.size(); i++) {
			listAllOperator.addItem(comparisonOpKeys.get(i));
			if (comparisonOpKeys.get(i).equalsIgnoreCase(operatorMethod)) {
				listAllOperator.setSelectedIndex(i+1);
			}
		}
		listAllOperator.setWidth("150px");
		Label lableOperator = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Operators");
		dialogContents.add(lableOperator);
		dialogContents.setCellHorizontalAlignment(lableOperator, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllOperator);
		dialogContents.setCellHorizontalAlignment(listAllOperator, HasHorizontalAlignment.ALIGN_LEFT);
		
		//Qunatity Text Box.
		final TextBox quantity = new TextBox();
		quantity.getElement().setId("quantity_TextBox");
		if (quantityValue != null) {
			quantity.setValue(quantityValue);
		}
		quantity.setWidth("150px");
		//validation to check only numeric values are added into Quantity.
		addHandlerToQuantityTextBox(quantity);
		Label lableQuantity = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Quantity");
		dialogContents.add(lableQuantity);
		dialogContents.setCellHorizontalAlignment(lableQuantity, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(quantity);
		dialogContents.setCellHorizontalAlignment(quantity, HasHorizontalAlignment.ALIGN_LEFT);
		
		
		//List of Units.
		final ListBoxMVP listAllUnits = new ListBoxMVP();
		//		if (unitType.contains("Select"))
		//			listAllUnits.addItem(unitType);
		listAllUnits.addItem("--Select--");
		if (cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE) {
			//Show list starting from seconds till Year for Timing. Since list is retrieved in sorted order, Year comes at 7th index.
			for (int i = 0; i < 7; i++) {
				listAllUnits.addItem(PopulationWorkSpaceConstants.units.get(i));
				if ((PopulationWorkSpaceConstants.units.get(i)).equalsIgnoreCase(unitType)) {
					listAllUnits.setSelectedIndex(i+1);
				}
			}
		} else {
			for (int i = 0; i < PopulationWorkSpaceConstants.units.size(); i++) {
				listAllUnits.addItem(PopulationWorkSpaceConstants.units.get(i));
				if ((PopulationWorkSpaceConstants.units.get(i)).equalsIgnoreCase(unitType)) {
					listAllUnits.setSelectedIndex(i+1);
				}
			}
			
		}
		listAllUnits.setWidth("150px");
		Label lableUnits = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Units");
		dialogContents.add(lableUnits);
		dialogContents.setCellHorizontalAlignment(lableUnits, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllUnits);
		dialogContents.setCellHorizontalAlignment(listAllUnits, HasHorizontalAlignment.ALIGN_LEFT);
		
		if (operatorMethod.contains("Select")){
			quantity.setEnabled(false);
			listAllUnits.setEnabled(false);
		}
		else{
			quantity.setEnabled(true);
			listAllUnits.setEnabled(true);
		}
		
		
		//changeHandler for listAllOperator
		listAllOperator.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				hPanel.clear();
				if (listAllOperator.getValue().contains("Select")){
					dialogContents.remove(0);
					quantity.removeStyleName("gwt-TextBoxRed");
					quantity.setEnabled(false);
					listAllUnits.setEnabled(false);
				}
				else{
					quantity.setEnabled(true);
					listAllUnits.setEnabled(true);
				}
				updateQuantityandUnits(quantity,listAllUnits);
			}
		});
		
		quantity.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				quantity.removeStyleName("gwt-TextBoxRed");
				
			}
		});
		
		// Add a Save button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hPanel.clear();
				if(validateQuantity(listAllOperator.getValue(),quantity,listAllUnits)){
					dialogContents.clear();
					dialogBox.hide();
					saveAttributesToNode(listAllTimeOrFunction.getValue(),
							listAllOperator.getValue(), quantity.getValue(), listAllUnits.getValue(), xmlTreeDisplay);
					xmlTreeDisplay.setDirty(true);
				}
				else{
					hPanel.clear();
					getWidget(hPanel);
				}
			}
		});
		save.getElement().setId("save_Button");
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogContents.clear();
				dialogBox.hide();
				
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(save);
		buttonPanel.setCellHorizontalAlignment(save, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(closeButton);
		buttonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.getElement().setId("buttonPanel_HorizontalPanel");
		dialogContents.add(buttonPanel);
		dialogBox.center();
		dialogBox.show();
		
	}
	
	/**
	 * Validate quantity.
	 *
	 * @param operator the operator
	 * @param quantity the quantity
	 * @param listAllUnits the list all units
	 * @return true, if successful
	 */
	private static boolean validateQuantity(final String operator,final TextBox quantity,final ListBoxMVP listAllUnits){
		boolean isValid = true;
		if(operator.contains("Select")){
			quantity.setEnabled(false);
			listAllUnits.setEnabled(false);
			isValid = true;
		} else {
			if(quantity.getValue().equals("") ){
				quantity.setStyleName("gwt-TextBoxRed");
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * Method to set attributes to CellTreeNode extraInformation.
	 * 
	 * @param functionOrTiming
	 *            the function or timing
	 * @param operator
	 *            the operator
	 * @param quantity
	 *            the quantity
	 * @param unit
	 *            the unit
	 * @param xmlTreeDisplay
	 *            the xml tree display
	 */
	private static void saveAttributesToNode(String functionOrTiming, String operator,
			String quantity, String unit, XmlTreeDisplay xmlTreeDisplay) {
		Map<String, String> extraAttributes = new HashMap<String, String>();
		if (!operator.contains("Select")) {
			extraAttributes.put(PopulationWorkSpaceConstants.OPERATOR_TYPE, operator);
		} else {
			operator = "";
		}
		if (!unit.contains("Select")) {
			extraAttributes.put(PopulationWorkSpaceConstants.UNIT, unit);
		} else {
			unit = "";
		}
		if (!functionOrTiming.contains("Select")) {
			extraAttributes.put(PopulationWorkSpaceConstants.TYPE, MatContext.get().operatorMapKeyLong.get(functionOrTiming));
			StringBuilder displayName = new StringBuilder();
			if (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.TIMING_NODE) {
				//				String operatorType = ClauseConstants.getComparisonOperatorMap().containsKey(operator) ? ClauseConstants.getComparisonOperatorMap().get(operator) : " ";
				String operatorType = MatContext.get().operatorMapKeyLong.containsKey(operator)
						? MatContext.get().operatorMapKeyLong.get(operator) : " ";
						//				StringBuilder operatorTypeKey = new StringBuilder(operatorType);
						if(operator.equals("")){
							displayName.append(functionOrTiming);
							//extraAttributes.remove("quantity");
						} else {
							displayName.append(operatorType).append(" ").append(quantity).append(" ")
							.append(unit).append(" ").append(functionOrTiming);
						}
			} else if (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE) {
				String operatorType = MatContext.get().operatorMapKeyLong.containsKey(operator)
						? MatContext.get().operatorMapKeyLong.get(operator) : " ";
						if(operator.equals("")){
							displayName.append(functionOrTiming);
							//extraAttributes.remove("quantity");
						}
						else{
							displayName.append(functionOrTiming).append(" ").append(operatorType).append(" ")
							.append(quantity).append(" ").append(unit);
						}
			}
			extraAttributes.put(PopulationWorkSpaceConstants.DISPLAY_NAME, displayName.toString());
			xmlTreeDisplay.editNode(displayName.toString(), displayName.toString());
		}
		
		if(!operator.equals("")){
			extraAttributes.put(PopulationWorkSpaceConstants.QUANTITY, quantity);
		}
		xmlTreeDisplay.getSelectedNode().setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, extraAttributes);
	}
	
	/**
	 * TextBox allow only Digits.
	 * 
	 * @param quantity
	 *            the quantity
	 */
	private static void addHandlerToQuantityTextBox(TextBox quantity) {
		
		quantity.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode())
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB)
						&& (event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE)) {
					((TextBox) event.getSource()).cancelKey();
				}
				
			} });
	}
	
	/**
	 * Update quantityand units.
	 *
	 * @param quantity the quantity
	 * @param listAllUnits the list all units
	 */
	private static void updateQuantityandUnits(TextBox quantity,ListBoxMVP listAllUnits){
		String quantityValue ="";
		quantity.setValue(quantityValue);
		listAllUnits.setSelectedIndex(0);
	}
	
	/**
	 * Sets the focus.
	 *
	 * @param hPanel the new focus
	 */
	public static void setFocus(HorizontalPanel hPanel){
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
	 * @return the widget
	 */
	public static Widget getWidget(HorizontalPanel hPanel){
		hPanel.clear();
		FlowPanel imagePanel = new FlowPanel();
		FlowPanel msgPanel = new FlowPanel();
		Image errorIcon = new Image(ImageResources.INSTANCE.msg_error());
		Label label = new Label(MatContext.get()
				.getMessageDelegate().getComparisonDiloagBoxErrorDisplay());
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
	
}
