package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.view.ClauseWorkspaceContextMenu;
import mat.client.clause.clauseworkspace.view.PopulationWorkSpaceContextMenu;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.WarningMessageDisplay;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;

import java.util.List;

/**
 * The Interface XmlTreeDisplay.
 */
public interface XmlTreeDisplay {
	
	/**
	 * Gets the xml tree.
	 * 
	 * @return the xml tree
	 */
	public CellTree getXmlTree();
	
	/**
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	public Button getSaveButton();
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget();
	
	/**
	 * Gets the success message display.
	 * 
	 * @return the success message display
	 */
	public SuccessMessageDisplay getSuccessMessageDisplay();
	
	/**
	 * Gets the error message display.
	 * 
	 * @return the error message display
	 */
	public ErrorMessageDisplay getErrorMessageDisplay();
	
	/**
	 * Gets the warning message display.
	 * 
	 * @return the warning message display
	 */
	public WarningMessageDisplay getWarningMessageDisplay();
	
	/**
	 * Clear messages.
	 */
	public void clearMessages();
	
	/**
	 * Sets the enabled.
	 * 
	 * @param enable
	 *            the new enabled
	 */
	public void setEnabled(boolean enable);
	
	/**
	 * Gets the selected node.
	 * 
	 * @return the selected node
	 */
	public CellTreeNode getSelectedNode();
	
	/**
	 * Adds the node.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 * @param nodeType
	 *            the node type
	 * @return the cell tree node
	 */
	public CellTreeNode addNode(String name, String label, short nodeType);
	
	/**
	 * Removes the node.
	 */
	public void removeNode();
	
	/**
	 * Copy.
	 */
	public void copy();
	
	/**
	 * Move Node up in List.
	 */
	public void moveUp();
	
	/**
	 * Paste.
	 */
	public void paste();
	
	/**
	 * Gets the copied node.
	 * 
	 * @return the copied node
	 */
	public CellTreeNode getCopiedNode();
	
	/**
	 * Refresh cell tree after adding.
	 * 
	 * @param selectedNode
	 *            the selected node
	 */
	void refreshCellTreeAfterAdding(CellTreeNode selectedNode);
	
	/**
	 * Edits the node.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 */
	public void editNode(String name, String label);
	
	/**
	 * Sets the copied node.
	 * 
	 * @param cellTreeNode
	 *            the new copied node
	 */
	public void setCopiedNode(CellTreeNode cellTreeNode);
	
	
	/**
	 * Sets the dirty.
	 * 
	 * @param isDirty
	 *            the new dirty
	 */
	public void setDirty(boolean isDirty);
	
	/**
	 * Checks if is dirty.
	 * 
	 * @return true, if is dirty
	 */
	public boolean isDirty();
	
	/**
	 * Expand selected.
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	public void expandSelected(TreeNode treeNode);
	
	/**
	 * Adds the node.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 * @param uuid
	 *            the uuid
	 * @param nodeType
	 *            the node type
	 * @return the cell tree node
	 */
	public CellTreeNode addNode(String name, String label, String uuid, short nodeType);
	
	/**
	 * Edits the node.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 * @param uuid
	 *            the uuid
	 */
	public void editNode(String name, String label, String uuid);
	
	/**
	 * Gets the validate btn.
	 *
	 * @return the validate btn
	 */
	//Commented Validate Button from Population Work Space as part of Mat-3162
	// Back to commeneted as part of MAT-7837
	//Button getValidateBtn();
	/**
	 * Gets the validate btn populationWorkspace.
	 *
	 * @param treeNode the tree node
	 * @return the validate btn
	 */
//	Button getValidateBtnPopulationWorkspace();
	/**
	 * Validate cell tree nodes populationWorkspace.
	 * 
	 * @param treeNode
	 *            the tree node
	 * @return list
	 */
	List<String> validateCellTreeNodesPopulationWorkspace(TreeNode treeNode);
	
	/**
	 * Validate cell tree nodes.
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	//void validateCellTreeNodes(TreeNode treeNode);
	
	/**
	 * Close nodes.
	 * 
	 * @param node
	 *            the node
	 */
	void closeNodes(TreeNode node);
	
	/**
	 * Open all nodes.
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	void openAllNodes(TreeNode treeNode);
	
	/**
	 * Edits the node.
	 *
	 * @param isValid the new valid
	 */
	//	void editNode(boolean isValideNodeValue, CellTreeNode node,
	//			TreeNode subTree);
	
	/**
	 * Gets the cell tree.
	 *
	 * @return the cell tree
	 */
	CellTree getCellTree();
	
	/**
	 * Sets the cell tree.
	 *
	 * @param cellTree the new cell tree
	 */
	void setCellTree(CellTree cellTree);
	
	/**
	 * Gets the save btn clause work space.
	 *
	 * @return the save btn clause work space
	 */
	Button getSaveBtnClauseWorkSpace();
	
	/**
	 * Gets the validate btn clause work space.
	 *
	 * @return the validate btn clause work space
	 */
	Button getValidateBtnClauseWorkSpace();
	
	/**
	 * Gets the clear clause work space.
	 *
	 * @return the clear clause work space
	 */
	Button getClearClauseWorkSpace();
	
	/**
	 * Sets the clause workspace context menu.
	 *
	 * @param clauseWorkspaceContextMenu the new clause workspace context menu
	 */
	void setClauseWorkspaceContextMenu(ClauseWorkspaceContextMenu clauseWorkspaceContextMenu);
	
	/**
	 * Gets the clear error display.
	 *
	 * @return the clear error display
	 */
	public ErrorMessageDisplay getClearErrorDisplay();
	
	/**
	 * Gets the comment buttons.
	 *
	 * @return Button.
	 */
	Button getCommentButtons();
	
	/**
	 * Gets the comment area.
	 *
	 * @return TextArea.
	 */
	mat.client.clause.clauseworkspace.view.XmlTreeView.CommentAreaTextBox getCommentArea();
	
	/**
	 * Success Message Panel for Add Comment.
	 *
	 * @return the success message add comment display
	 */
	SuccessMessageDisplay getSuccessMessageAddCommentDisplay();
	
	/**
	 *  On Save, Comment Added to Selected Nodes are also Saved.
	 */
	void addCommentNodeToSelectedNode();
	
	/**
	 * Update suggest oracle.
	 */
	public abstract void updateSuggestOracle();
	
	/**
	 * Clear and add clause names to list box.
	 */
	public abstract void clearAndAddClauseNamesToListBox();
	
	/**
	 * Gets the clause names list box.
	 *
	 * @return the clause names list box
	 */
	public abstract ListBox getClauseNamesListBox();
	
	/**
	 * Gets the show clause button.
	 *
	 * @return the show clause button
	 */
	public abstract Button getShowClauseButton();
	
	/**
	 * Gets the delete clause button.
	 *
	 * @return the delete clause button
	 */
	Button getDeleteClauseButton();
	
	/**
	 * Refresh Cell Node to show inline comment indicator.
	 * @param selectedNode - CellTreeNode.
	 */
	void refreshCellTreeAfterAddingComment(CellTreeNode selectedNode);
	
	/**
	 * Move Node Down in List.
	 */
	void moveDown();
	
	/**
	 * Gets the button collapse clause work space.
	 *
	 * @return the button collapse clause work space
	 */
	public abstract Button getButtonCollapseClauseWorkSpace();
	
	/**
	 * Gets the button expand clause work space.
	 *
	 * @return the button expand clause work space
	 */
	public abstract Button getButtonExpandClauseWorkSpace();
	
	/**
	 * Edits the node.
	 *
	 * @param isValideNodeValue the is valide node value
	 * @param node the node
	 */
	void editNode(boolean isValideNodeValue, CellTreeNode node);
	
	/**
	 * Sets the clause enabled.
	 *
	 * @param isClauseOpen the new clause enabled
	 */
	public void setClauseEnabled(boolean isClauseOpen);
	
	/**
	 * Gets the include qdm varibale.
	 *
	 * @return the include qdm varibale
	 */
	CheckBox getIncludeQdmVaribale();
	
	/**
	 * Sets the qdm variable.
	 *
	 * @param isQdmVariable the new qdm variable
	 */
	public void setQdmVariable(String isQdmVariable);
	
	/**
	 * Checks if is qdm variable.
	 *
	 * @return the string
	 */
	public String isQdmVariable();
	
	/**
	 * Checks if is qdm variable dirty.
	 *
	 * @return true, if is qdm variable dirty
	 */
	boolean isQdmVariableDirty();
	
	/**
	 * Sets the qdm variable dirty.
	 *
	 * @param isQdmVariableDirty the new qdm variable dirty
	 */
	void setQdmVariableDirty(boolean isQdmVariableDirty);
	/*
	 * POC Global Copy Paste
	void copyToClipboard();
	
	void pasteFromClipboard();
	 */
	
	/**
	 * Gets the search suggest text box.
	 *
	 * @return the search suggest text box
	 */
	SuggestBox getSearchSuggestTextBox();
	
	void validateCellTreeNodes(TreeNode treeNode, boolean isValidateButtonClicked);
	
	boolean isValidHumanReadable();
	
	List<String> validatePopulationCellTreeNodes(CellTreeNode cellNode);

	void setpopulationWorkspaceContextMenu(PopulationWorkSpaceContextMenu populationWorkspaceContextMenu);
}
