package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Widget;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;

// TODO: Auto-generated Javadoc
/**
 * The Interface XmlTreeDisplay.
 */
public interface CQLXmlTreeDisplay {
	
	/**
	 * Gets the xml tree.
	 * 
	 * @return the xml tree
	 */
	public CellTree getXmlTree();
	
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget();
	
	/**
	 * Clear messages.
	 */
	public void clearMessages();
	
	/**
	 * Gets the selected node.
	 * 
	 * @return the selected node
	 */
	public CQLCellTreeNode getSelectedNode();
	
	
	/**
	 * Expand selected.
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	public void expandSelected(TreeNode treeNode);
	
	
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
	
	}
