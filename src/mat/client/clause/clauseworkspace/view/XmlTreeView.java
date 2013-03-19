package mat.client.clause.clauseworkspace.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.clause.clauseworkspace.model.TreeModel;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkspacePresenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class XmlTreeView implements XmlTreeDisplay, TreeViewModel{
	
	private FlowPanel  mainPanel = new FlowPanel();
	
	CellTree cellTree;
	
	private Button saveBtn = new Button("Save");
	
	private Button createNodeBtn = new Button("Submit");
	
	private Button createAttrBtn = new Button("Submit");
	
	private Button removeNode = new Button("remove");
	
	private Button editNode = new Button("Edit");
	
	private TextBox nodeTex = new TextBox();
	
	private TextBox attrName = new TextBox();
	
	private TextBox attrValue = new TextBox();
	
	private Button expand = new Button("+");
	
	private Button collapse = new Button("-");
	
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
	
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	boolean enabled;
	
	private ListDataProvider<TreeModel> nodeDataProvider;
	
	private Map<TreeModel, ListDataProvider<TreeModel>> mapDataProvider = new HashMap<TreeModel, ListDataProvider<TreeModel>>();
	
	TreeModel selectedNode;
	
	final SingleSelectionModel<TreeModel> selectionModel = new SingleSelectionModel<TreeModel>();
	
	private Map<TreeModel, Boolean> nodeOpenMap = new HashMap<TreeModel, Boolean>();
	
	
	public XmlTreeView(TreeModel treeModel) {
		clearMessages();
		createRootNode(treeModel);
		addHandlers();
	}


	/**
	 * Creates the Root Node in the CellTree.
	 * @param treeModel
	 */
	private void createRootNode(TreeModel treeModel) {
		nodeDataProvider = new ListDataProvider<TreeModel>(treeModel.getChilds());
		mapDataProvider.put(treeModel, nodeDataProvider);
	}


	/**
	 * Page widgets
	 * @param cellTree
	 */
	public void createPageView(CellTree cellTree) {
		this.cellTree = cellTree;
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
		treePanel.add(cellTree);
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
		
		cellTreeHandlers();
	}
	
	/**
	 * Selection Handler, Tree Open and Close Handlers Defined
	 */
	private void cellTreeHandlers() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedNode = selectionModel.getSelectedObject();
				createAttrBtn.setEnabled(true);
				createNodeBtn.setEnabled(true);
				editNode.setEnabled(true);
				removeNode.setEnabled(true);
				clearMessages();
				if(selectedNode != null){
					
					if(selectedNode.getName().equals("attributes")){// if selected node is Attribute, disable Editing or creating child Element Node for this
						editNode.setEnabled(false);
						createNodeBtn.setEnabled(false);
					}
					
					if(selectedNode.getParent().getParent() == null){// if Root node is selected disable remove button.
						removeNode.setEnabled(false);
					}else if(selectedNode.getParent().getName().equals("attributes")){// if selected value is a attribute value, set the text boxes with the value
						attrName.setText(selectedNode.getName().split("=")[0]);
						attrValue.setText(selectedNode.getName().split("=")[1]);
						nodeTex.setText(null);
						createAttrBtn.setEnabled(false);
						createNodeBtn.setEnabled(false);
					}else{// set the nodeText text box with the selected Tree node.
						nodeTex.setText(selectedNode.getName());
						attrName.setText(null);
						attrValue.setText(null);
					}
				}
			}
		});
		
		this.cellTree.addOpenHandler(new OpenHandler<TreeNode>() {
			
			@Override
			public void onOpen(OpenEvent<TreeNode> event) {

				TreeModel node = (TreeModel)event.getTarget().getValue();
				nodeOpenMap.put((TreeModel) node, true);// map holds the node object key with boolean value as true. 
				clearMessages();
			}
		});
		
		this.cellTree.addCloseHandler(new CloseHandler<TreeNode>() {
			@Override
			public void onClose(CloseEvent<TreeNode> event) {
				TreeModel node = (TreeModel)event.getTarget().getValue();
				nodeOpenMap.put((TreeModel) node, false);// map holds the node object key with boolean value as false. 
				clearMessages();
			}
		});
	}


	/**
	 * Closing all nodes in the CellTree
	 * @param treeNode
	 * @param fireEvents
	 */
	private void closeNodes(TreeNode treeNode, boolean fireEvents) {
		if(treeNode != null){
	      	 for (int i = 0; i < treeNode.getChildCount(); i++) {
	      		 TreeNode subTree = treeNode.setChildOpen(i, false, fireEvents);  
	      		 if (subTree != null && subTree.getChildCount() > 0){
	      			 closeNodes(subTree, fireEvents);
	      		 }
		     }  
	    }
	}
	
	/**
	 * Open nodes based on the nodeOpenMap values.
	 * @param treeNode
	 */
	private void openNodes(TreeNode treeNode){
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeModel node = (TreeModel)treeNode.getChildValue(i);
	      		TreeNode subTree = treeNode.setChildOpen(i, nodeOpenMap.get(node) != null  && nodeOpenMap.get(node) ? true : false );  
	      		if (subTree != null && subTree.getChildCount() > 0){
	      			openNodes(subTree);
	      	    }
	      	}  
	    }
	}
	
	
	/**
	 * Opens all nodes.
	 * @param treeNode
	 */
	private void openAllNodes(TreeNode treeNode){		
		if(treeNode != null){
			getNodeInfo(treeNode.getValue());
	      	  for (int i = 0; i < treeNode.getChildCount(); i++) {
	      			  TreeNode subTree = treeNode.setChildOpen(i, true);	      			
	      			 if (subTree != null && subTree.getChildCount() > 0){
	      					  openAllNodes(subTree);
	      			 }
		          }  
	        }
	}
	
	
	
	/**
	 * Click Handlers
	 */
	private void addHandlers(){
		
		createNodeBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(selectedNode != null &&  nodeTex.getValue() != null && nodeTex.getValue().trim().length() > 0){//if nodeTex textbox is not empty
				 	TreeModel child = new TreeModel(nodeTex.getValue()); //Create a TreeModel child Object
                    addChild(selectedNode, child);
                    nodeOpenMap.put(selectedNode, true);
	                closeNodes(cellTree.getRootTreeNode(), false);
	                openNodes(cellTree.getRootTreeNode());
					selectionModel.setSelected(selectedNode, true);
				}
			}

		});
		
		removeNode.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(selectedNode != null){
					ListDataProvider<TreeModel> dataprovider = mapDataProvider.get(selectedNode);
                    dataprovider.getList().remove(selectedNode);// remove the selected Object from ListDataProvider and Refresh
                    dataprovider.refresh();
                    dataprovider.flush();
                    closeNodes(cellTree.getRootTreeNode(), false);
                    openNodes(cellTree.getRootTreeNode());
					selectionModel.setSelected(selectedNode.getParent(), true);
				}
				
			}
		});
		
		editNode.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(selectedNode != null &&  nodeTex.getValue() != null && nodeTex.getValue().trim().length() > 0){
                     selectedNode.setName(nodeTex.getValue());
				}
				 closeNodes(cellTree.getRootTreeNode(), false);
				 openNodes(cellTree.getRootTreeNode());
				 nodeDataProvider.refresh();
				 selectionModel.setSelected(selectedNode, true);
				 
			}
		});
		
		createAttrBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				if(selectedNode != null && attrName.getValue() != null
						&& attrName.getValue().trim().length() > 0 && attrValue.getValue().trim().length() > 0){//if the Attribute name and Attribute value text box is not empty
					TreeModel child = new TreeModel(attrName.getValue() + " = " + attrValue.getValue());// creating TreeModel Object with name and value concatenated with "="
					if(selectedNode.getName().equals("attributes")){// if the selected node name is attributes, add child directly to it
						addChild(selectedNode, child);
					}else if(selectedNode.getChilds() == null || selectedNode.getChilds().size() == 0 ||
							!(selectedNode.getChilds().get(0).getName().equals("attributes"))){// if selected node is not having any childs or having other childs which are not Attributes
						List<TreeModel> treeChild = null;
						if(selectedNode.getChilds() == null){
							treeChild = new ArrayList<TreeModel>();
						}else{
							treeChild = selectedNode.getChilds();
						}
						TreeModel attr = new TreeModel("attributes");// create a child node named Attribute under the selected node
						treeChild.add(0, attr);//setting attribute node as the first node.
						selectedNode.setChilds(treeChild);
						attr.setParent(selectedNode);
						addChild(attr, child);
						nodeOpenMap.put(attr, true);
					}else if(selectedNode.getChilds().get(0).getName().equals("attributes")){// if selected node has Attribute node
						addChild(selectedNode.getChilds().get(0), child);
					}
					 nodeOpenMap.put(selectedNode, true);
					 nodeOpenMap.put(selectedNode.getChilds().get(0), true);// opening Attribute node
					 closeNodes(cellTree.getRootTreeNode(), false);
					 openNodes(cellTree.getRootTreeNode());
					 
				}
			}
		});
		
		expand.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				openAllNodes(cellTree.getRootTreeNode());
				expand.setVisible(false);
				collapse.setVisible(true);
			}
		});
		
		
		collapse.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				closeNodes(cellTree.getRootTreeNode(), true);
				expand.setVisible(true);
				collapse.setVisible(false);
			}
		});
	}
	
	
	/**
	 * Adding child object to the list and setting it to the Parent object
	 * @param parent
	 * @param child
	 */
	private void addChild(TreeModel parent, TreeModel child){
		if(parent.getChilds() == null){// if parent has no child
        	ArrayList<TreeModel> treeChild = new ArrayList<TreeModel>();//create child list
        	treeChild.add(child);//add child
        	parent.setChilds(treeChild);//set child list to parent
        	child.setParent(parent);// set parent to child
	     }else{// if parent has children
        	parent.getChilds().add(child);// add child to the existing childern
        	child.setParent(parent);//set parent to child
	     }
	}
	
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
        if (value == null) { 
        	NodeCell nodeCell = new NodeCell();
            return new DefaultNodeInfo<TreeModel>(nodeDataProvider, nodeCell, selectionModel, null);
        } else {
        	TreeModel myValue = (TreeModel) value;
            ListDataProvider<TreeModel> dataProvider = new ListDataProvider<TreeModel>(myValue.getChilds());
            NodeCell nodeCell = new NodeCell();
                for(TreeModel currentNode : myValue.getChilds()){
                        mapDataProvider.put(currentNode, dataProvider);
                }
            return new DefaultNodeInfo<TreeModel>(dataProvider, nodeCell, selectionModel, null);
        }
	}

	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof TreeModel) {
			TreeModel t = (TreeModel) value;
            if (!t.hasChildrens()){
                return true;
            }
        }
		return false;
	}

	
	 public class NodeCell extends AbstractCell<TreeModel> {
         public NodeCell() {
           super(BrowserEvents.CONTEXTMENU);
           
         }
         @Override
         public void render(Context context, TreeModel value, SafeHtmlBuilder sb) {
           if (value == null) {
             return;
           }
                        
           sb.appendEscaped(value.getName());
         }
        
        /* @Override
         public void onBrowserEvent(Context context, Element parent, TreeModel value,
        	      NativeEvent event, ValueUpdater<TreeModel> valueUpdater) {
     		if(event.getType().equals(BrowserEvents.CONTEXTMENU)){
     			 event.preventDefault();
     			Window.alert("Right click happened at:"+value.getName());
     		}else{
     			super.onBrowserEvent(context, parent, value, event, valueUpdater);
     		}

     	}*/

 } 

	@Override
	public CellTree getXmlTree() {
		return cellTree;
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


	public CellTree getCellTree() {
		return cellTree;
	}


	public void setCellTree(CellTree cellTree) {
		this.cellTree = cellTree;
	}
}
