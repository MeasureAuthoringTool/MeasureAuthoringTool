package mat.client.clause.clauseworkspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.util.UUIDUtilClient;

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
		extraInfos.putAll(((CellTreeNodeImpl)model).getExtraInformationMap());
		((CellTreeNodeImpl)copyModel).setExtraInformationMap(extraInfos);
		if(model.getNodeType() == CLAUSE_NODE){
			copyModel.setUUID(UUIDUtilClient.uuid());	
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

}
