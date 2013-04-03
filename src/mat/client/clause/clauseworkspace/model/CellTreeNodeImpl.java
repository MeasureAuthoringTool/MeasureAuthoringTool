package mat.client.clause.clauseworkspace.model;

import java.util.ArrayList;
import java.util.List;

public class CellTreeNodeImpl implements CellTreeNode{

	private String name;
	
	private List<CellTreeNode> childs;
	
	private CellTreeNode parent;
	
	private String label;
	
	private boolean isOpen;
	
	private boolean isEditable = true;// this should not be used once we have the complete celltree functionality
	
	private boolean isRemovable = true;
	
	private short nodeType;


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
		cellTreeNode.setNodeType(CellTreeNode.CLAUSE_NODE);
		cellTreeNode.setOpen(true);
		cellTreeNode.setRemovable(true);
		cellTreeNode.setEditable(true);
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
		copyModel.setEditable(model.isEditable());
		copyModel.setLabel(model.getLabel());
		copyModel.setName(model.getName());
		copyModel.setRemovable(model.isRemovable());
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
	public boolean isRemovable() {
		return this.isRemovable;
	}


	@Override
	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
		
	}


	@Override
	public boolean isEditable() {
		return this.isEditable;
	}


	@Override
	public boolean setEditable(boolean isEditable) {
		return this.isEditable = isEditable;
	}
	
}
