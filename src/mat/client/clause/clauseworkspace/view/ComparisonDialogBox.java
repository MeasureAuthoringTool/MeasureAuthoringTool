package mat.client.clause.clauseworkspace.view;

import java.util.Arrays;

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
//	    dialogContents.setWidth("25em");
	   // dialogContents.setHeight("100%");
	    dialogContents.setSpacing(5);
	    dialogBox.setWidget(dialogContents);

	    ListBoxMVP listAllTimeRelations = new ListBoxMVP();
	    listAllTimeRelations.addItem("--Select--");
	    String[] timeRelationKey = ClauseConstants.getTimingOperators().keySet().toArray(new String[0]);
		//Arrays.sort(key);
		for(int i=0;i<timeRelationKey.length;i++){
			listAllTimeRelations.addItem(timeRelationKey[i]);
		}
		listAllTimeRelations.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(listAllTimeRelations, "Timing"));
		dialogContents.add(listAllTimeRelations);
			    	    
	    ListBoxMVP listAllLogicalOps = new ListBoxMVP();
	    listAllLogicalOps.addItem("--Select--");
	    String[] logicalOpsKey = ClauseConstants.LOGICAL_OPS;
		//Arrays.sort(key1);
		for(int i=0;i<logicalOpsKey.length;i++){
			listAllLogicalOps.addItem(logicalOpsKey[i]);
		}
		listAllLogicalOps.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(listAllLogicalOps, "Logical Operator"));
		dialogContents.add(listAllLogicalOps);
		
		
	    ListBoxMVP listAllOperator = new ListBoxMVP();
	    listAllOperator.addItem("--Select--");
	    String[] comparisonOpKeys = ClauseConstants.comparisonOperators;
		//Arrays.sort(key2);
		for(int i=0;i<comparisonOpKeys.length;i++){
			listAllOperator.addItem(comparisonOpKeys[i]);
		}
		listAllOperator.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(listAllOperator, "Operators"));
		dialogContents.add(listAllOperator);
		
		ListBoxMVP listAllFunctions = new ListBoxMVP();
		listAllFunctions.addItem("--Select--");
	    String[] functionKeys = ClauseConstants.FUNCTIONS;
		//Arrays.sort(key2);
		for(int i=0;i<functionKeys.length;i++){
			listAllFunctions.addItem(functionKeys[i]);
		}
		listAllFunctions.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(listAllFunctions, "Functions"));
		dialogContents.add(listAllFunctions);
		
		TextBox quantity = new TextBox();
		quantity.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(quantity, "Quantity"));
		dialogContents.add(quantity);
		
		ListBoxMVP listAllUnits = new ListBoxMVP();
		listAllUnits.addItem("--Select--");
	    //String[] unitsKeys = (String[]) ClauseConstants.units.toArray();
		//Arrays.sort(key2);
		for(int i=0;i<ClauseConstants.units.size();i++){
			listAllUnits.addItem(ClauseConstants.units.get(i));
		}
		listAllUnits.setWidth("150px");
		dialogContents.add(LabelBuilder.buildLabel(listAllUnits, "Units"));
		dialogContents.add(listAllUnits);

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
	    buttonPanel.setCellHorizontalAlignment(save, HasHorizontalAlignment.ALIGN_CENTER);
	    buttonPanel.add(closeButton);
	    buttonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    dialogContents.add(buttonPanel);
	    dialogBox.center();
	    dialogBox.show();
		
	}

}
