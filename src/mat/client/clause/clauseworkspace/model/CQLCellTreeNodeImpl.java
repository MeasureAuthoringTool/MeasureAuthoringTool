package mat.client.clause.clauseworkspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.shared.UUIDUtilClient;

/**
 * CQLCellTreeNodeImpl.java.
 */
public class CQLCellTreeNodeImpl implements CQLCellTreeNode {
	
	/** The childs. {@link List} of child {@link CQLCellTreeNode}. */
	private List<CQLCellTreeNode> childs;
	
	/** The extra information map. {@link HashMap} of Node's extra information. */
	private Map<String, Object> extraInformationMap = new HashMap<String, Object>();
	/**
	 * Node open/close status.
	 */
	private boolean isOpen;
	/**
	 * Valid node.
	 */
	private boolean isValid = true;
	/**
	 * Node Label.
	 */
	private String label;
	/**
	 * Node Name.
	 */
	private String name;
	/**
	 * Node type.
	 */
	private short nodeType;
	/**
	 * Node Parent.
	 */
	private CQLCellTreeNode parent;
	/**
	 * Node UUID information.
	 */
	private String uuid;
	
	private String nodeText;
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#appendChild(mat.
	 * client.clause.clauseworkspace.model.CQLCellTreeNode)
	 */
	@Override
	public final CQLCellTreeNode appendChild(CQLCellTreeNode child) {
		child.setParent(this);
		if (this.getChilds() != null) {
			this.getChilds().add(child);
		} else {
			List<CQLCellTreeNode> childNodes = new ArrayList<CQLCellTreeNode>();
			childNodes.add(child);
			this.setChilds(childNodes);
		}
		isOpen = true;
		return child;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#cloneNode()
	 */
	@Override
	public final CQLCellTreeNode cloneNode() {
		CQLCellTreeNode copyModel = createCopyOfTreeModel(this);
		if (this.getChilds() != null) {
			createCopyChilds(this.getChilds(), copyModel);
		}
		return copyModel;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#createChild(java
	 * .lang.String, java.lang.String, short)
	 */
	@Override
	public final CQLCellTreeNode createChild(String nodeName, String nodeLabel, short nodesType) {
		CQLCellTreeNode CQLCellTreeNode = new CQLCellTreeNodeImpl();
		CQLCellTreeNode.setName(nodeName);
		CQLCellTreeNode.setLabel(nodeLabel);
		CQLCellTreeNode.setNodeType(nodesType);
		CQLCellTreeNode.setOpen(true);
		if (CQLCellTreeNode.getNodeType() == CLAUSE_NODE) {
			CQLCellTreeNode.setUUID(UUIDUtilClient.uuid());
		}
		this.setOpen(true); // open parent
		return appendChild(CQLCellTreeNode);
	}
	
	/**
	 * Creates the copy childs.
	 * 
	 * @param childNodes
	 *            - {@link List} of {@link CQLCellTreeNode}.
	 * @param parentNode
	 *            - {@link CQLCellTreeNode}.
	 */
	private void createCopyChilds(List<CQLCellTreeNode> childNodes, CQLCellTreeNode parentNode) {
		List<CQLCellTreeNode> newChilds = new ArrayList<CQLCellTreeNode>();
		for (CQLCellTreeNode treeNode : childNodes) {
			CQLCellTreeNode child = createCopyOfTreeModel(treeNode);
			child.setParent(parentNode);
			newChilds.add(child);
			parentNode.setChilds(newChilds);
			if ((treeNode.getChilds() != null) && (treeNode.getChilds().size() > 0)) {
				createCopyChilds(treeNode.getChilds(), child);
			}
		}
	}
	
	/**
	 * Creates the copy of tree model.
	 * 
	 * @param model
	 *            - {@link CQLCellTreeNode}.
	 * @return {@link CQLCellTreeNode}.
	 */
	private CQLCellTreeNode createCopyOfTreeModel(CQLCellTreeNode model) {
		CQLCellTreeNode copyModel = new CQLCellTreeNodeImpl();
		copyModel.setLabel(model.getLabel());
		copyModel.setName(model.getName());
		copyModel.setNodeType(model.getNodeType());
		copyModel.setOpen(model.isOpen());
		Map<String, Object> extraInfos = new HashMap<String, Object>();
		if (model.getNodeType() == ELEMENT_REF_NODE) {
			// MAT-2282 : Bug fix for issue
			// "Copy/Paste in Clause Workspace Dropping Attributes".
			// List<CQLCellTreeNode> attributes = (List<CQLCellTreeNode>)
			// extraInformationMap.get("attributes");
			@SuppressWarnings("unchecked")
			List<CQLCellTreeNode> attributes = (List<CQLCellTreeNode>) model
			.getExtraInformation("attributes");
			// MAT-2282 : Bug fix ends.
			if (attributes != null) {
				List<CQLCellTreeNode> extraAttrList = new ArrayList<CQLCellTreeNode>();
				for (CQLCellTreeNode CQLCellTreeNode : attributes) {
					CQLCellTreeNode attrNode = new CQLCellTreeNodeImpl();
					attrNode.setName(CQLCellTreeNode.getName());
					attrNode.setLabel(CQLCellTreeNode.getLabel());
					attrNode.setNodeType(CQLCellTreeNode.getNodeType());
					Map<String, Object> extraInfoAttr = new HashMap<String, Object>();
					extraInfoAttr.putAll(((CQLCellTreeNodeImpl) CQLCellTreeNode)
							.getExtraInformationMap());
					((CQLCellTreeNodeImpl) attrNode)
					.setExtraInformationMap(extraInfoAttr);
					extraAttrList.add(attrNode);
				}
				copyModel.setExtraInformation("attributes", extraAttrList);
			}
		} else if ((model.getNodeType() == LOGICAL_OP_NODE)
				|| (model.getNodeType() == SUBTREE_REF_NODE)) {
			@SuppressWarnings("unchecked")
			List<CQLCellTreeNode> attributes = (List<CQLCellTreeNode>) model
			.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
			// MAT-2282 : Bug fix ends.
			if (attributes != null) {
				List<CQLCellTreeNode> extraAttrList = new ArrayList<CQLCellTreeNode>();
				for (CQLCellTreeNode CQLCellTreeNode : attributes) {
					CQLCellTreeNode attrNode = new CQLCellTreeNodeImpl();
					attrNode.setName(CQLCellTreeNode.getName());
					attrNode.setNodeType(CQLCellTreeNode.getNodeType());
					attrNode.setNodeText(CQLCellTreeNode.getNodeText());
					Map<String, Object> extraInfoAttr = new HashMap<String, Object>();
					extraInfoAttr.putAll(((CQLCellTreeNodeImpl) CQLCellTreeNode)
							.getExtraInformationMap());
					((CQLCellTreeNodeImpl) attrNode)
					.setExtraInformationMap(extraInfoAttr);
					extraAttrList.add(attrNode);
				}
				copyModel.setExtraInformation(PopulationWorkSpaceConstants.COMMENTS, extraAttrList);
			}
		} else {
			extraInfos.putAll(((CQLCellTreeNodeImpl) model)
					.getExtraInformationMap());
			((CQLCellTreeNodeImpl) copyModel).setExtraInformationMap(extraInfos);
		}
		if (model.getNodeType() == CLAUSE_NODE) {
			copyModel.setUUID(UUIDUtilClient.uuid());
		} else {
			copyModel.setUUID(model.getUUID());
		}
		return copyModel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getChilds()
	 */
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getChilds()
	 */
	@Override
	public List<CQLCellTreeNode> getChilds() {
		return childs;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getExtraInformation
	 * (java.lang.String)
	 */
	@Override
	public Object getExtraInformation(String key) {
		return extraInformationMap.get(key);
	}
	
	/**
	 * Gets the extra information map.
	 * 
	 * @return the extraInformationMap
	 */
	public Map<String, Object> getExtraInformationMap() {
		return extraInformationMap;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return nodeType;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getParent()
	 */
	@Override
	public CQLCellTreeNode getParent() {
		return parent;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getQdmAttribute()
	 */
	@Override
	public String getQdmAttribute() {
		String attrib = "";
		if (getQdmAttributeCount() == 1) {
			List<CQLCellTreeNode> attributeList = (List<CQLCellTreeNode>) getExtraInformation("attributes");
			CQLCellTreeNode attributeNode = attributeList.get(0);
			StringBuilder stringBuilder = new StringBuilder(" ("
					+ attributeNode.getExtraInformation("name").toString());
			String modeName = (String) attributeNode
					.getExtraInformation("mode");
			if ("Value Set".equalsIgnoreCase(modeName)) {
				String qdmId = (String) attributeNode
						.getExtraInformation("qdmUUID");
				String qdmName = PopulationWorkSpaceConstants.getElementLookUpName().get(
						qdmId);
				stringBuilder.append(": '").append(qdmName).append("'");
			} else if (!("Check if Present".equalsIgnoreCase(modeName))) {
				if ("Less Than".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" < ");
				} else if ("Less Than Or Equal To".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" <= ");
				} else if ("Greater Than".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" > ");
				} else if ("Greater Than Or Equal To"
						.equalsIgnoreCase(modeName)) {
					stringBuilder.append(" >= ");
				} else if ("Equal To".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" = ");
				}
				String attrName = attributeNode.getExtraInformation("name").toString();
				if(attrName.contains("date")){
					String attributeDate = (String) attributeNode
							.getExtraInformation("attrDate");
					stringBuilder.append(attributeDate);
				} else {
				String comparisonValue = (String) attributeNode
						.getExtraInformation("comparisonValue");
				stringBuilder.append(comparisonValue);
				String unit = (String) attributeNode
						.getExtraInformation("unit");
				if (null != unit) {
					stringBuilder.append(" ").append(unit);
				}
				}
			}
			attrib = stringBuilder.append(")").toString();
		}
		return attrib;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getQdmAttributeCount
	 * ()
	 */
	@Override
	public int getQdmAttributeCount() {
		if (getNodeType() == ELEMENT_REF_NODE) {
			List<CQLCellTreeNode> attributeList = (List<CQLCellTreeNode>) getExtraInformation("attributes");
			if (attributeList != null) {
				return attributeList.size();
			}
		}
		return 0;
	}
	// TODO : this is called every time when we make a change to the Celltree,
	// instead we can have a title variable in the CQLCellTreeNodeImpl and set it
	// for each functionality.
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getTitle()
	 */
	@Override
	public String getTitle() {
		String title = getName();
		String nodeLabel = getName();
		if (nodeLabel.length() > PopulationWorkSpaceConstants.LABEL_MAX_LENGTH) {
			nodeLabel = nodeLabel.substring(0, PopulationWorkSpaceConstants.LABEL_MAX_LENGTH - 1)
					.concat("...");
		}
		if (getNodeType() == CQLCellTreeNode.ELEMENT_REF_NODE) { // checking if QDM
			// node
			String oid = "";
			if (PopulationWorkSpaceConstants.getElementLookUpNode().get(
					getName() + "~" + getUUID()) != null) {
				oid = PopulationWorkSpaceConstants.getElementLookUpNode()
						.get(getName() + "~" + getUUID()).getAttributes()
						.getNamedItem("oid").getNodeValue(); // getting the OID
				// for the QDM
			}
			int attrCount = getQdmAttributeCount();
			if (attrCount > 1) { // if count greater than 1 just append the
				// count to the label
				title = name + " (" + oid + ")";
				nodeLabel = nodeLabel + " (" + attrCount + ")";
			} else if (attrCount == 1) { // if count equals one check if the
				// length of name plus the length
				// attribute name is greater than
				// the max length allowed,
				// if yes add the count as number to
				// name and set the title as
				// attribute name else directly add
				// the attribute name to name
				String qdmAttr = getQdmAttribute();
				if ((qdmAttr.length() + name.length()) > PopulationWorkSpaceConstants.LABEL_MAX_LENGTH) {
					nodeLabel = nodeLabel + " (" + attrCount + ")";
				} else {
					nodeLabel = getName() + qdmAttr;
				}
				title = name + " (" + oid + ")" + qdmAttr;
			}
		}
		setLabel(nodeLabel);
		return title;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getUUID()
	 */
	@Override
	public String getUUID() {
		return uuid;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#getValidNode()
	 */
	@Override
	public boolean getValidNode() {
		return isValid;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return (childs != null) && (childs.size() > 0);
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CQLCellTreeNode#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#removeChild(mat.
	 * client.clause.clauseworkspace.model.CQLCellTreeNode)
	 */
	@Override
	public CQLCellTreeNode removeChild(CQLCellTreeNode child) {
		if ((this.getChilds() != null) && (this.getChilds().size() > 0)) {
			this.getChilds().remove(child);
		}
		return child;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setChilds(java.util
	 * .List)
	 */
	@Override
	public void setChilds(List<CQLCellTreeNode> childNodes) {
		childs = childNodes;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setExtraInformation
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void setExtraInformation(String key, Object value) {
		extraInformationMap.put(key, value);
	}
	
	/**
	 * Sets the extra information map.
	 * 
	 * @param extraInformationMap
	 *            the extraInformationMap to set
	 */
	public void setExtraInformationMap(Map<String, Object> extraInformationMap) {
		this.extraInformationMap = extraInformationMap;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setLabel(java.lang
	 * .String)
	 */
	@Override
	public void setLabel(String nodeLabel) {
		label = nodeLabel;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(String nodeName) {
		name = nodeName;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setNodeType(short)
	 */
	@Override
	public void setNodeType(short type) {
		nodeType = type;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setOpen(boolean)
	 */
	@Override
	public void setOpen(boolean isNodeOpen) {
		isOpen = isNodeOpen;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setParent(mat.client
	 * .clause.clauseworkspace.model.CQLCellTreeNode)
	 */
	@Override
	public void setParent(CQLCellTreeNode parentNode) {
		parent = parentNode;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setUUID(java.lang
	 * .String)
	 */
	@Override
	public void setUUID(String nodeUUID) {
		uuid = nodeUUID;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CQLCellTreeNode#setValidNode(boolean
	 * )
	 */
	@Override
	public void setValidNode(boolean isNodeValid) {
		isValid = isNodeValid;
	}
	/**
	 * @return the nodeText
	 */
	@Override
	public String getNodeText() {
		return nodeText;
	}
	/**
	 * @param nodeText the nodeText to set
	 */
	@Override
	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}
}
