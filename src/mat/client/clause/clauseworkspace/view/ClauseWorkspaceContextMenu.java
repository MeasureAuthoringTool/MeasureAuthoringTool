package mat.client.clause.clauseworkspace.view;

import java.util.SortedSet;
import java.util.TreeSet;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

public class ClauseWorkspaceContextMenu {

	XmlTreeDisplay xmlTreeDisplay;

	MenuItem addMenu;

	MenuItem copyMenu;

	MenuItem pasteMenu;

	MenuItem deleteMenu;

	MenuBar popupMenuBar = new MenuBar(true);

	PopupPanel popupPanel;

	public ClauseWorkspaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		this.xmlTreeDisplay = treeDisplay;
		this.popupPanel = popPanel;
		Command copyCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.copy();
			}
		};
		copyMenu = new MenuItem("Copy", true, copyCmd);


		Command deleteCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.removeNode();
			}
		};
		deleteMenu = new MenuItem("Delete", true, deleteCmd);
	}


	public void displayMenuItems( final PopupPanel popupPanel){
		popupMenuBar.clearItems();
		popupPanel.clear();
		copyMenu.setEnabled(false);
		deleteMenu.setEnabled(false);


		switch (xmlTreeDisplay.getSelectedNode().getNodeType()) {

		case CellTreeNode.MASTER_ROOT_NODE:
			popupPanel.hide();
			break;
		case CellTreeNode.ROOT_NODE:
			Command addNodeCmd = new Command() {
				public void execute( ) {
					popupPanel.hide();
					addItem();

				}
			};
			addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0)) , true, addNodeCmd);
			Command pasteCmd = new Command() {
				public void execute( ) {
					popupPanel.hide();
					pasteItem();
				}
			};
			pasteMenu = new MenuItem("Paste", true, pasteCmd);
			popupMenuBar.addItem(addMenu);
			addCommonMenus();
			addMenu.setEnabled(true);
			copyMenu.setEnabled(false);
			deleteMenu.setEnabled(false);
			pasteMenu.setEnabled(canShowPaste());
			break;

		case CellTreeNode.CLAUSE_NODE:
			Command pasteClause = new Command() {
				public void execute( ) {
					popupPanel.hide();
				}
			};
			pasteMenu = new MenuItem("Paste", true, pasteClause);
			addCommonMenus();
			copyMenu.setEnabled(true);
			pasteMenu.setEnabled(false);
			deleteMenu.setEnabled(canShowDelete());//with options
			break;

		case CellTreeNode.LOGICAL_OP_NODE:	
			popupPanel.hide();
			break;

		default:
			break;
		}
	}

	protected void pasteItem() {
		String clauseNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber ;
		CellTreeNode pasteNode = xmlTreeDisplay.getCopiedNode().cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}


	protected void addItem() {
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


	private boolean canShowPaste(){
		if(xmlTreeDisplay.getCopiedNode() != null 
				&& xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.CLAUSE_NODE
				&& xmlTreeDisplay.getCopiedNode().getParent().equals(xmlTreeDisplay.getSelectedNode())){
			return true;
		}
		return false;
	}

	private boolean canShowDelete(){
		if(xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1){
			return true;
		}
		return false;
	}
}
