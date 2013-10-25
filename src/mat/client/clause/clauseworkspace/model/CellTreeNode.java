package mat.client.clause.clauseworkspace.model;

import java.util.List;


/**
 * CellTreeNode Interface.
 */
public interface CellTreeNode {
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
	 * @param child
	 *            - {@link CellTreeNode}
	 * @return {@link CellTreeNode}
	 */
	CellTreeNode appendChild(CellTreeNode child);
	/**
	 * @return {@link CellTreeNode}.
	 */
	CellTreeNode cloneNode();
	/**
	 * Create Child in selected Node with given Name, Label and Node Type.
	 * @param name
	 *            - {@link String}
	 * @param label
	 *            - {@link String}
	 * @param nodeType
	 *            - {@link Short}.
	 * @return - {@link CellTreeNode}.
	 */
	CellTreeNode createChild(String name, String label, short nodeType);
	/**
	 * @return {@link List} of {@link CellTreeNode}.
	 */
	List<CellTreeNode> getChilds();
	/**
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
	 * @return Node Type - {@link Short}.
	 */
	short getNodeType();
	/**
	 * @return Node Parent - {@link CellTreeNode}.
	 */
	CellTreeNode getParent();
	/**
	 * @return {@link String}
	 */
	String getQdmAttribute();
	/**
	 * @return QDM Attribute Count - {@link Integer}.
	 */
	int getQdmAttributeCount();
	/**
	 * @return Node Title - {@link String}.
	 */
	String getTitle();
	/**
	 * @return UUID - {@link String}
	 */
	String getUUID();
	/**
	 * @return Boolean - Valid Node.
	 * */
	boolean getValidNode();
	/**
	 * @return true if Node has child else return false.
	 */
	boolean hasChildren();
	/**
	 * @return true if Node is opened else return false.
	 */
	boolean isOpen();
	/**
	 * Remove child node attached to Parent node.
	 * @param child
	 *            - {@link CellTreeNode}.
	 * @return sub Tree will removed Node - {@link CellTreeNode}.
	 */
	CellTreeNode removeChild(CellTreeNode child);
	/**
	 * @param childs
	 *            - {@link List} of {@link CellTreeNode}.
	 */
	void setChilds(List<CellTreeNode> childs);
	/**
	 * @param key
	 *            - {@link String}
	 * @param value
	 *            - {@link Object}.
	 */
	void setExtraInformation(String key, Object value);
	/**
	 * @param label
	 *            - Node Label - {@link String}.
	 */
	void setLabel(String label);
	/**
	 * @param name
	 *            - Node Name - {@link String}.
	 */
	void setName(String name);
	/**
	 * @param nodeType
	 *            - Node Type - {@link String}.
	 */
	void setNodeType(short nodeType);
	/**
	 * @param isOpen
	 *            - {@link Boolean}.
	 */
	void setOpen(boolean isOpen);
	/**
	 * @param treeNode
	 *            - Parent Node - {@link CellTreeNode}
	 */
	void setParent(CellTreeNode treeNode);
	/**
	 * @param uuid
	 *            - {@link String}
	 */
	void setUUID(String uuid);
	/**
	 * @param isValid
	 *            - {@link Boolean}
	 */
	void setValidNode(boolean isValid);
}
