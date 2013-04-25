package mat.client.clause.clauseworkspace.view;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;

public class ClauseWorkspaceContextMenu {
	
	interface Template extends SafeHtmlTemplates {
	    @Template("<table width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}</td></tr></table>")
	    SafeHtml menuTable(String name, String shortCut);
	}
	
	private static final Template template = GWT.create(Template.class);

	XmlTreeDisplay xmlTreeDisplay;

	MenuItem addMenu;
	
	MenuItem addMenuLHS;
	
	MenuItem addMenuRHS;

	MenuItem copyMenu;

	MenuItem pasteMenu;

	MenuItem deleteMenu;
	
	MenuItem cutMenu;
	
	MenuItem editMenu;
	
	MenuBar popupMenuBar = new MenuBar(true);
	
	MenuBar subMenuBar;
	
	MenuItemSeparator separator = new MenuItemSeparator();

	PopupPanel popupPanel;
	
	Command pasteCmd;
	
	public ClauseWorkspaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		this.xmlTreeDisplay = treeDisplay;
		this.popupPanel = popPanel;
		Command copyCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.copy();
			}
		};
		copyMenu = new MenuItem(template.menuTable("Copy", "Ctrl+C"), copyCmd);
		
		Command deleteCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.removeNode();
			}
		};
		deleteMenu = new MenuItem(template.menuTable("Delete", "Delete"), deleteCmd);
		Command cutCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.copy();
				xmlTreeDisplay.removeNode();
			}
		};
		cutMenu = new MenuItem(template.menuTable("Cut", "Ctrl+X"), cutCmd);
		
		Command pasteCmd = new Command() {
			public void execute() {
				popupPanel.hide();
				if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.ROOT_NODE){
					pasteRootNodeTypeItem();
				}else{
					xmlTreeDisplay.paste();	
				}
			}
		};
		pasteMenu = new MenuItem(template.menuTable("Paste", "Ctrl+V"), pasteCmd);
	}


	/**
	 * Method displays the rightClick options based on the nodeType of the node selected on CellTree
	 * @param popupPanel
	 */
	public void displayMenuItems( final PopupPanel popupPanel){
		popupMenuBar.clearItems();
		popupPanel.clear();
		copyMenu.setEnabled(false);
		deleteMenu.setEnabled(false);
		pasteMenu.setEnabled(false);
		cutMenu.setEnabled(false);

		switch (xmlTreeDisplay.getSelectedNode().getNodeType()) {

		case CellTreeNode.MASTER_ROOT_NODE:
			popupPanel.hide();
			break;
			
		case CellTreeNode.ROOT_NODE:
			Command addNodeCmd = new Command() {
				public void execute( ) {
					popupPanel.hide();
					addRootNodeTypeItem();

				}
			};
			addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0)) , true, addNodeCmd);
			popupMenuBar.addItem(addMenu);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			addMenu.setEnabled(true);
			copyMenu.setEnabled(false);
			deleteMenu.setEnabled(false);
			if(xmlTreeDisplay.getCopiedNode() != null && 
					xmlTreeDisplay.getCopiedNode().getParent().equals(xmlTreeDisplay.getSelectedNode())){
				pasteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(false);
			break;

		case CellTreeNode.CLAUSE_NODE:
			addCommonMenus();
			copyMenu.setEnabled(true);
			pasteMenu.setEnabled(false);
			if(xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1){
				deleteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(false);
			break;

		case CellTreeNode.LOGICAL_OP_NODE:	
			subMenuBar = new MenuBar(true);
			popupMenuBar.setAutoOpen(true);
			subMenuBar.setAutoOpen(true);
			createAddMenus(ClauseConstants.LOGICAL_OPS, CellTreeNode.LOGICAL_OP_NODE, subMenuBar);// creating logical Operators Menu 2nd level
			createAddQDM_MenuItem(subMenuBar);
			MenuBar timingMenuBar = new MenuBar(true); 
			subMenuBar.addItem("Timing", timingMenuBar);//Timing menu 2nd level
			createAddMenus(ClauseConstants.getTimingOperators().keySet().toArray(new String[0]), CellTreeNode.TIMING_NODE, timingMenuBar);// Timing sub menus 3rd level
			MenuBar functionsMenuBar = new MenuBar(true); 
			subMenuBar.addItem("Functions", functionsMenuBar);//functions menu 2nd level
			createAddMenus(ClauseConstants.FUNCTIONS, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar);// functions sub menus 3rd level			
			addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
			popupMenuBar.addItem(addMenu);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE){//can paste LOGOP,RELOP, QDM, TIMING & FUNCS
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
				deleteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
					&& xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE){
				cutMenu.setEnabled(true);
				subMenuBar = new MenuBar(true);
				createEditMenus(ClauseConstants.LOGICAL_OPS, subMenuBar);
				editMenu = new MenuItem("Edit", true, subMenuBar);
				popupMenuBar.addItem(editMenu);				
			}
			break;
		case CellTreeNode.TIMING_NODE:
			MenuBar subMenuBarLHS = createMenuBarWithTimingFuncAndQDM();			
			addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
			
			MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM();
			addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS);//RHS Sub Menu
			
			//Disable  RHS by default.
			if(xmlTreeDisplay.getSelectedNode().getChilds()==null){
				addMenuRHS.setEnabled(false);
			}
			//Disable LHS when One element is added and disable RHS when two elements are added.
			if(xmlTreeDisplay.getSelectedNode().getChilds()!=null){
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()>=1){
					addMenuLHS.setEnabled(false);
				}
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()>=2){
					addMenuRHS.setEnabled(false);
				}
			}
			popupMenuBar.addItem(addMenuLHS);
			popupMenuBar.addItem(addMenuRHS);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE
							&& (xmlTreeDisplay.getSelectedNode().getChilds() == null || xmlTreeDisplay.getSelectedNode().getChilds().size() < 2)){
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
				deleteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
					&& xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.TIMING_NODE){
				cutMenu.setEnabled(true);
				//subMenuBar = new MenuBar(true);
				//createEditMenus(ClauseConstants.getTimingOperators().keySet().toArray(new String[0]), subMenuBar);
				Command editCmd = new Command() {
					public void execute() {
						popupPanel.hide();
						ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay,xmlTreeDisplay.getSelectedNode());
						/*if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.ROOT_NODE){
							pasteRootNodeTypeItem();
						}else{
							xmlTreeDisplay.paste();	
						}*/
					}
				};
				editMenu = new MenuItem("Edit", true, editCmd);
				popupMenuBar.addItem(editMenu);				
			}
			break;
		
		case CellTreeNode.ELEMENT_REF_NODE:
			createQDMAttributeMenuItem(popupMenuBar,xmlTreeDisplay.getSelectedNode());
			addCommonMenus();
			copyMenu.setEnabled(true);
			pasteMenu.setEnabled(false);
			cutMenu.setEnabled(true);
			deleteMenu.setEnabled(true);
			break;
			
		case CellTreeNode.FUNCTIONS_NODE:
			subMenuBar = new MenuBar(true);
			popupMenuBar.setAutoOpen(true);
			subMenuBar.setAutoOpen(true);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE){//can paste LOGOP, RELOP, QDM, TIMING & FUNCS
				pasteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(true);
			deleteMenu.setEnabled(true);
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
					&& xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
				cutMenu.setEnabled(true);
				//subMenuBar = new MenuBar(true);
				//createEditMenus(ClauseConstants.getTimingOperators().keySet().toArray(new String[0]), subMenuBar);
				Command editCmd = new Command() {
					public void execute() {
						popupPanel.hide();
						ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay,xmlTreeDisplay.getSelectedNode());
						/*if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.ROOT_NODE){
							pasteRootNodeTypeItem();
						}else{
							xmlTreeDisplay.paste();	
						}*/
					}
				};
				editMenu = new MenuItem("Edit", true, editCmd);
				popupMenuBar.addItem(editMenu);	
			}
				break;
		default:
			break;
		}
	}

	private void createQDMAttributeMenuItem(MenuBar menuBar, final CellTreeNode cellTreeNode) {
		Command addQDMAttributeCmd = new Command() {
			public void execute() {
				popupPanel.hide();
				showQDMAttributePopup(cellTreeNode);
			}
		};
		MenuItem item = new MenuItem("Attribute",true,addQDMAttributeCmd);		
		menuBar.addItem(item);		
	}

	protected void showQDMAttributePopup(CellTreeNode cellTreeNode) {
		QDMAttributeDialogBox.showQDMAttributeDialogBox(xmlTreeDisplay,cellTreeNode);
	}


	private MenuBar createMenuBarWithTimingFuncAndQDM(){
		MenuBar menuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		createAddMenus(ClauseConstants.LOGICAL_OPS, CellTreeNode.LOGICAL_OP_NODE, menuBar);// creating logical Operators Menu 2nd level
		createAddQDM_MenuItem(menuBar);
		MenuBar timingMenuBar = new MenuBar(true); 
		menuBar.addItem("Timing", timingMenuBar);//Timing menu 2nd level
		//Sort TimingOperators.
		String[] key = ClauseConstants.getTimingOperators().keySet().toArray(new String[0]);
//		Arrays.sort(key);
		createAddMenus(key, CellTreeNode.TIMING_NODE, timingMenuBar);// Timing sub menus 3rd level
		MenuBar functionsMenuBar = new MenuBar(true); 
		menuBar.addItem("Functions", functionsMenuBar);//functions menu 2nd level
		createAddMenus(ClauseConstants.FUNCTIONS, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar);// functions sub menus 3rd level
		return menuBar;
	}
	
	private void createAddQDM_MenuItem(MenuBar menuBar) {
		Command addQDMCmd = new Command() {
			public void execute() {
				popupPanel.hide();
				showQDMPopup();
			}
		};
		MenuItem item = new MenuItem("QDM Element",true,addQDMCmd);		
		menuBar.addItem(item);
	}

	protected void showQDMPopup() {
		QDMDialogBox.showQDMDialogBox(xmlTreeDisplay);
	}

	/**
	 * Method iterates through the menuNames and creates MenuItems, 
	 * on selection of a MenuItem , a child node is created and added to the selected node 
	 * with the name and label set to selected menuItem's name and the nodeType set to the passed nodeType value.
	 * 
	 * and @param nodeType passed  
	 * @param menuNames
	 * @param nodeType
	 * @param menuBar
	 */
	private void createAddMenus(String[] menuNames, final short nodeType, MenuBar menuBar){
		for (final String name : menuNames) {
			Command addCmd = new Command() {
				public void execute( ) {
					popupPanel.hide();
					xmlTreeDisplay.addNode(name, name, nodeType);
				}
			};
			MenuItem menu = new MenuItem(name, true, addCmd);
			menuBar.addItem(menu);
		}
	}
	
	
	/**
	 * Method iterates through the editMenuNames and creates MenuItems, 
	 * on selection of a MenuItem, the selected node's name and label is updated with the selected menuItem's name.
	 * @param editMenuNames
	 * @param subMenuBar
	 */
	private void createEditMenus(String[] editMenuNames, MenuBar subMenuBar){
		for (final String editMenuName : editMenuNames) {
			Command timingCmd = new Command() {
				
				@Override
				public void execute() {
					popupPanel.hide();
					xmlTreeDisplay.editNode(editMenuName, editMenuName);
				}
			};
			MenuItem timingMenus = new MenuItem(editMenuName, true, timingCmd);
			subMenuBar.addItem(timingMenus);
		}
	}
	

	protected void pasteRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber ;
		CellTreeNode pasteNode = xmlTreeDisplay.getCopiedNode().cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
		xmlTreeDisplay.setCopiedNode(pasteNode);//make the new pasted node as the copied node
	}


	protected void addRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name =clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber ;

		CellTreeNode clauseNode  = xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.CLAUSE_NODE);
		clauseNode.createChild(ClauseConstants.AND, ClauseConstants.AND, CellTreeNode.LOGICAL_OP_NODE);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}


	private void addCommonMenus(){
		popupMenuBar.addItem(copyMenu);
		popupMenuBar.addItem(pasteMenu);
		popupMenuBar.addItem(cutMenu);
		popupMenuBar.addItem(deleteMenu);
		popupMenuBar.setVisible(true);		  
		popupPanel.add(popupMenuBar);
	}


	private String getAddMenuName(CellTreeNode selectedNode){
		return "Add" + " " +  selectedNode.getName().substring(0, selectedNode.getName().lastIndexOf(" "));
	}


	private int getNextHighestSequence(CellTreeNode selectedNode){
		SortedSet<Integer> sortedName = new TreeSet<Integer>();
		Integer lastInt = 0;
		sortedName.add(lastInt);
		if(selectedNode.getNodeType() == CellTreeNode.ROOT_NODE){
			if(selectedNode.hasChildren()){
				for (CellTreeNode treeNode : selectedNode.getChilds()) {
					String clauseNodeName = treeNode.getName().substring(treeNode.getName().lastIndexOf(" ")).trim();
					try {
						lastInt = Integer.parseInt(clauseNodeName);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(lastInt > 0){
						sortedName.add(lastInt);
					}
				}
			}else{
				return 1;
			}

		}
		return sortedName.last() + 1;
	}




	
}
