package mat.client.clause.clauseworkspace.view;

import java.util.Arrays;

import org.apache.poi.ss.format.CellNumberFormatter;

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
	
	 
	 public static void showComparisonDialogBox(final XmlTreeDisplay xmlTreeDisplay, CellTreeNode cellTreeNode) {
		
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		//dialogBox.setWidth("22em");
	    //dialogBox.setHeight("15em");
	    dialogBox.setText("Edit");
	    dialogBox.setTitle("Edit");
		// Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    //dialogContents.setWidth("100%");
//	    dialogContents.setHeight("21em");
	    dialogContents.setWidth("20em");
	   // dialogContents.setHeight("100%");
	    dialogContents.setSpacing(5);
	    dialogBox.setWidget(dialogContents);

	    ListBoxMVP listAllTimeOrFunction = new ListBoxMVP();
	    listAllTimeOrFunction.addItem("--Select--");
	    String labelForListBox = null;
	    if(cellTreeNode.getNodeType() == CellTreeNode.TIMING_NODE){
	    	String[] timeRelationKey = ClauseConstants.getTimingOperators().keySet().toArray(new String[0]);
		
	    	for(int i=0;i<timeRelationKey.length;i++){
	    		listAllTimeOrFunction.addItem(timeRelationKey[i]);
	    	}
	    	listAllTimeOrFunction.setWidth("150px");
	    	labelForListBox = "Timing";
	    }else{
	    	String[] functionKeys = ClauseConstants.FUNCTIONS;
			for(int i=0;i<functionKeys.length;i++){
				listAllTimeOrFunction.addItem(functionKeys[i]);
			}
			labelForListBox = "Functions";
	    	
	    }
	    Label lableListBoxTimingOrFunction = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, labelForListBox);
		dialogContents.add(lableListBoxTimingOrFunction);
		dialogContents.setCellHorizontalAlignment(lableListBoxTimingOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllTimeOrFunction);
		dialogContents.setCellHorizontalAlignment(listAllTimeOrFunction, HasHorizontalAlignment.ALIGN_LEFT);
		
	    ListBoxMVP listAllOperator = new ListBoxMVP();
	    listAllOperator.addItem("--Select--");
	    String[] comparisonOpKeys = ClauseConstants.comparisonOperators;
		
		for(int i=0;i<comparisonOpKeys.length;i++){
			listAllOperator.addItem(comparisonOpKeys[i]);
		}
		listAllOperator.setWidth("150px");
		Label lableOperator = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Operators");
		dialogContents.add(lableOperator);
		dialogContents.setCellHorizontalAlignment(lableOperator, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(listAllOperator);
		dialogContents.setCellHorizontalAlignment(listAllOperator, HasHorizontalAlignment.ALIGN_LEFT);
		
		TextBox quantity = new TextBox();
		quantity.setWidth("150px");
		Label lableQuantity = (Label) LabelBuilder.buildLabel(listAllTimeOrFunction, "Quantity");
		dialogContents.add(lableQuantity);
		dialogContents.setCellHorizontalAlignment(lableQuantity, HasHorizontalAlignment.ALIGN_LEFT);
		dialogContents.add(quantity);
		dialogContents.setCellHorizontalAlignment(quantity, HasHorizontalAlignment.ALIGN_LEFT);
		
		ListBoxMVP listAllUnits = new ListBoxMVP();
		listAllUnits.addItem("--Select--");
	  
		for(int i=0;i<ClauseConstants.units.size();i++){
			listAllUnits.addItem(ClauseConstants.units.get(i));
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

}
