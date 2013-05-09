package mat.client.clause.clauseworkspace.view;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Node;

public class QDMDialogBox{	
	 
	 public static void showQDMDialogBox(final XmlTreeDisplay xmlTreeDisplay) {
		final DialogBox dialogBox = new DialogBox(false,true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
	    dialogBox.setText("Double Click to Select QDM Element.");
	    dialogBox.setTitle("Double Click to Select QDM Element.");
		
		// Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setWidth("20em");
	    dialogContents.setHeight("15em");
	    dialogContents.setSpacing(8);
	    dialogBox.setWidget(dialogContents);
	    
	    //Create Search box
	    final SuggestBox suggestBox = new SuggestBox(createSuggestOracle(ClauseConstants.getElementLookUps()));
	    suggestBox.setWidth("18em");
	    suggestBox.setText("Search");
	    suggestBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if("Search".equals(suggestBox.getText())){
					suggestBox.setText("");
				}
			}
		});
	    
	    dialogContents.add(suggestBox);
	    dialogContents.setCellHorizontalAlignment(
	    		suggestBox, HasHorizontalAlignment.ALIGN_CENTER);
	    	    	    
	    //Create ListBox
	    final ListBox listBox = new ListBox();
	    listBox.setWidth("18em");
	    listBox.setVisibleItemCount(10);
	    addQDMNamesToListBox(listBox, ClauseConstants.getElementLookUps());
	    
	    //Add listbox to vertical panel and align it in center.
	    dialogContents.add(listBox);
	    dialogContents.setCellHorizontalAlignment(
	    		listBox, HasHorizontalAlignment.ALIGN_CENTER);
	    // Add a Close button at the bottom of the dialog
	    Button closeButton = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();		
			}
		});
	  	
	    Button selectButton = new Button("Select", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    DomEvent.fireNativeEvent(Document.get().createDblClickEvent(0, 0, 0, 0, 0, false, false, false, false), listBox);				
			}
		});
	    HorizontalPanel horizontalButtonPanel = new HorizontalPanel();
	    horizontalButtonPanel.setSpacing(5);
	    horizontalButtonPanel.add(selectButton);
	    horizontalButtonPanel.setCellHorizontalAlignment(selectButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    horizontalButtonPanel.add(closeButton);
	    horizontalButtonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    dialogContents.add(horizontalButtonPanel);
	    dialogContents.setCellHorizontalAlignment(horizontalButtonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    addSuggestHandler(suggestBox,listBox);
	    addListBoxHandler(listBox,suggestBox,xmlTreeDisplay, dialogBox);
	    
	    dialogBox.center();	    		
	}
	 
	private static void addListBoxHandler(final ListBox listBox, final SuggestBox suggestBox, final XmlTreeDisplay xmlTreeDisplay, final DialogBox dialogBox) {
		listBox.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = listBox.getSelectedIndex();
				String selectedItem = listBox.getItemText(selectedIndex);
				suggestBox.setText(selectedItem);
			}
		});
		listBox.addDoubleClickHandler(new DoubleClickHandler() {			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(listBox.getSelectedIndex() == -1){
					return;
				}
				String value = listBox.getItemText(listBox.getSelectedIndex());
				String label = value;
				if(label.length() > ClauseConstants.LABEL_MAX_LENGTH){
					label = label.substring(0,  ClauseConstants.LABEL_MAX_LENGTH -1).concat("...");
				}
				xmlTreeDisplay.addNode(value, label, CellTreeNode.ELEMENT_REF_NODE);
				xmlTreeDisplay.setDirty(true);
				dialogBox.hide();
			}
		});		
	}

	private static void addSuggestHandler(final SuggestBox suggestBox, final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem().getReplacementString();
				for(int i=0;i<listBox.getItemCount();i++){
					if(selectedQDMName.equals(listBox.getItemText(i))){
						listBox.setItemSelected(i, true);
						break;
					}
				}
			}
		});;
	}

	
	private static void addQDMNamesToListBox(ListBox listBox, Map<String, Node> qdmElementLookupNode) {
		ArrayList<Node> nodeItems = new ArrayList<Node>();
		nodeItems.addAll(qdmElementLookupNode.values());
		Map<String,Node> map = new TreeMap<String,Node>();
		for(Node node : nodeItems){
			if(!node.getAttributes().getNamedItem("datatype").getNodeValue().toString().equalsIgnoreCase("attribute")){
				map.put(node.getAttributes().getNamedItem("name").getNodeValue().toString().concat(": "+node.getAttributes().getNamedItem("id").getNodeValue().toString()).toLowerCase(), node);
			}
			
		}
		Set<String> keys = map.keySet();
		for (String qdm : keys) {
			Node node = map.get(qdm);
			String listName = node.getAttributes().getNamedItem("name").getNodeValue().toString();
			if(node.getAttributes().getNamedItem("instance") != null){
				listName = node.getAttributes().getNamedItem("instance").getNodeValue() + " of " + listName;
			}
		
			if(node.getAttributes().getNamedItem("datatype") != null){
				listName = listName + ": " + node.getAttributes().getNamedItem("datatype").getNodeValue();
			}
			listBox.addItem(listName);
		}
		
		//Set tooltips for each element in listbox
		SelectElement selectElement = SelectElement.as(listBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
		for (int i = 0; i < options.getLength(); i++) {
			OptionElement optionElement = options.getItem(i);
	        optionElement.setTitle(optionElement.getInnerText());
	    }
	}
	
	
	private static MultiWordSuggestOracle createSuggestOracle(Map<String, Node> qdmElementLookupNode){
		MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
		multiWordSuggestOracle.addAll(qdmElementLookupNode.keySet());
		return multiWordSuggestOracle;
	}
}
