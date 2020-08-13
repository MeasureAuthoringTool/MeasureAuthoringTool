package mat.client.clause.clauseworkspace.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.XmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.MatContext;

public class PopulationWorkSpaceContextMenu extends ClauseWorkspaceContextMenu {

	private static final String STRATUM = "Stratum";
	
	private static final String MEASURE_OBSERVATION = "Measure Observation";

	private static final String STRATIFICATION = "Stratification";
	
	private static final String MEASURE_OBSERVATIONS = "Measure Observations";
	
	MenuItem viewHumanReadableMenu;

	public PopulationWorkSpaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		super(treeDisplay, popPanel);
		Command viewHumanReadableCmd = new Command() {
			@Override
			public void execute() {
				System.out.println("View Human Readable clicked...");
				popupPanel.hide();
				CellTreeNode selectedNode = xmlTreeDisplay.getSelectedNode();
				if ((selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE)
						|| (selectedNode.getNodeType() == CellTreeNode.SUBTREE_NODE)) {
					String xmlForPopulationNode = XmlConversionlHelper.createXmlFromTree(selectedNode);
					final String populationName = selectedNode.getName();
					String measureId = MatContext.get().getCurrentMeasureId();
					xmlTreeDisplay.validatePopulationCellTreeNodes(xmlTreeDisplay.getSelectedNode());
					if (xmlTreeDisplay.isValidHumanReadable()) {
						MatContext.get().getMeasureService().getHumanReadableForNode(measureId, xmlForPopulationNode, new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								showHumanReadableDialogBox(result, populationName);
							}
							@Override
							public void onFailure(Throwable caught) {
							}
						});
					} else {
						xmlTreeDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
								.getMessageDelegate().getINVALID_LOGIC_CQL_WORK_SPACE());
					}
				}
			}
		};
		viewHumanReadableMenu = new MenuItem(template.menuTable("View Human Readable", ""), viewHumanReadableCmd);
	}

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
				
				addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0))
						, true, addNodeCmd);
				popupMenuBar.addItem(addMenu);
				popupMenuBar.addSeparator(separator);
				addCommonMenus();
				addMenu.setEnabled(true);
				
				if ((xmlTreeDisplay.getSelectedNode().getName().contains(STRATIFICATION)
						|| xmlTreeDisplay.getSelectedNode().getName().contains(MEASURE_OBSERVATIONS))
						&& (xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1)) {
					deleteMenu.setEnabled(true);
				}
				break;
			case CellTreeNode.CQL_DEFINITION_NODE:				
				deleteMenu.setEnabled(true);
				Command replaceCQLDefinitionCmd = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						//To show CQL Definitions on Population Workspace
						CQLArtifactsDialogBox.showCQLArtifactsDialogBox(xmlTreeDisplay, false, true);
					}
				};
				replaceMenu = new MenuItem("Replace", true, replaceCQLDefinitionCmd);
				replaceMenu.setEnabled(true);
				popupMenuBar.addItem(deleteMenu);
				popupMenuBar.addItem(replaceMenu);
				popupMenuBar.setVisible(true);
				popupPanel.add(popupMenuBar);
				
				break;
			case CellTreeNode.CQL_FUNCTION_NODE:
				deleteMenu.setEnabled(true);
				Command replaceCQLFunctionCmd = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						//To show CQL Definitions on Population Workspace
						CQLArtifactsDialogBox.showCQLArtifactsDialogBox(xmlTreeDisplay, false, false);
					}
				};
				replaceMenu = new MenuItem("Replace", true, replaceCQLFunctionCmd);
				replaceMenu.setEnabled(true);
				popupMenuBar.addItem(deleteMenu);
				popupMenuBar.addItem(replaceMenu);
				popupMenuBar.setVisible(true);
				popupPanel.add(popupMenuBar);
				break;
			case CellTreeNode.CQL_AGG_FUNCTION_NODE:	
				subMenuBar = new MenuBar(true);
				popupMenuBar.setAutoOpen(true);
				subMenuBar.setAutoOpen(true);				
				Command addCQLFunctionCmd = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						//To show CQL Definitions on Population Workspace
						CQLArtifactsDialogBox.showCQLArtifactsDialogBox(xmlTreeDisplay, true, false);
					}
				};
				addMenu = new MenuItem("Add Function", true, addCQLFunctionCmd);
				
				popupMenuBar.addItem(addMenu);
	
				if(xmlTreeDisplay.getSelectedNode().hasChildren()){
					addMenu.setEnabled(false);
				}
				
				Command addCQLAggFunctionCmd = new Command() {
					@Override
					public void execute() {
						popupPanel.hide();
						//To show CQL Definitions on Population Workspace
						CQLArtifactsDialogBox.showEditCQLAggFuncDialogBox(xmlTreeDisplay, false);
					}
				};
				replaceMenu = new MenuItem("Replace", true, addCQLAggFunctionCmd);
				deleteMenu.setEnabled(true);
				replaceMenu.setEnabled(true);
				popupMenuBar.addItem(deleteMenu);
				popupMenuBar.addItem(replaceMenu);
								
				popupMenuBar.setVisible(true);
				popupPanel.add(popupMenuBar);
				break;	
			case CellTreeNode.CLAUSE_NODE:							
					
				if(!xmlTreeDisplay.getSelectedNode().getName().contains(MEASURE_OBSERVATION)) {
					Command addCQLDefinitionCmd = new Command() {
						@Override
						public void execute() {
							popupPanel.hide();
							//To show CQL Definitions on Population Workspace
							CQLArtifactsDialogBox.showCQLArtifactsDialogBox(xmlTreeDisplay, true, true);
						}
					};
					addMenu = new MenuItem("Add Definition", true, addCQLDefinitionCmd); // 1st level menu

					popupMenuBar.addItem(addMenu);
					
								
				}else{
					subMenuBar = new MenuBar(true);
					subMenuBar.setAutoOpen(true);	
					Command addCQLAggregateFuncCmd = new Command() {
						@Override
						public void execute() {
							popupPanel.hide();
							//To show CQL Definitions on Population Workspace
							CQLArtifactsDialogBox.showEditCQLAggFuncDialogBox(xmlTreeDisplay, true);
						}
					};
					addMenu = new MenuItem("Add Aggregate Function", true, addCQLAggregateFuncCmd);
					
					Command addCQLFuncCmd = new Command() {
						@Override
						public void execute() {
							popupPanel.hide();
							//To show CQL Definitions on Population Workspace
							CQLArtifactsDialogBox.showCQLArtifactsDialogBox(xmlTreeDisplay, true, false);
						}
					};
					addFuncMenu = new MenuItem("Add Function", true, addCQLFuncCmd);
					addFuncMenu.setEnabled(true);
					
					if(xmlTreeDisplay.getSelectedNode().hasChildren()){
						addFuncMenu.setEnabled(false);
					}
					popupMenuBar.addItem(addMenu);
					popupMenuBar.addItem(addFuncMenu);
									
				}
				popupMenuBar.setAutoOpen(true);
				
				if(xmlTreeDisplay.getSelectedNode().hasChildren()){
					addMenu.setEnabled(false);
				}
				addCommonMenus();
								
				if (xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1) {
					deleteMenu.setEnabled(true);
				}
				popupMenuBar.addItem(viewHumanReadableMenu);
				viewHumanReadableMenu.setEnabled(true);
							
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
				if ((xmlTreeDisplay.getCopiedNode() != null)
						&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE)
						&& (xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.ROOT_NODE)) {
					pasteMenu.setEnabled(true);
				}

				if ((xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE)
						|| (xmlTreeDisplay.getSelectedNode().getParent().getName().contains(STRATUM))
						|| (xmlTreeDisplay.getSelectedNode().getParent().getName().contains(MEASURE_OBSERVATION))) {
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
				//commonMenuEnableState(false, false, true, false, true);
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.ELEMENT_REF_NODE:
				addCommonMenus();
				//commonMenuEnableState(false, false, true, false, true);
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				addCommonMenus();
				//commonMenuEnableState(false, false, true, false, true);
				copyMenu.setEnabled(false);
				cutMenu.setEnabled(false);
				deleteMenu.setEnabled(true);
				pasteMenu.setEnabled(false);
				expandMenu.setEnabled(true);
				break;
			case CellTreeNode.RELATIONSHIP_NODE:
				addCommonMenus();
				//commonMenuEnableState(false, false, true, false, true);
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
