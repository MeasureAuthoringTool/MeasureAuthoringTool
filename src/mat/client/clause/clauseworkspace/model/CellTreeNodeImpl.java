package mat.client.clause.clauseworkspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.shared.UUIDUtilClient;

public class CellTreeNodeImpl implements CellTreeNode{

	private String name;
	
	private List<CellTreeNode> childs;
	
	private CellTreeNode parent;
	
	private String label;
	
	private boolean isOpen;

	private short nodeType;
	
	Map<String,Object> extraInformationMap = new HashMap<String, Object>();
	
	private String uuid;
	

	@Override
	public List<CellTreeNode> getChilds() {
		return childs;
	}

	
	@Override
	public void setChilds(List<CellTreeNode> childs) {
		this.childs = childs;
	}

	@Override
	public CellTreeNode appendChild(CellTreeNode child) {
		child.setParent(this);
		if(this.getChilds() != null){
			this.getChilds().add(child);
		}else{
			List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
			childs.add(child);
			this.setChilds(childs);
		}
		isOpen = true;	
		return child;
	}

	@Override
	public CellTreeNode removeChild(CellTreeNode child) {
		if(this.getChilds() != null && this.getChilds().size() > 0){
			this.getChilds().remove(child);
		}
		return child;
	}
	
	@Override
	public CellTreeNode createChild(String name, String label, short nodeType){
		CellTreeNode cellTreeNode = new CellTreeNodeImpl();
		cellTreeNode.setName(name);
		cellTreeNode.setLabel(label);
		cellTreeNode.setNodeType(nodeType);
		cellTreeNode.setOpen(true);
		if(nodeType == CLAUSE_NODE){
			cellTreeNode.setUUID(UUIDUtilClient.uuid());	
		}
		
		this.setOpen(true);//open parent
		return appendChild(cellTreeNode);
	}

	@Override
	public short getNodeType() {
		return this.nodeType;
	}
	
	@Override
	public void setNodeType(short nodeType) {
		this.nodeType = nodeType;
	}

	@Override
	public CellTreeNode getParent() {
		return this.parent;
	}

	
	@Override
	public void setParent(CellTreeNode parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean hasChildren() {
		return childs != null && childs.size() > 0;
	}


	@Override
	public CellTreeNode cloneNode() {
		CellTreeNode copyModel = createCopyOfTreeModel(this);
		if(this.getChilds() != null){			
			createCopyChilds(this.getChilds(), copyModel);
		}
		return copyModel;
	}
	


	private void createCopyChilds(List<CellTreeNode> childs,
			CellTreeNode parent) {
		List<CellTreeNode> newChilds = new ArrayList<CellTreeNode>();
		for (CellTreeNode treeNode : childs) {
			CellTreeNode child = createCopyOfTreeModel(treeNode);
			child.setParent(parent);
			newChilds.add(child);
			parent.setChilds(newChilds);
			if(treeNode.getChilds() != null && treeNode.getChilds().size() > 0){
				createCopyChilds(treeNode.getChilds(), child);
			}
		}
	}


	private CellTreeNode createCopyOfTreeModel(CellTreeNode model) {
		CellTreeNode copyModel = new CellTreeNodeImpl();
		copyModel.setLabel(model.getLabel());
		copyModel.setName(model.getName());
		copyModel.setNodeType(model.getNodeType());
		copyModel.setOpen(model.isOpen());	
		Map<String, Object> extraInfos = new HashMap<String, Object>();
		if(model.getNodeType() == ELEMENT_REF_NODE){
			List<CellTreeNode> attributes = (List<CellTreeNode>) extraInformationMap.get("attributes");
			if(attributes != null){
				List<CellTreeNode> extraAttrList = new ArrayList<CellTreeNode>();
				for (CellTreeNode cellTreeNode : attributes) {
					CellTreeNode attrNode = new CellTreeNodeImpl();
					attrNode.setName(cellTreeNode.getName());
					attrNode.setLabel(cellTreeNode.getLabel());
					attrNode.setNodeType(cellTreeNode.getNodeType());
					Map<String, Object> extraInfoAttr = new HashMap<String, Object>();
					extraInfoAttr.putAll(((CellTreeNodeImpl)cellTreeNode).getExtraInformationMap());
					((CellTreeNodeImpl)attrNode).setExtraInformationMap(extraInfoAttr);
					extraAttrList.add(attrNode);
				}
				copyModel.setExtraInformation("attributes", extraAttrList);
			}
		}else{
			extraInfos.putAll(((CellTreeNodeImpl)model).getExtraInformationMap());
			((CellTreeNodeImpl)copyModel).setExtraInformationMap(extraInfos);
		}
		
		
		if(model.getNodeType() == CLAUSE_NODE){
			copyModel.setUUID(UUIDUtilClient.uuid());	
		}else{
			copyModel.setUUID(model.getUUID());
		}
		return copyModel;
	}


	@Override
	public boolean isOpen() {
		return isOpen;
	}


	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}


	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(String label){
		this.label = label;
	}


	@Override
	public void setExtraInformation(String key, Object value) {
		this.extraInformationMap.put(key, value);		
	}


	@Override
	public Object getExtraInformation(String key) {
		return this.extraInformationMap.get(key);
	}


	/**
	 * @return the extraInformationMap
	 */
	public Map<String, Object> getExtraInformationMap() {
		return extraInformationMap;
	}


	/**
	 * @param extraInformationMap the extraInformationMap to set
	 */
	public void setExtraInformationMap(Map<String, Object> extraInformationMap) {
		this.extraInformationMap = extraInformationMap;
	}


	@Override
	public String getUUID() {
		return this.uuid;
	}


	@Override
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}


	@Override
	public String getQdmAttribute() {
		String attrib = "";
		if(getQdmAttributeCount() == 1){
			List<CellTreeNode> attributeList = (List<CellTreeNode>) getExtraInformation("attributes");
			CellTreeNode attributeNode  = attributeList.get(0);
			StringBuilder stringBuilder = new StringBuilder(" (" + attributeNode.getExtraInformation("name").toString());
			String modeName = (String)attributeNode.getExtraInformation("mode");
			if("Value Set".equalsIgnoreCase(modeName)){
					String qdmId = (String)attributeNode.getExtraInformation("qdmUUID");
					String qdmName = ClauseConstants.getElementLookUpName().get(qdmId);
					stringBuilder.append(": '").append(qdmName).append("'");
			}else if(!("Check if Present".equalsIgnoreCase(modeName))){				
				if("Less Than".equalsIgnoreCase(modeName)){
					stringBuilder.append(" < ");
				}else if("Less Than Or Equal To".equalsIgnoreCase(modeName)){
					stringBuilder.append(" <= ");
				}else if ("Greater Than".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" > ");
				}else if ("Greater Than Or Equal To".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" >= ");
				}else if ("Equal To".equalsIgnoreCase(modeName)) {
					stringBuilder.append(" = ");
				} 
				String comparisonValue = (String)attributeNode.getExtraInformation("comparisonValue");
				stringBuilder.append(comparisonValue);

				String unit = (String)attributeNode.getExtraInformation("unit"); 
				if(null != unit){
					stringBuilder.append(" ").append(unit);	
				}
				
			}
			attrib =  stringBuilder.append(")").toString();
		}
		
		return attrib;
	}

	
 //TODO : this is called every time when we make a change to the Celltree, instead we can have a title variable in the CellTreeNodeImpl and set it for each functionality.
	@Override
	public String getTitle() {
		String title = getName();
		String label = getName();
		if(label.length() > ClauseConstants.LABEL_MAX_LENGTH){
			label = label.substring(0,  ClauseConstants.LABEL_MAX_LENGTH - 1).concat("...");
		}
		if(getNodeType() == CellTreeNode.ELEMENT_REF_NODE){//checking if QDM node
			String oid = ClauseConstants.getElementLookUpNode().get(getName() + "~" + getUUID()).getAttributes().getNamedItem("oid").getNodeValue();// getting the OID for the QDM
			int attrCount = getQdmAttributeCount();
			if(attrCount > 1){ //if count greater than 1 just append the count to the label
				title = name + " (" + oid + ")";
				label = label + " (" + attrCount + ")";
			}else if(attrCount == 1){ // if count  equals one check if the length of name plus the length attribute name is greater than the max length allowed, 
												  //if yes add the count as number to name and set the title as attribute name else directly add the attribute name to name 
				String qdmAttr = getQdmAttribute();
				if((qdmAttr.length() + name.length()) > ClauseConstants.LABEL_MAX_LENGTH){
					label = label + " (" + attrCount + ")";
				}else{
					label = getName() + qdmAttr;
				}
				title = name + " (" + oid + ")" + qdmAttr;
			}
		}
		setLabel(label);
		return title;
	}


	@Override
	public int getQdmAttributeCount() {
		if(getNodeType() == ELEMENT_REF_NODE){
			List<CellTreeNode> attributeList = (List<CellTreeNode>) getExtraInformation("attributes");
			if(attributeList != null){
				return attributeList.size();
			}
		}
		return 0;
	}
	

}
