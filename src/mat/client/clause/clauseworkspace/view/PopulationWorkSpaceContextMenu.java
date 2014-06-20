package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SecondaryButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Node;
// TODO: Auto-generated Javadoc
/**
 * The Class PopulationWorkSpaceContextMenu.
 *
 * @author jnarang
 */
public class PopulationWorkSpaceContextMenu extends ClauseWorkspaceContextMenu {
	/**
	 * Stratum Node name.
	 */
	private static final String STRATUM = "Stratum";
	/**
	 * Stratification Node name.
	 */
	private static final String STRATIFICATION = "Stratification";
	
	/** The clause tree display. */
	private XmlTreeDisplay clauseTreeDisplay;
	
	/**
	 * Instantiates a new population work space context menu.
	 *
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
		viewHumanReadableMenu.setEnabled(false);
		showHideExpandMenu();
		switch (xmlTreeDisplay.getSelectedNode().getNodeType()) {
			case CellTreeNode.MASTER_ROOT_NODE:
				if (xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase(STRATIFICATION)) {
					Command addNodeCmd = new Command() {
						@Override
						public void execute() {
							xmlTreeDisplay.setDirty(true);
							popupPanel.hide();
							addMasterRootNodeTypeItem();
						}
					};
					addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0))
							, true, addNodeCmd);
					popupMenuBar.addItem(addMenu);
					popupMenuBar.addSeparator(separator);
					if ((xmlTreeDisplay.getCopiedNode() != null)
							&& (xmlTreeDisplay.getCopiedNode().getParent()
									.equals(xmlTreeDisplay.getSelectedNode()))) {
						pasteMenu.setEnabled(true);
					}
				}
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
				
				//TODO by Ravi
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
				if (xmlTreeDisplay.getSelectedNode().getName().contains(STRATIFICATION)) {
					copyMenu.setEnabled(true);
				}
				if (xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1) {
					deleteMenu.setEnabled(true);
				}
				break;
			case CellTreeNode.CLAUSE_NODE:
				if (xmlTreeDisplay.getSelectedNode().getName().contains(STRATUM)) {
					subMenuBar = new MenuBar(true);
					popupMenuBar.setAutoOpen(true);
					subMenuBar.setAutoOpen(true);
					createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE
							, subMenuBar); // creating logical Operators Menu 2nd level
					createAddClauseMenuItem(subMenuBar);
					addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
					popupMenuBar.addItem(addMenu);
					if ((xmlTreeDisplay.getCopiedNode() != null)
							&& xmlTreeDisplay.getCopiedNode().getParent().
							equals(xmlTreeDisplay.getSelectedNode())) {
						pasteMenu.setEnabled(true);
					}
				}
				addCommonMenus();
				//Add "View Human Readable" right click option for all populations: Start
				popupMenuBar.addItem(viewHumanReadableMenu);
				viewHumanReadableMenu.setEnabled(true);
				//Add "View Human Readable" right click option for all populations: End
				copyMenu.setEnabled(true);
				//pasteMenu.setEnabled(false);
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
						&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.ROOT_NODE)) {
					pasteMenu.setEnabled(true);
				}
				if ((xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						|| (xmlTreeDisplay.getSelectedNode().getParent().getName().contains(STRATUM))) {
					deleteMenu.setEnabled(true);
				}
				if (((xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE))
						|| (xmlTreeDisplay.getSelectedNode().getParent().getName().contains(STRATUM))) {
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
				Command showClauseLogic = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						ShowSubTreeLogicDialogBox.showSubTreeLogicDialogBox(xmlTreeDisplay, false);
					}
				};
				addMenu = new MenuItem("View Clause Logic", true, showClauseLogic);
				popupMenuBar.addItem(addMenu);
				popupMenuBar.addSeparator(separator);
				addCommonMenus();
				addMenu.setEnabled(true);
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
	 * Creates the add clause menu item.
	 *
	 * @param menuBar - MenuBar.
	 */
	private void createAddClauseMenuItem(MenuBar menuBar) {
		Command addClauseCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				//To show the Clause Logic on Population Workspace
				SubTreeDialogBox.showSubTreeDialogBox(xmlTreeDisplay, true);
			}
		};
		MenuItem item = new MenuItem("Clause", true, addClauseCmd);
		menuBar.addItem(item);
	}	
	
}
