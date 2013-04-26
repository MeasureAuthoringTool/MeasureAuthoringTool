package mat.client.clause.clauseworkspace.view;


import java.util.HashMap;
import java.util.Map;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

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
	    dialogBox.setWidget(dialogContents);
	    @SuppressWarnings("unchecked")
		HashMap<String,String> extraAttributesMap = (HashMap<String, String>) cellTreeNode.getExtraInformation("extraAttributes_"+cellTreeNode.getNodeType());
	
	    final ListBoxMVP listAllTimeOrFunction = new ListBoxMVP();
	    String timingMethod ="--Select--";
	    String operatorMethod="--Select--";
	    String quantityValue ="";
	    String unitType ="--Select--";
	 
	    if(extraAttributesMap!=null){
		   timingMethod = extraAttributesMap.get(ClauseConstants.DISPLAY_NAME);
		   operatorMethod = extraAttributesMap.containsKey(ClauseConstants.OPERATOR_TYPE) ? extraAttributesMap.get(ClauseConstants.OPERATOR_TYPE) : operatorMethod;
		   quantityValue = extraAttributesMap.containsKey(ClauseConstants.QUANTITY) ? extraAttributesMap.get(ClauseConstants.QUANTITY) : quantityValue;
		   unitType = extraAttributesMap.containsKey(ClauseConstants.UNIT) ? extraAttributesMap.get(ClauseConstants.UNIT) : unitType;
	   }else{
		   timingMethod = cellTreeNode.getLabel();
	   }
	    String labelForListBox = null;
	  //List for Timing or Function based on Node Type.
	    if(cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE){
	    	String[] timeRelationKey = ClauseConstants.getTimingOperators().keySet().toArray(new String[0]);
	    	if(timingMethod.contains("Select"))
	    		listAllTimeOrFunction.addItem(timingMethod);
	    	for(int i=0;i<timeRelationKey.length;i++){
	    		listAllTimeOrFunction.addItem(timeRelationKey[i]);
	    		if(timeRelationKey[i].equalsIgnoreCase(timingMethod)){
	    			listAllTimeOrFunction.setSelectedIndex(i);
	    		}
	    	}
	    	listAllTimeOrFunction.setWidth("150px");
	    	labelForListBox = "Timing";
	    }else{
	    	String[] functionKeys = ClauseConstants.FUNCTIONS;
	    	if(timingMethod.contains("Select"))
	    		listAllTimeOrFunction.addItem(timingMethod);
			for(int i=0;i<functionKeys.length;i++){
				listAllTimeOrFunction.addItem(functionKeys[i]);
				if(functionKeys[i].equalsIgnoreCase(timingMethod)){
	    			listAllTimeOrFunction.setSelectedIndex(i);
	    		}
			}
			listAllTimeOrFunction.setWidth("150px");
			labelForListBox = "Functions";
	    	
	    }
	    Label lableListBoxTimingOrFunction = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, labelForListBox);
		dialogContents.add(lableListBoxTimingOrFunction);
		dialogContents.setCellHorizontalAlignment(lableListBoxTimingOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllTimeOrFunction);
		dialogContents.setCellHorizontalAlignment(listAllTimeOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		
		//List of Operators.
	    final ListBoxMVP listAllOperator = new ListBoxMVP();
	   // listAllOperator.addItem("--Select--");
	    String[] comparisonOpKeys = ClauseConstants.comparisonOperators;
	    if(operatorMethod.contains("Select"))
	    	listAllOperator.addItem(operatorMethod);
		for(int i=0;i<comparisonOpKeys.length;i++){
			listAllOperator.addItem(comparisonOpKeys[i]);
			if(comparisonOpKeys[i].equalsIgnoreCase(operatorMethod)){
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
	    Button save = new Button("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				saveAttributesToNode(listAllTimeOrFunction.getValue(), listAllOperator.getValue(),quantity.getValue(),listAllUnits.getValue(),xmlTreeDisplay);
			}
		});
	    // Add a Close button at the bottom of the dialog
	    Button closeButton = new Button("Close", new ClickHandler() {
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
	    
	    dialogContents.add(buttonPanel);
	    dialogBox.center();
	    dialogBox.show();
		
	}
	 
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
			extraAttributes.put(ClauseConstants.TYPE, functionOrTiming);
			if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.TIMING_NODE){
				String operatorType = ClauseConstants.getComparisonOperatorMap().containsKey(operator) ? ClauseConstants.getComparisonOperatorMap().get(operator) : " ";
				StringBuilder operatorTypeKey = new StringBuilder(operatorType);
				functionOrTiming = (operatorTypeKey.append(" ").append(quantity).append(" ").append(unit).append(" ").append(functionOrTiming)).toString();
			}
			else if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
				String operatorType = ClauseConstants.getComparisonOperatorMap().containsKey(operator) ? ClauseConstants.getComparisonOperatorMap().get(operator) : " ";
				StringBuilder functionOrTimingKey = new StringBuilder(functionOrTiming);
				functionOrTiming = (functionOrTimingKey.append(" ").append(operatorType).append(" ").append(quantity).append(" ").append(unit)).toString();
			}
			extraAttributes.put(ClauseConstants.DISPLAY_NAME, functionOrTiming);
			xmlTreeDisplay.editNode(functionOrTiming, functionOrTiming);
		}
		
		extraAttributes.put(ClauseConstants.QUANTITY, quantity);
		
		xmlTreeDisplay.getSelectedNode().setExtraInformation("extraAttributes_"+xmlTreeDisplay.getSelectedNode().getNodeType(), extraAttributes);
	}

}
