package mat.client.clause.clauseworkspace.view;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

//import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ComparisonDialogBox{

	public static DialogBox dialogBox = new DialogBox(true,true);


	public static void showComparisonDialogBox(final XmlTreeDisplay xmlTreeDisplay, final CellTreeNode cellTreeNode) {

		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setText("Edit");
		dialogBox.setTitle("Edit");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("20em");
		dialogContents.setSpacing(5);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		//- POC to change width and height for dialogBox. - Success 
		//dialogContents.setWidth("50em");
		/*DOM.setStyleAttribute(dialogBox.getElement(), "width", "950px");
		DOM.setStyleAttribute(dialogBox.getElement(), "height", "950px");
		DOM.setStyleAttribute(dialogBox.getElement(), "top", "155px");*/
		dialogBox.setWidget(dialogContents);
		@SuppressWarnings("unchecked")
		HashMap<String,String> extraAttributesMap = (HashMap<String, String>) cellTreeNode.getExtraInformation(ClauseConstants.EXTRA_ATTRIBUTES);

		final ListBoxMVP listAllTimeOrFunction = new ListBoxMVP();
		String timingOrFuncMethod ="--Select--";
		String operatorMethod="--Select--";
		String quantityValue ="";
		String unitType ="--Select--";

		if(extraAttributesMap!=null){
			timingOrFuncMethod =  extraAttributesMap.get(ClauseConstants.TYPE);
			timingOrFuncMethod =  MatContext.get().operatorMapKeyShort.containsKey(timingOrFuncMethod) 
					? MatContext.get().operatorMapKeyShort.get(timingOrFuncMethod) : "--Select--";
			operatorMethod = extraAttributesMap.containsKey(ClauseConstants.OPERATOR_TYPE) ? extraAttributesMap.get(ClauseConstants.OPERATOR_TYPE) : operatorMethod;
			quantityValue = extraAttributesMap.containsKey(ClauseConstants.QUANTITY) ? extraAttributesMap.get(ClauseConstants.QUANTITY) : quantityValue;
			unitType = extraAttributesMap.containsKey(ClauseConstants.UNIT) ? extraAttributesMap.get(ClauseConstants.UNIT) : unitType;
			if(operatorMethod.trim().length() == 0){
				operatorMethod = "--Select--";
			}
			if(unitType.trim().length() == 0){
				unitType = "--Select--";
			}
		}else{
			timingOrFuncMethod = cellTreeNode.getLabel();
		}
		String labelForListBox = null;
		List<String> keys = null;
		//List for Timing or Function based on Node Type.
		if(cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE){
			keys = MatContext.get().timings;
			labelForListBox = "Timing";
		}else{
			keys = MatContext.get().functions;
			labelForListBox = "Functions";
		}
		
		if(timingOrFuncMethod.contains("Select")){ //added is null check for Functions which are not in recent list but exists in prod like COUNTDISTINCT 
			listAllTimeOrFunction.addItem(timingOrFuncMethod);
		}
			
		for(int i=0; i < keys.size(); i++){
			listAllTimeOrFunction.addItem(keys.get(i));
			if(keys.get(i).equalsIgnoreCase(timingOrFuncMethod)){
				listAllTimeOrFunction.setSelectedIndex(i);
			}
		}
		listAllTimeOrFunction.setWidth("150px");
		
		Label lableListBoxTimingOrFunction = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, labelForListBox);
		dialogContents.add(lableListBoxTimingOrFunction);
		dialogContents.setCellHorizontalAlignment(lableListBoxTimingOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllTimeOrFunction);
		dialogContents.setCellHorizontalAlignment(listAllTimeOrFunction, HasHorizontalAlignment.ALIGN_LEFT);

		//List of Operators.
		final ListBoxMVP listAllOperator = new ListBoxMVP();
		// listAllOperator.addItem("--Select--");
		List<String> comparisonOpKeys = MatContext.get().comparisonOps;
		if(operatorMethod.contains("Select"))
			listAllOperator.addItem(operatorMethod);
		for(int i=0; i < comparisonOpKeys.size(); i++){
			listAllOperator.addItem(comparisonOpKeys.get(i));
			if(comparisonOpKeys.get(i).equalsIgnoreCase(operatorMethod)){
				listAllOperator.setSelectedIndex(i);
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
		if(quantityValue!=null){
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
		if(unitType.contains("Select"))
			listAllUnits.addItem(unitType);
		if(cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE){
			//Show list starting from seconds till Year for Timing. Since list is reterived in sorted order, Year comes at 7th index.
			for(int i=0;i<7;i++){
				listAllUnits.addItem(ClauseConstants.units.get(i));
				if((ClauseConstants.units.get(i)).equalsIgnoreCase(unitType)){
					listAllUnits.setSelectedIndex(i);
				}
			}
		}else{
			for(int i=0;i<ClauseConstants.units.size();i++){
				listAllUnits.addItem(ClauseConstants.units.get(i));
				if((ClauseConstants.units.get(i)).equalsIgnoreCase(unitType)){
					listAllUnits.setSelectedIndex(i);
				}
			}

		}
		listAllUnits.setWidth("150px");
		Label lableUnits = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Units");;
		dialogContents.add(lableUnits);
		dialogContents.setCellHorizontalAlignment(lableUnits, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllUnits);
		dialogContents.setCellHorizontalAlignment(listAllUnits, HasHorizontalAlignment.ALIGN_LEFT);
		// Add a Save button at the bottom of the dialog
		Button save = new Button("OK", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				saveAttributesToNode(listAllTimeOrFunction.getValue(), listAllOperator.getValue(),quantity.getValue(),listAllUnits.getValue(),xmlTreeDisplay);
				xmlTreeDisplay.setDirty(true);
			}
		});
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();		
			}
		});
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
	 * Method to set attributes to CellTreeNode extraInformation.
	 * */ 
	private static void saveAttributesToNode(String functionOrTiming, String operator, String quantity, String unit, XmlTreeDisplay xmlTreeDisplay){
		Map<String,String> extraAttributes = new HashMap<String,String>();
		if(!operator.contains("Select")){
			extraAttributes.put(ClauseConstants.OPERATOR_TYPE, operator);
		}else{
			operator="";
		}
		if(!unit.contains("Select")){
			extraAttributes.put(ClauseConstants.UNIT, unit);
		}
		else{
			unit="";
		}
		if(!functionOrTiming.contains("Select")){
			extraAttributes.put(ClauseConstants.TYPE, MatContext.get().operatorMapKeyLong.get(functionOrTiming));
			StringBuilder displayName = new StringBuilder();
			if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.TIMING_NODE){
//				String operatorType = ClauseConstants.getComparisonOperatorMap().containsKey(operator) ? ClauseConstants.getComparisonOperatorMap().get(operator) : " ";
				String operatorType = MatContext.get().operatorMapKeyLong.containsKey(operator) ? MatContext.get().operatorMapKeyLong.get(operator) : " ";
//				StringBuilder operatorTypeKey = new StringBuilder(operatorType);
				displayName.append(operatorType).append(" ").append(quantity).append(" ").append(unit).append(" ").append(functionOrTiming);
			}
			else if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
				String operatorType = MatContext.get().operatorMapKeyLong.containsKey(operator) ? MatContext.get().operatorMapKeyLong.get(operator) : " ";
				displayName.append(functionOrTiming).append(" ").append(operatorType).append(" ").append(quantity).append(" ").append(unit);
			}
			extraAttributes.put(ClauseConstants.DISPLAY_NAME, displayName.toString());
			xmlTreeDisplay.editNode(displayName.toString(), displayName.toString());
		}

		extraAttributes.put(ClauseConstants.QUANTITY, quantity);

		xmlTreeDisplay.getSelectedNode().setExtraInformation(ClauseConstants.EXTRA_ATTRIBUTES, extraAttributes);
	}

	/**
	 * TextBox allow only Digits.
	 * */
	private static void addHandlerToQuantityTextBox(TextBox quantity){

		quantity.addKeyPressHandler(new KeyPressHandler(){
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode()) 
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB 
						&& event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE){
					((TextBox) event.getSource()).cancelKey();
				}

			}});
	}

}
