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
import com.google.gwt.xml.client.Node;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class QDMDialogBox {
	
	private static final String ATTRIBUTES = "attributes";
	
	private static boolean isSelected;
	
	public static void showQDMDialogBox(final XmlTreeDisplay xmlTreeDisplay,
			boolean isAdd) {
		final DialogBox dialogBox = new DialogBox(false, true);
		dialogBox.setGlassEnabled(true);
		dialogBox.getElement().setId("dialogBox_DialogBox");
		dialogBox.setAnimationEnabled(true);
		setSelected(false);
		dialogBox.setText("Double Click to Select QDM Element.");
		dialogBox.setTitle("Double Click to Select QDM Element.");
		
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("20em");
		dialogContents.setHeight("15em");
		dialogContents.setSpacing(8);
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		dialogBox.setWidget(dialogContents);
		
		// Create Search box
		final SuggestBox suggestBox = new SuggestBox(createSuggestOracle());
		suggestBox.setWidth("18em");
		suggestBox.setText("Search");
		suggestBox.getElement().setId("suggestBox_SuggestBox");
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
		listBox.getElement().setAttribute("id", "QDMPopUpListBox");
		listBox.setWidth("18em");
		listBox.setVisibleItemCount(10);
		String currentSelectedQDMUuid = xmlTreeDisplay.getSelectedNode()
				.getUUID();
		addQDMNamesToListBox(listBox, currentSelectedQDMUuid);
		
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
		horizontalButtonPanel.getElement().setId("horizontalButtonPanel_HorizontalPanel");
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
				if(!isSelected){
					String value = listBox.getItemText(listBox.getSelectedIndex());
					String uuid = listBox.getValue(listBox.getSelectedIndex());
					if (isAdd) {
						xmlTreeDisplay.addNode(value, value, uuid,
								CellTreeNode.ELEMENT_REF_NODE);
					} else {
						@SuppressWarnings("unchecked")
						List<CellTreeNode> attributeList = (List<CellTreeNode>) xmlTreeDisplay
								.getSelectedNode().getExtraInformation(ATTRIBUTES);
						if (attributeList != null) {
							attributeList.clear();
						}
						xmlTreeDisplay.editNode(value, value, uuid);
					}
					xmlTreeDisplay.setDirty(true);
					setSelected(true);
				}
				dialogBox.hide();
			}
		});
	}
	
	private static void addSuggestHandler(final SuggestBox suggestBox,
			final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem()
						.getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedQDMName.equals(listBox.getItemText(i))) {
						listBox.setItemSelected(i, true);
						break;
					}
				}
			}
		});
	}
	

	private static void addQDMNamesToListBox(ListBox listBox,
			String currentSelectedQDMUuid) {
		Set<Entry<String, Node>> elementLookUpNodes = PopulationWorkSpaceConstants
				.getElementLookUpNode().entrySet();
		for (Entry<String, Node> elementLookup : elementLookUpNodes) {
			Node node = elementLookup.getValue();
			
			if (!isDataTypeAttrib(node)) {
				String key = elementLookup.getKey();
				String uuid = key.substring(key.lastIndexOf("~") + 1);
				String item = PopulationWorkSpaceConstants.getElementLookUpName().get(uuid);
				listBox.addItem(item, uuid);
				if (uuid.equals(currentSelectedQDMUuid)) {
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
			String uuid = options.getItem(i).getAttribute("value");
			
			String oid = "";
			if (PopulationWorkSpaceConstants.getElementLookUpNode().get(text + "~" + uuid) != null) {
				oid = PopulationWorkSpaceConstants.getElementLookUpNode()
						.get(text + "~" + uuid).getAttributes()
						.getNamedItem("oid").getNodeValue();
			}
			String title = text + " (" + oid + ")";
			OptionElement optionElement = options.getItem(i);
			optionElement.setTitle(title);
		}
	}
	
	/**
	 * This method will check for data type of the node to be "Attribute" or
	 * "Timing Element". If yes, return true, else return false.
	 */
	private static boolean isDataTypeAttrib(Node node) {
		boolean returnType = false;
		String nodeDataType = node.getAttributes()
				.getNamedItem(QDMAttributeDialogBox.DATATYPE).getNodeValue();
		if (QDMAttributeDialogBox.ATTRIBUTE.equalsIgnoreCase(nodeDataType)) {
			returnType = true;
		}
		
		return returnType;
	}

	private static MultiWordSuggestOracle createSuggestOracle() {
		MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
		multiWordSuggestOracle.addAll(PopulationWorkSpaceConstants.getElementLookUpName()
				.values());
		return multiWordSuggestOracle;
	}
	
	public static boolean isSelected() {
		return isSelected;
	}
	
	public static void setSelected(boolean isSelected) {
		QDMDialogBox.isSelected = isSelected;
	}
}
