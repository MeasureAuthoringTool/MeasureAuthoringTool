package mat.client.clause.clauseworkspace.view;


import java.util.Iterator;

import mat.client.clause.clauseworkspace.presenter.ClauseWorkspacePresenter.XmlTreeDisplay;
import mat.client.shared.LabelBuilder;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
	
	
	
	/*
	 * selection indicator for the text view diagram tree
	 */
	private final String INDICATOR = "\u25ba";
	
	
	public XmlTree() {
		super();
	}

	
	public XmlTree(Tree xmltree) {		
		this.tree = xmltree;
		mainPanel.setStyleName("div-wrapper");
		
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.setStyleName("div-first bottomPadding10px");
		
		SimplePanel rightPanel = new SimplePanel();
		rightPanel.setStyleName("div-second");
		
		HorizontalPanel treePanel =  new HorizontalPanel();
		HTML space = new HTML("&nbsp;");
		treePanel.add(space);

		treePanel.add(tree);
		leftPanel.add(treePanel);
		
		VerticalPanel buttonPanel =  new VerticalPanel();
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
		bottomSavePanel.add(saveBtn);
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);		
	
//		initWidget(mainPanel);
		
		tree.addSelectionHandler(new TreeSelectionHandler());
		saveBtn.setEnabled(tree.getItemCount() > 0 ? true :false);
		
		createNodeBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveBtn.setEnabled(true);
				if(nodeTex.getValue().trim().length() > 0 && !nodeTex.getValue().trim().equals("attributes")){
					if(tree.getItemCount() > 0){
						treeItem = tree.getSelectedItem();					
						Label label = new Label(nodeTex.getValue());
						treeItem.addItem(label);		
						treeItem.setSelected(false);
						TreeItem  childItem = treeItem.getChild(treeItem.getChildCount()-1);
						selectDiagramTreeItem(childItem);
					}else{
						treeItem = new TreeItem(new Label(nodeTex.getValue()));
						tree.addItem(treeItem);		
						selectDiagramTreeItem(treeItem);
					}
				}
			}
		});
		
		createAttrBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(attrName.getValue().trim().length() > 0 && attrValue.getValue().trim().length() > 0){
					TreeItem treeItem = tree.getSelectedItem();
					Label label = new Label(attrName.getValue()+" = " + attrValue.getValue());
					if(treeItem != null){
						if(trimSelectionIndicator(treeItem.getText()).equals("attributes")){
							treeItem.addItem(label);
							selectDiagramTreeItem(treeItem);
						}else{
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
								TreeItem attr = new TreeItem(new Label("attributes"));
								attr.addItem(label);								
								treeItem.addItem(attr);
								selectDiagramTreeItem(attr);
							}
						}
					}
				}
			}
		});
		
		removeNode.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				treeItem = tree.getSelectedItem();
				TreeItem parentItem = treeItem.getParentItem();
				treeItem.remove();
				if(parentItem != null){
					selectDiagramTreeItem(parentItem);
				}else{
					saveBtn.setEnabled(false);
				}
			}
		});
		
		editNode.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				TreeItem item = tree.getSelectedItem();
				if(item.getParentItem() != null && item.getParentItem().getText().equals("attributes")){
					if(attrName.getValue().trim().length() > 0 || attrValue.getValue().trim().length() > 0){
						TreeItem treeItem = tree.getSelectedItem();
						if(treeItem != null){
							treeItem.setWidget(new Label(attrName.getValue()+" = "+attrValue.getValue()));
							selectDiagramTreeItem(treeItem);
						}
					}
				}else if(nodeTex.getValue().trim().length() > 0){
					TreeItem treeItem = tree.getSelectedItem();
					if(treeItem != null){
						treeItem.setWidget(new Label(nodeTex.getText()));
						selectDiagramTreeItem(treeItem);
					}
				}
				
			}
		});
	}

	private void selectDiagramTreeItem(TreeItem item) {
		tree.setFocus(true);
		item.setVisible(true);
		item.setState(true);
//		expandDiagramTree();
		item.getTree().setSelectedItem(item, true);
		tree.ensureSelectedItemVisible();
	}
	
	private void expandDiagramTree() {
		for (Iterator<TreeItem> treeItemIterator = tree.treeItemIterator(); treeItemIterator.hasNext(); ) {
			TreeItem next = treeItemIterator.next();
			next.setState(true);
			
		}
	}
	
	class TreeSelectionHandler implements SelectionHandler<TreeItem>{
		
		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			
			TreeItem item = event.getSelectedItem();			
			resetTreeItemSelectionIndicators(getRoot(item));
			item.setText(addSelectionIndicator(item.getText()));
			nodeTex.setValue(trimSelectionIndicator(item.getText()));
			createNodeBtn.setEnabled(true);
			editNode.setEnabled(true);
			attrName.setValue("");
			attrValue.setValue("");
			createNodeBtn.setEnabled(true);
			createAttrBtn.setEnabled(true);
			if(nodeTex.getValue().equals("attributes")){
				editNode.setEnabled(false);
				createNodeBtn.setEnabled(false);
				nodeTex.setValue("");
			}
			if((item.getParentItem() != null && item.getParentItem().getText().equals("attributes"))){
				createNodeBtn.setEnabled(false);
				createAttrBtn.setEnabled(false);
				String[] attrs = nodeTex.getValue().split("=");
				attrName.setText(attrs[0].trim());
				attrValue.setText(attrs[1].trim());
				nodeTex.setValue("");
			}
		}
		
		private TreeItem getRoot(TreeItem item){
			while(item.getParentItem() != null)
				item = item.getParentItem();
			return item;
		}
		
		private void resetTreeItemSelectionIndicators(TreeItem root){
			if(root.getText().startsWith(INDICATOR)){
				root.setText(trimSelectionIndicator(root.getText()));
			}
			for(int i = 0; i < root.getChildCount(); i++){
				resetTreeItemSelectionIndicators(root.getChild(i));
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
}
