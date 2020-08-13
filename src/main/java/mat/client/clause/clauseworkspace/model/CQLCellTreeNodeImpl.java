package mat.client.clause.clauseworkspace.model;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.shared.UUIDUtilClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CQLCellTreeNodeImpl implements CQLCellTreeNode {
	
	private List<CQLCellTreeNode> childs;
	
	private Map<String, Object> extraInformationMap = new HashMap<String, Object>();

	private boolean isOpen;

	private boolean isValid = true;

	private String label;
	
	private String name;

	private short nodeType;
	
	private CQLCellTreeNode parent;
	
	private String uuid;
	
	private String nodeText;

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
	
	@Override
	public final CQLCellTreeNode cloneNode() {
		CQLCellTreeNode copyModel = createCopyOfTreeModel(this);
		if (this.getChilds() != null) {
			createCopyChilds(this.getChilds(), copyModel);
		}
		return copyModel;
	}
	
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
	
	private CQLCellTreeNode createCopyOfTreeModel(CQLCellTreeNode model) {
		CQLCellTreeNode copyModel = new CQLCellTreeNodeImpl();
		copyModel.setLabel(model.getLabel());
		copyModel.setName(model.getName());
		copyModel.setNodeType(model.getNodeType());
		copyModel.setOpen(model.isOpen());
		Map<String, Object> extraInfos = new HashMap<String, Object>();
		if (model.getNodeType() == ELEMENT_REF_NODE) {
			List<CQLCellTreeNode> attributes = (List<CQLCellTreeNode>) model
			.getExtraInformation("attributes");
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
			List<CQLCellTreeNode> attributes = (List<CQLCellTreeNode>) model
			.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
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

	@Override
	public List<CQLCellTreeNode> getChilds() {
		return childs;
	}

	@Override
	public Object getExtraInformation(String key) {
		return extraInformationMap.get(key);
	}
	
	public Map<String, Object> getExtraInformationMap() {
		return extraInformationMap;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public short getNodeType() {
		return nodeType;
	}
	
	@Override
	public CQLCellTreeNode getParent() {
		return parent;
	}
	
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

	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public boolean getValidNode() {
		return isValid;
	}

	@Override
	public boolean hasChildren() {
		return (childs != null) && (childs.size() > 0);
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public CQLCellTreeNode removeChild(CQLCellTreeNode child) {
		if ((this.getChilds() != null) && (this.getChilds().size() > 0)) {
			this.getChilds().remove(child);
		}
		return child;
	}

	@Override
	public void setChilds(List<CQLCellTreeNode> childNodes) {
		childs = childNodes;
	}

	@Override
	public void setExtraInformation(String key, Object value) {
		extraInformationMap.put(key, value);
	}

	public void setExtraInformationMap(Map<String, Object> extraInformationMap) {
		this.extraInformationMap = extraInformationMap;
	}
	
	public void setLabel(String nodeLabel) {
		label = nodeLabel;
	}

	@Override
	public void setName(String nodeName) {
		name = nodeName;
	}

	@Override
	public void setNodeType(short type) {
		nodeType = type;
	}
	
	@Override
	public void setOpen(boolean isNodeOpen) {
		isOpen = isNodeOpen;
	}

	@Override
	public void setParent(CQLCellTreeNode parentNode) {
		parent = parentNode;
	}
	
	@Override
	public void setUUID(String nodeUUID) {
		uuid = nodeUUID;
	}
	
	@Override
	public void setValidNode(boolean isNodeValid) {
		isValid = isNodeValid;
	}
	
	@Override
	public String getNodeText() {
		return nodeText;
	}

	@Override
	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}
}
