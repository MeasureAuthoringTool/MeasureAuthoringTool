package mat.client.clause.clauseworkspace.view;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import mat.client.ImageResources;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.MatContext;
import mat.shared.UUIDUtilClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.xml.client.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class ClauseWorkspaceContextMenu.
 */
public class ClauseWorkspaceContextMenu {
	
	/**
	 * Constant Stratum 1 node - default Node Stratification tab.
	 */
	private static final String DEFAULT_STRATUM_NODE = "Stratum 1";
	
	/**
	 * Constant Stratification node.
	 */
	private static final String STRATIFICATION = "Stratification";
	/**
	 * The Interface Template.
	 */
	interface Template extends SafeHtmlTemplates {
		
		/**
		 * Menu table.
		 * 
		 * @param name
		 *            the name
		 * @param shortCut
		 *            the short cut
		 * @return the safe html
		 */
		@Template("<table tabindex =\"0\"width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}</td></tr></table>")
		SafeHtml menuTable(String name, String shortCut);
		
		/**
		 * Menu table with icon.
		 *
		 * @param name - Menu Item Name.
		 * @param shortCut - Short Cut Key text.
		 * @param imageSource - Image source.
		 * @return SafeHtml.
		 */
		@Template("<table tabindex =\"0\"width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}"
				+ "<img src=\"{2}\"/></td></tr></table>")
		SafeHtml menuTableWithIcon(String name, String shortCut, SafeUri imageSource);
	}
	
	/** The Constant template. */
	protected static final Template template = GWT.create(Template.class);
	
	/** The xml tree display. */
	XmlTreeDisplay xmlTreeDisplay;
	
	/** The add menu. */
	MenuItem addMenu;
	
	/** The add menu lhs. */
	MenuItem addMenuLHS;
	
	/** The add menu rhs. */
	MenuItem addMenuRHS;
	
	/** The copy menu. */
	MenuItem copyMenu;
	
	/**
	 * Move Up Menu.
	 */
	MenuItem moveUpMenu;
	
	/**
	 * MOve Down Menu.
	 */
	MenuItem moveDownMenu;
	
	/** The paste menu. */
	MenuItem pasteMenu;
	
	/** The delete menu. */
	MenuItem deleteMenu;
	
	/** The cut menu. */
	MenuItem cutMenu;
	
	/** The edit menu. */
	MenuItem editMenu;
	
	/** The edit qdm menu. */
	MenuItem editQDMMenu;
	
	/** The View Human Readable menu *. */
	MenuItem viewHumanReadableMenu;
	
	/** The popup menu bar. */
	MenuBar popupMenuBar = new MenuBar(true);
	
	/** The sub menu bar. */
	MenuBar subMenuBar;
	
	/** The separator. */
	MenuItemSeparator separator = new MenuItemSeparator();
	
	/** The popup panel. */
	PopupPanel popupPanel;
	
	/** The paste cmd. */
	Command pasteCmd;
	
	/** The expand menu. */
	MenuItem expandMenu;
	
	
	/**
	 * Instantiates a new clause workspace context menu.
	 * 
	 * @param treeDisplay
	 *            the tree display
	 * @param popPanel
	 *            the pop panel
	 */
	public ClauseWorkspaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		xmlTreeDisplay = treeDisplay;
		xmlTreeDisplay.setDirty(false);
		popupPanel = popPanel;
		popupPanel.setWidth("200px");
		Command copyCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				xmlTreeDisplay.copy();
			}
		};
		copyMenu = new MenuItem(template.menuTable("Copy", "Ctrl+C"), copyCmd);
		
		Command deleteCmd = new Command() {
			@Override
			public void execute() {
				xmlTreeDisplay.setDirty(true);
				popupPanel.hide();
				if ((xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE)
						|| (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_REF_NODE)) {
					xmlTreeDisplay.getCommentArea().setText("");
				}
				xmlTreeDisplay.removeNode();
			}
		};
		deleteMenu = new MenuItem(template.menuTable("Delete", "Delete"), deleteCmd);
		Command cutCmd = new Command() {
			@Override
			public void execute() {
				xmlTreeDisplay.setDirty(true);
				popupPanel.hide();
				if ((xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE)
						|| (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_REF_NODE)) {
					xmlTreeDisplay.getCommentArea().setText("");
					@SuppressWarnings("unchecked")
					List<CellTreeNode> extraInformationchildNode = (List<CellTreeNode>)
					xmlTreeDisplay.getSelectedNode().getParent().
					getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
					if (extraInformationchildNode != null) {
						xmlTreeDisplay.getCommentArea().setText(extraInformationchildNode.get(0).getNodeText());
					}
				}
				xmlTreeDisplay.copy();
				xmlTreeDisplay.removeNode();
			}
		};
		cutMenu = new MenuItem(template.menuTable("Cut", "Ctrl+X"), cutCmd);
		
		Command pasteCmd = new Command() {
			@Override
			public void execute() {
				xmlTreeDisplay.setDirty(true);
				if (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.ROOT_NODE) {
					pasteRootNodeTypeItem();
				} else if (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.MASTER_ROOT_NODE) {
					pasteMasterRootNodeTypeItem();
				} else {
					xmlTreeDisplay.paste();
				}
				popupPanel.hide();
			}
		};
		pasteMenu = new MenuItem(template.menuTable("Paste", "Ctrl+V"), pasteCmd);
		
		Command expandCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				xmlTreeDisplay.expandSelected(xmlTreeDisplay.getXmlTree().getRootTreeNode());
			}
		};
		expandMenu = new MenuItem(template.menuTable("Expand", ""), expandCmd);
		
		Command viewHumanReadableCmd = new Command() {
			@Override
			public void execute() {
				System.out.println("View Human Readable clicked...");
				popupPanel.hide();
				CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
				if(selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE || selectedNode.getNodeType() == CellTreeNode.SUBTREE_NODE){
					String xmlForPopulationNode = XmlConversionlHelper.createXmlFromTree(selectedNode);
					final String populationName = selectedNode.getName();
					String measureId = MatContext.get().getCurrentMeasureId();
//					String url = GWT.getModuleBaseURL() + "export?id=" +measureId+ "&xml=" + xmlForPopulationNode+ "&format=subtreeHTML";
//					Window.open(url + "&type=open", "_blank", "");
					
					MatContext.get().getMeasureService().getHumanReadableForNode(measureId, xmlForPopulationNode, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							showHumanReadableDialogBox(result,populationName);
						}
									

						@Override
						public void onFailure(Throwable caught) {
														
						}
					});
				}				
			}
		};
		viewHumanReadableMenu = new MenuItem(template.menuTable("View Human Readable", ""), viewHumanReadableCmd);
	}
	/**
	 * Method displays the rightClick options based on the nodeType of the node
	 * selected on CellTree.
	 * @param popupPanel
	 *            the PopupPanel
	 */
	public void displayMenuItems( final PopupPanel popupPanel) {
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
			case CellTreeNode.TIMING_NODE:
				timingNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.ELEMENT_REF_NODE:
				elementRefNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.SET_OP_NODE:
				setOpsNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				functionNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.RELATIONSHIP_NODE:
				relationalOpNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.SUBTREE_NODE:
				subTreeNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.SUBTREE_ROOT_NODE:
				subTreeRootNodePopUpMenuItems(popupPanel);
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				subTreeNodePopupMenuItems(popupPanel);
				break;
			default:
				break;
		}
	}
	/**
	 * Generates popup menu for right click on a node of type 'CLAUSE_NODE'
	 */
	private void subTreeNodePopupMenuItems(final PopupPanel popupPanel) {
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
				SubTreeDialogBox.showSubTreeDialogBox(xmlTreeDisplay, false, true);
			}
		};
		editMenu = new MenuItem("Edit", true, editClauseCmd);
		popupMenuBar.addItem(editMenu);
	}
	/**
	 * Sub Tree Root Node Pop up Menu Items.
	 * @param popupPanel - PopupPanel.
	 */
	private void subTreeRootNodePopUpMenuItems(final PopupPanel popupPanel) {
		Command addSubTreeCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				EditSubTreeDialogBox.showAddDialogBox(xmlTreeDisplay
						, xmlTreeDisplay.getSelectedNode());
			}
		};
		MenuItem addSubTreeMenu = new MenuItem("Add New Clause", true, addSubTreeCmd);
		popupMenuBar.addItem(addSubTreeMenu);
		popupMenuBar.addSeparator(separator);
		if ((xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_ROOT_NODE)
				&& xmlTreeDisplay.getSelectedNode().hasChildren()) {
			addSubTreeMenu.setEnabled(false);
		}
		addCommonMenus();
	}
	/**
	 * Sub Tree Node Pop up Menu Items.
	 * @param popupPanel -PopupPanel.
	 */
	private void subTreeNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, subMenuBar);
		createAddQDM_MenuItem(subMenuBar);
		MenuBar timingSubTreeMenuBar = new MenuBar(true);
		subMenuBar.addItem("Timing", timingSubTreeMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE
				, timingSubTreeMenuBar); // Timing sub menus 3rd level
		MenuBar functionsSubTreeMenuBar = new MenuBar(true);
		subMenuBar.addItem("Functions", functionsSubTreeMenuBar); //functions menu 2nd level
		createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE
				, functionsSubTreeMenuBar); // functions sub menus 3rd level
		MenuBar relSubTreeMenuBar2 = new MenuBar(true);
		subMenuBar.addItem("Relationship", relSubTreeMenuBar2); //functions menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relSubTreeMenuBar2); // Relationship sub menus 3rd level
		addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
		popupMenuBar.addItem(addMenu);
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		if ((xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_NODE)) {
			// Only One child is allow in SubTree Root Node.
			if (xmlTreeDisplay.getSelectedNode().hasChildren()) {
				addMenu.setEnabled(false);
				pasteMenu.setEnabled(false);
			}
			copyMenu.setEnabled(false);
		}
		//can paste LOGOP,RELOP, QDM, TIMING & FUNCS
		if ((xmlTreeDisplay.getCopiedNode() != null)
				&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.SUBTREE_NODE)
				&& (xmlTreeDisplay.getSelectedNode().getNodeType() != CellTreeNode.SUBTREE_NODE)) {
			pasteMenu.setEnabled(true);
		}
		if (xmlTreeDisplay.getSelectedNode().getNodeType() != CellTreeNode.SUBTREE_NODE) {
			deleteMenu.setEnabled(true);
			if (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE) {
				cutMenu.setEnabled(true);
				subMenuBar = new MenuBar(true);
				createEditMenus(MatContext.get().logicalOps, subMenuBar);
				editMenu = new MenuItem("Edit", true, subMenuBar);
				popupMenuBar.addItem(editMenu);
			}
		}
		Command editSubTreeCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				EditSubTreeDialogBox.showEditDialogBox(xmlTreeDisplay
						, xmlTreeDisplay.getSelectedNode());
			}
		};
		MenuItem editSubTreeMenu = new MenuItem("Edit", true, editSubTreeCmd);
		popupMenuBar.addItem(editSubTreeMenu);
		
		popupMenuBar.addItem(viewHumanReadableMenu);
		viewHumanReadableMenu.setEnabled(true);
	}
	
	
	/**
	 * Relational Ops Node Pop up Menu Items.
	 * @param popupPanel - PopupPanel
	 */
	private void relationalOpNodePopUpMenuItems(PopupPanel popupPanel) {
		MenuBar subMenuBarRelLHS = createMenuBarWithTimingFuncAndQDM(false);
		MenuBar relAssociationMenuBar = new MenuBar(true);
		
		//subMenuBarRelLHS.addItem("Relationship", relAssociationMenuBar); //Relationship menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relAssociationMenuBar); // Relationship sub menus 3rd level
		addMenuLHS = new MenuItem("Add LHS", subMenuBarRelLHS); //LHS Sub Menu
		MenuBar subMenuBarRelRHS = createMenuBarWithTimingFuncAndQDM(false);
		MenuBar relAssociationMenuBarRHS = new MenuBar(true);
		//subMenuBarRelRHS.addItem("Relationship", relAssociationMenuBar); //Relationship menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relAssociationMenuBarRHS); // Relationship sub menus 3rd level
		addMenuRHS = new MenuItem("Add RHS", subMenuBarRelRHS); //RHS Sub Menu
		//Disable  RHS by default.
		if (xmlTreeDisplay.getSelectedNode().getChilds() == null) {
			addMenuRHS.setEnabled(false);
		}
		//Disable LHS when One element is added and disable RHS when two elements are added.
		if (xmlTreeDisplay.getSelectedNode().getChilds() != null) {
			if (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 1) {
				addMenuLHS.setEnabled(false);
			}
			if ((xmlTreeDisplay.getSelectedNode().getChilds().size() == 0)
					|| (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 2)) {
				addMenuRHS.setEnabled(false);
			}
		}
		popupMenuBar.addItem(addMenuLHS);
		popupMenuBar.addItem(addMenuRHS);
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		//MAT-3887 Disabling copy and paste options to avoid adding the relationship to relationship
		copyMenu.setEnabled(false);
//		if ((xmlTreeDisplay.getCopiedNode() != null)
//				&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)
//				&& ((xmlTreeDisplay.getSelectedNode().getChilds() == null)
//						|| (xmlTreeDisplay.getSelectedNode().getChilds().size() < 2))) {
//			pasteMenu.setEnabled(true);
//		}
		pasteMenu.setEnabled(false);
		//End MAT-3887
		if (xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE) {
			deleteMenu.setEnabled(true);
		}
		addMoveUpMenu(popupPanel);
		popupMenuBar.addItem(moveUpMenu);
		moveUpMenu.setEnabled(checkIfTopChildNode());
		addMoveDownMenu(popupPanel);
		popupMenuBar.addItem(moveDownMenu);
		moveDownMenu.setEnabled(checkIfLastChildNode());
		MenuBar subMenuBarEdit = new MenuBar(true);
		createEditMenus(MatContext.get().relationships, subMenuBarEdit);
		editMenu = new MenuItem("Edit", true, subMenuBarEdit);
		popupMenuBar.addItem(editMenu);
		cutMenu.setEnabled(true);
	}
	/**
	 * Function Node Pop Up Menu Items.
	 * @param popupPanel -PopupPanel.
	 */
	private void functionNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		//Commented for User story MAT-3167.
		/*createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE
				, subMenuBar);*/ // creating logical Operators Menu 2nd level
		
		if(xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ALL") || 
				xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ANY")){
			MenuBar subMenuBarLHS = createMenuBarWithOnlyQDM();
			addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
			
			MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM(true);
			MenuBar relSetOpMenuBar = new MenuBar(true);
			subMenuBarRHS.addItem("Relationship", relSetOpMenuBar); //functions menu 2nd level
			createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
					, relSetOpMenuBar); 
			addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS); //RHS Sub Menu
			
			//Disable  RHS by default.
			if (xmlTreeDisplay.getSelectedNode().getChilds() == null) {
				addMenuRHS.setEnabled(false);
			}
			//Disable LHS when One element is added and disable RHS when two elements are added.
			if (xmlTreeDisplay.getSelectedNode().getChilds() != null) {
				if (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 1) {
					addMenuLHS.setEnabled(false);
				}
				if ((xmlTreeDisplay.getSelectedNode().getChilds().size() == 0)
						|| (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 2)) {
					addMenuRHS.setEnabled(false);
				}
			}
			popupMenuBar.addItem(addMenuLHS);
			popupMenuBar.addItem(addMenuRHS);
			
		} else {//Menu Items for functions other than SATISFIES ALL and SATISFIES ANY
			createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
					, subMenuBar);	
			createAddQDM_MenuItem(subMenuBar);
			MenuBar timing = new MenuBar(true);
			subMenuBar.addItem("Timing", timing); //Timing menu 2nd level
			createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timing); // Timing sub menus 3rd level
			MenuBar functions = new MenuBar(true);
			subMenuBar.addItem("Functions", functions); //functions menu 2nd level
			createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE
					, functions); // functions sub menus 3rd level
			createAddClauseMenuItem(subMenuBar);
			addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
			popupMenuBar.addItem(addMenu);
		}
		
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		//can paste LOGOP, RELOP, QDM, TIMING & FUNCS
		if ((xmlTreeDisplay.getCopiedNode() != null)
				&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)) {
			pasteMenu.setEnabled(true);
		}
		cutMenu.setEnabled(true);
		deleteMenu.setEnabled(true);
		addMoveUpMenu(popupPanel);
		popupMenuBar.addItem(moveUpMenu);
		moveUpMenu.setEnabled(checkIfTopChildNode());
		addMoveDownMenu(popupPanel);
		popupMenuBar.addItem(moveDownMenu);
		moveDownMenu.setEnabled(checkIfLastChildNode());
		Command editFunctionsCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay
						, xmlTreeDisplay.getSelectedNode());
			}
		};
		editMenu = new MenuItem("Edit", true, editFunctionsCmd);
		popupMenuBar.addItem(editMenu);
	}
	/**
	 * Set Op Node Pop up Menu Items.
	 * @param popupPanel - PopupPanel.
	 */
	private void setOpsNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, subMenuBar);
		createAddQDM_MenuItem(subMenuBar);
		MenuBar timingSetOpMenuBar = new MenuBar(true);
		subMenuBar.addItem("Timing", timingSetOpMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings,
				CellTreeNode.TIMING_NODE, timingSetOpMenuBar); // Timing sub menus 3rd level
		MenuBar functionsSetOpMenuBar = new MenuBar(true);
		subMenuBar.addItem("Functions", functionsSetOpMenuBar); //functions menu 2nd level
		createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE
				, functionsSetOpMenuBar); // functions sub menus 3rd level
		MenuBar relSetOpMenuBar = new MenuBar(true);
		subMenuBar.addItem("Relationship", relSetOpMenuBar); //functions menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relSetOpMenuBar); // Timing sub menus 3rd level
		createAddClauseMenuItem(subMenuBar);
		addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
		popupMenuBar.addItem(addMenu);
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		//can paste LOGOP, RELOP, QDM, TIMING & FUNCS
		if ((xmlTreeDisplay.getCopiedNode() != null)
				&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)) {
			pasteMenu.setEnabled(true);
		}
		deleteMenu.setEnabled(true);
		addMoveUpMenu(popupPanel);
		popupMenuBar.addItem(moveUpMenu);
		moveUpMenu.setEnabled(checkIfTopChildNode());
		addMoveDownMenu(popupPanel);
		popupMenuBar.addItem(moveDownMenu);
		moveDownMenu.setEnabled(checkIfLastChildNode());
		if ((xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.SUBTREE_NODE)
				&& (xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SET_OP_NODE)) {
			cutMenu.setEnabled(true);
			subMenuBar = new MenuBar(true);
			createEditMenus(MatContext.get().setOps, subMenuBar);
			editMenu = new MenuItem("Edit", true, subMenuBar);
			popupMenuBar.addItem(editMenu);
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
				SubTreeDialogBox.showSubTreeDialogBox(xmlTreeDisplay, true,true);
			}
		};
		MenuItem item = new MenuItem("Clause", true, addClauseCmd);
		menuBar.addItem(item);
	}
	
	/**
	 * Element Ref Node Pop Up Menu Items.
	 * @param popupPanel - PopupPanel.
	 */
	private void elementRefNodePopUpMenuItems(final PopupPanel popupPanel) {
		Command editQDMCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				//To edit the QDM element
				showQDMPopup(false);
			}
		};
		editQDMMenu = new MenuItem("Edit", true, editQDMCmd);
		popupMenuBar.addItem(editQDMMenu);
		createQDMAttributeMenuItem(popupMenuBar, xmlTreeDisplay.getSelectedNode());
		addCommonMenus();
		copyMenu.setEnabled(true);
		pasteMenu.setEnabled(false);
		cutMenu.setEnabled(true);
		deleteMenu.setEnabled(true);
		addMoveUpMenu(popupPanel);
		popupMenuBar.addItem(moveUpMenu);
		moveUpMenu.setEnabled(checkIfTopChildNode());
		addMoveDownMenu(popupPanel);
		popupMenuBar.addItem(moveDownMenu);
		moveDownMenu.setEnabled(checkIfLastChildNode());
	}
	/**
	 * Timing Node Pop Up Menu Items.
	 * @param popupPanel - PopupPanel.
	 */
	private void timingNodePopUpMenuItems(final PopupPanel popupPanel) {
		MenuBar subMenuBarLHS = createMenuBarWithTimingFuncAndQDM(true);
		addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
		MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM(true);
		addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS); //RHS Sub Menu
		//Disable  RHS by default.
		if (xmlTreeDisplay.getSelectedNode().getChilds() == null) {
			addMenuRHS.setEnabled(false);
		}
		//Disable LHS when One element is added and disable RHS when two elements are added.
		if (xmlTreeDisplay.getSelectedNode().getChilds() != null) {
			if (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 1) {
				addMenuLHS.setEnabled(false);
			}
			if ((xmlTreeDisplay.getSelectedNode().getChilds().size() == 0)
					|| (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 2)) {
				addMenuRHS.setEnabled(false);
			}
		}
		popupMenuBar.addItem(addMenuLHS);
		popupMenuBar.addItem(addMenuRHS);
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		if ((xmlTreeDisplay.getCopiedNode() != null)
				&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)
				&& ((xmlTreeDisplay.getSelectedNode().getChilds() == null)
						|| (xmlTreeDisplay.getSelectedNode().getChilds().size() < 2))) {
			pasteMenu.setEnabled(true);
		}
		if (xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE) {
			deleteMenu.setEnabled(true);
		}
		addMoveUpMenu(popupPanel);
		popupMenuBar.addItem(moveUpMenu);
		moveUpMenu.setEnabled(checkIfTopChildNode());
		addMoveDownMenu(popupPanel);
		popupMenuBar.addItem(moveDownMenu);
		moveDownMenu.setEnabled(checkIfLastChildNode());
		Command editCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay
						, xmlTreeDisplay.getSelectedNode());
			}
		};
		editMenu = new MenuItem("Edit", true, editCmd);
		popupMenuBar.addItem(editMenu);
		cutMenu.setEnabled(true);
	}
	
	/**
	 * Show hide expand menu.
	 */
	protected void showHideExpandMenu() {
		if (xmlTreeDisplay.getSelectedNode().hasChildren()) {
			expandMenu.setEnabled(true);
		} else {
			expandMenu.setEnabled(false);
		}
	}
	
	/**
	 * Creates the qdm attribute menu item.
	 * 
	 * @param menuBar
	 *            the menu bar
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	protected void createQDMAttributeMenuItem(MenuBar menuBar, final CellTreeNode cellTreeNode) {
		Command addQDMAttributeCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				showQDMAttributePopup(cellTreeNode);
			}
		};
		MenuItem item = new MenuItem(template.menuTable("Edit Attribute", ""), addQDMAttributeCmd);
		menuBar.addItem(item);
		checkForTimingElementDataType(cellTreeNode, item);
	}
	
	/**
	 * Check if the Data Type for the selected node is "Timing Element" and
	 * disable the menuItem.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param item
	 *            the item
	 */
	protected void checkForTimingElementDataType(CellTreeNode cellTreeNode,
			MenuItem item) {
		if (cellTreeNode.getNodeType() != CellTreeNode.ELEMENT_REF_NODE) {
			return;
		}
		String qdmName = PopulationWorkSpaceConstants.getElementLookUpName().get(cellTreeNode.getUUID());
		Node qdmNode = PopulationWorkSpaceConstants.getElementLookUpNode().get(qdmName + "~" + cellTreeNode.getUUID());
		//Could not find the qdm node in elemenentLookup tag
		if (qdmNode == null) {
			return;
		}
		String qdmDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		if ("Timing Element".equals(qdmDataType)) {
			item.setEnabled(false);
		}
	}
	
	/**
	 * Show qdm attribute popup.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	protected void showQDMAttributePopup(CellTreeNode cellTreeNode) {
		QDMAttributeDialogBox.showQDMAttributeDialogBox(xmlTreeDisplay, cellTreeNode);
	}
	
	
	/**
	 * Creates the menu bar with timing func and qdm.
	 * 
	 * @return the menu bar
	 */
	protected MenuBar createMenuBarWithTimingFuncAndQDM(boolean addClauseMenuItem) {
		MenuBar menuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		//Commented for User story MAT-3167.
		//createAddMenus(MatContext.get().logicalOps,
		//CellTreeNode.LOGICAL_OP_NODE, menuBar);// creating logical Operators Menu 2nd level
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, menuBar);
		createAddQDM_MenuItem(menuBar);
		MenuBar timingMenuBar = new MenuBar(true);
		menuBar.addItem("Timing", timingMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timingMenuBar); // Timing sub menus 3rd level
		MenuBar functionsMenuBar = new MenuBar(true);
		menuBar.addItem("Functions", functionsMenuBar); //functions menu 2nd level
		createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar); // functions sub menus 3rd level
		if(addClauseMenuItem){
			createAddClauseMenuItem(menuBar);
		}
		return menuBar;
	}
	
	/**
	 * Creates the add qdm_ menu item.
	 * 
	 * @param menuBar
	 *            the menu bar
	 */
	protected void createAddQDM_MenuItem(MenuBar menuBar) {
		Command addQDMCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				showQDMPopup(true);
			}
		};
		MenuItem item = new MenuItem("QDM Element", true, addQDMCmd);
		menuBar.addItem(item);
	}
	/**
	 * Show qdm popup.
	 * 
	 * @param isAdd
	 *            the is add
	 */
	protected void showQDMPopup(boolean isAdd) {
		QDMDialogBox.showQDMDialogBox(xmlTreeDisplay, isAdd);
	}
	
	/**
	 * Method iterates through the menuNames and creates MenuItems, on selection
	 * of a MenuItem , a child node is created and added to the selected node
	 * with the name and label set to selected menuItem's name and the nodeType
	 * set to the passed nodeType value.
	 * 
	 * and @param nodeType passed
	 * 
	 * @param menuNames
	 *            the menu names
	 * @param nodeType
	 *            the node type
	 * @param menuBar
	 *            the menu bar
	 */
	protected void createAddMenus(List<String> menuNames, final short nodeType, MenuBar menuBar) {
		for (final String name : menuNames) {
			Command addCmd = new Command() {
				@Override
				public void execute() {
					xmlTreeDisplay.setDirty(true);
					popupPanel.hide();
					xmlTreeDisplay.addNode(name, name, nodeType);
				}
			};
			MenuItem menu = new MenuItem(name, true, addCmd);
			menuBar.addItem(menu);
		}
	}
	
	
	/**
	 * Method iterates through the editMenuNames and creates MenuItems, on
	 * selection of a MenuItem, the selected node's name and label is updated
	 * with the selected menuItem's name.
	 * 
	 * @param editMenuNames
	 *            the edit menu names
	 * @param subMenuBar
	 *            the sub menu bar
	 */
	protected void createEditMenus(List<String> editMenuNames, MenuBar subMenuBar) {
		for (final String editMenuName : editMenuNames) {
			Command timingCmd = new Command() {
				
				@Override
				public void execute() {
					xmlTreeDisplay.setDirty(true);
					popupPanel.hide();
					xmlTreeDisplay.editNode(editMenuName, editMenuName);
				}
			};
			MenuItem timingMenus = new MenuItem(editMenuName, true, timingCmd);
			subMenuBar.addItem(timingMenus);
		}
	}
	/**
	 * Paste root node type item.
	 */
	protected void pasteRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber;
		CellTreeNode pasteNode = xmlTreeDisplay.getCopiedNode().cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
		CellTreeNode clonedNode = pasteNode.cloneNode();//created new instance for pasted node
		clonedNode.setParent(pasteNode.getParent());//set parent of the cloned node
		xmlTreeDisplay.setCopiedNode(clonedNode); //make the new cloned node as the copied node
	}
	
	/**
	 * Paste master root node type item.
	 */
	protected void pasteMasterRootNodeTypeItem() {
		String rootNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = rootNodeName.substring(0, rootNodeName.lastIndexOf(" ")) + " " + seqNumber;
		CellTreeNode pasteNode = xmlTreeDisplay.getCopiedNode().cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
		CellTreeNode clonedNode = pasteNode.cloneNode();//created new instance for pasted node
		clonedNode.setParent(pasteNode.getParent());//set parent of the cloned node
		xmlTreeDisplay.setCopiedNode(clonedNode);  //make the new cloned node as the copied node
	}
	
	/**
	 * Adds the root node type item.
	 */
	protected void addRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		String selectedNodeName = xmlTreeDisplay.getSelectedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber;
		
		CellTreeNode clauseNode  = xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.CLAUSE_NODE);
		String nodeName = selectedNodeName.replaceAll("\\s+", "").toLowerCase();
		if (!xmlTreeDisplay.getSelectedNode().getName().contains(STRATIFICATION)) {
			if (PopulationWorkSpaceConstants.topNodeOperatorMap.containsKey((nodeName))) {
				String nodeTopLogicalOperator = PopulationWorkSpaceConstants.topNodeOperatorMap.get(nodeName);
				clauseNode.createChild(nodeTopLogicalOperator.toUpperCase(),
						nodeTopLogicalOperator.toUpperCase(), CellTreeNode.LOGICAL_OP_NODE);
			}
		}
		
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}
	
	/**
	 * Adds the parent root node type item.
	 */
	protected void addMasterRootNodeTypeItem() {
		String rootNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = rootNodeName.substring(0, rootNodeName.lastIndexOf(" ")) + " " + seqNumber;
		
		CellTreeNode rootNode  = xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.ROOT_NODE);
		rootNode.setUUID(UUIDUtilClient.uuid());
		rootNode.createChild(DEFAULT_STRATUM_NODE, DEFAULT_STRATUM_NODE, CellTreeNode.CLAUSE_NODE);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}
	
	
	/**
	 * Adds the common menus.
	 */
	protected void addCommonMenus() {
		popupMenuBar.addItem(copyMenu);
		popupMenuBar.addItem(pasteMenu);
		popupMenuBar.addItem(cutMenu);
		popupMenuBar.addItem(deleteMenu);
		popupMenuBar.addItem(expandMenu);
		popupMenuBar.setVisible(true);
		popupPanel.add(popupMenuBar);
	}
	
	
	/**
	 * Gets the adds the menu name.
	 * 
	 * @param selectedNode
	 *            the selected node
	 * @return the adds the menu name
	 */
	protected String getAddMenuName(CellTreeNode selectedNode) {
		return "Add" + " " +  selectedNode.getName().substring(0, selectedNode.getName().lastIndexOf(" "));
	}
	
	/**
	 * Menu - Move Up.
	 * @param popupPanel - PopupPanel.
	 */
	protected final void addMoveUpMenu(final PopupPanel popupPanel) {
		Command moveUpCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				xmlTreeDisplay.moveUp();
			}
		};
		SafeUri uri = new SafeUri() {
			@Override
			public String asString() {
				Image moveUpIcon = new Image(ImageResources.INSTANCE.go_up());
				return moveUpIcon.getUrl();
			}
		};
		
		
		moveUpMenu = new MenuItem(template.menuTableWithIcon("Move Up", "", uri), moveUpCmd);
	}
	/**
	 * Menu - Move Down.
	 * @param popupPanel -PopupPanel.
	 */
	protected final void addMoveDownMenu(final PopupPanel popupPanel) {
		Command moveDownCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				xmlTreeDisplay.moveDown();
			}
		};
		SafeUri uri = new SafeUri() {
			@Override
			public String asString() {
				Image moveDownIcon = new Image(ImageResources.INSTANCE.go_down());
				return moveDownIcon.getUrl();
			}
		};
		moveDownMenu = new MenuItem(template.menuTableWithIcon("Move Down", "" , uri), moveDownCmd);
	}
	/**
	 * Move Up Menu is set Enabled if selected node is not the first child of its parent node.
	 * @return boolean.
	 */
	protected final boolean checkIfTopChildNode() {
		CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
		CellTreeNode parentNode = selectedNode.getParent();
		if ((parentNode.getChilds() == null) || (parentNode.getChilds().size() <= 1)) {
			return false;
		}
		for (int i = 0; i <= parentNode.getChilds().size(); i++) {
			if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
				return i > 0;
			}
		}
		return true;
	}
	/**
	 * Move down menu is set to enabled if the selected node is not the last child of its parent node.
	 * @return boolean.
	 */
	protected final boolean checkIfLastChildNode() {
		CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
		CellTreeNode parentNode = selectedNode.getParent();
		if ((parentNode.getChilds() == null) || (parentNode.getChilds().size() <= 1)) {
			return false;
		}
		for (int i = 0; i <= parentNode.getChilds().size(); i++) {
			if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
				return !(i == (parentNode.getChilds().size() - 1));
			}
		}
		return true;
	}
	/**
	 * Gets the next highest sequence.
	 * 
	 * @param selectedNode
	 *            the selected node
	 * @return the next highest sequence
	 */
	private int getNextHighestSequence(CellTreeNode selectedNode) {
		SortedSet<Integer> sortedName = new TreeSet<Integer>();
		Integer lastInt = 0;
		sortedName.add(lastInt);
		if ((selectedNode.getNodeType() == CellTreeNode.ROOT_NODE)
				|| (selectedNode.getNodeType() == CellTreeNode.MASTER_ROOT_NODE)) {
			if (selectedNode.hasChildren()) {
				for (CellTreeNode treeNode : selectedNode.getChilds()) {
					String clauseNodeName = treeNode.getName().substring(treeNode.getName().lastIndexOf(" ")).trim();
					try {
						lastInt = Integer.parseInt(clauseNodeName);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (lastInt > 0) {
						sortedName.add(lastInt);
					}
				}
			} else {
				return 1;
			}
		}
		return sortedName.last() + 1;
	}
	
	/**
	 * Show human readable dialog box.
	 *
	 * @param result the result
	 * @param populationName the population name
	 */
	private native void showHumanReadableDialogBox(String result, String populationName) /*-{
	var humanReadableWindow = window.open("","","width=1000,height=700");
	if(humanReadableWindow && humanReadableWindow.top){
		//Populate the human readable in the new window.
		humanReadableWindow.document.write(result);
		humanReadableWindow.document.title = populationName;
	}
}-*/;
	
	/**
	 * Gets the view human readable menu.
	 *
	 * @return MenuItem.
	 */
	public MenuItem getViewHumanReadableMenu() {
		return viewHumanReadableMenu;
	}
	
	/**
	 * Creates the menu bar with only qdm.
	 *
	 * @return the menu bar
	 */
	protected MenuBar createMenuBarWithOnlyQDM() {
		MenuBar menuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		createAddQDM_MenuItem(menuBar);
		return menuBar;
	}
	
}
