package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author jnarang
 *
 */
public class PopulationWorkSpaceContextMenu extends ClauseWorkspaceContextMenu {
	/**
	 * @param treeDisplay
	 * @param popPanel
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
				addCommonMenus();
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
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
			default:
				break;
		}
	}
}
