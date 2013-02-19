package mat.client.clause.clauseworkspace.view;


import java.util.Iterator;

import mat.client.clause.clauseworkspace.presenter.ClauseWorkspacePresenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author skarunakaran
 *
 */
public class XmlTree implements XmlTreeDisplay {
	
	private FlowPanel  mainPanel = new FlowPanel(); 
	
	private Tree tree;
	
	private TreeItem treeItem;
	
	private Button saveBtn = new Button("Save");
	
	private Button createNodeBtn = new Button("Submit");
	
	private Button createAttrBtn = new Button("Submit");
	
	private Button removeNode = new Button("remove");
	
	private Button editNode = new Button("Edit");
	
	private TextBox nodeTex = new TextBox();
	
	private TextBox attrName = new TextBox();
	
	private TextBox attrValue = new TextBox();
	
	private TreeItem previousSelectedItem;
	
	private Button expand = new Button("+");
	
	private Button collapse = new Button("-");
	
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
	
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	boolean enabled;
	
	
	/*
	 * selection indicator for the text view diagram tree
	 */
	private final String INDICATOR = "\u25ba";
	
	
	public XmlTree() {
		super();
	}

	
	public XmlTree(Tree xmltree) {		
		clearMessages();
		this.tree = xmltree;
		mainPanel.setStyleName("div-wrapper");//main div
		
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.setStyleName("div-first bottomPadding10px");//left side div which will  have tree
		
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.setStyleName("div-second");//right div having tree creation inputs.
		
		ScrollPanel scrollPanel =  new ScrollPanel();//tree rendered in scroll panel
		VerticalPanel treePanel =  new VerticalPanel();
		expand.setSize("25px", "25px");
		collapse.setSize("25px", "25px");
		collapse.setVisible(false);
		treePanel.add(expand);
		treePanel.add(collapse);
		treePanel.add(tree);
		scrollPanel.add(treePanel);		
		leftPanel.add(scrollPanel);
		
		VerticalPanel buttonPanel =  new VerticalPanel();// has all inputs and buttons which is in right div
		buttonPanel.add(new HTML("</br>"));
		Label addNode = new Label("Add Node");
		addNode.setStyleName("leftAligned leftpadding4px bottomPadding10px labelStyling");
		buttonPanel.add(addNode);
		
		HorizontalPanel nodePanel = new HorizontalPanel();
		nodePanel.setSize("60%", "100%");
		nodePanel.setSpacing(2);
		nodeTex.setWidth("100px");
		nodeTex.setHeight("20px");
		nodePanel.add(nodeTex);
		
		nodePanel.add(createNodeBtn);
		buttonPanel.add(nodePanel);
		
		buttonPanel.add(new HTML("</br>"));
		HorizontalPanel attrPanel = new HorizontalPanel();
		attrPanel.setSpacing(3);
		Label addAttribute = new Label("Add Attribute");
		addAttribute.setStyleName("leftAligned leftpadding4px bottomPadding10px labelStyling");
		buttonPanel.add(addAttribute);
		
		attrPanel.add(LabelBuilder.buildRequiredLabel(new HTML(), "Name"));
		attrPanel.add(attrName);
		attrName.setSize("50px", "20px");
		
		attrPanel.add(LabelBuilder.buildRequiredLabel(new HTML(), "Value"));
		attrPanel.add(attrValue);
		attrValue.setSize("90px", "20px");
		
		attrPanel.add(createAttrBtn);
		buttonPanel.add(attrPanel);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		
		horizontalPanel.add(editNode);			
		horizontalPanel.add(removeNode);		
		buttonPanel.add(horizontalPanel);
		
		rightPanel.add(buttonPanel);
		
		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.setStyleName("div-first");
		VerticalPanel savePanel = new VerticalPanel();
		savePanel.add(errorMessageDisplay);
		savePanel.add(successMessageDisplay);
		savePanel.add(saveBtn);
		
		bottomSavePanel.add(savePanel);
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);		
	
		tree.addSelectionHandler(new TreeSelectionHandler());		
		addhandlers();
		
	}

	
	/**
	 * has click handlers
	 */
	private void addhandlers() {
		createNodeBtn.addClickHandler(new ClickHandler() {// create node button on click 
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(nodeTex.getValue().trim().length() > 0 && !nodeTex.getValue().trim().equals("attributes")){// checking if input text is not null and has some value
					if(tree.getItemCount() > 0){// if tree already having children, then add item below the selected child item.
						treeItem = tree.getSelectedItem();					
						treeItem.addItem(nodeTex.getValue());		
						treeItem.setSelected(false);
						TreeItem  childItem = treeItem.getChild(treeItem.getChildCount()-1);
						selectDiagramTreeItem(childItem);
					}else{// first time adding the child to the main Tree
						treeItem = new TreeItem(new Label(nodeTex.getValue()));
						tree.addItem(treeItem);		
						selectDiagramTreeItem(treeItem);
					}
				}
			}
		});
		
		createAttrBtn.addClickHandler(new ClickHandler() {// atrribute creation
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(attrName.getValue().trim().length() > 0 && attrValue.getValue().trim().length() > 0){// if name and value not null
					TreeItem treeItem = tree.getSelectedItem();
					String label = attrName.getValue()+" = " + attrValue.getValue();// concat attributes name and value with "=" symbol 
					if(treeItem != null){
						if(trimSelectionIndicator(treeItem.getText()).equals("attributes")){// if selected child item was "attributes" then add item below this child
							treeItem.addItem(label);
							selectDiagramTreeItem(treeItem);
						}else{//if iterate the selected tree item and check if there is an atrribute item, if yes then add the attribute values below this node, else create a new treeItem "attributes" and add the values below this
							boolean isAttrfound = false;
							int chCnt = treeItem.getChildCount();
							if(chCnt > 0){
								for (int i = 0; i < chCnt; i++) {
									if(treeItem.getChild(i).getText().equals("attributes")){
										isAttrfound = true;
										treeItem.getChild(i).addItem(label);
										selectDiagramTreeItem(treeItem.getChild(i));
										break;
									}
								}
							}
							if(!isAttrfound){
								TreeItem attr = new TreeItem("attributes");
								attr.addItem(label);								
								treeItem.addItem(attr);
								selectDiagramTreeItem(attr);
							}
						}
					}
				}
			}
		});
		
		removeNode.addClickHandler(new ClickHandler() {// romove button on click
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				treeItem = tree.getSelectedItem();// get the selected item 
				TreeItem parentItem = treeItem.getParentItem();
				treeItem.remove();// remove
				if(parentItem  != null && parentItem.getText().equalsIgnoreCase("attributes") 
						&& parentItem.getChildCount() == 0){
					TreeItem parentParentItem = parentItem.getParentItem();
					parentItem.remove();
					parentItem = parentParentItem;
				}
				if(parentItem != null){
					selectDiagramTreeItem(parentItem);// change the selected node to parent node of removed node.
				}
			}
		});
		
		editNode.addClickHandler(new ClickHandler() {// Edit button on click
			
			@Override
			public void onClick(ClickEvent event) {	
				clearMessages();
				TreeItem item = tree.getSelectedItem();
				previousSelectedItem = item;
				if(item != null){
					if(item.getParentItem() != null && item.getParentItem().getText().equals("attributes")){// if selected item is attribute name value
						if(attrName.getValue().trim().length() > 0 || attrValue.getValue().trim().length() > 0){
							TreeItem treeItem = tree.getSelectedItem();
							if(treeItem != null){
								treeItem.setText(attrName.getValue()+" = "+attrValue.getValue());
								selectDiagramTreeItem(treeItem);
							}
						}
					}else if(nodeTex.getValue().trim().length() > 0){// if selected item is child item
						TreeItem treeItem = tree.getSelectedItem();
						if(treeItem != null){
							treeItem.setText(nodeTex.getText());
							selectDiagramTreeItem(treeItem);
						}
					}
				}
			}
		});
		
		expand.addClickHandler(new ClickHandler() {// when expand "+" button clicked
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				collapse.setVisible(true);
				expand.setVisible(false);
				expandDiagramTree();	
			}
		});
		
		collapse.addClickHandler(new ClickHandler() {// when collapse "-" button clicked
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				collapse.setVisible(false);
				expand.setVisible(true);
				collapseDiagramTree();	
			}
		});
	}


	private void selectDiagramTreeItem(TreeItem item) {// selection style of a Tree Item
		tree.setFocus(true);
		item.setVisible(true);
		item.setState(true);
		item.getTree().setSelectedItem(item, true);
		tree.ensureSelectedItemVisible();
	}
	
	private void expandDiagramTree() {
		for (Iterator<TreeItem> treeItemIterator = tree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			next.setState(true);
		}
	}
	
	private void collapseDiagramTree() {
		for (Iterator<TreeItem> treeItemIterator = tree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			next.setState(false);
		}
	}

	
	class TreeSelectionHandler implements SelectionHandler<TreeItem>{
		
		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {// on selection of Tree Item
			clearMessages();
			TreeItem item = event.getSelectedItem();	
			resetTreeItemSelectionIndicators();// removing the selection indicator from the previous selected item
			item.setText(addSelectionIndicator(item.getText())); // adding the selection indicator to the current selected item
			nodeTex.setValue(trimSelectionIndicator(item.getText()));// the text box value is updated with the selected item value without the selection indicator 
			attrName.setValue("");
			attrValue.setValue("");
			if(enabled){
				createNodeBtn.setEnabled(true);
				editNode.setEnabled(true);
				createNodeBtn.setEnabled(true);
				createAttrBtn.setEnabled(true);	
			}
			
			if(nodeTex.getValue().equals("attributes")){// not allowing the user to update the "attributes" tree item				
				editNode.setEnabled(false);
				createNodeBtn.setEnabled(false);
				nodeTex.setValue("");
			}
			if((item.getParentItem() != null && item.getParentItem().getText().equals("attributes"))){// if attribute value selected, splitting the values and setting it to the textboxes 
				createNodeBtn.setEnabled(false);
				createAttrBtn.setEnabled(false);
				String[] attrs = nodeTex.getValue().split("=");
				attrName.setText(attrs[0].trim());
				attrValue.setText(attrs[1].trim());
				nodeTex.setValue("");
			}
			previousSelectedItem = item;
		}
		
		private void resetTreeItemSelectionIndicators(){
			if(previousSelectedItem != null){
				previousSelectedItem.setText(trimSelectionIndicator(previousSelectedItem.getText()));
			}
		} 
		
	}
	
	/**
	 * remove the selection indicator if it exists and then trim
	 * @param str
	 * @return
	 */
	private String trimSelectionIndicator(String str){
		return str.replaceFirst(INDICATOR, "").trim();
	}
	
	private String addSelectionIndicator(String str){
		return INDICATOR+" "+str;
	}
	
	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	@Override
	public Tree getXmlTree() {
		return tree;
	}


	@Override
	public Button getSaveButton() {
		return saveBtn;
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}


	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}


	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessageDisplay;
	}


	@Override
	public void clearMessages() {
		successMessageDisplay.clear();
		errorMessageDisplay.clear();
		
	}


	@Override
	public void setEnabled(boolean enabled) {
		nodeTex.setEnabled(enabled);
		createAttrBtn.setEnabled(enabled);
		createNodeBtn.setEnabled(enabled);
		attrName.setEnabled(enabled);
		attrValue.setEnabled(enabled);
		editNode.setEnabled(enabled);
		removeNode.setEnabled(enabled);
		saveBtn.setEnabled(enabled);
		this.enabled = enabled;
	}
}
