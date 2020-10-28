package mat.client.clause.clauseworkspace.view;

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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Node;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.CQLSuggestOracle;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * The Class CQLDefinitionsDialogBox.
 * This class is used to show a pop-up of all the CQL Definitions which can be attached to the population workspace.
 */
public class CQLArtifactsDialogBox {
	
	
	/** The is selected. */
	private static boolean isSelected;
	
	/**
	 * Show CQL Definition dialog box. (used for Population workspace).
	 *
	 * @param xmlTreeDisplay            the xml tree display
	 * @param isAdd            the is add
	 * @param isCQLDefinitions the is CQL definitions
	 */
	public static void showCQLArtifactsDialogBox(final XmlTreeDisplay xmlTreeDisplay,
			boolean isAdd, boolean isCQLDefinitions) {
		final DialogBox dialogBox = new DialogBox(false, true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		setSelected(false);
		String cqlString = "CQL Definition";
		if(!isCQLDefinitions){
			cqlString  = "CQL Functions";
		}
		dialogBox.setText("Double Click to Select a "+cqlString + ".");
		dialogBox.setTitle("Double Click to Select a "+cqlString + ".");
		dialogBox.getElement().setAttribute("id", "CQLArtifactsDialogBox");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("20em");
		dialogContents.setHeight("15em");
		dialogContents.setSpacing(8);
		dialogBox.setWidget(dialogContents);
		
		// Create Search box
		final SuggestBox suggestBox = new SuggestBox(getSuggestOracle(isCQLDefinitions));
		suggestBox.getElement().setId("cqlsuggestBox_SuggestBox");
		suggestBox.setWidth("18em");
		suggestBox.setText("Search");
		suggestBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(suggestBox.getText())) {
					suggestBox.setText("");
				}
			}
		});
		
		dialogContents.add(suggestBox);
		dialogContents.setCellHorizontalAlignment(suggestBox,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		// Create ListBox
		final ListBox listBox = new ListBox();
		listBox.getElement().setId("listBox_ListBox");
		listBox.setWidth("18em");
		listBox.setVisibleItemCount(10);
		String currentSelectedCQLDefinitionuid = xmlTreeDisplay.getSelectedNode()
				.getUUID();
		addCQLNamesToListBox(listBox, currentSelectedCQLDefinitionuid, isCQLDefinitions);
		
		// Add listbox to vertical panel and align it in center.
		dialogContents.add(listBox);
		dialogContents.setCellHorizontalAlignment(listBox,
				HasHorizontalAlignment.ALIGN_CENTER);
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		Button selectButton = new Button("Select", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(!isSelected()){
					DomEvent.fireNativeEvent(
							Document.get().createDblClickEvent(0, 0, 0, 0, 0,
									false, false, false, false), listBox);
					setSelected(true);
				}
			}
		});
		selectButton.getElement().setId("selectButton_Button");
		HorizontalPanel horizontalButtonPanel = new HorizontalPanel();
		horizontalButtonPanel.setSpacing(5);
		horizontalButtonPanel.add(selectButton);
		horizontalButtonPanel.setCellHorizontalAlignment(selectButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalButtonPanel.add(closeButton);
		horizontalButtonPanel.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		dialogContents.add(horizontalButtonPanel);
		dialogContents.setCellHorizontalAlignment(horizontalButtonPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		addSuggestHandler(suggestBox, listBox);
		addListBoxHandler(listBox, suggestBox, xmlTreeDisplay, dialogBox, isAdd, isCQLDefinitions,false);
		
		dialogBox.center();
	}
	
	/**
	 * Gets the suggest oracle.
	 *
	 * @param isCQLDefinitions the is CQL definitions
	 * @return the suggest oracle
	 */
	private static SuggestOracle getSuggestOracle(final boolean isCQLDefinitions) {
		if(isCQLDefinitions){
			return new CQLSuggestOracle(PopulationWorkSpaceConstants.defNames);
		}
		else{
			return new CQLSuggestOracle(PopulationWorkSpaceConstants.funcNames);
		}
	}

	/**
	 * Adds the list box handler.
	 *
	 * @param listBox            the list box
	 * @param suggestBox            the suggest box
	 * @param xmlTreeDisplay            the xml tree display
	 * @param dialogBox            the dialog box
	 * @param isAdd            the is add
	 * @param isCQLDefinitions the is CQL definitions
	 */
	private static void addListBoxHandler(final ListBox listBox,
			final SuggestBox suggestBox, final XmlTreeDisplay xmlTreeDisplay,
			final DialogBox dialogBox, final boolean isAdd, final boolean isCQLDefinitions, final boolean isCQLAggFunction) {
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
				if (listBox.getSelectedIndex() == -1) {
					return;
				}
				
				if(!isSelected()){
					String value = listBox.getItemText(listBox.getSelectedIndex());
					String uuid = listBox.getValue(listBox.getSelectedIndex());
					if (isAdd) {
						if(isCQLAggFunction){
							xmlTreeDisplay.addNode(value, value, CellTreeNode.CQL_AGG_FUNCTION_NODE);
						} else {
							xmlTreeDisplay.addNode(value, value, uuid,
									(isCQLDefinitions?CellTreeNode.CQL_DEFINITION_NODE:CellTreeNode.CQL_FUNCTION_NODE));
						}
						
					} else {
						xmlTreeDisplay.editNode(value, value, uuid);
					}
					xmlTreeDisplay.setDirty(true);
					setSelected(true);
				}
				dialogBox.hide();
			}
		});
	}
	
	/**
	 * Adds the suggest handler.
	 * 
	 * @param suggestBox
	 *            the suggest box
	 * @param listBox
	 *            the list box
	 */
	private static void addSuggestHandler(final SuggestBox suggestBox,
			final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedCQLDefinitionName = event.getSelectedItem()
						.getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedCQLDefinitionName.equals(listBox.getItemText(i))) {
						listBox.setItemSelected(i, true);
						break;
					}
				}
			}
		});
	}
	
	/**
	 * Adds the SubTree names to list box.
	 *
	 * @param listBox the list box
	 * @param currentSelectedCQLUuid the current selected CQL uuid
	 * @param isDefinition the is definition
	 */
	private static void addCQLNamesToListBox(ListBox listBox,
			String currentSelectedCQLUuid, boolean isDefinition) {
		Set<Entry<String, Node>> cqlNodes = PopulationWorkSpaceConstants
				.cqlDefinitionLookupNode.entrySet();
		Map<String, String> cqlNodesMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		
		// Loading a sorted TreeMap for Definitions Or Functions.
		if(!isDefinition){
			cqlNodes = PopulationWorkSpaceConstants.cqlFunctionLookupNode.entrySet();
			for (Entry<String, Node> cqlFunction : cqlNodes) {
				String key = cqlFunction.getKey();
				int lastIndexOfTilde = key.lastIndexOf("~");
				String uuid = key.substring(lastIndexOfTilde + 1);
				cqlNodesMap.put(key.substring(0, lastIndexOfTilde), uuid);
			}
		}
		else{
			for (Entry<String, Node> cqlDefinition : cqlNodes) {
				String key = cqlDefinition.getKey();
				int lastIndexOfTilde = key.lastIndexOf("~");
				String uuid = key.substring(lastIndexOfTilde + 1);
				cqlNodesMap.put(key.substring(0, lastIndexOfTilde), uuid);
			}
		}
		
		//Adding Definitions or Functions to Listbox.
		if(!isDefinition){
			for(Map.Entry<String,String> cqlFunction : cqlNodesMap.entrySet()) {
				String key = cqlFunction.getKey();
				String uuid = cqlFunction.getValue();
				listBox.addItem(key, uuid);
				
				if (uuid.equals(currentSelectedCQLUuid)) {
					listBox.setItemSelected(listBox.getItemCount() - 1, true);
				}
			}
		}
		else{
			for(Map.Entry<String,String> cqlDefinition : cqlNodesMap.entrySet()) {
				String key = cqlDefinition.getKey();
				String uuid = cqlDefinition.getValue();
				listBox.addItem(key, uuid);
				
				if (uuid.equals(currentSelectedCQLUuid)) {
					listBox.setItemSelected(listBox.getItemCount() - 1, true);
				}
			}
		}
		
		// Set tooltips for each element in listbox
		SelectElement selectElement = SelectElement.as(listBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
				.getOptions();
		for (int i = 0; i < options.getLength(); i++) {
			String text = options.getItem(i).getText();
			String title = text;
			OptionElement optionElement = options.getItem(i);
			optionElement.setTitle(title);
		}
	}
	
	
	/**
	 * Show edit CQL agg func dialog box.
	 *
	 * @param xmlTreeDisplay the xml tree display
	 */
	public static void showEditCQLAggFuncDialogBox(final XmlTreeDisplay xmlTreeDisplay, boolean isAdd){
		
		final DialogBox aggDialogBox = new DialogBox(false, true);
		aggDialogBox.setGlassEnabled(true);
		aggDialogBox.setAnimationEnabled(true);
		setSelected(false);
		
		aggDialogBox.setText("Double Click to Select a Aggregate Function.");
		aggDialogBox.setTitle("Double Click to Select a Aggregate Function.");
		aggDialogBox.getElement().setAttribute("id", "CQLAggregateDialogBox");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("20em");
		dialogContents.setHeight("15em");
		dialogContents.setSpacing(8);
		aggDialogBox.setWidget(dialogContents);
		
		// Create Search box
		final SuggestBox suggestBox = new SuggestBox(new CQLSuggestOracle(PopulationWorkSpaceConstants.AggfuncNames));
		suggestBox.getElement().setId("cqlsuggestBox_SuggestBox");
		suggestBox.setWidth("18em");
		suggestBox.setText("Search");
		suggestBox.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(suggestBox.getText())) {
					suggestBox.setText("");
				}
			}
		});
		
		dialogContents.add(suggestBox);
		dialogContents.setCellHorizontalAlignment(suggestBox,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		// Create ListBox
		final ListBox listBox = new ListBox();
		listBox.getElement().setId("listBox_ListBox");
		listBox.setWidth("18em");
		listBox.setVisibleItemCount(10);
		String currentSelectedCQLAggregateFunc = xmlTreeDisplay.getSelectedNode()
				.getName();
		addCQLAggrNamesToListBox(listBox, currentSelectedCQLAggregateFunc);
		
		// Add listbox to vertical panel and align it in center.
		dialogContents.add(listBox);
		dialogContents.setCellHorizontalAlignment(listBox,
				HasHorizontalAlignment.ALIGN_CENTER);
		// Add a Close button at the bottom of the dialog
		Button closeButton = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				aggDialogBox.hide();
			}
		});
		closeButton.getElement().setId("closeButton_Button");
		Button selectButton = new Button("Select", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(!isSelected()){
					DomEvent.fireNativeEvent(
							Document.get().createDblClickEvent(0, 0, 0, 0, 0,
									false, false, false, false), listBox);
					setSelected(true);
				}
			}
		});
		selectButton.getElement().setId("selectButton_Button");
		HorizontalPanel horizontalButtonPanel = new HorizontalPanel();
		horizontalButtonPanel.setSpacing(5);
		horizontalButtonPanel.add(selectButton);
		horizontalButtonPanel.setCellHorizontalAlignment(selectButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalButtonPanel.add(closeButton);
		horizontalButtonPanel.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		dialogContents.add(horizontalButtonPanel);
		dialogContents.setCellHorizontalAlignment(horizontalButtonPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		addSuggestHandler(suggestBox, listBox);
		addListBoxHandler(listBox, suggestBox, xmlTreeDisplay, aggDialogBox, isAdd, false, true);
		
		aggDialogBox.center();
		
	}

	/**
	 * Adds the CQL aggr names to list box.
	 *
	 * @param listBox the list box
	 * @param currentSelectedCQLAggregateFunc the current selected CQL aggregate func
	 */
	private static void addCQLAggrNamesToListBox(ListBox listBox, String currentSelectedCQLAggregateFunc) {
		List<String> cqlAggArtifacts = PopulationWorkSpaceConstants.AggfuncNames;
		for(int i=0; i<cqlAggArtifacts.size();i++){
			String key = cqlAggArtifacts.get(i);
			String value = cqlAggArtifacts.get(i);
			listBox.addItem(key, value);
			
			if (value.equals(currentSelectedCQLAggregateFunc)) {
				listBox.setItemSelected(listBox.getItemCount() - 1, true);
			}
			
		}
		
		// Set tooltips for each element in listbox
				SelectElement selectElement = SelectElement.as(listBox.getElement());
				com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
						.getOptions();
				for (int i = 0; i < options.getLength(); i++) {
					String text = options.getItem(i).getText();
					String title = text;
					OptionElement optionElement = options.getItem(i);
					optionElement.setTitle(title);
				}
		
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public static boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * Sets the selected.
	 *
	 * @param isSelected the new selected
	 */
	public static void setSelected(boolean isSelected) {
		CQLArtifactsDialogBox.isSelected = isSelected;
	}
	
}
