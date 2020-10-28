package mat.client.clause.clauseworkspace.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.xml.client.Node;
import mat.client.ImageResources;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.event.ClauseSpecificOccurenceEvent;
import mat.client.shared.MatContext;
import mat.shared.MatConstants;
import mat.shared.UUIDUtilClient;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ClauseWorkspaceContextMenu {
	
	private static final String DEFAULT_STRATUM_NODE = "Stratum 1";
	
	private static final String STRATIFICATION = "Stratification";

	
	protected static final Template template = GWT.create(Template.class);
	
	XmlTreeDisplay xmlTreeDisplay;
	
	MenuItem addMenu;
	
	MenuItem addFuncMenu;
	
	MenuItem addMenuLHS;
	
	MenuItem addMenuRHS;
	
	MenuItem copyMenu;

	MenuItem moveUpMenu;
	
	MenuItem moveDownMenu;
	
	MenuItem pasteMenu;

	MenuItem deleteMenu;
	
	MenuItem replaceMenu;
	
	MenuItem cutMenu;
	
	MenuItem editMenu;
	
	MenuItem editQDMMenu;
	
	MenuItem viewHumanReadableMenu;
	
	MenuBar popupMenuBar = new MenuBar(true);
	
	MenuBar subMenuBar;
	
	MenuItemSeparator separator = new MenuItemSeparator();
	
	PopupPanel popupPanel;
	
	Command pasteCmd;
	
	MenuItem expandMenu;

	public ClauseWorkspaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		xmlTreeDisplay = treeDisplay;
		xmlTreeDisplay.setDirty(false);
		popupPanel = popPanel;
		popupPanel.setWidth("200px");
		popupPanel.getElement().setAttribute("id", "mainPopUpPanel");
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
		deleteMenu = new MenuItem(template.menuTable("Delete"), deleteCmd);
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
				if((selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE) || (selectedNode.getNodeType() == CellTreeNode.SUBTREE_NODE)){
					String xmlForPopulationNode = XmlConversionlHelper.createXmlFromTree(selectedNode);
					final String populationName = selectedNode.getName();
					String measureId = MatContext.get().getCurrentMeasureId();
					xmlTreeDisplay.validateCellTreeNodes(xmlTreeDisplay.getXmlTree()
							.getRootTreeNode(), false);
					if (xmlTreeDisplay.isValidHumanReadable()) {
						MatContext.get().getMeasureService().getHumanReadableForNode(measureId, xmlForPopulationNode, new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								System.out.println("On Success....showHumanReadableDialogBox:");
								showHumanReadableDialogBox(result, populationName);
							}
							@Override
							public void onFailure(Throwable caught) {
							}
						});
					}
				}
			}
		};
		viewHumanReadableMenu = new MenuItem(template.menuTable("View Human Readable", ""), viewHumanReadableCmd);
	}
	

	public void displayMenuItems(PopupPanel popupPanel) {}
	
	private void subTreeNodePopupMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		subMenuBar.getElement().setAttribute("id", "subMenuBarPopUpPanel");
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
		checkIsParentSatisfy();
	}

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

	private void subTreeNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		subMenuBar.getElement().setAttribute("id", "subMenuBarSubTreeNodePopUpPanel");
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, subMenuBar);
		createAddQDM_MenuItem(subMenuBar);
		MenuBar timingSubTreeMenuBar = new MenuBar(true);
		timingSubTreeMenuBar.getElement().setAttribute("id", "timingsSubMenu");
		subMenuBar.addItem("Timing", timingSubTreeMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE
				, timingSubTreeMenuBar); // Timing sub menus 3rd level
		MenuBar functionsSubTreeMenuBar = new MenuBar(true);
		subMenuBar.addItem("Functions", functionsSubTreeMenuBar); //functions menu 2nd level
		List<String> functionsList = ComparisonDialogBox.filterFunctions(xmlTreeDisplay.getSelectedNode(),MatContext.get().functions);
		createAddMenus(functionsList, CellTreeNode.FUNCTIONS_NODE
				, functionsSubTreeMenuBar); // functions sub menus 3rd level
		MenuBar relSubTreeMenuBar2 = new MenuBar(true);
		relSubTreeMenuBar2.getElement().setAttribute("id", "relOpSubMenu");
		subMenuBar.addItem("Relationship", relSubTreeMenuBar2); //functions menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relSubTreeMenuBar2); // Relationship sub menus 3rd level
		addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
		popupMenuBar.addItem(addMenu);
		popupMenuBar.addSeparator(separator);
		Command addSpecificOccSubTreeCmd = new Command() {
			@Override
			public void execute() {
				popupPanel.hide();
				MatContext.get().getEventBus().fireEvent(
						new ClauseSpecificOccurenceEvent(xmlTreeDisplay.getSelectedNode(), true));
			}
		};
		String qdmVariableValue = null;
		MenuItem specificOccMenuItem = new MenuItem("Create Specific Occurrence", true, addSpecificOccSubTreeCmd);
		popupMenuBar.addItem(specificOccMenuItem);
		if(!xmlTreeDisplay.isQdmVariableDirty()) {
			if (xmlTreeDisplay.getSelectedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES) != null) {
				@SuppressWarnings("unchecked")
				HashMap<String , String> map = (HashMap<String, String>)
				xmlTreeDisplay.getSelectedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				qdmVariableValue = map.get("qdmVariable");
			}
			if ((qdmVariableValue != null) && qdmVariableValue.equalsIgnoreCase("true")) {
				specificOccMenuItem.setEnabled(true);
				popupMenuBar.addItem(specificOccMenuItem);
			} else {
				specificOccMenuItem.setEnabled(false);
				
			}
		} else {
			specificOccMenuItem.setEnabled(false);
		}
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
		//Allow paste option
		if ((xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.SUBTREE_NODE)) {
			if (!xmlTreeDisplay.getSelectedNode().hasChildren() && (xmlTreeDisplay.getCopiedNode() != null)) {
				pasteMenu.setEnabled(true);
			}
		}
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
				subMenuBar.getElement().setAttribute("id", "SubMenuBarLogicalOp");
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
	
	
	private void relationalOpNodePopUpMenuItems(PopupPanel popupPanel) {
		MenuBar subMenuBarRelLHS = createMenuBarWithTimingFuncAndQDM(false);
		MenuBar relAssociationMenuBar = new MenuBar(true);
		
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relAssociationMenuBar); // Relationship sub menus 3rd level
		addMenuLHS = new MenuItem("Add LHS", subMenuBarRelLHS); //LHS Sub Menu
		MenuBar subMenuBarRelRHS = createMenuBarWithTimingFuncAndQDM(false);
		subMenuBarRelLHS.getElement().setAttribute("id", "subMenuRelOpLHS");
		MenuBar relAssociationMenuBarRHS = new MenuBar(true);
		subMenuBarRelLHS.getElement().setAttribute("id", "relAssociationMenuBarRHS");
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
		copyMenu.setEnabled(false);
		pasteMenu.setEnabled(false);
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
		subMenuBarEdit.getElement().setAttribute("id", "SubMenuBarRelOpEdit");
		createEditMenus(MatContext.get().relationships, subMenuBarEdit);
		editMenu = new MenuItem("Edit", true, subMenuBarEdit);
		popupMenuBar.addItem(editMenu);
		cutMenu.setEnabled(true);
		checkIsParentSatisfy();
	}
	
	private void functionNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		subMenuBar.getElement().setAttribute("id", "SubMenuBarFuncOp");
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		if(xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ALL") ||
				xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ANY")){
			MenuBar subMenuBarLHS = createMenuBarWithOnlyQDM();
			subMenuBarLHS.getElement().setAttribute("id","SubMenuFnxOpLHS");
			addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
			
			MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM(false);
			subMenuBarLHS.getElement().setAttribute("id","SubMenuFnxOpRHS");
			MenuBar relSetOpMenuBar = new MenuBar(true);
			subMenuBarLHS.getElement().setAttribute("id","SubMenuRelOpLHS");
			subMenuBarRHS.addItem("Relationship", relSetOpMenuBar); //functions menu 2nd level
			createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
					, relSetOpMenuBar);
			createAddClauseMenuItem(subMenuBarRHS);
			addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS); //RHS Sub Menu
			
			//Disable  RHS by default.
			if ((xmlTreeDisplay.getSelectedNode().getChilds() == null) || (xmlTreeDisplay.getSelectedNode().getChilds().size() == 0) ) {
				addMenuRHS.setEnabled(false);
			}
			//Disable LHS when One element is added and disable RHS when two elements are added.
			if (xmlTreeDisplay.getSelectedNode().getChilds() != null) {
				if (xmlTreeDisplay.getSelectedNode().getChilds().size() >= 1) {
					addMenuLHS.setEnabled(false);
				}
			}
			if(xmlTreeDisplay.getCopiedNode() != null){
				if( (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE) &&
						(xmlTreeDisplay.getSelectedNode().getChilds() != null) &&
						(xmlTreeDisplay.getSelectedNode().getChilds().size() >=1)) {
					if(xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
						String copiedFuncName = xmlTreeDisplay.getCopiedNode().getName().toUpperCase();
						
						@SuppressWarnings("unchecked")
						HashMap<String, String> map =  (HashMap<String, String>) xmlTreeDisplay.getCopiedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
						if(map != null){
							copiedFuncName = map.get(PopulationWorkSpaceConstants.TYPE).toUpperCase();
						}
						if(!isAllowedFunction(copiedFuncName)){
							pasteMenu.setEnabled(false);
						} else{
							pasteMenu.setEnabled(true);
						}
					}else{
						pasteMenu.setEnabled(true);
					}
				}else {
					pasteMenu.setEnabled(false);
				}
			}

			popupMenuBar.addItem(addMenuLHS);
			popupMenuBar.addItem(addMenuRHS);
			
		} else {//Menu Items for functions other than SATISFIES ALL and SATISFIES ANY
			createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
					, subMenuBar);
			createAddQDM_MenuItem(subMenuBar);
			MenuBar timing = new MenuBar(true);
			timing.getElement().setAttribute("id","SubMenuTimingLHS");
			subMenuBar.addItem("Timing", timing); //Timing menu 2nd level
			createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timing); // Timing sub menus 3rd level
			MenuBar functions = new MenuBar(true);
			timing.getElement().setAttribute("id","SubMenufunctions");
			subMenuBar.addItem("Functions", functions); //functions menu 2nd level
			List<String> functionsList = ComparisonDialogBox.filterFunctions(xmlTreeDisplay.getSelectedNode(),MatContext.get().functions);
			createAddMenus(functionsList, CellTreeNode.FUNCTIONS_NODE
					, functions); // functions sub menus 3rd level
			createAddClauseMenuItem(subMenuBar);
			addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
			
			String selectedFunctionName = xmlTreeDisplay.getSelectedNode().getName().toUpperCase();
			@SuppressWarnings("unchecked")
			HashMap<String, String> attribMap =  (HashMap<String, String>) xmlTreeDisplay.getSelectedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
			if(attribMap != null){
				selectedFunctionName = attribMap.get(PopulationWorkSpaceConstants.TYPE).toUpperCase();
			}
			
			if(ComparisonDialogBox.getAggregateFunctionsList().contains(selectedFunctionName) || ComparisonDialogBox.getSubSetFunctionsList().contains(selectedFunctionName)
					|| selectedFunctionName.contains(MatConstants.AGE_AT.toUpperCase())){
				if(xmlTreeDisplay.getSelectedNode().hasChildren()){
					addMenu.setEnabled(false);
				}
			}
			
			popupMenuBar.addItem(addMenu);
			
			if(xmlTreeDisplay.getCopiedNode() != null){
				if(ComparisonDialogBox.getAggregateFunctionsList().contains(selectedFunctionName)
						|| ComparisonDialogBox.getSubSetFunctionsList().contains(selectedFunctionName)
						|| selectedFunctionName.contains(MatConstants.AGE_AT.toUpperCase())) {
					if(xmlTreeDisplay.getSelectedNode().hasChildren()){
						pasteMenu.setEnabled(false);
					}else if( (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE) ) {
						if(xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
							@SuppressWarnings("unchecked")
							HashMap<String, String> map =  (HashMap<String, String>) xmlTreeDisplay.getCopiedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
							String copiedFuncName = xmlTreeDisplay.getCopiedNode().getName().toUpperCase();
							if(map != null){
								copiedFuncName = map.get(PopulationWorkSpaceConstants.TYPE);
							}
							if(!isAllowedFunction(copiedFuncName)){
								pasteMenu.setEnabled(false);
							} else{
								pasteMenu.setEnabled(true);
							}
						}else{
							pasteMenu.setEnabled(true);
						}
					}else {
						pasteMenu.setEnabled(false);
					}
				}else{
					if((xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)) {
						if(xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
							@SuppressWarnings("unchecked")
							HashMap<String, String> map =  (HashMap<String, String>) xmlTreeDisplay.getCopiedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
							String copiedFuncName = xmlTreeDisplay.getCopiedNode().getName().toUpperCase();
							if(map != null){
								copiedFuncName = map.get(PopulationWorkSpaceConstants.TYPE).toUpperCase();
							}
							if(!isAllowedFunction(copiedFuncName)){
								pasteMenu.setEnabled(false);
							} else{
								pasteMenu.setEnabled(true);
							}
						}else{
							pasteMenu.setEnabled(true);
						}
					}else {
						pasteMenu.setEnabled(false);
					}
				}
			}
			System.out.println("paste menu enabled2?"+pasteMenu.isEnabled());
		}
		
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		
		System.out.println("paste menu enabled3?"+pasteMenu.isEnabled());

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
		if(xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ALL") ||
				xmlTreeDisplay.getSelectedNode().getName().equalsIgnoreCase("SATISFIES ANY")) {
			editMenu.setEnabled(false);
		} else {
			editMenu.setEnabled(true);
		}
		checkIsParentSatisfy();
	}
	/**
	 * Set Op Node Pop up Menu Items.
	 * @param popupPanel - PopupPanel.
	 */
	@SuppressWarnings("unchecked")
	private void setOpsNodePopUpMenuItems(final PopupPanel popupPanel) {
		subMenuBar = new MenuBar(true);
		subMenuBar.getElement().setAttribute("id", "SubMenuBarSetOp");
		popupMenuBar.setAutoOpen(true);
		subMenuBar.setAutoOpen(true);
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, subMenuBar);
		createAddQDM_MenuItem(subMenuBar);
		MenuBar timingSetOpMenuBar = new MenuBar(true);
		timingSetOpMenuBar.getElement().setAttribute("id", "SubMenuBartimingSetOpMenuBar");
		subMenuBar.addItem("Timing", timingSetOpMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings,
				CellTreeNode.TIMING_NODE, timingSetOpMenuBar); // Timing sub menus 3rd level
		MenuBar functionsSetOpMenuBar = new MenuBar(true);
		functionsSetOpMenuBar.getElement().setAttribute("id", "SubMenuBarfunctionsSetOpMenuBar");
		subMenuBar.addItem("Functions", functionsSetOpMenuBar); //functions menu 2nd level
		List<String> functionsList = ComparisonDialogBox.filterFunctions(xmlTreeDisplay.getSelectedNode(),MatContext.get().functions);
		createAddMenus(functionsList, CellTreeNode.FUNCTIONS_NODE
				, functionsSetOpMenuBar); // functions sub menus 3rd level
		MenuBar relSetOpMenuBar = new MenuBar(true);
		relSetOpMenuBar.getElement().setAttribute("id", "SubMenuBarrelSetOpMenuBar");
		subMenuBar.addItem("Relationship", relSetOpMenuBar); //functions menu 2nd level
		createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE
				, relSetOpMenuBar); // Timing sub menus 3rd level
		createAddClauseMenuItem(subMenuBar);
		addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
		popupMenuBar.addItem(addMenu);
		popupMenuBar.addSeparator(separator);
		addCommonMenus();
		copyMenu.setEnabled(true);
		if (xmlTreeDisplay.getCopiedNode() != null){
			if (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE) {
				pasteMenu.setEnabled(true);
			}
			if(xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
				HashMap<String, String> map =  (HashMap<String, String>) xmlTreeDisplay.getCopiedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				String funcName = xmlTreeDisplay.getCopiedNode().getName();
				if(map != null){
					funcName = map.get(PopulationWorkSpaceConstants.TYPE);
				}
				
				if(!isAllowedFunction(funcName)){
					pasteMenu.setEnabled(false);
				} else {
					pasteMenu.setEnabled(true);
				}
				
			}
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
			subMenuBar.getElement().setAttribute("id", "SubMenuBarEditSetOp");
			createEditMenus(MatContext.get().setOps, subMenuBar);
			editMenu = new MenuItem("Edit", true, subMenuBar);
			popupMenuBar.addItem(editMenu);
		}
		checkIsParentSatisfy();
	}
	
	
	private boolean isAllowedFunction(String funcName) {
		List<String> allowedFunctionsList = ComparisonDialogBox.filterFunctions(xmlTreeDisplay.getSelectedNode(), MatContext.get().functions);
		boolean retValue = false;
		for(String name:allowedFunctionsList){
			if(name.equalsIgnoreCase(funcName)){
				retValue = true;
				break;
			}
		}
		
		return retValue;
	}
	
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
		checkIsParentSatisfy();
	}
	
	
	private void checkIsParentSatisfy(){
		if((xmlTreeDisplay.getSelectedNode().getParent().getLabel().equalsIgnoreCase("SATISFIES ALL") ||
				xmlTreeDisplay.getSelectedNode().getParent().getLabel().equalsIgnoreCase("SATISFIES ANY"))){
			deleteMenu.setEnabled(checkIfTopChildNode());
			cutMenu.setEnabled(checkIfTopChildNode());
			moveUpMenu.setEnabled(checkIfTopChildNodeForSatisfy());
			moveDownMenu.setEnabled(checkIfLastChildNodeForSatisfy());
		}
		
	}

	private void timingNodePopUpMenuItems(final PopupPanel popupPanel) {
		MenuBar subMenuBarLHS = createMenuBarWithTimingFuncAndQDM(true);
		addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
		addMenuLHS.getElement().setAttribute("id", "SubMenuBaraddMenuLHS");
		MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM(true);
		addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS); //RHS Sub Menu
		addMenuRHS.getElement().setAttribute("id", "SubMenuBaraddMenuRHS");
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
		if (xmlTreeDisplay.getCopiedNode() != null){
			
			if( (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)
					&& ((xmlTreeDisplay.getSelectedNode().getChilds() == null)
							|| (xmlTreeDisplay.getSelectedNode().getChilds().size() < 2))) {
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getCopiedNode().getNodeType() == CellTreeNode.FUNCTIONS_NODE){
				@SuppressWarnings("unchecked")
				HashMap<String, String> map =  (HashMap<String, String>) xmlTreeDisplay.getCopiedNode().getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				String funcName = xmlTreeDisplay.getCopiedNode().getName();
				if(map != null){
					funcName = map.get(PopulationWorkSpaceConstants.TYPE);
				}
				
				if(!isAllowedFunction(funcName)){
					pasteMenu.setEnabled(false);
				} else{
					pasteMenu.setEnabled(true);
				}
			}
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
		checkIsParentSatisfy();
	}
	
	protected void showHideExpandMenu() {
		if (xmlTreeDisplay.getSelectedNode().hasChildren()) {
			expandMenu.setEnabled(true);
		} else {
			expandMenu.setEnabled(false);
		}
	}
	
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
	
	protected void showQDMAttributePopup(CellTreeNode cellTreeNode) {
		QDMAttributeDialogBox.showQDMAttributeDialogBox(xmlTreeDisplay, cellTreeNode);
	}
	

	protected MenuBar createMenuBarWithTimingFuncAndQDM(boolean addClauseMenuItem) {
		MenuBar menuBar = new MenuBar(true);
		menuBar.getElement().setAttribute("id", "SubMenuBarmenuBar");
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		//CellTreeNode.LOGICAL_OP_NODE, menuBar);// creating logical Operators Menu 2nd level
		createAddMenus(MatContext.get().setOps, CellTreeNode.SET_OP_NODE
				, menuBar);
		createAddQDM_MenuItem(menuBar);
		MenuBar timingMenuBar = new MenuBar(true);
		timingMenuBar.getElement().setAttribute("id", "timingMenuBar");
		menuBar.addItem("Timing", timingMenuBar); //Timing menu 2nd level
		createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timingMenuBar); // Timing sub menus 3rd level
		MenuBar functionsMenuBar = new MenuBar(true);
		menuBar.addItem("Functions", functionsMenuBar); //functions menu 2nd level
		List<String> functionList = ComparisonDialogBox.filterFunctions(xmlTreeDisplay.getSelectedNode(),MatContext.get().functions);
		createAddMenus(functionList, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar); // functions sub menus 3rd level
		if(addClauseMenuItem){
			createAddClauseMenuItem(menuBar);
		}
		return menuBar;
	}
	
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

	protected void showQDMPopup(boolean isAdd) {
		QDMDialogBox.showQDMDialogBox(xmlTreeDisplay, isAdd);
	}

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

	protected void pasteMasterRootNodeTypeItem() {
		String rootNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = rootNodeName.substring(0, rootNodeName.lastIndexOf(" ")) + " " + seqNumber;
		CellTreeNode rootNode = xmlTreeDisplay.getCopiedNode();
		rootNode.setUUID(UUIDUtilClient.uuid());
		CellTreeNode pasteNode = rootNode.cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
		CellTreeNode clonedNode = pasteNode.cloneNode();//created new instance for pasted node
		clonedNode.setParent(pasteNode.getParent());//set parent of the cloned node
		xmlTreeDisplay.setCopiedNode(clonedNode);  //make the new cloned node as the copied node
		/*
		 * POC Global Copy Paste.
		 *MatContext.get().setCopiedNode(clonedNode);*/
	}
	
	protected void addRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		String selectedNodeName = xmlTreeDisplay.getSelectedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber;
		
		xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.CLAUSE_NODE);
		selectedNodeName.replaceAll("\\s+", "").toLowerCase();

		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}
	
	protected void addMasterRootNodeTypeItem() {
		String rootNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = rootNodeName.substring(0, rootNodeName.lastIndexOf(" ")) + " " + seqNumber;
		
		CellTreeNode rootNode  = xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.ROOT_NODE);
		rootNode.setUUID(UUIDUtilClient.uuid());
		rootNode.createChild(DEFAULT_STRATUM_NODE, DEFAULT_STRATUM_NODE, CellTreeNode.CLAUSE_NODE);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}
	
	protected void addCommonMenus() {
		popupMenuBar.addItem(deleteMenu);
		popupMenuBar.setVisible(true);
		popupPanel.add(popupMenuBar);
	}
	
	
	protected String getAddMenuName(CellTreeNode selectedNode) {
		return "Add" + " " +  selectedNode.getName().substring(0, selectedNode.getName().lastIndexOf(" "));
	}

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
	

	private final boolean checkIfTopChildNodeForSatisfy() {
		CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
		CellTreeNode parentNode = selectedNode.getParent();
		if ((parentNode.getChilds() == null) || (parentNode.getChilds().size() <= 2)) {
			return false;
		}
		for (int i = 0; i <= parentNode.getChilds().size(); i++) {
			if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
				return i > 1;
			}
		}
		return true;
	}
	
	private final boolean checkIfLastChildNodeForSatisfy() {
		CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
		CellTreeNode parentNode = selectedNode.getParent();
		if ((parentNode.getChilds() == null) || (parentNode.getChilds().size() <= 2)) {
			return false;
		}
		for (int i = 0; i <= parentNode.getChilds().size(); i++) {
			if((i == 0) && selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
				return false;
			}
			if (selectedNode.equals(selectedNode.getParent().getChilds().get(i))) {
				return !(i == (parentNode.getChilds().size() - 1));
			}
		}
		return true;
	}
	
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

	public native void showHumanReadableDialogBox(String result, String populationName) /*-{
		var dummyURL = window.location.protocol + "//" +  window.location.hostname + ":" + window.location.port + "/" + "mat/humanreadable.html";
		var humanReadableWindow = window.open(dummyURL,"","width=1200,height=700,scrollbars=yes,resizable=yes");
		
		if(humanReadableWindow && humanReadableWindow.top){
			humanReadableWindow.document.write(result);
			humanReadableWindow.document.title = populationName;
		}
	}-*/;

	public MenuItem getViewHumanReadableMenu() {
		return viewHumanReadableMenu;
	}

	protected MenuBar createMenuBarWithOnlyQDM() {
		MenuBar menuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		createAddQDM_MenuItem(menuBar);
		return menuBar;
	}


	
}
