package mat.client.clause.clauseworkspace.presenter;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.view.ClauseWorkspaceContextMenu;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.WarningMessageDisplay;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
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
	 * @param treeNode the tree node
	 * @return the validate btn
	 */
	//Commented Validate Button from Population Work Space as part of Mat-3162
	//Button getValidateBtn();
	
	/**
	 * Validate cell tree nodes.
	 * 
	 * @param treeNode
	 *            the tree node
	 * @return true, if successful
	 */
	boolean validateCellTreeNodes(TreeNode treeNode);
	
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
	 * @param isValideNodeValue
	 *            the is valide node value
	 * @param node
	 *            the node
	 * @param subTree
	 *            the sub tree
	 */
	void editNode(boolean isValideNodeValue, CellTreeNode node,
			TreeNode subTree);
	
	/**
	 * Sets the valid.
	 * 
	 * @param isValid
	 *            the new valid
	 */
	void setValid(boolean isValid);
	
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
	 * @return Button.
	 */
	Button getCommentButtons();
	
	/**
	 * @return TextArea.
	 */
	mat.client.clause.clauseworkspace.view.XmlTreeView.CommentAreaTextBox getCommentArea();
	
	/**
	 * Success Message Panel for Add Comment.
	 * @return
	 */
	SuccessMessageDisplay getSuccessMessageAddCommentDisplay();
	
	void addCommentNode();
}
