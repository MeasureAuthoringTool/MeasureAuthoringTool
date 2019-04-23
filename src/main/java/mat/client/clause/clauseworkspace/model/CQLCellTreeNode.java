package mat.client.clause.clauseworkspace.model;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * CQLCellTreeNode Interface.
 */
public interface CQLCellTreeNode {
	/**
	 * Attribute Node.
	 */
	short ATTRIBUTE_NODE = 9;
	/**
	 * Clause Node.
	 */
	short CLAUSE_NODE = 3;
	/**
	 * Element Ref Node.
	 */
	short ELEMENT_REF_NODE = 6;
	/**
	 * Function Node.
	 */
	short FUNCTIONS_NODE = 7;
	/**
	 * Logical Op Node.
	 */
	short LOGICAL_OP_NODE = 4;
	/**
	 * Master Node.
	 */
	short MASTER_ROOT_NODE = 1;
	/**
	 * RelationShip Node.
	 */
	short RELATIONSHIP_NODE = 8;
	/**
	 * Root Node.
	 */
	short ROOT_NODE = 2;
	/**
	 * Timing Node.
	 */
	short TIMING_NODE = 5;
	/**
	 * Sub tree Node.
	 */
	short SUBTREE_NODE = 10;
	/**
	 * Sub tree Root Node.
	 */
	short SUBTREE_ROOT_NODE = 11;
	
	/**
	 * Sub tree Ref Node.
	 */
	short SUBTREE_REF_NODE = 12;
	
	/**
	 * Comment Node.
	 */
	short COMMENT_NODE = 13;
	
	
	/** The set op node. */
	short SET_OP_NODE = 14;
	
	/** The CQL DEfinition node */
	short CQL_DEFINITION_NODE = 15;  
	short CQL_FUNCTION_NODE = 16;
	short CQL_AGG_FUNCTION_NODE = 17;
	
	short MAIN_NODE = 20;
	/**
	 * Append child.
	 * 
	 * @param child
	 *            - {@link CQLCellTreeNode}
	 * @return {@link CQLCellTreeNode}
	 */
	CQLCellTreeNode appendChild(CQLCellTreeNode child);
	
	/**
	 * Clone node.
	 * 
	 * @return {@link CQLCellTreeNode}.
	 */
	CQLCellTreeNode cloneNode();
	/**
	 * Create Child in selected Node with given Name, Label and Node Type.
	 * @param name
	 *            - {@link String}
	 * @param label
	 *            - {@link String}
	 * @param nodeType
	 *            - {@link Short}.
	 * @return - {@link CQLCellTreeNode}.
	 */
	CQLCellTreeNode createChild(String name, String label, short nodeType);
	
	/**
	 * Gets the childs.
	 * 
	 * @return {@link List} of {@link CQLCellTreeNode}.
	 */
	List<CQLCellTreeNode> getChilds();
	
	/**
	 * Gets the extra information.
	 * 
	 * @param key
	 *            - {@link String}
	 * @return - {@link Object}.
	 */
	Object getExtraInformation(String key);
	/**
	 * Return's Node Label value.
	 * @return {@link String}
	 */
	String getLabel();
	/**
	 * Node Name is returned.
	 * @return {@link String}
	 */
	String getName();
	
	/**
	 * Gets the node type.
	 * 
	 * @return Node Type - {@link Short}.
	 */
	short getNodeType();
	
	/**
	 * Gets the parent.
	 * 
	 * @return Node Parent - {@link CQLCellTreeNode}.
	 */
	CQLCellTreeNode getParent();
	
	/**
	 * Gets the qdm attribute.
	 * 
	 * @return {@link String}
	 */
	String getQdmAttribute();
	
	/**
	 * Gets the qdm attribute count.
	 * 
	 * @return QDM Attribute Count - {@link Integer}.
	 */
	int getQdmAttributeCount();
	
	/**
	 * Gets the title.
	 * 
	 * @return Node Title - {@link String}.
	 */
	String getTitle();
	
	/**
	 * Gets the uuid.
	 * 
	 * @return UUID - {@link String}
	 */
	String getUUID();
	
	/**
	 * Gets the valid node.
	 * 
	 * @return Boolean - Valid Node.
	 */
	boolean getValidNode();
	
	/**
	 * Checks for children.
	 * 
	 * @return true if Node has child else return false.
	 */
	boolean hasChildren();
	
	/**
	 * Checks if is open.
	 * 
	 * @return true if Node is opened else return false.
	 */
	boolean isOpen();
	/**
	 * Remove child node attached to Parent node.
	 * @param child
	 *            - {@link CQLCellTreeNode}.
	 * @return sub Tree will removed Node - {@link CQLCellTreeNode}.
	 */
	CQLCellTreeNode removeChild(CQLCellTreeNode child);
	
	/**
	 * Sets the childs.
	 * 
	 * @param childs
	 *            - {@link List} of {@link CQLCellTreeNode}.
	 */
	void setChilds(List<CQLCellTreeNode> childs);
	
	/**
	 * Sets the extra information.
	 * 
	 * @param key
	 *            - {@link String}
	 * @param value
	 *            - {@link Object}.
	 */
	void setExtraInformation(String key, Object value);
	
	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            - Node Label - {@link String}.
	 */
	void setLabel(String label);
	
	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            - Node Name - {@link String}.
	 */
	void setName(String name);
	
	/**
	 * Sets the node type.
	 * 
	 * @param nodeType
	 *            - Node Type - {@link String}.
	 */
	void setNodeType(short nodeType);
	
	/**
	 * Sets the open.
	 * 
	 * @param isOpen
	 *            - {@link Boolean}.
	 */
	void setOpen(boolean isOpen);
	
	/**
	 * Sets the parent.
	 * 
	 * @param treeNode
	 *            - Parent Node - {@link CQLCellTreeNode}
	 */
	void setParent(CQLCellTreeNode treeNode);
	
	/**
	 * Sets the uuid.
	 * 
	 * @param uuid
	 *            - {@link String}
	 */
	void setUUID(String uuid);
	
	/**
	 * Sets the valid node.
	 * 
	 * @param isValid
	 *            - {@link Boolean}
	 */
	void setValidNode(boolean isValid);
	
	/**
	 * Gets the node text.
	 *
	 * @return Node Text
	 */
	String getNodeText();
	
	/**
	 * Sets the node text.
	 *
	 * @param nodeText - String.
	 */
	void setNodeText(String nodeText);
}
