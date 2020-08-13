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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import java.util.Map.Entry;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class SubTreeDialogBox.
 */
public class SubTreeDialogBox {
	
	
	/** The is selected. */
	private static boolean isSelected;
	
	/**
	 * Show SubTree dialog box. (used for Population workspace).
	 * 
	 * @param xmlTreeDisplay
	 *            the xml tree display
	 * @param isAdd
	 *            the is add
	 */
	public static void showSubTreeDialogBox(final XmlTreeDisplay xmlTreeDisplay,
			boolean isAdd){
		showSubTreeDialogBox(xmlTreeDisplay, isAdd,false);
	}
	
	/**
	 * Show SubTree dialog box. (used for Clause workspace)
	 *
	 * @param xmlTreeDisplay the xml tree display
	 * @param isAdd the is add
	 * @param isClauseWorkspace the is clause workspace
	 */
	public static void showSubTreeDialogBox(final XmlTreeDisplay xmlTreeDisplay,
			boolean isAdd, boolean isClauseWorkspace) {
		final DialogBox dialogBox = new DialogBox(false, true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		setSelected(false);
		dialogBox.setText("Double Click to Select SubTree Element.");
		dialogBox.setTitle("Double Click to Select SubTree Element.");
		dialogBox.getElement().setAttribute("id", "SubTreeDialogBox");
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogContents.setWidth("20em");
		dialogContents.setHeight("15em");
		dialogContents.setSpacing(8);
		dialogBox.setWidget(dialogContents);
		
		// Create Search box
		final SuggestBox suggestBox = new SuggestBox(createSuggestOracle());
		suggestBox.getElement().setId("suggestBox_SuggestBox");
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
		String currentSelectedSubTreeuid = xmlTreeDisplay.getSelectedNode()
				.getUUID();
		
		if(isClauseWorkspace){
			CellTreeNode cellTreeNode = (CellTreeNode) (xmlTreeDisplay
					.getXmlTree().getRootTreeNode().getChildValue(0));
			currentSelectedSubTreeuid = cellTreeNode.getChilds().get(0).getUUID();
		}
		
		addSubTreeNamesToListBox(listBox, currentSelectedSubTreeuid, isClauseWorkspace);
		
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
		addListBoxHandler(listBox, suggestBox, xmlTreeDisplay, dialogBox, isAdd);
		
		dialogBox.center();
	}
	
	/**
	 * Adds the list box handler.
	 * 
	 * @param listBox
	 *            the list box
	 * @param suggestBox
	 *            the suggest box
	 * @param xmlTreeDisplay
	 *            the xml tree display
	 * @param dialogBox
	 *            the dialog box
	 * @param isAdd
	 *            the is add
	 */
	private static void addListBoxHandler(final ListBox listBox,
			final SuggestBox suggestBox, final XmlTreeDisplay xmlTreeDisplay,
			final DialogBox dialogBox, final boolean isAdd) {
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
						xmlTreeDisplay.addNode(value, value, uuid,
								CellTreeNode.SUBTREE_REF_NODE);
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
				String selectedSubTreeName = event.getSelectedItem()
						.getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedSubTreeName.equals(listBox.getItemText(i))) {
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
	 * @param currentSelectedSubTreeUuid the current selected SubTree uuid
	 * @param isClauseWorkSpace the is clause work space
	 */
	private static void addSubTreeNamesToListBox(ListBox listBox,
			String currentSelectedSubTreeUuid, boolean isClauseWorkSpace) {
		Set<Entry<String, Node>> subTreeLookUpNodes = PopulationWorkSpaceConstants
				.getSubTreeLookUpNode().entrySet();
		for (Entry<String, Node> subTreeLookup : subTreeLookUpNodes) {
			String key = subTreeLookup.getKey();
			String uuid = key.substring(key.lastIndexOf("~") + 1);
			if (uuid.equals(currentSelectedSubTreeUuid) && isClauseWorkSpace) {
				continue;
			}
			if (PopulationWorkSpaceConstants.getSubTreeLookUpName().get(uuid) != null) {
				String item = PopulationWorkSpaceConstants.getSubTreeLookUpName().get(uuid);
				boolean noCycle = true;
				if (isClauseWorkSpace) {
					System.out.println();
					System.out.println();
					System.out.println("checkForCycleConditions for:" + item);
					System.out.println("currentSelectedSubTreeUuid for:" + currentSelectedSubTreeUuid);
					Node node = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(item + "~" + uuid);
					NamedNodeMap namedNodeMap = node.getAttributes();
					if (namedNodeMap.getNamedItem("instance") != null) { // filter Occurrences of Selected Clause.
						String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
						if (currentSelectedSubTreeUuid.equalsIgnoreCase(instanceOfUUID)) {
							noCycle = false;
						} else {
							noCycle = checkForCycleConditions(item, uuid, currentSelectedSubTreeUuid);
						}
					} else {
						noCycle = checkForCycleConditions(item, uuid, currentSelectedSubTreeUuid);
					}
				}
				if (noCycle) {
					listBox.addItem(item, uuid);
				}
			}
			if (uuid.equals(currentSelectedSubTreeUuid) && !isClauseWorkSpace) {
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
	 * Check for cycle conditions.
	 *
	 * @param subTreeName the sub tree name
	 * @param uuid the uuid
	 * @param currentSelectedSubTreeUuid the current selected sub tree uuid
	 * @return true, if successful
	 */
	private static boolean checkForCycleConditions(String subTreeName, String uuid, String currentSelectedSubTreeUuid) {
		Node node = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(subTreeName + "~" + uuid);
		
		return checkForCycleConditions(node,currentSelectedSubTreeUuid);
	}
	
	/**
	 * Check for cycle conditions.
	 *
	 * @param node the node
	 * @param currentSelectedSubTreeUuid the current selected sub tree uuid
	 * @return true, if successful
	 */
	private static boolean checkForCycleConditions(Node node,
			String currentSelectedSubTreeUuid) {
		System.out.println("node.hasChildNodes():"  + node.hasChildNodes());
		if (node.hasChildNodes()) {
			NodeList childNodeList = node.getChildNodes();
			System.out.println("childNodeList.getLength():"+childNodeList.getLength());
			for(int i=0;i<childNodeList.getLength();i++){
				Node childNode = childNodeList.item(i);
				System.out.println("childNode: node type:"+childNode.getNodeName());
				if("subTreeRef".equals(childNode.getNodeName())){
					String uuid = childNode.getAttributes().getNamedItem("id").getNodeValue();
					String nodeValue = PopulationWorkSpaceConstants.subTreeLookUpName.get(uuid);
					childNode.getAttributes().getNamedItem("displayName").setNodeValue(nodeValue);
					String displayName = childNode.getAttributes().getNamedItem("displayName").getNodeValue();
					System.out.println("subtree name:"+displayName);
					System.out.println("subtree uuid:"+uuid);
					//					String uuid = childNode.getAttributes().getNamedItem("id").getNodeValue();
					Node subTreeNode = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(displayName + "~" + uuid);
					NamedNodeMap namedNodeMap = subTreeNode.getAttributes();
					if (namedNodeMap.getNamedItem("instance") != null) { // filter Occurrences of Selected Clause.
						String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
						if (currentSelectedSubTreeUuid.equalsIgnoreCase(instanceOfUUID)) {
							return false;
						} else {
							String parentDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
							parentDisplayName = parentDisplayName.replace("Occurrence "
									+ namedNodeMap.getNamedItem("instance").getNodeValue() + " of " , "");
							Node subTreeParentNode = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(parentDisplayName + "~" + instanceOfUUID);
							boolean isInnerSubTreeCycle = checkForCycleConditions(subTreeParentNode, currentSelectedSubTreeUuid);
							if(!isInnerSubTreeCycle){
								return false;
							}
						}
					} else if(uuid.equals(currentSelectedSubTreeUuid)){
						return false;
					}else{
						Node childSubTreeNode = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(displayName+"~"+uuid);
						boolean isInnerSubTreeCycle = checkForCycleConditions(childSubTreeNode, currentSelectedSubTreeUuid);
						if(!isInnerSubTreeCycle){
							return false;
						}
					}
				}else{
					boolean isInnerSubTreeCycle = checkForCycleConditions(childNode, currentSelectedSubTreeUuid);
					if(!isInnerSubTreeCycle){
						return false;
					}
				}
			}
		} else {
			
			NamedNodeMap namedNodeMap = node.getAttributes();
			if (namedNodeMap.getNamedItem("instance") != null) { // filter Occurrences of Selected Clause.
				String instanceOfUUID = namedNodeMap.getNamedItem("instanceOf").getNodeValue();
				if (currentSelectedSubTreeUuid.equalsIgnoreCase(instanceOfUUID)) {
					return false;
				} else {
					String parentDisplayName = namedNodeMap.getNamedItem("displayName").getNodeValue();
					parentDisplayName = parentDisplayName.replace("Occurrence "
							+ namedNodeMap.getNamedItem("instance").getNodeValue() + " of " , "");
					Node subTreeParentNode = PopulationWorkSpaceConstants.getSubTreeLookUpNode().get(parentDisplayName + "~" + instanceOfUUID);
					boolean isInnerSubTreeCycle = checkForCycleConditions(subTreeParentNode, currentSelectedSubTreeUuid);
					if(!isInnerSubTreeCycle){
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * Creates the suggest oracle.
	 * 
	 * @return the multi word suggest oracle
	 */
	private static MultiWordSuggestOracle createSuggestOracle() {
		MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
		multiWordSuggestOracle.addAll(PopulationWorkSpaceConstants.getSubTreeLookUpName()
				.values());
		return multiWordSuggestOracle;
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
		SubTreeDialogBox.isSelected = isSelected;
	}
	
}
