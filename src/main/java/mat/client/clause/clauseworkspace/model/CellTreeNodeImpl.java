package mat.client.clause.clauseworkspace.model;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.shared.UUIDUtilClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CellTreeNodeImpl.java.
 */
public class CellTreeNodeImpl implements CellTreeNode {
	
	/** The childs. {@link List} of child {@link CellTreeNode}. */
	private List<CellTreeNode> childs;
	
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
	private CellTreeNode parent;
	/**
	 * Node UUID information.
	 */
	private String uuid;
	
	private String nodeText;
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#appendChild(mat.
	 * client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public final CellTreeNode appendChild(CellTreeNode child) {
		child.setParent(this);
		if (this.getChilds() != null) {
			this.getChilds().add(child);
		} else {
			List<CellTreeNode> childNodes = new ArrayList<CellTreeNode>();
			childNodes.add(child);
			this.setChilds(childNodes);
		}
		isOpen = true;
		return child;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#cloneNode()
	 */
	@Override
	public final CellTreeNode cloneNode() {
		CellTreeNode copyModel = createCopyOfTreeModel(this);
		if (this.getChilds() != null) {
			createCopyChilds(this.getChilds(), copyModel);
		}
		return copyModel;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#createChild(java
	 * .lang.String, java.lang.String, short)
	 */
	@Override
	public final CellTreeNode createChild(String nodeName, String nodeLabel, short nodesType) {
		CellTreeNode cellTreeNode = new CellTreeNodeImpl();
		cellTreeNode.setName(nodeName);
		cellTreeNode.setLabel(nodeLabel);
		cellTreeNode.setNodeType(nodesType);
		cellTreeNode.setOpen(true);
		if (cellTreeNode.getNodeType() == CLAUSE_NODE) {
			cellTreeNode.setUUID(UUIDUtilClient.uuid());
		}
		this.setOpen(true); // open parent
		return appendChild(cellTreeNode);
	}
	
	/**
	 * Creates the copy childs.
	 * 
	 * @param childNodes
	 *            - {@link List} of {@link CellTreeNode}.
	 * @param parentNode
	 *            - {@link CellTreeNode}.
	 */
	private void createCopyChilds(List<CellTreeNode> childNodes, CellTreeNode parentNode) {
		List<CellTreeNode> newChilds = new ArrayList<CellTreeNode>();
		for (CellTreeNode treeNode : childNodes) {
			CellTreeNode child = createCopyOfTreeModel(treeNode);
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
	 *            - {@link CellTreeNode}.
	 * @return {@link CellTreeNode}.
	 */
	private CellTreeNode createCopyOfTreeModel(CellTreeNode model) {
		CellTreeNode copyModel = new CellTreeNodeImpl();
		copyModel.setLabel(model.getLabel());
		copyModel.setName(model.getName());
		copyModel.setNodeType(model.getNodeType());
		copyModel.setOpen(model.isOpen());
		Map<String, Object> extraInfos = new HashMap<String, Object>();
		if (model.getNodeType() == ELEMENT_REF_NODE) {
			// MAT-2282 : Bug fix for issue
			// "Copy/Paste in Clause Workspace Dropping Attributes".
			// List<CellTreeNode> attributes = (List<CellTreeNode>)
			// extraInformationMap.get("attributes");
			@SuppressWarnings("unchecked")
			List<CellTreeNode> attributes = (List<CellTreeNode>) model
			.getExtraInformation("attributes");
			// MAT-2282 : Bug fix ends.
			if (attributes != null) {
				List<CellTreeNode> extraAttrList = new ArrayList<CellTreeNode>();
				for (CellTreeNode cellTreeNode : attributes) {
					CellTreeNode attrNode = new CellTreeNodeImpl();
					attrNode.setName(cellTreeNode.getName());
					attrNode.setLabel(cellTreeNode.getLabel());
					attrNode.setNodeType(cellTreeNode.getNodeType());
					Map<String, Object> extraInfoAttr = new HashMap<String, Object>();
					extraInfoAttr.putAll(((CellTreeNodeImpl) cellTreeNode)
							.getExtraInformationMap());
					((CellTreeNodeImpl) attrNode)
					.setExtraInformationMap(extraInfoAttr);
					extraAttrList.add(attrNode);
				}
				copyModel.setExtraInformation("attributes", extraAttrList);
			}
		} else if ((model.getNodeType() == LOGICAL_OP_NODE)
				|| (model.getNodeType() == SUBTREE_REF_NODE)) {
			@SuppressWarnings("unchecked")
			List<CellTreeNode> attributes = (List<CellTreeNode>) model
			.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
			// MAT-2282 : Bug fix ends.
			if (attributes != null) {
				List<CellTreeNode> extraAttrList = new ArrayList<CellTreeNode>();
				for (CellTreeNode cellTreeNode : attributes) {
					CellTreeNode attrNode = new CellTreeNodeImpl();
					attrNode.setName(cellTreeNode.getName());
					attrNode.setNodeType(cellTreeNode.getNodeType());
					attrNode.setNodeText(cellTreeNode.getNodeText());
					Map<String, Object> extraInfoAttr = new HashMap<String, Object>();
					extraInfoAttr.putAll(((CellTreeNodeImpl) cellTreeNode)
							.getExtraInformationMap());
					((CellTreeNodeImpl) attrNode)
					.setExtraInformationMap(extraInfoAttr);
					extraAttrList.add(attrNode);
				}
				copyModel.setExtraInformation(PopulationWorkSpaceConstants.COMMENTS, extraAttrList);
			}
		} else {
			extraInfos.putAll(((CellTreeNodeImpl) model)
					.getExtraInformationMap());
			((CellTreeNodeImpl) copyModel).setExtraInformationMap(extraInfos);
		}
		if (model.getNodeType() == CLAUSE_NODE) {
			copyModel.setUUID(UUIDUtilClient.uuid());
		} else {
			copyModel.setUUID(model.getUUID());
		}
		return copyModel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getChilds()
	 */
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getChilds()
	 */
	@Override
	public List<CellTreeNode> getChilds() {
		return childs;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#getExtraInformation
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
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return nodeType;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getParent()
	 */
	@Override
	public CellTreeNode getParent() {
		return parent;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#getQdmAttribute()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getQdmAttribute() {
		String attrib = "";
		if (getQdmAttributeCount() == 1) {
			List<CellTreeNode> attributeList = (List<CellTreeNode>) getExtraInformation("attributes");
			CellTreeNode attributeNode = attributeList.get(0);
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

	@SuppressWarnings("unchecked")
	@Override
	public int getQdmAttributeCount() {
		if (getNodeType() == ELEMENT_REF_NODE) {
			List<CellTreeNode> attributeList = (List<CellTreeNode>) getExtraInformation("attributes");
			if (attributeList != null) {
				return attributeList.size();
			}
		}
		return 0;
	}

	@Override
	public String getTitle() {
		String title = getName();
		String nodeLabel = getName();
		if (nodeLabel.length() > PopulationWorkSpaceConstants.LABEL_MAX_LENGTH) {
			nodeLabel = nodeLabel.substring(0, PopulationWorkSpaceConstants.LABEL_MAX_LENGTH - 1)
					.concat("...");
		}
		if (getNodeType() == CellTreeNode.ELEMENT_REF_NODE) {
			String oid = "";
			if (PopulationWorkSpaceConstants.getElementLookUpNode().get(
					getName() + "~" + getUUID()) != null) {
				oid = PopulationWorkSpaceConstants.getElementLookUpNode()
						.get(getName() + "~" + getUUID()).getAttributes()
						.getNamedItem("oid").getNodeValue(); // getting the OID for the QDM
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
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getUUID()
	 */
	@Override
	public String getUUID() {
		return uuid;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#getValidNode()
	 */
	@Override
	public boolean getValidNode() {
		return isValid;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return (childs != null) && (childs.size() > 0);
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.model.CellTreeNode#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#removeChild(mat.
	 * client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public CellTreeNode removeChild(CellTreeNode child) {
		if ((this.getChilds() != null) && (this.getChilds().size() > 0)) {
			this.getChilds().remove(child);
		}
		return child;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setChilds(java.util
	 * .List)
	 */
	@Override
	public void setChilds(List<CellTreeNode> childNodes) {
		childs = childNodes;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setExtraInformation
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
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setLabel(java.lang
	 * .String)
	 */
	@Override
	public void setLabel(String nodeLabel) {
		label = nodeLabel;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setName(java.lang
	 * .String)
	 */
	@Override
	public void setName(String nodeName) {
		name = nodeName;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setNodeType(short)
	 */
	@Override
	public void setNodeType(short type) {
		nodeType = type;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setOpen(boolean)
	 */
	@Override
	public void setOpen(boolean isNodeOpen) {
		isOpen = isNodeOpen;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setParent(mat.client
	 * .clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void setParent(CellTreeNode parentNode) {
		parent = parentNode;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setUUID(java.lang
	 * .String)
	 */
	@Override
	public void setUUID(String nodeUUID) {
		uuid = nodeUUID;
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * mat.client.clause.clauseworkspace.model.CellTreeNode#setValidNode(boolean
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
