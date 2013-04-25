package mat.client.clause.clauseworkspace.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class QDMAttributeDialogBox {
	
	
	private static final class DeleteSelectedClickHandler implements
			ClickHandler {
		private final Grid grid;

		private DeleteSelectedClickHandler(Grid grid) {
			this.grid = grid;
		}

		@Override
		public void onClick(ClickEvent event) {
			int rowCount = grid.getRowCount();
			List<Integer> selectedRowNums = new ArrayList<Integer>();
			//collect all the row nums for checked rows.
			for(int i=0;i<rowCount;i++){
				CheckBox checkBox = (CheckBox)grid.getWidget(i, 0);
				if(checkBox.getValue()){
					selectedRowNums.add(i);
				}
			}
			System.out.println("Selected Rows:"+selectedRowNums);
			//Iterate through the selected row nums backwards so that we delete the higher rows before the lower ones.
			for(int i=selectedRowNums.size()-1;i>=0;i--){
				int rowNum = selectedRowNums.get(i);
				System.out.println("deleting rowNum:"+rowNum);
				grid.removeRow(rowNum);
			}
		}
	}

	private static final class QDMAttributeGridClickHandler implements
			ClickHandler {
		private final Grid grid;

		private QDMAttributeGridClickHandler(Grid grid) {
			this.grid = grid;
		}

		@Override
		public void onClick(ClickEvent event) {
			HTMLTable.Cell cell = grid.getCellForEvent(event);
			
			Widget widget = grid.getWidget(cell.getRowIndex(), cell.getCellIndex());
			if(cell.getCellIndex() == 1){
				ListBox attributeListBox = (ListBox)widget;
				System.out.println(attributeListBox.getItemText(attributeListBox.getSelectedIndex()));
				
				String row = attributeListBox.getValue(attributeListBox.getSelectedIndex());
				String text = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
				int rowNum = Integer.parseInt(row);
				
				if("Select".equalsIgnoreCase(text)){
					ListBox modeListBox = (ListBox)grid.getWidget(rowNum, 2);
					modeListBox.setSelectedIndex(1);
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), modeListBox);
					modeListBox.setEnabled(false);
				}else{
					ListBox modeListBox = (ListBox)grid.getWidget(rowNum, 2);
					modeListBox.setEnabled(true);
				}
				
			}else if(cell.getCellIndex() == 2){
				ListBox modeListBox = (ListBox)widget;
				String row = modeListBox.getValue(modeListBox.getSelectedIndex());
				String text = modeListBox.getItemText(modeListBox.getSelectedIndex());
				int rowNum = Integer.parseInt(row);
				
				if("Select".equalsIgnoreCase(text) || "Check if Present".equalsIgnoreCase(text) || "Comparison".equalsIgnoreCase(text)){
					TextBox textBox = new TextBox();
					textBox.setEnabled(false);
					textBox.setWidth("8em");
					grid.setWidget(rowNum, 3, textBox);
				}else if("Value Set".equals(text)){

					//need to get all the QDM's in elementLookUp tag
					//for demo purpose
					ListBox qdmListBox = new ListBox(false);
					qdmListBox.setVisibleItemCount(1);
					qdmListBox.setWidth("8em");
					
					//demo only..remove later
					qdmListBox.addItem("QDM 1");
					qdmListBox.addItem("QDM 2");
					qdmListBox.addItem("QDM 3");
					
					grid.setWidget(rowNum, 3, qdmListBox);
				}else {
					HorizontalPanel panel = new HorizontalPanel();
					panel.setWidth("8em");
					panel.setSpacing(0);
					
					TextBox textBox = new TextBox();
					textBox.addKeyPressHandler(new KeyPressHandler() {
						
						@Override
						public void onKeyPress(KeyPressEvent event) {
							TextBox sender = (TextBox)event.getSource();
							if (sender.isReadOnly() || !sender.isEnabled()) {
						        return;
						    }

						    Character charCode = event.getCharCode();
						    int unicodeCharCode = event.getUnicodeCharCode();

						    // allow digits, '.' and non-characters
						    //if (!(Character.isDigit(charCode) || charCode == '.' || unicodeCharCode == 0))
						    //allow only digits
						    if (!(Character.isDigit(charCode))){
						        sender.cancelKey();
						    }
						}
					});
					textBox.setWidth("4em");
					textBox.setHeight("19");
					
					ListBox units = new ListBox(false);
					units.setVisibleItemCount(1);
					units.setWidth("4em");
					
					units.addItem("Days");
					units.addItem("hours");
					units.addItem("mg");
					
					panel.add(textBox);
					panel.add(units);
					
					grid.setWidget(rowNum, 3, panel);
					
				}
			}
		}
	}

	private static final class AddNewQDMAttributeClickHandler implements ClickHandler {
		private final List<String> attributeList;
		private final List<String> mode;
		private final Grid grid;

		private AddNewQDMAttributeClickHandler(List<String> attributeList,
				List<String> mode, Grid grid) {
			this.attributeList = attributeList;
			this.mode = mode;
			this.grid = grid;
		}

		@Override
		public void onClick(ClickEvent event) {
			grid.resizeRows(grid.getRowCount()+1);
			int i = grid.getRowCount()-1;
			
			CheckBox checkBox = new CheckBox();
			grid.setWidget(i, 0, checkBox);
			//grid.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);
			grid.getCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			
			final ListBox attributeListBox = new ListBox(false);
			attributeListBox.setVisibleItemCount(1);
			attributeListBox.setWidth("8em");
			attributeListBox.addItem("Select", ""+i);
			for(String attributeName:attributeList){
				attributeListBox.addItem(attributeName,""+i);
			}
			grid.setWidget(i, 1, attributeListBox);
			//If needed, attributeListBox can have a ChangeHandler here.
			
			final ListBox modeListBox = new ListBox(false);
			modeListBox.setVisibleItemCount(1);
			modeListBox.setWidth("8em");
			modeListBox.addItem("Select", ""+i);
			for(String modeValue:mode){
				modeListBox.addItem(modeValue, ""+i);
			}
			modeListBox.setEnabled(false);
			grid.setWidget(i, 2, modeListBox);
			
			//If needed, modeListBox can have a ChangeHandler here.
			
			TextBox textBox = new TextBox();
			textBox.setEnabled(false);
			textBox.setWidth("8em");
			grid.setWidget(i, 3, textBox);				
		}
	}

	public static void showQDMAttributeDialogBox(XmlTreeDisplay xmlTreeDisplay, CellTreeNode cellTreeNode) {
		
		//If the CellTreeNode type isnt CellTreeNode.ELEMENT_REF_NODE then return without doing anything.
		if(cellTreeNode.getNodeType() != CellTreeNode.ELEMENT_REF_NODE){
			return;
		}
		
		Node qdmNode = findElementLookUpNode(cellTreeNode.getName());
		//Could not find the qdm node in elemenentLookup tag 
		if(qdmNode == null){
			return;
		}
		
		String qdmDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		
		//Based on the qdmDataType, we need to fetch the possible attributes from the "QDM_ATTRIBUTES" table
		List<String> attributeList = dummyFetchAtttributes(qdmDataType);
		
		List<String> mode = getModeList();
		buildAndDisplayDialogBox(attributeList, mode,xmlTreeDisplay, cellTreeNode);
		
	}
	
	private static void buildAndDisplayDialogBox(List<String> attributeList,
			List<String> mode, XmlTreeDisplay xmlTreeDisplay, CellTreeNode cellTreeNode) {
		
		final DialogBox qdmAttributeDialogBox = new DialogBox(false,true);
		qdmAttributeDialogBox.getElement().setId("qdmAttributeDialog");
		qdmAttributeDialogBox.setGlassEnabled(true);
		qdmAttributeDialogBox.setAnimationEnabled(true);
	    qdmAttributeDialogBox.setText("QDM Attributes.");
	    qdmAttributeDialogBox.setTitle("QDM Attributes.");
	    
		// Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	        
	    qdmAttributeDialogBox.setWidget(dialogContents);
	    
	    HorizontalPanel horizontalDeleteAddNewPanel = new HorizontalPanel();
	    horizontalDeleteAddNewPanel.setSpacing(5);
	    
	    //Add a Delete Selected button
	    Button deleteSelectedButton = new Button("Delete Selected");
	    horizontalDeleteAddNewPanel.add(deleteSelectedButton);
	    horizontalDeleteAddNewPanel.setCellHorizontalAlignment(deleteSelectedButton, HasHorizontalAlignment.ALIGN_LEFT);
	    
	    //Add a Add New button
	    Button addNewButton = new Button("Add New");
	    horizontalDeleteAddNewPanel.add(addNewButton);
	    horizontalDeleteAddNewPanel.setCellHorizontalAlignment(addNewButton, HasHorizontalAlignment.ALIGN_LEFT);
	    	    
	    //Add a Close button at the bottom of the dialog
	    Button closeButton = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				qdmAttributeDialogBox.hide();		
				
			}
		});
	    
	    //Add a Save button at the bottom of the dialog
	    Button saveButton = new Button("Save", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO				
			}
		});
	    
	    HorizontalPanel horizontalSaveClosePanel = new HorizontalPanel();
	    horizontalSaveClosePanel.setSpacing(5);
	    
	    horizontalSaveClosePanel.add(saveButton);
	    horizontalSaveClosePanel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    horizontalSaveClosePanel.add(closeButton);
	    horizontalSaveClosePanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
	  	  
	    addTableToPanel(dialogContents,attributeList,mode,xmlTreeDisplay, cellTreeNode,deleteSelectedButton,addNewButton);
	    
	    dialogContents.add(horizontalDeleteAddNewPanel);
	    dialogContents.setCellHorizontalAlignment(horizontalDeleteAddNewPanel, HasHorizontalAlignment.ALIGN_LEFT);
	    
	    dialogContents.add(horizontalSaveClosePanel);
	    dialogContents.setCellHorizontalAlignment(horizontalSaveClosePanel, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    dialogContents.setHeight("21em");
	    qdmAttributeDialogBox.center();	    
	}
	
	/**
	 * This method will create the actual table to be placed and shown in the pop-up DialogBox.
	 * @param dialogContents
	 * @param attributeList
	 * @param mode
	 * @param cellTreeNode 
	 * @param xmlTreeDisplay 
	 * @param addNewButton 
	 * @param deleteSelectedButton 
	 */
	private static void addTableToPanel(VerticalPanel dialogContents,
			final List<String> attributeList, final List<String> mode, XmlTreeDisplay xmlTreeDisplay, CellTreeNode cellTreeNode, Button deleteSelectedButton, Button addNewButton) {
		
		@SuppressWarnings("unchecked")
		List<Node> attributeNodeList = (List<Node>) cellTreeNode.getExtraInformation("attributes");
		int rows = (attributeNodeList == null)?0:attributeNodeList.size();
	    
		final Grid grid = new Grid(rows,4);
		grid.addClickHandler(new QDMAttributeGridClickHandler(grid));
		
		//Handler to Add New rows to the attribute table.
		addNewButton.addClickHandler(new AddNewQDMAttributeClickHandler(attributeList, mode, grid));
		deleteSelectedButton.addClickHandler(new DeleteSelectedClickHandler(grid));
		
		if(rows == 0){
			//Add a blank attribute row to the table for the user to fill in.
			DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false), addNewButton);
		}else{
			for(int i=0;i<rows;i++){
				//Add a blank row to the table.
				DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false), addNewButton);
				//TODO:Fill in the blank row with values from the node.
				
			}
		}
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("28em", "19em");
		
		DecoratorPanel decoratorPanel = new DecoratorPanel();
		scrollPanel.setWidget(grid);
		decoratorPanel.setWidget(scrollPanel);
		dialogContents.add(decoratorPanel);
		
		dialogContents.setCellHorizontalAlignment(decoratorPanel, HasHorizontalAlignment.ALIGN_LEFT);		
	}

	private static List<String> getModeList() {
		List<String> modeList = new ArrayList<String>();
		
		modeList.add("Check if Present");
		modeList.add("Comparison");
		modeList.add("-- Less Than");
		modeList.add("-- Greater Than");
		modeList.add("-- Less Than Or Equal To");
		modeList.add("-- Greater Than Or Equal To");
		modeList.add("Value Set");
		
		return modeList;
	}

	private static List<String> dummyFetchAtttributes(String qdmDataType) {
		List<String> attributeList = new ArrayList<String>();
		
		attributeList.add("Health Record Field");
		attributeList.add("Source");
		attributeList.add("Recorder");
		
		return attributeList;
	}

	private static Node findElementLookUpNode(String name) {
		Set<String> qdmNames = ClauseConstants.getElementLookUps().keySet();
		for(String qdmName:qdmNames){
			if(qdmName.equals(name)){
				com.google.gwt.xml.client.Node qdmNode = ClauseConstants.getElementLookUps().get(qdmName);
				return qdmNode;
			}
		}
		return null;
		
	}

}
