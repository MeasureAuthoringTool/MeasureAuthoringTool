package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.MatContext;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author jnarang
 *
 */
public class PopulationWorkSpaceContextMenu extends ClauseWorkspaceContextMenu {
	/**
	 * @param treeDisplay - XmlTreeDisplay.
	 * @param popPanel - PopupPanel.
	 */
	
	public PopulationWorkSpaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		super(treeDisplay, popPanel);
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.view.ClauseWorkspaceContextMenu#displayMenuItems(com.google.gwt.user.client.ui.PopupPanel)
	 */
	@Override
	public void displayMenuItems(final PopupPanel popupPanel) {
		popupMenuBar.clearItems();
		popupMenuBar.setFocusOnHoverEnabled(true);
		popupMenuBar.focus();
		popupPanel.clear();
		copyMenu.setEnabled(false);
		deleteMenu.setEnabled(false);
		pasteMenu.setEnabled(false);
		cutMenu.setEnabled(false);
		showHideExpandMenu();
		switch (xmlTreeDisplay.getSelectedNode().getNodeType()) {
			case CellTreeNode.MASTER_ROOT_NODE:
				addCommonMenus();
				break;
			case CellTreeNode.ROOT_NODE:
				Command addNodeCmd = new Command() {
					@Override
					public void execute() {
						xmlTreeDisplay.setDirty(true);
						popupPanel.hide();
						addRootNodeTypeItem();
					}
				};
				addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0))
						, true, addNodeCmd);
				popupMenuBar.addItem(addMenu);
				popupMenuBar.addSeparator(separator);
				addCommonMenus();
				addMenu.setEnabled(true);
				copyMenu.setEnabled(false);
				deleteMenu.setEnabled(false);
				if ((xmlTreeDisplay.getCopiedNode() != null)
						&& xmlTreeDisplay.getCopiedNode().getParent().equals(xmlTreeDisplay.getSelectedNode())) {
					pasteMenu.setEnabled(true);
				}
				cutMenu.setEnabled(false);
				break;
			case CellTreeNode.CLAUSE_NODE:
				addCommonMenus();
				
				
				copyMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				if (xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1) {
					deleteMenu.setEnabled(true);
				}
				cutMenu.setEnabled(false);
				break;
			case CellTreeNode.LOGICAL_OP_NODE:
				subMenuBar = new MenuBar(true);
				popupMenuBar.setAutoOpen(true);
				subMenuBar.setAutoOpen(true);
				createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE
						, subMenuBar); // creating logical Operators Menu 2nd level
				createAddClauseMenuItem(subMenuBar);
				addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
				popupMenuBar.addItem(addMenu);
				addCommonMenus();
				copyMenu.setEnabled(true);
				//can paste LOGOP,RELOP, QDM, TIMING & FUNCS
				if ((xmlTreeDisplay.getCopiedNode() != null)
						&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)) {
					pasteMenu.setEnabled(true);
				}
				if (xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE) {
					deleteMenu.setEnabled(true);
				}
				if ((xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE)) {
					cutMenu.setEnabled(true);
					addMoveUpMenu(popupPanel);
					popupMenuBar.addItem(moveUpMenu);
					moveUpMenu.setEnabled(checkIfTopChildNode());
					addMoveDownMenu(popupPanel);
					popupMenuBar.addItem(moveDownMenu);
					moveDownMenu.setEnabled(checkIfLastChildNode());
					subMenuBar = new MenuBar(true);
					createEditMenus(MatContext.get().logicalOps, subMenuBar);
					editMenu = new MenuItem("Edit", true, subMenuBar);
					popupMenuBar.addItem(editMenu);
				}
				break;
			case CellTreeNode.TIMING_NODE:
				addCommonMenus();
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.ELEMENT_REF_NODE:
				addCommonMenus();
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				addCommonMenus();
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.RELATIONSHIP_NODE:
				addCommonMenus();
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				subMenuBar = new MenuBar(true);
				popupMenuBar.setAutoOpen(true);
				subMenuBar.setAutoOpen(true);
				addCommonMenus();
				addMoveUpMenu(popupPanel);
				popupMenuBar.addItem(moveUpMenu);
				moveUpMenu.setEnabled(checkIfTopChildNode());
				addMoveDownMenu(popupPanel);
				popupMenuBar.addItem(moveDownMenu);
				moveDownMenu.setEnabled(checkIfLastChildNode());
				deleteMenu.setEnabled(true);
				Command editClauseCmd = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						//To edit the Clause element
						SubTreeDialogBox.showSubTreeDialogBox(xmlTreeDisplay, false);
					}
				};
				editMenu = new MenuItem("Edit", true, editClauseCmd);
				popupMenuBar.addItem(editMenu);
				break;
			default:
				break;
		}
	}
	
	/**
	 * @param menuBar - MenuBar.
	 */
	private void createAddClauseMenuItem(MenuBar menuBar) {
		Command addClauseCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				SubTreeDialogBox.showSubTreeDialogBox(xmlTreeDisplay, true);
			}
		};
		MenuItem item = new MenuItem("Clause", true, addClauseCmd);
		menuBar.addItem(item);
	}
}
